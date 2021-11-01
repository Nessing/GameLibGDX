package ru.nessing.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.nessing.GameGdx;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GameGdx(), config);
//		config.height = 2000;
//		config.width = 3700;
		config.height = 800;
		config.width = 1200;
	}
}
