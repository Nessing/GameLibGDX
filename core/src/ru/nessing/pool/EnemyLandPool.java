package ru.nessing.pool;

import com.badlogic.gdx.audio.Sound;

import ru.nessing.base.SpritesPool;
import ru.nessing.math.Rect;
import ru.nessing.sprites.EnemyAirplane;
import ru.nessing.sprites.EnemyPanzer;

public class EnemyLandPool extends SpritesPool<EnemyPanzer> {
    private final BulletPool bulletPool;
    private final ExplosionPool explosionPool;
    private final Rect worldBounds;
    private final Sound soundShoot;

    public EnemyLandPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound soundShoot) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected EnemyPanzer newObject() {
        return new EnemyPanzer(bulletPool, explosionPool, worldBounds, soundShoot);
    }
}
