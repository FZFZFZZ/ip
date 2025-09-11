package chiochat;
public class Ui {
    private static final String DIVLINE = "______________________________________________\n";
    private static final String GREETING_MSG = "Hello! I'm ChioChat\nWhat can I do for you?";
    private static final String BYE_MSG = "Bye. Hope to see you again soon!";
    private static final String MARK_MSG = "Nice! I've marked this task as done:\n";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:\n";
    
    public Ui() {}

    // wrap text output in-between the div line
    public String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return DIVLINE + input + "\n" + DIVLINE;
    }

    // wrap error output
    public String wrapError(String input) {
        return "【ERROR】" + input;
    }

    // delete message
    public void deleteMS(Task task, Storage storage) {
        System.out.print(wrapOutput(
            "Noted. I've removed this task:\n" + task
            + "\nNow you have " + storage.getChatHistorySize() + " tasks in the list."));
    }

    // greet message
    public void greetMS() {
        System.out.print(wrapOutput(GREETING_MSG));
    }

    // bye message
    public void byeMS() {
        System.out.print(wrapOutput(BYE_MSG));
    }

    // mark completion status message
    public void markStateMS(Task task, boolean state) {
        if (state) {
            System.out.print(wrapOutput(MARK_MSG + task));
        } else {
            System.out.print(wrapOutput(UNMARK_MSG + task));
        }
    }

    // enumerate and output all the tasks in the storage
    public void showTaskList(Storage storage) {
        StringBuilder res = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < storage.getChatHistorySize(); i++) {
            res.append(i + 1).append(". ").append(storage.getChatHistory().get(i)).append("\n");
        }
        System.out.print(wrapOutput(res.toString()));
    }
}
