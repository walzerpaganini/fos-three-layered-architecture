package app.layers.c.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.layers.c.data.entities.Patient;

public interface PatientsRepository extends CrudRepository<Patient, Long> {
	
	/**
	 * Di default, i metodi dei repository di Spring Data selezionano dal DB tutti i
	 * campi delle entità. Se vogliamo selezionare soltanto alcuni campi (es. per 
	 * migliorare le prestazioni tralasciando i campi che non ci servono) possiamo
	 * farlo sfruttando le cosiddette proiezioni: scrivendo metodi che accettano
	 * una classe come parametro, Spring Data selezionerà soltanto le proprietà
	 * dell'entità che sono presenti anche sulla classe indicata come parametro.
	 * 
	 * Inoltre, Spring Data effettuerà un mapping automatico per convertire le entità
	 * effettivamente gestite dal repository in oggetti appartenenti alla classe ricevuta
	 * come parametro. Questo può essere molto utile per mantenere la segregazione tra il
	 * data layer e i layer superiori, in quanto il service layer può sfruttare questo
	 * meccanismo per evitare di lavorare direttamente con le entità relative al DB. 
	 * 
	 * Non è necessario che il parametro corrisponda a una classe vera e propria:
	 * la proiezione può essere effettuata anche utilizzando interfacce che espongono
	 * dei getter appropriati. Se utilizziamo delle classi, potrebbe essere necessario
	 * dotarle anche di setter o di costruttori appropriati.
	 * 
	 * (Vedi PatientsSummary.java)
	 * 
	 * ATTENZIONE: In alcuni casi, le proiezioni NON funzionano se il metodo ha lo stesso
	 * identico nome di un metodo già presente nell'interfaccia CrudRepository (es. findAll):
	 * può perciò capitare che venga invocata l'implementazione di default, che restituisce
	 * entità complete. Per evitare questo problema possiamo utilizzare nomi alternativi che
	 * corrispondano alla medesima funzionalità, sempre rispettando la sintassi supportata
	 * da Spring Data. Una buona alternativa al metodo "findAll" può essere "findBy".
	 */
	
	<T> List<T> findBy(Class<T> projection);
}
