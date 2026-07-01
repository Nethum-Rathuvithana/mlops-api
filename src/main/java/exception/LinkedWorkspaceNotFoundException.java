package exception;

/**
 * Thrown when a model references a workspaceId that does not exist
 * in the data store.
 */
public class LinkedWorkspaceNotFoundException extends RuntimeException {

    public LinkedWorkspaceNotFoundException() {
        super("Referenced workspace does not exist");
    }

    public LinkedWorkspaceNotFoundException(String message) {
        super(message);
    }
}
