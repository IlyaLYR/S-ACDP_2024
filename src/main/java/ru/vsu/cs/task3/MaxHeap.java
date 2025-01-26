package ru.vsu.cs.task3;

import java.util.ArrayList;

public class MaxHeap {
    private ArrayList<Integer> heap;

    public MaxHeap() {
        heap = new ArrayList<>();
    }


    private int parent(int index) { // Получаем индекс родителя
        return (index - 1) / 2;
    }

    private int leftChild(int index) {  // Получаем индекс левого ребенка
        return 2 * index + 1;
    }

    private int rightChild(int index) {   // Получаем индекс правого ребенка
        return 2 * index + 2;
    }

    // Всплытие элемента вверх, если он больше родителя
    private void ShiftUp(int index) {
        while (index > 0 && heap.get(parent(index)) < heap.get(index)) {
            int parentIndex = parent(index);  // Меняем местами родителя и текущий элемент
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    // Погружение элемента вниз для восстановления кучи после удаления корня
    private void ShiftDown(int index) {
        int size = heap.size();
        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);
            int largest = index;

            // Проверяем, больше ли левый ребенок
            if (left < size && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            // Проверяем, больше ли правый ребенок
            if (right < size && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            // Если самый большой элемент — текущий, завершаем цикл
            if (largest == index) {
                break;
            }

            // Иначе меняем элементы местами и продолжаем
            swap(index, largest);
            index = largest;
        }
    }

    // Вставка элемента с использованием всплытия
    public void insert(int element) {
        heap.add(element); // Добавляем элемент в конец
        ShiftUp(heap.size() - 1); // "Всплытие" элемента вверх для восстановления структуры Max-Heap
    }

    // Извлечение максимального элемента (корня)
    public int extractMax() {
        if (heap.size() == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int maxElement = heap.get(0); // Самый большой элемент (корень)
        int lastElement = heap.remove(heap.size() - 1); // Удаляем последний элемент
        if (heap.size() > 0) {
            heap.set(0, lastElement); // Ставим последний элемент на место корня
            ShiftDown(0); // "Погружение" элемента вниз для восстановления Max-Heap
        }
        return maxElement;
    }

    // Обмен элементов местами
    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Вывод кучи (опционально для демонстрации)
    public void printHeap() {
        System.out.println(heap);
    }

    public static void main(String[] args) {
    }
}