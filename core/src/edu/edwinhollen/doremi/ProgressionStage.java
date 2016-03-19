package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fubar on 3/5/16.
 */
public class ProgressionStage extends Stage {
    private Texture progressionLines;

    public ProgressionStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        DoReMi.addBackButton(this, Color.LIGHT_GRAY);

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
                circleActor.setColor(Color.valueOf(Chromatic.E_NATURAL.getColor()));
            }else if(i == progress){
                circleActor.setColor(Color.valueOf(Chromatic.F_NATURAL.getColor()));
            }else{
                circleActor.setColor(Color.valueOf(Chromatic.G_NATURAL.getColor()));
                Image lock = new Image(DoReMi.sprites.findRegion("lock"));
                lock.setSize(circleGroup.getWidth() * 0.59f, circleGroup.getHeight() * 1.5f);
                lock.setPosition(circleGroup.getWidth() * 0.5f, circleGroup.getHeight() * 0.5f, Align.center);
                circleGroup.addActor(lock);
            }
        }
        circlesAndLines.addActor(circles);

        circlesAndLines.setSize(linesActor.getWidth(), linesActor.getHeight());
        circlesAndLines.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);

        addActor(circlesAndLines);
        circlesAndLines.setOrigin(Align.center);
    }

    @Override
    public void dispose() {
        super.dispose();
        progressionLines.dispose();
    }
}
