package edu.edwinhollen.doremi;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fubar on 1/17/2016.
 */
public class GameStage extends Stage {
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

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        this.assetManager = new AssetManager();
        this.yaySound = new AssetDescriptor<Sound>("sounds/yay.mp3", Sound.class);
        this.assetManager.load(yaySound);

        this.puzzle = new Puzzle(Puzzle.Difficulty.EASY);
        System.out.println(this.puzzle.toString());

        notePieces = new Group();
        notePieces.setZIndex(1);

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
            notePieces.addActor(actor);
        }

        // extra notes
        for(int i = 0; i < puzzle.getExtraNotes().size(); i++){
            NotePieceActor actor = new NotePieceActor(DoReMi.sprites.findRegion(Pick.pick(new String[]{
                    "closedmale",
                    "femaleclosed",
                    "femalemale"
            })), assetManager, puzzle.getExtraNotes().get(i));
            notePieces.addActor(actor);
        }

        // all note pieces
        for(Actor a : notePieces.getChildren()){
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
            actor.setPosition(viewport.getWorldWidth() / 2 + Pick.integer(-50, 50) - actor.getWidth() / 2, viewport.getWorldHeight() * 0.25f + Pick.integer(-5, 5));
            actor.addAction(Actions.moveBy(Pick.integer(-5, 5), Pick.integer(-5, 5), 0.5f, Interpolation.sineOut));
        }

        notePieces.getChildren().shuffle();

        addActor(notePieces);

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
