package lab2;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        FileHandler file = new FileHandler(args[0]);
        FileHandler file = new FileHandler("C:/studia/JP/Lab2/Softwarehouse2/src/main/resources/data.txt");
        file.readFile();
    }
}
