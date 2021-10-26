package ru.nessing.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.nessing.base.BaseButton;
import ru.nessing.math.Rect;
import ru.nessing.screen.GameScreen;
import ru.nessing.screen.MenuScreen;

public class BackButton extends BaseButton {

    private static final float HEIGHT = 0.15f;
    private final Game game;

    public BackButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btBack"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setRight(worldBounds.getRight() - 0.05f);
        setTop(worldBounds.getTop());
    }

    @Override
    public void action() {
        game.setScreen(new MenuScreen(game));
    }
}
