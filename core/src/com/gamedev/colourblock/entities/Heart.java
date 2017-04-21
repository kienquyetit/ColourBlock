package com.gamedev.colourblock.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gamedev.colourblock.main.Game;

/**
 * Created by Quyet on 3/29/2017.
 */

public class Heart extends B2DSprite {

    public Heart(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("heart");
        TextureRegion[] sprites = TextureRegion.split(tex, 19, 16)[0];
        animation.setFrames(sprites, 1 / 30f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

}