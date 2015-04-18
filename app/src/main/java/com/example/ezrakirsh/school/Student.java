package com.example.ezrakirsh.school;

import java.util.List;

/**
 * Created by ezrakirsh on 15-04-11.
 */

public class Student {

    private String name;
    private String grade;
    private List<String> homework;
    private int gender;

    public Student(String name, String grade, List<String> homework) {
        this.name = name;
        this.grade = grade;
        this.homework = homework;
        this.gender = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<String> getHomework() {
        return homework;
    }

    public void setHomework(List<String> homework) {
        this.homework = homework;
    }



}
