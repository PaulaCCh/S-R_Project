/**
 * MODEL OF THE MVC DESIGN PATTERN
 * The matrix class contains all the logic of the program, it includes two main methods to 
 * rearrange the matrix and add a new connection in the network.
 * 
 * @author De Silva, Pébrier, Caballero 
 *
 */

package slepianDuguidSim;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

public class Matrix {

	private Hashtable<Key, Cell> matrix = new Hashtable<Key, Cell>();
	private int r1;
	private int r2;
	private int r3;
	private int policy; 
	private ArrayList<Integer> rearrangementsArray;

	

	/**
	 * Constructor of the class matrix
	 * 
	 * @param Slepian-Duguid network parameters
	 */
	
	public Matrix(Data param) {
		this.r1 = param.getR1();
		this.r2 = param.getR2();
		this.r3 = param.getR3();
		rearrangementsArray = new ArrayList<Integer>();
		this.policy = param.getPolicy();

		for (int i = 1; i <= param.getR1(); i++) {

			for (int j = 1; j <= param.getR3(); j++) {
				Cell elem = new Cell();
				Key id = new Key(i, j);
				matrix.put(id, elem);
			}
		}		
	}
	
	

	/**
	 * This method add a new connection between the matrices indicated by the two parameter. 
	 * If there's a free common symbol in row and column, the connection is established through 
	 * the matrix represented by the symbol. If it's not the case, the method call the 
	 * reArrange method which performs rearrangements according to the algorithm
	 *  of the Slepian-Duguid network. 
	 *  
	 *  @param row and column of the matrices of the new connection
	 *  @return true if the adding connection succeeds, false otherwise
	 *  
	 */
	public boolean establishConnection(int row, int column) {
		HashSet<Integer> unusuedSymbolsRow = new HashSet<Integer>(findUnusuedSymbols(0,row)); 
		HashSet<Integer> unusuedSymbolsColumn = new HashSet<Integer>(findUnusuedSymbols(1,column)); 
		
		HashSet<Integer> commonMissingSymbols = new HashSet<Integer>(unusuedSymbolsRow);
		commonMissingSymbols.retainAll(unusuedSymbolsColumn);
		
		// CASE 1: there is at least one symbol missing in the row AND column 
		if (!commonMissingSymbols.isEmpty()) { 

			Key k = new Key(row, column);
			int symbol = 0;
			for (Integer i : commonMissingSymbols) {  
				symbol = i;
				break;
			}
			Cell elem = matrix.get(k);
			elem.insert(symbol);
			return true;
		}

		// CASE 2: there are NOT symbols missing in both row and column, REARRANGMENT(S) needed 

		else {
			System.out.println("Rearrangement needed for connection between matrices "+row+"-"+column); 
			System.out.println(matrixToString()); 
			
			ArrayList<Integer> symInColNotInRow = new ArrayList<Integer>(); // symbols in column that are not found in row

			for (Integer i : unusuedSymbolsRow) { 
				
				boolean inColNotInRow=true; 
				for (Integer j : unusuedSymbolsColumn) { 

					if (i == j) {
						inColNotInRow=false;
					}
				}
				if (inColNotInRow) { 
					symInColNotInRow.add(i);

				}
			}
					
			ArrayList<Integer> symInRowNotInCol = new ArrayList<Integer>(); 

			for (Integer i : unusuedSymbolsColumn) { 
				boolean inRowNotInCol=true; 
				for (Integer j : unusuedSymbolsRow) {

					if (i == j) {
						inRowNotInCol=false; 
					}
				}
				if (inRowNotInCol) { 
					symInRowNotInCol.add(i); 
				}
			}
			
			Random ran = new Random(); 
			Hashtable<Key, Cell> duplicatedMatrix1;
			Hashtable<Key, Cell> duplicatedMatrix2;
			
			
			
			switch (policy) {

			case 1: // random policy: symbols and starting row/column randomly chosen 
				int indexRowOrColSymbolToLookFor;
				int indexRowOrColSymbolToAdd;
				int startRowOrCol=ran.nextInt(2);

				if (startRowOrCol == 0) { 
					
					indexRowOrColSymbolToLookFor = ran.nextInt(symInRowNotInCol.size()); 
					indexRowOrColSymbolToAdd = ran.nextInt(symInColNotInRow.size());
					int rearrangements=rearrange(new Key(row, column), 0, matrix, startRowOrCol, row, 
							symInRowNotInCol.get(indexRowOrColSymbolToLookFor), 
							symInColNotInRow.get(indexRowOrColSymbolToAdd));
					
					rearrangementsArray.add(rearrangements); 
					
					System.out.println("Random symbols for rearrangement: "+symInRowNotInCol.get(indexRowOrColSymbolToLookFor)+", "
							+symInColNotInRow.get(indexRowOrColSymbolToAdd));
					System.out.println("After rearrangement:");  
					System.out.println(matrixToString()); 
					break;
					
				} else {
	
					indexRowOrColSymbolToLookFor = ran.nextInt(symInColNotInRow.size()); 
					indexRowOrColSymbolToAdd = ran.nextInt(symInRowNotInCol.size());
					
					int rearrangements=rearrange(new Key(row, column), 0, matrix, startRowOrCol, column, 
							symInColNotInRow.get(indexRowOrColSymbolToLookFor), 
							symInRowNotInCol.get(indexRowOrColSymbolToAdd));
					
					rearrangementsArray.add(rearrangements);
					
					System.out.println("Random symbols for rearrangement: "+symInColNotInRow.get(indexRowOrColSymbolToLookFor)+", "
							+symInRowNotInCol.get(indexRowOrColSymbolToAdd));
					System.out.println("After rearrangement:");  
					System.out.println(matrixToString()); 
					break;
				}


			case 2: // selects the combination that guarantees the minimum number of rearrangements
				Hashtable<Key, Cell> bestMatrix = null;
				int indexRowOrColSym1;
				int indexRowOrColSym2;
				int bestNumRearrangements = 9999999;
				int bestSym1=0; 
				int bestSym2=0; 
				int numRearrangementRow;
				int numRearrangementColumn;
				
				for(indexRowOrColSym1=0;indexRowOrColSym1<symInRowNotInCol.size();indexRowOrColSym1++) {
					
					for(indexRowOrColSym2=0;indexRowOrColSym2<symInColNotInRow.size();indexRowOrColSym2++) {
						
						duplicatedMatrix1 = this.duplicateMatrix();
						duplicatedMatrix2 = this.duplicateMatrix();

						numRearrangementRow = rearrange(new Key(row, column), 0, duplicatedMatrix1, 0, row,
								symInRowNotInCol.get(indexRowOrColSym1),
								symInColNotInRow.get(indexRowOrColSym2));
						numRearrangementColumn = rearrange(new Key(row, column), 0, duplicatedMatrix2, 1, column,
								symInColNotInRow.get(indexRowOrColSym2),
								symInRowNotInCol.get(indexRowOrColSym1));

						if (numRearrangementRow < bestNumRearrangements) {
							bestSym1=symInRowNotInCol.get(indexRowOrColSym1); 
							bestSym2=symInColNotInRow.get(indexRowOrColSym2);
							bestMatrix = duplicatedMatrix1;
							bestNumRearrangements=numRearrangementRow;
								
						}
						if (numRearrangementColumn < bestNumRearrangements) {
							bestSym1=symInRowNotInCol.get(indexRowOrColSym1); 
							bestSym2=symInColNotInRow.get(indexRowOrColSym2);
							bestMatrix = duplicatedMatrix2;
							bestNumRearrangements=numRearrangementColumn;
							
						}
						
					}
					
				}
				
				System.out.println("Best couple of symbols for rearrangement: "+bestSym1+", "+bestSym2);
				System.out.println("After rearrangement:");  
				
				matrix=bestMatrix;
				System.out.println(matrixToString()); 
				rearrangementsArray.add(bestNumRearrangements);
				
				
				break;

			case 3: // randomly selects symbols, selects the best between row
						// and column

				indexRowOrColSym1 = ran.nextInt(symInRowNotInCol.size());
				indexRowOrColSym2 = ran.nextInt(symInColNotInRow.size());			

				duplicatedMatrix1 = this.duplicateMatrix();
				duplicatedMatrix2 = this.duplicateMatrix();
				
				
				System.out.println("Symbols for rearrangement: "+symInRowNotInCol.get(indexRowOrColSym1)+
						", "+symInColNotInRow.get(indexRowOrColSym2));
				
				numRearrangementRow = rearrange(new Key(row, column), 0, duplicatedMatrix1, 0, row,
						symInRowNotInCol.get(indexRowOrColSym1),
						symInColNotInRow.get(indexRowOrColSym2));
				numRearrangementColumn = rearrange(new Key(row, column), 0, duplicatedMatrix2, 1, column,
						symInColNotInRow.get(indexRowOrColSym2),
						symInRowNotInCol.get(indexRowOrColSym1));

				if (numRearrangementRow <= numRearrangementColumn) {
					
					System.out.println("After rearrangement:");  
					this.matrix = duplicatedMatrix1;
					System.out.println(matrixToString()); 
					rearrangementsArray.add(numRearrangementRow);
				} else {
					System.out.println("After rearrangement:");  
					this.matrix = duplicatedMatrix2; 
					System.out.println(matrixToString());
					rearrangementsArray.add(numRearrangementColumn);

				}

				break;

			}

			return true;

		}

	}

