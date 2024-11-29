package ru.vsu.cs.graph;

import java.util.*;

public class ListDigraph implements Graph {
    ArrayList<Integer>[] adjList;
    private int vCount = 0;
    private int eCount = 0;
    private HashMap<Integer, Integer> values = new HashMap<>();

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
        }
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

//    public int[] waveSearch(int v1, int v2) {
//        Map<Integer, Integer> values = new HashMap<>(); // Дистанции до каждой вершины
//        Map<Integer, Integer> parents = new HashMap<>(); // Для восстановления пути
//        Queue<Integer> queue = new LinkedList<>();
//
//        queue.add(v1);
//        values.put(v1, 0); // Начальная вершина на расстоянии 0
//        parents.put(v1, null);
//
//        while (!queue.isEmpty()) {
//            int current = queue.poll();
//            int currentValue = values.get(current);
//
//            for (int neighbor : edges(current)) {
//                if (!values.containsKey(neighbor)) { // Если вершина ещё не обработана
//                    values.put(neighbor, currentValue + 1);
//                    parents.put(neighbor, current);
//                    queue.add(neighbor);
//
//                    if (neighbor == v2) { // Если достигли цели
//                        return restoreRoute(v2, parents);
//                    }
//                }
//            }
//        }
//
//        return new int[0]; // Если путь не найден
//    }
//
//    // Восстановление пути от v2 к v1 с использованием родителей
//    private int[] restoreRoute(int v2, Map<Integer, Integer> parents) {
//        List<Integer> path = new ArrayList<>();
//        for (Integer current = v2; current != null; current = parents.get(current)) {
//            path.add(current);
//        }
//        Collections.reverse(path); // Разворачиваем путь
//        return path.stream().mapToInt(Integer::intValue).toArray();
//    }

    public int[] waveSearch(int v1, int v2) {
        // Инициализация значений
        for (int i = 0; i < vCount; i++) {
            values.put(i, -1);
        }
        Queue<Integer> queue = new LinkedList<>();

        queue.add(v1);
        values.put(v1, 0);

        // Выполняем BFS
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentValue = values.get(current);

            for (int neighbor : edges(current)) {
                if (values.get(neighbor) == -1) {
                    values.put(neighbor, currentValue + 1);
                    queue.add(neighbor);
                    if (neighbor == v2) {
                        return restoreRouteByValues(v1, v2);
                    }
                }
            }
        }

        return new int[0]; // Если путь не найден
    }

    // Восстанавливаем путь по значениям из словаря
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
                throw new RuntimeException("Путь не может быть восстановлен!"); // Если путь не найден
            }
        }

        path.add(v1);
        Collections.reverse(path);
        return path.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] aStarSearch(int v1, int v2) { //TODO Реализовать A*
        return null;
    }

    public HashMap<Integer, Integer> getValues() {
        return values;
    }

    public int getCountVertex() {
        return vCount;
    }
}
