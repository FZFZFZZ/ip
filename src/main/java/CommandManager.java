import java.util.Map;
import java.util.function.Consumer;

public class CommandManager {
    private final Ui ui;
    private final Storage storage;
    
    public final Map<String, Consumer<String>> COMMAND_MAP = Map.of(
        "bye", (input) -> handleBye(),
        "list", (input) -> handleList(),
        "mark", (input) -> markDoneState(input, true),
        "unmark", (input) -> markDoneState(input, false),
        "deadline", (input) -> addTask(input, "deadline"),
        "event", (input) -> addTask(input, "event"),
        "todo", (input) -> addTask(input, "todo"),
        "delete", (input) -> deleteTask(input)
    );

    private void addTask(String input, String taskType) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            System.out.print(this.ui.wrapOutput("OOPS!!! The description of a " + taskType + " cannot be empty."));
            return;
        }
        switch (taskType) {
            case "deadline" -> {
                DeadlineTask deadlineTask = new DeadlineTask(parts[1]);
                this.storage.getChatHistory().add(deadlineTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('D', false, parts[1]));
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + deadlineTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list."));
            }
            case "event" -> {
                EventTask eventTask = new EventTask(parts[1]);
                this.storage.getChatHistory().add(eventTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('E', false, parts[1]));
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + eventTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list."));
            }
            case "todo" -> {
                ToDoTask todoTask = new ToDoTask(parts[1]);
                this.storage.getChatHistory().add(todoTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('T', false, parts[1]));
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + todoTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list."));
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }

    private void deleteTask(String input) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistorySize()) {
                throw new ChioChatException.InvalidTaskID(taskId);
            }
            Task task = this.storage.getChatHistory().remove(taskId - 1);
            this.storage.getMetaHistory().remove(taskId - 1);
            this.storage.saveToDisk();
            this.ui.deleteMS(task, this.storage);
        } catch (ChioChatException.InvalidTaskID e) {
            System.out.print(this.ui.wrapError(e.getMessage()));
        }
    }

    public Storage getStorage() {
        return this.storage;
    }

    public void handleGreeting() {
        this.ui.greetMS();
    }

    private void handleBye() {
        this.ui.byeMS();
        System.exit(0);
    }

    private void handleList() {
        this.ui.showTaskList(this.storage);
    }

    private void markDoneState(String input, boolean state) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistorySize()) {
                throw new ChioChatException.InvalidTaskID(taskId);
            }
            Task task = this.storage.getChatHistory().get(taskId - 1);
            task.markState(state);
            this.storage.getMetaHistory().get(taskId - 1).done = state;
            this.storage.saveToDisk();
            this.ui.markStateMS(task, state);
        } catch (ChioChatException.InvalidTaskID e) {
            System.out.print(this.ui.wrapError(e.getMessage()));
        }
    }

    public CommandManager(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
    }


}
