package com.gamedev.colourblock.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Quyet on 4/1/2017.
 */

public class Assets {
    public static AssetManager manager = new AssetManager();
    public static Skin menuSkin;
    public static TextureAtlas textureAtlas;

    public static void load() {
        manager.load("mainmenu.pack",TextureAtlas.class);
    }

    public static void loadLevel() {
        manager.load("levelatlas.pack",TextureAtlas.class);
    }

    public static void setMenuSkin() {
        if (menuSkin == null)
            menuSkin = new Skin(Gdx.files.internal("mainmenu.json"),
                    manager.get("mainmenu.pack", TextureAtlas.class));
    }

    public static void setLevel() {
        if (textureAtlas == null)
            textureAtlas=manager.get("levelatlas.pack",TextureAtlas.class);
    }

    public static boolean update(){
        return manager.update();
    }

    public static void dispose() {
        manager.dispose();
    }

}
