package com.gamedev.colourblock.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamedev.colourblock.handler.GameButton;
import com.gamedev.colourblock.handler.GameStateManager;
import com.gamedev.colourblock.main.Game;

/**
 * Created by Quyet on 3/31/2017.
 */

public class Tutorial extends GameState {

    private Texture tutorialTexture, tex;
    private GameButton backButton;

    public Tutorial(final GameStateManager gsm) {
        super(gsm);
        tutorialTexture = Game.res.getTexture("tutorial");
        tex = Game.res.getTexture("hud");

        backButton = new GameButton(new TextureRegion(tex, 75, 39, 20, 20), 16, 220, cam);
    }

    public void handleInput() {
        if (backButton.isClicked()) {
            Game.res.getSound("heart").play();
            gsm.setState(GameStateManager.MENU);
        }
    }

    public void update(float dt) {

        handleInput();

        // update button
        backButton.update(dt);
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(tutorialTexture, 0, 0);
        sb.end();

        backButton.render(sb);
    }

    public void dispose() {

    }
}

