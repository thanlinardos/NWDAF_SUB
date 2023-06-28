package io.nwdaf.eventsubscription.api.config;

import java.util.List;

public class Student {
	private Grade grade;
    private List<String> subjects;
    private String name;
    private String email;
    private String city;
    private String township;
    private List<String> day;
    private List<String> month;
    private List<String> year;

    public List<String> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
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
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getTownship() {
        return township;
    }
    public void setTownship(String township) {
        this.township = township;
    }
    public List<String> getDay() {
        return day;
    }
    public void setDay(List<String> day) {
        this.day = day;
    }
    public List<String> getMonth() {
        return month;
    }
    public void setMonth(List<String> month) {
        this.month = month;
    }
    public List<String> getYear() {
        return year;
    }
    public void setYear(List<String> year) {
        this.year = year;
    }
    public Grade getGrade() {
        return grade;
    }
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
