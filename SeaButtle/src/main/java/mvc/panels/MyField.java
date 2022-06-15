package mvc.panels;

import mvc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyField extends JPanel {
    private View view;
    private ChoosePanel choosePanel;

    public void setChoosePanel(ChoosePanel choosePanel) {
        this.choosePanel = choosePanel;
    }

    public MyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
        this.addMouseListener(new ActionMouse());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintMyField(g);
    }

    //класс слушатель на нажатие кнопки мыши по панели
    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            //получаем координаты и округляем
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            //получаем кол-во палуб и ориентацию корабля
            int countDeck = choosePanel.getCountDeck();
            int placement = choosePanel.getPlacement();
            Ship ship;
            Ship removedShip;
            //если была нажата ЛКМ то добавляем корабль
            if (e.getButton() == MouseEvent.BUTTON1 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE)) {
                if (countDeck > 0 && countDeck <= 4) {
                    switch (placement) {
                        case 1: {
                            ship = new Ship(countDeck, false);
                            ship.createVerticalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        case 2: {
                            ship = new Ship(countDeck, true);
                            ship.createHorizontalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        default:
                            View.callInformationWindow("Не выбрана ориентацию размещения.");
                    }
                } else {
                    View.callInformationWindow("Не выбрано количество палуб.");
                    return;
                }
                //если ПКМ то удаляем орабль
            } else if (e.getButton() == MouseEvent.BUTTON3 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) &&
                    (removedShip = view.removeShip(x, y)) != null) {
                //меняем в имени радиоБаттона кол-во оставшихся кораблей в связи с удалением корабля
                view.changeCountShipOnChoosePanel(removedShip.getCountDeck());
            }
            repaint(); //переотрисовываем панель
            //меняем в имени радиоБаттона кол-во оставшихся кораблей в связи с добавлнием корабля
            view.changeCountShipOnChoosePanel(countDeck);
        }
    }
}
