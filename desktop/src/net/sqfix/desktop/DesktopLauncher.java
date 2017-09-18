package net.sqfix.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.sqfix.Mario;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.height = 480; config.width = 800;
		config.width = 1200;
		config.height = 624;
		new LwjglApplication(new Mario(), config);
	}
}