import javax.swing.*;

public class BuildWindow extends JFrame{

    JPanel pnl = new JPanel(); // создали объект JPanel

    public BuildWindow() {
        super("SWING window");
        setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(pnl);
        setVisible(true);
    }

    public static void main(String[] args) { //метод main такой же как и фушкция fun main в Котлин
        BuildWindow window = new BuildWindow();
    }
}
