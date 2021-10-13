package ru.nessing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameGdx extends ApplicationAdapter {
    SpriteBatch batch;
    Texture backLayer, backCloudy, car, flasher;
    TextureRegion backCloudyRegion;
    int x, y, timeout, cloudyMove, moveCarHeight;
    boolean down = true;
    boolean toRight = true;

    @Override
    public void create() {
        batch = new SpriteBatch();
        backLayer = new Texture("backLayer.jpg");
        backCloudy = new Texture("cloudy.png");
        car = new Texture("car.png");
        flasher = new Texture("flasher.png");
        backCloudyRegion = new TextureRegion(backCloudy, 50, 300);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.43f, 0.11f, 0.91f, 0);
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(backLayer, 0, 0, 700, 500);

        // generator clouds
        if (cloudyMove == -250) cloudyMove = 0;

        // cloudy on the top
        batch.draw(backCloudy, cloudyMove + 50, 300, 100, 100);
        batch.draw(backCloudy, cloudyMove + 300, 300, 100, 100);
        batch.draw(backCloudy, cloudyMove + 550, 300, 100, 100);
        batch.draw(backCloudy, cloudyMove + 800, 300, 100, 100);

        // cloudy on the bottom
        batch.draw(backCloudy, cloudyMove + 150, 200, 100, 100);
        batch.draw(backCloudy, cloudyMove + 400, 200, 100, 100);
        batch.draw(backCloudy, cloudyMove + 650, 200, 100, 100);

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
        cloudyMove--;

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
        batch.dispose();
        backCloudy.dispose();
        backLayer.dispose();
        car.dispose();
        flasher.dispose();
    }
}
