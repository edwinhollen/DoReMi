package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fubar on 3/5/16.
 */
public class ProgressionStage extends Stage {
    private Texture progressionLines;
    private AssetManager assetManager;

    private AssetDescriptor<Sound> chain;

    public ProgressionStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        DoReMi.addBackButton(this, Color.LIGHT_GRAY);

        assetManager = new AssetManager();

        chain = new AssetDescriptor<Sound>(Gdx.files.internal("sounds/chain.mp3"), Sound.class);
        assetManager.load(chain);

        progressionLines = new Texture(Gdx.files.internal("bigimages/progressionlines.png"));

        // group for circles and lines
        Group circlesAndLines = new Group();

        Image linesActor = new Image(progressionLines);
        linesActor.setColor(Color.BLACK);
        linesActor.setWidth(viewport.getWorldWidth() * 0.95f);
        linesActor.setHeight(viewport.getWorldHeight() * 0.35f);
        circlesAndLines.addActor(linesActor);

        // progression circles
        Label.LabelStyle circleLabelStyle = new Label.LabelStyle(DoReMi.font, Color.WHITE);

        if(DoReMi.preferences.getInteger("progress") >= 10){
            DoReMi.preferences.putInteger("progress", 0);
            DoReMi.preferences.flush();
        }

        Integer progress = DoReMi.preferences.getInteger("progress");

        Group circles = new Group();
        for(int i = 0; i < 10; i++){
            Group circleGroup = new Group();

            Image circleActor = new Image(DoReMi.sprites.findRegion("progressiondot"));
            circleActor.setColor(Color.GRAY);
            circleActor.setSize(linesActor.getWidth() * 0.11f, linesActor.getHeight() * 0.26f);

            circleGroup.setSize(circleActor.getWidth(), circleActor.getHeight());

            // circleActor.setOrigin(Align.center);
            circleGroup.setY(linesActor.getHeight() * (i >= 5 ? -0.05f : 0.77f));
            circleGroup.setX(linesActor.getWidth() * 0.065f + (circleActor.getWidth() * (i % 5)) + ((i % 5) * linesActor.getWidth() * 0.079f));

            circleGroup.addAction(Actions.sequence(
                Actions.delay(1.0f + i * 0.1f),
                Actions.scaleTo(1.25f, 1.25f, 0.1f),
                Actions.scaleTo(1f, 1f, 0.35f)
            ));

            circleGroup.addActor(circleActor);
            circles.addActor(circleGroup);

            circleGroup.setOrigin(Align.center);

            // status
            if(i < progress){
                circleActor.setColor(Color.valueOf(Chromatic.A_FLAT.getColor()));
                // add the check mark
                Image checkMark = new Image(DoReMi.sprites.findRegion("checkmark"));
                checkMark.setSize(checkMark.getWidth() * 0.25f, checkMark.getHeight() * 0.25f);
                checkMark.setPosition(circleGroup.getWidth() * 0.5f, circleGroup.getHeight() * 0.5f, Align.center);
                circleGroup.addActor(checkMark);

                Group confettiGroup = new Group();
                confettiGroup.setSize(circleGroup.getWidth(), circleGroup.getHeight());
                for(int j = 0; j < 10; j++){
                    Image confetti = new Image(DoReMi.sprites.findRegion(String.format("confetti%d", Pick.integer(1,4))));
                    confetti.setSize(confetti.getWidth() * 0.2f, confetti.getHeight() * 0.2f);
                    confetti.setRotation(Pick.integer(0, 360));
                    confetti.setColor(Color.valueOf(Pick.pick(Chromatic.values()).getColor()));
                    confetti.setPosition(confettiGroup.getWidth() * (float) Math.random(), confettiGroup.getHeight() * (float) Math.random());
                    confetti.addAction(Actions.forever(Actions.parallel(
                            Actions.sequence(
                                    Actions.moveBy(0, confetti.getHeight() * 0.25f, 2f + (float) Math.random() * 3f),
                                    Actions.moveBy(0, confetti.getHeight() * -0.25f, 2f + (float) Math.random() * 3f)
                            ),
                            Actions.sequence(
                                    Actions.rotateBy(Pick.integer(-20, 20) / 100f, 3f + (float) Math.random() * 5f)
                            )
                    )));
                    confettiGroup.addActor(confetti);
                }
                confettiGroup.setOrigin(Align.center);
                confettiGroup.addAction(Actions.sequence(
                    Actions.scaleTo(0, 0),
                    Actions.delay(0.5f),
                    Actions.parallel(
                        Actions.moveBy(0, confettiGroup.getHeight() * 0.5f, 0.1f),
                        Actions.scaleTo(1.0f, 1.0f, 0.25f)
                    )
                ));
                circleGroup.addActor(confettiGroup);

            }else if(i == progress){
                // this is the next stage
                circleActor.setColor(Color.valueOf(Chromatic.F_NATURAL.getColor()));
                circleGroup.addAction(Actions.delay(2, Actions.forever(Actions.sequence(
                    Actions.moveBy(0, circleGroup.getHeight() * 0.1f, 0.25f),
                    Actions.moveBy(0, circleGroup.getHeight() * -0.1f, 0.25f),
                    Actions.delay(2)
                ))));
                circleGroup.addListener(new ActorGestureListener(){
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        super.tap(event, x, y, count, button);
                        DoReMi.changeStage(GameStage.class);
                    }
                });
            }else{
                circleActor.setColor(Color.valueOf(Chromatic.G_NATURAL.getColor()));
                final Image lock = new Image(DoReMi.sprites.findRegion("lock"));
                lock.setSize(circleGroup.getWidth() * 0.59f, circleGroup.getHeight() * 1.5f);
                lock.setPosition(circleGroup.getWidth() * 0.5f, circleGroup.getHeight() * 0.5f, Align.center);
                circleGroup.addActor(lock);
                circleGroup.addListener(new ActorGestureListener(){
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        super.tap(event, x, y, count, button);
                        Actor a = event.getListenerActor();
                        float currentX = a.getX();
                        float currentY = a.getY();
                        final float dur = 0.05f;
                        lock.addAction(Actions.repeat(1, Actions.sequence(
                            Actions.moveBy(lock.getWidth() * 0.25f, 0, dur),
                            Actions.moveBy(-lock.getWidth() * 0.25f, 0, dur),
                            Actions.moveBy(-lock.getWidth() * 0.25f, 0, dur),
                            Actions.moveBy(lock.getWidth() * 0.25f, 0, dur)
                        )));
                        assetManager.get(chain).play(0.25f, 1.0f, 1.0f);
                    }
                });
            }
        }
        circlesAndLines.addActor(circles);

        circlesAndLines.setSize(linesActor.getWidth(), linesActor.getHeight());
        circlesAndLines.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);

        addActor(circlesAndLines);
        circlesAndLines.setOrigin(Align.center);
    }

    @Override
    public void act() {
        super.act();
        assetManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        progressionLines.dispose();
        assetManager.dispose();
    }
}
