package ru.vsu.cs.task5;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyQueue<T> implements Iterable<T> {


    private MyListNode<T> head = null;
    private MyListNode<T> tail = null;
    private int count = 0;


    private class MyListNode<T> {
        private T value;
        private MyListNode<T> next;


        public MyListNode(T value, MyListNode<T> next) {
            this.value = value;
            this.next = next;
        }

        public MyListNode(T value) {
            this(value, null);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public MyListNode<T> getNext() {
            return next;
        }

        public void setNext(MyListNode<T> next) {
            this.next = next;
        }
    }

    public void add(T value) { //добавить в очередь
        MyListNode<T> last = new MyListNode<>(value);
        if (count == 0) {
            head = tail = last;
        } else {
            tail.next = last;
            tail = last;
        }
        count++;
    }
    public T poll(){
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пута!!!");
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
            throw new NoSuchElementException("Очередь пута!!!");
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
    public Iterator<T> iterator() {
        class MyListIterator implements Iterator<T> {
            MyListNode<T> curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T value = curr.value;
                curr = curr.next;
                return value;
            }
            public void reset(){
                curr=head;
            }
        }

        return new MyListIterator();
    }

}



