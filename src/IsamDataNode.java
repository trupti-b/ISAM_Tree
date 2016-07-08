package edu.cornell.cs4320.hw2;

import java.util.ArrayList;
import java.util.Iterator;

public class IsamDataNode extends IsamNode {
	protected final Integer[] keys = new Integer[getSize()];
	protected final String[] values = new String[getSize()];

	private IsamDataNode successor = null;
	private IsamDataNode predecessor = null;

	IsamDataNode(int size) {
		super(size);
	}

	@Override
	public String search(Integer key) {
		/*
		 * TODO: Search for an entry with the specified key
		 */
		int i = 0;

		for (i = 0; i < getSize(); i++) {
			if (keys[i] != null) {
				if (keys[i].equals(key)) {
					return this.values[i];
				}
			}
		}
		if (successor != null) {
			return successor.search(key);
		}
		return null;
	}

	public boolean hasOverflow() {
		return successor != null;
	}

	public IsamDataNode getOverflowPage() {
		return successor;
	}

	@Override
	public boolean insert(Integer key, String value) {
		/*
		 * TODO: insert an new value into this node
		 */
		int i = 0;
		for (i = 0; i < getSize(); i++) {
			if (keys[i] == null) {
				// Found an empty slot, insert node.
				keys[i] = key;
				values[i] = value;
				return true;
			}
		}

		if (this.successor == null) {
			// create overflow and add node
			this.successor = new IsamDataNode(getSize());
			this.successor.keys[0] = key;
			this.successor.values[0] = value;
			this.successor.predecessor = this;
			return true;
		} else {
			return successor.insert(key, value);
		}
	}

	@Override
	public String toString() {
		String data = "[ ";

		for (int i = 0; i < getSize(); ++i) {
			Integer key = keys[i];

			if (key == null) {
				data += "E ";
			} else {
				data += keys[i] + " ";
			}
		}

		if (hasOverflow()) {
			return data + successor.toString() + " ]";
		} else {
			return data + "]";
		}
	}

	@Override
	public boolean delete(Integer key) {
		/*
		 * TODO: delete a value from this node (or one of its successors)
		 */
		IsamDataNode temp = this;
		int i = 0;
		Integer tempKey = null;
		String tempValue = null;
		Boolean deleteOverflowPage = false;
		Boolean deletedEntry = false;

		// find the last key stored.
		//Go to the last node
		while(temp.successor != null){
			temp = temp.successor;
		}
		
		for (i = 0; i < getSize(); i++) {
			if (temp.keys[i] == null || (i == (getSize() - 1))) {
				// You have reached the last key in last node.
				if(i == (getSize() - 1) && (temp.keys[i] != null)){
					i++;
				}
				tempKey = temp.keys[i - 1];
				tempValue = temp.values[i - 1];
				
				temp.keys[i - 1] = null;
				temp.values[i - 1] = null;
				if (tempKey.equals(key)) {
					// if last entry is the one to be deleted
					deletedEntry = true;
				}
				if ((i - 1) == 0) {
					deleteOverflowPage = true;
				}
				break;
			}
		}

		// find the key to be deleted
		IsamDataNode temp2 = this;
		if (deletedEntry == false) {
			while (temp2 != null) {
				for (i = 0; i < getSize(); i++) {
					if (temp2.keys[i].equals(key)) {
						temp2.keys[i] = tempKey;
						temp2.values[i] = tempValue;
						deletedEntry = true;
						break;
					}
				}
				if (deletedEntry) {
					break;
				}
				temp2 = temp2.successor;
			}
		}

		if (deleteOverflowPage) {
			// Delete the last node ( as long as its not a leaf node )
			IsamDataNode temp3 = this;
			while (temp3.successor != null) {
				temp3 = temp3.successor;
			}
			if(temp3 != this){
				// Set the predecessor of the last page to have no successor
				temp3.predecessor.successor = null;
			}
		}
		return true;
	}
}
