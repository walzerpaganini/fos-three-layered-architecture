package app.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * In certi casi, potreste voler implementare dei meccanismi di autenticazione diversi da
 * quello di default di Spring Security che fa uso del bean di tipo UserDetailsService. In
 * quel caso, potete definire un vostro bean di tipo AuthenticationManager e implementare
 * il metodo .authenticate() secondo le vostre logiche: così facendo, Spring Security non
 * utilizzerà più lo UserDetailsService per recuperare le informazioni degli utenti, a meno
 * che non siate voi a farlo esplicitamente all'interno del vostro AuthenticationManager.
 */

// @Component
public class AuthManager implements AuthenticationManager {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		/**
		 * L'istanza di Authentication che riceviamo in ingresso è dotata di due metodi per
		 * recuperare le credenziali fornite dal client. Nel caso del protocollo HTTP Basic,
		 * il metodo .getPrincipal() restituisce lo username e .getCredentials() restituisce
		 * invece la password; tuttavia, questi metodi potrebbero significare cose diverse
		 * nel caso di altri protocolli di autenticazione.
		 */
		
		Object principal = authentication.getPrincipal();
		Object credentials = authentication.getCredentials();
		
		System.out.println("PRINCIPAL (User): " + principal);
		System.out.println("CREDENTIALS (Password): " + credentials);
				
		/**
		 * All'interno di questo metodo, dovremmo fare tutti i controlli necessario per 
		 * verificare che i dati forniti per l'autenticazione siano effettivamente validi,
		 * e raccogliere le eventuali "authorities" dell'utente.
		 * 
		 * Se i dati sono corretti, restituiamo un'implementazione di Authentication dotata
		 * delle "authorities" del caso; altrimenti, la specifica dell'interfaccia esige che
		 * solleviamo un'eccezione che sia sottoclasse di AuthenticationException.
		 * 
		 * Anche qui forniamo soltanto una finta implementazione di esempio.
		 */
		
		String username = "admin";
		String password = "pippo";
		
		if(!principal.toString().equals(username)) {
			throw new UnknownUserException("Utente sconosciuto");
		}
		
		if(!credentials.toString().equals(password)) {
			throw new WrongPasswordException("Password sbagliata");
		}
		
		Collection<? extends GrantedAuthority> authorities = List.of();
		
		return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
	}
}