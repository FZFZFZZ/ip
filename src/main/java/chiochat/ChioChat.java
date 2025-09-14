package chiochat;

import java.util.function.Function;

public class ChioChat {

    public final CommandManager commandMgr;

    public ChioChat(String filePath) {
        this.commandMgr = new CommandManager(new Ui(), new Storage(filePath));
    }

    public String getResponse(String input) throws ChioChatException.EmptyInput {
        String request = Parser.parseRequest(input);
        Function<String, String> command = commandMgr.COMMAND_MAP.get(request);
        if (command == null) {
            return "Sorry! I cannot understand the command...";
        }
        try {
            return command.apply(input);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

