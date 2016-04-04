/**
 * 
 *The class Key is used to access to the hashset representing the PaullMatrix. 
 *The object key contains two integers that represent the first stage index (row index in the Paull Matrix) 
 *and the third stage index (column index of the Paull Matrix).
 *Each index indicates a matrix in the first and third stage of the Slepian-Duguid network.
 *
 * @author De Silva, Pébrier, Caballero 
 *
 */

package slepianDuguidSim;

public class Key {

	private int r1Index;
	private int r3Index;
	
	/**
	 * Constructor
	 */
	public Key(int r1Index, int r3Index) {
	
		
		this.r1Index=r1Index;
		this.r3Index=r3Index;
		
	}
	

	
	
	/**
	 * Calculate the hashcode of the key. This method is used to compare keys with the same coordinates
	 * (but that are different object). 
	 * 
	 * @return the ingeger representation of the key. 
	 */
	public int hashCode() {
        
		String first = Integer.toString(r1Index)+Integer.toString(r3Index);
		
		int hash = 1+first.hashCode()+Integer.toString(r1Index).hashCode();
        hash = hash * 17 + (r1Index+r3Index)^2/(r1Index+1);
        return hash;
    }
	
	/**
	 * Method that check if 
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
	
	public int getR1Index(){ //TODO TEST, DA ELIMINARE
		return r1Index; 
	}
	
	public int getR2Index(){
		return r3Index; 
	}
}
