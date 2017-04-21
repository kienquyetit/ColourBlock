package com.gamedev.colourblock.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gamedev.colourblock.main.Game;

/**
 * Created by Quyet on 3/29/2017.
 */

public class Player extends B2DSprite {

    private int numHearts;
    private int totalHearts;

    public Player(Body body) {
        super(body);
        drawPlayer("red_face");
    }

    private void drawPlayer(String texName){
        Texture tex = Game.res.getTexture(texName);
        TextureRegion[] sprites = new TextureRegion[8];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 18, 0, 18, 16);
        }

        animation.setFrames(sprites, 1 / 16f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }

    public void updatePlayer(String texName){
        drawPlayer(texName);
    }

    public void collectCrystal() {
        numHearts++;
    }

    public int getNumHearts() {
        return numHearts;
    }

    public void setTotalHearts(int i) {
        totalHearts = i;
    }

    public int getTotalHeart() {
        return totalHearts;
    }

}