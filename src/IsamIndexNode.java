package edu.cornell.cs4320.hw2;

public class IsamIndexNode extends IsamNode {
	protected final Integer[] keys = new Integer[getSize()];
	protected final IsamNode[] children = new IsamNode[getSize() + 1];
		
	IsamIndexNode(int size) {
		super(size);
	}
		
	public Integer getIndex(int pos) {
		return keys[pos];
	}
	
	@Override
	public String search(Integer key) {
		/*
		 * TODO: retrieve a value from the node
		 */
		int i = 0;
		for(i = 0; i < getSize(); i++){
			if(keys[i] == null){			
				return this.children[i].search(key);
			}else if(keys[i] > key){
				return this.children[i].search(key);
			}else if(keys[i].equals(key)){
				return this.children[i+1].search(key);
			}
		}
		return this.children[i].search(key);

	}

	@Override
	public boolean insert(Integer key, String value) {
		/*
		 * TODO: insert a new value into the (sub)tree
		 */
		int i = 0;
		for(i = 0; i < getSize(); i++){
			if(keys[i] == null){			
				return this.children[i].insert(key, value);
			}else if(keys[i] > key){
				return this.children[i].insert(key, value);
			}else if(keys[i].equals(key)){
				return children[i+1].insert(key, value);
			}
		}
		return this.children[i].insert(key, value);
	}

	/**
	 * Create a child at a specific position and give the height of the subtree
	 * Only for internal use
	 */
	protected void createChild(int i, int height) {
		/*
		 * TODO: create the correct type of child depending on height and position
		 */
	}
	
	/**
	 * Get child at a specific position
	 */
	public IsamNode getChild(int pos) {
		return children[pos];
	}

	/**
	 * Get the to string for a specific child
	 * (this is a helper function for toString)
	 */
	private String getChildInOrderString(int pos) {
		IsamNode child = getChild(pos);
		
		if(child == null) {
			return "()";
		} else {
			return child.toString();
		}
	}

	@Override
	public String toString() {
		// Dont change this. This already does everything it is supposed to do
		String output = "";
		
		output += "(";
		
		int i = 0; 
		
		for(; i < getSize(); ++i) {
			output += getChildInOrderString(i);
				
			Integer index = getIndex(i);						
			if(index != null) {
				output += " " + index + " ";
			} else {
				output += " E ";
			}
		}
		
		output += getChildInOrderString(i);
		output += ")";
		
		return output;
	}

	@Override
	public boolean delete(Integer key) {
		/*
		 * TODO: delete an entry with the specified key
		 */
		int i = 0;
		for(i = 0; i < getSize(); i++){
			if(keys[i] == null){			
				return this.children[i].delete(key);
			}else if(keys[i] > key){
				return this.children[i].delete(key);
			}else if(keys[i].equals(key)){
				return this.children[i+1].delete(key);
			}
		}
		return this.children[i].delete(key);
	}
}
