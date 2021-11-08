package ru.nessing.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.BaseButton;
import ru.nessing.math.Rect;
import ru.nessing.screen.GameScreen;

public class RestartButton extends BaseButton {

    private static final float HEIGHT = 0.10f;
    private final Game game;

    public RestartButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btRestart"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() / 2);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
