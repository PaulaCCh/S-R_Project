/**
 * 
 * The Cell class represents the single entry of the PaullMatrix. 
 * Each cell contains an hashset of integers representing the matrices through which we establish a connection
 * between a first stage matrix and a third stage one.  
 *
 * @author De Silva, Pébrier, Caballero 
 *
 */

package source;

import java.util.HashSet;


public class Cell {


	private HashSet<Integer> activeConnections;
	
	/**
	 * Constructor of the class Element
	 */
	public Cell() {
		
		activeConnections = new HashSet<Integer>();
		
		
	}
	
	
	/**
	 * 
	 * This method checks if a value is contained in the cell. 
	 * @param  	symbol to look for in the cell
	 * @return 	true if the symbols is found in the cell, false otherwise
	 */
	public boolean contains(int c) {
		
		return activeConnections.contains(c);
		
	}
	
	/**
	 * This method inserts a new symbol in the cell, if the symbol it is not already present.
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
	
	
	/**
	 * 
	 * This method gives a string representation of the symbols contained. 
	 * @return 	a string representing the elements in the cell.
	 */
	public String toString() {
		
		return activeConnections.toString();	
		
	}
	
	
	/**
	 * This method creates a copy of the cell containing the same elements. 
	 * @return the cloned cell
	 */
	public Cell clone() {
		
		Cell elem = new Cell();
		
		for(Integer i: activeConnections) {
			
			elem.insert(i);
			
		}
		return elem;
		
	}
	

	/**
	 * This method remove the given symbol if it is contained in the cell.
	 * 
	 * @param  	symbol to be deleted
	 * 		
	 */
	public void remove(int c) {
		
		if(activeConnections.contains(c)) {
			
			activeConnections.remove(c);
		}
		
	}
	
}
