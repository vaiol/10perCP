package com.checkpoint.vaiol.myDataStructure;

import java.util.*;

public class MyLinkedList<E> implements List<E> {

    private long lifetime;
    private LifeTimer lifeTimer;
    private int currentSize;

    private Node<E> head;


    public MyLinkedList() {
        clear();
    }

    public void startLifeTimer(long lifetime) {
        this.lifetime = lifetime;
        lifeTimer = new LifeTimer();
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
    public boolean add(E e) {
        Node<E> newNode = new Node<E>(e);

        newNode.next = head;
        newNode.prev = head.prev;
        head.prev.next = newNode;
        head.prev = newNode;
        currentSize++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head.next;
        boolean b = false;
        while (current != head) {
            if(current.value.equals(o)) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                b = true;
                currentSize--;
            }
            current = current.next;
        }
        return b;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if(! contains(object)) {
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
        Node<E> current = head.next;
        boolean b = false;
        while (current != head) {
            if( ! c.contains(current.value)) {
                remove(current.value);
                b = true;
            }
            current = current.next;
        }
        return b;
    }

    @Override
    public void clear() {
        head = new Node<E>(null);
        head.next = head;
        head.prev = head;
        currentSize = 0;
    }

    @Override
    public E get(int index) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head.next;
        int i = 0;
        while (current != head || i >= currentSize) {
            if(i == index) {
                return current.value;
            }
            i++;
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head.next;
        while (current != head) {
            if(current.value.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head.next;
        int i = 0;
        while (current != head || i >= currentSize) {
            if(i == index) {
                E result = current.value;
                current.value = element;
                return result;
            }
            i++;
            current = current.next;
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head.next;
        int i = 0;
        while (current != head || i >= currentSize) {
            if(i == index) {
                Node<E> newNode = new Node<E>(element);
                newNode.next = current;
                newNode.prev = current.prev;
                current.prev.next = newNode;
                current.prev = newNode;
                currentSize++;
                return;
            }
            i++;
            current = current.next;
        }
    }

    @Override
    public E remove(int index) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head.next;
        int i = 0;
        while (current != head || i >= currentSize) {
            if(i == index) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                currentSize--;
                return current.value;
            }
            i++;
            current = current.next;
        }
        return null;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> current = head.next;
        int i = 0;
        while (current != head) {
            if(current.value.equals(o)) {
                return i;
            }
            i++;
            current = current.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = head.prev;
        int i = currentSize-1;
        while (current != head) {
            if(current.value.equals(o)) {
                return i;
            }
            i--;
            current = current.prev;
        }
        return -1;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if(toIndex >= currentSize || toIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        if(fromIndex >= currentSize || fromIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        if(fromIndex >= toIndex) {
            throw new IndexOutOfBoundsException();
        }
        List<E> result = new MyLinkedList<E>();
        Node<E> current = head.next;
        int i = 0;
        while (current != head) {
            if(i <= fromIndex && i < toIndex) {
                result.add(current.value);
            }
            i++;
            current = current.next;
        }
        return result;
    }

    @Override
    public Object[] toArray() {
        Object[] newArray = new Object[currentSize];
        Node<E> current = head.next;
        int i = 0;
        while (current != head) {
            newArray[i] = current.value;
            i++;
            current = current.next;
        }
        return newArray;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyIterator();
    }

    @Override
    public synchronized ListIterator<E> listIterator(int index) {
        if(index >= this.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            return new MyIterator(index);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < currentSize)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), currentSize);
        int i = 0;
        Object[] result = a;
        for (Node<E> current = head.next; current != head; current = current.next)
            result[i++] = current.value;

        if (a.length > currentSize)
            a[currentSize] = null;

        return a;
    }

    @Override
    protected void finalize() throws Throwable {
        lifeTimer.stopTimer();
    }

    @Override
    public String toString() {
        Node<E> current = head.next;

        String str = currentSize + ": ";
        while (current.next != head) {
            str += current.value + " ";
            current = current.next;
        }
        str += (current.value + "");
        return str;
    }


    private class Node<E> {
        public Node(E value) {
            this.value = value;
            creationTime = System.currentTimeMillis();
        }
        private Node<E> next;
        private Node<E> prev;
        private E value;
        private long creationTime;
    }

    private class MyIterator implements ListIterator<E> {

        private Node<E> current;
        private int index;
        private boolean nextInvoked = true;
        private Node<E> lastReturned = null;

        public MyIterator() {
            current = head.next;
        }


        public MyIterator(int index) {
            if(index == size()-1) {
                nextInvoked = false;
            } else {
                nextInvoked = true;
            }
            current = head.next;
            this.index = index;

            int b = 0;
            while(b < index) {
                current = current.next;
                b++;
            }
        }

        @Override
        public void add(E e) {
            Node<E> elemToAdd = new Node<E>(e);
            if(head.next==null) {
                head.next=elemToAdd;
                head.prev = head.next;
                return;
            }

            if(nextInvoked) {
                elemToAdd.next = (current);
                elemToAdd.prev = (current.prev);
                if(current.prev!=null) {
                    current.prev.next = (elemToAdd);
                } else {
                    head.next = elemToAdd;
                }
                current.prev = (elemToAdd);

            } else {
                elemToAdd.prev = (current);
                elemToAdd.next = (current.next);
                if(current.next!=null) {
                    current.next.prev = (elemToAdd);
                } else {
                    head.prev = elemToAdd;
                }
                current.next = (elemToAdd);
            }
        }

        @Override
        public boolean hasNext() {
            return ! (current == head.prev);
        }

        @Override
        public boolean hasPrevious() {
            return ! (current == head.next);
        }

        @Override
        public E next() {
            E value = current.value;
            index++;
            lastReturned = current;
            current = current.next;
            nextInvoked = true;
            return value;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public E previous() {
            E value = current.value;
            index--;
            lastReturned = current;
            current = current.prev;
            nextInvoked = false;
            return value;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if(lastReturned.prev!=null) {
                lastReturned.prev.next = (lastReturned.next);
            } else {
                head.next = lastReturned.next;
            }
            if(lastReturned.next!=null) {
                lastReturned.next.prev = (lastReturned.prev);
            } else {
                head.prev = lastReturned.prev;
            }

        }

        @Override
        public void set(E e) {
            lastReturned.value = (e);
        }

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
                    Node<E> current = head.next;
                    int i = 0;
                    while (current != head) {
                        if(System.currentTimeMillis() - current.creationTime > lifetime) {
                            remove(i);
                            i--;
                        }
                        i++;
                        current = current.next;
                    }
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
