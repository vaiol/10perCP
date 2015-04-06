package com.checkpoint.vaiol.myDataStructure;

import java.util.*;

public class MyLinkedList<E> implements List<E> {

    private long lifetime;
    private LifeTimer lifeTimer;
    private int currentSize;

    private Node mainNode;


    public MyLinkedList() {
        mainNode = new Node<E>(null);
        mainNode.next = mainNode;
        mainNode.prev = mainNode;
        currentSize = 0;
    }

    public void startLifeTimer(long lifetime) {
        this.lifetime = lifetime;
        lifeTimer = new LifeTimer();
        LinkedList
    }

    public void stopLifeTimer() {
        lifeTimer.stopTimer();
        lifeTimer = null;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        Node newNode = new Node<E>(e);
        mainNode.prev.next = newNode;
        mainNode.prev = newNode;
        currentSize++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node current = mainNode.next;
        boolean b = false;
        while (current != mainNode) {
            if(current.value.equals(o)) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                b = true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if(! contains((E) object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends E> c) {
        for (Object object : c) {
            add((E) object);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends E> c) {
        for(Object object : c) {
            add(index++, (E) object);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = false;
        for(Object object : c){
            if(remove(object)) {
                b = true;
            }
        }
        return b;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    private class Node<E> {
        public Node(E value) {
            this.value = value;
            creationTime = System.currentTimeMillis();
        }
        private Node next;
        private Node prev;
        private E value;
        private long creationTime;
    }

    private class LifeTimer extends Thread {

        boolean checkLife = true;

        public LifeTimer() {
            this.start();
        }

        public void stopTimer() {
            checkLife = false;
        }

        @Override
        public void run() {
            while(checkLife) {
                synchronized(MyLinkedList.this) {
//                    for(int i = 0; i < currentSize; i++) {
//                        if(System.currentTimeMillis()- array[i].createTime > lifetime) {
//                            remove(i);
//                            i--;
//                        }
//                    }
                }
                try {
                    Thread.sleep(1000); //every 1 second
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }
}
