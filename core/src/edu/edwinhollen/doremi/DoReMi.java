package edu.edwinhollen.doremi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class DoReMi extends ApplicationAdapter {
	static SpriteBatch batch;
	static Viewport viewport;
	static Stage currentStage;
	static TextureAtlas sprites;
	static BitmapFont font;
	static Preferences preferences;

	static AssetManager assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		viewport = new ExtendViewport(2560, 1600);

		sprites = new TextureAtlas("pack.atlas");
		assets = new AssetManager();

		font = new BitmapFont(Gdx.files.internal("fonts/font_normal.fnt"));

		preferences = Gdx.app.getPreferences("doremi");

		// set up preferences
		if((!preferences.contains("difficulty"))){
			preferences.putString("difficulty", Puzzle.Difficulty.values()[0].toString());
		}

		if(!preferences.contains("user_id")){
			preferences.putString("user_id", UUID.randomUUID().toString());
		}

		if(!preferences.contains("progress")){
			preferences.putInteger("progress", 0);
		}

		preferences.flush();

		changeStage(TitleStage.class);
	}

	@Override
	public void render () {
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(currentStage != null){
			currentStage.act();
			currentStage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		super.resize(width, height);
	}

	public static void changeStage(final Class<? extends Stage> newStageClass){
		Stage newStage = null;
		try {
			newStage = newStageClass.getConstructor(Viewport.class, Batch.class).newInstance(viewport, batch);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		final float fade = 0.5f;
		if(currentStage != null){
			final Stage finalNewStage = newStage;
			currentStage.addAction(Actions.sequence(
				Actions.fadeOut(fade),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						currentStage.dispose();
						currentStage = null;
						openNewStage(finalNewStage);
					}
				})
			));
		}else{
			openNewStage(newStage);
		}
	}

	public static void addBackButton (Stage stage, Color c){
		addBackButton(stage, c, TitleStage.class);
	}

	public static void addBackButton(Stage fromStage, Color c, final Class<? extends Stage> toStage){
		final Image backButton = new Image(sprites.findRegion("back"));
		float scale = 0.4f;
		backButton.setSize(backButton.getWidth() * scale, backButton.getHeight() * scale);
		backButton.setPosition(viewport.getWorldWidth() * 0.02f, viewport.getWorldHeight() * 0.98f - backButton.getHeight());
		backButton.setColor(c);
		backButton.setOrigin(Align.center);
		backButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				backButton.addAction(Actions.sequence(Actions.scaleTo(0.75f, 0.75f, 0.08f), Actions.scaleTo(1.0f, 1.0f, 0.08f)));
				DoReMi.changeStage(toStage);
				super.tap(event, x, y, count, button);
			}
		});

		fromStage.addActor(backButton);
	}

	public static void openNewStage(Stage newStage){
		newStage.getRoot().setColor(Color.CLEAR);
		newStage.addAction(Actions.sequence(
				Actions.delay(0.25f),
				Actions.fadeIn(0.5f)
		));
		Gdx.input.setInputProcessor(newStage);

		currentStage = newStage;
	}

	@Override
	public void dispose() {
		preferences.flush();
		sprites.dispose();
		batch.dispose();
		currentStage.dispose();
		super.dispose();
	}
}
