package Java.SWING;

import javax.swing.*;
import java.awt.*;

public class NewWindow extends JFrame {
    public NewWindow() {
        setTitle("Мое окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocation(320, 240);
        setVisible(true);
    }

    public static void main(String[] args) {
        NewWindow newWindow = new NewWindow();
    }
}
