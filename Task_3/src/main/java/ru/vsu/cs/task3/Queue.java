package ru.vsu.cs.task3;

import java.util.NoSuchElementException;

public class Queue<T> {
    private ListNode<T> head = null;
    private ListNode<T> tail = null;
    private int count = 0;


    private class ListNode<T> {
        private T value;
        private ListNode<T> next;


        public ListNode(T value, ListNode<T> next) {
            this.value = value;
            this.next = next;
        }

        public ListNode(T value) {
            this(value, null);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public ListNode<T> getNext() {
            return next;
        }

        public void setNext(ListNode<T> next) {
            this.next = next;
        }
    }

    public void insert(T value) { //добавить в очередь
        ListNode<T> last = new ListNode<>(value);
        if (count == 0) {
            head = tail = last;
        } else {
            tail.next = last;
            tail = last;
        }
        count++;
    }

    public T extract() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста!!!");
        }
        T value = head.value;
        head = head.next;
        count--;
        if (isEmpty()) {
            tail = null;
        }
        return value;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста!!!");
        }
        return head.value;
    }

    public int length() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        count = 0;
    }
}



