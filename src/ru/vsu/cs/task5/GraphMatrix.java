package ru.vsu.cs.task5;

import java.util.HashMap;
import java.util.Map;

public class GraphMatrix {
    private final int[][] matrix;
    private final Map<Integer,Integer> vertices;
    private int size;

    public GraphMatrix(int size) {
        this.matrix = new int[size][size];
        this.vertices = new HashMap<>();
        this.size = size;
    }


}
