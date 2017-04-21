package com.gamedev.colourblock.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Quyet on 4/3/2017.
 */

public class LevelStatus {

    public static int level[]=new int[10];
    public static Integer levelclear;

    public static void put(int lvlclear, int lv, int star) {
        Preferences prefs = Gdx.app.getPreferences("SmileBlockPrefs");
        prefs.putInteger("levelclear", lvlclear);
        prefs.putInteger("level"+lv, star);
        prefs.flush();
    }

    public static void get() {
        Preferences prefs = Gdx.app.getPreferences("SmileBlockPrefs");
        levelclear = prefs.getInteger("levelclear",1);
        level[0] = prefs.getInteger("level1",0);
        level[1] = prefs.getInteger("level2",0);
        level[2] = prefs.getInteger("level3",0);
        level[3] = prefs.getInteger("level4",0);
        level[4] = prefs.getInteger("level5",0);
        level[5] = prefs.getInteger("level6",0);
        level[6] = prefs.getInteger("level7",0);
        level[7] = prefs.getInteger("level8",0);
        level[8] = prefs.getInteger("level9",0);
        level[9]= prefs.getInteger("level10",0);
    }
}
