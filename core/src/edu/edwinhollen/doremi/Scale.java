package edu.edwinhollen.doremi;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fubar on 1/16/2016.
 */
public class Scale {
    private List<Note> notes;

    public Scale(Note rootNote, Integer[] scalePattern){
        this.notes = new LinkedList<Note>();
        this.notes.add(rootNote);

        for(Integer patternIndex = 1; patternIndex < scalePattern.length; patternIndex++){
            Chromatic thisChromatic = Chromatic.values()[(this.notes.get(this.notes.size() - 1).getChromatic().ordinal() + scalePattern[patternIndex]) % Chromatic.values().length];
            Integer thisOctave = this.notes.get(this.notes.size() - 1).getOctave() + (this.notes.get(this.notes.size() - 1).getChromatic().ordinal() + scalePattern[patternIndex] > Chromatic.values().length - 1 ? 1 : 0);
            this.notes.add(new Note(thisChromatic, thisOctave));
        }
    }

    public Note getRootNote(){
        return this.notes.get(0);
    }

    public List<Note> getNotes() {
        return notes;
    }
}
