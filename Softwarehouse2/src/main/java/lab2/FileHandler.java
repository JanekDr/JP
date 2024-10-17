package lab2;

import java.io.*;

public class FileHandler {
    public String filePath;

    public FileHandler(String filePath){
        this.filePath = filePath;
    }
    public String readFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder data = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            data.append(line);
            System.out.println(line);
        }

        return data.toString();
    }
}
