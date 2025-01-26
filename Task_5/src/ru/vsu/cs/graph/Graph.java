package ru.vsu.cs.graph;

import java.awt.*;
import java.util.ArrayList;

/**
 * Интерфейс для описания неориентированного графа (н-графа)
 * с реализацией некоторых методов графа
 */
public interface Graph {
    /**
     * Кол-во вершин в графе
     *
     * @return число вершин
     */
    int vertexCount();

    /**
     * Кол-во ребер в графе
     *
     * @return число ребер
     */
    int edgeCount();

    /**
     * Добавление ребра между вершинами с номерами v1 и v2
     *
     * @param v1
     * @param v2
     */
    void addEdge(int v1, int v2);

    /**
     * Удаление ребра/ребер между вершинами с номерами v1 и v2
     *
     * @param v1 вершина 1
     * @param v2 вершина 2
     */
    void removeEdge(int v1, int v2);

    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    ArrayList<Integer> edges(int v);




    default boolean isAdj(int v1, int v2) {
        for (Integer adj : edges(v1)) {
            if (adj == v2) {
                return true;
            }
        }
        return false;
    }
}
