package ru.vsu.cs.task5;

import ru.vsu.cs.graph.ListGraph;
import ru.vsu.cs.util.SwingUtils;

import java.util.ArrayList;

/**
 * Класс, реализующий логику проекта
 */
public class Game {
    public enum CellState {
        OPENED, CLOSED, START, RESULT, END, TELEPORT
    }

    public static class Cell {
        public CellState state;
        public int value;

        public Cell(CellState state, int value) {
            this.state = state;
            this.value = value;
        }
    }

    /**
     * Двумерный массив для хранения игрового поля
     */
    private Cell[][] field = null;
    private ListGraph graph = null;
    private final ArrayList<int[]>[] teleports = new ArrayList[10];
    private int result = 0;


    int[][] solve = {
            {-1, 1, 0, 0}, //row
            {0, 0, -1, 1} // col
    };

    public int getResult() {
        return result;
    }

    public Game() {
    }

    public void newGame(int rowCount, int colCount) {
        // создаем поле
        field = new Cell[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                field[row][col] = new Cell(CellState.OPENED, 0);
            }
        }
        for (int i = 0; i < 10; i++) {
            teleports[i] = new ArrayList<>();
        }
        graph = new ListGraph(rowCount * colCount);
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
            field[row][col].state = CellState.START;
        } else field[row][col].state = CellState.OPENED;
    }

    public void StartClick(int type) {
        clean();
        createGraph();

        int[] startEnd = coordinates();
        if (startEnd == null) {
            SwingUtils.showInfoMessageBox("Начальная или конечная точка не указана!");
            return;
        }

        try {
            int v1 = cordToVertex(startEnd[0], startEnd[1]);
            int v2 = cordToVertex(startEnd[2], startEnd[3]);
            int[] path = switch (type) {
                case 1 -> graph.waveSearch(v1, v2);
                //TODO добавить A*
                default -> new int[]{};
            };

            if (path.length == 0) {
                SwingUtils.showInfoMessageBox("Решений нет");
            }
            resultStatus(path);
            result = field[startEnd[2]][startEnd[3]].value;
        } catch (Exception e) {
            SwingUtils.showInfoMessageBox("Ошибка поиска пути: " + e.getMessage());
        }
    }


    private int[] coordinates() { // координаты начало и конца
        int[] array = new int[4];
        int start = 0;

        int end = 0;
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                if (cellValue.state == CellState.START) {
                    start++;
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
        if (start != 1 || end != 1) {
            return null;
        }
        return array;
    }

    //Очистка поля от прошлого результата
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


    private boolean checkProgress(int row, int col, int row_rez, int col_rez) {
        return field[row][col].value == field[row_rez][col_rez].value - 1 && field[row][col].value != 0;
    }

    public boolean checkInPlace(int row, int col) {
        return ((row >= 0) && (row < getRowCount())) && ((col >= 0) && (col < getColCount())) && ((field[row][col].state != CellState.CLOSED));
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

    private void createGraph() {
        graph = new ListGraph(getRowCount() * getColCount()); // Очистка графа перед построением или его инициализация при первом запуске

        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                if (checkInPlace(row, col)) {
                    int vertex = cordToVertex(row, col);

                    // Добавление рёбер для соседних ячеек
                    for (int j = 0; j < solve[0].length; j++) {
                        int rowV = row + solve[0][j];
                        int colV = col + solve[1][j];
                        if (checkInPlace(rowV, colV)) {
                            graph.addEdge(vertex, cordToVertex(rowV, colV));
                        }
                    }
                }

                // Работа с телепортами
                if (field[row][col].state == CellState.TELEPORT) {
                    teleports[field[row][col].value].add(new int[]{row, col});
                }
            }
        }

        // Проверка телепортов
        for (ArrayList<int[]> list : teleports) {
            if (list.size() != 2 && !list.isEmpty()) {
                throw new IllegalArgumentException("Некорректно заданы телепорты");
            } else if (list.size() == 2) {
                // Связывание телепортов
                int[] t1 = list.get(0);
                int[] t2 = list.get(1);
                int v1 = cordToVertex(t1[0], t1[1]);
                int v2 = cordToVertex(t2[0], t2[1]);
                graph.addEdge(v1, v2);
                graph.addEdge(v2, v1);
            }
        }
    }


    private int cordToVertex(int row, int col) {
        return getColCount() * row + col;
    }

    private void resultStatus(int[] way) {
        try {
            for (int i = 0; i < way.length; i++) {
                int row = way[i] / getColCount();
                int col = way[i] % getColCount();
                if (field[row][col].state == CellState.OPENED) {
                    field[row][col].state = CellState.RESULT;
                }
                field[row][col].value = i;
            }
        } catch (Exception e) {
            SwingUtils.showInfoMessageBox("Ошибка обработки результата: " + e.getMessage());
        }
    }
}
