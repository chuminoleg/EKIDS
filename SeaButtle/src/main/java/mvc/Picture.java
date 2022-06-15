package mvc;

import javax.swing.*;
import java.awt.*;

public enum Picture {

    CLOSED, //значение для картинки закрытого бокса
    DESTROY_SHIP, //для угичтоженной палубы корабля
    EMPTY, //для пустой клети
    NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9, NUM10, //для нумерации стро игрового поля
    POINT, //для пустого отстрелянного бокса
    SHIP, //для палубы корабля
    INFO, //для информации на панели выбора настроек размещения кораблей
    SYM1, SYM2, SYM3, SYM4, SYM5, SYM6, SYM7, SYM8, SYM9, SYM10; //для обозначения буквами столбцов
    public static final int COLUMNS = 11; //кол-во столбцов
    public static final int ROWS = 11; //ол-во строк
    public static final int IMAGE_SIZE = 40; //размер стороны картинки в писелах

    public static Image getImage(String nameImage) {
        String fileName = "C:\\Users\\Oleg_Chumin\\IdeaProjects\\SeaButtle\\src\\res\\img" + nameImage.toLowerCase() + ".png"; //для подгрузки имейджа из ресурсов
        //String fileName = "D:\\JAVA\\Pictures for BattleSea\\50\\" + nameImage.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(fileName);
        return icon.getImage();
    }
}
