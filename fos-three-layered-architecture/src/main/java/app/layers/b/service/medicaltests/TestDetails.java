package app.layers.b.service.medicaltests;

import java.time.LocalDateTime;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestDetails {
	private Long id;
	private Long patientId;
	private String description;
	private LocalDateTime dateTime;
	private Collection<TestResult> results;

	/**
	 * Se utilizziamo una classe invece di un'interfaccia per le proiezioni di Spring Data,
	 * è necessario che la classe esponga un costruttore con tutti i parametri da mappare.
	 * Classi come questa possono anche essere deserializzate dai controller REST, perciò
	 * possono risultare piuttosto comode per essere usate con l'annotazione @RequestBody.
	 * 
	 * Tuttavia, in generale non è una buona idea utilizzare la stessa classe sia per le
	 * proiezioni che per il body delle chiamate POST e PUT, perché molto spesso ci sono
	 * differenze tra i dati che vogliamo ricevere dai client e i dati che vogliamo inviare
	 * loro in risposta alle chiamate GET (es. vogliamo inviare l'ID di una risorsa, ma non
	 * vogliamo riceverlo dal client quando richiede di creare una risorsa nuova).
	 * 
	 * Una possibile soluzione per aggirare il problema senza creare due classi diverse è
	 * quella di utilizzare l'annotazione @JsonIgnore di Jackson per far sì che un certo
	 * setter non venga mai chiamato durante la deserializzazione degli oggetti ricevuti
	 * dal client. In quel caso, bisogna annotare il getter come @JsonProperty, altrimenti
	 * anche quest'ultimo verrà ignorato durante la serializzazione e il dato non verrà
	 * scritto negli oggetti JSON da inviare in risposta al client.
	 */
	
	public TestDetails(Long id, Long patientId, String description, LocalDateTime dateTime) {
		setId(id);
		setPatientId(patientId);
		setDescription(description);
		setDateTime(dateTime);
	}
	
	@JsonProperty
	public Long getId() {
		return id;
	}

	@JsonIgnore
	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
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

	public Collection<TestResult> getResults() {
		return results;
	}

	public void setResults(Collection<TestResult> results) {
		this.results = results;
	}
}
