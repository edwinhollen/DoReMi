package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fubar on 2/8/16.
 */
public class TitleStage extends Stage {
    private AssetManager assetManager;
    private HorizontalGroup menuItems;

    // asset descriptions
    private AssetDescriptor<Music> warmupMusic;

    public TitleStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.assetManager = new AssetManager();
        this.warmupMusic = new AssetDescriptor<Music>("sounds/warmup.mp3", Music.class);
        assetManager.load(warmupMusic);

        Image blank = new Image(DoReMi.sprites.findRegion("blank"));
        Group playGroup = new Group(),
                optionsGroup = new Group(),
                infoGroup = new Group(),
                statsGroup = new Group();

        this.menuItems = new HorizontalGroup();
        this.menuItems.addActor(playGroup);
        this.menuItems.addActor(optionsGroup);
        this.menuItems.addActor(infoGroup);
        this.menuItems.addActor(statsGroup);

        setDebugAll(true);

        for(Actor a : menuItems.getChildren()){
            ((Group) a).setSize(blank.getWidth(), blank.getHeight());
            ((Group) a).addActor(blank);
        }

        {
            Image playIcon = new Image(DoReMi.sprites.findRegion("play"));
            playIcon.setOrigin(Align.center);
            playIcon.setAlign(Align.center);
            playIcon.setPosition(playGroup.getWidth() / 2 - playIcon.getWidth() / 2, playGroup.getHeight() / 2 - playIcon.getWidth() / 2);
            playGroup.addActor(playIcon);
        }

        {
            Image optionsIcon = new Image(DoReMi.sprites.findRegion("wrench"));
            optionsIcon.setOrigin(Align.center);
            optionsIcon.setRotation(45f);
            optionsGroup.addActor(optionsIcon);
        }

        {
            Image infoIcon = new Image(DoReMi.sprites.findRegion("info"));
            infoGroup.addActor(infoIcon);
        }

        {
            Image statsIcon = new Image(DoReMi.sprites.findRegion("stats"));
            statsGroup.addActor(statsIcon);
        }

        menuItems.setHeight(blank.getHeight());
        for(Actor a : menuItems.getChildren()){
            menuItems.setSize(menuItems.getWidth() + a.getWidth(), menuItems.getHeight());
        }

        this.menuItems.setPosition(viewport.getWorldWidth() / 2f - this.menuItems.getWidth() / 2, viewport.getWorldHeight() / 2f);

        addActor(menuItems);
    }

    @Override
    public void act() {
        super.act();
        this.assetManager.update();

        if(assetManager.isLoaded(warmupMusic.fileName, warmupMusic.type)){
            Music warmupMusic = assetManager.get(this.warmupMusic);
            if(!warmupMusic.isPlaying()){
                warmupMusic.setVolume(0);
                warmupMusic.play();
            }
            warmupMusic.setVolume(Math.min(1.0f, warmupMusic.getVolume() + (0.01f * Gdx.graphics.getDeltaTime())));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }
}
