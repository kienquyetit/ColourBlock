package com.gamedev.colourblock.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gamedev.colourblock.handler.Assets;
import com.gamedev.colourblock.handler.GameStateManager;

/**
 * Created by Quyet on 4/1/2017.
 */

public class Splash extends GameState {

    private Texture texture=new Texture(Gdx.files.internal("smiles.png"));
    private Image splashImage=new Image(texture);
    private Stage stage=new Stage();

    public Splash(final GameStateManager gsm){
        super(gsm);
        stage.addActor(splashImage);
        splashImage.setPosition(Gdx.graphics.getWidth()/2-texture.getWidth()/2, Gdx.graphics.getHeight()/2-texture.getHeight()/2);
        splashImage.addAction(Actions.sequence(Actions.alpha(0)
                ,Actions.fadeIn(4.0f)
                ,Actions.delay(1)
                ,Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        gsm.setState(GameStateManager.MENU);
                    }
                })));
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(159/255.0f,220/255.0f,235/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if(Assets.update()){
            //-------------------------------------------
            Assets.setLevel();
            Assets.setMenuSkin();
        }

    }

    @Override
    public void dispose() {
        texture.dispose();
        stage.dispose();
    }
}
