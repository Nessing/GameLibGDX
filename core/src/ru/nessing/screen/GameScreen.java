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
import ru.nessing.base.Font;
import ru.nessing.math.Rect;
import ru.nessing.math.Rnd;
import ru.nessing.pool.BulletPool;
import ru.nessing.pool.EnemyPool;
import ru.nessing.pool.EnemyLandPool;
import ru.nessing.pool.ExplosionPool;
import ru.nessing.pool.HealthPool;
import ru.nessing.pool.SuperShotPool;
import ru.nessing.sprites.Airplane;
import ru.nessing.sprites.BackButton;
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Bullet;
import ru.nessing.sprites.Cloudy;
import ru.nessing.sprites.EnemyAirplane;
import ru.nessing.sprites.EnemyPanzer;
import ru.nessing.sprites.ExitButton;
import ru.nessing.sprites.ForestBack;
import ru.nessing.sprites.HealthUp;
import ru.nessing.sprites.LogoGame;
import ru.nessing.sprites.RestartButton;
import ru.nessing.sprites.StartButton;
import ru.nessing.sprites.SuperShotGet;
import ru.nessing.util.EnemyEmitter;
import ru.nessing.util.EnemyLandEmitter;
import ru.nessing.util.Health;
import ru.nessing.util.HealthEmitter;
import ru.nessing.util.SuperShotEmitter;

public class GameScreen extends BaseScreen {

    private static final String FRAGS = "Frags: ";
    private static final String LEVEL = "LEVEL: ";
    private static final float FONT_SIZE = 0.04f;
    private static final float TIMER_SUPER_SHOT = 4f;

    private final Game game;

    private Texture bg, forestTexture, bgEnd, gameOverLogo;
    private TextureAtlas sky, mainButtons, airplaneAtlas,
            enemyAirplaneAtlas, explosionAir, UserComponents, enemyPanzerAtlas;

    private final Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
    private final Music backMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Single for gameScreen.mp3"));
    private final Music gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Single outro for endGame.mp3"));
    private final Sound soundShootEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/shotRifle.wav"));
    private final Sound soundHP = Gdx.audio.newSound(Gdx.files.internal("sounds/takeHp.wav"));
    private final Sound explosionEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyExplosion.wav"));
    private final Sound explosionUser = Gdx.audio.newSound(Gdx.files.internal("sounds/largeExplosion.wav"));

    private final Sound levelUp = Gdx.audio.newSound(Gdx.files.internal("sounds/levelUp.wav"));
    private final Sound getMinigun = Gdx.audio.newSound(Gdx.files.internal("sounds/takeMinigun.wav"));

    private final Sound soundHit1 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet-flyby-1.wav"));
    private final Sound soundHit2 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet-flyby-2.wav"));
    private final Sound soundHit3 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet-flyby-3.wav"));
    private final Sound soundHit4 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet-flyby-4.wav"));
    private final Sound soundHit5 = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet-flyby-5.wav"));

    private final Sound[] soundsHit = new Sound[]{soundHit1, soundHit2, soundHit3, soundHit4, soundHit5};

    private boolean playGameOverTrack = true;
    private boolean isSuperShot = false;
    private float timerSuperShot = 0;
    private float timerGameOverBack = 0;
    private float timerDarkSky = 1;
    private int countDayNight = 0;

    private int checkNextLevel = 5;

    private int randomHitSound;
    private int frags;

    private StringBuilder sbFrags;
    private StringBuilder sbLevel;

    private Font font;
    private Font fontLevel;

    private Background background;
    private Background backgroundEnd;
    private ForestBack forestBack;
    private ForestBack forestBack2;
    private Cloudy cloudy[];

    private Airplane airplane;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyLandPool enemyTankPool;
    private ExplosionPool explosionPool;
    private ExplosionPool explosionTankPool;
    private HealthPool healthPool;
    private SuperShotPool superShotPool;

    private BackButton backButton;

