public class Ui {
    public static final String DIVLINE = "______________________________________________\n";
    
    public Ui() {}

    public String wrapOutput(String input) {
        if (input.endsWith("\n")) {
            input = input.substring(0, input.length() - 1);
        }
        return DIVLINE + input + "\n" + DIVLINE;
    }
}
