package Main;

import GUI.DietBuilderGUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DietBuilderGUI gui = new DietBuilderGUI();
            gui.setVisible(true);
        });
    }
}
