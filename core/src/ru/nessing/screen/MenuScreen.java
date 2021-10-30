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
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Cloudy;
import ru.nessing.sprites.ExitButton;
import ru.nessing.sprites.ForestBack;
import ru.nessing.sprites.LogoGame;
import ru.nessing.sprites.StartButton;

public class MenuScreen extends BaseScreen {

    private final Game game;
    private final Sound screenSound = Gdx.audio.newSound(Gdx.files.internal("sounds/startScreen.wav"));
    private final Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
    private final Music backMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Single intro for menuScreen.mp3"));

    private Texture bg, airplaneTexture, forestTexture, hailfront;
    private TextureAtlas sky, mainButtons;

    private Background background;
    private ForestBack forestBack;
    private ForestBack forestBack2;
    private LogoGame logoGame;
    private Cloudy cloudy[];

    private ExitButton exitButton;
    private StartButton startButton;

    private int timeout = 0;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        screenSound.loop(0.5f);
        backMusic.play();
        backMusic.setVolume(1);
        backMusic.setLooping(true);

        sky = new TextureAtlas("textures/skyAtlas.pack");
        mainButtons = new TextureAtlas("textures/mainButtonAtlas.pack");
        bg = new Texture("textures/skyBackMenu.png");
        forestTexture = new Texture("textures/forestMenu.png");
        airplaneTexture = new Texture("textures/airplane.png");
        hailfront = new Texture("textures/Hailfront.png");

        background = new Background(bg);
        forestBack = new ForestBack(forestTexture);
        forestBack2 = new ForestBack(forestTexture);
        logoGame = new LogoGame(hailfront);

        cloudy = new Cloudy[16];
        int num = 1;
        for (int i = 0; i < cloudy.length; i++) {
            cloudy[i] = new Cloudy(sky, "cloudy" + num, -0.05f, -0.10f);
            if (num == 4) num = 1;
            else num++;
        }

        exitButton = new ExitButton(mainButtons);
        startButton = new StartButton(mainButtons, game);

    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        update(deltaTime);
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
        logoGame.resize(worldBounds);
        exitButton.resize(worldBounds);
        startButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();

        screenSound.stop();
        screenSound.dispose();
        sky.dispose();
        mainButtons.dispose();
        bg.dispose();
        forestTexture.dispose();
        hailfront.dispose();
        airplaneTexture.dispose();
        backMusic.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        exitButton.touchDown(touch, pointer, button);
        startButton.touchDown(touch, pointer, button);
        if (startButton.isMe(touch) || exitButton.isMe(touch)) clickSound.play();
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        exitButton.touchUp(touch, pointer, button);
        startButton.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float deltaTime) {
        for (Cloudy cloudy : cloudy) {
            cloudy.update(deltaTime);
        }
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Cloudy cloudy : cloudy) {
            cloudy.draw(batch);
        }
        forestBack.draw(batch);
        forestBack2.draw(batch);
        if (timeout == 1130) {
            logoGame.draw(batch);
        } else {
            timeout++;
        }
        exitButton.draw(batch);
        startButton.draw(batch);
        batch.end();
    }
}
