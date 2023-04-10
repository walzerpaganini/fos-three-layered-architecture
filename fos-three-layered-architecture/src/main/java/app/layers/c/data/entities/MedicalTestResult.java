package app.layers.c.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="medical_test_results")
public class MedicalTestResult {
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private MedicalTest medicalTest;
	
	@Column(nullable=false)
	private String paramName;
	
	private Float paramValue;

	public MedicalTestResult() {}
	
	public MedicalTestResult(MedicalTest medicalTest, String paramName, Float paramValue) {
		this.medicalTest = medicalTest;
		this.paramName = paramName;
		this.paramValue = paramValue;
	}
	
	public Long getId() {
		return id;
	}

	public MedicalTest getMedicalTest() {
		return medicalTest;
	}

	public void setMedicalTest(MedicalTest medicalTest) {
		this.medicalTest = medicalTest;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Float getParamValue() {
		return paramValue;
	}

	public void setParamValue(Float paramValue) {
		this.paramValue = paramValue;
	}
}
