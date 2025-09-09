import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChioChat {
    private final Ui ui;
    private final Storage storage;
    private static final String FILE_PATH = "./ip/src/main/java/data/TaskDB.txt";
    private final Map<String, Consumer<String>> COMMAND_MAP = Map.of(
        "bye", (input) -> handleBye(),
        "list", (input) -> handleList(),
        "mark", (input) -> markDoneState(input, true),
        "unmark", (input) -> markDoneState(input, false),
        "deadline", (input) -> addTask(input, "deadline"),
        "event", (input) -> addTask(input, "event"),
        "todo", (input) -> addTask(input, "todo"),
        "delete", (input) -> deleteTask(input)
    );

    private void deleteTask(String input) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistory().size()) {
                throw new ChioChatException("Task ID " + taskId + " does not exist!");
            }
            Task task = this.storage.getChatHistory().remove(taskId - 1);
            this.storage.getMetaHistory().remove(taskId - 1);
            this.storage.saveToDisk();
            System.out.print(this.ui.wrapOutput(
                "Noted. I've removed this task:\n" + task
                + "\nNow you have " + this.storage.getChatHistory().size() + " tasks in the list."));
        } catch (NumberFormatException e) {
            System.out.print(this.ui.wrapOutput("OOPS!!! Please provide a valid number!"));
        } catch (ChioChatException e) {
            System.out.print(this.ui.wrapOutput(e.getMessage()));
        } catch (Exception e) {
            System.out.print(this.ui.wrapOutput("An error occurred: " + e.getMessage()));
        }
    }

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
                    + "\nNow you have " + this.storage.getChatHistory().size() + " tasks in the list."));
            }
            case "event" -> {
                EventTask eventTask = new EventTask(parts[1]);
                this.storage.getChatHistory().add(eventTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('E', false, parts[1]));
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + eventTask
                    + "\nNow you have " + this.storage.getChatHistory().size() + " tasks in the list."));
            }
            case "todo" -> {
                ToDoTask todoTask = new ToDoTask(parts[1]);
                this.storage.getChatHistory().add(todoTask);
                this.storage.getMetaHistory().add(new Storage.SavedMeta('T', false, parts[1]));
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput(
                    "Got it. I've added this task:\n" + todoTask
                    + "\nNow you have " + this.storage.getChatHistory().size() + " tasks in the list."));
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }

    private void markDoneState(String input, boolean state) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > this.storage.getChatHistory().size()) {
                throw new ChioChatException("Task ID " + taskId + " does not exist!");
            }
            Task task = this.storage.getChatHistory().get(taskId - 1);
            if (state) {
                task.markAsDone();
                this.storage.getMetaHistory().get(taskId - 1).done = true;
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput("Nice! I've marked this task as done:\n" + task));
            } else {
                task.markAsUndone();
                this.storage.getMetaHistory().get(taskId - 1).done = false;
                this.storage.saveToDisk();
                System.out.print(this.ui.wrapOutput("OK, I've marked this task as not done yet:\n" + task));
            }
        } catch (NumberFormatException e) {
            System.out.print(this.ui.wrapOutput("Please provide a valid number!"));
        } catch (ChioChatException e) {
            System.out.print(this.ui.wrapOutput(e.getMessage()));
        } catch (Exception e) {
            System.out.print(this.ui.wrapOutput("An error occurred: " + e.getMessage()));
        }
    }

    private void handleBye() {
        System.out.print(this.ui.wrapOutput("Bye. Hope to see you again soon!"));
        System.exit(0);
    }

    private void handleGreeting() {
        System.out.print(this.ui.wrapOutput("Hello! I'm ChioChat\nWhat can I do for you?"));
    }

    private void handleList() {
        StringBuilder res = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < this.storage.getChatHistory().size(); i++) {
            res.append(i + 1).append(". ").append(this.storage.getChatHistory().get(i)).append("\n");
        }
        System.out.print(this.ui.wrapOutput(res.toString()));
    }

    public ChioChat(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
    }

    public void run() {
        // 1) Load previously saved tasks (if any)
        this.storage.loadFromDisk();

        // 2) Greet and start REPL
        try (Scanner sc = new Scanner(System.in)) {
            handleGreeting();
            while (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;
                String request = input.split(" ")[0];
                COMMAND_MAP.getOrDefault(request, ChioChatException::new).accept(input);
            }
        }
    }

    public static void main(String[] args) {
        new ChioChat(FILE_PATH).run();
    }
}

