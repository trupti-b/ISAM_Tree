package edu.cornell.cs4320.hw2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

/**
 * A very simple ISAM tree that makes a few assumptions
 * 
 * - Key is always an Integer - Value is always a String
 */
public class IsamTree {

	// used to keep track of how many nodes are to be created and which entries
	// have been added to leaf node in the tree creation phase
	int positionInSortedArray;
	int indexPosition;

	public IsamTree(int pageSize) {
		assert (pageSize > 0);

		this.pageSize = pageSize;
		positionInSortedArray = 0;
		indexPosition = 0;
	}

	/**
	 * Create initial tree from a data set
	 */
	public void create(Set<Entry<Integer, String>> entries) {
		assert (entries.size() > this.pageSize);
		Integer[] entryKeys = new Integer[entries.size()];
		Integer numberOfIndexEntries = (int) (Math.ceil(entries.size()
				/ pageSize));
		Integer[] indexValuesArray = new Integer[numberOfIndexEntries];
		HashMap<Integer, String> keyValueMap = new HashMap<Integer, String>();
		int k = 0, j = 0;
		Iterator<Entry<Integer, String>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, String> entry = iterator.next();
			keyValueMap.put(entry.getKey(), entry.getValue());
			entryKeys[k] = entry.getKey();
			k++;
		}
		sortKeys(entryKeys);

		for (int i = pageSize; i < entryKeys.length; i = i + pageSize, j++) {
			indexValuesArray[j] = entryKeys[i];
		}
		// Create an new root (old data will be discarded)
		// this.root = new IsamIndexNode(pageSize);

		/*
		 * TODO: - calculate height of the tree first - insert indexes and data
		 */
		double x = (double) ((double) entries.size() / (double) pageSize);
		int no_of_leaf_nodes = (int) Math.ceil(x);
		int heightOfTree = (int) Math.ceil(Math.log(no_of_leaf_nodes)
				/ Math.log(pageSize + 1));
		root = (IsamIndexNode) createTree(root, heightOfTree, keyValueMap,
				entryKeys);
		root.toString();
		fillIndexValues(root, indexValuesArray);
	}

	/**
	 * Index nodes are filled by performing an in-order traversal of the tree
	 * and adding the index values
	 * 
	 * @param root
	 * @param indexValuesArray
	 */
	private void fillIndexValues(IsamNode root, Integer[] indexValuesArray) {
		if (root == null) {
			return;
		}
		if (root instanceof IsamDataNode) {
			return;
		}

		IsamIndexNode root2 = (IsamIndexNode) root;
		fillIndexValues(root2.children[0], indexValuesArray);
		for (int i = 0; i < getPageSize(); i++) {
			if (indexPosition < indexValuesArray.length) {
				root2.keys[i] = indexValuesArray[indexPosition];
				indexPosition++;
				fillIndexValues(root2.children[i + 1], indexValuesArray);
			}
		}

	}

	/**
	 * Creates an empty tree using the computed height. Leaf nodes are also
	 * filled as the tree is created, the creation is stopped when all the
	 * entries in the entryKeys array have been added to leaves.
	 * 
	 * @param root2
	 * @param heightOfTree
	 * @param keyValueMap
	 * @param entryKeys
	 * @return
	 */
	private IsamNode createTree(IsamNode root2, int heightOfTree,
			HashMap<Integer, String> keyValueMap, Integer[] entryKeys) {

		if (heightOfTree == 0) {
			if (this.positionInSortedArray >= entryKeys.length) {
				return null;
			}
			IsamDataNode leaf = new IsamDataNode(this.pageSize);
			for (int i = 0; i < this.pageSize; i++) {
				if (this.positionInSortedArray < entryKeys.length) {
					leaf.keys[i] = entryKeys[this.positionInSortedArray];
					this.positionInSortedArray++;
					leaf.values[i] = keyValueMap.get(leaf.keys[i]);
				}
			}
			return leaf;
		}
		IsamIndexNode root3 = null;
		if (root2 == null) {
			if (this.positionInSortedArray >= entryKeys.length) {
				return null;
			}
			root3 = new IsamIndexNode(this.pageSize);
			for (int i = 0; i < this.pageSize + 1; i++) {
				root3.children[i] = createTree(root3.children[i],
						heightOfTree - 1, keyValueMap, entryKeys);
			}
		}
		return root3;
	}

	private void sortKeys(Integer[] keyArray) {

		int i = 0, j = 0, min = 0, temp;
		for (i = 0; i < keyArray.length; i++) {
			min = i;
			for (j = i + 1; j < keyArray.length; j++) {
				if (keyArray[j] < keyArray[min]) {
					min = j;
				}
			}
			if (min != i) {
				temp = keyArray[i];
				keyArray[i] = keyArray[min];
				keyArray[min] = temp;
			}
		}
	}

	/**
	 * Get the height of this tree
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the root of this tree Note: Should only be used internally and/or by
	 * helper classes
	 */
	protected IsamIndexNode getRoot() {
		return root;
	}

	/**
	 * Get a in-order string representation of the tree
	 * 
	 * Note: - this only prints the indexes - index nodes should be shown by
	 * curly braces - data nodes by square brackets - empty indexes by the
	 * letter 'E' - and empty nodes/subtrees by ()
	 * 
	 * See the unit tests for examples
	 */
	public String toString() {
		return root.toString();
	}

	/**
	 * Search for a specific entry Returns the entry if found and null otherwise
	 */
	public String search(Integer key) {
		return root.search(key);
	}

	/**
	 * Insert a new value This will return false if the value already exists
	 */
	public boolean insert(Integer key, String value) {

		if (this.search(key) != null) {
			return false;
		}
		return root.insert(key, value);
	}

	/**
	 * Remove the entry with the specified key This will return false if the
	 * value wasn't found
	 */
	public boolean delete(Integer key) {
		if (this.search(key) == null) {
			return false;
		}
		return root.delete(key);
	}

	/**
	 * Get the size of one page/node
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Root of the tree It is assumed that this is never a data node
	 */
	private IsamIndexNode root;

	/**
	 * Size of each node/page This is set via the constructor
	 */
	private final int pageSize;

	/**
	 * The height of the tree Should be calculated by create()
	 */
	private int height;
}
