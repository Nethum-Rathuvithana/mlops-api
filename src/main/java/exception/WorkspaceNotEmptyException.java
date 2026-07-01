package exception;

/**
 * Thrown when an attempt is made to delete a workspace that still has
 * one or more models linked to it.
 */
public class WorkspaceNotEmptyException extends RuntimeException {

    public WorkspaceNotEmptyException() {
        super("Workspace contains models");
    }

    public WorkspaceNotEmptyException(String message) {
        super(message);
    }
}
