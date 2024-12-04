package ru.vsu.cs.graph;

import java.util.*;

public class ListDigraph implements Graph {
    ArrayList<Integer>[] adjList;
    private final int vCount;
    private int eCount = 0;
    private final HashMap<Integer, Integer> values = new HashMap<>();
    ArrayList<int[]>[] teleports;

    public ListDigraph(int vCount) {
        this.vCount = vCount;
        adjList = new ArrayList[vCount];
        for (int i = 0; i < vCount; i++) {
            adjList[i] = new ArrayList<>();
            values.put(i, -1);
        }
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public void addEdge(int v1, int v2) {
        // Проверяем, существует ли уже ребро между v1 и v2
        if (!adjList[v1].contains(v2)) {
            adjList[v1].add(v2);
            eCount++;
        }
    }

    public void setTeleports(ArrayList<int[]>[] teleports) {
        this.teleports = teleports.clone();
    }

    @Override
    public void removeEdge(int v1, int v2) {
        adjList[v1].remove(v2);
        adjList[v2].remove(v1);
    }

    @Override
    public ArrayList<Integer> edges(int v) {
        return adjList[v];
    }

    /**
     * Волновой поиск BFS
     *
     * @param start начальная точка
     * @param goal  конечная точка
     * @return массив -> маршрут
     */
    public int[] waveSearch(int start, int goal) {
        values.put(start, 0); //values -> массив маршрутный лист

        Queue<Integer> queue = new LinkedList<>(); //очередь на основе связанного списка
        queue.add(start);

        // Выполняем BFS
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDistance = values.get(current);

            for (int neighbor : edges(current)) {
                if (values.get(neighbor) == -1) {
                    values.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);

                    if (neighbor == goal) {
                        return restoreRouteByValues(start, goal);
                    }
                }
            }
        }

