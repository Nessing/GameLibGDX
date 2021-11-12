package ru.nessing.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;
import ru.nessing.pool.ExplosionPool;
import ru.nessing.sprites.Bullet;
import ru.nessing.sprites.Explosion;

public class Ship extends Sprite {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected Vector2 direction;

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;

    protected int damage;
    protected int hp;

    protected Sound soundShoot;
    protected float volumeShoot;

    protected TextureRegion bulletRegion;
    protected Vector2 bulletSpeed;
    protected float bulletHeight;

    protected float speed;
    protected Vector2 positionBullet;

    protected float reloadTimer;
    protected float reloadInterval;
    protected Rect worldBounds;

    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

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
        damageAnimateTimer += deltaTime;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void damage(int damageHp) {
        this.hp -= damageHp;
        if (this.hp <= 0) {
            this.hp = 0;
            destroy();
            boom();
        }
        damageAnimateTimer = 0;
        frame = 1;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, positionBullet, bulletSpeed, worldBounds, bulletHeight, damage);
        soundShoot.play(volumeShoot);
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(this.pos, getHeight());
    }
}
