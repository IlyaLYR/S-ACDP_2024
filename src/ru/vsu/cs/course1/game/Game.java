package ru.vsu.cs.course1.game;

import ru.vsu.cs.util.SwingUtils;

/**
 * Класс, реализующий логику игры
 */
public class Game {
    /**
     * объект Random для генерации случайных чисел
     * (можно было бы объявить как static)
     */
    public enum CellState {
        OPENED,
        CLOSED,
        HORSE,
        RESULT,
        END
    }

    public class Cell {
        public CellState state;
        public int value;

        public Cell(CellState state, int value) {
            this.state = state;
            this.value = value;
        }
    }

    /**
     * двумерный массив для хранения игрового поля
     * (в данном случае цветов, 0 - пусто; создается / пересоздается при старте игры)
     */
    private Cell[][] field = null;


    public Game() {
    }

    public void newGame(int rowCount, int colCount) {
        // создаем поле
        field = new Cell[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                field[row][col] = new Cell(CellState.OPENED, 0); /////////Поменял
            }
        }
    }

    public void leftMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }
        field[row][col].state = CellState.CLOSED;
    }

    public void middleMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }
        field[row][col].state = CellState.END;
    }

    public void rightMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }
        if (field[row][col].state == CellState.OPENED) {
            field[row][col].state = CellState.HORSE;
        } else field[row][col].state = CellState.OPENED;
    }

    public int StartClick() { //начало программы
        clean();
        try {
            int[] cord = coordinates(); //начало и конец
            queue.add(new int[]{cord[0], cord[1]}); //начало в очередь
            while (!queue.isEmpty() && Flag) {
                int[] coordinates = queue.poll();
                towardsGoal(coordinates[0], coordinates[1], cord[2], cord[3]);
            }
            if (queue.isEmpty() && Flag) {
                SwingUtils.showInfoMessageBox("Решений нет!", "Сообщение");
            } else {
                wayToHorse(cord[2], cord[3]);
                return field[cord[2]][cord[3]].value;
            }
        } catch (Exception e) {
            SwingUtils.showInfoMessageBox("Некорретно заданы точки начала и конца", "Ошибка");
        }
        return 0;
    }


    private int[] coordinates() { // координаты начало и конца
        int[] array = new int[4];
        int horse = 0;
        int end = 0;
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                if (cellValue.state == CellState.HORSE) {
                    horse++;
                    array[0] = row;
                    array[1] = col;
                }
                if (cellValue.state == CellState.END) {
                    end++;
                    array[2] = row;
                    array[3] = col;
                }
            }
        }
        if (horse != 1 || end != 1) {
            return null;
        }
        return array;
    }

    public void clean() { //очистка поля
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                field[row][col].value = 0;
                if (cellValue.state == CellState.RESULT) {
                    field[row][col].state = CellState.OPENED;
                }
            }
        }
        Flag = true;
        queue.clear();
    }

    public void clean_field() { //метод очистки поля
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                field[row][col].value = 0;
                if (cellValue.state != CellState.OPENED) {
                    field[row][col].state = CellState.OPENED;
                }
            }
        }
    }

    int[][] solve = {
            {-2, -2, -1, 1, 2, 2, 1, -1}, //массив с решениями хода коня
            {-1, 1, 2, 2, 1, -1, -2, -2}
    };

    MyQueue<int[]> queue = new MyQueue<>();
    boolean Flag = true;

//    public void knightMove(int row, int col, int N, int value) { //ход конем
//        while (N > 0) {
//            for (int i = 0; i < solve[0].length; i++) {
//                if (chechInPlace(row + solve[0][i], col + solve[1][i])) {
//                    field[row + solve[0][i]][col + solve[1][i]].state = CellState.RESULT;
//                    field[row + solve[0][i]][col + solve[1][i]].value = value;
//                    knightMove(row + solve[0][i], col + solve[1][i], N - 1, value + 1);
//                }
//            }
//            N -= 1;
//        }
//    }

//    public void towardsGoal(int row, int col, int rez_row, int rez_col) {
//        try {
//            while (Flag) {
//                for (int i = 0; i < solve[0].length && Flag; i++) {
//                    if ((row + solve[0][i]) == rez_row && (col + solve[1][i]) == rez_col) {
//                        field[row + solve[0][i]][col + solve[1][i]].value = field[row][col].value + 1;
//                        Flag = false;
//                    } else if (chechInPlace(row + solve[0][i], col + solve[1][i]) && field[row + solve[0][i]][col + solve[1][i]].value == 0) {
//                        queue.add(new int[]{row + solve[0][i], col + solve[1][i]});
//                        field[row + solve[0][i]][col + solve[1][i]].value = field[row][col].value + 1;
//                    }
//                }
//                if (Flag) {
//                    int[] coordinates = queue.poll();
//                    towardsGoal(coordinates[0], coordinates[1], rez_row, rez_col);
//                }
//            }
//        } catch (Exception e) {
//            SwingUtils.showInfoMessageBox("Решений нет!", "Сообщение");
//            Flag = false;
//        }
//    }

    public void towardsGoal(int row, int col, int rez_row, int rez_col) {
        for (int i = 0; i < solve[0].length && Flag; i++) {
            if ((row + solve[0][i]) == rez_row && (col + solve[1][i]) == rez_col) {
                field[row + solve[0][i]][col + solve[1][i]].value = field[row][col].value + 1;
                Flag = false;
            } else if (chechInPlace(row + solve[0][i], col + solve[1][i]) && field[row + solve[0][i]][col + solve[1][i]].value == 0) {
                queue.add(new int[]{row + solve[0][i], col + solve[1][i]});
                field[row + solve[0][i]][col + solve[1][i]].value = field[row][col].value + 1;
            }
        }
    }

    private void wayToHorse(int row, int col) {
        if (field[row][col].state != CellState.END) {
            field[row][col].state = CellState.RESULT;
        }
        for (int i = 0; i < solve[0].length; i++) {
            if (chechInPlace(row + solve[0][i], col + solve[1][i]) && cheakProgress(row + solve[0][i], col + solve[1][i], row, col)) {
                wayToHorse(row + solve[0][i], col + solve[1][i]);
                break;
            }
        }
    }

    private boolean cheakProgress(int row, int col, int row_rez, int col_rez) {
        return field[row][col].value == field[row_rez][col_rez].value - 1 && field[row][col].value != 0;
    }

    public boolean chechInPlace(int row, int col) {
        return ((row >= 0) && (row < getRowCount())) && ((col >= 0) && (col < getColCount())) && ((field[row][col].state != CellState.CLOSED) && (field[row][col].state != CellState.HORSE));
    }


    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= getRowCount() || col < 0 || col >= getColCount()) return null;
        return field[row][col];
    }
}
