package ru.nessing.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;

public class Health extends Sprite {

    private Rect worldBounds;
    private int currentFrame = 3;

    private final float LIGHT_INTERVAL = 0.5f;
    private float lightOn;
    private float lightOff;

    private final float HEIGHT = 0.1f;

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Health(TextureAtlas atlas, String name) {
        super(atlas.findRegion(name), 1, 4, 5);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft());
        setTop(worldBounds.getTop());
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float deltaTime) {
        if (currentFrame == 0) {
            lightOn += deltaTime;
            if (lightOn >= LIGHT_INTERVAL) {
                lightOff += deltaTime;
                if (lightOff >= LIGHT_INTERVAL) {
                    lightOff = 0;
                    lightOn = 0;
                }
                frame = 2;
            } else {
                frame = 0;
            }
        } else {
            frame = currentFrame;
        }
    }
}
