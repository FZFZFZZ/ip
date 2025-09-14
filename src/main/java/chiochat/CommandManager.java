package chiochat;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;


public class CommandManager {
    private final Ui ui;
    private final Storage storage;
    
    // command map
    public final Map<String, Function<String, String>> COMMAND_MAP = Map.of(
        "list", (input) -> handleList(),
        "mark", (input) -> markDoneState(input, true),
        "unmark", (input) -> markDoneState(input, false),
        "deadline", (input) -> addTask(input, "deadline"),
        "event", (input) -> addTask(input, "event"),
        "todo", (input) -> addTask(input, "todo"),
        "delete", (input) -> deleteTask(input),
        "find", (input) -> findTask(input)
    );

    private String findTask(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return this.ui.wrapError("Please provide a keyword to search for.");
        }
        String keyword = parts[1].trim();
        ArrayList<Task> matchedTasks = this.storage.find(keyword);
        return this.ui.showFindResult(matchedTasks);
    }

    private String addTask(String input, String taskType) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            return this.ui.wrapOutput("OOPS!!! The description of a " + taskType + " cannot be empty.");
        }
        switch (taskType) {
            case "deadline" -> {
                DeadlineTask deadlineTask = new DeadlineTask(parts[1]);
                this.storage.getChatHistory().add(deadlineTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('D', false, parts[1]));
                this.storage.saveToDisk();
                return this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + deadlineTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list.");
            }
            case "event" -> {
                EventTask eventTask = new EventTask(parts[1]);
                this.storage.getChatHistory().add(eventTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('E', false, parts[1]));
                this.storage.saveToDisk();
                return this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + eventTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list.");
            }
            case "todo" -> {
                ToDoTask todoTask = new ToDoTask(parts[1]);
                this.storage.getChatHistory().add(todoTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('T', false, parts[1]));
                this.storage.saveToDisk();
                return this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + todoTask
                    + "\nNow you have " + this.storage.getChatHistorySize() + " tasks in the list.");
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }

    private String deleteTask(String input) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistorySize()) {
                throw new ChioChatException.InvalidTaskID(taskId);
            }
            Task task = this.storage.getChatHistory().remove(taskId - 1);
            this.storage.getMetaHistory().remove(taskId - 1);
            this.storage.saveToDisk();
            return this.ui.deleteMS(task, this.storage);
        } catch (ChioChatException.InvalidTaskID e) {
            return this.ui.wrapError(e.getMessage());
        }
    }

    // getter for storage instance used by manager
    public Storage getStorage() {
        return this.storage;
    }

    private String handleList() {
        return this.ui.showTaskList(this.storage);
    }

    private String markDoneState(String input, boolean state) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistorySize()) {
                throw new ChioChatException.InvalidTaskID(taskId);
            }
            Task task = this.storage.getChatHistory().get(taskId - 1);
            task.markState(state);
            this.storage.getMetaHistory().get(taskId - 1).done = state;
            this.storage.saveToDisk();
            return this.ui.markStateMS(task, state);
        } catch (ChioChatException.InvalidTaskID e) {
            return this.ui.wrapError(e.getMessage());
        }
    }

    // constructor
    public CommandManager(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
    }


}
