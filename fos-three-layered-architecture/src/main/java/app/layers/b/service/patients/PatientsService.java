package app.layers.b.service.patients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.layers.c.data.entities.Patient;
import app.layers.c.data.repositories.PatientsRepository;
import jakarta.persistence.EntityManager;

/**
 * L'annotazione @Service è un alias di @Component: si tratta semplicemente di un'annotazione
 * più descrittiva, adatta a documentare il fatto che questo particolare bean fa parte
 * del service layer della nostra applicazione.
 */

@Service
public class PatientsService {
	
	/**
	 * Similmente a come i controller del presentation layer non devono conoscere i dettagli
	 * della logica applicativa, allo stesso modo è bene che il service layer non conosca i
	 * dettagli tecnici di come i dati vengono persistiti e recuperati dal DB: questo è
	 * compito del data layer, le cui classi diventano dipendenze del service layer.
	 */
	
	private PatientsRepository patientsRepository;
	
	@Autowired
	public PatientsService(PatientsRepository patientsRepository) {
		this.patientsRepository = patientsRepository;
	}
	
	/**
	 * Le entità del data layer sono fortemente accoppiate alla struttura delle tabelle del
	 * database: per fare in modo che eventuali modifiche alle entità non ci costringano a
	 * modificare anche il presentation layer, uno dei compiti del service layer è quello
	 * di convertire le entità in classi meno rigide, più svincolate dalla struttura del DB.
	 * 
	 * L'approccio più naive consiste nel caricare le entità dal DB e poi costruire a mano
	 * gli oggetti del dominio applicativo (o viceversa, quando dobbiamo persistere i dati).
	 * Come potete immaginare, non si tratta di un sistema particolarmente efficace: spesso
	 * carichiamo dal DB più dati di quelli che realmente ci servono, e dobbiamo scrivere
	 * codice ripetitivo per istanziare gli oggetti che ci servono e valorizzarne tutti i
	 * campi leggendo i dati dagli oggetti o dalle entità di partenza.
	 *
	 * Quando il mapping è piuttosto semplice e diretto, si cerca di automatizzarlo il più
	 * possibile. In questo esempio, sfruttiamo le proiezioni dei repository di Spring Data
	 * per costruire un oggetto più adatto a essere restituito al layer dei controller (il
	 * che ci permette anche di caricare dal DB solo le informazioni che ci interessano).
	 * 
	 * In alternativa, potete anche usare l'EntityManager del modulo JPA di Spring Data,
	 * scrivendo query in linguaggio JPQL (o SQL) per selezionare soltanto i dati che vi
	 * servono ed effettuare il mapping verso le classi del dominio.
	 * 
	 * https://thorben-janssen.com/projections-with-jpa-and-hibernate/
	 */
	
	public List<PatientSummary> getAllPatients() {
		return patientsRepository.findBy(PatientSummary.class);
	}
}
