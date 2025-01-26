package ru.vsu.cs.task3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;

public class PriorityQueueMaxHeap<T> {
    private final ArrayList<ListNode<T>> heap;
    Random random = new Random();

    private static class ListNode<T> {
        private T value;
        private int priority;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public ListNode(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    public PriorityQueueMaxHeap() {
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


    /**
     * Метод подъема элемента O(log2(N))
     *
     * @param index индекс элемента
     */
    private void ShiftUp(int index) {
        while (index > 0 && heap.get(parent(index)).getPriority() <= heap.get(index).getPriority()) {
            int parentIndex = parent(index);  // Меняем местами родителя и текущий элемент
            Collections.swap(heap, index, parentIndex);
            index = parentIndex;
        }
    }

    /**
     * Спуск элемента при удалении O(log2(N))
     * Рекурсия... о нет)
     *
     * @param index индекс начала координат
     */
    private void ShiftDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Проверка левого дочернего узла
        if (left < heap.size() && heap.get(left).getPriority() >= heap.get(largest).getPriority()) {
            largest = left;
        }
        // Проверка правого дочернего узла
        if (right < heap.size() && heap.get(right).getPriority() >= heap.get(largest).getPriority()) {
            largest = right;
        }

        // Если наибольший элемент не корень
        if (largest != index) {
            Collections.swap(heap, largest, index); // обмен значениями
            ShiftDown(largest); // повторяем процесс для дочернего узла
        }
    }

    /**
     * Добавление элемента в очередь O(log2(N) +1) -> O(log2(N))
     *
     * @param value    значение
     * @param priority приоритет
     */
    public void insert(T value, int priority) {
        heap.add(new ListNode<T>(value, priority));
        ShiftUp(heap.size() - 1);
    }


    public T extractMax() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        ListNode<T> max = heap.getFirst();
        ListNode<T> last = heap.removeLast();
        if (!heap.isEmpty()) {
            heap.set(0, last);
            ShiftDown(0); // "Погружение" элемента вниз для восстановления Max-Heap
        }
        return max.getValue();
    }

    public void clean(){
        heap.clear();
    }
}
