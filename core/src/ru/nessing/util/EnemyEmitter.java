package ru.nessing.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.pool.EnemyPool;
import ru.nessing.sprites.EnemyAirplane;

public class EnemyEmitter {

    private static float generateInterval = 3f;

    private static final float ENEMY_SMALL_HEIGHT = 0.07f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;

    private static float speedUpGenerateMin = 5f;
    private static float speedUpGenerateMax = 7f;

    private static float speedUpshotMin = 3f;
    private static float speedUpshotMax = 5f;
    private static final float enemySmallReloadInterval = Rnd.nextFloat(2.5f, 4f);

    private static int enemySmallHp = 1;

    private final Rect worldBounds;
    private final EnemyPool enemyPool;
    private final TextureRegion bulletRegion;

    private final TextureRegion[] enemySmallRegions;

    private final Vector2 enemySmallSpeed = new Vector2(-0.3f, 0);

    private final Vector2 enemySmallBulletSpeed = new Vector2(-0.5f, 0);


    public EnemyEmitter(EnemyPool enemyPool, Rect worldBounds, TextureAtlas atlas) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        bulletRegion = atlas.findRegion("enemyBullet");
        enemySmallRegions = Regions.split(atlas.findRegion("airplaneEnemy"), 2, 1, 2);
    }

    private float generateTimer;

    public void generate(float deltaTime) {
        generateTimer += deltaTime;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            generateInterval = Rnd.nextFloat(speedUpGenerateMin, speedUpGenerateMax);
            EnemyAirplane enemy = enemyPool.obtain();
            enemy.set(
                    enemySmallRegions,
                    enemySmallSpeed,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    enemySmallBulletSpeed,
                    ENEMY_SMALL_BULLET_DAMAGE,
                    enemySmallHp,
                    enemySmallReloadInterval,
                    ENEMY_SMALL_HEIGHT
            );
            enemy.pos.y = Rnd.nextFloat(worldBounds.getTop() - enemy.getHalfHeight(), -0.3f);
            enemy.setLeft(worldBounds.getRight());
        }
    }

    public void speedUpGenerate() {
        if (speedUpGenerateMin - 0.7f <= 0) speedUpGenerateMin = 0.5f;
        else speedUpGenerateMin -= 0.7f;
        if (speedUpGenerateMax - 1f <= 0) speedUpGenerateMax = 1f;
        else speedUpGenerateMax -= 1f;

        if (speedUpshotMin - 0.6f <= 0) speedUpshotMin = 0.2f;
        else speedUpshotMin -= 0.6f;
        if (speedUpshotMax - 0.5f <= 0) speedUpshotMax = 0.5f;
        else speedUpshotMax -= 0.5f;
        enemySmallHp += 1;

    }
}
