package chiochat;
public class Ui {
    private static final String MARK_MSG = "Nice! I've marked this task as done:\n";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:\n";
    
    public Ui() {}

    // wrap text output in-between the div line
    public String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return input + "\n";
    }

    // wrap error output
    public String wrapError(String input) {
        return "【ERROR】" + input;
    }

    // delete message
    public String deleteMS(Task task, Storage storage) {
        return wrapOutput(
            "Noted. I've removed this task:\n" + task
            + "\nNow you have " + storage.getChatHistorySize() + " tasks in the list.");
    }

    // mark completion status message
    public String markStateMS(Task task, boolean state) {
        if (state) {
            return wrapOutput(MARK_MSG + task);
        } else {
            return wrapOutput(UNMARK_MSG + task);
        }
    }

    // enumerate and output all the tasks in the storage
    public String showTaskList(Storage storage) {
        StringBuilder res = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < storage.getChatHistorySize(); i++) {
            res.append(i + 1).append(". ").append(storage.getChatHistory().get(i)).append("\n");
        }
        return wrapOutput(res.toString());
    }

    // show find result
    public String showFindResult(java.util.List<Task> matchedTasks) {
        StringBuilder res = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchedTasks.size(); i++) {
            res.append(i + 1).append(".").append(matchedTasks.get(i)).append("\n");
        }
        return wrapOutput(res.toString());
    }
}
