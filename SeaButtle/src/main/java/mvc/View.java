package mvc;

import mvc.panels.ChoosePanel;
import mvc.panels.EnemyField;
import mvc.panels.MyField;
import mvc.panels.PanelButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;


public class View extends JFrame {

    private Controller controller;
    private Model model;
    private MyField myField; //панель нашего игрового поля
    private EnemyField enemyField; //панель игрового поля соперника
    private ChoosePanel choosePanel; //панель выбора настроек при добавлении корабля
    private PanelButtons panelButtons; //панель кнопок

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Battle Sea");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(Picture.getImage("icon"));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    //инициализация графического интерфейса
    public void init() {
        if (enemyField != null) {
            remove(enemyField);
            remove(myField);
            remove(panelButtons);
        }
        controller.loadEmptyMyField();
        add(choosePanel = new ChoosePanel(this), BorderLayout.EAST);
        add(myField = new MyField(this), BorderLayout.WEST);
        add(panelButtons = new PanelButtons(this), BorderLayout.SOUTH);
        myField.setChoosePanel(choosePanel);
        pack();
        revalidate();
        setVisible(true);
    }

    //метод для вызова информационного диалогового окна с заданныйм текстом
    public static void callInformationWindow(String message) {
        JOptionPane.showMessageDialog(
                null, message,
                "Внимание!", JOptionPane.ERROR_MESSAGE
        );
    }

    //метод для загрузки нашего пустого игровоо поля
    public void loadEmptyMyField() {
        controller.loadEmptyMyField();
        myField.repaint();            //переотрисовка нашего игрового поля
        //установка имени радиоБаттонов на панели выбора настроек добавления корабля
        choosePanel.setNameOneDeck(4);
        choosePanel.setNameTwoDeck(3);
        choosePanel.setNameThreeDeck(2);
        choosePanel.setNameFourDeck(1);
    }

    //добавление корабля
    public void addShip(Ship ship) {
        controller.addShip(ship);
    }

    //удаление корабля с нашего поля по координатам
    public Ship removeShip(int x, int y) {
        return controller.removeShip(x, y);
    }

    //метод, который изменяет имя у радиоБаттонов при удалении/добавлении кораблей по параметру число палуб
    public void changeCountShipOnChoosePanel(int countDeck) {
        switch (countDeck) {
            case 1: {
                //параметр - число кораблей которое осталось добавить (максимальное число кораблей данного типа -
                //число кораблей уже добавленных в соответствующий список в model
                choosePanel.setNameOneDeck(4 - model.getShipsOneDeck().size());
                break;
            }
            case 2: {
                choosePanel.setNameTwoDeck(3 - model.getShipsTwoDeck().size());
                break;
            }
            case 3: {
                choosePanel.setNameThreeDeck(2 - model.getShipsThreeDeck().size());
                break;
            }
            case 4: {
                choosePanel.setNameFourDeck(1 - model.getShipsFourDeck().size());
                break;
            }
        }
        choosePanel.revalidate();
    }

