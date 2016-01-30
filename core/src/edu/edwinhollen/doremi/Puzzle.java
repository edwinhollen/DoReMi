package edu.edwinhollen.doremi;

import java.util.*;

/**
 * Created by Fubar on 1/17/2016.
 */
public class Puzzle {
    private List<Note> solutionNotes, extraNotes;

    private final static Integer LOWEST_OCTAVE = 2, HIGHEST_OCTAVE = 2;


    public Puzzle(Difficulty difficulty){
        this.solutionNotes = new LinkedList<Note>();
        this.extraNotes = new LinkedList<Note>();

        ScalePatterns pattern;
        Chromatic rootChromatic = Pick.pick(Chromatic.values());

        // pick a scale pattern
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
        Stack<Chromatic> availableChromatics = new Stack<Chromatic>();
        availableChromatics.addAll(Arrays.asList(Chromatic.values()));

        for(Note note : this.solutionNotes){
            availableChromatics.remove(note.getChromatic());
        }

        Collections.shuffle(availableChromatics);

        for(int i = 0; i < 4; i++){
            this.extraNotes.add(new Note(availableChromatics.pop(), Pick.integer(LOWEST_OCTAVE, HIGHEST_OCTAVE)));
        }
    }

    public List<Note> getSolutionNotes() {
        return solutionNotes;
    }

    public List<Note> getExtraNotes() {
        return extraNotes;
    }

    public List<Note> getAllNotes(){
        List<Note> newList = new LinkedList<Note>();
        newList.addAll(this.solutionNotes);
        newList.addAll(this.extraNotes);
        return newList;
    }

    enum Difficulty{
        EASY,
        MEDIUM,
        HARD
    }
}
