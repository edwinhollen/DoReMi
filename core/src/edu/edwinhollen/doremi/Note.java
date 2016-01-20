package edu.edwinhollen.doremi;

import java.util.Optional;

/**
 * Created by Fubar on 1/16/2016.
 */
public class Note {
    private Chromatic chromatic;
    private Integer octave;

    public Note(Chromatic chromatic, Integer octave){
        this.chromatic = chromatic;
        this.octave = octave;
    }

    public Note(Chromatic chromatic){
        this(chromatic, null);
    }

    public boolean hasOctave(){
        return octave != null;
    }

    public Chromatic getChromatic() {
        return chromatic;
    }

    public Integer getOctave() {
        return octave;
    }

    public void setChromatic(Chromatic chromatic) {
        this.chromatic = chromatic;
    }

    public void setOctave(Integer octave) {
        this.octave = octave;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Note)) return false;
        Note otherNote = (Note) obj;
        return equalsIgnoreOctave(otherNote) && otherNote.getOctave().equals(this.octave);
    }

    public boolean equalsIgnoreOctave(Object obj){
        if(!(obj instanceof Note)) return false;
        Note otherNote = (Note) obj;
        return otherNote.getChromatic().equals(this.chromatic);
    }

    @Override
    public String toString() {
        return String.format("%s%d", this.chromatic.getShortName(), this.octave);
    }
}
