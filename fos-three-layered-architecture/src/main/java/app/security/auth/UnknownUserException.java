package app.security.auth;

import org.springframework.security.core.AuthenticationException;

public class UnknownUserException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UnknownUserException(String msg) {
		super(msg);
	}
}
