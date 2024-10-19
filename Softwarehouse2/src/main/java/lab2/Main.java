package lab2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
//        FileHandler file = new FileHandler(args[0]);
        FileHandler file = new FileHandler("C:/java/JP-lab/lab2/JP/Softwarehouse2/src/main/resources/data.txt");
        List<Project> projects = new ArrayList<>();
        List<Staff> staff = new ArrayList<>();
        file.parseFile(projects, staff);
        GeneticAlgorithm algo = new GeneticAlgorithm();
        Map<Project, List<Staff>>allocation = algo.generateRandomAllocation(projects, staff);

//        for (Map.Entry<Project, List<Staff>> all: allocation.entrySet()){
//            Project project = all.getKey();
//            List<Staff> staf = all.getValue();
//            System.out.println(project.name+" "+ project.rolesRequired);
//            for(Staff s:staf){
//                System.out.println(s.name+" "+s.roles+" "+s.projectAssigned);
//            }
//            System.out.println();
//        }
        for (int i=0;i<50;i++){
            file.parseFile(projects, staff);
            allocation = algo.generateRandomAllocation(projects, staff);
            System.out.println(algo.calculateFitness(allocation));
        }

    }
}
