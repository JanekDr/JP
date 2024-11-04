package Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EmployeesManager {
    private List<Employee> employees;

    public EmployeesManager(){
        this.employees = new ArrayList<>();
    }

    public EmployeesManager(List<Employee> employees){
        this.employees = employees;
        Employee.setEmployeeIdCounter(getMaxId());

    }

    public void addEmployee(String name){
        Employee employee = new Employee(name);
        employees.add(employee);
    }

    public Employee getEmployeeById(int id){
        Optional<Employee> optionalEmployee =  employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst();
        return optionalEmployee.orElse(null);
    }

    public List<Employee> getAllEmployees(){
        return employees;
    }

    public boolean deleteEmployee(int id){
        return employees.removeIf(employee -> employee.getId() == id);
    }

    private int getMaxId(){
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0);
    }

}
