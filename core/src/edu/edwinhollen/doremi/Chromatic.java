package edu.edwinhollen.doremi;

/**
 * Created by Fubar on 1/16/2016.
 */
public enum Chromatic {
    C_NATURAL("cn"),
    C_SHARP("c#"),
    D_NATURAL("dn"),
    E_FLAT("eb"),
    E_NATURAL("en"),
    F_NATURAL("fn"),
    F_SHARP("f#"),
    G_NATURAL("gn"),
    A_FLAT("ab"),
    A_NATURAL("an"),
    B_FLAT("bb"),
    B_NATURAL("bn");

    private final String shortName;

    Chromatic(String shortName){
        this.shortName = shortName;
    }

    public String getShortName(){
        return this.shortName;
    }
}
