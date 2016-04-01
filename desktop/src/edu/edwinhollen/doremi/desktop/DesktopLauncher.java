package edu.edwinhollen.doremi.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.edwinhollen.doremi.DoReMi;

public class DesktopLauncher {
	public static void main (String[] arg) {
		/*
		TexturePacker.Settings settings = new TexturePacker.Settings();
		TexturePacker.process(settings, "images/", ".", "pack");
		*/

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) (config.getDesktopDisplayMode().width * 0.5f);
		config.height = (int) (config.getDesktopDisplayMode().height * 0.5f);
		config.samples = 16;
		new LwjglApplication(new DoReMi(), config);
	}
}
