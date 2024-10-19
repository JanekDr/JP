package lab2;

import java.io.*;
import java.util.List;

public class FileHandler {
    public String filePath;

    public FileHandler(String filePath){
        this.filePath = filePath;
    }
    public void parseFile(List<Project> projects, List<Staff>staff) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        boolean  parsingProjects = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals(""))continue;
            if(line.equals("PROJECTS")){
                parsingProjects = true;
                continue;
            } else if (line.equals("STAFF")) {
                parsingProjects = false;
                continue;
            }
            String[] splittedLine = line.split(":");
            String name = splittedLine[0].trim();
            List<String> roles = List.of(splittedLine[1].trim().split(" "));
            if (parsingProjects){
                projects.add(new Project(name, roles));
            } else {
                staff.add(new Staff(name, roles));
            }
        }
        reader.close();
    }
}
