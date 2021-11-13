package ru.nessing.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.pool.HealthPool;
import ru.nessing.pool.SuperShotPool;
import ru.nessing.sprites.HealthUp;
import ru.nessing.sprites.SuperShotGet;

public class SuperShotEmitter {

    private static float generateInterval = 30f;

    private static final float HELPER_HEIGHT = 0.07f;

    private final Rect worldBounds;
    private final SuperShotPool healthPool;
    private final TextureRegion helperRegion;

    private final TextureRegion[] helperRegions;

    private final Vector2 helperSpeed = new Vector2(-0.3f, 0);

    public SuperShotEmitter(SuperShotPool superShotPool, Rect worldBounds, TextureAtlas atlas) {
        this.healthPool = superShotPool;
        this.worldBounds = worldBounds;
        helperRegion = atlas.findRegion("minigun");
        helperRegions = Regions.split(atlas.findRegion("minigun"), 1, 1, 1);
    }

    private float generateTimer;

    public void generate(float deltaTime) {
        generateTimer += deltaTime;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            generateInterval = Rnd.nextFloat(20f, 60f);
            SuperShotGet SuperShotGet = healthPool.obtain();
            SuperShotGet.set(
                    helperRegions,
                    helperSpeed,
                    HELPER_HEIGHT
            );
            SuperShotGet.pos.y = Rnd.nextFloat(worldBounds.getTop() - SuperShotGet.getHalfHeight(),
                    -0.3f
            );
            SuperShotGet.setLeft(worldBounds.getRight());
        }
    }
}
