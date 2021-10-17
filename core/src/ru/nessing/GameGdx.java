package ru.nessing;

import com.badlogic.gdx.Game;

import ru.nessing.screen.MenuScreen;

public class GameGdx extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen());
    }
}
