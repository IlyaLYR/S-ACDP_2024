package ru.vsu.cs.graph;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixGraph implements Graph {

    private boolean[][] adjMatrix = null;
    private int vCount = 0;
    private int eCount = 0;

    public MatrixGraph(int vCount) {
        this.vCount = vCount;
        adjMatrix = new boolean[vCount][vCount];
    }

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addEdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        if (maxV >= vertexCount()) {
            adjMatrix = Arrays.copyOf(adjMatrix, maxV + 1);
            for (int i = 0; i <= maxV; i++) {
                adjMatrix[i] = i < vCount ? Arrays.copyOf(adjMatrix[i], maxV + 1) : new boolean[maxV + 1];
            }
            vCount = maxV + 1;
        }
        if (!adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = true;
            eCount++;
        }
    }

    @Override
    public void removeEdge(int v1, int v2) {
        if (adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = false;
            eCount--;
        }
    }

    @Override
    public ArrayList<Integer> edges(int v) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < vCount; i++) {
            if (adjMatrix[v][i]) {
                result.add(i);
            }
        }
        return result;
    }

    //Можно и так...
    @Override
    public boolean isAdj(int v1, int v2) {
        return adjMatrix[v1][v2];
    }
}
