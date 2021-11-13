package ru.nessing.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;

public class Explosion extends Sprite {
    private static final float ANIMATE_INTERVAL = 0.017f;
    private float animateTimer;

    private final Sound explosionSound;

    public Explosion(TextureAtlas atlas, String name, Sound explosionSound, int row, int cols, int frames) {
        super(atlas.findRegion(name), row, cols, frames);
//            this.animateTimer = animateTimer;
        this.explosionSound = explosionSound;
    }

    public void set(Vector2 position, float height) {
        this.pos.set(position);
        setHeightProportion(height);
        explosionSound.play(0.8f);
    }

    @Override
    public void update(float deltaTime) {
        animateTimer += deltaTime;
        if (animateTimer >= ANIMATE_INTERVAL) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
