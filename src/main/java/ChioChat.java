import java.util.Scanner;

public class ChioChat {
    private static String wrapOutput(String input) {
        StringBuilder res = new StringBuilder();
        res.append("____________________________________________________________\n");
        res.append(input).append("\n");
        res.append("____________________________________________________________\n");
        return res.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String greatingMSG = wrapOutput("Hello! I'm ChioChat\nWhat can I do for you?");
        System.out.println(greatingMSG);

        while (true) { 
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                System.out.println(wrapOutput("Bye. Hope to see you again soon!"));
                break;
            }
            System.out.println(wrapOutput(userInput));

        }
    }
}
