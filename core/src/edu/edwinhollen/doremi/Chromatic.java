package edu.edwinhollen.doremi;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Fubar on 1/16/2016.
 */
public enum Chromatic {
    C_NATURAL("cn", "d83b3b"),
    C_SHARP("c#", "d5803a"),
    D_NATURAL("dn", "d5c83a"),
    E_FLAT("eb", "9cd53a"),
    E_NATURAL("en", "54d53a"),
    F_NATURAL("fn", "3ad566"),
    F_SHARP("f#", "3ad5af"),
    G_NATURAL("gn", "3ab6d5"),
    A_FLAT("ab", "3a6ed5"),
    A_NATURAL("an", "4c3ad5"),
    B_FLAT("bb", "953ad5"),
    B_NATURAL("bn", "d53a88");

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
