package ru.nessing.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.BaseScreen;
import ru.nessing.math.Rect;
import ru.nessing.sprites.Background;
import ru.nessing.sprites.Bird;

public class MenuScreen extends BaseScreen {

    private Texture bg, birdTexture;

    private Background background;
    private Bird bird;

    @Override
    public void show() {
        super.show();

        bg = new Texture("textures/background.jpg");
        birdTexture = new Texture("textures/bird_1.png");
        background = new Background(bg);
        bird = new Bird(birdTexture);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        background.resize(worldBounds);
        bird.resize(worldBounds);
    }

    @Override
    public void render(float v) {
        super.render(v);

        batch.begin();
        background.draw(batch);
        bird.draw(batch, 0.08f, 0.08f);

        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();

        bg.dispose();
        birdTexture.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        bird.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }
}
