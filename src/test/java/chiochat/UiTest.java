package chiochat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UiTest {
    @Test
    public void testWrapOutput_Wraps() {
        Ui ui = new Ui();
        String input = "hello world\n";
        String expected = "______________________________________________\nhello world\n______________________________________________\n";
        assertEquals(expected, ui.wrapOutput(input));
    }
}
