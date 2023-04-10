package app.layers.c.data.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="medical_tests")
public class MedicalTest {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="patient_id")
	private Patient patient;
	
	private String description;
	private LocalDateTime dateTime;
	
	@OneToMany(mappedBy="medicalTest")
	private Set<MedicalTestResult> medicalTestResults;
	
	public MedicalTest() {}
	
	public MedicalTest(Patient patient, String description) {
		setPatient(patient);
		setDescription(description);
		setDateTime(LocalDateTime.now());
	}
	
	public Long getId() {
		return id;
	}
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Set<MedicalTestResult> getMedicalTestResults() {
		return medicalTestResults;
	}

	public void setMedicalTestResults(Set<MedicalTestResult> medicalTestResults) {
		this.medicalTestResults = medicalTestResults;
	}
}
