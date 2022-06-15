package mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    private final int countDeck;  //количество палуб
    private List<Box> boxesOfShip; //список всех босов данногго корабля
    private final boolean isHorizontalPlacement; //ориентация корабля

    public List<Box> getBoxesOfShip() {
        return boxesOfShip;
    }

    public boolean isHorizontalPlacement() {
        return isHorizontalPlacement;
    }

    public int getCountDeck() {
        return countDeck;
    }

    public Ship(int countDeck, boolean isHorizontalPlacement) {
        this.countDeck = countDeck;
        this.isHorizontalPlacement = isHorizontalPlacement;
        boxesOfShip = new ArrayList<>(countDeck);
    }

    //метод создания корабля горизонтальной ориентации по заданным кординатам x и y
    public void createHorizontalShip(int x, int y) {
        //для того чтобы корабль не отрисовался за пределами игрового поля, высчитываем максимально возможную координату по X для отрисовки с учетом количества палуб
        int pointLimitValueForPaint = (Picture.COLUMNS - countDeck) * Picture.IMAGE_SIZE;
        for (int i = 0; i < countDeck; i++) {
            Box newBox;
            //если Х больше максимально допустимой точки отрисовки, то координата по Х для бокса = pointLimitValueForPaint + i * Picture.IMAGE_SIZE
            if (x > pointLimitValueForPaint) {
                newBox = new Box(Picture.SHIP, pointLimitValueForPaint + i * Picture.IMAGE_SIZE, y);
                boxesOfShip.add(newBox);
            } else {
                newBox = new Box(Picture.SHIP, (x + i * Picture.IMAGE_SIZE), y);
                boxesOfShip.add(newBox);
            }
        }
    }

    //аналогичный метод для создания корабля с горизонтальной ориентацией
    public void createVerticalShip(int x, int y) {
        int pointStartPaint = (Picture.ROWS - countDeck) * Picture.IMAGE_SIZE;
        for (int i = 0; i < countDeck; i++) {
            Box newBox;
            if (pointStartPaint < y) {
                newBox = new Box(Picture.SHIP, x, pointStartPaint + i * Picture.IMAGE_SIZE);
                boxesOfShip.add(newBox);
            } else {
                newBox = new Box(Picture.SHIP, x, (y + i * Picture.IMAGE_SIZE));
                boxesOfShip.add(newBox);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Ship ship = (Ship) o;
        return countDeck == ship.getCountDeck() && (boxesOfShip != null && ship.getBoxesOfShip() != null &&
                boxesOfShip.hashCode() == ship.getBoxesOfShip().hashCode());
    }
}
