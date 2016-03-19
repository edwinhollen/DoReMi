package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Fubar on 1/16/2016.
 */
public enum Chromatic {
    C_NATURAL("cn", "d83b3b"),      // red
    C_SHARP("c#", "d5803a"),        // orange
    D_NATURAL("dn", "4c3ad5"),      // darkest blue
    E_FLAT("eb", "953ad5"),         // violet
    E_NATURAL("en", "d53a88"),      // magenta
    F_NATURAL("fn", "3ad566"),      // teal-green
    F_SHARP("f#", "3ad5af"),        // turquoise
    G_NATURAL("gn", "d5c83a"),      // yellow
    A_FLAT("ab", "9cd53a"),         // lightest green
    A_NATURAL("an", "54d53a"),      // medium green
    B_FLAT("bb", "3ab6d5"),         // lightest blue
    B_NATURAL("bn", "3a6ed5");      // medium blue

    private final String shortName;
    private final String color;

    Chromatic(String shortName, String color){
        this.shortName = shortName;
        this.color = color;
    }

    public String getShortName(){
        return this.shortName;
    }

    public String getColor(){
        return this.color;
    }
}
