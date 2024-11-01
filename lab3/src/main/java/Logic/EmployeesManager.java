package Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeesManager {
    public List<Employee> employees = new ArrayList<>();
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

    public List<Employee> getEmployees(){
        return employees;
    }

    public boolean deleteEmployee(int id){
        return employees.removeIf(employee -> employee.getId() == id);
    }

}
