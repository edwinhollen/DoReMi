package edu.edwinhollen.doremi;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Fubar on 1/17/2016.
 */
public class GameStage extends Stage {
    private AssetManager assetManager;

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        this.assetManager = new AssetManager();

        final Puzzle puzzle = new Puzzle(Puzzle.Difficulty.EASY);

        Group noteActors = new Group();

        for(int i = 0; i < puzzle.getSolutionNotes().size(); i++){
            TextureAtlas.AtlasRegion region;
            if(i == 0){
                region = DoReMi.sprites.findRegion("note_closed_male");
            }else if(i == (puzzle.getSolutionNotes().size() - 1)){
                region = DoReMi.sprites.findRegion("note_female_closed");
            }else{
                region = DoReMi.sprites.findRegion("note_female_male");
            }

            Image actor = new Image(region);
            actor.setUserObject(puzzle.getSolutionNotes().get(i));
            noteActors.addActor(actor);
        }

        for(int i = 0; i < puzzle.getExtraNotes().size(); i++){
            Image actor = new Image(DoReMi.sprites.findRegion(Pick.pick(new String[]{
                    "note_closed_male",
                    "note_female_closed",
                    "note_female_male"
            })));
            actor.setUserObject(puzzle.getExtraNotes().get(i));
            noteActors.addActor(actor);
        }

        for(Actor a : noteActors.getChildren()){
            final Image actor = (Image) a;

            actor.setAlign(Align.center);

            final AssetDescriptor<Sound> noteSound = new AssetDescriptor<Sound>(String.format("notes/%s.mp3", ((Note) actor.getUserObject()).toString()), Sound.class);
            assetManager.load(noteSound);

            actor.addListener(new DragListener(){
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
                    super.drag(event, x, y, pointer);
                }
            });

            actor.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    System.out.println("clicked");
                    assetManager.get(noteSound).play(1.0f);
                    super.tap(event, x, y, count, button);
                }
            });
        }

        addActor(noteActors);
    }

    @Override
    public void act() {
        this.assetManager.update();
        super.act();
    }
}
