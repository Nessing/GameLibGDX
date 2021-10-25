package ru.nessing.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.nessing.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture backLayer, backCloudy, car, flasher, bird1, bird2;
    private int x, y, timeout, moveCarHeight, timeoutBird;
    private Vector2 touch, speedCloudTop, speedCloudBottom, cloudBottom, cloudTop, userBird, endPointUserBird;
    private Vector2 testImg;
    private boolean down = true;
    private boolean toRight = true;

    @Override
    public void show() {
        super.show();
        backLayer = new Texture("backLayer.jpg");
        backCloudy = new Texture("cloudy.png");
        car = new Texture("car.png");
        flasher = new Texture("flasher.png");
        bird1 = new Texture("bird_1.png");
        bird2 = new Texture("bird_2.png");

        touch = new Vector2();
        cloudTop = new Vector2();
        cloudBottom = new Vector2();
        userBird = new Vector2();
        speedCloudTop = new Vector2(-1, 0);
        speedCloudBottom = new Vector2(-2, 0);
        endPointUserBird = new Vector2();

        testImg = new Vector2(1, 1);
    }

    @Override
    public void render(float v) {
        super.render(v);
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(backLayer, 0, 0, 700, 500);

        batch.draw(backCloudy, testImg.x, testImg.y);

        testImg.scl(1);

        // сравнение координат птицы с местом нажатия мыши
        if (Math.round(userBird.x) == Math.round(touch.x) || Math.round(userBird.y) == Math.round(touch.y)) {
            endPointUserBird.set(0, 0);
        }

        // анимация птицы
        if (timeoutBird == 12) timeoutBird = 0;
        if (timeoutBird < 6) {
            if (userBird.cpy().sub(touch).x < 0) {
                batch.draw(bird1, userBird.x, userBird.y);
            } else batch.draw(bird1, userBird.x, userBird.y, -35, 35);
            timeoutBird++;
        }
        else  {
            if (userBird.cpy().sub(touch).x < 0) {
                batch.draw(bird2, userBird.x - 20, userBird.y - 20);
            } else batch.draw(bird2, userBird.x + 10, userBird.y - 20, -65, 65);
            timeoutBird++;
        }

        userBird.add(endPointUserBird);
        cloudTop.add(speedCloudTop);
        cloudBottom.add(speedCloudBottom);

        // generator clouds top
        if (cloudTop.x == -250) cloudTop.x = 0;

        // cloudy on the top
        batch.draw(backCloudy, cloudTop.x + 50, 300, 100, 100);
        batch.draw(backCloudy, cloudTop.x + 300, 300, 100, 100);
        batch.draw(backCloudy, cloudTop.x + 550, 300, 100, 100);
        batch.draw(backCloudy, cloudTop.x + 800, 300, 100, 100);

        // generator clouds bottom
        if (cloudBottom.x == -250) cloudBottom.x = 0;
        // cloudy on the bottom
        batch.draw(backCloudy, cloudBottom.x + 150, 200, 100, 100);
        batch.draw(backCloudy, cloudBottom.x + 400, 200, 100, 100);
        batch.draw(backCloudy, cloudBottom.x + 650, 200, 100, 100);

        // chose direction car and flasher
        if (toRight) {
            forward(car, flasher, 230, 100, 20);
        } else {
            forward(car, flasher, 170, -100, -20);
        }

        batch.end();

        // chose destination
        if (toRight) x++;
        else x--;

        if (down) {
            moveCarHeight++;
            y++;
        } else {
            moveCarHeight--;
            y--;
        }

        // emulation started of car
        if (moveCarHeight > 3) down = false;
        else if (moveCarHeight <= 0) down = true;

        // change direction
        if (x >= 550) toRight = false;
        else if (x <= -300) toRight = true;
    }

    public void forward(Texture car, Texture flasher, int placeFlasher, int directionCar, int directionFlasher) {
        batch.draw(car, x + 200, 100 - moveCarHeight, directionCar, 50);
        batch.setColor(Color.BLUE);
        batch.draw(flasher, x + placeFlasher, 150 - y, directionFlasher, 10);

        if (x % 2 == 0 || y % 2 == 0) {
            timeout++;
        }
        if (timeout >= 15) {
            if (timeout == 60) {
                timeout = 0;
            } else {
                batch.setColor(Color.RED);
                batch.draw(flasher, x + placeFlasher, 150 - y, directionFlasher, 10);
                timeout++;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        backCloudy.dispose();
        backLayer.dispose();
        car.dispose();
        flasher.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        endPointUserBird.set(touch.cpy().sub(userBird)).nor().scl(1.2f);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
