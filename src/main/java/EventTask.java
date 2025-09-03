import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventTask extends Task {
    private static final String ICON = "[E]";
    private String startTime;
    private String endTime;

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

    public EventTask(String description) {
        super(extractMainDescription(description));
        parseTimeFields(description);
    }

    private static String extractMainDescription(String fullDescription) {
        String desc = fullDescription;
        if (desc.contains("/from")) {
            desc = desc.substring(0, desc.indexOf("/from")).trim();
        }
        return desc;
    }

    private void parseTimeFields(String fullDescription) {
        try {
            int fromIndex = fullDescription.indexOf("/from");
            int toIndex = fullDescription.indexOf("/to");
            
            if (fromIndex != -1 && toIndex != -1) {
                // Extract time between /from and /to
                startTime = fullDescription.substring(fromIndex + 5, toIndex).trim();
                if (isStandardTimeRep(startTime)) {
                    startTime = convertToStandardTime(startTime);
                }
                // Extract time after /to
                endTime = fullDescription.substring(toIndex + 3).trim();
                if (isStandardTimeRep(endTime)) {
                    endTime = convertToStandardTime(endTime);
                }
            }
        } catch (Exception e) {
            startTime = "";
            endTime = "";
        }
    }

    @Override
    public String toString() {
        String baseString = ICON + super.toString();
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            return String.format("%s (from %s to %s)", baseString, startTime, endTime);
        }
        return baseString;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}