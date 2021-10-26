package ru.nessing.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class Airplane extends Sprite {
    private Vector2 position = new Vector2();
    private Vector2 endPosition = new Vector2();
    private Vector2 checkPosStop = new Vector2();

    public Airplane(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        endPosition.set(touch.cpy().sub(position)).nor().scl(0.006f);
        checkPosStop.set(touch);
        return false;
    }

    public void draw(SpriteBatch batch) {
        // Остановка в указанной мышью точке
        if (position.dst(checkPosStop) <= endPosition.len()) {
            endPosition.set(0, 0);
        } else {
            position.add(endPosition);
        }

        super.draw(batch, position.x, position.y);
    }
}
