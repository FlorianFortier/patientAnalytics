package com.abernathyclinic.patientAnalytics.controller;

import com.abernathyclinic.patientAnalytics.model.Patient;
import com.abernathyclinic.patientAnalytics.service.DiabetesRiskCalculatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing diabetes risk calculations.
 * Provides an endpoint for calculating the diabetes risk level of a patient
 * based on their health data and notes.
 */
@RestController
@RequestMapping("/api/diabetes")
public class DiabetesRiskController {

    private final DiabetesRiskCalculatorService calculatorService;

    /**
     * Constructs a new {@code DiabetesRiskController} with the specified {@link DiabetesRiskCalculatorService}.
     *
     * @param calculator the service used to calculate diabetes risk levels.
     */
    public DiabetesRiskController(DiabetesRiskCalculatorService calculator) {
        this.calculatorService = calculator;
    }

    /**
     * Calculates the diabetes risk level for the given patient.
     * If the patient's notes are null, an empty list is assigned by default.
     *
     * @param patient the patient data used for calculating the risk level.
     * @return a {@link ResponseEntity} containing the risk level as a string if the calculation is successful,
     *         or an error message if an exception occurs.
     */
    @PostMapping("/risk")
    public ResponseEntity<String> calculateRisk(@RequestBody Patient patient) {
        if (patient.getNote() == null) {
            patient.setNote(List.of());
        }

        try {
            DiabetesRiskCalculatorService.RiskLevel riskLevel = calculatorService.calculateRisk(patient);
            return ResponseEntity.ok("Risk level for patient is: " + riskLevel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du calcul du risque : " + e.getMessage());
        }
    }
}
