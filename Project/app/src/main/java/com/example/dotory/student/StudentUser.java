package com.example.dotory.student;

public class StudentUser {

    private String name;
    private String room;
    private String entrance_year;
    private String phone;
    private String email;
    private String guardian_phone;
    private String address_load;
    private String address_detail;
    private String grade;
    private String school_class;
    private String class_number;
    private String birth_year;
    private String birth_month;
    private String birth_day;

    public StudentUser(){}

    public StudentUser(String name, String room, String entrance_year, String phone, String email, String guardian_phone, String address_load, String address_detail, String grade, String school_class, String class_number, String birth_year, String birth_month, String birth_day) {
        this.name = name;
        this.room = room;
        this.entrance_year = entrance_year;
        this.phone = phone;
        this.email = email;
        this.guardian_phone = guardian_phone;
        this.address_load = address_load;
        this.address_detail = address_detail;
        this.grade = grade;
        this.school_class = school_class;
        this.class_number = class_number;
        this.birth_year = birth_year;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
    }

    public String getEntrance_year() {
        return entrance_year;
    }

    public void setEntrance_year(String entrance_year) {
        this.entrance_year = entrance_year;
    }

    public String getGuardian_phone() {
        return guardian_phone;
    }

    public void setGuardian_phone(String guardian_phone) {
        this.guardian_phone = guardian_phone;
    }

    public String getAddress_load() {
        return address_load;
    }

    public void setAddress_load(String address_load) {
        this.address_load = address_load;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(String birth_month) {
        this.birth_month = birth_month;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool_class() {
        return school_class;
    }

    public void setSchool_class(String school_class) {
        this.school_class = school_class;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
