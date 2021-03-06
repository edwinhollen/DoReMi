package edu.edwinhollen.doremi;

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

    public Note(){

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

    public String toFancyString(){
        char[] chromaticParts = chromatic.getShortName().toCharArray();
        Character letter = chromaticParts[0];
        Character accidental = chromaticParts[1];
        String properAccidental = null;

        switch(accidental){
            case 'b':
                properAccidental = "b";
                break;
            case '#':
                properAccidental = "#";
                break;
            default:
                properAccidental = "";
        }

        return String.format("%s%s", letter.toString().toUpperCase(), properAccidental);
    }

    public String toFancyStringWithOctave(){
        return String.format("%s%d", toFancyString(), this.octave);
    }
}
