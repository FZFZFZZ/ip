public class ToDoTask extends Task {
    private static final String ICON = "[T]";
    public ToDoTask(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return ICON + super.toString();
    }
}
