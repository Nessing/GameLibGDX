package ru.nessing.pool;

import ru.nessing.base.SpritesPool;
import ru.nessing.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
