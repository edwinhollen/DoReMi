package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Edwin Hollen on 3/29/16.
 */
public class InfoStage extends Stage {
    private ShapeRenderer shapeRenderer;

    public InfoStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        DoReMi.addBackButton(this, Color.WHITE, TitleStage.class);

        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw() {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.rect(0, 0, getViewport().getScreenWidth(), getViewport().getScreenHeight());
        super.draw();
    }
}
