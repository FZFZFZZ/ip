import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChioChat {
    private static final String DIVIDE_LINE = "____________________________________________________________\n";

    private static String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return DIVIDE_LINE + input + "\n" + DIVIDE_LINE;
    }

    private static final Map<String, Consumer<String>> COMMAND_MAP = Map.of(
        "bye", (input) -> handleBye(),
        "list", (input) -> handleList(),
        "mark", (input) -> markDoneState(input, true),
        "unmark", (input) -> markDoneState(input, false),
        "deadline", (input) -> addTask(input, "deadline"),
        "event", (input) -> addTask(input, "event"),
        "todo", (input) -> addTask(input, "todo")
    );

    private static void addTask(String input, String taskType) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            System.out.print(wrapOutput("Please provide a description for the " + taskType + " task."));
            return;
        }
        switch (taskType) {
            case "deadline" -> {
                DeadlineTask deadlineTask = new DeadlineTask(parts[1]);
                chatHistory.add(deadlineTask);
                System.out.print(wrapOutput("Got it. I've added this task:\n" + deadlineTask.toString()
                    + "\nNow you have " + chatHistory.size() + " tasks in the list."));
            }
            case "event" -> {
                EventTask eventTask = new EventTask(parts[1]);
                chatHistory.add(eventTask);
                System.out.print(wrapOutput("Got it. I've added this task:\n" + eventTask.toString()
                    + "\nNow you have " + chatHistory.size() + " tasks in the list."));
            }
            case "todo" -> {
                ToDoTask todoTask = new ToDoTask(parts[1]);
                chatHistory.add(todoTask);
                System.out.print(wrapOutput("Got it. I've added this task:\n" + todoTask.toString()
                    + "\nNow you have " + chatHistory.size() + " tasks in the list."));
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + taskType);
        }

    }

    private static void markDoneState(String input, boolean state) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > chatHistory.size()) {
                throw new ChioChatException("Task ID " + taskId + " does not exist!");
            }
            Task task = chatHistory.get(taskId - 1);
            if (state) {
                task.markAsDone();
                System.out.print(wrapOutput("Nice! I've marked this task as done:\n" + task.toString()));
            } else {
                task.markAsUndone();
                System.out.print(wrapOutput("OK, I've marked this task as not done yet:\n" + task.toString()));
            }
        } catch (NumberFormatException e) {
            System.out.print(wrapOutput("Please provide a valid number!"));
        } catch (ChioChatException e) {
            System.out.print(wrapOutput(e.getMessage()));
        } catch (Exception e) {
            System.out.print(wrapOutput("An error occurred: " + e.getMessage()));
        }
    }

    private static void handleBye() {
        System.out.print(wrapOutput("Bye. Hope to see you again soon!"));
        System.exit(0);
    }

    private static void handleGreeting() {
        System.out.print(wrapOutput("Hello! I'm ChioChat\nWhat can I do for you?"));
    }

    private static void handleList() {
        String res = "Here are the tasks in your list:\n";
        for (int i = 0; i < chatHistory.size(); i++) {
            res += (i + 1) + ". " + chatHistory.get(i).toString() + "\n";
        }
        System.out.print(wrapOutput(res));
    }

    private static final ArrayList<Task> chatHistory = new ArrayList<>();

    private static void defaultOperation(String input) {
        System.out.print("Please provide a valid command.");
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            handleGreeting();
            while (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                String request = input.split(" ")[0];
                COMMAND_MAP.getOrDefault(request, ChioChat::defaultOperation).accept(input);
            }
        }
    }
}
