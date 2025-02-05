package lab2;

import java.util.List;

public class Staff {
    String name;
    List<String> roles;
    int maxProjects;
    int projectsAssigned;

    public Staff(String name, List<String> roles){
        this.name = name;
        this.roles = roles;
        this.projectsAssigned = 0;

        if(roles.size()==1 && (roles.contains("QA") || roles.contains("PM"))){
            this.maxProjects = 2;
        } else {
            this.maxProjects = 1;
        }
    }

    public boolean canBeAssigned(String role) {
        return projectsAssigned < maxProjects && roles.contains(role);
    }

    public void assignToProject(){
        projectsAssigned++;
    }
    public void reset() {
        this.projectsAssigned = 0;
    }
}