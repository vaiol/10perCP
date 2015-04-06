package com.checkpoint.vaiol.myDataStructure;


import com.sun.istack.internal.NotNull;

import java.util.*;

public class MyArrayList<E> implements List<E> {

    private Entity<E>[] array;
    private int currentSize;
    private long lifetime;
    private LifeTimer lifeTimer;


    private static final int INITIAL_CAPACITY = 10;
    private static final double FACTOR = 1.5;


    @SuppressWarnings("unchecked")
    public MyArrayList() {
        currentSize = 0;
        array = new Entity[INITIAL_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public MyArrayList(int initialCapacity) {
        currentSize = 0;
        array = new Entity[initialCapacity];
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
    public boolean add(E e) {
        add(currentSize, e);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(int index, E element) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if(currentSize >= array.length) {
            Entity[] newArray = new Entity[ (int)(array.length * FACTOR) ];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        System.arraycopy(array, index, array, index + 1, array.length - (index + 1));
        array[index] = new Entity<E>(element, System.currentTimeMillis());
        currentSize++;
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
        for(int i = 0; i < currentSize; i++) {
            if(array[i].object.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[currentSize];
        for(int i = 0; i < currentSize; i++) {
            result[i] = array[i].object;
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean b = false;
        for(int i = 0; i < currentSize; i++) {
            if(array[i].object.equals(o)) {
                remove(i);
                b = true;
            }
        }
        return b;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object object : c) {
            if ( ! contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends E> c) {
        for(Object object : c) {
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
    @SuppressWarnings("unchecked")
    public void clear() {
        array = new Entity[INITIAL_CAPACITY];
        currentSize = 0;
    }

    @Override
    public E get(int index) {
        return array[index].object;
    }

    @Override
    public E set(int index, E element) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E tmp = array[index].object;
        array[index] = new Entity<E>(element, System.currentTimeMillis());
        return tmp;
    }

    @Override
    public E remove(int index) {
        if(index > currentSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E tmp = array[index].object;
        System.arraycopy(array, index + 1, array, index, array.length - (index + 1));
        currentSize--;
        return tmp;
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
        boolean b = false;
        for(int i = 0; i < currentSize; i++) {
            if( ! c.contains(array[i].object)) {
                remove(array[i].object);
                b = true;
                i--;
            }
        }
        return b;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < currentSize; i++) {
            if(array[i].object.equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = (currentSize - 1); i >= 0; i--) {
            if (array[i].object.equals(o)) {
                return i;
            }
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
        List<E> result = new MyArrayList<E>(currentSize);
        for(int i = fromIndex; i < toIndex; i++) {
            result.add((E) array[i].object);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < currentSize) {
            return (T[]) Arrays.copyOf(array, currentSize, a.getClass());
        }
        System.arraycopy(array, 0, a, 0, currentSize);
        if (a.length > currentSize) {
            a[currentSize] = null;
        }
        return a;
    }

    @Override
    public ListIterator<E> listIterator() {
        //todo
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        //todo
        return null;
    }


    @Override
    public Iterator<E> iterator() {
        //todo
        return null;
    }

    private class Entity<E> {
        public Entity(E object, long creationTime) {
            this.object = object;
            this.creationTime = creationTime;
        }
        private E object;
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
                synchronized(MyArrayList.this) {
                    for(int i = 0; i < currentSize; i++) {
                        if(System.currentTimeMillis()- array[i].creationTime > lifetime) {
                            remove(i);
                            i--;
                        }
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
