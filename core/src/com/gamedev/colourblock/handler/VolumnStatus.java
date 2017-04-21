package com.gamedev.colourblock.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Quyet on 4/3/2017.
 */

public class VolumnStatus {
    public static Integer volumnstatus;

    public static void put(int status) {
        Preferences prefs = Gdx.app.getPreferences("VolumnPrefs");
        prefs.putInteger("volumnstatus", status);
        prefs.flush();
    }

    public static void get() {
        Preferences prefs = Gdx.app.getPreferences("VolumnPrefs");
        volumnstatus = prefs.getInteger("volumnstatus", 0);
    }
}
