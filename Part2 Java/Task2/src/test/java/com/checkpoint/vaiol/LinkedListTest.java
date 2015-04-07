package com.checkpoint.vaiol;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.checkpoint.vaiol.myDataStructure.MyLinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class LinkedListTest {

	private List<Integer> list;
	private List<Integer> contains;
	private List<Integer> doesNotContain;
	private List<Integer> addAll;
	
	@Before
	public void initialize() {

		list = new MyLinkedList<Integer>();
		for(int i = 0; i < 10; i++) {
			list.add(i);
		}
		contains = Arrays.asList(0, 4, 9);
		doesNotContain = Arrays.asList(-1, 2, 5);
		addAll = Arrays.asList(13, 12, 11);
	}
	
	@Test
	public void testRemoveOldObject() throws InterruptedException {
		MyLinkedList<Integer> linkedList = new MyLinkedList<Integer>();
		linkedList.startLifeTimer(1000);

		for(int i = 0; i < 10; i++) {
			linkedList.add(i);
		}
		assertFalse(linkedList.isEmpty());
		Thread.sleep(1010);
		assertTrue(linkedList.isEmpty());
		linkedList.stopLifeTimer();
	}
	
	@Test
	public void testAdd() {
		Assert.assertEquals(list.size(), 10);
		Assert.assertEquals(new Integer(0), list.get(0));
		Assert.assertEquals(new Integer(4), list.get(4));
		Assert.assertEquals(new Integer(9), list.get(9));
	}
	
	
	@Test 
	public void testAddByIndex(){
		list.add(1, 65);
		list.add(2, 66);
		list.add(7, 67);
		list.add((list.size()), 68);

		Assert.assertEquals(14, list.size());
		assertEquals(new Integer(65),list.get(1));
		assertEquals(new Integer(66),list.get(2));
		assertEquals(new Integer(67),list.get(7));
		assertEquals(new Integer(68),list.get(list.size()-1));
	}
	
	@Test
	public void testGet() {

		Assert.assertEquals(new Integer(0),list.get(0));
		Assert.assertEquals(new Integer(4),list.get(4));
		Assert.assertEquals(new Integer(9),list.get(9));
	}
	
	@Test
	public void testSet(){
		list.set(0, 25);
		list.set(3, 34);
		list.set(list.size() - 1, 27);
		Assert.assertEquals(new Integer(34),list.get(3));
		Assert.assertEquals(new Integer(25),list.get(0));
		Assert.assertEquals(new Integer(27),list.get(list.size() - 1));

	}
	
	@Test
	public void testClear(){
		list.clear();
		Assert.assertEquals(0, list.size());
	}
	
	@Test
	public void testContains() {
		Assert.assertEquals(true, list.containsAll(contains));
		Assert.assertEquals(false, list.containsAll(doesNotContain));
	}
	
	@Test
	public void testContainsOneElement() {
		Assert.assertEquals(true, list.contains(new Integer(4)));
		Assert.assertEquals(false, list.contains(new Integer(1423423423)));
	}
	
	@Test
	public void testIndexOf() {
		Assert.assertEquals(4,list.indexOf(4));
		Assert.assertEquals(0,list.indexOf(0));
		Assert.assertEquals(9,list.indexOf(9));
		Assert.assertEquals(-1,list.indexOf(11));
	}
	
	@Test
	public void testAddAll() {
		list.addAll(1, addAll);

		for(int i = 1, j = 0; i < 4; i++, j++) {
			assertEquals(addAll.get(j), list.get(i));
		}
	}
	
	@Test
	public void testLastIndexOf() {
		list.add(6, 3);
		Assert.assertEquals(6, list.lastIndexOf(3));
		Assert.assertEquals(-1, list.lastIndexOf(11));
		Assert.assertEquals(1, list.lastIndexOf(1));
	}
	
	@Test 
	public void testRemove() {
		assertTrue(list.remove(new Integer(0)));
		assertTrue(list.remove(new Integer(4)));
		assertTrue(list.remove(new Integer(list.size()-1)));
	}
	
	@Test 
	public void testRemoveByIndex() {
		list.remove(0);
		list.remove(4);
		list.remove(list.size()-1);

		List<Integer> l = Arrays.asList(0, 5, 9);

		for(Integer i : list) {
			assertEquals(false, l.contains(i));
		}
	}
	
	
	@Test 
	public void testRemoveAll(){
		list.removeAll(contains);
		for(Integer i : list) {
			assertEquals(false, contains.contains(i));
		}
	}
	
	@Test 
	public void testRetainAll(){
		list.retainAll(contains);
		for(int i = 0; i < contains.size(); i++) {
			assertEquals(contains.get(i), list.get(i));
		}
	}
	
	@Test
	public void testToArray() {
		MyLinkedList<Integer> arr = new MyLinkedList<Integer>();
		for (int i = 0; i < 5; i++) {
			arr.add(i);
		}

		Object o[] = arr.toArray();
		for (int i = 0; i < 5; i++) {
			assertEquals(i, o[i]);
		}
	}

	@Test
	public void testForeach() {
		MyLinkedList<Integer> arr = new MyLinkedList<Integer>();
		for (int i = 0; i < 5; i++) {
			arr.add(i);
		}
		int j = 0;
		for (int i : arr) {
			assertEquals(i, j++);
		}
	}
}