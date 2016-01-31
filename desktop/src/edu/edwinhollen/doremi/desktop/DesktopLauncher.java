package edu.edwinhollen.doremi.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import edu.edwinhollen.doremi.DoReMi;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		TexturePacker.process(settings, "images/", ".", "pack");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1440;
		config.height = 900;
		new LwjglApplication(new DoReMi(), config);
	}
}