    private EnemyEmitter enemyEmitter;
    private EnemyLandEmitter enemyLandEmitter;
    private HealthEmitter healthEmitter;
    private SuperShotEmitter superShotEmitter;

    private Health health;

    /**
     * game over
     **/
    private ExitButton exitButton;
    private StartButton startButton;
    private RestartButton restartButton;

    private LogoGame logoGameEnd;

    private boolean largeBoom = false;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        gameOverMusic.setVolume(1);
        gameOverMusic.setLooping(false);

        backMusic.setVolume(1);
        backMusic.setLooping(true);
        backMusic.play();

        sky = new TextureAtlas("textures/skyAtlas.pack");
        bg = new Texture("textures/skyBack.png");
        bgEnd = new Texture("textures/skyBackEnd.png");
        mainButtons = new TextureAtlas("textures/mainButtonAtlas.pack");
        forestTexture = new Texture("textures/forest.png");
        airplaneAtlas = new TextureAtlas("textures/userAirplaneAtlas.pack");
        enemyAirplaneAtlas = new TextureAtlas("textures/enemyAirplaneAtlas.pack");
        enemyPanzerAtlas = new TextureAtlas("textures/panzerAtlas.pack");

        UserComponents = new TextureAtlas("textures/healthUserAtlas.pack");

        explosionAir = new TextureAtlas("textures/blastAtlas.pack");

        gameOverLogo = new Texture("textures/Hailfront_GameOver.png");

        logoGameEnd = new LogoGame(gameOverLogo);

