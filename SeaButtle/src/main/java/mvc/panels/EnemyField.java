package mvc.panels;

import mvc.Picture;
import mvc.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class EnemyField extends JPanel {
    private View view;

    public EnemyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintEnemyField(g);
    }

    //добавляет слушателя к панели поля соперника
    public void addListener() {
        addMouseListener(new ActionMouse());
    }

    //удаляет слушателя у панели поля соперника
    public void removeListener() {
        MouseListener[] listeners = getMouseListeners();
        for (MouseListener lis : listeners) {
            removeMouseListener(lis);
        }
    }

    //при событии (нажатие кнопки мыши по панели) получает координаты, округляет их, проверяет не за игровом полем
    //эти координаты и отправляет координаты сопернику
    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            if (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) {
                view.sendShot(x, y);
            }
        }
    }
}
