package chiochat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DeadlineTask extends Task {
    private static final String ICON = "[D]";
    private String deadline;

    private static final DateTimeFormatter INPUT_FORMAT_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_FORMAT_2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    private boolean isStandardTimeRep(String timeStr) {
        return tryParse(timeStr, INPUT_FORMAT_1) != null || tryParse(timeStr, INPUT_FORMAT_2) != null;
    }

    private String convertToStandardTime(String timeStr) {
        LocalDateTime dt = tryParse(timeStr, INPUT_FORMAT_1);
        if (dt == null) {
            dt = tryParse(timeStr, INPUT_FORMAT_2);
        }
        return dt != null ? dt.format(OUTPUT_FORMAT) : timeStr;
    }

    private LocalDateTime tryParse(String timeStr, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(timeStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // constructor
    public DeadlineTask(String description) {
        super(extractMainDescription(description));
        parseDeadline(description);
    }

    private static String extractMainDescription(String fullDescription) {
        String desc = fullDescription;
        if (desc.contains("/by")) {
            desc = desc.substring(0, desc.indexOf("/by")).trim();
        }
        return desc;
    }

    private void parseDeadline(String fullDescription) {
        try {
            int byIndex = fullDescription.indexOf("/by");
            if (byIndex != -1) {
                deadline = fullDescription.substring(byIndex + 3).trim();
                if (isStandardTimeRep(deadline)) {
                    deadline = convertToStandardTime(deadline);
                }
            }
        } catch (Exception e) {
            deadline = "";
        }
    }

    @Override
    public String toString() {
        String baseString = ICON + super.toString();
        if (deadline != null && !deadline.isEmpty()) {
            return String.format("%s (by %s)", baseString, deadline);
        }
        return baseString;
    }

    // getter for deadline stored
    public String getDeadline() {
        return deadline;
    }
}