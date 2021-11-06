package ru.nessing.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.nessing.base.BaseScreen;
import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;
import ru.nessing.pool.EnemyPool;
import ru.nessing.pool.ExplosionPool;
import ru.nessing.sprites.Airplane;
import ru.nessing.sprites.BackButton;
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Bullet;
import ru.nessing.sprites.Cloudy;
import ru.nessing.sprites.EnemyAirplane;
import ru.nessing.sprites.ExitButton;
import ru.nessing.sprites.ForestBack;
import ru.nessing.sprites.LogoGame;
import ru.nessing.sprites.RestartButton;
import ru.nessing.sprites.StartButton;
import ru.nessing.util.EnemyEmitter;
import ru.nessing.util.Health;

public class GameScreen extends BaseScreen {

    private final Game game;

    private Texture bg, forestTexture, bgEnd, gameOverLogo;
    private TextureAtlas sky, mainButtons, airplaneAtlas, enemyAirplaneAtlas, explosionAir, healthUser;

    private final Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
    private final Music backMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Single for gameScreen.mp3"));
    private final Sound gameOverMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/Single outro for endGame.mp3"));
    private final Sound soundShootEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/shotRifle.wav"));
    private final Sound explosionEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyExplosion.wav"));

    private boolean playGameOverTrack = false;
    private float timerGameOverBack = 0;

    private Background background;
    private Background backgroundEnd;
    private ForestBack forestBack;
    private ForestBack forestBack2;
    private Cloudy cloudy[];

    private Airplane airplane;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private BackButton backButton;

    private EnemyEmitter enemyEmitter;

    private Health health;

    /**
     * game over
     **/
    private ExitButton exitButton;
    private StartButton startButton;
    private RestartButton restartButton;

    private LogoGame logoGameEnd;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        backMusic.play();
        backMusic.setLooping(true);

        sky = new TextureAtlas("textures/skyAtlas.pack");
        bg = new Texture("textures/skyBack.png");
        bgEnd = new Texture("textures/skyBackEnd.png");
        mainButtons = new TextureAtlas("textures/mainButtonAtlas.pack");
        forestTexture = new Texture("textures/forest.png");
        airplaneAtlas = new TextureAtlas("textures/userAirplaneAtlas.pack");
        enemyAirplaneAtlas = new TextureAtlas("textures/enemyAirplaneAtlas.pack");

        healthUser = new TextureAtlas("textures/healthUserAtlas.pack");

        explosionAir = new TextureAtlas("textures/blastAtlas.pack");

        gameOverLogo = new Texture("textures/Hailfront_GameOver.png");

        logoGameEnd = new LogoGame(gameOverLogo);

