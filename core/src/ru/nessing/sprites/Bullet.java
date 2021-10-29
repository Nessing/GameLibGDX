package ru.nessing.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class Bullet extends Sprite {

    private final Vector2 speed = new Vector2();

    private Rect worldBounds;
    private int damage;
    private Sprite owner;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void set(Sprite owner, TextureRegion region,
                    Vector2 pos, Vector2 speed,
                    Rect worldBounds,
                    float height, int damage) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos);
        this.speed.set(speed);
        this.worldBounds = worldBounds;
        setHeightProportion(height);
        this.damage = damage;
    }

    @Override
    public void update(float deltaTime) {
        pos.mulAdd(speed, deltaTime);
        if (isOutside(worldBounds)) destroy();
    }

    public int getDamage() {
        return damage;
    }

    public Sprite getOwner() {
        return owner;
    }
}
