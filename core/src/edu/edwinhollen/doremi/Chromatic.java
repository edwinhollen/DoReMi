package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Fubar on 1/16/2016.
 */
public enum Chromatic {
    C_NATURAL("cn", "d83b3b"),
    C_SHARP("c#", "d5803a"),
    D_NATURAL("dn", "4c3ad5"),
    E_FLAT("eb", "953ad5"),
    E_NATURAL("en", "d53a88"),
    F_NATURAL("fn", "3ad566"),
    F_SHARP("f#", "3ad5af"),
    G_NATURAL("gn", "d5c83a"),
    A_FLAT("ab", "9cd53a"),
    A_NATURAL("an", "54d53a"),
    B_FLAT("bb", "3ab6d5"),
    B_NATURAL("bn", "3a6ed5");

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
