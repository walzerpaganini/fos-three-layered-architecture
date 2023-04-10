package app.layers.c.data.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="patients")
public class Patient {
	@Id @GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	private LocalDate birthdate;
	
	@OneToMany(mappedBy="patient")
	private Set<MedicalTest> medicalTests;

	public Patient() {}
	
	public Patient(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	public Set<MedicalTest> getMedicalTests() {
		return medicalTests;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	@Transient
	public Integer getAge() {
		if(birthdate == null) {
			return null;
		}
				
		Period p = Period.between(birthdate, LocalDate.now());
		return p.getYears();
	}
}