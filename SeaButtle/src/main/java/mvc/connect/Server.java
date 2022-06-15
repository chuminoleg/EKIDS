package mvc.connect;

import mvc.Box;
import mvc.Ship;
import mvc.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Box[][] fieldPlayer1; //матрица игрового поля игрока 1
    private Box[][] fieldPlayer2; //матрица игрового поля игрока 2
    private List<Ship> allShipsPlayer1; //список кораблей игрока 1
    private List<Ship> allShipsPlayer2; //список кораблей игрока 2
    private volatile boolean allPlayersConnected = false; //флаг подключились ли все игроки
    private List<Connection> listConnection = new ArrayList<>(); //список коннекшенов всех игроков

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        startServer();
    }

    //запускает сервре
    private void startServer() {
        try {
            while (!allPlayersConnected) { //не всели игроки подключились?
                Socket socket = serverSocket.accept(); //принимаем подключение клиента
                if (listConnection.size() == 0) { //если в списке еще нет подключений, то...
                    Connection connection = new Connection(socket);
                    listConnection.add(connection); //добавляем коннекшн в список
                    connection.send(new Message(MessageType.ACCEPTED)); //отправляем клиенту о принятии
                    Message message = connection.receive();  //ждем от клиента матрицу его поля и спислк кораблей
                    if (message.getMessageType() == MessageType.FIELD) {
                        //устанавливаем поле и корабли в соотствующие поля
                        fieldPlayer1 = message.getGameField();
                        allShipsPlayer1 = message.getListOfAllShips();
                    }
                    //запускаем нить основного цикла общения клиента и сервера
                    new ThreadConnection(connection).start();
                }
                //аналогично для игрока 2, только после того как он пришлет поле и корабли, отправляем клиентам поля и корабли соперника
                else if (listConnection.size() == 1) {
                    Connection connection = new Connection(socket);
                    listConnection.add(connection);
                    connection.send(new Message(MessageType.ACCEPTED));
                    Message message = connection.receive();
                    if (message.getMessageType() == MessageType.FIELD) {
                        fieldPlayer2 = message.getGameField();
                        allShipsPlayer2 = message.getListOfAllShips();
                        connection.send(new Message(MessageType.FIELD, fieldPlayer1, allShipsPlayer1));
                        listConnection.get(0).send(new Message(MessageType.FIELD, fieldPlayer2, allShipsPlayer2));
                    }
                    new ThreadConnection(connection).start();
                    allPlayersConnected = true;
                }
            }
            serverSocket.close();
        } catch (Exception e) {
            View.callInformationWindow("Возникла ошибка при запуске сервера игровой комнаты.");
        }
    }

    //класс-поток исполнения для основного цикла общения клиента и сервера
    private class ThreadConnection extends Thread {
        private Connection connection;
        private volatile boolean stopCicle = false; //флаг прерывания цикла


        public ThreadConnection(Connection connection) {
            this.connection = connection;
        }

        private void mainCicle(Connection connection) {
            try {
                while (!stopCicle) {
                    Message message = connection.receive(); //принимаем сообщение от клиента
                    //если дисконнект, то перенаправляем сообщение клиенту противника и останавливаем цикл
                    if (message.getMessageType() == MessageType.DISCONNECT || message.getMessageType() == MessageType.DEFEAT) {
                        sendMessageEnemy(message);
                        stopCicle = true;
                    //если тип сообщения MY_DISCONNECT то просто останавливаем цикл
                    } else if (message.getMessageType() == MessageType.MY_DISCONNECT) {
                        stopCicle = true;
                    // в любом друго случае перенаправляем сообщение противнику
                    } else sendMessageEnemy(message);
                }
                connection.close();
            } catch (Exception e) {
                View.callInformationWindow("Ошибка при обмене выстрелами. Связь потеряна");
            }
        }

        //отправляет сообщение противнику
        private void sendMessageEnemy(Message message) throws IOException {
            for (Connection con : listConnection) {
                if (!connection.equals(con)) {
                    con.send(message);
                }
            }
        }

        @Override
        public void run() {
            mainCicle(connection);
        }
    }
}
