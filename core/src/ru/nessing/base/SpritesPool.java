package ru.nessing.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {
    protected final List<T> activeObjects = new ArrayList<>();
    protected final List<T> freeObjects = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        return object;
    }

    public void updateActiveObjects(float deltaTime) {
        for(T obj : activeObjects) {
            if (!obj.isDestroyed()) obj.update(deltaTime);
        }
    }
    public void drawActiveObjects(SpriteBatch batch) {
        for(T obj : activeObjects) {
            if (!obj.isDestroyed()) obj.draw(batch);
        }
    }

    public void freeAllDestroyed() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T object = activeObjects.get(i);
            if (object.isDestroyed()) {
                free(object);
                i--;
            }
        }
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    private void free(T object) {
        object.flushDestroy();
        if (activeObjects.remove(object)) {
            freeObjects.add(object);
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }
}
