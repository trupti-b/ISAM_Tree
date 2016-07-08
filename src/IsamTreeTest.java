package edu.cornell.cs4320.hw2.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import org.junit.Test;

import edu.cornell.cs4320.hw2.IsamTree;

public class IsamTreeTest {
	@Test
	public void simpleTree() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		data.add(new SimpleEntry<Integer, String>(35, "lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals("([ 10 15 ] 20 [ 20 27 ] 35 [ 35 E ])", tree.toString());
		assertEquals("the", tree.search(10));
	}

	@Test
	public void simpleTree2() {
		int pageSize = 3;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		// data.add(new SimpleEntry<Integer,String>(35,"lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals("([ 10 15 20 ] 27 [ 27 E E ] E () E ())", tree.toString());
		assertEquals("the", tree.search(10));
		assertEquals("a", tree.search(27));
		// assertEquals("lie",tree.search(35));
		assertEquals(null, tree.search(7));
	}

	@Test
	public void cornerCaseTree() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(25, "a"));
		data.add(new SimpleEntry<Integer, String>(30, "lie"));
		data.add(new SimpleEntry<Integer, String>(35, "lie"));
		data.add(new SimpleEntry<Integer, String>(40, "lie"));
		data.add(new SimpleEntry<Integer, String>(45, "lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals(
				"(([ 10 15 ] 20 [ 20 25 ] 30 [ 30 35 ]) 40 ([ 40 45 ] E () E ()) E ())",
				tree.toString());
		// assertEquals("the", tree.search(10));
	}

	@Test
	public void textBookTree() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "this"));
		data.add(new SimpleEntry<Integer, String>(15, "is"));
		data.add(new SimpleEntry<Integer, String>(20, "just"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		data.add(new SimpleEntry<Integer, String>(33, "simple"));
		data.add(new SimpleEntry<Integer, String>(37, "test"));
		data.add(new SimpleEntry<Integer, String>(40, "of"));
		data.add(new SimpleEntry<Integer, String>(46, "the"));
		data.add(new SimpleEntry<Integer, String>(51, "data"));
		data.add(new SimpleEntry<Integer, String>(55, "bases"));
		data.add(new SimpleEntry<Integer, String>(63, "text"));
		data.add(new SimpleEntry<Integer, String>(97, "book"));
		data.add(new SimpleEntry<Integer, String>(100, "book"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals(
				"(([ 10 15 ] 20 [ 20 27 ] 33 [ 33 37 ]) 40 ([ 40 46 ] 51 [ 51 55 ] 63 [ 63 97 ]) 100 ([ 100 E ] E () E ()))",
				tree.toString());
		assertEquals("this", tree.search(10));
	}

	@Test
	public void initialTree5() {
		int pageSize = 5;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(1, "this"));
		data.add(new SimpleEntry<Integer, String>(2, "is"));
		data.add(new SimpleEntry<Integer, String>(3, "just"));
		data.add(new SimpleEntry<Integer, String>(4, "a"));
		data.add(new SimpleEntry<Integer, String>(5, "simple"));
		data.add(new SimpleEntry<Integer, String>(6, "test"));
		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(8, "db"));
		data.add(new SimpleEntry<Integer, String>(9, "class"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals("([ 1 2 3 4 5 ] 6 [ 6 8 9 10 E ] E () E () E () E ())",
				tree.toString());
		assertEquals("db", tree.search(8));
		assertEquals(null, tree.search(500));
	}

	@Test
	public void insertDataIntoTree() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(1, "this"));
		data.add(new SimpleEntry<Integer, String>(2, "is"));
		data.add(new SimpleEntry<Integer, String>(3, "just"));
		// data.add(new SimpleEntry<Integer,String>(4, "a"));
		data.add(new SimpleEntry<Integer, String>(5, "simple"));
		data.add(new SimpleEntry<Integer, String>(6, "test"));
		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(8, "db"));
		data.add(new SimpleEntry<Integer, String>(9, "class"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		assertEquals(true, tree.insert(4, "a"));
		assertEquals(false, tree.insert(5, "simple"));
	}

	@Test
	public void overflow() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		data.add(new SimpleEntry<Integer, String>(35, "lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		tree.insert(36, "1");
		tree.insert(37, "2");
		tree.insert(38, "3");

		assertEquals("([ 10 15 ] 20 [ 20 27 ] 35 [ 35 36 [ 37 38 ] ])",
				tree.toString());
	}

	@Test
	public void deleteFromTree() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		data.add(new SimpleEntry<Integer, String>(35, "lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		tree.insert(40, "a");
		tree.insert(45, "b");
		tree.insert(50, "c");
		tree.insert(55, "d");
		tree.insert(60, "e");

		assertEquals(
				"([ 10 15 ] 20 [ 20 27 ] 35 [ 35 40 [ 45 50 [ 55 60 ] ] ])",
				tree.toString());
		assertEquals(true, tree.delete(20));
		assertEquals(true, tree.delete(27));
		assertEquals(
				"([ 10 15 ] 20 [ E E ] 35 [ 35 40 [ 45 50 [ 55 60 ] ] ])",
				tree.toString());

	}

	@Test
	public void deletion() {
		int pageSize = 2;
		Set<Entry<Integer, String>> data = new HashSet<Entry<Integer, String>>();

		data.add(new SimpleEntry<Integer, String>(10, "the"));
		data.add(new SimpleEntry<Integer, String>(15, "cake"));
		data.add(new SimpleEntry<Integer, String>(20, "is"));
		data.add(new SimpleEntry<Integer, String>(27, "a"));
		data.add(new SimpleEntry<Integer, String>(35, "lie"));

		IsamTree tree = new IsamTree(pageSize);
		tree.create(data);

		tree.insert(40, "a");
		tree.insert(45, "b");
		tree.insert(50, "c");
		tree.insert(55, "d");
		tree.insert(60, "e");

		assertEquals(
				"([ 10 15 ] 20 [ 20 27 ] 35 [ 35 40 [ 45 50 [ 55 60 ] ] ])",
				tree.toString());
		assertEquals(true, tree.delete(60));
		
		assertEquals(false, tree.delete(60));
		assertEquals(true, tree.delete(35));
		assertEquals("([ 10 15 ] 20 [ 20 27 ] 35 [ 55 40 [ 45 50 ] ])",
				tree.toString());

	}
}
