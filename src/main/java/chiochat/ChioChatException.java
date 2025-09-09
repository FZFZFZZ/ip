package chiochat;

public class ChioChatException extends Exception {
    public ChioChatException(String message) {
        super(message);
    }

    public static class InvalidTaskID extends ChioChatException {
        public InvalidTaskID(int taskID) {
            super("Task ID " + taskID + " does not exist!");
        }
    }

    public static class EmptyInput extends ChioChatException {
        public EmptyInput() {
            super("Empty input is not allowed!");
        }
    }
}
