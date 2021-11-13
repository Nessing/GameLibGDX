package ru.nessing.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.SpritesPool;
import ru.nessing.sprites.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    private final TextureAtlas atlas;

    private final Sound explosionSound;
    private final String name;
    private final int row, cols, frames;

    public ExplosionPool(TextureAtlas atlas, String name, Sound explosionSound, int row, int cols, int frames) {
        this.atlas = atlas;
        this.name = name;
        this.row = row;
        this.cols = cols;
        this.frames = frames;
        this.explosionSound = explosionSound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas, name, explosionSound, row, cols, frames);
    }
}
