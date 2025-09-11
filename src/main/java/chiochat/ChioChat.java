package chiochat;

import java.util.Scanner;

public class ChioChat {

    private static final String FILE_PATH = "../src/main/java/data/TaskDB.txt";
    private final CommandManager commandMgr;

    private ChioChat(String filePath) {
        this.commandMgr = new CommandManager(new Ui(), new Storage(filePath));
    }

    private void run() {
        commandMgr.getStorage().loadFromDisk();

        try (Scanner sc = new Scanner(System.in)) {
            commandMgr.handleGreeting();
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                try {
                    String request = Parser.parseRequest(input);
                    commandMgr.COMMAND_MAP
                              .getOrDefault(request, ChioChatException::new)
                              .accept(input);
                } catch (ChioChatException.EmptyInput e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new ChioChat(FILE_PATH).run();
    }
}

