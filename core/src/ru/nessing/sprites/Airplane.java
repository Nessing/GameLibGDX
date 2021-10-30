package ru.nessing.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.Sprite;
import ru.nessing.math.Rect;
import ru.nessing.pool.BulletPool;

public class Airplane extends Sprite {
    private Rect worldBounds;
    private Vector2 direction;
    private final float HEIGHT = 0.1f;
    private final int INVALID_POINTER = -1;
    private final Sound userEngine = Gdx.audio.newSound(Gdx.files.internal("sounds/userEngine.wav"));
    private final Sound userPullUp = Gdx.audio.newSound(Gdx.files.internal("sounds/pullUpAlarm.mp3"));
    private final Sound userShot = Gdx.audio.newSound(Gdx.files.internal("sounds/shotRifle.wav"));
    private final Sound userAlarm = Gdx.audio.newSound(Gdx.files.internal("sounds/alarm.mp3"));

    private final TextureRegion bulletRegion;
    private final BulletPool bulletPool;
    private final Vector2 bulletSpeed;
    private final float bulletHeight;
    private final int damage;

    private float SPEED;
    private Vector2 positionBullet = new Vector2();
    private int upPointer = INVALID_POINTER;
    private int downPointer = INVALID_POINTER;

    private boolean isPullUp = false;
    private boolean isPlayingSound;
    private boolean isPressedLeft;
    private boolean isPressedRight;
    private boolean isPressedDown;
    private boolean isPressedUp;

    public Airplane(TextureAtlas textureAtlas, String name, BulletPool bulletPool) {
        super(textureAtlas.findRegion(name), 2, 1, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = textureAtlas.findRegion("bullet");
        this.bulletSpeed = new Vector2(1f, 0);
        this.bulletHeight = 0.01f;
        this.damage = 1;
        this.direction = new Vector2(0, 0);
        this.pos.set(-0.6f, 0);
        SPEED = 0.3f;
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
        switch (button) {
            case Input.Keys.W :
            case Input.Keys.UP :
                isPressedUp = true;
                moveUp();
                break;
            case Input.Keys.S :
            case Input.Keys.DOWN :
                isPressedDown = true;
                moveDown();
                break;
            case Input.Keys.A :
            case Input.Keys.LEFT :
                isPressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D :
            case Input.Keys.RIGHT :
                isPressedRight = true;
                moveRight();
                break;
            case Input.Keys.NUMPAD_0 :
                shoot();
                break;
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
            case Input.Keys.W :
            case Input.Keys.UP :
                isPressedUp = false;
                if (isPressedDown) moveDown();
                else if (isPressedRight) moveRight();
                else if (isPressedLeft) moveLeft();
                else moveStop();
                break;
            case Input.Keys.S :
            case Input.Keys.DOWN :
                isPressedDown = false;
                if (isPressedUp) moveUp();
                else if (isPressedRight) moveRight();
                else if (isPressedLeft) moveLeft();
                else moveStop();
                break;
            case Input.Keys.A :
            case Input.Keys.LEFT :
                isPressedLeft = false;
                if (isPressedRight) moveRight();
                else if (isPressedUp) moveUp();
                else if (isPressedDown) moveDown();
                else moveStop();
                break;
            case Input.Keys.D :
            case Input.Keys.RIGHT :
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
        if (touch.y > 0) {
            if (upPointer != INVALID_POINTER) return false;
            else upPointer = pointer;
            moveUp();
        } else if (touch.y < 0) {
            if (downPointer != INVALID_POINTER) return false;
            else downPointer = pointer;
            moveDown();
        }
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
        }
        moveStop();
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        if (timerCounter >= 10) {
//            shoot();
//            timerCounter = 0;
//        } else {
//            timerCounter++;
//        }
        pos.mulAdd(direction, deltaTime);
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
        if (isPullUp && !isPlayingSound) {
            userPullUp.loop(0.83f);
            isPlayingSound = true;
        } else if (!isPullUp && isPlayingSound) {
            userPullUp.stop();
            isPlayingSound = false;
        }
    }

    private void moveUp() {
        direction.set(0, SPEED);
    }
    private void moveDown() {
        direction.set(0, -SPEED);
    }
    private void moveRight() {
        direction.set(SPEED, 0);
    }
    private void moveLeft() {
        direction.set(-SPEED, 0);
    }
    private void moveUpAndRight() {
        direction.set(SPEED, SPEED);
    }
    private void moveUpAndLeft() {
        direction.set(-SPEED, SPEED);
    }
    private void moveDownAndRight() {
        direction.set(SPEED, -SPEED);
    }
    private void moveDownAndLeft() {
        direction.set(-SPEED, -SPEED);
    }

    private void moveStop() {
        direction.set(0, 0);
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        positionBullet.set(this.pos.x + 0.15f, this.pos.y);
        bullet.set(this, bulletRegion, this.positionBullet, bulletSpeed, worldBounds, bulletHeight, damage);
        userShot.play(0.4f);
    }

    public void startSounds() {
        userEngine.loop(0.25f);
    }

    public void stopSounds() {
        userEngine.dispose();
        userAlarm.dispose();
        userShot.dispose();
        userPullUp.dispose();
    }

    public void pressButtonStopMove() {
        this.direction.set(0, 0);
    }
}
