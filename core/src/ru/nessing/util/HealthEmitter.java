package ru.nessing.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.pool.HealthPool;
import ru.nessing.sprites.HealthUp;

public class HealthEmitter {

    private static float generateInterval = 20f;

    private static final float HELPER_HEIGHT = 0.07f;

    private final Rect worldBounds;
    private final HealthPool healthPool;
    private final TextureRegion helperRegion;

    private final TextureRegion[] helperRegions;

    private final Vector2 helperSpeed = new Vector2(-0.3f, 0);

    public HealthEmitter(HealthPool enemyPool, Rect worldBounds, TextureAtlas atlas) {
        this.healthPool = enemyPool;
        this.worldBounds = worldBounds;
        helperRegion = atlas.findRegion("repair");
        helperRegions = Regions.split(atlas.findRegion("repair"), 1, 1, 1);
    }

    private float generateTimer;

    public void generate(float deltaTime) {
        generateTimer += deltaTime;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            generateInterval = Rnd.nextFloat(20f, 60f);
            HealthUp healthUp = healthPool.obtain();
            healthUp.set(
                    helperRegions,
                    helperSpeed,
                    HELPER_HEIGHT
            );
            healthUp.pos.y = Rnd.nextFloat(worldBounds.getTop() - healthUp.getHalfHeight(),
                    -0.3f
            );
            healthUp.setLeft(worldBounds.getRight());
        }
    }
}
