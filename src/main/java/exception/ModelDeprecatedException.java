package exception;

/**
 * Thrown when an attempt is made to add an evaluation metric to a
 * model whose status is DEPRECATED.
 */
public class ModelDeprecatedException extends RuntimeException {

    public ModelDeprecatedException() {
        super("Model is deprecated");
    }

    public ModelDeprecatedException(String message) {
        super(message);
    }
}
