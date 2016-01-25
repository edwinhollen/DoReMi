package edu.edwinhollen.doremi;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Created by Fubar on 1/21/2016.
 */
public class NotePieceActor extends Image {
    private DragListener dragListener;
    private ActorGestureListener gestureListener;

    public NotePieceActor(TextureRegion region, final AssetManager assetManager, final Note note) {
        super(region);
        setUserObject(note);
        final AssetDescriptor<Sound> noteSound = new AssetDescriptor<Sound>(String.format("notes/%s.mp3", ((Note) getUserObject()).toString()), Sound.class);
        assetManager.load(noteSound);
        this.dragListener = new DragListener(){
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                moveBy(x - getWidth() / 2, y - getHeight() / 2);
                super.drag(event, x, y, pointer);
            }

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                addAction(Actions.scaleTo(1.1f, 1.1f, 0.075f));
                toFront();
                super.dragStart(event, x, y, pointer);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                addAction(Actions.scaleTo(1.0f, 1.0f, 0.075f));
                super.dragStop(event, x, y, pointer);
            }
        };

        this.dragListener.setTapSquareSize(0.5f);

        this.gestureListener = new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                assetManager.get(noteSound).play(1.0f);
                addAction(Actions.sequence(Actions.rotateTo(0), Actions.repeat(4, Actions.sequence(
                        Actions.rotateTo(3f, 0.15f),
                        Actions.rotateTo(-3f, 0.15f)
                )), Actions.rotateTo(0, 0.15f)));
                super.tap(event, x, y, count, button);
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {

                return super.longPress(actor, x, y);
            }
        };

        addListener(dragListener);
        addListener(gestureListener);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {


        if(dragListener.isDragging()){
            setColor(0, 0, 0, 0.25f);
            float oldX = getX();
            float oldY = getY();
            setPosition(getX(), getY() - getHeight() * 0.04f);
            super.draw(batch, parentAlpha);
            setPosition(oldX, oldY);
            setColor(Color.WHITE);
        }
        super.draw(batch, parentAlpha);

        DoReMi.font.setColor(Color.BLACK);
        DoReMi.font.draw(batch, getUserObject().toString(), getX(), getY());
    }
}
