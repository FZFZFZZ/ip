public class Parser {

    public static String parseRequest(String input) throws ChioChatException.EmptyInput {
        if (input == null || input.trim().isEmpty()) {
            throw new ChioChatException.EmptyInput();
        }
        return input.trim().split(" ")[0];
    }

}