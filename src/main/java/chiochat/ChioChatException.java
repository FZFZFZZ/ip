package chiochat;

public class ChioChatException extends Exception {
    
    // parent class
    public ChioChatException(String message) {
        super(message);
    }

    // task ID invalid error
    public static class InvalidTaskID extends ChioChatException {
        public InvalidTaskID(int taskID) {
            super("Task ID " + taskID + " does not exist!");
        }
    }

    // empty input error
    public static class EmptyInput extends ChioChatException {
        public EmptyInput() {
            super("Empty input is not allowed!");
        }
    }
}
