package ru.nessing.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class LogoGame extends Sprite {

    private Rect worldBounds;

    public LogoGame(Texture forest) {
        super(new TextureRegion(forest));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHalfHeight());
        this.worldBounds = worldBounds;
    }
}
