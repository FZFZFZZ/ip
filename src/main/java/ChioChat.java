import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChioChat {
    private static final String DIVIDELINE = "______________________________________________\n";
    private static final String FILE_PATH = "../src/main/java/data/TaskDB.txt";

    private static String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return DIVIDELINE + input + "\n" + DIVIDELINE;
    }

    private static class SavedMeta {
        char type;        // 'T' | 'D' | 'E'
        boolean done;     // true = 1
        String payload;   // task description

        SavedMeta(char type, boolean done, String payload) {
            this.type = type; this.done = done; this.payload = payload;
        }

        String serialize() {
            // Use a fixed 3-field format: type|done|payload
            return (type + "|" + (done ? "1" : "0") + "|" + payload);
        }

        static SavedMeta parse(String line) throws Exception {
            String[] parts = line.split("\\|", 3);
            if (parts.length < 3) throw new Exception("Bad line: " + line);
            char type = parts[0].trim().charAt(0);
            boolean done = parts[1].trim().equals("1");
            String payload = parts[2];
            if (type != 'T' && type != 'D' && type != 'E') {
                throw new Exception("Unknown type: " + type);
            }
            return new SavedMeta(type, done, payload);
        }
    }

    private static void ensureParentDir() {
        File f = new File(FILE_PATH);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private static void saveToDisk() {
        ensureParentDir();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_PATH, StandardCharsets.UTF_8, false))) {
            for (SavedMeta meta : metaHistory) {
                bw.write(meta.serialize());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.print(wrapOutput("Warning: failed to save tasks: " + e.getMessage()));
        }
    }

    private static void loadFromDisk() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;

        ArrayList<Task> loadedTasks = new ArrayList<>();
        ArrayList<SavedMeta> loadedMeta = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) continue;

                SavedMeta meta = SavedMeta.parse(line);
                Task t;
                switch (meta.type) {
                    case 'T' -> t = new ToDoTask(meta.payload);
                    case 'D' -> t = new DeadlineTask(meta.payload);
                    case 'E' -> t = new EventTask(meta.payload);
                    default -> throw new IllegalStateException("Unexpected value: " + meta.type);
                }
                if (meta.done) {
                    t.markAsDone();
                } else {
                    t.markAsUndone();
                }
                loadedTasks.add(t);
                loadedMeta.add(meta);
            }
            chatHistory.clear();
            chatHistory.addAll(loadedTasks);
            metaHistory.clear();
            metaHistory.addAll(loadedMeta);
        } catch (Exception e) {
            System.out.print(wrapOutput("Warning: failed to load tasks: " + e.getMessage()));
            // If load fails, start with empty lists to avoid partial corruption.
            chatHistory.clear();
            metaHistory.clear();
        }
    }

    private static final Map<String, Consumer<String>> COMMAND_MAP = Map.of(
        "bye", (input) -> handleBye(),
        "list", (input) -> handleList(),
        "mark", (input) -> markDoneState(input, true),
        "unmark", (input) -> markDoneState(input, false),
        "deadline", (input) -> addTask(input, "deadline"),
        "event", (input) -> addTask(input, "event"),
        "todo", (input) -> addTask(input, "todo"),
        "delete", (input) -> deleteTask(input)
    );

    private static void deleteTask(String input) {
        try {
            int taskId = Integer.parseInt(input.split(" ")[1]);
            if (taskId <= 0 || taskId > chatHistory.size()) {
                throw new ChioChatException("Task ID " + taskId + " does not exist!");
            }
            Task task = chatHistory.remove(taskId - 1);
            metaHistory.remove(taskId - 1);
            saveToDisk();
            System.out.print(wrapOutput(
                "Noted. I've removed this task:\n" + task
                + "\nNow you have " + chatHistory.size() + " tasks in the list."));
        } catch (NumberFormatException e) {
            System.out.print(wrapOutput("OOPS!!! Please provide a valid number!"));
        } catch (ChioChatException e) {
            System.out.print(wrapOutput(e.getMessage()));
        } catch (Exception e) {
            System.out.print(wrapOutput("An error occurred: " + e.getMessage()));
        }
    }

    private static void addTask(String input, String taskType) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            System.out.print(wrapOutput("OOPS!!! The description of a " + taskType + " cannot be empty."));
            return;
        }
        switch (taskType) {
            case "deadline" -> {
                DeadlineTask deadlineTask = new DeadlineTask(parts[1]);
                chatHistory.add(deadlineTask);
                metaHistory.add(new SavedMeta('D', false, parts[1]));
                saveToDisk();
                System.out.print(wrapOutput(
                    "Got it. I've added this task:\n" + deadlineTask
                    + "\nNow you have " + chatHistory.size() + " tasks in the list."));
            }
            case "event" -> {
                EventTask eventTask = new EventTask(parts[1]);
                chatHistory.add(eventTask);
                metaHistory.add(new SavedMeta('E', false, parts[1]));
                saveToDisk();
                System.out.print(wrapOutput(
                    "Got it. I've added this task:\n" + eventTask
                    + "\nNow you have " + chatHistory.size() + " tasks in the list."));
            }
            case "todo" -> {
                ToDoTask todoTask = new ToDoTask(parts[1]);
                chatHistory.add(todoTask);
                metaHistory.add(new SavedMeta('T', false, parts[1]));
                saveToDisk();
                System.out.print(wrapOutput(
                    "Got it. I've added this task:\n" + todoTask
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
                metaHistory.get(taskId - 1).done = true;
                saveToDisk();
                System.out.print(wrapOutput("Nice! I've marked this task as done:\n" + task));
            } else {
                task.markAsUndone();
                metaHistory.get(taskId - 1).done = false;
                saveToDisk();
                System.out.print(wrapOutput("OK, I've marked this task as not done yet:\n" + task));
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
        StringBuilder res = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < chatHistory.size(); i++) {
            res.append(i + 1).append(". ").append(chatHistory.get(i)).append("\n");
        }
        System.out.print(wrapOutput(res.toString()));
    }

    private static final ArrayList<Task> chatHistory = new ArrayList<>();

    private static final ArrayList<SavedMeta> metaHistory = new ArrayList<>();

    private static void defaultOperation(String input) {
        try {
            throw new ChioChatException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        } catch (ChioChatException e) {
            System.out.print(wrapOutput(e.getMessage()));
        }
    }

    public static void main(String[] args) {
        // 1) Load previously saved tasks (if any)
        loadFromDisk();

        // 2) Greet and start REPL
        try (Scanner sc = new Scanner(System.in)) {
            handleGreeting();
            while (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;
                String request = input.split(" ")[0];
                COMMAND_MAP.getOrDefault(request, ChioChat::defaultOperation).accept(input);
            }
        }
    }
}

