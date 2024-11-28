package ru.vsu.cs.graph;

import java.util.*;

public class ListGraph implements Graph {
    ArrayList<Integer>[] adjList;
    private int vCount = 0;
    private int eCount = 0;
    private HashMap<Integer, Integer> values = new HashMap<>();

    public ListGraph(int vCount) {
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
            adjList[v2].add(v1);
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

    public int[] waveSearch(int v1, int v2) {
        Map<Integer, Integer> values = new HashMap<>(); // Дистанции до каждой вершины
        Map<Integer, Integer> parents = new HashMap<>(); // Для восстановления пути
        Queue<Integer> queue = new LinkedList<>();

        queue.add(v1);
        values.put(v1, 0); // Начальная вершина на расстоянии 0
        parents.put(v1, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentValue = values.get(current);

            for (int neighbor : edges(current)) {
                if (!values.containsKey(neighbor)) { // Если вершина ещё не обработана
                    values.put(neighbor, currentValue + 1);
                    parents.put(neighbor, current);
                    queue.add(neighbor);

                    if (neighbor == v2) { // Если достигли цели
                        return restoreRoute(v2, parents);
                    }
                }
            }
        }

        return new int[0]; // Если путь не найден
    }

    // Восстановление пути от v2 к v1 с использованием родителей
    private int[] restoreRoute(int v2, Map<Integer, Integer> parents) {
        List<Integer> path = new ArrayList<>();
        for (Integer current = v2; current != null; current = parents.get(current)) {
            path.add(current);
        }
        Collections.reverse(path); // Разворачиваем путь
        return path.stream().mapToInt(Integer::intValue).toArray();
    }


//    public int[] waveSearch(int v1, int v2) {    GPT
//        boolean found = false;
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(v1);
//        values.put(v1, 0); // Начальная вершина, расстояние 0
//
//        while (!queue.isEmpty() && !found) {
//            int current = queue.poll(); // Берём текущую вершину
//            int currentValue = values.get(current); // Текущая "глубина"
//            for (int neighbor : edges(current)) { // Проходим по соседям
//                if (values.get(neighbor) == -1) { // Если вершина ещё не посещена
//                    values.put(neighbor, currentValue + 1); // Устанавливаем расстояние
//                    queue.add(neighbor); // Добавляем в очередь
//                    if (neighbor == v2) { // Если нашли целевую вершину
//                        found = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (values.get(v2) == -1) {
//            return new int[0]; // Если путь не найден, возвращаем пустой массив
//        }
//
//        // Восстанавливаем путь с помощью restoreRoute
//        return restoreRoute(v2, v1).stream().mapToInt(Integer::intValue).toArray();
//    }


//    private ArrayList<Integer> restoreRoute(int v2, int v1) {
//        int value = values.get(v2);
//        ArrayList<Integer> res = new ArrayList<>();
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(v2);
//        res.add(v2); // Добавляем конечную вершину сразу
//
//        while (!queue.isEmpty() && !queue.peek().equals(v1)) { // Проверка, что очередь не пуста
//            int current = queue.poll();
//            for (int i : edges(current)) {
//                if (values.get(i) == value - 1) { // Ищем вершину с предыдущим значением
//                    res.add(i); // Добавляем вершину в результат
//                    queue.add(i); // Добавляем её для следующей итерации
//                    value--;
//                    break;
//                }
//            }
//        }
//        Collections.reverse(res); // Путь восстанавливается от v2 к v1, разворачиваем
//        return res;
//    }

    private ArrayList<Integer> restoreRoute(int v2, int v1) {
        ArrayList<Integer> route = new ArrayList<>();
        int current = v2;

        while (current != v1) { // Пока не достигнем начальной вершины
            route.add(current); // Добавляем текущую вершину в маршрут
            int currentValue = values.get(current); // Текущее значение уровня
            boolean found = false;

            for (int neighbor : edges(current)) {
                if (values.get(neighbor) == currentValue - 1) { // Находим соседнюю вершину на уровне ниже
                    current = neighbor; // Переходим к этой вершине
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Путь не может быть восстановлен!"); // В случае ошибки
            }
        }

        route.add(v1); // Добавляем начальную вершину
        Collections.reverse(route); // Путь был восстановлен с конца, переворачиваем
        return route;
    }

}
