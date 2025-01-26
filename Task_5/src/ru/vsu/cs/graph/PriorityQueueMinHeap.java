package ru.vsu.cs.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class PriorityQueueMinHeap<T> {
    private final ArrayList<ListNode<T>> heap;

    private static class ListNode<T> {
        private T value;
        private double priority;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public double getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public ListNode(T value, double priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    public PriorityQueueMinHeap() {
        heap = new ArrayList<>();
    }

    //Вспомогательные методы!!
    private int parent(int index) { // Получаем индекс родителя
        return (index - 1) / 2;
    }

    private int leftChild(int index) {  // Получаем индекс левого ребенка
        return 2 * index + 1;
    }

    private int rightChild(int index) {   // Получаем индекс правого ребенка
        return 2 * index + 2;
    }


    private void ShiftUp(int index) {
        while (index > 0 && heap.get(parent(index)).getPriority() >= heap.get(index).getPriority()) {
            int parentIndex = parent(index);  // Меняем местами родителя и текущий элемент
            Collections.swap(heap, index, parentIndex);
            index = parentIndex;
        }
    }

    private void ShiftDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Проверка левого дочернего узла
        if (left < heap.size() && heap.get(left).getPriority() <= heap.get(largest).getPriority()) {
            largest = left;
        }
        // Проверка правого дочернего узла
        if (right < heap.size() && heap.get(right).getPriority() <= heap.get(largest).getPriority()) {
            largest = right;
        }

        // Если наибольший элемент не корень
        if (largest != index) {
            Collections.swap(heap, largest, index); // обмен значениями
            ShiftDown(largest); // повторяем процесс для дочернего узла
        }
    }

    public void insert(T value, double priority) {
        heap.add(new ListNode<T>(value, priority));
        ShiftUp(heap.size() - 1);
    }

    public T extract() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        ListNode<T> max = heap.getFirst();
        ListNode<T> last = heap.removeLast();
        if (!heap.isEmpty()) {
            heap.set(0, last); // Ставим последний элемент на место корня
            ShiftDown(0); // "Погружение" элемента вниз для восстановления Max-Heap
        }
        return max.getValue();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
