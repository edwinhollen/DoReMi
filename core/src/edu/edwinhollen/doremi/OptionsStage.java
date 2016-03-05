package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fubar on 2/17/16.
 */
public class OptionsStage extends Stage{
    private ShapeRenderer shapeRenderer;
    private final Color backgroundColor = Color.WHITE;

    public OptionsStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.shapeRenderer = new ShapeRenderer();

        DoReMi.addBackButton(this, Color.LIGHT_GRAY);

        setDebugAll(true);

        Label.LabelStyle lblStyle = new Label.LabelStyle(DoReMi.font, Color.BLACK);

        Table table = new Table();
        table.setSize(viewport.getWorldWidth() * 0.9f, viewport.getWorldHeight() * 0.9f);
        table.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);
        table.align(Align.center);
        table.setOrigin(Align.center);

        table.columnDefaults(0).width(Value.percentWidth(0.35f, table)).padRight(Value.percentWidth(0.1f, table));
        table.columnDefaults(1).width(Value.percentWidth(1 - 0.35f, table));




        {
            Label lblDifficulty = new Label("Difficulty:", lblStyle);
            lblDifficulty.setAlignment(Align.right);
            table.add(lblDifficulty);
            Label valDifficulty = new Label("????", lblStyle);
            valDifficulty.setAlignment(Align.left);
            table.add(valDifficulty);

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
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}
