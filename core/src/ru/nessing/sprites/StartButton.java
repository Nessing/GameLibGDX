package ru.nessing.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.BaseButton;
import ru.nessing.math.Rect;
import ru.nessing.screen.GameScreen;

public class StartButton extends BaseButton {

    private static final float HEIGHT = 0.15f;
    private final Game game;

    public StartButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btStart"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft());
        setBottom(worldBounds.getBottom());
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
