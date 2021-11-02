package ru.nessing.sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Ship;
import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;

public class EnemyAirplane extends Ship {

    public EnemyAirplane(BulletPool bulletPool, Rect worldBounds, Sound soundShoot) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
        bulletSpeed = new Vector2();
        positionBullet = new Vector2();
        this.direction = new Vector2(-speed, 0);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        positionBullet.set(pos.x - 0.1f, pos.y);
        if (getLeft() < worldBounds.getLeft() - getWidth()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 startSpeed,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletSpeed,
            int damage,
            int hp,
            float reloadInterval,
            float height
    ) {
        this.regions = regions;
        this.direction.set(startSpeed);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletSpeed = bulletSpeed;
        this.damage = damage;
        this.hp = hp;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
    }
}
