package ru.nessing.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.BaseScreen;
import ru.nessing.math.Rect;
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Airplane;
import ru.nessing.sprites.Cloudy;
import ru.nessing.sprites.ForestBack;

public class MenuScreen extends BaseScreen {

    private Texture bg, birdTexture, forestTexture;
    private TextureAtlas sky;

    private Background background;
    private ForestBack forestBack;
    private ForestBack forestBack2;
    private Cloudy cloudy[];
    private Airplane airplane;

    @Override
    public void show() {
        super.show();

        sky = new TextureAtlas("textures/skyAtlas.pack");
        bg = new Texture("textures/skyBack.png");
        forestTexture = new Texture("textures/forest.png");
        birdTexture = new Texture("textures/airplane.png");

        background = new Background(bg);
        forestBack = new ForestBack(forestTexture);
        forestBack2 = new ForestBack(forestTexture);
        airplane = new Airplane(birdTexture);

        cloudy = new Cloudy[16];
        int num = 1;
        for (int i = 0; i < cloudy.length; i++) {
            cloudy[i] = new Cloudy(sky, "cloudy" + num);
            if (num == 4) num = 1;
            else num++;
        }
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
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        update(deltaTime);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();

        sky.dispose();
        bg.dispose();
        forestTexture.dispose();
        birdTexture.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        airplane.touchDown(touch, pointer, button);
        return false;
    }

    private void update(float deltaTime) {
        for (Cloudy cloudy : cloudy) {
            cloudy.update(deltaTime);
        }
        forestBack.update(deltaTime);
        forestBack2.update(deltaTime);
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Cloudy cloudy : cloudy) {
            cloudy.draw(batch);
        }
        forestBack.draw(batch);
        forestBack2.draw(batch);
        airplane.draw(batch);
        batch.end();
    }
}
