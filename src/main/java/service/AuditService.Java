package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String FILE_NAME = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logDAOAction(String daoClass, String method, String details) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            writer.append(daoClass)
                    .append(",")
                    .append(method)
                    .append(",")
                    .append(details)
                    .append(",")
                    .append(timestamp)
                    .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}