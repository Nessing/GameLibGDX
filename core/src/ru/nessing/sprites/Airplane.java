package ru.nessing.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class Airplane extends Sprite {
//    private Vector2 position = new Vector2();
//    private Vector2 endPosition = new Vector2();
//    private Vector2 checkPosStop = new Vector2();
    private Rect worldBounds;
    private Vector2 direction;
    private final float SPEED = 0.3f;
    private int push;

    public Airplane(TextureAtlas textureAtlas, String name) {
        super(new TextureRegion(textureAtlas.findRegion(name).getTexture(), 0, 500, 2000, 600));
//        super(textureAtlas.findRegion(name));
        direction = new Vector2(0, 0);
        pos.set(-0.6f, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.worldBounds = worldBounds;
    }

    // 51 ↑  47 ↓  29 ←  32 →
    @Override
    public boolean keyDown(int button) {
        if (button == 51) {
            push = 51;
        }
        if (button == 47) {
            push = 47;
        }
        if (button == 29) {
            push = 29;
        }
        if (button == 32) {
            push = 32;
        }
        return false;
    }

    @Override
    public boolean keyUp(int button) {
        push = 0;
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
//        endPosition.set(touch.cpy().sub(position)).nor().scl(0.006f);
//        checkPosStop.set(touch);
        if (touch.y > 0) {
            push = 51;
        } else if (touch.y < 0) {
            push = 47;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        push = 0;
        return false;
    }

    @Override
    public void update(float deltaTime) {
//        if (position.dst(checkPosStop) <= endPosition.len()) {
//            endPosition.set(0, 0);
//        } else {
//            position.add(endPosition);
//        }
//        if (pos.y >= 0.5f) {
//            pos.y = 0.5f;
//        }
//        if (pos.y <= -0.3f) {
//            pos.y = -0.3f;
//        }
        if (push == 51) {
            direction.set(0, SPEED);
            pos.mulAdd(direction, deltaTime);
        }
        if (push == 47) {
            direction.set(0, -SPEED);
            pos.mulAdd(direction, deltaTime);
        }
        if (push == 29) {
            direction.set(-SPEED, 0);
            pos.mulAdd(direction, deltaTime);
        }
        if (push == 32) {
            direction.set(SPEED, 0);
            pos.mulAdd(direction, deltaTime);
        }

        if (pos.x <= worldBounds.getLeft() + 0.2f) {
            pos.x = worldBounds.getLeft() + 0.2f;
        }
        if (pos.x >= worldBounds.getRight() - 0.2f) {
            pos.x = worldBounds.getRight() - 0.2f;
        }
        if (pos.y >= worldBounds.getTop() - 0.05f) {
            pos.y = worldBounds.getTop() - 0.05f;
        }
        if (pos.y <= worldBounds.getBottom() + 0.2f) {
            pos.y = worldBounds.getBottom() + 0.2f;
        } else {
            direction.set(0, 0);
        }

    }

//    public void draw(SpriteBatch batch) {
//        // Остановка в указанной мышью точке
//
//        super.draw(batch, position.x, position.y);
//    }
}
