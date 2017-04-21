package com.gamedev.colourblock.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamedev.colourblock.handler.Assets;
import com.gamedev.colourblock.handler.BoundedCamera;
import com.gamedev.colourblock.handler.CBInput;
import com.gamedev.colourblock.handler.CBInputProcessor;
import com.gamedev.colourblock.handler.Content;
import com.gamedev.colourblock.handler.GameStateManager;

/**
 * Created by Quyet on 3/29/2017.
 */

public class Game extends ApplicationAdapter {

    public static final String TITLE = "Colour Block";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final float STEP = 1 / 60f;

    private SpriteBatch sb;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public static Content res;

    public void create() {
        Gdx.input.setInputProcessor(new CBInputProcessor());

        Assets.load();
        //-------------------------------------------
        Assets.loadLevel();
        Assets.manager.finishLoading();

        res = new Content();
        res.loadTexture("images/buttons.png");
        res.loadTexture("images/tutorial_icon.png");
        res.loadTexture("images/tutorial.png");
        res.loadTexture("images/menu.png");
        res.loadTexture("images/bg_menu.png");
        res.loadTexture("images/bgs.png");
        res.loadTexture("images/hud.png");
        res.loadTexture("images/red_face.png");
        res.loadTexture("images/green_face.png");
        res.loadTexture("images/blue_face.png");
        res.loadTexture("images/heart.png");
        res.loadTexture("images/ghost.png");

        res.loadSound("sfx/jump.wav");
        res.loadSound("sfx/heart.wav");
        res.loadSound("sfx/levelselect.wav");
        res.loadSound("sfx/hit.wav");
        res.loadSound("sfx/changeblock.wav");

        res.loadMusic("music/noinaycoanh.ogg");
        res.getMusic("noinaycoanh").setLooping(true);
        res.getMusic("noinaycoanh").setVolume(0.5f);
        res.getMusic("noinaycoanh").play();

        cam = new BoundedCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        sb = new SpriteBatch();

        gsm = new GameStateManager(this);

    }

    public void render() {

        Gdx.graphics.setTitle(TITLE);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
        CBInput.update();

    }

    public void dispose() {
        res.removeAll();
        Assets.dispose();
    }

    public void resize(int w, int h) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public BoundedCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

}
