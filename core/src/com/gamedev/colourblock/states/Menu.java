package com.gamedev.colourblock.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gamedev.colourblock.entities.B2DSprite;
import com.gamedev.colourblock.handler.Animation;
import com.gamedev.colourblock.handler.B2DVars;
import com.gamedev.colourblock.handler.Background;
import com.gamedev.colourblock.handler.GameButton;
import com.gamedev.colourblock.handler.GameStateManager;
import com.gamedev.colourblock.handler.VolumnStatus;
import com.gamedev.colourblock.main.Game;

import static com.gamedev.colourblock.handler.B2DVars.PPM;

/**
 * Created by Quyet on 3/29/2017.
 */

public class Menu extends GameState {

    private boolean debug = false;

    private Background bg;
    private Animation animation;
    private GameButton playButton, tutorialButton, volumnButton, exitButton;

    private World world;
    private Box2DDebugRenderer b2dRenderer;

    private Array<B2DSprite> blocks;

    private Texture tex;

    public Menu(GameStateManager gsm) {

        super(gsm);

        tex = Game.res.getTexture("menu");
        bg = new Background(new TextureRegion(tex), cam, 1f);
        bg.setVector(-20, 0);

        tex = Game.res.getTexture("blue_face");
        TextureRegion[] reg = new TextureRegion[8];
        for (int i = 0; i < reg.length; i++) {
            reg[i] = new TextureRegion(tex, i * 18, 0, 18, 16);
        }
        animation = new Animation(reg, 1 / 16f);

        tex = Game.res.getTexture("hud");
        tutorialButton = new GameButton(new TextureRegion(tex, 57, 39, 20, 20), 300, 220, cam);

        tex = Game.res.getTexture("buttons");
        volumnButton = new GameButton(new TextureRegion(tex, 20, 0, 20, 20), 16, 220, cam);
        playButton = new GameButton(new TextureRegion(tex, 0, 20, 46, 20), 160, 100, cam);
        exitButton = new GameButton(new TextureRegion(tex, 0, 40, 46, 20), 160, 70, cam);

        VolumnStatus.get();

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(0, 0), true);
        b2dRenderer = new Box2DDebugRenderer();

