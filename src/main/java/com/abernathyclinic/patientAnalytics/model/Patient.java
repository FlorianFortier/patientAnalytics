package com.abernathyclinic.patientAnalytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class Patient {
    // Getters et Setters
    private String nom;
    private int age;
    private String genre; // "m" ou "f"
    private List<String> note;

}
