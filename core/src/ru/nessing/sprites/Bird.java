package ru.nessing.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class Bird extends Sprite {
    private Vector2 position = new Vector2();
    private Vector2 endPosition = new Vector2();
    private Vector2 checkPosStop = new Vector2();

    public Bird(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        endPosition.set(touch.cpy().sub(position)).nor().scl(0.006f);
        checkPosStop.set(touch);
        return super.touchDown(touch, pointer, button);
    }

    public void draw(SpriteBatch batch, float weight, float height) {
        // Остановка в указанной мышью точке
        if (position.dst(checkPosStop) <= endPosition.len()) {
            endPosition.set(0, 0);
        } else {
            position.add(endPosition);
        }

        super.draw(batch, weight, height, position.x, position.y);
    }
}
