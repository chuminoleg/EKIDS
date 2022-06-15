package mvc;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private Box[][] myField = new Box[Picture.COLUMNS][Picture.ROWS]; //матрица с боксами нашего игрового поля
    private Box[][] enemyField = new Box[Picture.COLUMNS][Picture.ROWS];  //матрица с боксами игрового поля соперника
    private List<Ship> shipsOneDeck = new ArrayList<>(); //список всех наших однопалубных кораблей
    private List<Ship> shipsTwoDeck = new ArrayList<>(); //список всех наших двухпалубных кораблей
    private List<Ship> shipsThreeDeck = new ArrayList<>(); //список всех наших трехпалубных кораблей
    private List<Ship> shipsFourDeck = new ArrayList<>(); //список всех наших четырехпалубных кораблей
    private List<Ship> allShipsOfEnemy = new ArrayList<>(); //список всех кораблей соперника


    public void setAllShipsOfEnemy(List<Ship> allShipsOfEnemy) {
        this.allShipsOfEnemy = allShipsOfEnemy;
    }

    //метод, который возвращает список ВСЕХ наших ораблей
    public List<Ship> getAllShips() {
        List<Ship> allBoxesOfShips = new ArrayList<>();
        allBoxesOfShips.addAll(shipsFourDeck);
        allBoxesOfShips.addAll(shipsThreeDeck);
        allBoxesOfShips.addAll(shipsTwoDeck);
        allBoxesOfShips.addAll(shipsOneDeck);
        return allBoxesOfShips;
    }

    //метод, который возвращает ВСЕ боксы из ВСЕХ наших кораблей
    public List<Box> getAllBoxesOfShips() {
        List<Box> allBoxes = new ArrayList<>();
        List<Ship> allShips = getAllShips();
        for (Ship ship : allShips) {
            allBoxes.addAll(ship.getBoxesOfShip());
        }
        return allBoxes;
    }

    public List<Ship> getShipsOneDeck() {
        return shipsOneDeck;
    }

    public List<Ship> getShipsTwoDeck() {
        return shipsTwoDeck;
    }

    public List<Ship> getShipsThreeDeck() {
        return shipsThreeDeck;
    }

    public List<Ship> getShipsFourDeck() {
        return shipsFourDeck;
    }

    public Box[][] getMyField() {
        return myField;
    }

    public void setMyField(Box[][] myField) {
        this.myField = myField;
    }

    public Box[][] getEnemyField() {
        return enemyField;
    }

    public void setEnemyField(Box[][] enemyField) {
        this.enemyField = enemyField;
    }

    //метод, устанавливающий значение указанного бокс в указанную матрицу (игрового поля)
    public void addBoxInField(Box[][] fieldBox, Box box) {
        //по координатам бокса вычисляем индексы соответствующего места в матрице
        int i = box.getX() / Picture.IMAGE_SIZE;
        int j = box.getY() / Picture.IMAGE_SIZE;
        fieldBox[i][j] = box;
    }

    //метод, возвращающий бокс из указанной матрицы (игрового поля) по координатам панели отрисовки игрового поля
    public Box getBox(Box[][] field, int x, int y) {
        int i = x / Picture.IMAGE_SIZE;
        int j = y / Picture.IMAGE_SIZE;
        int lenght = field.length - 1;
        //если координаты указывают на элемент индекс которого больше размерности матрицы, то возвращаем null
        if (!(i > lenght || j > lenght)) {
            return field[i][j];
        }
        return null;
    }

    //метод, который устанавливает значение isOpen в true (открывает боксы после попадания в корабль) боксов, находящихся рядом с боксом корабля, определенного входными координатами
    public void openBoxAroundBoxOfShipEnemy(int x, int y, boolean isHorizontalPlacement) {
        //для горизонтально ориентированного корабля
        if (isHorizontalPlacement) {
            Box boxUp = getBox(enemyField, x, y - Picture.IMAGE_SIZE);
            if (boxUp != null) boxUp.setOpen(true);
            Box boxDown = getBox(enemyField, x, y + Picture.IMAGE_SIZE);
            if (boxDown != null) boxDown.setOpen(true);
        }
        //для вертиально ориентированного корабля
        else {
            Box boxLeft = getBox(enemyField, x - Picture.IMAGE_SIZE, y);
            if (boxLeft != null) boxLeft.setOpen(true);
            Box boxRight = getBox(enemyField, x + Picture.IMAGE_SIZE, y);
            if (boxRight != null) boxRight.setOpen(true);
        }
    }

    //открывает все пустые боксы по пириметру орабля, используется в случае, когда все палубы орабля подбиты
    public void openAllBoxesAroundShip(Ship ship) {
        //циклами открываем все боксы вокруг первого и последнего боксов корабля, т.к. больше 4 палуб быть не может, то
        //достаточно открыть все вокруг первой палубы и последней
        Box startBox = ship.getBoxesOfShip().get(0);
        Box endBox = ship.getBoxesOfShip().get(ship.getCountDeck() - 1);
        for (int i = startBox.getX() - Picture.IMAGE_SIZE; i <= startBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = startBox.getY() - Picture.IMAGE_SIZE; j <= startBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) box.setOpen(true);
            }
        }
        for (int i = endBox.getX() - Picture.IMAGE_SIZE; i <= endBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = endBox.getY() - Picture.IMAGE_SIZE; j <= endBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) box.setOpen(true);
            }
        }
    }

    //добавляет заданный корабль в соответствующий список кораблей в зависимости от количества палуб
    public void addShip(Ship ship) {
        int countDeck = ship.getCountDeck();
        switch (countDeck) {
            case 1: {
                //проверка - если в списке уже есть максимальное кол-во кораблей данного типа (кол-во палуб)
                //то вызывается соответствующее информационное окно, иначе добавляем корабль в нужный список
                if (shipsOneDeck.size() < 4) {
                    shipsOneDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else View.callInformationWindow("Перебор однопалубных. Максимально возможно - 4.");
                break;
            }
            case 2: {
                if (shipsTwoDeck.size() < 3) {
                    shipsTwoDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else View.callInformationWindow("Перебор двухпалубных. Максимально возможно - 3.");
                break;
            }
            case 3: {
                if (shipsThreeDeck.size() < 2) {
                    shipsThreeDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else View.callInformationWindow("Перебор трехпалубных. Максимально возможно - 2.");
                break;
            }
            case 4: {
                if (shipsFourDeck.size() < 1) {
                    shipsFourDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else View.callInformationWindow("Четырехпалубный уже добавлен. Максимально возможно - 1.");
                break;
            }
        }
    }


    //возвращает по боксу, в который произвели выстрел, корабль противника, если координаты боксШота равный координатам
    //бокса одного из корабля противника, иначе возвращаем null
    public Ship getShipOfEnemy(Box boxShot) {
        for (Ship ship : allShipsOfEnemy) {
            for (Box box : ship.getBoxesOfShip()) {
                if (boxShot.getX() == box.getX() && boxShot.getY() == box.getY()) {
                    return ship;
                }
            }
        }
        return null;
    }

    //удаляет корабль из соответствующего списка - используется в процессе расстановки
    // и удалении кораблей на своем игровом поле
    public void removeShip(Ship ship) {
        //если корабль содержится в одном из списков, то перебираем все боксы списка,
        //меняем значение их картинки на EMPTY и добавляем в матрицу нашего игрового поля этот бокс
        if (shipsOneDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsOneDeck.remove(ship);
            }
        } else if (shipsTwoDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsTwoDeck.remove(ship);
            }
        } else if (shipsThreeDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsThreeDeck.remove(ship);
            }
        } else if (shipsFourDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsFourDeck.remove(ship);
            }
        }
    }
}