        return new int[0]; // Путь не найден
    }

    // Восстанавливаем путь по значениям из словаря

    /**
     * Восстановление маршрута по маршрутному листу
     *
     * @param v1 start
     * @param v2 end
     * @return массив - путь
     */
    private int[] restoreRouteByValues(int v1, int v2) {
        List<Integer> path = new ArrayList<>();
        int current = v2;

        while (current != v1) {
            path.add(current);
            int currentValue = values.get(current);

            // Ищем вершину-соседа с меньшим значением value
            boolean flag = false;
            for (int neighbor : edges(current)) {
                if (values.get(neighbor) == currentValue - 1) {
                    current = neighbor;
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                throw new RuntimeException("Путь не может быть восстановлен!"); //Ошибка, ошибка, еще раз ошибка
            }
        }

        path.add(v1);

        // Преобразуем список в массив
        int[] result = new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(path.size() - 1 - i); // Заполняем массив в обратном порядке
        }

        return result;
    }

    public HashMap<Integer, Integer> getValues() {
        return values;
    }

    public int getCountVertex() {
        return vCount;
    }


    /**
     * Алгоритм Дейкстры
     *
     * @param start начальная вершина
     * @param goal  конечная вершина
     * @return массив маршрут
     */
    public int[] dijkstra(int start, int goal) {

        Set<Integer> visited = new HashSet<>();

        //Дистанция
        int[] distances = new int[vCount];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;

        int[] prev = new int[vCount];
        Arrays.fill(prev, -1);

        // Собственная минимальная очередь!!! На куче!!
        PriorityQueueMinHeap<Integer> queue = new PriorityQueueMinHeap<>();
        queue.insert(start, 0);

        while (!queue.isEmpty()) {
            int currentVertex = queue.extract();//// Извлекаем вершину с минимальным расстоянием
            if (visited.contains(currentVertex)) {
                continue;
            }

            visited.add(currentVertex);
            if (currentVertex == goal) {
                return restorePath(prev, goal);
            }

            // Обрабатываем всех соседей текущей вершины
            for (int neighbor : edges(currentVertex)) {
                int newDistance = distances[currentVertex] + 1;

                if (newDistance < distances[neighbor]) {
                    distances[neighbor] = newDistance;
                    values.put(neighbor, newDistance);
                    prev[neighbor] = currentVertex;
                    queue.insert(neighbor, newDistance);
                }
            }
        }
        return restorePath(prev, goal);
    }

    // Метод для восстановления пути
    private int[] restorePath(int[] predecessors, int goal) {
        int pathLength = 0;
        for (int current = goal; current != -1; current = predecessors[current]) {
            pathLength++;
        }

        int[] path = new int[pathLength];
        int index = pathLength - 1;
        for (int current = goal; current != -1; current = predecessors[current]) {
            path[index--] = current;
        }

        return path;
    }


    /**
     * Алгоритм A*
     *
     * @param start начальная точка
     * @param goal  конечная точка
     * @param rows  количество строк
     * @param cols  количество столбцов
     * @return массив -> маршрут
     */
    public int[] aStarSearch(int start, int goal, int rows, int cols) {
        Set<Integer> visited = new HashSet<>(); // посещенные...
        int vCount = rows * cols;

        int[] dist = new int[vCount]; //расстояние до вершин
        double[] fScore = new double[vCount]; //эвристика
        int[] prev = new int[vCount]; // ход до...

        //подготовка массивов
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(fScore, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);

        dist[start] = 0;
        fScore[start] = euclideanHeuristic(goal % cols, goal / cols, start % cols, start / cols); //Здесь пожалуй используем обычную...

        PriorityQueueMinHeap<Integer> queue = new PriorityQueueMinHeap<>();
        queue.insert(start, fScore[start]);

        while (!queue.isEmpty()) { // обход
            int current = queue.extract();

            if (!visited.add(current)) continue;
            if (current == goal) return restorePath(prev, goal);

            //Обход соседей
            for (int neighbor : edges(current)) {
                if (visited.contains(neighbor)) continue;

                int distance = dist[current] + 1;
                double heuristic = euclideanHeuristicWithTeleports(neighbor, goal, cols);
                double totalScore = distance + heuristic;

                if (totalScore < fScore[neighbor]) {
                    prev[neighbor] = current;
                    dist[neighbor] = distance;
                    values.put(neighbor, distance);
                    fScore[neighbor] = totalScore;

                    queue.insert(neighbor, totalScore);
                }
            }
        }

        return new int[0];
    }

    /**
     * Евклидово расстояние
     *
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     * @return без комментариев...
     */
    private double euclideanHeuristic(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Евклидово расстояние с учетом телепортов
     *
     * @param current текущая вершина
     * @param goal    цель маршрута
     * @param cols    количество строк
     * @return эвристика...
     */
    private double euclideanHeuristicWithTeleports(int current, int goal, int cols) {
        int rowCurrent = current / cols;
        int colCurrent = current % cols;

        int rowGoal = goal / cols;
        int colGoal = goal % cols;

        // Основная эвристика (евклидово расстояние)
        double baseHeuristic = Math.sqrt(
                Math.pow(colGoal - colCurrent, 2) + Math.pow(rowGoal - rowCurrent, 2)
        );

        // Проверяем наличие телепортов
        for (ArrayList<int[]> teleportPair : teleports) {
            if (teleportPair.size() == 2) {
                int[] tp1 = teleportPair.get(0);
                int[] tp2 = teleportPair.get(1);

                // Вычисляем расстояния через телепорты
                double teleportHeuristic1 = Math.sqrt(Math.pow(colGoal - tp1[1], 2) + Math.pow(rowGoal - tp1[0], 2)) +
                        Math.sqrt(Math.pow(colCurrent - tp2[1], 2) + Math.pow(rowCurrent - tp2[0], 2));
                double teleportHeuristic2 = Math.sqrt(Math.pow(colGoal - tp2[1], 2) + Math.pow(rowGoal - tp2[0], 2)) +
                        Math.sqrt(Math.pow(colCurrent - tp1[1], 2) + Math.pow(rowCurrent - tp1[0], 2));

                baseHeuristic = Math.min(baseHeuristic, Math.min(teleportHeuristic1, teleportHeuristic2));
            }
        }

        return baseHeuristic;
    }

}
