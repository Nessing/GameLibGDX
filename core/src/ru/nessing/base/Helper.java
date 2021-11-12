package ru.nessing.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;

public class Helper extends Sprite {

    protected Vector2 direction;
    protected int hp;

    protected Sound sound;
    protected float volumeShoot;
    protected Rect worldBounds;

    public int getHp() {
        return hp;
    }

    public Helper() {
    }

    public Helper(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float deltaTime) {
        pos.mulAdd(direction, deltaTime);
        frame = 0;
    }

    public void damage(int damageHp) {
        this.hp -= damageHp;
        if (this.hp <= 0) {
            this.hp = 0;
            destroy();
        }
    }
}
