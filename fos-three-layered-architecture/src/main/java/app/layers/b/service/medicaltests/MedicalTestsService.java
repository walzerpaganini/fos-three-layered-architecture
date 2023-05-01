package app.layers.b.service.medicaltests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import app.layers.c.data.entities.MedicalTest;
import app.layers.c.data.entities.MedicalTestResult;
import app.layers.c.data.entities.Patient;
import app.layers.c.data.repositories.MedicalTestResultsRepository;
import app.layers.c.data.repositories.MedicalTestsRepository;

@Service
public class MedicalTestsService {
	
	private MedicalTestsRepository medicalTestsRepo;
	private MedicalTestResultsRepository medicalTestResultsRepo;

	@Autowired
	public MedicalTestsService(MedicalTestsRepository medicalTestsRepo, MedicalTestResultsRepository medicalTestResultsRepo) {
		this.medicalTestsRepo = medicalTestsRepo;
		this.medicalTestResultsRepo = medicalTestResultsRepo;
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.REQUIRED)
	public List<TestSummary> getAllTests() {
		medicalTestsRepo.findBy(TestSummary.class);

		// se qualcuno inserisce nella tabelle un nuovo record
		// (quindi non una modifica a un record già esistente)
		
		return medicalTestsRepo.findBy(TestSummary.class);
	}

	/**
	 * Abbiamo qui un ottimo caso di esempio in cui la struttura relazionale delle tabelle
	 * del DB (e dunque delle entità) non combacia perfettamente con la struttura degli
	 * oggetti scambiati tra il service layer e il presentation layer, decisamente più
	 * object-oriented. Per il service layer, un oggetto che rappresenta un esame medico
	 * include al suo interno, per definizione, anche le misurazioni di ogni parametro
	 * clinico previsto dall'esame; sul DB, al contrario, l'esame e i suoi risultati
	 * sono salvati su due tabelle separate, legate tra loro tramite chiavi esterne.
	 * 
	 * Diventa quindi compito del service layer invocare le classi del data layer in modo
	 * che la discrepanza risulti del tutto opaca per i layer superiori (in questo caso per
	 * il presentation layer, e a maggior ragione per il client). Anche in questo caso, le
	 * proiezioni supportate dai repository di Spring Data ci aiutano notevolmente a rendere
	 * più automatico il mapping tra le entità e gli oggetti del dominio applicativo.
	 */
	
	public TestDetails getTestDetails(Long id) {
		TestDetails testDetails = medicalTestsRepo.findById(id, TestDetails.class);
		List<TestResult> testResults = medicalTestResultsRepo.findAllByMedicalTestId(id, TestResult.class);
		
		testDetails.setResults(testResults);
		
		return testDetails;
	}
	
	/**
	 * La discrepanza tra la struttura degli oggetti applicativi e la struttura del DB pone
	 * qui un'ulteriore difficoltà. Il service layer ha la responsabilità di effettuare il
	 * salvataggio di un esame in maniera opaca: agli occhi di chi invoca il service layer,
	 * il fatto che il salvataggio di un esame preveda in realtà diverse scritture su due
	 * diverse tabelle deve essere del tutto nascosto: la richiesta del client (in questo
	 * caso il presentation layer) è quella di salvare un esame sul DB, non importa come.
	 * 
	 * Ma che succede se si verifica un'eccezione nel mezzo delle operazioni di scrittura?
	 * In questo esempio, c'è il rischio è che venga salvato un record sulla tabella degli
	 * esami, senza che però vengano salvati anche i risultati dell'esame: in altre parole,
	 * l'applicazione avrebbe compiuto soltanto metà del lavoro, creando una situazione di
	 * inconsistenza sul DB.
	 * 
	 * La cosa migliore è che questa operazione avvenga in maniera atomica: le scritture
	 * devono andare tutte a buon fine, oppure l'intera operazione deve essere annullata
	 * in caso di errori. A questo scopo, ci viene in aiuto l'annotazione @Transactional
	 * di Spring: il framework farà in modo che le operazioni di persistenza di questo
	 * metodo vengano eseguite all'interno di un'unica transazione del DB, la quale verrà
	 * annullata in caso di RuntimeException, oppure committata soltanto al termine delle
	 * istruzioni contenute nel corpo del metodo.
	 * 
	 * Un altro possibile modo di avviare, annullare, o committare una transazione è quello
	 * di farsi iniettare come dipendenza l'EntityManager di JPA, il quale espone metodi
	 * che possiamo utilizzare per effettuare manualmente queste operazioni.
	 * @throws Exception 
	 */

	@Transactional(rollbackFor = { Exception.class })
	public TestDetails saveTest(TestDetails testDetails) throws Exception {		
		MedicalTest testEntity = new MedicalTest();
		
		Patient p = new Patient();
		p.setId(testDetails.getPatientId());
		
		testEntity.setPatient(p);
		testEntity.setDescription(testDetails.getDescription());
		
		// Senza @Transactional, questa operazione verrebbe committata immediatamente.
		medicalTestsRepo.save(testEntity);

		// Scommentate questo blocco di codice per simulare il verificarsi di un errore
		// nel mezzo delle operazioni di scrittura sulle tabelle del DB.
		
		/*
		if(p.getId() != null) {
			throw new Exception("Something went wrong while saving the test.");
		}
		*/
				
		for(TestResult res : testDetails.getResults()) {
			MedicalTestResult resultEntity = new MedicalTestResult(testEntity, res.getParamName(), res.getParamValue());
			medicalTestResultsRepo.save(resultEntity);
		}
		
		testDetails.setId(testEntity.getId());
				
		return testDetails;
	}
}