        background = new Background(bg);
        backgroundEnd = new Background(bgEnd);
        forestBack = new ForestBack(forestTexture);
        forestBack2 = new ForestBack(forestTexture);

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(explosionAir, "blast_air", explosionEnemy, 6, 6, 34);
        explosionTankPool = new ExplosionPool(explosionAir, "blast_ground", explosionEnemy, 3, 5, 15);

        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, soundShootEnemy);
        enemyTankPool = new EnemyLandPool(bulletPool, explosionTankPool, worldBounds, soundShootEnemy);
        healthPool = new HealthPool(worldBounds, soundHP);
        superShotPool = new SuperShotPool(worldBounds, getMinigun);

        airplane = new Airplane(airplaneAtlas, "airplaneUser", bulletPool, explosionPool);

        health = new Health(UserComponents, "health");

        enemyEmitter = new EnemyEmitter(enemyPool, worldBounds, enemyAirplaneAtlas);
        enemyLandEmitter = new EnemyLandEmitter(enemyTankPool, worldBounds, enemyPanzerAtlas);

        healthEmitter = new HealthEmitter(healthPool, worldBounds, UserComponents);
        superShotEmitter = new SuperShotEmitter(superShotPool, worldBounds, UserComponents);

        airplane.startSounds();

        cloudy = new Cloudy[16];
        int num = 1;
        for (int i = 0; i < cloudy.length; i++) {
            cloudy[i] = new Cloudy(sky, "cloudy" + num, -0.15f, -0.2f);
            if (num == 4) num = 1;
            else num++;
        }
        backButton = new BackButton(mainButtons, game);

        sbFrags = new StringBuilder();
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);

        sbLevel = new StringBuilder();
        fontLevel = new Font("font/font.fnt", "font/font.png");
        fontLevel.setSize(FONT_SIZE);

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
        enemyPanzerAtlas.dispose();
        mainButtons.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        enemyTankPool.dispose();
        explosionPool.dispose();
        explosionTankPool.dispose();
        healthPool.dispose();
        superShotPool.dispose();
        explosionAir.dispose();
        airplane.stopSounds();
        UserComponents.dispose();
        backMusic.dispose();
        gameOverMusic.dispose();
        explosionEnemy.dispose();
        explosionUser.dispose();
        soundShootEnemy.dispose();
        soundHP.dispose();
        getMinigun.dispose();
        levelUp.dispose();
        soundHit1.dispose();
        soundHit2.dispose();
        soundHit3.dispose();
        soundHit4.dispose();
        soundHit5.dispose();

        levelUp.dispose();
        getMinigun.dispose();

        font.dispose();
        fontLevel.dispose();

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
        if (airplane.isDestroyed()) {
            restartButton.touchDown(touch, pointer, button);
            if (restartButton.isMe(touch) || exitButton.isMe(touch)) clickSound.play();
        }
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

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        airplane.touchDragged(touch, pointer);
        return false;
    }

    private void update(float deltaTime) {
        for (Cloudy cloudy : cloudy) {
            cloudy.update(deltaTime);
        }
        if (!airplane.isDestroyed()) {
            randomHitSound = Rnd.getRandomInteger(0, 4);
            airplane.update(deltaTime);
            bulletPool.updateActiveObjects(deltaTime);
            enemyPool.updateActiveObjects(deltaTime);
            enemyTankPool.updateActiveObjects(deltaTime);
            if (airplane.isLevelUp()) {
                airplane.setLevelUp(false);
                enemyEmitter.speedUpGenerate();
                forestBack.setSpeedUp(0.1f);
                forestBack2.setSpeedUp(0.1f);
                countDayNight++;
                if (countDayNight == 4) {
                    countDayNight = 0;
                }
                if (airplane.getLevel() > 5) {
                    enemyLandEmitter.speedUpGenerate();
                }
            }
            enemyEmitter.generate(deltaTime);
            if (airplane.getLevel() >= 5) {
                enemyLandEmitter.generate(deltaTime);
            }
            healthEmitter.generate(deltaTime);
            superShotEmitter.generate(deltaTime);
            if (isSuperShot) {
                if (timerSuperShot < TIMER_SUPER_SHOT) {
                    timerSuperShot += deltaTime;
                } else {
                    timerSuperShot = 0;
                    airplane.setReloadInterval(airplane.getReloadInterval() + 0.2f);
                    isSuperShot = false;
                }
            }
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
        forestBack.update(deltaTime);
        forestBack2.update(deltaTime);
        if (airplane.isDestroyed()) {
            if (!largeBoom) {
                largeBoom = true;
                explosionUser.play();
            }
            airplane.setDestroy(true);
            backMusic.stop();
            if (playGameOverTrack) {
                gameOverMusic.play();
                playGameOverTrack = false;
            }
            airplane.stopSounds();
        }
        explosionPool.updateActiveObjects(deltaTime);
        explosionTankPool.updateActiveObjects(deltaTime);
        healthPool.updateActiveObjects(deltaTime);
        superShotPool.updateActiveObjects(deltaTime);
    }

    private void checkCollision() {
        if (airplane.isDestroyed()) {
            return;
        }
        List<EnemyAirplane> enemyAirplanes = enemyPool.getActiveObjects();
        List<EnemyPanzer> enemyPanzers = enemyTankPool.getActiveObjects();
        for (EnemyAirplane enemy : enemyAirplanes) {
            if (!enemy.isDestroyed() && !airplane.isOutside(enemy)) {
                enemy.setCheckDirectY(false);
                enemy.damage(100);
                airplane.damage(enemy.getDamage() * 2);
            }
        }

        for (EnemyPanzer panzer : enemyPanzers) {
            if (!panzer.isDestroyed() && !airplane.isOutside(panzer)) {
                panzer.setCheckDirectY(false);
                panzer.damage(100);
                airplane.damage(panzer.getDamage() * 2);
            }
        }

        List<Bullet> bulletsUser = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletsUser) {
            if (bullet.isDestroyed()) continue;
            if (bullet.getOwner() != airplane) {
                if (!airplane.isOutside(bullet)) {
                    airplane.damage(bullet.getDamage());
                    bullet.destroy();
                    soundsHit[randomHitSound].play(0.7f);
                }
                continue;
            }
            for (EnemyAirplane enemy : enemyAirplanes) {
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    enemy.setCheckDirectY(false);
                    if (enemy.isDestroyed()) {
                        frags += 1;
                    }
                    if (frags >= checkNextLevel) {
                        airplane.setLevel(airplane.getLevel() + 1);
                        checkNextLevel += 5;
                        levelUp.play();
                        airplane.setLevelUp(true);
                    }
                    bullet.destroy();
                    soundsHit[randomHitSound].play(0.7f);
                }
            }

            for (EnemyPanzer panzer : enemyPanzers) {
                if (panzer.isBulletCollision(bullet)) {
                    panzer.damage(bullet.getDamage());
                    panzer.setCheckDirectY(false);
                    if (panzer.isDestroyed()) {
                        frags += 1;
                    }
                    if (frags >= checkNextLevel) {
                        airplane.setLevel(airplane.getLevel() + 1);
                        checkNextLevel += 10;
                        airplane.setLevelUp(true);
                    }
                    bullet.destroy();
                    soundsHit[randomHitSound].play(0.7f);
                }
            }
        }

        List<HealthUp> healths = healthPool.getActiveObjects();
        for (HealthUp health : healths) {
            if (!health.isDestroyed() && !airplane.isOutside(health)) {
                health.setCheckDirectY(false);
                health.damage(100);
                airplane.setHp(airplane.getHp() + 2);
            }
        }

        List<SuperShotGet> superShot = superShotPool.getActiveObjects();
        for (SuperShotGet health : superShot) {
            if (!health.isDestroyed() && !airplane.isOutside(health)) {
                health.setCheckDirectY(false);
                health.damage(100);
                isSuperShot = true;
                getMinigun.play();
                airplane.setReloadInterval(airplane.getReloadInterval() - 0.2f);
            }
        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        enemyTankPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        explosionTankPool.freeAllDestroyed();
        healthPool.freeAllDestroyed();
        superShotPool.freeAllDestroyed();
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
            for (Cloudy cloudy : cloudy) {
                cloudy.draw(batch);
            }
            /** game over **/
        } else {
            background.draw(batch);
            for (Cloudy cloudy : cloudy) {
                cloudy.draw(batch);
            }
        }
        if (!airplane.isDestroyed()) {
            if (countDayNight >= 2) {
                if (timerDarkSky >= 0.3f) {
                    timerDarkSky -= 0.001f;
                    batch.setColor(timerDarkSky, timerDarkSky, timerDarkSky, 1);
                }
            } else {
                if (timerDarkSky <= 1f) {
                    timerDarkSky += 0.001f;
                    batch.setColor(timerDarkSky, timerDarkSky, timerDarkSky, 1);
                }
            }
            background.draw(batch);
            for (Cloudy cloudy : cloudy) {
                cloudy.draw(batch);
            }
            forestBack.draw(batch);
            forestBack2.draw(batch);
            batch.setColor(1, 1, 1, 1);
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
            enemyTankPool.drawActiveObjects(batch);
            airplane.draw(batch);
            health.draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        explosionTankPool.drawActiveObjects(batch);
        healthPool.drawActiveObjects(batch);
        superShotPool.drawActiveObjects(batch);
        backButton.draw(batch);
        if (airplane.isDestroyed()) {
            airplane.setDestroy(true);
            if (timerGameOverBack <= 1f) {
                batch.setColor(timerDarkSky, timerDarkSky, timerDarkSky, 1 - timerGameOverBack);
                forestBack.draw(batch);
                forestBack2.draw(batch);
                batch.setColor(1, 1, 1, 1);
            }
            logoGameEnd.draw(batch);
            restartButton.draw(batch);
        }
        printInfo();
        batch.setColor(timerDarkSky, timerDarkSky, timerDarkSky, 1);
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + 0.5f, worldBounds.getTop() - 0.03f);
        fontLevel.draw(batch, sbLevel.append(LEVEL).append(airplane.getLevel()), worldBounds.getLeft() + worldBounds.getRight(), worldBounds.getTop() - 0.03f);
    }
}
