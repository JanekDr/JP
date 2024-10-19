package lab2;

import java.util.*;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.05;
    private static final int MAX_GENERATIONS = 1000;

    public static Map<Project, List<Staff>> generateRandomAllocation(List<Project> projects, List<Staff> staff) {
        Map<Project, List<Staff>> allocation = new HashMap<>();

        for (Project project: projects){
            List<Staff> assignedStaff = new ArrayList<>();
            Collections.shuffle(staff);

            for (String role : project.rolesRequired){
                for (Staff s : staff){
                    if(s.canBeAssigned(role)){
                        s.assignToProject();
                        assignedStaff.add(s);
                        break;
                    }
                }
            }
            allocation.put(project, assignedStaff);
        }
        return allocation;
    }

    public int calculateFitness(Map<Project, List<Staff>> allocation){
        int fitness = 0;
        for (Map.Entry<Project, List<Staff>> all: allocation.entrySet()){
            Project project = all.getKey();
            List<Staff> staf = all.getValue();
            Set<String> rolesInProject = new HashSet<>();
//            sprobowac w przyszlosci dawac mniej punktow za to ze jest więcej ról w projekcie niz jest to wymagane
            for(Staff s: staf) rolesInProject.addAll(s.roles);
            if(rolesInProject.containsAll(project.rolesRequired))fitness+=100;
        }
        return fitness;
    }

    public List<Map<Project, List<Staff>>> generatePopulation(List<Project> projects, List<Staff> staff){
        List<Map<Project, List<Staff>>> population = new ArrayList<>();
        for(int i = 0; i < POPULATION_SIZE; i++){
            population.add(generateRandomAllocation(projects, staff));
        }
        return population;
    }

    public Map<Project, List<Staff>> selection(List<Map<Project, List<Staff>>> population){
        Random rand = new Random();
        Map<Project, List<Staff>> best = population.get(rand.nextInt(population.size()));
        for (int i = 0; i<3; i++){
            Map<Project, List<Staff>> randPopulation = population.get(rand.nextInt(population.size()));
            if (calculateFitness(randPopulation) > calculateFitness(best)){
                best = randPopulation;
            }
        }
        return best;
    }
}
