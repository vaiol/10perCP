package com.checkpoint.vaiol;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.checkpoint.vaiol.myDataStructure.MyArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ArrayListTest {

	private MyArrayList<Integer> list;
	private List<Integer> containsAllTrue;
	private List<Integer> containsAllFalse;
	private List<Integer> checkAddAll;
	
	@Before
	public void init() {

		list = new MyArrayList<Integer>();
		for(int i = 0; i<10; i++) {
			list.add(i);
		}
		
		containsAllTrue = Arrays.asList(new Integer(0),new Integer(4),new Integer(9));
		containsAllFalse = Arrays.asList(new Integer(-1),new Integer(2),new Integer(5));
		checkAddAll = Arrays.asList(new Integer(13),new Integer(12),new Integer(11));
	}
	
	@Test
	public void temporalRemovement() throws InterruptedException {

		MyArrayList<Integer> ls = new MyArrayList<Integer>();

		for(int i = 0; i<15; i++) {
			ls.add(i);
			Thread.sleep(1000);
		}

		MyArrayList<Integer> toCompare = new MyArrayList<Integer>();

		for(int i=6; i<15; i++)
			toCompare.add(new Integer(i));

		Thread.sleep(200);
		assertEquals(toCompare, ls);
	}
	
	@Test
	public void testAdd() {
		Assert.assertEquals(list.size(), 10);
	}
	
	
	@Test 
	public void testIndexualAdd(){
		list.add(0,new Integer(11));
		list.add(3,new Integer(12));
		list.add(5,new Integer(13));
		list.add(list.size(),new Integer(14));

		Assert.assertEquals(14,list.size());
		assertEquals(new Integer(11),list.get(0));
		assertEquals(new Integer(12),list.get(3));
		assertEquals(new Integer(13),list.get(5));

		assertEquals(new Integer(14),list.get(list.size()-1));
	}
	
	@Test
	public void testGet() {

		Assert.assertEquals(new Integer(0),list.get(0));
		Assert.assertEquals(new Integer(4),list.get(4));
		Assert.assertEquals(new Integer(9),list.get(9));
	}
	
	@Test
	public void testSet(){
		list.set(0, new Integer(15));
		list.set(3, new Integer(16));
		list.set(list.size()-1, new Integer(17));
		Assert.assertEquals(new Integer(16),list.get(3));
		Assert.assertEquals(new Integer(15),list.get(0));
		Assert.assertEquals(new Integer(17),list.get(list.size()-1));

	}

	
	@Test
	public void testClear(){
		list.clear();
		Assert.assertEquals(0,list.size());
	}
	
	@Test
	public void testContains() {
		Assert.assertEquals(true,list.containsAll(containsAllTrue));
		Assert.assertEquals(false,list.containsAll(containsAllFalse));
	}
	
	@Test
	public void testContainsSingle() {
		Assert.assertEquals(true,list.contains(new Integer(4)));
		Assert.assertEquals(false,list.contains(new Integer(11)));
	}
	
	@Test
	public void testIndexOf() {
		Assert.assertEquals(4,list.indexOf(new Integer(4)));
		Assert.assertEquals(0,list.indexOf(new Integer(0)));
		Assert.assertEquals(9,list.indexOf(new Integer(9)));
		Assert.assertEquals(-1,list.indexOf(new Integer(11)));
	}
	
	@Test
	public void testAddAll() {
		list.addAll(1,checkAddAll);
		for(int i=1, j=0; i<4; i++, j++)
			assertEquals(checkAddAll.get(j),list.get(i));
	}
	
	@Test
	public void testLastIndex() {
		list.add(6,new Integer(3));
		Assert.assertEquals(6, list.lastIndexOf(new Integer(3)));
		Assert.assertEquals(-1, list.lastIndexOf(new Integer(11)));
		Assert.assertEquals(1, list.lastIndexOf(new Integer(1)));
	}
	
	@Test 
	public void testRemove() {
		list.remove(new Integer(0));
		list.remove(new Integer(4));
		list.remove(new Integer(list.size()-1));
	}
	
	@Test 
	public void testRemoveIndex() {
		list.remove((int)0);
		list.remove((int)4);
		list.remove((int)(list.size()-1));
		
		List<Integer> l = Arrays.asList(new Integer(0),new Integer(5),new Integer(9));
		
		for(int i=0; i<list.size(); i++) {
			assertEquals(false,l.contains(list.get(i)));
		}
	}
	
	
	@Test 
	public void testRemoveAll(){
		list.removeAll(containsAllTrue);
		for(int i =0; i<list.size(); i++)
			assertEquals(false,containsAllTrue.contains(list.get(i)));
	}
	
	@Test 
	public void testRetainAll(){
		list.retainAll(containsAllTrue);
		for(int i=0; i<containsAllTrue.size();i++)
			assertEquals(containsAllTrue.get(i),list.get(i));
	}
	
	@Test
	public void testToArray() {
		MyArrayList<Integer> arr = new MyArrayList<Integer>();
		for(int i=0; i<5; i++){
			arr.add(i);
		}

		Object o[] = arr.toArray();
		for(int i=0; i<5; i++){
			Assert.assertEquals(i, o[i]);
		}
	}
}