package ru.vsu.cs.task5;

import ru.vsu.cs.graph.ListDigraph;
import ru.vsu.cs.util.SwingUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс, реализующий логику проекта
 */
public class Game {
    public enum CellState {
        OPENED, CLOSED, START, RESULT, END, TELEPORT, CRAWL
    }

    public static class Cell {
        public CellState state;
        public int value;
        public int teleportNumber;

        public Cell(CellState state, int value) {
            this.state = state;
            this.value = value;
        }
    }

    /**
     * Двумерный массив для хранения игрового поля
     */
    private Cell[][] field = null;
    private ListDigraph graph = null;
    private ArrayList<int[]>[] teleports = new ArrayList[10];
    private int result = 0;


    int[][] solve = {{-1, 0}, // Вверх
            {1, 0},  // Вниз
            {0, -1}, // Влево
            {0, 1}   // Вправо
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
        initializeTeleports();

        graph = new ListDigraph(rowCount * colCount);
    }

    private void initializeTeleports() {
        for (int i = 0; i < 10; i++) {
            teleports[i] = new ArrayList<>();
        }
    }

    public void leftMouseClick(int row, int col) {
        if (outOfBound(row, col) || field[row][col].state == CellState.TELEPORT) {
            return;
        }
        field[row][col].state = CellState.CLOSED;
    }

    public void middleMouseClick(int row, int col) {
        if (outOfBound(row, col) || field[row][col].state == CellState.TELEPORT) {
            return;
        }
        field[row][col].state = CellState.END;
    }

    //Правая кнопка возвращает статус OPENED -> удаление
    public void rightMouseClick(int row, int col) {
        if (outOfBound(row, col)) {
            return;
        }
        switch (field[row][col].state) {
            case OPENED -> field[row][col].state = CellState.START;
            case TELEPORT -> {
                int id = field[row][col].teleportNumber;
                for (int[] cord : teleports[id]) {
                    field[cord[0]][cord[1]].state = CellState.OPENED;
                }
                teleports[id].clear();
            }
            default -> field[row][col].state = CellState.OPENED;
        }
    }

    //Установка телепортов
    public void leftMouseClickTeleport(int row, int col) {
        if (outOfBound(row, col) || field[row][col].state == CellState.TELEPORT) {
            return;
        }
        if (teleports[teleports.length - 1].size() == 2) {
            //Расширение массива телепортов
            teleports = Arrays.copyOf(teleports, teleports.length + 5);
            for (int j = teleports.length - 5; j < teleports.length; j++) {
                teleports[j] = new ArrayList<int[]>();
            }
        }
        addTeleport(row, col);

    }

    public void StartClick(int type, boolean typeTeleport) {
        clean();
        createGraph(typeTeleport);
        int[] startEnd = coordinates();
        if (startEnd == null) {
            SwingUtils.showInfoMessageBox("Начальная или конечная точка не указана! Или точки указанны некорректно...");
            return;
        }
        try {
            int v1 = cordToVertex(startEnd[0], startEnd[1]);
            int v2 = cordToVertex(startEnd[2], startEnd[3]);
            int[] path = switch (type) {
                case 0 -> graph.waveSearch(v1, v2);
                case 1 -> graph.aStarSearch(v1, v2, getRowCount(), getColCount());
                case 2 -> graph.dijkstra(v1,v2);
                default -> new int[]{};
            };
            if (path.length == 0) {
                SwingUtils.showInfoMessageBox("Решений нет");
            }
            markResultStatus(path);
            markCrawl();
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
                if (cellValue.state == CellState.CRAWL) {
                    field[row][col].state = CellState.OPENED;
                }
            }
        }
    }

    public void cleanField() { //метод очистки поля
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                field[row][col].value = 0;
                if (cellValue.state != CellState.OPENED) {
                    field[row][col].state = CellState.OPENED;
                }
            }
        }
        initializeTeleports();
    }

    public boolean checkInPlace(int row, int col) {
        return ((row >= 0) && (row < getRowCount())) && ((col >= 0) && (col < getColCount())) && field[row][col].state != CellState.CLOSED;
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

    private void createGraph(boolean isThrough) {
        graph = new ListDigraph(getRowCount() * getColCount()); // Очистка графа перед построением или его инициализация при первом запуске
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                if (isThrough || field[row][col].state != CellState.TELEPORT) {
                    if (field[row][col].state == CellState.CLOSED) {
                        continue;
                    } else addToGraph(row, col);
                }
            }
        }
        try {
            graph.setTeleports(teleports);
            for (ArrayList<int[]> list : teleports) {
                if (!list.isEmpty()) {
                    int[] cord1 = list.get(0);
                    int[] cord2 = list.get(1);
                    int v1 = cordToVertex(cord1[0], cord1[1]);
                    int v2 = cordToVertex(cord2[0], cord2[1]);
                    graph.addEdge(v1, v2);
                    graph.addEdge(v2, v1);
                }
            }
        } catch (Exception e) {
            SwingUtils.showInfoMessageBox("Некорректно указаны телепорты)");
        }
    }

    private void addToGraph(int row, int col) {
        int vertex = cordToVertex(row, col);

        // Добавление рёбер для соседних ячеек
        for (int[] into : solve) {
            int rowV = row + into[0];
            int colV = col + into[1];
            if (checkInPlace(rowV, colV)) {
                graph.addEdge(vertex, cordToVertex(rowV, colV));
            }
        }
    }


    private int cordToVertex(int row, int col) {
        return getColCount() * row + col;
    }

    private void markResultStatus(int[] way) {
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

    private void markCrawl() {
        for (int i = 0; i < graph.getCountVertex(); i++) {
            int row = i / getColCount();
            int col = i % getColCount();
            if (field[row][col].state == CellState.OPENED && graph.getValues().get(i) != -1) {
                field[row][col].state = CellState.CRAWL;
                field[row][col].value = graph.getValues().get(i);
            }
        }

    }


    private boolean outOfBound(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        return row < 0 || row >= rowCount || col < 0 || col >= colCount;
    }

    private void addTeleport(int row, int col) {
        int i = 0;
        while (teleports[i].size() == 2) {
            i++;
        }
        teleports[i].add(new int[]{row, col});
        field[row][col].state = CellState.TELEPORT;
        field[row][col].teleportNumber = i;
    }
}
