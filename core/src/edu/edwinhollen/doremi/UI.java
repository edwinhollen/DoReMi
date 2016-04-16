package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by fubar on 4/15/16.
 */
public class UI {


    public static Group ClickButton(String text, Label.LabelStyle style, Runnable onClick){
        return ClickButton(text, style, onClick, Color.BLACK);
    }

    public static Group ClickButton(String text, Label.LabelStyle style, final Runnable onClick, Color color){
        Group g = new Group();
        Label.LabelStyle s = new Label.LabelStyle(style);
        s.fontColor = color;
        Label l = new Label(text, s);
        l.setColor(color);
        Image leftCap = new Image(DoReMi.sprites.findRegion("button-left"));
        leftCap.setColor(color);
        Image rightCap = new Image(DoReMi.sprites.findRegion("button-right"));
        rightCap.setColor(color);
        Image middle = new Image(DoReMi.sprites.findRegion("button-middle"));
        middle.setColor(color);
        g.setSize(l.getWidth()+leftCap.getWidth()+rightCap.getWidth(), leftCap.getHeight());

        leftCap.setPosition(0, 0, Align.bottomLeft);
        rightCap.setPosition(g.getWidth(), 0, Align.bottomRight);
        middle.setPosition(leftCap.getWidth(), 0, Align.bottomLeft);

        l.setPosition(g.getWidth() * 0.5f, g.getHeight() * 0.5f, Align.center);

        middle.setWidth(g.getWidth() - leftCap.getWidth() - rightCap.getWidth());

        g.addActor(leftCap);
        g.addActor(middle);
        g.addActor(rightCap);
        g.addActor(l);

        g.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onClick.run();
                Actor a = event.getListenerActor();
                a.setOrigin(Align.center);
                a.addAction(Actions.sequence(
                    Actions.scaleTo(0.85f, 0.5f, 0.04f),
                    Actions.scaleTo(0.95f, 1.75f, 0.05f),
                    Actions.scaleTo(1.0f, 1.0f, 0.05f)
                ));
                a.addAction(Actions.parallel(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            DoReMi.assets.get(DoReMi.sound_click).play(0.5f, 0.9f, 1.0f);
                        }
                    }),
                    Actions.delay(0.075f, Actions.run(new Runnable() {
                             @Override
                             public void run() {
                                 DoReMi.assets.get(DoReMi.sound_click).play(0.5f, 1.25f, 1.0f);
                             }
                         }
                    ))
                ));
                super.tap(event, x, y, count, button);
            }
        });

        return g;
    }
}
