package Logic;

import java.io.Serializable;
import java.time.LocalDate;

public class Comment implements Serializable {
    private static int commentIdCounter = 1;
    private int commentId;
    private LocalDate date;
    private String content;
    private int weight;
    private String type;

    public Comment(String content, int weight, String type, LocalDate date){
        this.commentId = commentIdCounter++;
        this.date = date;
        this.content = content;
        this.weight = weight;
        this.type = type;
    }

    public int getCommentId() {
        return commentId;
    }

    public static void setCommentIdCounter(int newId){
        commentIdCounter = newId + 1;
    }

    @Override
    public String toString() {
        return "ID: " + commentId + ", Typ: " + type + ", Waga: " + weight + ", Data: " + date + ", komentarz: " + content;
    }
}
