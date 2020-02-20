package com.selecttvapp.channels;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public class Programs  implements Serializable {
    private String started;


    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public ArrayList<ProgramList> getProgramlist() {
        return programs;
    }

    public void setProgramlist(ArrayList<ProgramList> programlist) {
        this.programs = programs;
    }

    private ArrayList<ProgramList> programs;
}