	/**
	 * Method which performs the rearrangement operations according to the rules of the algorithm. 
	 * @param: - startRowOrCol indicates if the rearrangement starts from a row (case 0) or a column
	 * (case 1) - indexRowOrCol indicates the indexRowOrCol of the row/column from which the algorithm starts -
	 * - symbolToLookFor indicates the symbol to look for in the row/column -
	 * - symbolToAdd indicates the symbol which substitute the symbolToLookFor -
	 * - numRearrangements indicates the number of rearrangements performed so far -
	 * - matrix is the hashtable on which rearrangements algorithm is performed -
	 * - startKey is the cell where the symbolToLookFor is added and from which all the rearrangements 
	 * of the row/column depend. 
	 * 
	 * @return the number of rearrangements performed by the algorithm to establish the new connection. 
	 */

	private int rearrange(Key startKey, int rearrangements, Hashtable<Key, Cell> matrix, 
			int startRowOrCol, int indexRowOrCol, int symbolToLookFor, int symbolToAdd) {

		boolean replaced = false;
		int index = 1;

		int bound = 0;
		if (startRowOrCol == 0) { //starts from row

			bound = r3;

		} else { //starts from column

			bound = r1;
		}
		if (rearrangements == 0 ) { 
			
			Cell elem = matrix.get(startKey);  
			elem.insert(symbolToLookFor);
			}
		
		while (!replaced && index <= bound) {

			Key k;
			if (startRowOrCol == 0) {
				k = new Key(indexRowOrCol, index); 
			} else {
				k = new Key(index, indexRowOrCol);
			}

			if (k.hashCode() != startKey.hashCode()) {
				Cell cell = matrix.get(k);

				if (cell.contains(symbolToLookFor)) {
					cell.remove(symbolToLookFor);
					cell.insert(symbolToAdd);
					replaced = true;
					startKey = k;
				}
			}

			if (!replaced) {

				index++;

			}

		}

		if (!replaced) { //END of rearrangement
			return rearrangements; 
			
		} else {
			
			int newStartRowOrCol;
			int newIndexRowOrCol;
			int newSymbolToLook;
			int newSymbolToAdd;
			int cumulativeRearrangements;
			
			newStartRowOrCol = 1 - startRowOrCol;
			newIndexRowOrCol = index;
			newSymbolToLook = symbolToAdd;
			newSymbolToAdd = symbolToLookFor;
			cumulativeRearrangements = rearrangements + 1;

			return rearrange(startKey, cumulativeRearrangements, matrix, newStartRowOrCol, newIndexRowOrCol, 
					newSymbolToLook,newSymbolToAdd);

		}

	}
	
