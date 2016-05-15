package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;

/**
 * Created by fubar on 2/17/16.
 */
public class OptionsStage extends Stage{
    private ShapeRenderer shapeRenderer;
    private final Color backgroundColor = Color.WHITE;

    private Puzzle.Difficulty currentDifficulty;
    private Boolean noteNames;
    private Boolean noteOutlines;

    private void plusMinusVisibility(Actor plus, Actor minus){
        boolean minusVisible = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty) > 0;
        boolean plusVisible = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty) < Puzzle.Difficulty.values().length - 1;

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

    public static Action inputInteractAction(){
        return Actions.sequence(
                Actions.scaleTo(1.0f, 0.1f, 0.05f),
                Actions.scaleTo(1.0f, 1.25f, 0.05f),
                Actions.scaleTo(1.0f, 1.0f, 0.1f)
        );
    }

    public OptionsStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.shapeRenderer = new ShapeRenderer();

        DoReMi.addBackButton(this, Color.LIGHT_GRAY, TitleStage.class);

        // get prefs
        this.currentDifficulty = Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty"));
        this.noteNames = DoReMi.preferences.getBoolean("note_names");
        this.noteOutlines = DoReMi.preferences.getBoolean("note_outlines");



        // build table
        Table table = new Table();
        table.setSize(viewport.getWorldWidth() * 0.85f, viewport.getWorldHeight() * 0.9f);
        // table.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);
        table.align(Align.center);
        table.setOrigin(Align.center);
        table.defaults().spaceBottom(viewport.getWorldHeight() * 0.05f);

        // scroll
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setWidth(table.getWidth());
        scrollPane.setHeight(table.getHeight());

        scrollPane.setPosition(viewport.getWorldWidth() * 0.95f, viewport.getWorldHeight() * 0.5f, Align.right);

        // label
        table.columnDefaults(0).width(table.getWidth() * 0.35f);
        // checkbox
        table.columnDefaults(1).width(table.getWidth() * 0.15f);
        // description
        table.columnDefaults(2).width(table.getWidth() * 0.5f);

        float plusMinusScale = 0.4f;

        {
            final Label difficultyDescription = new Label("", DoReMi.labelMini);
            difficultyDescription.setAlignment(Align.center);

            final Image plusDifficulty = new Image(DoReMi.sprites.findRegion("plus"));
            plusDifficulty.setSize(plusDifficulty.getWidth() * plusMinusScale, plusDifficulty.getHeight() * plusMinusScale);
            final Image minusDifficulty = new Image(DoReMi.sprites.findRegion("minus"));
            minusDifficulty.setSize(minusDifficulty.getWidth() * plusMinusScale, minusDifficulty.getHeight() * plusMinusScale);


            final Label lblDifficulty = new Label("Difficulty:", DoReMi.labelNormal);
            lblDifficulty.setAlignment(Align.topLeft);


            final Label valDifficulty = new Label(this.currentDifficulty.toString(), DoReMi.labelNormal);
            valDifficulty.setAlignment(Align.center);

            minusDifficulty.setColor(Color.BLACK);
            minusDifficulty.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    Integer currentDifficultyIndex = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty);
                    if(currentDifficultyIndex > 0){
                        currentDifficulty = Puzzle.Difficulty.values()[currentDifficultyIndex - 1];
                        DoReMi.preferences.putString("difficulty", currentDifficulty.toString());
                        valDifficulty.setText(currentDifficulty.toString());
                        plusMinusVisibility(plusDifficulty, minusDifficulty);

                        difficultyDescription.setText(currentDifficulty.getDescription());

                        DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, 1.25f, 1.0f);

                        minusDifficulty.addAction(inputInteractAction());
                    }
                }
            });

            plusDifficulty.setColor(Color.BLACK);
            plusDifficulty.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    Integer currentDifficultyIndex = Arrays.asList(Puzzle.Difficulty.values()).indexOf(currentDifficulty);
                    if(currentDifficultyIndex < Puzzle.Difficulty.values().length - 1){
                        currentDifficulty = Puzzle.Difficulty.values()[currentDifficultyIndex + 1];
                        DoReMi.preferences.putString("difficulty", currentDifficulty.toString());
                        valDifficulty.setText(currentDifficulty.toString());
                        plusMinusVisibility(plusDifficulty, minusDifficulty);

                        difficultyDescription.setText(currentDifficulty.getDescription());

                        DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, 1.5f, 1.0f);

                        plusDifficulty.addAction(inputInteractAction());
                    }
                }
            });

            Group difficultyVal = new Group();
            difficultyVal.setWidth(table.columnDefaults(1).getPrefWidth() + table.columnDefaults(2).getPrefWidth());
            difficultyVal.setHeight(plusDifficulty.getHeight() * plusMinusScale * 2.5f);
            difficultyVal.setPosition(0, 0);

            minusDifficulty.setPosition(0, 0, Align.bottomLeft);
            valDifficulty.setPosition(difficultyVal.getWidth() * 0.5f, difficultyVal.getHeight() * 0.5f, Align.center);
            plusDifficulty.setPosition(difficultyVal.getWidth(), 0, Align.bottomRight);

            difficultyVal.addActor(minusDifficulty);
            difficultyVal.addActor(valDifficulty);
            difficultyVal.addActor(plusDifficulty);

            table.add(lblDifficulty);

            table.add(difficultyVal).colspan(2).left();
            plusMinusVisibility(plusDifficulty, minusDifficulty);
            table.row().spaceBottom(difficultyVal.getHeight() * 0.5f);

            difficultyVal.toFront();

            plusDifficulty.toFront();
            minusDifficulty.toFront();


            difficultyDescription.setText(currentDifficulty.getDescription());
            table.add();
            table.add(difficultyDescription).colspan(2);
            table.row();
        }

        {
            final Label noteNameLabel = new Label("Note names:", DoReMi.labelNormal);
            final Label noteNameDescription = new Label("Always show note names on note pieces", DoReMi.labelMini);
            final Group checkGroup = new Group();
            final Image check = new Image(DoReMi.sprites.findRegion("checkboxchecked-sized"));
            final Image uncheck = new Image(DoReMi.sprites.findRegion("checkboxunchecked-sized"));

            check.setColor(Color.BLACK);
            uncheck.setColor(Color.BLACK);

            checkGroup.setSize(check.getWidth(), check.getHeight());

            checkGroup.addActor(check);
            checkGroup.addActor(uncheck);

            final Runnable showHideCheck = new Runnable() {
                @Override
                public void run() {
                    check.setVisible(noteNames);
                    uncheck.setVisible(!check.isVisible());
                }
            };

            checkGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    event.getListenerActor().addAction(inputInteractAction());
                    noteNames = !noteNames;
                    DoReMi.preferences.putBoolean("note_names", noteNames);
                    float pitch = 0.4f;
                    if(noteNames){
                        pitch = 0.75f;
                    }
                    DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, pitch, 1.0f);
                    showHideCheck.run();
                    super.tap(event, x, y, count, button);
                }
            });

            checkGroup.setOrigin(Align.center);

            showHideCheck.run();

            table.add(noteNameLabel);
            table.add(checkGroup);
            table.add(noteNameDescription);
            table.row();
        }

        {
            final Label noteOutlineLabel = new Label("Note outlines:", DoReMi.labelNormal);
            final Label noteOutlineDescription = new Label("Use outlined note heads", DoReMi.labelMini);
            final Group checkGroup = new Group();
            final Image check = new Image(DoReMi.sprites.findRegion("checkboxchecked-sized"));
            final Image uncheck = new Image(DoReMi.sprites.findRegion("checkboxunchecked-sized"));

            check.setColor(Color.BLACK);
            uncheck.setColor(Color.BLACK);

            checkGroup.setSize(check.getWidth(), check.getHeight());

            checkGroup.addActor(check);
            checkGroup.addActor(uncheck);

            final Runnable showHideCheck = new Runnable() {
                @Override
                public void run() {
                    check.setVisible(noteOutlines);
                    uncheck.setVisible(!check.isVisible());
                }
            };

            checkGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    event.getListenerActor().addAction(inputInteractAction());
                    noteOutlines = !noteOutlines;
                    DoReMi.preferences.putBoolean("note_outlines", noteOutlines);
                    float pitch = 0.4f;
                    if(noteOutlines){
                        pitch = 0.75f;
                    }
                    DoReMi.assets.get(DoReMi.sound_pop).play(0.5f, pitch, 1.0f);
                    showHideCheck.run();
                    super.tap(event, x, y, count, button);
                }
            });

            checkGroup.setOrigin(Align.center);

            showHideCheck.run();

            table.add(noteOutlineLabel);
            table.add(checkGroup);
            table.add(noteOutlineDescription);
            table.row();
        }

        {
            // clear stats
            Label clearStatsLabel = new Label("Statistics:", DoReMi.labelNormal);
            Group clearStatsButton = UI.ClickButton("Clear statistics", DoReMi.labelNormal, new Runnable() {
                @Override
                public void run() {
                    Json json = new Json();
                    DoReMi.preferences.putString("stats", json.toJson(new PuzzleStatisticsJson()));
                }
            });
            table.add(clearStatsLabel);
            table.add(clearStatsButton);
            table.add();
            table.row();
        }

        /*
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
        */


        // addActor(table);
        addActor(scrollPane);

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
    public void dispose() {
        DoReMi.preferences.flush();
        shapeRenderer.dispose();
        super.dispose();
    }
}
