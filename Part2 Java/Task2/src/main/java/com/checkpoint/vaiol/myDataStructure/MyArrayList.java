package com.checkpoint.vaiol.myDataStructure;

import java.lang.reflect.Array;
import java.util.*;

public class MyArrayList<E> implements List<E> {


    private Entity<E>[] array;
    private int currentSize;

    private static final int INITIAL_CAPACITY = 10;
    private static final double FACTOR = 1.5;

    public MyArrayList() {
        currentSize = 0;
        array = new Entity[INITIAL_CAPACITY];
    }

    public MyArrayList(int initialCapacity) {
        currentSize = 0;
        array = new Entity[initialCapacity];
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
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
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        array = new Entity[INITIAL_CAPACITY];
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
        return tmp;
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

    private class Entity<E> {
        public Entity(E object, long createTime) {
            this.object = object;
            this.createTime = createTime;
        }
        private E object;
        private long createTime;
    }
}
