package app.layers.b.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Abbiamo qui un'implementazione di esempio dell'interfaccia UserDetailsService, da usare
 * come bean da parte di Spring Security per recuperare i dati di un certo utente a partire
 * dal suo username. A questo scopo, dobbiamo implementare il metodo loadUserByUsername().
 * Notate che il nostro compito non è verificare che le credenziali siano corrette, ma solo
 * fornire a Spring Security le informazioni necessarie per effettuare questa verifica.
 * 
 * In particolare, tra i dati che dobbiamo restituire dobbiamo includere la password dell'
 * utente (la quale sarà tipicamente criptata in qualche modo), in modo che Spring Security
 * possa confrontarla con quella fornita dal client per decidere se autenticarlo o meno.
 * 
 * Se l'utente corrispondente allo username è dotato di un qualche tipo di ruolo o permesso
 * speciale, dobbiamo anche includere tra i suoi dati le "authorities" corrispondenti. Anche
 * in questo caso, Spring Security userà le nostre informazioni per determinare se l'utente
 * può invocare l'endpoint richiesto oppure no.
 * 
 * Per questo esempio abbiamo un bean "finto", all'interno della quale costruiamo manualmente
 * il profilo di un unico utente conosciuto dal sistema. Naturalmente, in un caso reale le
 * informazioni sugli utenti si troveranno presumibilmente all'interno di un database, oppure
 * saranno fornite da un servizio di autenticazione esterno (es. Amazon Cognito).
 */

// @Service
public class UsersService implements UserDetailsService {
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired(required=false)
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		/**
		 * In caso di username non riconosciuti, dobbiamo sollevare un'eccazione di tipo
		 * UsernameNotFoundException, così come specificato dalla firma del metodo. In
		 * questo esempio, riconosciamo soltanto un utente con username "admin".
		 */
		
		if(!username.equals("admin")) {
			throw new UsernameNotFoundException(username);
		}
		
		/**
		 * Simuliamo qui la lettura della password dell'utente "admin". Se è presente un
		 * bean di tipo PasswordEncoder, Spring Security lo userà per criptare la password
		 * fornita dal client prima di confrontarla con quella fornita dal nostro servizio,
		 * perciò in questo esempio critpiamo la finta password in modo da permetterne il
		 * riconoscimento da parte del framework.
		 * 
		 * Il PasswordEncoder di default è in grado di utilizzare diversi algoritmi. Per
		 * capire qual'è l'algoritmo da usare per criptare una certa password, esso si basa
		 * su un prefisso racchiuso tra parentesi graffe all'inizio della password stessa:
		 * Per semplicità, se non viene trovato un PasswordEncoder personalizzato usiamo il
		 * prefisso "{noop}" per indicare che la password è salvata in chiaro. Va da sé che
		 * le password in chiaro possono essere comode in fase di sviluppo, ma vanno evitate
		 * a tutti i costi in produzione.
		 */
		
		String plainPassword = "pippo";
		
		String encodedPassword =
			passwordEncoder != null
				? passwordEncoder.encode(plainPassword)
				: "{noop}" + plainPassword;
		
		/**
		 * Per costruire un'implementazione dell'interfaccia UserDetails così come richiesto
		 * dalla firma del metodo, possiamo usare la classe User fornita da Spring Security.
		 * In questo esempio specifichiamo a mano le "authorities" di cui è dotato l'utente,
		 * ma naturalmente in un caso reale dovremmo recuperare anche questa informazione da
		 * un database o da un servizio esterno di qualche genere.
		 */
		
		return User
			.builder()
			.username("admin")
			.password(encodedPassword)
			.authorities("DOCTOR")
			.build();
	}
}
