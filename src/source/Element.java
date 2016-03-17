/**
 * 
 * The class Element is used to represent the single element (row/column)
 * of the Paull matrix. An object of the class element contains a HashSet
 * which is the list of the active connections/symbols active in that 
 * element.
 *
 * @author Maretti, Elnor, Huamani
 *
 */

package source;

import java.util.HashSet;


public class Element {


	private HashSet<Integer> activeConnections;
	
	/**
	 * Constructor of the class Element
	 */
	public Element() {
		
		activeConnections = new HashSet<Integer>();
		
		
	}
	
	
	/**
	 * This method looks for the element c in the hashset and returns a boolean
	 * value on whether the element is contained or not in the set.
	 *
	 * @param  	symbol to look for in the set
	 * @return 	true if the symbols is found in the set, false otherwise
	 */
	public boolean contains(int c) {
		
		return activeConnections.contains(c);
		
	}
	
	/**
	 * This method inserts a new symbol in the set, if the symbol it is not already present.
	 * 
	 * @param  	symbol to be inserted
	 * @return 	true if the symbols is inserted correctly, false is the element is already in
	 * 			the set
	 */
	public boolean insert(int c) {
		
		if(!activeConnections.contains(c)) {
			
			activeConnections.add(c);
			return true;
			
		}
		return false;
		
	}
	
	
	public void remove(int c) {
		
		if(activeConnections.contains(c)) {
			
			activeConnections.remove(c);
		}
		
	}
	
	/**
	 * This method returns the symbols contained in the set as a string for representation
	 * purposes
	 * @return 	a string representing the elements in the set
	 */
	public String toString() {
		
		return activeConnections.toString();
		
		
	}
	
	
	/**
	 * This method clones an element by creating a new disjoint element with the same
	 * items as the current element
	 * @return 	and new Element object which is the copy of the current element.
	 */
	public Element clone() {
		
		Element elem = new Element();
		
		for(Integer i: activeConnections) {
			
			elem.insert(i);
			
		}
		return elem;
		
	}
	
}
