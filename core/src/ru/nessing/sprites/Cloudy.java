package ru.nessing.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;

public class Cloudy extends Sprite {

    private final Vector2 speed;
    private Rect worldBounds;

    public Cloudy(TextureAtlas cloudy, String name) {
        super(cloudy.findRegion(name));
        speed = new Vector2(Rnd.nextFloat(-0.15f, -0.2f), 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float height = speed.x * Rnd.nextFloat(-0.5f, -2);
        setHeightProportion(height);
        float x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(x, y);
    }

    @Override
    public void update(float deltaTime) {
        pos.mulAdd(speed, deltaTime);
        checkBounds();
    }

    private void checkBounds() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
        if (getBottom() > worldBounds.getTop()) {
            setTop(worldBounds.getBottom());
        }
    }
}
