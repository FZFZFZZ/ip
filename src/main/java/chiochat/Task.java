package chiochat;
public class Task {
    private final String description;
    private boolean isDone;

    // constructor
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    // getter for status
    public boolean isDone() {
        return isDone;
    }

    // change isDone status
    public void markState(boolean state) {
        isDone = state;
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
