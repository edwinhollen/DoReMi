package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

/**
 * Created by fubar on 2/8/16.
 */
public class TitleStage extends Stage {
    private AssetManager assetManager;
    private HorizontalGroup menuItems;

    // asset descriptions
    private AssetDescriptor<Music> warmupMusic;
    private AssetDescriptor<Sound> pop, rustle;

    public TitleStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.assetManager = new AssetManager();
        this.warmupMusic = new AssetDescriptor<Music>("sounds/warmup.mp3", Music.class);
        assetManager.load(warmupMusic);

        this.pop = new AssetDescriptor<Sound>("sounds/pop.mp3", Sound.class);
        assetManager.load(this.pop);

        this.rustle = new AssetDescriptor<Sound>("sounds/rustle.mp3", Sound.class);
        assetManager.load(this.rustle);

        setDebugAll(false);

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
        }

        {
            Image statsIcon = new Image(DoReMi.sprites.findRegion("stats"));
            statsIcon.setOrigin(Align.center);
            statsIcon.setScale(0.75f);
            statsIcon.setPosition(statsGroup.getWidth() / 2 - statsIcon.getWidth() / 2, statsGroup.getHeight() / 2 - statsIcon.getHeight() / 2);
            statsGroup.addActor(statsIcon);
        }

        menuItems.setHeight(menuItems.getChildren().get(0).getHeight());
        for(Actor a : menuItems.getChildren()){
            menuItems.setSize(menuItems.getWidth() + a.getWidth(), menuItems.getHeight());
        }


        for(int i = 0; i < menuItems.getChildren().size; i++){
            Actor a = menuItems.getChildren().get(i);
            a.addAction(Actions.sequence(
                    Actions.delay(0.25f + i * 0.25f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            assetManager.get(rustle).play(0.5f, (float) Pick.integer(175, 250) / 100f, 1.0f);
                        }
                    }),
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
                                            assetManager.get(pop).play(0.75f, 0.5f, 1.0f);
                                        }
                                    })
                            ),
                            Actions.parallel(
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        assetManager.get(pop).play(1.0f);
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


        // logo
        Group logoGroup = new Group();
        logoGroup.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        {
            AssetDescriptor<Texture> logoImage = new AssetDescriptor<Texture>(Gdx.files.internal("bigimages/logosingle.png"), Texture.class);
            assetManager.load(logoImage);
            assetManager.finishLoadingAsset(logoImage.fileName);
            Image logo = new Image(assetManager.get(logoImage));
            logo.setPosition(logoGroup.getWidth() * 0.5f - logo.getWidth() * 0.5f, logoGroup.getHeight() * 0.90f - logo.getHeight());
            logoGroup.addActor(logo);


            // handle cute little note parts
            float baseX = logoGroup.getX() + logoGroup.getWidth() * 0.5f;
            float baseY = logoGroup.getY() + logoGroup.getHeight() * 0.5f;
            Image[] logoNotes = new Image[7];
            for(int i = 0; i < logoNotes.length; i++){
                Image logoNote = new Image(DoReMi.sprites.findRegion(String.format("logonote%d", i + 1)));
                logoNotes[i] = logoNote;
            }
            logoNotes[0].setPosition(baseX - logo.getWidth() * -0.85f, baseY - logo.getHeight() * -0.85f);


            for(Image img : logoNotes){
                // logoGroup.addActor(img);
            }
        }

        addActor(logoGroup);

        logoGroup.setZIndex(0);
        menuItems.setZIndex(1);
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
