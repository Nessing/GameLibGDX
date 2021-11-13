package ru.nessing.pool;

import com.badlogic.gdx.audio.Sound;

import ru.nessing.base.SpritesPool;
import ru.nessing.math.Rect;
import ru.nessing.sprites.SuperShotGet;

public class SuperShotPool extends SpritesPool<SuperShotGet> {
    private final Rect worldBounds;
    private final Sound soundShoot;

    public SuperShotPool(Rect worldBounds, Sound soundShoot) {
        this.worldBounds = worldBounds;
        this.soundShoot = soundShoot;
    }

    @Override
    protected SuperShotGet newObject() {
        return new SuperShotGet(worldBounds, soundShoot);
    }
}
