package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collections;

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
        this.warmupMusic = new AssetDescriptor<Music>("sounds/warmupfades.mp3", Music.class);
        assetManager.load(warmupMusic);

        // logo
        Group logoGroup = new Group();
        logoGroup.setSize(viewport.getWorldWidth(), viewport.getWorldHeight() * 0.4f);
        logoGroup.setPosition(0, viewport.getWorldHeight() * 0.5f);
        {
            Group doReMiGroup = new Group();
            doReMiGroup.setSize(logoGroup.getWidth() * 0.65f, logoGroup.getHeight());
            doReMiGroup.setPosition(logoGroup.getWidth() * 0.5f, 0, Align.bottom);
            Image logoDo = new Image(DoReMi.sprites.findRegion("logo-do"));
            Image logoRe = new Image(DoReMi.sprites.findRegion("logo-re"));
            Image logoMi = new Image(DoReMi.sprites.findRegion("logo-mi"));

            logoDo.setPosition(0, 0);
            logoRe.setPosition(doReMiGroup.getWidth() * 0.33f, 0);
            logoMi.setPosition(doReMiGroup.getWidth() * 0.65f, 0);

            doReMiGroup.addActor(logoDo);
            doReMiGroup.addActor(logoRe);
            doReMiGroup.addActor(logoMi);

            logoGroup.addActor(doReMiGroup);

            for(int i = 0; i < doReMiGroup.getChildren().size; i++){
                Actor a = doReMiGroup.getChildren().get(i);
                a.setOrigin(Align.center);
                a.addAction(Actions.sequence(
                    Actions.scaleTo(0, 0),
                    Actions.delay(0.5f + 0.25f * i),
                    Actions.scaleTo(1.5f, 1.5f, 0.05f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, 0.5f, 1.0f);
                        }
                    }),
                    Actions.scaleTo(1.0f, 1.0f, 0.2f)
                ));
            }
        }

        {
            Group logoNotesGroup = new Group();
            logoNotesGroup.setSize(logoGroup.getWidth(), logoGroup.getHeight());
            logoNotesGroup.setPosition(0, 0);

            Image np1 = new Image(DoReMi.sprites.findRegion("logonote1"));
            np1.setPosition(logoNotesGroup.getWidth() * 0.1f, logoNotesGroup.getHeight() * 0.55f);
            np1.setSize(np1.getWidth() * 1.5f, np1.getHeight() * 1.5f);
            logoNotesGroup.addActor(np1);

            Image np2 = new Image(DoReMi.sprites.findRegion("logonote2"));
            np2.setPosition(logoNotesGroup.getWidth() * 0.5f, logoNotesGroup.getHeight() * 1.0f);
            np2.setSize(np2.getWidth() * 1.6f, np2.getHeight() * 1.6f);
            logoNotesGroup.addActor(np2);

            Image np3 = new Image(DoReMi.sprites.findRegion("logonote3"));
            np3.setPosition(logoNotesGroup.getWidth() * 0.8f, logoNotesGroup.getHeight() * 0.9f);
            np3.setSize(np3.getWidth() * 1.6f, np3.getHeight() * 1.6f);
            logoNotesGroup.addActor(np3);

            Image np4 = new Image(DoReMi.sprites.findRegion("logonote4"));
            np4.setPosition(logoNotesGroup.getWidth() * 0.89f, logoNotesGroup.getHeight() * 0.4f);
            np4.setSize(np4.getWidth() * 1.6f, np4.getHeight() * 1.6f);
            logoNotesGroup.addActor(np4);

            Image np5 = new Image(DoReMi.sprites.findRegion("logonote5"));
            np5.setPosition(logoNotesGroup.getWidth() * 0.75f, logoNotesGroup.getHeight() * -0.35f);
            np5.setSize(np5.getWidth() * 1.6f, np5.getHeight() * 1.6f);
            logoNotesGroup.addActor(np5);

            Image np6 = new Image(DoReMi.sprites.findRegion("logonote6"));
            np6.setPosition(logoNotesGroup.getWidth() * 0.28f, logoNotesGroup.getHeight() * -0.35f);
            np6.setSize(np6.getWidth() * 1.6f, np6.getHeight() * 1.6f);
            logoNotesGroup.addActor(np6);

            Image np7 = new Image(DoReMi.sprites.findRegion("logonote7"));
            np7.setPosition(logoNotesGroup.getWidth() * 0.13f, logoNotesGroup.getHeight() * -0.05f);
            np7.setSize(np7.getWidth() * 1.8f, np7.getHeight() * 1.8f);
            logoNotesGroup.addActor(np7);

            for(int i = 0; i < logoNotesGroup.getChildren().size; i++){
                Actor np = logoNotesGroup.getChildren().get(i);
                final AssetDescriptor<Sound> npSound = new AssetDescriptor<Sound>(String.format("sounds/logonotesound%d.mp3", i + 1), Sound.class);
                assetManager.load(npSound);
                np.addListener(new ActorGestureListener(){
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        Actor a = event.getListenerActor();
                        assetManager.get(npSound).play(assetManager.get(warmupMusic).getVolume() * 0.95f, 1.0f, 1.0f);
                        a.addAction(Actions.sequence(
                            Actions.scaleTo(0.9f, 0.9f, 0.05f),
                            Actions.scaleTo(1.1f, 1.1f, 0.1f),
                            Actions.scaleTo(1.0f, 1.0f, 0.1f)
                        ));
                        super.tap(event, x, y, count, button);
                    }
                });
            }

            logoNotesGroup.getChildren().shuffle();

            for(int i = 0; i < logoNotesGroup.getChildren().size; i++){
                Actor a = logoNotesGroup.getChildren().get(i);
                a.setOrigin(Align.center);
                a.addAction(Actions.sequence(
                    Actions.scaleTo(0, 0),
                    Actions.delay(1.5f + 0.11f * i),
                    Actions.scaleTo(2.0f, 2.0f, 0.075f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            float pitch = Pick.integer(50, 200) / 100;
                            DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, 1.0f, pitch);
                        }
                    }),
                    Actions.scaleTo(1.0f, 1.0f, 0.25f),
                    Actions.delay(i * 1.0f * (3.0f * (float) Math.random())),
                    Actions.forever(Actions.sequence(
                        Actions.moveBy(0, a.getHeight() * 0.1f, 3f, Interpolation.sine),
                        Actions.moveBy(0, a.getHeight() * -0.1f, 3f, Interpolation.sine)
                    ))
                ));
            }


            logoGroup.addActor(logoNotesGroup);
        }

        addActor(logoGroup);

        // buttons

        Group playGroup = new Group(),
                optionsGroup = new Group(),
                infoGroup = new Group(),
                statsGroup = new Group();

        this.menuItems = new HorizontalGroup();
        this.menuItems.addActor(playGroup);
        this.menuItems.addActor(optionsGroup);
        this.menuItems.addActor(infoGroup);
        this.menuItems.addActor(statsGroup);

        for(Actor a : menuItems.getChildren()){
            Image blank = new Image(DoReMi.sprites.findRegion("blank"));
            a.setBounds(0, 0, blank.getWidth(), blank.getHeight());
            // ((Group) a).addActor(blank);
        }

        {
            Image playIcon = new Image(DoReMi.sprites.findRegion("play"));
            playIcon.setOrigin(Align.center);
            playIcon.setScale(0.8f);
            playIcon.setPosition(playGroup.getWidth() / 2 - playIcon.getWidth() / 2, playGroup.getHeight() / 2 - playIcon.getHeight() / 2);
            playGroup.addActor(playIcon);

            playGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    DoReMi.changeStage(ProgressionStage.class);
                    super.tap(event, x, y, count, button);
                }
            });
        }

        {
            Image optionsIcon = new Image(DoReMi.sprites.findRegion("wrench"));
            optionsIcon.setOrigin(Align.center);
            optionsIcon.setScale(1.15f);
            optionsIcon.setPosition(optionsGroup.getWidth() / 2 - optionsIcon.getWidth() / 2, optionsGroup.getHeight() / 2 - optionsIcon.getHeight() / 2);
            optionsGroup.addActor(optionsIcon);

            optionsGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    DoReMi.changeStage(OptionsStage.class);
                    super.tap(event, x, y, count, button);
                }
            });
        }

        {
            Image infoIcon = new Image(DoReMi.sprites.findRegion("info"));
            infoIcon.setOrigin(Align.center);
            infoIcon.setScale(0.85f);
            infoIcon.setPosition(infoGroup.getWidth() / 2 - infoIcon.getWidth() / 2, infoGroup.getHeight() / 2 - infoIcon.getHeight() / 2);
            infoGroup.addActor(infoIcon);

            infoGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    DoReMi.changeStage(InfoStage.class);
                    super.tap(event, x, y, count, button);
                }
            });
        }

        {
            Image statsIcon = new Image(DoReMi.sprites.findRegion("stats"));
            statsIcon.setOrigin(Align.center);
            statsIcon.setScale(0.75f);
            statsIcon.setPosition(statsGroup.getWidth() / 2 - statsIcon.getWidth() / 2, statsGroup.getHeight() / 2 - statsIcon.getHeight() / 2);
            statsGroup.addActor(statsIcon);

            statsGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    DoReMi.changeStage(StatsStage.class);
                    super.tap(event, x, y, count, button);
                }
            });
        }

        menuItems.setHeight(menuItems.getChildren().get(0).getHeight());
        for(Actor a : menuItems.getChildren()){
            menuItems.setSize(menuItems.getWidth() + a.getWidth(), menuItems.getHeight());
        }


        for(int i = 0; i < menuItems.getChildren().size; i++){
            Actor a = menuItems.getChildren().get(i);
            a.addAction(Actions.sequence(
                    Actions.delay(4.0f + i * 0.1f),
                    Actions.moveBy(0, a.getHeight() * 0.1f, 0.1f, Interpolation.sine),
                    Actions.moveBy(0, -(a.getHeight() * 0.1f), 0.1f, Interpolation.sine)
            ));
            a.setOrigin(Align.center);
            a.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    event.getListenerActor().addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.scaleTo(0.5f, 0.5f, 0.1f),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            DoReMi.assets.get(DoReMi.sound_pop).play(0.75f, 0.5f, 1.0f);
                                        }
                                    })
                            ),
                            Actions.parallel(
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        DoReMi.assets.get(DoReMi.sound_pop).play(1.0f);
                                    }
                                }),
                                Actions.scaleBy(5f, 5f, 0.25f, Interpolation.sineIn),
                                Actions.fadeOut(0.25f, Interpolation.sineIn)
                            )
                    ));
                    super.tap(event, x, y, count, button);
                }
            });
        }


        this.menuItems.setPosition(viewport.getWorldWidth() / 2f - this.menuItems.getWidth() / 2, viewport.getWorldHeight() * 0.1f);

        addActor(menuItems);
    }

    @Override
    public void act() {
        super.act();
        this.assetManager.update();

        if(assetManager.isLoaded(warmupMusic.fileName, warmupMusic.type)){
            Music warmupMusic = assetManager.get(this.warmupMusic);
            if(!warmupMusic.isPlaying()){
                warmupMusic.setVolume(0.25f);
                warmupMusic.play();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }
}
