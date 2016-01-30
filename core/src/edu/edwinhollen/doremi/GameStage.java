package edu.edwinhollen.doremi;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fubar on 1/29/2016.
 */
public class GameStage extends Stage {
    final Puzzle puzzle;
    AssetManager assetManager;
    Group notePieces;

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.assetManager = new AssetManager();
        this.puzzle = new Puzzle(Puzzle.Difficulty.EASY);

        List<Actor> solutionNoteActors, extraNoteActors;

        // handle solution notes
        solutionNoteActors = new LinkedList<Actor>();
        for(int i = 0; i < this.puzzle.getSolutionNotes().size(); i++){
            Note note = this.puzzle.getSolutionNotes().get(i);
            String spriteName;
            if(i == 0){
                spriteName = "closedmale";
            }else if(i == this.puzzle.getSolutionNotes().size()){
                spriteName = "femaleclosed";
            }else{
                spriteName = "femalemale";
            }
            Image image = new Image(DoReMi.sprites.findRegion(spriteName));
            image.setUserObject(note);
            solutionNoteActors.add(image);
        }

        // handle extra notes
        extraNoteActors = new LinkedList<Actor>();
        for(int i = 0; i < this.puzzle.getExtraNotes().size(); i++){
            Note note = this.puzzle.getExtraNotes().get(i);
            Image image = new Image(DoReMi.sprites.findRegion(Pick.pick(new String[]{"closedmale", "femaleclosed", "femalemale"})));
            image.setUserObject(note);
            extraNoteActors.add(image);
        }

        // handle all note pieces
        this.notePieces = new Group();
        List<Actor> allNoteActors = new LinkedList<Actor>();
        allNoteActors.addAll(solutionNoteActors);
        allNoteActors.addAll(extraNoteActors);
        for(Actor a : allNoteActors){
            // make a group for the note piece and the note head
            final Group notePieceGroup = new Group();
            notePieceGroup.setSize(a.getWidth(), a.getHeight());
            notePieceGroup.setOrigin(Align.center);

            // add the note piece to the group
            notePieceGroup.addActor(a);

            Note note = (Note) a.getUserObject();
            notePieceGroup.setUserObject(note);

            // start loading note sounds here
            assetManager.load(String.format("notes/%s.mp3", note.toString()), Sound.class);

            float y = -1;
            boolean drawFlat = note.getChromatic().toString().contains("FLAT");
            boolean drawSharp = note.getChromatic().toString().contains("SHARP");
            boolean drawLine = false;
            switch(note.getChromatic()){
                case C_NATURAL:
                case C_SHARP:
                    y = note.getOctave() < 3 ? 0.121f : 0.592f;
                    drawLine = note.getOctave() < 3;
                    break;
                case D_NATURAL:
                    y = note.getOctave() < 3 ? 0.162f : 0.656f;
                    break;
                case E_FLAT:
                case E_NATURAL:
                    y = note.getOctave() < 3 ? 0.203f : 0.656f;
                    break;
                case F_NATURAL:
                case F_SHARP:
                    y = note.getOctave() < 3 ? 0.277f : 0.807f;
                    break;
                case G_NATURAL:
                    y = note.getOctave() < 3 ? 0.357f : 0.869f;
                    break;
                case A_FLAT:
                case A_NATURAL:
                    y = note.getOctave() < 3 ? 0.432f : 0.929f;
                    drawLine = note.getOctave() > 2;
                    break;
                case B_FLAT:
                case B_NATURAL:
                    y = 0.509f;
            }

            y = y - 0.1f;

            Image noteHead = new Image(DoReMi.sprites.findRegion(drawLine ? "noteheadwithline" : "notehead"));
            noteHead.setPosition(notePieceGroup.getWidth() / 2 - noteHead.getWidth() / 2, notePieceGroup.getHeight() * y);
            noteHead.setColor(Color.valueOf(note.getChromatic().getColor()));
            notePieceGroup.addActor(noteHead);

            if(drawFlat || drawSharp){
                float accidentalX = noteHead.getX() - notePieceGroup.getWidth() * 0.15f;
                float accidentalY = noteHead.getY() - notePieceGroup.getHeight() * 0.05f;
                Image accidentalImage = new Image(DoReMi.sprites.findRegion(drawFlat ? "flat" : "sharp"));
                accidentalImage.setPosition(accidentalX, accidentalY);
                accidentalImage.setColor(Color.valueOf(note.getChromatic().getColor()));
                notePieceGroup.addActor(accidentalImage);
            }

            Group notePieceWithLabelGroup = new Group();
            notePieceWithLabelGroup.setSize(notePieceGroup.getWidth(), notePieceGroup.getHeight());
            notePieceWithLabelGroup.setUserObject((Note) notePieceGroup.getUserObject());
            notePieceWithLabelGroup.setOrigin(Align.center);

            Label.LabelStyle notePieceLabelStyle = new Label.LabelStyle(DoReMi.font, Color.valueOf(note.getChromatic().getColor()));
            final Label label = new Label(note.toFancyStringWithOctave(), notePieceLabelStyle);
            label.setAlignment(Align.center);
            label.setPosition(notePieceGroup.getWidth() / 2 - label.getWidth() / 2, - notePieceGroup.getHeight() * 0.28f);
            label.addAction(Actions.fadeOut(0));
            notePieceWithLabelGroup.addActor(label);

            notePieceWithLabelGroup.addActor(notePieceGroup);

            // add event listeners for dragging
            notePieceWithLabelGroup.addListener(new DragListener(){
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    Actor actor = event.getListenerActor();
                    actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
                    actor.toFront();
                    super.drag(event, x, y, pointer);
                }
            });

            notePieceWithLabelGroup.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    Actor actor = event.getListenerActor();
                    Note note = (Note) actor.getUserObject();
                    assetManager.get(String.format("notes/%s.mp3", note.toString()), Sound.class).play(1.0f);
                    notePieceGroup.addAction(Actions.sequence(Actions.repeat(9, Actions.sequence(
                            Actions.rotateTo(-3f, 0.1f, Interpolation.circle),
                            Actions.rotateTo(3f, 0.1f, Interpolation.circle)
                    )), Actions.rotateTo(0f, 0.2f, Interpolation.circle)));
                    label.addAction(Actions.sequence(Actions.fadeIn(0.05f), Actions.delay(1f), Actions.fadeOut(1f)));
                    super.tap(event, x, y, count, button);
                }
            });

            notePieceWithLabelGroup.setPosition(viewport.getWorldWidth() / 2 - notePieceWithLabelGroup.getWidth() / 2, viewport.getWorldHeight() * 0.15f);
            notePieceWithLabelGroup.addAction(Actions.moveBy(
                    Pick.integer(0, (int) (viewport.getWorldWidth() * 0.35f)) * (Pick.bool() ? -1 : 1),
                    Pick.integer(0, (int) (viewport.getWorldHeight() * 0.1f)) * (Pick.bool() ? -1 : 1),
                    0.25f,
                    Interpolation.circle
            ));

            this.notePieces.addActor(notePieceWithLabelGroup);
        }

        // add note pieces
        addActor(this.notePieces);
    }

    @Override
    public void act() {
        assetManager.update();
        super.act();
    }

    @Override
    public void draw() {
        super.draw();
        /*
        getBatch().begin();
        for(Actor a : this.notePieces.getChildren()){
            Note note = (Note) a.getUserObject();
            DoReMi.font.setColor(Color.BLACK);
            DoReMi.font.draw(getBatch(), note.toString(), a.getX(), a.getY());
        }
        getBatch().end();
        */
    }
}
