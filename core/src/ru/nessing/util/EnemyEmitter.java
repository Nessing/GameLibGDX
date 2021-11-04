package ru.nessing.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.pool.EnemyPool;
import ru.nessing.sprites.EnemyAirplane;

public class EnemyEmitter {

    private static final float GENERATE_INTERVAL = 4f;

    private static final float ENEMY_SMALL_HEIGHT = 0.07f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = Rnd.nextFloat(2.5f, 4f);
    private static final int ENEMY_SMALL_HP = 1;

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
        enemySmallRegions = Regions.split(atlas.findRegion("enemyAirplane"), 1, 1, 1);
    }

    private float generateTimer;

    public void generate(float deltaTime) {
        generateTimer += deltaTime;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            EnemyAirplane enemy = enemyPool.obtain();
            enemy.set(
                    enemySmallRegions,
                    enemySmallSpeed,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    enemySmallBulletSpeed,
                    ENEMY_SMALL_BULLET_DAMAGE,
                    ENEMY_SMALL_HP,
                    ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HEIGHT
            );
            enemy.pos.y = Rnd.nextFloat(worldBounds.getTop() - enemy.getHalfHeight(),
                    -0.3f
            );
            enemy.setLeft(worldBounds.getRight());
        }
    }
}
