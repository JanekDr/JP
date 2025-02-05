package Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentManager {

    private EmployeesManager employeesManager;

    public CommentManager(EmployeesManager employeesManager) {
        this.employeesManager = employeesManager;
        Comment.setCommentIdCounter(getMaxId());
    }

    public boolean addComment(int employeeId, String content, int weight, String type, LocalDate date){
        if(employeesManager.getEmployeeById(employeeId) != null){
            Comment comment = new Comment(content,weight,type, date);
            Employee employee = employeesManager.getEmployeeById(employeeId);
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
        List<Employee> allEmployees = employeesManager.getAllEmployees();
        List<Comment> allComments = new ArrayList<>();
        for(Employee e:allEmployees){
            allComments.addAll(e.getComments());
        }
        return allComments;
    }

    public List<Comment> getCommentsByEmployeeId(int employeeId) {
        Employee employee = employeesManager.getEmployeeById(employeeId);
        if (employee != null) {
            return employee.getComments();
        } else {
            return null;
        }
    }

    private int getMaxId(){
        return employeesManager.getAllEmployees().stream()
                .flatMap(employee -> employee.getComments().stream())
                .mapToInt(Comment::getCommentId)
                .max()
                .orElse(0);
    }
}
