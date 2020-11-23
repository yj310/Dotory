package com.example.dotory.student;

public class StudentEnterState {

    private String name;
    private String room;
    private String state;
    private String enter_time;


    public StudentEnterState(){}

    public StudentEnterState(String name, String room, String state, String enter_time) {
        this.name = name;
        this.room = room;
        this.state = state;
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

    public String getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(String enter_time) {
        this.enter_time = enter_time;
    }
}
