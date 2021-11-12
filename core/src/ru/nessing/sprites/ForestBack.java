package ru.nessing.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class ForestBack extends Sprite {

    private float speedUp = -0.2f;

    private Vector2 speed;
    private Rect worldBounds;

    public void setSpeedUp(float speedUp) {
        this.speedUp -= speedUp;
    }

    public ForestBack(Texture forest) {
        super(new TextureRegion(forest));
        speed = new Vector2(-0.2f, 0);
    }

    public void resize(Rect worldBounds, boolean second) {
        setHeightProportion(worldBounds.getHeight() + worldBounds.getHalfHeight() / 2 + 0.1f);
        this.worldBounds = worldBounds;
        if (second) {
            pos.set(getWidth(), worldBounds.pos.y);
        } else {
            pos.set(worldBounds.pos);
        }
    }

    @Override
    public void update(float deltaTime) {
        speed.x = speedUp;
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
    }
}
