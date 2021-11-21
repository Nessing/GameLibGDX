package ru.nessing.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Ship;
import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;
import ru.nessing.pool.ExplosionPool;

public class Airplane extends Ship {

    private final float HEIGHT = 0.1f;
    private final int INVALID_POINTER = -1;
    private final Sound userEngine = Gdx.audio.newSound(Gdx.files.internal("sounds/userEngine.wav"));
    private final Sound userPullUp = Gdx.audio.newSound(Gdx.files.internal("sounds/pullUpAlarm.mp3"));
    //    private final Sound soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shotRifle.wav"));
    private final Sound userAlarm = Gdx.audio.newSound(Gdx.files.internal("sounds/alarm.mp3"));



    private int upPointer = INVALID_POINTER;
    private int downPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private int leftPointer = INVALID_POINTER;

    private boolean touchUp = false;
    private boolean leftRightDrag = false;
    private Vector2 startPos = new Vector2();

    private static int level = 1;
    private boolean isLevelUp = false;

    private float lowHp = -0.05f;
    private float overLowHp = -0.1f;

    private boolean isPullUp = false;
    private boolean isLowHealth = false;
    private boolean isDestroy = false;
    private boolean isMoveStop = true;

    private boolean isPlayingAlarm;
    private boolean isPlayingSound;
    private boolean isPressedLeft;
    private boolean isPressedRight;
    private boolean isPressedDown;
    private boolean isPressedUp;

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        Airplane.level = level;
    }

    public boolean isLevelUp() {
        return isLevelUp;
    }

    public void setLevelUp(boolean levelUp) {
        isLevelUp = levelUp;
        hp += 2;
        this.reloadInterval -= 0.01f;
    }

    public void setReloadInterval(float interval) {
        this.reloadInterval = interval;
    }

    public float getReloadInterval() {
        return this.reloadInterval;
    }

    public Airplane(TextureAtlas textureAtlas, String name, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(textureAtlas.findRegion(name), 2, 1, 2);
        this.reloadInterval = 0.4f;
        this.explosionPool = explosionPool;
        this.soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shotRifle.wav"));
        this.positionBullet = new Vector2();
        this.bulletPool = bulletPool;
        this.bulletRegion = textureAtlas.findRegion("bullet");
        this.bulletSpeed = new Vector2(1f, 0);
        this.bulletHeight = 0.01f;
        this.damage = 1;
        this.hp = 20;
        this.direction = new Vector2();
        this.pos.set(-0.6f, 0);
        this.speed = 0.3f;
        this.volumeShoot = 0.05f;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        this.worldBounds = worldBounds;
    }

    // 51 ↑  47 ↓  29 ←  32 →
    @Override
    public boolean keyDown(int button) {
        isMoveStop = false;
        switch (button) {
            case Input.Keys.W:
            case Input.Keys.UP:
                isPressedUp = true;
                moveUp();
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                isPressedDown = true;
                moveDown();
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isPressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isPressedRight = true;
                moveRight();
                break;
//            case Input.Keys.NUMPAD_0:
//                shoot();
//                break;
        }
        if (isPressedUp && isPressedRight) moveUpAndRight();
        if (isPressedUp && isPressedLeft) moveUpAndLeft();
        if (isPressedDown && isPressedRight) moveDownAndRight();
        if (isPressedDown && isPressedLeft) moveDownAndLeft();
        return false;
    }

    @Override
    public boolean keyUp(int button) {
        switch (button) {
            case Input.Keys.W:
            case Input.Keys.UP:
                isPressedUp = false;
                if (isPressedDown) moveDown();
                else if (isPressedRight) moveRight();
                else if (isPressedLeft) moveLeft();
                else moveStop();
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                isPressedDown = false;
                if (isPressedUp) moveUp();
                else if (isPressedRight) moveRight();
                else if (isPressedLeft) moveLeft();
                else moveStop();
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isPressedLeft = false;
                if (isPressedRight) moveRight();
                else if (isPressedUp) moveUp();
                else if (isPressedDown) moveDown();
                else moveStop();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isPressedRight = false;
                if (isPressedLeft) moveLeft();
                else if (isPressedUp) moveUp();
                else if (isPressedDown) moveDown();
                else moveStop();
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
//        if (touch.y > 0.2f && touch.x < 0) {
//            if (upPointer != INVALID_POINTER) return false;
//            else upPointer = pointer;
//            moveUp();
//        } else if (touch.y < -0.2f && touch.x < 0) {
//            if (downPointer != INVALID_POINTER) return false;
//            else downPointer = pointer;
//            moveDown();
//        } else if (touch.x > 0) {
//            if (rightPointer != INVALID_POINTER) return false;
//            else rightPointer = pointer;
//            moveRight();
//        }
//        else if (touch.x < 0) {
//            if (leftPointer != INVALID_POINTER) return false;
//            else leftPointer = pointer;
//            moveLeft();
//        }
        startPos.set(touch.x, touch.y);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {

        if (pointer == upPointer) {
            upPointer = INVALID_POINTER;
            if (downPointer != INVALID_POINTER) moveDown();
            else moveStop();
        } else if (pointer == downPointer) {
            downPointer = INVALID_POINTER;
            if (upPointer != INVALID_POINTER) moveUp();
            else moveStop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) moveLeft();
            else moveStop();
        } else if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) moveRight();
            else moveStop();
        }
        moveStop();
        leftRightDrag = false;
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
//        if (!touchUp) {
//            if (!leftRightDrag) {
//                if (startPos.y < touch.y - 0.055f) moveUp();
//                else if (startPos.y > touch.y + 0.055f) moveDown();
//                else if (startPos.x < touch.x - 0.055f) moveRight();
//                else if (startPos.x > touch.x + 0.055f) moveLeft();
//            }
//        } else moveStop();
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isMoveStop) {
            if (hp <= 4) {
                direction.y = overLowHp;
            } else if (hp <= 10) {
                direction.y = lowHp;
            }
        }
        positionBullet.set(pos.x + 0.2f, pos.y);
        if (getLeft() <= worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
        }
        if (getRight() >= worldBounds.getRight()) {
            setRight(worldBounds.getRight());
        }
        if (getTop() >= worldBounds.getTop()) {
            setTop(worldBounds.getTop());
        }
        if (getBottom() <= worldBounds.getBottom()) {
            setBottom(worldBounds.getBottom());
        }

        if (getBottom() <= worldBounds.getBottom() + 0.2f) {
            isPullUp = true;
        } else {
            isPullUp = false;
        }

        if (!isDestroy) {
            if (isPullUp && !isPlayingSound) {
                userPullUp.loop(0.83f);
                isPlayingSound = true;
            } else if (!isPullUp && isPlayingSound) {
                userPullUp.stop();
                isPlayingSound = false;
            }
            if (getBottom() <= worldBounds.getBottom()) {
                damage(1);
            }
            if (hp <= 4) {
                isLowHealth = true;
            } else {
                isLowHealth = false;
            }
            if (isLowHealth && !isPlayingAlarm) {
                userAlarm.loop(0.2f);
                isPlayingAlarm = true;
            } else if (!isLowHealth && isPlayingAlarm) {
                userAlarm.stop();
                isPlayingAlarm = false;
            }
        } else {
            userAlarm.stop();
            userPullUp.stop();
        }
    }

    private void moveUp() {
        direction.set(0, speed);
    }

    private void moveDown() {
        direction.set(0, -speed);
    }

    private void moveRight() {
        if (hp <= 4) {
            direction.set(speed, overLowHp);
        } else if (hp <= 10) {
            direction.set(speed, lowHp);
        } else {
            direction.set(speed, 0);
        }
    }

    private void moveLeft() {
        if (hp <= 4) {
            direction.set(-speed, overLowHp);
        } else if (hp <= 10) {
            direction.set(-speed, lowHp);
        } else {
            direction.set(-speed, 0);
        }
    }

    private void moveUpAndRight() {
        direction.set(speed, speed);
    }

    private void moveUpAndLeft() {
        direction.set(-speed, speed);
    }

    private void moveDownAndRight() {
        direction.set(speed, -speed);
    }

    private void moveDownAndLeft() {
        direction.set(-speed, -speed);
    }

    private void moveStop() {
        direction.set(0, 0);
        startPos.set(0, 0);
        isMoveStop = true;
        touchUp = false;
    }

    public void startSounds() {
        userEngine.loop(0.35f);
    }

    public void stopSounds() {
        userAlarm.stop();
        userPullUp.stop();
        userEngine.dispose();
        userAlarm.dispose();
        soundShoot.dispose();
        userPullUp.dispose();
    }

    public void pressButtonStopMove() {
        this.direction.set(0, 0);
    }
}
