package ru.nessing.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Helper;
import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.util.SuperShotEmitter;

public class SuperShotGet extends Helper {

    private boolean checkDirectY = false;

    private final Vector2 speedNormal = new Vector2();

    public void setCheckDirectY(boolean checkDirectY) {
        this.checkDirectY = checkDirectY;
    }


    public SuperShotGet(Rect worldBounds, Sound sound) {
        this.worldBounds = worldBounds;
        this.sound = sound;
        this.direction = new Vector2();
        this.volumeShoot = 0.2f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (pos.x + getHeight() < worldBounds.getHalfWidth()) {
            if (!checkDirectY) {
                this.direction.set(speedNormal);
                checkDirectY = true;
            }
        }
        if (getBottom() <= worldBounds.getBottom() + 0.2f) direction.y = Rnd.nextFloat(0.1f, 0.1f);
        else if (getTop() >= worldBounds.getTop()) direction.y = Rnd.nextFloat(-0.1f, -0.1f);

        if (getLeft() < worldBounds.getLeft() - getWidth()) {
            checkDirectY = false;
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 startSpeed,
            float height
    ) {
        this.regions = regions;
        this.speedNormal.set(startSpeed.x, Rnd.nextFloat(-0.1f, 0.1f));
        setHeightProportion(height);
        this.direction.set(-0.5f, 0);
    }

    @Override
    public void damage(int damageHp) {
        this.hp -= damageHp;
        if (this.hp <= 0) {
            this.hp = 0;
            sound.play();
            destroy();
        }
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < pos.x
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < getBottom());
    }
}
