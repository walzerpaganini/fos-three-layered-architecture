package app.layers.a.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.layers.b.service.medicaltests.TestSummary;
import app.layers.b.service.patients.PatientSummary;
import app.layers.b.service.patients.PatientsService;
import app.layers.c.data.entities.Patient;

@RestController
@RequestMapping("patients")
public class PatientsController {

	/**
	 * Nell'architettura a tre layer, i controller del presentation layer (anche chiamato 
	 * web layer) hanno la sola responsabilità di ricevere e rispondere alle chiamate HTTP,
	 * serializzando e deserializzando gli oggetti di scambio nei formati appropriati.
	 * 
	 * L'implementazione vera e propria delle funzionalità dell'applicazione è delegata alle
	 * classi del service layer, che vengono invocate da quelle del presentation layer. In
	 * questo modo, i controller non hanno conoscenza diretta di come le richieste vengono
	 * gestite: eventuali modifiche agli algoritmi utilizzati, ai meccanismi di persistenza,
	 * alle tabelle del DB, ecc. influiranno in maniera limitata sul codice dei controller.
	 */
	
	PatientsService patientsService;
	
	@Autowired
	public PatientsController(PatientsService patientsService) {
		this.patientsService = patientsService;
	}
	
	/*
	 * In genere, quando si riceve una chiamata GET su un endpoint che rappresenta una
	 * collezione di risorse (in questo caso un elenco di pazienti) non si restituiscono
	 * al client tutte le informazioni delle risorse, ma soltanto il loro identificativo
	 * con alcune informazioni riassuntive. Per esempio:
	 * 
	 * {
	 * 	 "id": 1,
	 *   "firstName": "Walter",
	 *   "lastName": "Paganini"
	 * }
	 * 
	 * Il client potrà poi reperire i dettagli di una singola risorsa chiamando un
	 * altro endpoint (es. "/patient/2"). A questo scopo, utilizziamo come tipo di
	 * ritorno un'interfaccia dotata di getter per le proprietà desiderate.
	 */
	
	@GetMapping
	public List<PatientSummary> getPatients() {
		List<PatientSummary> summaries = patientsService.getAllPatients();
		
		// Mapping verso un DTO
		
		return summaries;
	}
	
	// L'API RESTful risponde con l'entità appena creata.
	// Questo perché potrebbero esserci informazioni aggiuntive generate dal server.
	// Es. ID, timestamp esatto di creazione, ecc.
	
	@PostMapping
	public Patient createPatient(@RequestBody Patient patient) {				
		return null;
	}
	
	/**
	 * {
	 * 	 "id": 1,
	 *   "firstName": "Walter",
	 *   "lastName": "Paganini",
	 *   ...
	 *   ...
	 *   ...
	 * }
	 */
	
	@GetMapping("{id}")
	public Object getPatientDetails(@PathVariable long id) {
		return null;
	}
	
	/**
	 * Se abbiamo usato l'annotazione @EnableMethodSecurity valorizzando con true il
	 * parametro "securedEnabled", possiamo indicare tramite l'annotazione @Secured che
	 * l'endpoint necessita di autenticazione per poter essere invocato. Possiamo anche
	 * indicare una o più "authorities" che l'utente deve possedere per l'invocazione.
	 * 
	 * In realtà, non siete limitati a utilizzare questa annotazione soltanto all'interno
	 * dei controller: la si può usare anche nel service layer o (meno comunemente) in
	 * altri layer.
	 * 
	 * Se non valorizzate il parametro "securedEnabled" a true, potete comunque utilizzare
	 * altre annotazioni (es. @PreAuthorize e @PostAuthorize), il cui uso però risulta più
	 * complesso e specifico rispetto all'annotazione @Secured.
	 */
	
	@GetMapping("{id}/medical-tests")
	@Secured({})
	// @Secured("DOCTOR")
	// @Secured({ "DOCTOR", "ADMIN" })
	public List<TestSummary> getMedicalTestsOfPatient(@PathVariable long id) {
		return null;
	}
}
