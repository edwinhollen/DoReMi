package edu.edwinhollen.doremi;

/**
 * Created by Fubar on 1/16/2016.
 */

public enum ScalePatterns{
    MAJOR(new Integer[]{0, 2, 2, 1, 2, 2, 2, 1}),
    MINOR(new Integer[]{0, 2, 1, 2, 2, 1, (2 + 1), 1}),
    DORIAN(new Integer[]{0, 2, 1, 2, 2, 2, 1, 2}),
    CHROMATIC(new Integer[]{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

    private final Integer[] pattern;
    ScalePatterns(Integer[] pattern) {
        this.pattern = pattern;
    }

    public Integer[] getPattern(){
        return this.pattern;
    }
}