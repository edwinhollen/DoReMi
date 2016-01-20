package edu.edwinhollen.doremi;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Fubar on 1/17/2016.
 */
public class Puzzle {
    private List<Note> solutionNotes, extraNotes;

    private final static Integer LOWEST_OCTAVE = 1, HIGHEST_OCTAVE = 3;


    public Puzzle(Difficulty difficulty){
        this.solutionNotes = new LinkedList<Note>();
        this.extraNotes = new LinkedList<Note>();

        ScalePatterns pattern;
        Chromatic rootChromatic = Pick.pick(Chromatic.values());
        switch(difficulty){
            case MEDIUM:
                pattern = Pick.pick(new ScalePatterns[]{ScalePatterns.MAJOR, ScalePatterns.MINOR});
                break;
            case HARD:
                pattern = Pick.pick(ScalePatterns.values());
                break;
            default:
                pattern = ScalePatterns.MAJOR;
        }

        Scale scale = new Scale(new Note(rootChromatic, LOWEST_OCTAVE), pattern.getPattern());

        switch(difficulty){
            case MEDIUM:
                this.solutionNotes.add(scale.getNotes().get(0));
                this.solutionNotes.add(scale.getNotes().get(1));
                this.solutionNotes.add(scale.getNotes().get(2));
                this.solutionNotes.add(scale.getNotes().get(3));
                break;
            case HARD:
                Stack<Note> availableNotes = new Stack<Note>();
                availableNotes.addAll(scale.getNotes());
                Collections.shuffle(availableNotes);
                for(int i = 0; i < 4; i++){
                    this.solutionNotes.add(availableNotes.pop());
                }
                break;
            default:
                this.solutionNotes.add(scale.getNotes().get(0));
                this.solutionNotes.add(scale.getNotes().get(2));
                this.solutionNotes.add(scale.getNotes().get(4));
                this.solutionNotes.add(scale.getNotes().get(7));
        }

        // add extra notes
        while(extraNotes.size() < 4){
            Note candidate = new Note(Pick.pick(Chromatic.values()), Pick.integer(LOWEST_OCTAVE, HIGHEST_OCTAVE));
            if(this.solutionNotes.contains(candidate) || this.extraNotes.contains(candidate)) continue;
            this.extraNotes.add(candidate);
        }
    }

    public List<Note> getSolutionNotes() {
        return solutionNotes;
    }

    public List<Note> getExtraNotes() {
        return extraNotes;
    }

    enum Difficulty{
        EASY,
        MEDIUM,
        HARD
    }
}