        createTitleBodies();

    }

    private void createTitleBodies() {

        // top platform
        BodyDef tpbdef = new BodyDef();
        tpbdef.type = BodyDef.BodyType.StaticBody;
        tpbdef.position.set(160 / PPM, 180 / PPM);
        Body tpbody = world.createBody(tpbdef);
        PolygonShape tpshape = new PolygonShape();
        tpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef tpfdef = new FixtureDef();
        tpfdef.shape = tpshape;
        tpfdef.filter.categoryBits = B2DVars.BIT_TOP_PLATFORM;
        tpfdef.filter.maskBits = B2DVars.BIT_TOP_BLOCK;
        tpbody.createFixture(tpfdef);
        tpshape.dispose();

        // bottom platform
        BodyDef bpbdef = new BodyDef();
        bpbdef.type = BodyDef.BodyType.StaticBody;
        bpbdef.position.set(160 / PPM, 130 / PPM);
        Body bpbody = world.createBody(bpbdef);
        PolygonShape bpshape = new PolygonShape();
        bpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef bpfdef = new FixtureDef();
        bpfdef.shape = bpshape;
        bpfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_PLATFORM;
        bpfdef.filter.maskBits = B2DVars.BIT_BOTTOM_BLOCK;
        bpbody.createFixture(bpfdef);
        bpshape.dispose();

        Texture tex = Game.res.getTexture("hud");
        TextureRegion[] blockSprites = new TextureRegion[3];
        for (int i = 0; i < blockSprites.length; i++) {
            blockSprites[i] = new TextureRegion(tex, 58 + i * 5, 34, 5, 5);
        }
        blocks = new Array<B2DSprite>();

        int[][] spellColour = {
                {0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0}
        };
        int[][] spellBlock = {
                {1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0},
                {1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1}
        };

        // top blocks
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 29; col++) {
                BodyDef tbbdef = new BodyDef();
                tbbdef.type = BodyDef.BodyType.DynamicBody;
                tbbdef.fixedRotation = true;
                tbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6 + row) / PPM);
                Body tbbody = world.createBody(tbbdef);
                PolygonShape tbshape = new PolygonShape();
                tbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef tbfdef = new FixtureDef();
                tbfdef.shape = tbshape;
                tbfdef.filter.categoryBits = B2DVars.BIT_TOP_BLOCK;
                tbfdef.filter.maskBits = B2DVars.BIT_TOP_PLATFORM | B2DVars.BIT_TOP_BLOCK;
                tbbody.createFixture(tbfdef);
                tbshape.dispose();
                if (spellColour[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(tbbody);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }

        // bottom blocks
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 29; col++) {
                BodyDef bbbdef = new BodyDef();
                bbbdef.type = BodyDef.BodyType.DynamicBody;
                bbbdef.fixedRotation = true;
                bbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6 + row) / PPM);
                Body bbbody = world.createBody(bbbdef);
                PolygonShape bbshape = new PolygonShape();
                bbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef bbfdef = new FixtureDef();
                bbfdef.shape = bbshape;
                bbfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_BLOCK;
                bbfdef.filter.maskBits = B2DVars.BIT_BOTTOM_PLATFORM | B2DVars.BIT_BOTTOM_BLOCK;
                bbbody.createFixture(bbfdef);
                bbshape.dispose();
                if (spellBlock[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(bbbody);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }

    }

    public void handleInput() {

        // mouse/touch input
        if (playButton.isClicked()) {
            Game.res.getSound("heart").play();
            gsm.setState(GameStateManager.LEVEL_SELECT);
        }
        if (tutorialButton.isClicked()) {
            Game.res.getSound("heart").play();
            gsm.setState(GameStateManager.TUTORIAL);
        }
        if (volumnButton.isClicked()) {
            if(VolumnStatus.volumnstatus == 1){
                volumnButton.setReg(new TextureRegion(tex, 20, 0, 20, 20));
                VolumnStatus.put(0);
                VolumnStatus.get();
                Game.res.loadSound("sfx/jump.wav");
                Game.res.loadSound("sfx/heart.wav");
                Game.res.loadSound("sfx/levelselect.wav");
                Game.res.loadSound("sfx/hit.wav");
                Game.res.loadSound("sfx/changeblock.wav");

                Game.res.loadMusic("music/noinaycoanh.ogg");
                Game.res.getMusic("noinaycoanh").setLooping(true);
                Game.res.getMusic("noinaycoanh").setVolume(0.5f);
                Game.res.getMusic("noinaycoanh").play();
            }
            else {
                volumnButton.setReg(new TextureRegion(tex, 0, 0, 20, 20));
                VolumnStatus.put(1);
                VolumnStatus.get();
                Game.res.removeSound("jump");
                Game.res.removeSound("heart");
                Game.res.removeSound("levelselect");
                Game.res.removeSound("hit");
                Game.res.removeSound("changeblock");

                Game.res.removeMusic("noinaycoanh");
            }

        }
        if(exitButton.isClicked()){
            Gdx.app.exit();
        }
    }

    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

        bg.update(dt);
        animation.update(dt);

        playButton.update(dt);

        tutorialButton.update(dt);

        volumnButton.update(dt);

        exitButton.update(dt);
    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        // draw background
        bg.render(sb);

        // draw button
        playButton.render(sb);
        tutorialButton.render(sb);
        volumnButton.render(sb);
        exitButton.render(sb);

        // draw bunny
        sb.begin();
        sb.draw(animation.getFrame(), 146, 31);
        sb.end();

        // debug draw box2d
        if (debug) {
            cam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
            b2dRenderer.render(world, cam.combined);
            cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        }

        // draw title
        for (int i = 0; i < blocks.size; i++) {
            blocks.get(i).render(sb);
        }

    }

    public void dispose() {

    }

}