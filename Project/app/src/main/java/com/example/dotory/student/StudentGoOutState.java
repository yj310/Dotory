package com.example.dotory.student;

public class StudentGoOutState {

    private String name;
    private String room;
    private String state;
    private String go_out_time;
    private String expected_time;
    private String enter_time;


    public StudentGoOutState(){}

    public StudentGoOutState(String name, String room, String state, String go_out_time, String expected_time, String enter_time) {
        this.name = name;
        this.room = room;
        this.state = state;
        this.go_out_time = go_out_time;
        this.expected_time = expected_time;
        this.enter_time = enter_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGo_out_time() {
        return go_out_time;
    }

    public void setGo_out_time(String go_out_time) {
        this.go_out_time = go_out_time;
    }

    public String getExpected_time() {
        return expected_time;
    }

    public void setExpected_time(String expected_time) {
        this.expected_time = expected_time;
    }

    public String getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(String enter_time) {
        this.enter_time = enter_time;
    }
}
