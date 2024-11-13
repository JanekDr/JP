package Main;

import GUI.DietBuilderGUI;
//import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            DietBuilderGUI gui = new DietBuilderGUI();
            gui.setVisible(true);
        });
    }
}
