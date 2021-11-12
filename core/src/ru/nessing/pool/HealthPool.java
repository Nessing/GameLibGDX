package ru.nessing.pool;

import com.badlogic.gdx.audio.Sound;

import ru.nessing.base.SpritesPool;
import ru.nessing.math.Rect;
import ru.nessing.sprites.HealthUp;

public class HealthPool extends SpritesPool<HealthUp> {
    private final Rect worldBounds;
    private final Sound soundShoot;

    public HealthPool(Rect worldBounds, Sound soundShoot) {
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected HealthUp newObject() {
        return new HealthUp(worldBounds, soundShoot);
    }
}
