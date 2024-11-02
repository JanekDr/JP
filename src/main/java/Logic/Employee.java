package Logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable {
    private static int employeeIdCounter = 1;
    private int id;
    private String name;
    private List<Comment> comments;


    public Employee(String name) {
        this.id = employeeIdCounter++;
        this.name = name;
        this.comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public static void setEmployeeIdCounter(int newId) {
        employeeIdCounter = newId + 1;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void deleteComment(int commentId){
        comments.removeIf(comment -> comment.getCommentId()==commentId);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", nazwa: " + name;
    }
}
