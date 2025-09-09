public class Ui {
    private static final String DIVLINE = "______________________________________________\n";
    private static final String GREETING_MSG = "Hello! I'm ChioChat\nWhat can I do for you?";
    private static final String BYE_MSG = "Bye. Hope to see you again soon!";
    private static final String MARK_MSG = "Nice! I've marked this task as done:\n";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:\n";
    
    public Ui() {}

    public String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return DIVLINE + input + "\n" + DIVLINE;
    }

    public String wrapError(String input) {
        return "【ERROR】" + input;
    }

    public void deleteMS(Task task, Storage storage) {
        System.out.print(wrapOutput(
            "Noted. I've removed this task:\n" + task
            + "\nNow you have " + storage.getChatHistorySize() + " tasks in the list."));
    }

    public void greetMS() {
        System.out.print(wrapOutput(GREETING_MSG));
    }

    public void byeMS() {
        System.out.print(wrapOutput(BYE_MSG));
    }

    public void markStateMS(Task task, boolean state) {
        if (state) {
            System.out.print(wrapOutput(MARK_MSG + task));
        } else {
            System.out.print(wrapOutput(UNMARK_MSG + task));
        }
    }

    public void showTaskList(Storage storage) {
        StringBuilder res = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < storage.getChatHistorySize(); i++) {
            res.append(i + 1).append(". ").append(storage.getChatHistory().get(i)).append("\n");
        }
        System.out.print(wrapOutput(res.toString()));
    }
}
