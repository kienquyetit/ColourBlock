package com.gamedev.colourblock.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamedev.colourblock.main.Game;

/**
 * Created by Quyet on 3/29/2017.
 */

public class HUD {

    private Player player;

    private TextureRegion[] blocks;
    private TextureRegion heart;
    private TextureRegion[] font;

    public HUD(Player player) {

        this.player = player;

        Texture tex = Game.res.getTexture("hud");

        blocks = new TextureRegion[3];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
        }

        heart = new TextureRegion(tex, 80, 0, 16, 16);

        font = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }
        for (int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
        }

    }

    public void render(SpriteBatch sb) {

        sb.begin();

        // draw heart
        sb.draw(heart, 100, 208);

        // draw heart amount
        drawString(sb, player.getNumHearts() + " / " + player.getTotalHeart(), 132, 211);

        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '/') c = 10;
            else if (c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }
    }

}
