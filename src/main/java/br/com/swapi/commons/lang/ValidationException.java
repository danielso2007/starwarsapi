package br.com.swapi.commons.lang;

/**
 * Exceção lançada para indicar um erro de validação.
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 2131502432258359475L;

    private final Boolean validation = Boolean.TRUE;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public boolean isValidation() {
        return validation;
    }

}
