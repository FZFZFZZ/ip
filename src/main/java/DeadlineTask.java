public class DeadlineTask extends Task {
    private static final String ICON = "[D]";
    private String deadline;

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
            }
        } catch (Exception e) {
            deadline = "";
        }
    }

    @Override
    public String toString() {
        String baseString = ICON + super.toString();
        if (deadline != null && !deadline.isEmpty()) {
            return String.format("%s (by: %s)", baseString, deadline);
        }
        return baseString;
    }

    public String getDeadline() {
        return deadline;
    }
}