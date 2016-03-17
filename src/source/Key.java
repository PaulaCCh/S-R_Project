/**
 * 
 * The class Key indicates the key used to indicize elements in the HashMap
 * of the class PaullMatrix. A key is composed by the index of the first stage
 * matrix and the index of the third stage matrix which correspond respectively
 * to the row and the column index of the Paull Matrix.
 *
 * @author Maretti, Elnor, Huamani
 *
 */

package source;

public class Key {

	private int firstStageIndex;
	private int thirdStageIndex;
	
	/**
	 * Constructor of the class Key
	 * 
	 * @param 	index of the first and last stage matrix corresponding to the index
	 * 			of the row and column of the Paull matrix
	 * 
	 */
	public Key(int firstStageIndex, int thirdStageIndex) {
		
		this.firstStageIndex=firstStageIndex;
		this.thirdStageIndex=thirdStageIndex;
		
	}
	
	/**
	 * Implementation of the equals needed to manage the hashtable
	 * 
	 * @param object to be compared
	 * @return true if the object k is equal to the current object, otherwise false
	 */
	public boolean equals(Object k) {

		
		
		if(k.hashCode()==this.hashCode()) {
			
			return true;
			
		}
		return false;
		
	}
	
	/**
	 * Implementation of the hashcode in order to manage the hashtable
	 * 
	 * @return an integer which is the hash representation of the current object
	 */
	public int hashCode() {
        
		String first = Integer.toString(firstStageIndex)+Integer.toString(thirdStageIndex);
		
		int hash = 1+first.hashCode()+Integer.toString(firstStageIndex).hashCode();
        hash = hash * 17 + (firstStageIndex+thirdStageIndex)^2/(firstStageIndex+1);
        return hash;
    }
	
	
}
