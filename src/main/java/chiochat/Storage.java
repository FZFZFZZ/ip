package chiochat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Storage {
    private final String filePath;
    private final ArrayList<SavedMeta> metaHistory = new ArrayList<>();
    private final ArrayList<Task> chatHistory = new ArrayList<>();
    private final Ui ui;

    public Storage(String filePath) {
        this.filePath = filePath;
        ui = new Ui();
    }

    public ArrayList<Task> getChatHistory() {
        return this.chatHistory;
    }

    public int getChatHistorySize() {
        return this.chatHistory.size();
    }

    public ArrayList<SavedMeta> getMetaHistory() {
        return this.metaHistory;
    }

    public int getMetaHistorySize() {
        return this.metaHistory.size();
    }

    public static class SavedMeta {
        char type;        // 'T' | 'D' | 'E'
        boolean done;     // true = 1
        String payload;   // task description

        SavedMeta(char type, boolean done, String payload) {
            this.type = type; this.done = done; this.payload = payload;
        }

        String serialize() {
            // Use a fixed 3-field format: type|done|payload
            return (type + "|" + (done ? "1" : "0") + "|" + payload);
        }

        static SavedMeta parse(String line) throws Exception {
            String[] parts = line.split("\\|", 3);
            if (parts.length < 3) throw new Exception("Bad line: " + line);
            char type = parts[0].trim().charAt(0);
            boolean done = parts[1].trim().equals("1");
            String payload = parts[2];
            if (type != 'T' && type != 'D' && type != 'E') {
                throw new Exception("Unknown type: " + type);
            }
            return new SavedMeta(type, done, payload);
        }
    }

    private void ensureParentDir() {
        File f = new File(this.filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    public void saveToDisk() {
        ensureParentDir();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(this.filePath, StandardCharsets.UTF_8, false))) {
            for (SavedMeta meta : metaHistory) {
                bw.write(meta.serialize());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.print(this.ui.wrapOutput("Warning: failed to save tasks: " + e.getMessage()));
        }
    }

    public void loadFromDisk() {
        File f = new File(this.filePath);
        if (!f.exists()) return;

        ArrayList<Task> loadedTasks = new ArrayList<>();
        ArrayList<SavedMeta> loadedMeta = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) continue;

                SavedMeta meta = SavedMeta.parse(line);
                Task t;
                switch (meta.type) {
                    case 'T' -> t = new ToDoTask(meta.payload);
                    case 'D' -> t = new DeadlineTask(meta.payload);
                    case 'E' -> t = new EventTask(meta.payload);
                    default -> throw new IllegalStateException("Unexpected value: " + meta.type);
                }
                t.markState(meta.done);
                loadedTasks.add(t);
                loadedMeta.add(meta);
            }
            chatHistory.clear();
            chatHistory.addAll(loadedTasks);
            metaHistory.clear();
            metaHistory.addAll(loadedMeta);
        } catch (Exception e) {
            System.out.print(this.ui.wrapOutput("Warning: failed to load tasks: " + e.getMessage()));
            // If load fails, start with empty lists to avoid partial corruption.
            chatHistory.clear();
            metaHistory.clear();
        }
    }
}
