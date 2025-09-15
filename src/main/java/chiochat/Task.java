package chiochat;
public class Task {
    private final String description;
    private boolean isCompleted;

    // constructor
    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    // getter for status
    public boolean isDone() {
        return isCompleted;
    }

    // change isDone status
    public void markState(boolean state) {
        isCompleted = state;
    }

    @Override
    public String toString() {
        return (isCompleted ? "[X] " : "[ ] ") + description;
    }
}
