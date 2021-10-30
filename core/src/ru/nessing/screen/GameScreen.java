package ru.nessing.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.BaseScreen;
import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;
import ru.nessing.sprites.Airplane;
import ru.nessing.sprites.BackButton;
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Cloudy;
import ru.nessing.sprites.ForestBack;

public class GameScreen extends BaseScreen {

    private final Game game;

    private Texture bg, forestTexture;
    private TextureAtlas sky, mainButtons, airplaneAtlas;

    private final Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
    private final Music backMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Single for gameScreen.mp3"));

    private Background background;
    private ForestBack forestBack;
    private ForestBack forestBack2;
    private Cloudy cloudy[];
    private Airplane airplane;

    private BulletPool bulletPool;

    private BackButton backButton;

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
        mainButtons = new TextureAtlas("textures/mainButtonAtlas.pack");
        forestTexture = new Texture("textures/forest.png");
        airplaneAtlas = new TextureAtlas("textures/userAirplaneAtlas.pack");

        background = new Background(bg);
        forestBack = new ForestBack(forestTexture);
        forestBack2 = new ForestBack(forestTexture);

        bulletPool = new BulletPool();

        airplane = new Airplane(airplaneAtlas, "airplaneUser", bulletPool);
        airplane.startSounds();

        cloudy = new Cloudy[16];
        int num = 1;
        for (int i = 0; i < cloudy.length; i++) {
            cloudy[i] = new Cloudy(sky, "cloudy" + num, -0.15f, -0.2f);
            if (num == 4) num = 1;
            else num++;
        }
        backButton = new BackButton(mainButtons, game);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);
        update(deltaTime);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        background.resize(worldBounds);
        for (Cloudy cloudy : cloudy) {
            cloudy.resize(worldBounds);
        }
        forestBack.resize(worldBounds, false);
        forestBack2.resize(worldBounds, true);
        airplane.resize(worldBounds);
        backButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();

        sky.dispose();
        bg.dispose();
        forestTexture.dispose();
        airplaneAtlas.dispose();
        mainButtons.dispose();
        bulletPool.dispose();
        airplane.stopSounds();
        backMusic.dispose();
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
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        airplane.touchUp(touch, pointer, button);
        backButton.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float deltaTime) {
        for (Cloudy cloudy : cloudy) {
            cloudy.update(deltaTime);
        }
        bulletPool.updateActiveObjects(deltaTime);
        airplane.update(deltaTime);
        forestBack.update(deltaTime);
        forestBack2.update(deltaTime);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Cloudy cloudy : cloudy) {
            cloudy.draw(batch);
        }
        forestBack.draw(batch);
        forestBack2.draw(batch);
        bulletPool.drawActiveObjects(batch);
        airplane.draw(batch);
        backButton.draw(batch);
        batch.end();
    }
}
