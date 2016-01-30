package edu.edwinhollen.doremi;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fubar on 1/17/2016.
 */
public class GameStageOld extends Stage {
    boolean solutionFound = false;
    private AssetManager assetManager;
    private Puzzle puzzle;

    private final float notePieceScale = 0.35f;

    private AssetDescriptor<Sound> yaySound;

    private Group solutionSlots, notePieces, topRow;

    private boolean checkSolution(){
        if(solutionFound) return true;
        List<Note> currentNoteArrangement = new ArrayList<Note>(solutionSlots.getChildren().size);
        for(Actor solutionSlot : solutionSlots.getChildren()){
            if(solutionSlot.getUserObject() == null) return false;
            currentNoteArrangement.add((Note) ((NotePieceActor) solutionSlot.getUserObject()).getUserObject());
        }
        for(int i = 0; i < solutionSlots.getChildren().size; i++){
            if(!currentNoteArrangement.get(i).equals(puzzle.getSolutionNotes().get(i))) return false;
        }
        System.out.println("Solved!");
        assetManager.get(yaySound).play(0.75f);
        solutionFound = true;
        return true;
    }

    public GameStageOld(Viewport viewport, Batch batch) {
        super(viewport, batch);

        this.assetManager = new AssetManager();
        this.yaySound = new AssetDescriptor<Sound>("sounds/yay.mp3", Sound.class);
        this.assetManager.load(yaySound);

        this.puzzle = new Puzzle(Puzzle.Difficulty.EASY);
        System.out.println(this.puzzle.toString());

        Group temporaryNotePieces = new Group();
        temporaryNotePieces.setZIndex(1);

        // solution notes
        for(int i = 0; i < puzzle.getSolutionNotes().size(); i++){
            TextureAtlas.AtlasRegion region;
            if(i == 0){
                region = DoReMi.sprites.findRegion("closedmale");
            }else if(i == (puzzle.getSolutionNotes().size() - 1)){
                region = DoReMi.sprites.findRegion("femaleclosed");
            }else{
                region = DoReMi.sprites.findRegion("femalemale");
            }

            NotePieceActor actor = new NotePieceActor(region, assetManager, puzzle.getSolutionNotes().get(i));
            temporaryNotePieces.addActor(actor);
        }

        // extra notes
        for(int i = 0; i < puzzle.getExtraNotes().size(); i++){
            NotePieceActor actor = new NotePieceActor(DoReMi.sprites.findRegion(Pick.pick(new String[]{
                    "closedmale",
                    "femaleclosed",
                    "femalemale"
            })), assetManager, puzzle.getExtraNotes().get(i));
            temporaryNotePieces.addActor(actor);
        }

        // all note pieces
        for(Actor a : temporaryNotePieces.getChildren()){
            final NotePieceActor actor = (NotePieceActor) a;

            assetManager.load(String.format("notes/%s.mp3", ((Note) actor.getUserObject()).toString()), Sound.class);

            actor.addListener(new ActorGestureListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    for(Actor solutionSlot : solutionSlots.getChildren()){
                        if(solutionSlot.getUserObject() != null && solutionSlot.getUserObject().equals(actor)){
                            solutionSlot.setUserObject(null);
                            break;
                        }
                    }

                    Vector2 currentPos = actor.localToStageCoordinates(new Vector2());
                    Actor nearestSolutionSlot = null;
                    for(Actor solutionSlot : solutionSlots.getChildren()){
                        if(solutionSlot.getUserObject() != null) continue;
                        Vector2 solutionSlotPosition = solutionSlot.localToStageCoordinates(new Vector2());
                        if(Vector2.dst(currentPos.x, currentPos.y, solutionSlotPosition.x, solutionSlotPosition.y) < solutionSlot.getWidth() * 0.75){
                            nearestSolutionSlot = solutionSlot;
                        }
                    }
                    if(nearestSolutionSlot != null){
                        Vector2 nearestPosition = nearestSolutionSlot.localToStageCoordinates(new Vector2());
                        actor.addAction(Actions.moveTo(nearestPosition.x, nearestPosition.y, 0.25f, Interpolation.bounceOut));
                        nearestSolutionSlot.setUserObject(actor);
                        checkSolution();
                    }

                    super.touchUp(event, x, y, pointer, button);
                }
            });
            actor.setSize(actor.getWidth() * notePieceScale, actor.getHeight() * notePieceScale);
            actor.setAlign(Align.center);
            actor.setOrigin(Align.center);
            actor.setPosition(viewport.getWorldWidth() / 2 - actor.getWidth() / 2, viewport.getWorldHeight() * 0.25f);
            actor.addAction(Actions.moveBy(
                    Pick.integer(0, (int) (viewport.getWorldWidth() * 0.4f)) * (Pick.bool() ? -1 : 1),
                    Pick.integer(0, (int) (viewport.getWorldHeight() * 0.1f)),
                    0.5f,
                    Interpolation.sineOut
            ));
        }
        temporaryNotePieces.getChildren().shuffle();

        // apply note heads
        this.notePieces = new Group();
        for(Actor a : temporaryNotePieces.getChildren()){
            NotePieceActor npa = (NotePieceActor) a;
            Group g = new Group();
            Note note = (Note) npa.getUserObject();

            g.setSize(a.getWidth(), a.getHeight());

            Float y = null;
            boolean withLine = false;
            boolean withFlat = false;
            boolean withSharp = false;
            switch(note.getChromatic()){
                case C_NATURAL:
                    y = note.getOctave() < 3 ? 0.121f : 0.734f;
                    break;
                case C_SHARP:
                    y = note.getOctave() < 3 ? 0.121f : 0.734f;
                    withSharp = true;
                    break;
                case D_NATURAL:
                    y = note.getOctave() < 3 ? 0.162f : 0.656f;
                    break;
                case E_FLAT:
                    y = note.getOctave() < 3 ? 0.203f : 0.656f;
                    withFlat = true;
                    break;
                case E_NATURAL:
                    y = note.getOctave() < 3 ? 0.203f : 0.656f;
                    break;
                case F_NATURAL:
                    y = note.getOctave() < 3 ? 0.277f : 0.807f;
                    break;
                case F_SHARP:
                    y = note.getOctave() < 3 ? 0.277f : 0.807f;
                    withSharp = true;
                    break;
                case G_NATURAL:
                    y = note.getOctave() < 3 ? 0.357f : 0.869f;
                    break;
                case A_FLAT:
                    y = note.getOctave() < 3 ? 0.432f : 0.929f;
                    withLine = note.getOctave() < 3;
                    withFlat = true;
                    break;
                case A_NATURAL:
                    y = note.getOctave() < 3 ? 0.432f : 0.929f;
                    withLine = note.getOctave() < 3;
                    break;
                case B_FLAT:
                    y = 0.509f;
                    withFlat = true;
                    break;
                case B_NATURAL:
                    y = 0.509f;
                    break;
            }
            Image noteHead = new Image(DoReMi.sprites.findRegion(withLine ? "noteheadwithline" : "notehead"));
            noteHead.setPosition(g.getWidth() / 2, g.getHeight() * y);
            g.addActor(a);
            g.addActor(noteHead);
            g.setUserObject(a.getUserObject());
            this.notePieces.addActor(g);
        }

        addActor(this.notePieces);

        topRow = new Group();

        final Image listenButton = new Image(DoReMi.sprites.findRegion("listen"));
        listenButton.setPosition(0, 0);
        listenButton.setSize(listenButton.getWidth() * notePieceScale, listenButton.getHeight() * notePieceScale);
        listenButton.setAlign(Align.bottomLeft);
        listenButton.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                for (int i = 0; i < puzzle.getSolutionNotes().size(); i++){
                    final Note note = puzzle.getSolutionNotes().get(i);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            assetManager.get(String.format("notes/%s.mp3", note.toString()), Sound.class).play();
                        }
                    }, 0.5f * i);
                }
                super.tap(event, x, y, count, button);
            }
        });

        // solution slots
        solutionSlots = new Group();

        // #1
        final Image slot1 = new Image(DoReMi.sprites.findRegion("closedmaleoutline"));
        slot1.setPosition(0, 0);
        solutionSlots.addActor(slot1);
        // #2
        final Image slot2 = new Image(DoReMi.sprites.findRegion("femalemaleoutline"));
        slot2.setPosition(42, 0);
        solutionSlots.addActor(slot2);
        // #3
        final Image slot3 = new Image(DoReMi.sprites.findRegion("femalemaleoutline"));
        slot3.setPosition(42*2, 0);
        solutionSlots.addActor(slot3);
        // #4
        final Image slot4 = new Image(DoReMi.sprites.findRegion("femaleclosedoutline"));
        slot4.setPosition(42*3, 0);
        solutionSlots.addActor(slot4);

        for(int i = 0; i < solutionSlots.getChildren().size; i++){
            Actor solutionSlot = solutionSlots.getChildren().get(i);
            solutionSlot.setSize(solutionSlot.getWidth() * notePieceScale, solutionSlot.getHeight() * notePieceScale);
            solutionSlot.setPosition(160*i, 0, Align.bottomLeft);
        }

        solutionSlots.setPosition(listenButton.getWidth() + 128, 0);
        solutionSlots.setWidth(160 * 4);

        topRow.addActor(listenButton);
        topRow.addActor(solutionSlots);

        topRow.setWidth(listenButton.getWidth() + 128 + solutionSlots.getWidth());

        topRow.setPosition(viewport.getWorldWidth() / 2 - topRow.getWidth() / 2, viewport.getWorldHeight() * 2 / 3);

        addActor(topRow);
    }

    @Override
    public void act() {
        topRow.toBack();
        this.assetManager.update();
        for(Actor a : getActors()){
            a.addAction(Actions.moveTo(Math.round(a.getX()), Math.round(a.getY())));
        }
        super.act();
    }
}
