package com.abernathyclinic.patientAnalytics.service;

import com.abernathyclinic.patientAnalytics.model.Patient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service for calculating the diabetes risk level of a patient based on medical notes and demographic data.
 * <p>
 * The service evaluates the number of occurrences of specific trigger terms in a patient's medical notes
 * and considers the patient's age and gender to determine the risk level.
 * </p>
 */
@Service
public class DiabetesRiskCalculatorService {

    private static final List<String> TRIGGER_TERMS = Arrays.asList(
            "Hémoglobine A1C", "Microalbumine", "Taille", "Poids",
            "Fumeur", "Fumeuse", "Anormal", "Cholestérol",
            "Vertiges", "Rechute", "Réaction", "Anticorps"
    );

    /**
     * Enum representing the diabetes risk levels.
     */
    public enum RiskLevel {
        NONE, BORDERLINE, IN_DANGER, EARLY_ONSET
    }

    /**
     * Calculates the diabetes risk level of a patient based on their medical notes, age, and gender.
     *
     * @param patient the patient whose risk level needs to be calculated.
     * @return the risk level of the patient as a {@link RiskLevel}.
     * @throws IllegalArgumentException if the patient, gender, or notes are null.
     */
    public RiskLevel calculateRisk(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null.");
        }

        if (patient.getGenre() == null) {
            throw new IllegalArgumentException("Le genre (gender) ne peut pas être null.");
        }

        if (patient.getNote() == null) {
            throw new IllegalArgumentException("Les notes médicales (note) ne peuvent pas être null.");
        }

        int triggerCount = countTriggerTerms(patient.getNote());
        int age = patient.getAge();
        String gender = patient.getGenre().toLowerCase();

        if (isNoneRisk(triggerCount)) {
            return RiskLevel.NONE;
        }

        if (isBorderlineRisk(triggerCount, age)) {
            return RiskLevel.BORDERLINE;
        }

        if (isInDangerRisk(triggerCount, age, gender)) {
            return RiskLevel.IN_DANGER;
        }

        if (isEarlyOnsetRisk(triggerCount, age, gender)) {
            return RiskLevel.EARLY_ONSET;
        }

        return RiskLevel.NONE;
    }

    /**
     * Determines if the risk level is {@code NONE}.
     *
     * @param triggerCount the number of trigger terms found in the patient's medical notes.
     * @return {@code true} if the risk level is NONE, {@code false} otherwise.
     */
    private boolean isNoneRisk(int triggerCount) {
        return triggerCount == 0;
    }

    /**
     * Determines if the risk level is {@code BORDERLINE}.
     *
     * @param triggerCount the number of trigger terms found in the patient's medical notes.
     * @param age          the age of the patient.
     * @return {@code true} if the risk level is BORDERLINE, {@code false} otherwise.
     */
    private boolean isBorderlineRisk(int triggerCount, int age) {
        return triggerCount >= 2 && triggerCount <= 5 && age > 30;
    }

    /**
     * Determines if the risk level is {@code IN_DANGER}.
     *
     * @param triggerCount the number of trigger terms found in the patient's medical notes.
     * @param age          the age of the patient.
     * @param gender       the gender of the patient (e.g., "m" or "f").
     * @return {@code true} if the risk level is IN_DANGER, {@code false} otherwise.
     */
    private boolean isInDangerRisk(int triggerCount, int age, String gender) {
        if (age <= 30) {
            if (gender.equals("m") && triggerCount >= 3 && triggerCount < 5) {
                return true;
            }
            return gender.equals("f") && triggerCount >= 4 && triggerCount < 7;
        } else {
            return triggerCount >= 6 && triggerCount <= 7;
        }
    }

    /**
     * Determines if the risk level is {@code EARLY_ONSET}.
     *
     * @param triggerCount the number of trigger terms found in the patient's medical notes.
     * @param age          the age of the patient.
     * @param gender       the gender of the patient (e.g., "m" or "f").
     * @return {@code true} if the risk level is EARLY_ONSET, {@code false} otherwise.
     */
    private boolean isEarlyOnsetRisk(int triggerCount, int age, String gender) {
        if (age <= 30) {
            if (gender.equals("m") && triggerCount >= 5) {
                return true;
            }
            return gender.equals("f") && triggerCount >= 7;
        } else {
            return triggerCount >= 8;
        }
    }

    /**
     * Counts the number of trigger terms found in the provided medical notes.
     *
     * @param medicalNotes the list of medical notes for the patient.
     * @return the number of trigger terms found.
     */
    private int countTriggerTerms(List<String> medicalNotes) {
        if (medicalNotes == null || medicalNotes.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (String note : medicalNotes) {
            for (String term : TRIGGER_TERMS) {
                if (note.toLowerCase().contains(term.toLowerCase())) {
                    count++;
                }
            }
        }
        return count;
    }
}
