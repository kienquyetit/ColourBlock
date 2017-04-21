package com.gamedev.colourblock.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gamedev.colourblock.entities.HUD;
import com.gamedev.colourblock.entities.Heart;
import com.gamedev.colourblock.entities.Player;
import com.gamedev.colourblock.entities.Spike;
import com.gamedev.colourblock.handler.B2DVars;
import com.gamedev.colourblock.handler.Background;
import com.gamedev.colourblock.handler.BoundedCamera;
import com.gamedev.colourblock.handler.CBContactListener;
import com.gamedev.colourblock.handler.CBInput;
import com.gamedev.colourblock.handler.GameStateManager;
import com.gamedev.colourblock.handler.LevelStatus;
import com.gamedev.colourblock.main.Game;

import static com.gamedev.colourblock.handler.B2DVars.PPM;

/**
 * Created by Quyet on 3/29/2017.
 */

public class Play extends GameState {

    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private CBContactListener cl;
    private BoundedCamera b2dCam;

    private Player player;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private Array<Heart> crystals;
    private Array<Spike> spikes;

    private Background[] backgrounds;
    private HUD hud;

    public static int level;

    public Play(GameStateManager gsm) {

        super(gsm);

        // set up the box2d world and contact listener
        world = new World(new Vector2(0, -7f), true);
        cl = new CBContactListener();
        world.setContactListener(cl);
        b2dRenderer = new Box2DDebugRenderer();

        // create player
        createPlayer();

        // create walls
        createWalls();
        cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        // create crystals
        createHeart();
        player.setTotalHearts(crystals.size);

        // create spikes
        createSpikes();

        // create backgrounds
        Texture bgs = Game.res.getTexture("bgs");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
        backgrounds = new Background[3];
        backgrounds[0] = new Background(sky, cam, 0f);
        backgrounds[1] = new Background(clouds, cam, 0.1f);
        backgrounds[2] = new Background(mountains, cam, 0.2f);

        // create hud
        hud = new HUD(player);

        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);

    }

    /**
     * Creates the player.
     * Sets up the box2d body and sprites.
     */
    private void createPlayer() {

        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        bdef.linearVelocity.set(1f, 0f);

        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / PPM, 8 / PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1;
        fdef.friction = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED_BLOCK | B2DVars.BIT_HEART | B2DVars.BIT_SPIKE;

        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create box shape for player foot
        shape = new PolygonShape();
        shape.setAsBox(8 / PPM, 8 / PPM, new Vector2(0, -8 / PPM), 0);

        // create fixturedef for player foot
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED_BLOCK;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        // create new player
        player = new Player(body);
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        // i need a ratio of 0.005
        // so at 1kg, i need 200 N jump force

    }

    /**
     * Sets up the tile map collidable tiles.
     * Reads in tile map layers and sets up box2d bodies.
     */
    private void createWalls() {

        // load tile map and map renderer
        try {
            tileMap = new TmxMapLoader().load("maps/level" + level + ".tmx");
        } catch (Exception e) {
            System.out.println("Cannot find file: maps/level" + level + ".tmx");
            Gdx.app.exit();
        }
        tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
        tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
        tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap);

        // read each of the "red" "green" and "blue" layers
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
        createBlocks(layer, B2DVars.BIT_RED_BLOCK);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
        createBlocks(layer, B2DVars.BIT_GREEN_BLOCK);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
        createBlocks(layer, B2DVars.BIT_BLUE_BLOCK);

    }

    /**
     * Creates box2d bodies for all non-null tiles
     * in the specified layer and assigns the specified
     * category bits.
     *
     * @param layer the layer being read
     * @param bits  category bits assigned to fixtures
     */
    private void createBlocks(TiledMapTileLayer layer, short bits) {

        // tile size
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = B2DVars.BIT_PLAYER;
                world.createBody(bdef).createFixture(fd);
                cs.dispose();

            }
        }

    }

    /**
     * Set up box2d bodies for hearts in tile map "heart" layer
     */
    private void createHeart() {

        // create list of crystals
        crystals = new Array<Heart>();

        // get all crystals in "crystals" layer,
        // create bodies for each, and add them
        // to the crystals list
        MapLayer ml = tileMap.getLayers().get("hearts");
        if (ml == null) return;

        for (MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = Float.parseFloat(mo.getProperties().get("x").toString()) / PPM;
            float y = Float.parseFloat(mo.getProperties().get("y").toString()) / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_HEART;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("heart");
            Heart c = new Heart(body);
            body.setUserData(c);
            crystals.add(c);
            cshape.dispose();
        }
    }

    /**
     * Set up box2d bodies for spikes in "spikes" layer
     */
    private void createSpikes() {

        spikes = new Array<Spike>();

        MapLayer ml = tileMap.getLayers().get("spikes");
        if (ml == null) return;

        for (MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = Float.parseFloat(mo.getProperties().get("x").toString()) / PPM;
            float y = Float.parseFloat(mo.getProperties().get("y").toString()) / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(5 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_SPIKE;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("spike");
            Spike s = new Spike(body);
            body.setUserData(s);
            spikes.add(s);
            cshape.dispose();
        }
    }

    /**
     * Apply upward force to player body.
     */
    private void playerJump() {
        if (cl.playerCanJump()) {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyForceToCenter(0, 200, true);
            Game.res.getSound("jump").play();
        }
    }

    /**
     * Switch player mask bits to next block.
     */
    private void switchBlocks() {

        // get player foot mask bits
        Filter filter = player.getBody().getFixtureList().get(1).getFilterData();
        short bits = filter.maskBits;

        // switch to next block bit
        // red -> green -> blue
        if (bits == B2DVars.BIT_RED_BLOCK) {
            bits = B2DVars.BIT_GREEN_BLOCK;
        } else if (bits == B2DVars.BIT_GREEN_BLOCK) {
            bits = B2DVars.BIT_BLUE_BLOCK;
        } else if (bits == B2DVars.BIT_BLUE_BLOCK) {
            bits = B2DVars.BIT_RED_BLOCK;
        }

        // set player foot mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);

        // set player mask bits
        bits |= B2DVars.BIT_HEART | B2DVars.BIT_SPIKE;
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(0).setFilterData(filter);

        // play sound
        Game.res.getSound("changeblock").play();

        // draw player
        if ((bits & B2DVars.BIT_RED_BLOCK) != 0) {
            player.updatePlayer("red_face");
        } else if ((bits & B2DVars.BIT_GREEN_BLOCK) != 0) {
            player.updatePlayer("green_face");
        } else if ((bits & B2DVars.BIT_BLUE_BLOCK) != 0) {
            player.updatePlayer("blue_face");
        }
    }

    public void handleInput() {

        // keyboard input
        if (CBInput.isPressed(CBInput.BUTTON1)) {
            playerJump();
        }
        if (CBInput.isPressed(CBInput.BUTTON2)) {
            switchBlocks();
        }

        // mouse/touch input for android
        // left side of screen to switch blocks
        // right side of screen to jump
        if (CBInput.isPressed()) {
            if (CBInput.x < Gdx.graphics.getWidth() / 2) {
                switchBlocks();
            } else {
                playerJump();
            }
        }

    }

    public void update(float dt) {

        // check input
        handleInput();

        // update box2d world, move player
        world.step(Game.STEP, 1, 1);

        // check for collected crystals
        Array<Body> bodies = cl.getBodies();
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((Heart) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
            player.collectCrystal();
            Game.res.getSound("heart").play();
        }
        bodies.clear();

        // update player
        player.update(dt);

        // check player win
        if (player.getBody().getPosition().x * PPM > tileMapWidth * tileSize) {
            float star = (( player.getNumHearts() / (float)player.getTotalHeart()) * 10) / 3;
            LevelStatus.put(LevelStatus.levelclear + 1, level, (int)star);
            Game.res.getSound("levelselect").play();
            gsm.setState(GameStateManager.LEVEL_SELECT);
        }

        // check player failed
        if (player.getBody().getPosition().y < 0) {
            Game.res.getSound("hit").play();
            gsm.setState(GameStateManager.MENU);
        }
        if (player.getBody().getLinearVelocity().x < 0.001f) {
            Game.res.getSound("hit").play();
            gsm.setState(GameStateManager.MENU);
        }
        if (cl.isPlayerDead()) {
            Game.res.getSound("hit").play();
            gsm.setState(GameStateManager.MENU);
        }

        // update crystals
        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }

        // update spikes
        for (int i = 0; i < spikes.size; i++) {
            spikes.get(i).update(dt);
        }

    }

    public void render() {

        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
        cam.update();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (Background background : backgrounds) {
            background.render(sb);
        }

        // draw tilemap
        tmRenderer.setView(cam);
        tmRenderer.render();

        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        // draw crystals
        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);
        }

        // draw spikes
        for (int i = 0; i < spikes.size; i++) {
            spikes.get(i).render(sb);
        }

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        // debug draw box2d
        if (debug) {
            b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM, Game.V_HEIGHT / 2 / PPM);
            b2dCam.update();
            b2dRenderer.render(world, b2dCam.combined);
        }

    }

    public void dispose() {

    }

}