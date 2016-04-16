package edu.edwinhollen.doremi.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.edwinhollen.doremi.DoReMi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class DesktopLauncher {
	public static void main (String[] arg) {
		/*
		TexturePacker.Settings settings = new TexturePacker.Settings();
		TexturePacker.process(settings, "images/", ".", "pack");
		*/

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		try {
			Scanner in = new Scanner(new FileReader("version.txt"));
			String v = in.nextLine();
			String version = v.replace("[", "").replace("]", "");
			in.close();

			config.title = String.format("DoReMi - v%s", version);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		config.width = (int) (config.getDesktopDisplayMode().width * 0.5f);
		config.height = (int) (config.getDesktopDisplayMode().height * 0.5f);
		config.samples = 16;

		new LwjglApplication(new DoReMi(), config);
	}
}
