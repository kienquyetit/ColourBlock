package com.gamedev.colourblock.handler;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Quyet on 3/29/2017.
 */

public class CBInputProcessor extends InputAdapter {

    public boolean mouseMoved(int x, int y) {
        CBInput.x = x;
        CBInput.y = y;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        CBInput.x = x;
        CBInput.y = y;
        CBInput.down = true;
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        CBInput.x = x;
        CBInput.y = y;
        CBInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        CBInput.x = x;
        CBInput.y = y;
        CBInput.down = false;
        return true;
    }

    public boolean keyDown(int k) {
        if (k == Input.Keys.Z) CBInput.setKey(CBInput.BUTTON1, true);
        if (k == Input.Keys.X) CBInput.setKey(CBInput.BUTTON2, true);
        return true;
    }

    public boolean keyUp(int k) {
        if (k == Input.Keys.Z) CBInput.setKey(CBInput.BUTTON1, false);
        if (k == Input.Keys.X) CBInput.setKey(CBInput.BUTTON2, false);
        return true;
    }

}
