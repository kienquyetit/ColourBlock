package com.gamedev.colourblock.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.gamedev.colourblock.handler.Assets;
import com.gamedev.colourblock.handler.GameButton;
import com.gamedev.colourblock.handler.GameStateManager;
import com.gamedev.colourblock.handler.LevelStatus;
import com.gamedev.colourblock.main.Game;

/**
 * Created by Quyet on 3/29/2017.
 */

public class LevelSelect extends GameState {

    private Texture texture;

    private GameButton[][] buttons;

    private GameButton backButton;

    private TextureRegion lockselectbg[]=new TextureRegion[10];

    Array<AtlasRegion> atlasArrays;

    public LevelSelect(GameStateManager gsm) {

        super(gsm);

        texture = Game.res.getTexture("bg_menu");

        LevelStatus.get();

        atlasArrays = new Array<AtlasRegion>(Assets.textureAtlas.getRegions());

        Texture tex = Game.res.getTexture("hud");
        backButton = new GameButton(new TextureRegion(tex, 75, 39, 20, 20), 16, 220, cam);

        updateButtons();

        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

    }

    public void updateButtons(){
        Gdx.app.log("levelSelect", "levelclear " + LevelStatus.levelclear);
        for(int i=0; i<10; i++) {
            if (i+1<=LevelStatus.levelclear){
                int star = LevelStatus.level[i];
                Gdx.app.log("levelSelect", "level " + LevelStatus.level[i]);
                switch (star) {
                    case 1:  lockselectbg[i] = atlasArrays.get(21);
                        break;
                    case 2:  lockselectbg[i]  = atlasArrays.get(22);
                        break;
                    case 3:  lockselectbg[i]  = atlasArrays.get(23);
                        break;
                    default: lockselectbg[i]  = atlasArrays.get(21);
                        break;
                }
            }
            else{
                lockselectbg[i] = atlasArrays.get(i+1);
            }
        }

        buttons = new GameButton[2][5];
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                buttons[row][col] = new GameButton(lockselectbg[row * buttons[0].length + col], 70 + col * 50, 170 - row * 50, cam);
                if(row * buttons[0].length + col + 1 > LevelStatus.levelclear){
                    buttons[row][col].setText("");
                }
                else {
                    buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
                }

            }
        }
    }

    public void handleInput() {
        if (backButton.isClicked()) {
            Game.res.getSound("heart").play();
            gsm.setState(GameStateManager.MENU);
        }
    }

    public void update(float dt) {

        handleInput();

        // upate backButton
        backButton.update(dt);

        // update levelselect
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].update(dt);
                if (buttons[row][col].isClicked()) {
                    // TODO: Better handling of level out of range
                    int clickedLevel = row * buttons[0].length + col + 1;
                    if (clickedLevel <= LevelStatus.levelclear) {
                        Play.level = row * buttons[0].length + col + 1;
                        Game.res.getSound("levelselect").play();
                        gsm.setState(GameStateManager.PLAY);
                    }
                }
            }
        }

    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(texture, 0, 0);
        sb.end();

        for (GameButton[] button : buttons) {
            for (int col = 0; col < buttons[0].length; col++) {
                button[col].render(sb);
            }
        }

        // render backButton
        backButton.render(sb);
    }

    public void dispose() {

    }

}
