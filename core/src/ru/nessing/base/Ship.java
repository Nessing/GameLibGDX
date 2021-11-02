package ru.nessing.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;
import ru.nessing.sprites.Bullet;

public class Ship extends Sprite {

    protected Vector2 direction;

    protected int damage;
    protected int hp;

    protected Sound soundShoot;

    protected TextureRegion bulletRegion;
    protected BulletPool bulletPool;
    protected Vector2 bulletSpeed;
    protected float bulletHeight;

    protected float speed;
    protected Vector2 positionBullet;

    protected float reloadTimer;
    protected float reloadInterval;
    protected Rect worldBounds;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float deltaTime) {
        pos.mulAdd(direction, deltaTime);
        reloadTimer += deltaTime;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0;
            shoot();
        }
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
//        positionBullet.set(pos.x + 0.2f, pos.y);
        bullet.set(this, bulletRegion, positionBullet, bulletSpeed, worldBounds, bulletHeight, damage);
        soundShoot.play(0.1f);
    }
}