	private HashSet<Integer> findUnusuedSymbols(int rowOrCol, int indexRowOrCol){
		
		HashSet<Integer> unusuedSymbols = new HashSet<Integer>();
		
		
		if (rowOrCol==0) { //row
			
			for (int i = 1; i <= r2; i++) { 
				int occurences = 0;
				for (int j = 1; j <= r3; j++) {
					Key k = new Key(indexRowOrCol, j); 
					Cell el = matrix.get(k); 
					if (el.contains(i)) {
						occurences++; 
					}
				}
				
				if (occurences == 0) { //if row does NOT contain the symbol j, it's added to the unusuedSymbolsRow
					unusuedSymbols.add(i);
				}
			}
			return unusuedSymbols; 
			
		}
		else { //column
			for (int i = 1; i<= r2; i++) { 
				int occurences = 0;
				for (int j = 1; j <= r1; j++) {
					Key k = new Key(j, indexRowOrCol);
					Cell el = matrix.get(k);
					if (el.contains(i)) {
						occurences++; 
					}
				}

				if (occurences == 0) {
					unusuedSymbols.add(i);
				}
				
		}
			return unusuedSymbols; 
		}
	}

		
	public int getTotalRearrangements() {
		
		int total = 0;
		for(Integer i : rearrangementsArray) {
			total = total + i;
			
		}
		return total;
	}




	
	/**
	 * 
	 * @return A new matrix with the same elements of the caller matrix. 
	 * 
	 */
	public Hashtable<Key, Cell> duplicateMatrix() {

		Hashtable<Key, Cell> matrix = new Hashtable<Key, Cell>();

		for (int i = 1; i <= r1; i++) {

			for (int j = 1; j <= r3; j++) {

				Key id = new Key(i, j);
				Cell elem = this.matrix.get(id).clone();

				matrix.put(id, elem);

			}

		}

		return matrix;

	}
	
	
	/**
	 *
	 * Method to stamp the PaullMatrix 
	 * 
	 */
	public String matrixToString(){
		String[] line = new String[r1];

		int longest = 0;
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < r3; j++) {
				line[i] = new String("");
				String newLine = matrix.get(new Key(i + 1, j + 1)).toString();

				if (newLine.length() > longest) {
					longest = newLine.length(); 
				}
			}
		}

		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < r3; j++) {
				String newLine = matrix.get(new Key(i + 1, j + 1))
						.toString();
				String blank = new String("");
				for (int k = 0; k < (longest - newLine.length()); k++) {
					blank = blank + " ";
				}

				line[i] = line[i] + newLine + blank;

			}
			line[i] = line[i] + "\n";
		}

		String result = new String("");
		for (int i = 0; i < r1; i++) {

			result = result + line[i] + "\n";

		}

		return result;
		
	}
	
	

}
