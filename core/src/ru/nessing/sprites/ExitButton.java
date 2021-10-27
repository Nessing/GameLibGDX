package ru.nessing.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.BaseButton;
import ru.nessing.math.Rect;

public class ExitButton extends BaseButton {

    private static final float HEIGHT = 0.15f;

    public ExitButton(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setRight(worldBounds.getRight());
        setBottom(worldBounds.getBottom());
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
