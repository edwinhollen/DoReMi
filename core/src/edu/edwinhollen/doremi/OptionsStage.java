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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

        Table table = new Table();
        table.setSize(viewport.getWorldWidth() * 0.9f, viewport.getWorldHeight() * 0.9f);
        table.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);
        table.align(Align.center);
        table.setOrigin(Align.center);

        // label
        table.columnDefaults(0).width(table.getWidth() * 0.35f);
        // value
        table.columnDefaults(1).width(table.getWidth() * 0.65f);

        float plusMinusScale = 0.4f;

        {
            final Label difficultyDescription = new Label("", DoReMi.labelMini);
            difficultyDescription.setAlignment(Align.center);

            final Image plusDifficulty = new Image(DoReMi.sprites.findRegion("plus"));
            plusDifficulty.setSize(plusDifficulty.getWidth() * plusMinusScale, plusDifficulty.getHeight() * plusMinusScale);
            final Image minusDifficulty = new Image(DoReMi.sprites.findRegion("minus"));
            minusDifficulty.setSize(minusDifficulty.getWidth() * plusMinusScale, minusDifficulty.getHeight() * plusMinusScale);


            Label lblDifficulty = new Label("Difficulty:", DoReMi.labelNormal);
            lblDifficulty.setAlignment(Align.topLeft);
            table.add(lblDifficulty);

            final Label valDifficulty = new Label(DoReMi.preferences.getString("difficulty"), DoReMi.labelNormal);
            valDifficulty.setAlignment(Align.center);

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
                                Actions.scaleTo(1.0f, 0.1f, 0.05f),
                                Actions.scaleTo(1.0f, 1.25f, 0.05f),
                                Actions.scaleTo(1.0f, 1.0f, 0.1f)
                        ));
                    }
                }
            });

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
                                Actions.scaleTo(1.0f, 0.1f, 0.05f),
                                Actions.scaleTo(1.0f, 1.25f, 0.05f),
                                Actions.scaleTo(1.0f, 1.0f, 0.1f)
                        ));
                    }
                }
            });

            Group difficultyVal = new Group();
            difficultyVal.setWidth(table.columnDefaults(1).getPrefWidth());
            difficultyVal.setHeight(plusDifficulty.getHeight() * plusMinusScale * 2.5f);
            difficultyVal.setPosition(0, 0);

            minusDifficulty.setPosition(0, 0, Align.bottomLeft);
            valDifficulty.setPosition(difficultyVal.getWidth() * 0.5f, difficultyVal.getHeight() * 0.5f, Align.center);
            plusDifficulty.setPosition(difficultyVal.getWidth(), 0, Align.bottomRight);

            difficultyVal.addActor(minusDifficulty);
            difficultyVal.addActor(valDifficulty);
            difficultyVal.addActor(plusDifficulty);

            table.add(difficultyVal);
            plusMinusVisibility(plusDifficulty, minusDifficulty);
            table.row().spaceBottom(difficultyVal.getHeight() * 0.5f);

            difficultyVal.toFront();

            plusDifficulty.toFront();
            minusDifficulty.toFront();


            difficultyDescription.setText(Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty")).getDescription());
            table.add();
            table.add(difficultyDescription);
            table.row();
        }

        {
            final Image check = new Image(DoReMi.sprites.findRegion("checkboxchecked-sized"));
            check.setColor(Color.BLACK);
            final Image unchecked = new Image(DoReMi.sprites.findRegion("checkboxunchecked-sized"));
            unchecked.setColor(Color.BLACK);

            final Group g = new Group();
            final Label playerIdLabel = new Label("", DoReMi.labelNormal);

            final Runnable checkWebStatsStatus = new Runnable() {
                @Override
                public void run() {
                    boolean isWebStatsEnabled = DoReMi.preferences.getBoolean("web_stats");
                    check.setVisible(isWebStatsEnabled);
                    Actor b = isWebStatsEnabled ? check : unchecked;
                    Actor a = isWebStatsEnabled ? unchecked : check;

                    a.addAction(Actions.visible(false));
                    b.addAction(Actions.visible(true));

                    g.addAction(Actions.sequence(
                        Actions.scaleTo(1.0f, 0.1f, 0.05f),
                        Actions.scaleTo(1.0f, 1.25f, 0.05f),
                        Actions.scaleTo(1.0f, 1.0f, 0.1f)
                    ));

                    if(isWebStatsEnabled){
                        playerIdLabel.setText(String.format("Player ID: %s", DoReMi.preferences.getString("player_id")));
                    }else{
                        playerIdLabel.setText("");
                    }
                }
            };

            g.setSize(check.getWidth(), check.getHeight());
            g.addActor(check);
            g.addActor(unchecked);
            g.addActor(playerIdLabel);
            playerIdLabel.setPosition(g.getWidth() * 1.5f, g.getHeight() * 0.5f);
            checkWebStatsStatus.run();

            g.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    assetManager.get(pop).play(0.5f, 1.5f, 1.0f);
                    DoReMi.preferences.putBoolean("web_stats", !DoReMi.preferences.getBoolean("web_stats"));
                    if(DoReMi.preferences.getBoolean("web_stats")){
                        DoReMi.webStats.newPlayer(new Runnable() {
                            @Override
                            public void run() {
                                playerIdLabel.setText(String.format("Player ID: %s", DoReMi.preferences.getString("player_id")));
                            }
                        });
                    }

                    checkWebStatsStatus.run();
                    super.tap(event, x, y, count, button);
                }
            });

            if(DoReMi.preferences.getBoolean("web_stats")){
                unchecked.setVisible(false);
                check.setVisible(true);
                playerIdLabel.setText(String.format("Player ID: %s", DoReMi.preferences.getString("player_id")));
            }else{
                unchecked.setVisible(false);
                playerIdLabel.setText("");
            }

            Label webStatsLabel = new Label("Telemetry:", DoReMi.labelNormal);

            table.add(webStatsLabel);
            table.add(g);
            table.row();


            Label webStatsDescription = new Label("Opt-in anonymized statistics reporting of player progress. This data is used to help players evaluate their progress with other players.", DoReMi.labelMini);
            webStatsDescription.setWrap(true);
            table.add();
            table.add(webStatsDescription);
            table.row();
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
        DoReMi.preferences.flush();
        shapeRenderer.dispose();
        super.dispose();
    }
}
