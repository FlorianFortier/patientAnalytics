package com.abernathyclinic.patientAnalytics;

import com.abernathyclinic.patientAnalytics.model.Patient;
import com.abernathyclinic.patientAnalytics.service.DiabetesRiskCalculatorService;
import com.abernathyclinic.patientAnalytics.service.DiabetesRiskCalculatorService.RiskLevel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PatientAnalyticsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testCalculateRiskNone() {
		Patient patient = new Patient();
		patient.setNom("TestNone");
		patient.setAge(56);
		patient.setGenre("F");
		patient.setNote(Arrays.asList(
				"Le patient déclare qu'il 'se sent très bien'",
				"Poids égal ou inférieur au poids recommandé"
		));
		DiabetesRiskCalculatorService service = new DiabetesRiskCalculatorService();
		RiskLevel riskLevel = service.calculateRisk(patient);
		assertEquals(RiskLevel.NONE, riskLevel);
	}

	@Test
	void testCalculateRiskBorderline() {
		Patient patient = new Patient();
		patient.setNom("TestBorderline");
		patient.setAge(77);
		patient.setGenre("M");
		patient.setNote(Arrays.asList(
				"Le patient déclare qu'il ressent beaucoup de stress au travail",
				"Il se plaint également que son audition est anormale dernièrement",
				"Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois",
				"Il remarque également que son audition continue d'être anormale"
		));
		DiabetesRiskCalculatorService service = new DiabetesRiskCalculatorService();
		RiskLevel riskLevel = service.calculateRisk(patient);
		assertEquals(RiskLevel.BORDERLINE, riskLevel);
	}

	@Test
	void testCalculateRiskInDanger() {
		Patient patient = new Patient();
		patient.setNom("TestInDanger");
		patient.setAge(18);
		patient.setGenre("M");
		patient.setNote(Arrays.asList(
				"Le patient déclare qu'il fume depuis peu",
				"Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière",
				"Il se plaint également de crises d’apnée respiratoire anormales",
				"Tests de laboratoire indiquant un taux de cholestérol LDL élevé"
		));
		DiabetesRiskCalculatorService service = new DiabetesRiskCalculatorService();
		RiskLevel riskLevel = service.calculateRisk(patient);
		assertEquals(RiskLevel.IN_DANGER, riskLevel);
	}

	@Test
	void testCalculateRiskEarlyOnset() {
		Patient patient = new Patient();
		patient.setNom("TestEarlyOnset");
		patient.setAge(20);
		patient.setGenre("F");
		patient.setNote(Arrays.asList(
				"Le patient déclare qu'il lui est devenu difficile de monter les escaliers",
				"Il se plaint également d’être essoufflé",
				"Tests de laboratoire indiquant que les anticorps sont élevés",
				"Réaction aux médicaments",
				"Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
				"Le patient déclare avoir commencé à fumer depuis peu",
				"Hémoglobine A1C supérieure au niveau recommandé",
				"Taille, Poids, Cholestérol, Vertige et Réaction"
		));
		DiabetesRiskCalculatorService service = new DiabetesRiskCalculatorService();
		RiskLevel riskLevel = service.calculateRisk(patient);
		assertEquals(RiskLevel.EARLY_ONSET, riskLevel);
	}
}