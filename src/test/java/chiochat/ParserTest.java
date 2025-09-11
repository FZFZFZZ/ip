package chiochat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void testParseRequest_validInput() throws Exception {
        String input = "todo read book";
        String result = Parser.parseRequest(input);
        assertEquals("todo", result);
    }

    @Test
    public void testParseRequest_trimmedInput() throws Exception {
        String input = "   deadline submit report   ";
        String result = Parser.parseRequest(input);
        assertEquals("deadline", result);
    }

    @Test
    public void testParseRequest_emptyInput_throwsException() {
        Exception exception = assertThrows(ChioChatException.EmptyInput.class, () -> {
            Parser.parseRequest("   ");
        });
        assertNotNull(exception);
    }

    @Test
    public void testParseRequest_nullInput_throwsException() {
        Exception exception = assertThrows(ChioChatException.EmptyInput.class, () -> {
            Parser.parseRequest(null);
        });
        assertNotNull(exception);
    }
}
