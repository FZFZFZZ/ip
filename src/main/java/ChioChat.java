import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChioChat {
    private static final String DIVIDE_LINE = "____________________________________________________________\n";

    private static String wrapOutput(String input) {
        return DIVIDE_LINE + input + "\n" + DIVIDE_LINE;
    }

    private static final Map<String, Consumer<String>> COMMAND_MAP = Map.of(
        "bye", (input) -> handleBye(),
        "list", (input) -> handleList()
    );

    private static void handleBye() {
        System.out.print(wrapOutput("Bye. Hope to see you again soon!"));
        System.exit(0);
    }

    private static void handleGreeting() {
        System.out.print(wrapOutput("Hello! I'm ChioChat\nWhat can I do for you?"));
    }

    private static void handleList() {
        String res = "";
        for (int i = 0; i < chatHistory.size(); i++) {
            res += (i + 1) + ". " + chatHistory.get(i) + "\n";
        }
        res = res.substring(0, res.length() - 1);
        System.out.print(wrapOutput(res));
    }

    private static final ArrayList<String> chatHistory = new ArrayList<>();

    private static void defaultOperation(String input) {
        chatHistory.add(input);
        System.out.print(wrapOutput("added: " + input));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        handleGreeting();
        while (sc.hasNextLine()) {
            String input = sc.nextLine().trim();
            COMMAND_MAP.getOrDefault(input, ChioChat::defaultOperation).accept(input);
        }
        sc.close();
    }
}