        background = new Background(bg);
        backgroundEnd = new Background(bgEnd);
        forestBack = new ForestBack(forestTexture);
        forestBack2 = new ForestBack(forestTexture);

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(explosionAir, explosionEnemy);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, soundShootEnemy);

        airplane = new Airplane(airplaneAtlas, "airplaneUser", bulletPool, explosionPool);

        health = new Health(healthUser, "health");

        enemyEmitter = new EnemyEmitter(enemyPool, worldBounds, enemyAirplaneAtlas);

        airplane.startSounds();

        cloudy = new Cloudy[16];
        int num = 1;
        for (int i = 0; i < cloudy.length; i++) {
            cloudy[i] = new Cloudy(sky, "cloudy" + num, -0.15f, -0.2f);
            if (num == 4) num = 1;
            else num++;
        }
        backButton = new BackButton(mainButtons, game);

        /** game over **/
        exitButton = new ExitButton(mainButtons);
        startButton = new StartButton(mainButtons, game);
        restartButton = new RestartButton(mainButtons, game);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);
        update(deltaTime);
        checkCollision();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        background.resize(worldBounds);
        backgroundEnd.resize(worldBounds);
        for (Cloudy cloudy : cloudy) {
            cloudy.resize(worldBounds);
        }
        forestBack.resize(worldBounds, false);
        forestBack2.resize(worldBounds, true);
        airplane.resize(worldBounds);
        health.resize(worldBounds);
        backButton.resize(worldBounds);
        logoGameEnd.resize(worldBounds);
        /** game over **/
        exitButton.resize(worldBounds);
        startButton.resize(worldBounds);
        restartButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();

        sky.dispose();
        bg.dispose();
        bgEnd.dispose();
        forestTexture.dispose();
        airplaneAtlas.dispose();
        enemyAirplaneAtlas.dispose();
        mainButtons.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        explosionAir.dispose();
        airplane.stopSounds();
        healthUser.dispose();
        backMusic.dispose();
        gameOverMusic.dispose();
        explosionEnemy.dispose();
        soundShootEnemy.dispose();
        gameOverLogo.dispose();
    }

    @Override
    public boolean keyDown(int button) {
        airplane.keyDown(button);
        return false;
    }

    @Override
    public boolean keyUp(int button) {
        airplane.keyUp(button);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        airplane.touchDown(touch, pointer, button);
        backButton.touchDown(touch, pointer, button);
        if (backButton.isMe(touch)) {
            clickSound.play();
            airplane.pressButtonStopMove();
        }
        /** game over **/
        exitButton.touchDown(touch, pointer, button);
        restartButton.touchDown(touch, pointer, button);
        if (restartButton.isMe(touch) || exitButton.isMe(touch)) clickSound.play();
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        airplane.touchUp(touch, pointer, button);
        backButton.touchUp(touch, pointer, button);
        /** game over **/
        exitButton.touchUp(touch, pointer, button);
        restartButton.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float deltaTime) {
        for (Cloudy cloudy : cloudy) {
            cloudy.update(deltaTime);
        }
        if (!airplane.isDestroyed()) {
            airplane.update(deltaTime);
            bulletPool.updateActiveObjects(deltaTime);
            enemyPool.updateActiveObjects(deltaTime);
            enemyEmitter.generate(deltaTime);
            if (airplane.getHp() <= 4) {
                health.setCurrentFrame(0);
                health.update(deltaTime);
            } else if (airplane.getHp() <= 10) {
                health.setCurrentFrame(2);
                health.update(deltaTime);
            } else {
                health.setCurrentFrame(3);
                health.update(deltaTime);
            }
        }
        if (airplane.isDestroyed()) {
            backMusic.stop();
            if (!playGameOverTrack) {
                gameOverMusic.play();
                playGameOverTrack = true;
            }
            airplane.stopSounds();
        }
        explosionPool.updateActiveObjects(deltaTime);
        forestBack.update(deltaTime);
        forestBack2.update(deltaTime);
    }

    private void checkCollision() {
        if (airplane.isDestroyed()) {
            return;
        }
        List<EnemyAirplane> enemyAirplanes = enemyPool.getActiveObjects();
        for (EnemyAirplane enemy : enemyAirplanes) {
            if (!enemy.isDestroyed() && !airplane.isOutside(enemy)) {
                enemy.setCheckDirectY(false);
                enemy.damage(100);
                airplane.damage(enemy.getDamage() * 2);
            }
        }

        List<Bullet> bulletsUser = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletsUser) {
            if (bullet.isDestroyed()) continue;
            if (bullet.getOwner() != airplane) {
                if (!airplane.isOutside(bullet)) {
                    airplane.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyAirplane enemy : enemyAirplanes) {
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    enemy.setCheckDirectY(false);
                    bullet.destroy();
                }
            }
        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        if (airplane.isDestroyed()) {
            if (timerGameOverBack <= 1f) {
                timerGameOverBack += 0.001f;
            }
            batch.setColor(1, 1, 1, 1 - timerGameOverBack);
            background.draw(batch);
            batch.setColor(1, 1, 1, timerGameOverBack);
            backgroundEnd.draw(batch);
            batch.setColor(1, 1, 1, 1);
            /** game over **/
        } else {
            background.draw(batch);
        }
        for (Cloudy cloudy : cloudy) {
            cloudy.draw(batch);
        }
        if (!airplane.isDestroyed()) {
            forestBack.draw(batch);
            forestBack2.draw(batch);
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
            airplane.draw(batch);
            health.draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        backButton.draw(batch);
        if (airplane.isDestroyed()) {
            if (timerGameOverBack <= 1f) {
                batch.setColor(1, 1, 1, 1 - timerGameOverBack);
                forestBack.draw(batch);
                forestBack2.draw(batch);
                batch.setColor(1, 1, 1, 1);
            }
            logoGameEnd.draw(batch);
            restartButton.draw(batch);
        }
        batch.end();
    }
}
