package ru.nessing.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.BaseButton;
import ru.nessing.math.Rect;

public class ArrowRight extends BaseButton {

    private static final float HEIGHT = 0.15f;
    private final String name;

    public ArrowRight(TextureAtlas atlas, String name) {
        super(atlas.findRegion(name));
        this.name = name;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        if (name.equals("arrow_right")) {
            setLeft(worldBounds.getLeft() + 0.3f);
            setBottom(worldBounds.getBottom() + 0.3f);
        }
        if (name.equals("arrow_left")) {
            setLeft(worldBounds.getLeft());
            setBottom(worldBounds.getBottom() + 0.3f);
        }
        if (name.equals("arrow_up")) {
            setLeft(worldBounds.getLeft() + 0.19f);
            setBottom(worldBounds.getBottom() + 0.5f);
        }
        if (name.equals("arrow_down")) {
            setLeft(worldBounds.getLeft() + 0.19f);
            setBottom(worldBounds.getBottom() + 0.1f);
        }

    }

    @Override
    public void action() {

    }
}
