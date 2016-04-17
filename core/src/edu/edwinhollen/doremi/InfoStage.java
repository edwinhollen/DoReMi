package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Edwin Hollen on 3/29/16.
 */
public class InfoStage extends Stage {
    private ShapeRenderer shapeRenderer;

    public InfoStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        DoReMi.addBackButton(this, Color.GRAY, TitleStage.class);

        this.shapeRenderer = new ShapeRenderer();

        Label.LabelStyle linkStyle = new Label.LabelStyle(DoReMi.labelNormal);
        linkStyle.fontColor = Color.valueOf(Chromatic.B_NATURAL.getColor());

        Table masterTable = new Table();

        ScrollPane scrollpane = new ScrollPane(masterTable);
        scrollpane.setSize(viewport.getWorldWidth() * 0.8f, viewport.getWorldHeight());
        scrollpane.setPosition(viewport.getWorldWidth() * 0.5f, viewport.getWorldHeight() * 0.5f, Align.center);

        masterTable.setSize(scrollpane.getWidth(), scrollpane.getHeight() * 5);
        masterTable.columnDefaults(0).width(masterTable.getWidth());

        float bottomSpace = viewport.getWorldHeight() * 0.1f;

        {
            Label summary = new Label(String.format("DoReMi is an open-source educational\nmusic puzzle and ear training game."), DoReMi.labelNormal);
            summary.setWrap(true);
            summary.setAlignment(Align.center);
            masterTable.add(summary).spaceBottom(bottomSpace).row();

            Label developer = new Label(String.format("This app was designed and developed by Edwin Hollen as a senior computer science project at West Virginia Wesleyan College."), DoReMi.labelMini);
            developer.setWrap(true);
            developer.setAlignment(Align.center);
            masterTable.add(developer).spaceBottom(bottomSpace).row();

            Label site = new Label(String.format("Learn more at doremigame.com"), linkStyle);
            site.setAlignment(Align.center);
            site.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    Gdx.net.openURI("http://www.doremigame.com");
                    super.tap(event, x, y, count, button);
                }
            });

            masterTable.add(site).spaceBottom(bottomSpace).row();

            Label version = new Label(String.format("Currently running version %s", DoReMi.version), DoReMi.labelMini);
            version.setAlignment(Align.center);

            masterTable.add(version).spaceBottom(bottomSpace).row();
        }

        addActor(scrollpane);
    }

    @Override
    public void draw() {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
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