    //метод перерисовывает наше игровое поле
    public void repaintMyField(Graphics g) {
        Box[][] matrix = model.getMyField(); //получаем матрицу нашего поля
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j]; //присваиваем боксу значение элемента матрицы
                if (box == null) continue;
                //подгружаем картинку на панель нашего игрового поля
                g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), myField);
            }
        }
    }

    //метод перерисовывает игровое поле соперника
    public void repaintEnemyField(Graphics g) {
        Box[][] matrix = model.getEnemyField(); //получаем матрицу поля соперника
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j];
                if (box == null) continue;
                //если значение картинки = пустой клетки или клетки с кораблем то...
                if ((box.getPicture() == Picture.EMPTY || box.getPicture() == Picture.SHIP)) {
                    //если бокс открыт и картинка = пустая клетка, то отрисовываем эту клетку картинкой "пустая клетка с точкой"
                    if (box.isOpen() && box.getPicture() == Picture.EMPTY) {
                        g.drawImage(Picture.getImage(Picture.POINT.name()), box.getX(), box.getY(), enemyField);
                    }
                    //иначе если бокс открыт и картинка = клетка с кораблем, то отрисовываем эту клетку картинкой "клетка с зачеркнутым кораблем"
                    else if ((box.isOpen() && box.getPicture() == Picture.SHIP)) {
                        g.drawImage(Picture.getImage(Picture.DESTROY_SHIP.name()), box.getX(), box.getY(), enemyField);
                    }
                    //в остальных случаях отрисовываем клетку картинкой "закрытая клетка"
                    else g.drawImage(Picture.getImage(Picture.CLOSED.name()), box.getX(), box.getY(), enemyField);
                }
                //иначе, отрисовываем той картинкой, которая хранится в матрице - для клеток нумерации столбцов и строк
                else g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), enemyField);
            }
        }
    }

    //метод, который отрабатывает после нажатия кнопки СтартИгры
    public void startGame() {
        if (controller.checkFullSetShips()) { // проверка на то, что игрок добавил полный комплект кораблей
            //вызываем окно куда нужно вводить номер игровой комнаты, а также нажать кнопки "создать комнату" либо "подключиться к комнате"
            String[] options = {"Создать комнату", "Подключиться к комнате"};
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Создайте комнату, введя 4-ех значный номер комнаты,");
            JLabel label2 = new JLabel("либо подключитесь к уже созданной:");
            JTextField field = new JTextField(25);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label1);
            panel.add(label2);
            panel.add(field);

            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Создание комнаты:",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try {
                if (selectedOption == 0) { //если отжали кнопку "создать комнату"
                    int port = Integer.parseInt(field.getText().trim());
                    controller.createGameRoom(port); //создается игровая комната (запускается сервер с сокетными соединениями)
                    panelButtons.setTextInfo("ОЖИДАЕМ СОПЕРНИКА");
                    panelButtons.revalidate();
                    View.callInformationWindow("Ожидаем соперника: после того как соперник подключиться к комнате, появится уведомление. Затем начнется игра. Ваш ход первый.");
                    controller.connectToRoom(port); //коннект клиента к серверу
                    View.callInformationWindow("Второй игрок подключился! Можно начинать сражение.");
                    refreshGuiAfterConnect(); //обновление интерфейса клиента после подключения второго игрока
                    panelButtons.setTextInfo("СЕЙЧАС ВАШ ХОД");
                    panelButtons.getExitButton().setEnabled(true); //активация кнопки Выхода
                    enemyField.addListener(); //добавляем слушателя к объекту панели игрового поля соперника
                } else if (selectedOption == 1) { //если отжата кнопка "подключиться к комнате"
                    int port = Integer.parseInt(field.getText().trim());
                    controller.connectToRoom(port); //коннект клиента к серверу
                    View.callInformationWindow("Вы успешно подключились к комнате. Ваш соперник ходит первым.");
                    refreshGuiAfterConnect(); //обновление интерфейса клиента после подключения
                    panelButtons.setTextInfo("СЕЙЧАС ХОД СОПЕРНИКА");
                    new ReceiveThread().start(); //запуск нити, которая ожидает сообщение от сервера
                }
            } catch (Exception e) {
                View.callInformationWindow("Произошла ошибка при создании комнаты, либо некорректный номер комнаты, попробуйте еще раз.");
                e.printStackTrace();
            }
        } else View.callInformationWindow("Вы добавили не все корабли на своем поле!");
    }

    //метод отключеия клиента от сервера
    public void disconnectGameRoom() {
        try {
            controller.disconnectGameRoom();
            View.callInformationWindow("Вы отключились от комнаты. Игра окончена. Вы потерпели техническое поражение.");
            enemyField.removeListener(); //ужаляем слушателя у панели игрового поля соперника
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отключении от комнаты.");
        }
    }

    //обновляет интерфейс клиента после подключения обоих игроков
    public void refreshGuiAfterConnect() {
        MouseListener[] listeners = myField.getMouseListeners();
        for (MouseListener lis : listeners) {
            myField.removeMouseListener(lis); //удаление слушателя у панели нашего игрового поля
        }
        choosePanel.setVisible(false);
        remove(choosePanel);          //удаление панели настроек добавления корабля
        add(enemyField = new EnemyField(this), BorderLayout.EAST); //добавление панели игрового поля соперника
        enemyField.repaint(); //отрисовка поля соперника
        pack();  //репак формы
        panelButtons.getStartGameButton().setEnabled(false); //деактивация кнопки "Начать игру"
        revalidate();
    }

    //отправление на сервер сообщения с координатами отстреленной клетки
    public void sendShot(int x, int y) {
        try {
            boolean isSendShot = controller.sendMessage(x, y); //непосредственная отправка сообщения через контроллер
            if (isSendShot) { //если сообщение отправлено, то ...
                enemyField.repaint(); //переотрисовка поля соперника
                enemyField.removeListener(); //удаление слушателя у панели поля соперника
                panelButtons.setTextInfo("СЕЙЧАС ХОД СОПЕРНИКА");
                panelButtons.getExitButton().setEnabled(false); //деактивация кнопки выхода
                new ReceiveThread().start(); //запуск нити, которая ожидает сообщение от сервера
            }
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отправке выстрела.");
            e.printStackTrace();
        }
    }

    //класс-поток, который ожидает сообщение от сервера
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                boolean continueGame = controller.receiveMessage(); //контроллер принял сообщение
                myField.repaint();
                if (continueGame) { //если вернулось true, то...
                    panelButtons.setTextInfo("СЕЙЧАС ВАШ ХОД");
                    panelButtons.getExitButton().setEnabled(true); //активация кнопки выход
                    enemyField.addListener();  //добавление слушателя к полю соперника
                } else { //если вернлось false то игра окончена
                    panelButtons.setTextInfo("ИГРА ОКОНЧЕНА");
                    panelButtons.getExitButton().setEnabled(false);
                    enemyField.removeListener();
                    panelButtons.getRestartGameButton().setEnabled(true);
                }

            } catch (IOException | ClassNotFoundException e) {
                View.callInformationWindow("Произошла ошибка при приеме сообщения от сервера");
                e.printStackTrace();
            }
        }
    }
}
