package com.example.cpacsummary.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentSummary {
    private String surname;
    private String year12Set;
    private String year13Set;
    private Map<String, List<String>> cpacOutcomes = new LinkedHashMap<>(); // CPAC -> list of outcomes

    public StudentSummary() {}

    public StudentSummary(String surname, String year12Set, String year13Set) {
        this.surname = surname;
        this.year12Set = year12Set;
        this.year13Set = year13Set;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getYear12Set() {
        return year12Set;
    }

    public void setYear12Set(String year12Set) {
        this.year12Set = year12Set;
    }

    public String getYear13Set() {
        return year13Set;
    }

    public void setYear13Set(String year13Set) {
        this.year13Set = year13Set;
    }

    public Map<String, List<String>> getCpacOutcomes() {
        return cpacOutcomes;
    }

    public void setCpacOutcomes(Map<String, List<String>> cpacOutcomes) {
        this.cpacOutcomes = cpacOutcomes;
    }
}
