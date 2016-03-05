package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;

/**
 * Created by fubar on 2/17/16.
 */
public class OptionsStage extends Stage{
    private ShapeRenderer shapeRenderer;
    private final Color backgroundColor = Color.WHITE;

    private AssetManager assetManager;
    private AssetDescriptor<Sound> pop;

    private void plusMinusVisibility(Actor plus, Actor minus){
        boolean minusVisible = Arrays.asList(Puzzle.Difficulty.values()).indexOf(Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty"))) > 0;
        boolean plusVisible = Arrays.asList(Puzzle.Difficulty.values()).indexOf(Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty"))) < Puzzle.Difficulty.values().length - 1;

        if(!minusVisible){
            minus.addAction(Actions.alpha(0.1f, 0.1f));
        }else{
            minus.addAction(Actions.alpha(1, 0.1f));
        }
        if(!plusVisible) {
            plus.addAction(Actions.alpha(0.1f, 0.1f));
        }else{
            plus.addAction(Actions.alpha(1f, 0.1f));
        }
    }

    public OptionsStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.shapeRenderer = new ShapeRenderer();
        this.assetManager = new AssetManager();

        this.pop = new AssetDescriptor<Sound>("sounds/pop.mp3", Sound.class);
        this.assetManager.load(this.pop);

        DoReMi.addBackButton(this, Color.LIGHT_GRAY);

        Label.LabelStyle lblStyle = new Label.LabelStyle(DoReMi.font, Color.BLACK);

        Table table = new Table();
        table.setSize(viewport.getWorldWidth() * 0.9f, viewport.getWorldHeight() * 0.9f);
        table.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);
        table.align(Align.center);
        table.setOrigin(Align.center);

        // label
        table.columnDefaults(0).width(Value.percentWidth(0.25f, table));
        // minus button
        table.columnDefaults(1).width(Value.percentWidth(0.1f, table));
        // value
        table.columnDefaults(2).width(Value.percentWidth(0.45f, table));
        // plus button
        table.columnDefaults(3).width(Value.percentWidth(0.1f, table));

        float plusMinusScale = 0.4f;



        {
            final Label difficultyDescription = new Label("", lblStyle);
            difficultyDescription.setAlignment(Align.center);

            final Image plusDifficulty = new Image(DoReMi.sprites.findRegion("plus"));

            Label lblDifficulty = new Label("Difficulty:", lblStyle);
            lblDifficulty.setAlignment(Align.topLeft);
            table.add(lblDifficulty);

            final Label valDifficulty = new Label(DoReMi.preferences.getString("difficulty"), lblStyle);
            valDifficulty.setAlignment(Align.center);

            final Image minusDifficulty = new Image(DoReMi.sprites.findRegion("minus"));
            minusDifficulty.setColor(Color.BLACK);
            minusDifficulty.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    Puzzle.Difficulty currentDifficulty = Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty"));
                    Integer currentDifficultyIndex = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty);
                    if(currentDifficultyIndex > 0){
                        currentDifficultyIndex -= 1;
                        currentDifficulty = Puzzle.Difficulty.values()[currentDifficultyIndex];
                        DoReMi.preferences.putString("difficulty", currentDifficulty.toString());
                        valDifficulty.setText(currentDifficulty.toString());
                        plusMinusVisibility(plusDifficulty, minusDifficulty);

                        difficultyDescription.setText(currentDifficulty.getDescription());


                        assetManager.get(pop).play(0.5f, 1.25f, 1.0f);

                        minusDifficulty.addAction(Actions.sequence(
                                Actions.scaleBy(0f, -0.3f, 0.05f),
                                Actions.scaleBy(0f, 0.3f, 0.05f)
                        ));
                    }
                }
            });

            table.add(minusDifficulty).size(plusMinusScale * minusDifficulty.getWidth(), plusMinusScale * minusDifficulty.getHeight());
            table.add(valDifficulty).align(Align.top);

            plusDifficulty.setColor(Color.BLACK);

            plusDifficulty.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    Puzzle.Difficulty currentDifficulty = Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty"));
                    Integer currentDifficultyIndex = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty);
                    if(currentDifficultyIndex < Puzzle.Difficulty.values().length - 1){
                        currentDifficultyIndex += 1;
                        currentDifficulty = Puzzle.Difficulty.values()[currentDifficultyIndex];
                        DoReMi.preferences.putString("difficulty", currentDifficulty.toString());
                        valDifficulty.setText(currentDifficulty.toString());
                        plusMinusVisibility(plusDifficulty, minusDifficulty);

                        difficultyDescription.setText(currentDifficulty.getDescription());

                        assetManager.get(pop).play(0.5f, 1.5f, 1.0f);

                        plusDifficulty.addAction(Actions.sequence(
                                Actions.scaleBy(0f, -0.3f, 0.05f),
                                Actions.scaleBy(0f, 0.3f, 0.05f)
                        ));
                    }
                }
            });

            table.add(plusDifficulty).size(plusMinusScale * plusDifficulty.getWidth(), plusMinusScale * plusDifficulty.getHeight());
            plusMinusVisibility(plusDifficulty, minusDifficulty);
            table.row();


            difficultyDescription.setText(Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty")).getDescription());
            table.add().colspan(2);
            table.add(difficultyDescription);
            table.add();
        }

        addActor(table);

    }

    @Override
    public void draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(0, 0, getViewport().getScreenWidth(), getViewport().getScreenHeight());
        shapeRenderer.end();
        super.draw();
    }

    @Override
    public void act() {
        super.act();
        assetManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        DoReMi.preferences.flush();
    }
}
