package com.gamedev.colourblock.handler;

import com.gamedev.colourblock.main.Game;
import com.gamedev.colourblock.states.GameState;
import com.gamedev.colourblock.states.LevelSelect;
import com.gamedev.colourblock.states.Menu;
import com.gamedev.colourblock.states.Play;
import com.gamedev.colourblock.states.Splash;
import com.gamedev.colourblock.states.Tutorial;

import java.util.Stack;

/**
 * Created by Quyet on 3/29/2017.
 */

public class GameStateManager {

    private Game game;

    private Stack<GameState> gameStates;

    public static final int SPLASH = 0;
    public static final int MENU = 1;
    public static final int TUTORIAL = 2;
    public static final int PLAY = 3;
    public static final int LEVEL_SELECT = 4;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(SPLASH);
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public Game game() {
        return game;
    }

    private GameState getState(int state) {
        if (state == SPLASH) return new Splash(this);
        if (state == MENU) return new Menu(this);
        if (state == TUTORIAL) return new Tutorial(this);
        if (state == PLAY) return new Play(this);
        if (state == LEVEL_SELECT) return new LevelSelect(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}