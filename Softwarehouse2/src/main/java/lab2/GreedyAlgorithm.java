package lab2;

import java.util.*;

public class GreedyAlgorithm {
    private final int TRIALS;

    public  GreedyAlgorithm(){
        TRIALS = 100;
    }
    public Map<Project, List<Staff>> greedyAssignment(List<Project> projects, List<Staff> staff) {
        Map<Project, List<Staff>> assignment = new HashMap<>();
        Random random = new Random();

        for (Project project : projects) {
            List<Staff> assignedStaff = new ArrayList<>();

            for (String role : project.rolesRequired) {
                List<Staff> candidates = new ArrayList<>();

                for (Staff s : staff) {
                    if (s.canBeAssigned(role)) {
                        candidates.add(s);
                    }
                }
                if (!candidates.isEmpty()) {
                    Staff selectedStaff = candidates.get(random.nextInt(candidates.size()));
                    selectedStaff.assignToProject();
                    assignedStaff.add(selectedStaff);
                } else {
                    assignedStaff.add(null);
                }
            }
            assignment.put(project, assignedStaff);
        }
        return assignment;
    }

    public int calculateFitness(Map<Project, List<Staff>> individual) {
        int fitness = 0;

        for (Map.Entry<Project, List<Staff>> entry : individual.entrySet()) {
            Project project = entry.getKey();
            List<Staff> assignedStaff = entry.getValue();
            boolean allRolesFilled = true;
            Set<String> uniqueRolesCovered = new HashSet<>();

            for (Staff s:assignedStaff) {
                if(s!=null){
                    uniqueRolesCovered.addAll(s.roles);
                }
            }

            List<String> uniqueRolesCoveredList = new ArrayList<String>(uniqueRolesCovered);
            Collections.sort(uniqueRolesCoveredList);
            ArrayList<String> rolesRequiredSorted = new ArrayList<String>(project.rolesRequired);
            Collections.sort(rolesRequiredSorted);

            if (uniqueRolesCoveredList.equals(rolesRequiredSorted)){
                fitness+=15;
            } else {
                int mismatches = 0;
                for (int i = 0; i < rolesRequiredSorted.size(); i++) {
                    if (i >= uniqueRolesCoveredList.size() || !uniqueRolesCoveredList.get(i).equals(rolesRequiredSorted.get(i))) {
                        mismatches++;
                    }
                }
                fitness += -5*mismatches;
            }
            for (int i = 0; i < project.rolesRequired.size(); i++) {
                String requiredRole = project.rolesRequired.get(i);
                Staff staff = assignedStaff.get(i);

                if (staff != null && staff.canBeAssigned(requiredRole)) {
                    fitness += 10;
                    if (staff.projectsAssigned > staff.maxProjects) {
                        fitness -= 5;
                    }
                } else {
                    allRolesFilled = false;
                }
            }
            if (allRolesFilled) {
                fitness += 20;
            }
        }
        return fitness;
    }

    public void getBestSolution(List<Project> projects, List<Staff> staff){

        Map<Project, List<Staff>> initAssignment = greedyAssignment(projects,staff);
        int bestScore = calculateFitness(initAssignment);
        Map<Project, List<Staff>> bestAssignment = initAssignment;
        resetStaff(staff);

        for (int i = 0; i < TRIALS; i++){
            Collections.shuffle(staff);
            Map<Project, List<Staff>>potentialBest = greedyAssignment(projects, staff);
            int potentialBestScore = calculateFitness(potentialBest);
            System.out.println("Aktualne dopasowanie wynosi: "+potentialBestScore+".");
            if(potentialBestScore>bestScore){
                bestScore = potentialBestScore;
                bestAssignment = potentialBest;
            }
            resetStaff(staff);
        }
        System.out.println("\nNAJLEPSZE DOPASOWANIE: \n");
        displayAssignment(bestAssignment);
        System.out.println("Ocena najlepszego dopasowania wynosi: "+bestScore);
    }

    public void displayAssignment(Map<Project, List<Staff>> assignment){
        for(Map.Entry<Project, List<Staff>> a : assignment.entrySet()){
            Project project = a.getKey();
            List<Staff> staf = a.getValue();
            System.out.println("Do projektu: " + project.name + " " + project.rolesRequired + "dopasowano pracownikow: ");
            for(Staff s:staf){
                if(s != null){
                    System.out.println(s.name+" "+s.roles);
                }
            }
        }
        System.out.println();
    }

    public void resetStaff(List<Staff> staff) {
        for (Staff s : staff) {
            s.reset();
        }
    }
}
