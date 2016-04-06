package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Edwin Hollen on 3/29/16.
 */
public class StatsStage extends Stage {
    private ShapeRenderer shapeRenderer;
    private final Color backgroundColor = Color.WHITE;

    public StatsStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        DoReMi.addBackButton(this, Color.GRAY, TitleStage.class);

        setDebugAll(false);

        Json json = new Json();
        json.addClassTag("startTime", Date.class);
        json.addClassTag("endTime", Date.class);
        PuzzleStatisticsJson savedStatistics = json.fromJson(PuzzleStatisticsJson.class, DoReMi.preferences.getString("stats"));

        this.shapeRenderer = new ShapeRenderer();

        Label.LabelStyle lblStyle = new Label.LabelStyle(DoReMi.font, Color.BLACK);
        Label.LabelStyle lblStyleMini = new Label.LabelStyle(DoReMi.fontMini, Color.GRAY);

        {
            Table masterTable = new Table();

            ScrollPane scrollpane = new ScrollPane(masterTable);
            scrollpane.setSize(viewport.getWorldWidth() * 0.8f, viewport.getWorldHeight());
            scrollpane.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);

            masterTable.setSize(scrollpane.getWidth(), scrollpane.getHeight() * 5);
            masterTable.columnDefaults(0).width(masterTable.getWidth() * 0.5f);
            masterTable.columnDefaults(1).width(masterTable.getWidth() * 0.5f);

            {
                Label puzzlesPlayedLabel = new Label("Puzzles Played", lblStyle);
                Label puzzlesPlayedValue = new Label(String.valueOf(savedStatistics.puzzleStatistics.size()), lblStyle);
                masterTable.add(puzzlesPlayedLabel);
                masterTable.add(puzzlesPlayedValue);
                masterTable.row();
                Label puzzlesPlayedInfoLabel = new Label("The total number of puzzles played", lblStyleMini);
                masterTable.add(puzzlesPlayedInfoLabel).colspan(2).align(Align.left);
                masterTable.row();
            }
            {
                Label averageTimeLabel = new Label("Average time", lblStyle);
                long timeSum = 0;
                for(PuzzleStatistics ps : savedStatistics.puzzleStatistics){
                    long time = ps.getEndTime() - ps.getStartTime();
                    timeSum += time;
                }
                float averageTime = (timeSum > 0) ? timeSum / savedStatistics.puzzleStatistics.size() : 0;
                Label averageTimeValue = new Label(String.format("%s %s", new DecimalFormat("#.##").format(averageTime / 1000), "seconds"), lblStyle);
                masterTable.add(averageTimeLabel);
                masterTable.add(averageTimeValue);
                masterTable.row();

                Label averageTimeInfoLabel = new Label("The average amount of time spent on each puzzle", lblStyleMini);
                masterTable.add(averageTimeInfoLabel).colspan(2).align(Align.left);
                masterTable.row();



            }

            {

            }

            scrollpane.layout();
            addActor(scrollpane);
        }
    }

    @Override
    public void draw() {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(backgroundColor);
        this.shapeRenderer.rect(0, 0, getViewport().getScreenWidth(), getViewport().getScreenHeight());
        this.shapeRenderer.end();
        super.draw();
    }

    @Override
    public void dispose() {
        this.shapeRenderer.dispose();
        super.dispose();
    }
}
