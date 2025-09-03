public class EventTask extends Task {
    private static final String ICON = "[E]";
    private String startTime;
    private String endTime;

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
                // Extract time after /to
                endTime = fullDescription.substring(toIndex + 3).trim();
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
            return String.format("%s (from: %s to: %s)", baseString, startTime, endTime);
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