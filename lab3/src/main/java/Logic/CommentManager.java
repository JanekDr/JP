package Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentManager {
//    public List<Comment> comments = new ArrayList<>();
    private EmployeesManager employeesManager;

    public CommentManager(EmployeesManager employeesManager) {
        this.employeesManager = employeesManager;
    }

    public boolean addComment(int employeeId, String content, int weight, String type, LocalDate date){
        if(employeesManager.getEmployeeById(employeeId) != null){
            Comment comment = new Comment(content,weight,type, date);
            Employee employee = employeesManager.getEmployeeById(employeeId);
//            comments.add(comment);
            employee.addComment(comment);
            return true;
        } else {
            return false;
        }
    }

    public int deleteComment(int employeeId, int commentId){
        if(employeesManager.getEmployeeById(employeeId) == null)return -1;
        if(getCommentById(employeeId, commentId)==null)return -2;

        Employee employee = employeesManager.getEmployeeById(employeeId);
//        comments.removeIf(comment -> employee.getId() == commentId);
        employee.deleteComment(commentId);
        return 1;
    }

    public Comment getCommentById(int employeeId, int commentId){
        if(employeesManager.getEmployeeById(employeeId) != null) {

            Employee employee = employeesManager.getEmployeeById(employeeId);
            Optional<Comment> optionalComment = employee.getComments().stream()
                    .filter(comment -> comment.getCommentId()==commentId)
                    .findFirst();

            return optionalComment.orElse(null);
        }else{
            return null;
        }

    }

    public List<Comment> getAllComments() {
        List<Employee> allEmployees = employeesManager.getEmployees();
        List<Comment> allComments = new ArrayList<>();
        for(Employee e:allEmployees){
            allComments.addAll(e.getComments());
        }
        return allComments;
    }
}
