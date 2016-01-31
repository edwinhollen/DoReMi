package edu.edwinhollen.doremi;

import java.util.Date;

/**
 * Created by Fubar on 1/31/2016.
 */
public class PuzzleStatistics {
    final private Puzzle puzzle;
    private Integer solutionListens = 0;
    private Integer notePieceListens = 0;
    private Date startTime, endTime = null;
    public PuzzleStatistics(Puzzle puzzle){
        this.puzzle = puzzle;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public Integer getSolutionListens() {
        return solutionListens;
    }

    public Integer getNotePieceListens() {
        return notePieceListens;
    }

    public void addSolutionListen(){
        this.solutionListens++;
    }

    public void addNotePieceListen(){
        this.notePieceListens++;
    }

    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
