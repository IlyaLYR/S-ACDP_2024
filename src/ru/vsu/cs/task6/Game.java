package ru.vsu.cs.task6;

import ru.vsu.cs.graph.ListDigraph;
import ru.vsu.cs.util.SwingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static ru.vsu.cs.util.ArrayUtils.readLinesFromFile;

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

        public Cell(CellState state, int value, int teleportNumber) {
            this.state = state;
            this.value = value;
            this.teleportNumber = teleportNumber;
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

//    public void middleMouseClick(int row, int col) {
//        if (outOfBound(row, col) || field[row][col].state == CellState.TELEPORT) {
//            return;
//        }
//        field[row][col].state = CellState.END;
//    }

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

    //TODO -> переделать метод
    public void StartClick(boolean typeTeleport) {
        clean();
        createGraph(typeTeleport);
        int[] startEnd = minotaurs();
        if (startEnd == null) {
            SwingUtils.showInfoMessageBox("Ищем минотавра...");
            return;
        }
        try {
            int v1 = cordToVertex(startEnd[0], startEnd[1]);


            result = graph.FF();  ; //ОТВЕТ!!!
            //TODO Разработать алгоритм также учесть вывод клеток! markResult();!!!

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

    private int[] minotaurs() {
        int[] array = new int[2];
        int start = 0;

        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                Cell cellValue = getCell(row, col);
                if (cellValue.state == CellState.START) {
                    start++;
                    array[0] = row;
                    array[1] = col;
                }
            }
        }
        if (start != 1) {
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


    public void readFromTXT(String fileName) {
        try {
            String[] vv = readLinesFromFile(fileName);
            toField(readLinesFromFile(fileName));
        } catch (IOException e) {
            SwingUtils.showInfoMessageBox("ОШИБКА ЧТЕНИЯ ФАЙЛА");
        }
    }

    public void toField(String[] lines) {
        // Инициализация поля по размерам входного массива строк
        int rows = lines.length;
        int cols = lines[0].length();
        Cell[][] copy = new Cell[rows][cols];

        // Парсинг строк
        for (int i = 0; i < rows; i++) {
            String line = lines[i];
            for (int j = 0; j < cols; j++) {
                char ch = line.charAt(j);
                Cell cell;

                switch (ch) {
                    case '#': // Стена
                        cell = new Cell(CellState.CLOSED, 0);
                        break;
                    case '.': // Открытое поле
                        cell = new Cell(CellState.OPENED, 0);
                        break;
                    case '*': // Цель
                        cell = new Cell(CellState.START, 0);
                        break;
                    //Телепорты
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        int teleportNumber = Character.getNumericValue(ch);
                        cell = new Cell(CellState.TELEPORT, 0, teleportNumber);
                        teleports[teleportNumber].add(new int[]{i, j});
                        break;
                    default: // Неподдерживаемый символ
                        throw new IllegalArgumentException("Неверный символ в строке: " + ch);
                }

                // Сохраняем ячейку в массив поля
                copy[i][j] = cell;
            }
        }
        field = copy;
    }
}
