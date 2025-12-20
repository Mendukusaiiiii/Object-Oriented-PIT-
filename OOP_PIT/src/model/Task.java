package model;

import java.awt.Color;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task {

    private int id;
    private String subject;
    private String title;
    private String description;
    private LocalDate dateCreated;
    private LocalDate dueDate;
    private boolean done;
    private Integer folderId;

    public Task(int id, String subject, String title, String description,
                LocalDate dateCreated, LocalDate dueDate,
                boolean done, Integer folderId) {

        this.id = id;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;
        this.done = done;
        this.folderId = folderId;
    }

    public int getId() { return id; }
    public String getSubject() { return subject; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isDone() { return done; }
    public Integer getFolderId() { return folderId; }

    public long daysRemaining() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public String timeRemainingText() {
        long d = daysRemaining();
        if (d < 0) return "Overdue";
        if (d == 0) return "Due Today";
        if (d == 1) return "1 day remaining";
        return d + " days remaining";
    }

    // SOLID COLOR ONLY
    public Color getUrgencyColor() {
        if (done) return Color.LIGHT_GRAY;
        long d = daysRemaining();
        if (d <= 1) return Color.RED;
        if (d <= 3) return Color.YELLOW;
        if (d <= 7) return Color.GREEN;
        return Color.GREEN;
    }
}