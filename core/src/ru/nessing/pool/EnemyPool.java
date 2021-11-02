package ru.nessing.pool;

import com.badlogic.gdx.audio.Sound;

import ru.nessing.base.SpritesPool;
import ru.nessing.math.Rect;
import ru.nessing.sprites.EnemyAirplane;

public class EnemyPool extends SpritesPool<EnemyAirplane> {
    private final BulletPool bulletPool;
    private final Rect worldBounds;
    private final Sound soundShoot;

    public EnemyPool(BulletPool bulletPool, Rect worldBounds, Sound soundShoot) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected EnemyAirplane newObject() {
        return new EnemyAirplane(bulletPool, worldBounds, soundShoot);
    }
}
