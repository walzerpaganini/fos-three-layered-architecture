package app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Di default, quando aggiungiamo la dipendenza dal modulo Spring Security a una webapp
 * sviluppata in Spring Boot, tutti gli endpoint diventano automaticamente protetti, e
 * necessitano l'autenticazione da parte del client per poter essere invocati: in caso
 * contrario, tutte le chiamate HTTP otterrano in risposta un codice 401.
 * 
 * Per autenticare le chiamate HTTP, bisogna usare di default il protocollo HTTP Basic,
 * fornendo come credenziali lo username "user" e una password alfanumerica generata in
 * automatico all'avvio dell'applicazione (quest'ultima verrà stampata in console da
 * Spring Security). In alternativa, si possono specificare le credenziali richieste
 * per l'utenza di default nel file application.properties.
 * 
 * Naturalmente, la configurazione di default è praticamente inutile per la maggior parte
 * delle applicazioni, specialmente in ambiente di produzione. Per configurare il modulo
 * di sicurezza, si può esporre un bean che implementi l'interfaccia SecurityFilterChain:
 * come suggerisce il nome, bisognerà creare un oggetto che implementi una sequenza di
 * filtri di sicurezza che ogni richiesta HTTP dovrà superare prima di poter essere presa
 * in carico dalle componenti applicative vere e proprie della webapp.
 * 
 * L'uso (facoltativo) dell'annotazione @EnableMethodSecurity vi permette anche di impostare
 * le autorizzazioni necessarie per l'invocazione di un certo endoint tramite annotazioni da
 * apporre direttamente all'interno dei controller.
 * 
 * (Vedi PatientsController.java)
 */

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
	
	/**
	 * Per costruire una nostra SecurityFilterChain, possiamo scrivere un metodo annotato
	 * come @Bean che riceva in ingresso un oggetto di classe HttpSecurity: questa classe
	 * può essere usata per costruire un oggetto di classe DefaultSecurityFilterChain, e
	 * utilizzandola come parametro Spring ce ne inietterà automaticamente un'istanza.
	 * 
	 * In un primo stadio di sviluppo, potreste voler semplicemente disabilitare tutti i
	 * controlli di sicurezza: abbiamo qui un primo esempio di una catena configurata per
	 * ottenere questo risultato.
	 */
	
	// @Bean
	public SecurityFilterChain getUnsecuredFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			
			/**
			 * Questo primo metodo restituisce un oggetto che ci permette di impostare i
			 * controlli di sicurezza per un determinato pattern di richieste HTTP.
			 */
		
			.authorizeHttpRequests()
			
			/**
			 * In particolare, l'oggetto espone dei metodi che ci permettono di descrivere
			 * quali richieste intendiamo configurare. In questo caso, dichiariamo di voler
			 * applicare la successiva configurazione a tutte le chiamate HTTP, senza tener
			 * conto dell'endpoint chiamato o del verbo usato (es. GET, POST).
			 */
			
			.anyRequest()
			
			/**
			 * Otteniamo dunque un nuovo oggetto dotato di metodi per stabilire il tipo di
			 * controllo da effettuare. In questo caso, permettiamo tutte le chiamate che 
			 * rispondono al pattern indicato in precedenza, senza richiedere che il client
			 * sia autenticato o che possieda permessi speciali.
			 */
			
			.permitAll()
			
			/**
			 * I metodi precedenti ci portano a lavorare su oggetti diversi dalla catena 
			 * HttpSecurity che abbiamo ricevuto in ingresso. Se vogliamo aggiungere altre
			 * configurazioni alla catena, possiamo usare il metodo .and() per richiamare
			 * l'oggetto di classe HttpSecurity originale e chiamarne nuovamente i metodi. 
			 */
			
			.and()
			
			/** 
			 * Di default, Spring Security attiva una serie di protezioni contro le più
			 * comuni vulnerabilità a cui potrebbe essere soggetta una webapp. Nel caso
			 * degli attacchi CSRF, il server rifiuta tutte le chiamate capaci di causare
			 * side-effects (es. POST, PUT) fintanto che non configuriamo esplicitamente
			 * una strategia di prevenzione o finché non decidiamo di disabilitare del 
			 * tutto queste misure di sicurezza. Naturalmente, disabilitare i controlli
			 * in produzione sarebbe altamente inappropriato.
			 */
			
			.csrf().disable()
			
			/**
			 * Se utilizzate la console web del database H2 consultabile tramite browser
			 * (l'indirizzo viene scritto in console all'avvio dell'applicazione), dovete
			 * disabilitare anche questo controllo.
			 */
			
			.headers().frameOptions().disable();
		
		/**
		 * Una volta configurata la sicurezza, utilizziamo il metodo .build() per ottenere
		 * l'istanza vera e propria dell'interfaccia SecurityFilterChain.
		 */
		
		return httpSecurity.build();
	}
	
	/**
	 * Segue un esempio più realistico, nella quale andiamo a configurare una catena di
	 * sicurezza che limita l'accesso a certi endpoint soltanto ad alcune categorie di
	 * client autenticati.
	 * 
	 * Di default, Spring Security utilizza un bean di tipo UserDetailsService per capire
	 * se le credenziali fornite dal client corrispondono a un utente dell'applicazione
	 * oppure no. Se non forniamo esplicitamente un bean di questo tipo ne viene usato
	 * uno di default, il quale riconosce un unico utente dotato delle credenziali di
	 * default menzionate prima. Attenzione però: il vostro bean per l'autenticazione
	 * non verrà usato in alcun caso se non definite una vostra SecurityFilterChain.
	 * 
	 * A ogni utente possono essere associate delle "authorities" (nel caso più semplice
	 * sono stringhe di testo) che permettono di distinguere i diversi tipi di utenti in
	 * base ai ruoli e ai permessi che possiedono nel contesto della nostra webapp. In
	 * questo modo possiamo impedire a certi utenti di chiamare alcuni endpoint riservati
	 * a utenti più privilegiati (es. utenti semplici vs. amministratori del sistema).
	 * 
	 * (Vedi UsersService.java)
	 */
	
	// @Bean
	public SecurityFilterChain getSecuredFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity	
			/** 
			 * Configuriamo l'autorizzazione di specifici modelli di richiesta HTTP. Usando
			 * una specifica notazione, si possono intercettare le chiamate ricevute su più
			 * path (es. "/main/**" intercetta anche le chiamate ricevute su "/main/sub").
			 */

			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.POST, "/medical-tests")
			
			/** Soltanto gli utenti autenticati possono effettuare questa chiamata... */
			
			// .authenticated()
			
			/** ...oppure, indichiamo i ruoli o i permessi che l'utente deve possedere. */
			
			.hasAuthority("DOCTOR")
			// .hasRole("DOCTOR") // Stessa cosa di .hasAuthority("ROLE_DOCTOR")
			
			/** Indichiamo che tutte le altre richieste non necessitano di autenticazione. */
			
			.anyRequest()
			.permitAll()
			
			/** Per praticità, disabilitiamo anche qui alcuni controlli importanti. */
			
			.and()
			.csrf().disable()
		    .headers().frameOptions().disable()
		    
			/** 
			 * Possiamo configurare esplicitamente il protocollo di autenticazione. Non è
			 * particolarmente utile in questo caso, perché il protocollo HTTP Basic è già
			 * la scelta di default, ma siamo liberi di sceglierne altri (es. OAuth 2.0).
			 */
		    
		    .and()
		    .httpBasic();
		  		    
	    return httpSecurity.build();
	}

	/**
	 * In alcuni casi, potreste voler esporre anche un bean di tipo PasswordEncoder: esso
	 * verrà usato da Spring Security per codificare le password ricevute dai client in
	 * fase di autenticazione, in modo da poterle confrontare con le password (criptate)
	 * salvate sul database o su un servizio esterno di autenticazione. In questo esempio,
	 * esponiamo un PasswordEncoder fornito da Spring Security che usa l'algoritmo BCrypt.
	 * 
	 * Notate che Spring Security utilizza l'encoder SOLO in fase di autenticazione, e la
	 * responsabilità di memorizzare le password nella codifica corretta sta a noi: questo
	 * avverrà presumibilmente tramite specifici endpoint (es. chiamate POST su "/users"
	 * per creare nuovi account) gestiti da specifiche classi del service layer (in cui
	 * presumibilmente useremo lo stesso PasswordEncoder prima di persistere i dati).
	 * 
	 * Comunque, a meno che non abbiate esigenze particolari (es. state sviluppando una
	 * webapp che deve collegarsi a un vecchio database in cui sono già presenti delle
	 * password criptate secondo un certo algoritmo), il consiglio è quello di affidarsi
	 * al PasswordEncoder di default di Spring Security, il quale supporta nativamente
	 * diversi algoritmi ed è in grado di identificare l'algoritmo corretto per ciascuna
	 * singola password (in altre parole, potete decidere di passare a un nuovo algoritmo
	 * quando volete: esso verrà usato soltanto per le password salvate da quel momento
	 * in poi, mentre per le altre verrà usato il vecchio algoritmo).
	 * 
	 * https://www.baeldung.com/spring-security-5-default-password-encoder
	 * 
	 * (Vedi UsersService.java)
	 */
	
	// @Bean
	public PasswordEncoder getPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
