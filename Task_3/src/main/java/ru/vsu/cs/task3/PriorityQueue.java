package ru.vsu.cs.task3;

import java.util.NoSuchElementException;

public class PriorityQueue<T> {
    private ListNode<T> head = null;
    private ListNode<T> tail = null;
    private int count = 0;

    public static class ListNode<T> {
        private T value;
        private int priority;
        private ListNode<T> next;

        public ListNode(T value, int priority, ListNode<T> next) {
            this.value = value;
            this.priority = priority;
            this.next = next;
        }

        public ListNode(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }

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

        public ListNode<T> getNext() {
            return next;
        }

        public void setNext(ListNode<T> next) {
            this.next = next;
        }
    }

    /**
     * Вставка в очередь с приоритетом:
     * Оценка сложности в худшем случае O(n-1) -> O(n)
     *
     * @param value    значение
     * @param priority приоритет
     */
    public void insert(T value, int priority) {
        ListNode<T> newNode = new ListNode<>(value, priority);
        if (isEmpty()) {
            head = tail = newNode;
        } else if (tail.priority <= priority) {
            tail.next = newNode;
            tail = newNode;
        } else if (head.priority > priority) {
            newNode.setNext(head);
            head = newNode;
        } else {
            ListNode<T> current = head;
            while (current.next != null && current.next.priority <= priority) {
                current = current.next;
            }
            newNode.setNext(current.next);
            current.next = newNode;
        }
        count++;
    }

    /**
     * Ну тут O(1), думаю нет вопросов...
     *
     * @return Кто последний в очереди?
     */
    public T peek() {
        return head.value;
    }

    /**
     * O(1)
     *
     * @return длинна очереди
     */
    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Извлечение последнего (максимальный приоритет) в очереди O(1)
     *
     * @return Элемент очереди
     */
    public T extractMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста!");
        }
        T value = head.value;
        head = head.next;
        count--;
        return value;
    }

    /**
     * Напечатать элементы очереди O(N)
     */
    public void print() {
        System.out.println("Очередь");
        ListNode<T> current = head;
        while (current != null) {
            System.out.printf("(%s, %s) -> ", current.value, current.priority);
            current = current.next;
        }
        System.out.println();
    }

    /**
     * Поиск элемента в очереди O(N)
     *
     * @param value значение элемента очереди
     * @return Объект - значение, приоритет
     */
    public ListNode<T> find(T value) {
        ListNode<T> current = head;
        while (current != null) {
            if (current.value == value) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public ListNode<T> getHead() {
        return head;
    }

    public void setHead(ListNode<T> head) {
        this.head = head;
    }

    public ListNode<T> getTail() {
        return tail;
    }

    public void setTail(ListNode<T> tail) {
        this.tail = tail;
    }

    public int getCount() {
        return count;
    }

    public void clean(){
        head=null;
    }
}
