/**
 * 
 * The PaullMatrix class contains all the logic of the program, it includes two main methods to 
 * rearrange the matrix and add a new connection in the network.
 *
 * @author De Silva, Pébrier, Caballero 
 *
 */

package source;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

public class PaullMatrix {

	private Hashtable<Key, Cell> paullMatrix = new Hashtable<Key, Cell>();

	private int r1;
	private int r2;
	private int r3;

	private int maxSymbolsRow;
	private int maxSymbolsColumn;

	private ArrayList<Integer> numRearrangements;

	private int policy;

	/**
	 * Constructor of the class PaullMatrix
	 * 
	 * @param Slepian-Duguid network parameters
	 */
	
	public PaullMatrix(Data param) {

		for (int i = 1; i <= param.getR1(); i++) {

			for (int j = 1; j <= param.getR3(); j++) {

				Key id = new Key(i, j);
				Cell elem = new Cell();

				paullMatrix.put(id, elem);

			}

		}

		this.r1 = param.getR1();
		this.r2 = param.getR2();
		this.r3 = param.getR3();

		this.maxSymbolsRow = Math.min(param.getNumInletsFirstStage(), param.getR2());
		this.maxSymbolsColumn = Math.min(param.getR2(), param.getNumOutletsThirdsStage());

		numRearrangements = new ArrayList<Integer>();

		this.policy = param.getPolicy();

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
	public boolean addNewConnection(int row, int column) {

		// getting all the non used symbols in the row and column of the Paull
		// Matrix

		// row
		HashSet<Integer> freeSymbolsRow = new HashSet<Integer>(); //find free symbols in row

		for (int j = 1; j <= maxSymbolsRow; j++) { //cicla su tutti i simboli distinti che possono apparire in una riga

			int result = 0;

			for (int i = 1; i <= r3; i++) {

				Key k = new Key(row, i); //key made by row of new connection and column i

				Cell el = paullMatrix.get(k); //extracts the element(s) contained in this cell (row,i)

				if (el.contains(j)) {

					result++; 

				}

			}

			if (result == 0) { //se la riga "row" non contiene il simbolo j, esso viene aggiunto al set di simboli free
				freeSymbolsRow.add(j);
			}
		}

		// columns
		HashSet<Integer> freeSymbolsColumn = new HashSet<Integer>(); //find free symbols in column
		for (int j = 1; j <= maxSymbolsColumn; j++) {

			int result = 0;

			for (int i = 1; i <= r1; i++) {

				Key k = new Key(i, column);

				Cell el = paullMatrix.get(k);

				if (el.contains(j)) {

					result++; //result=occurrences

				}

			}

			if (result == 0) {
				freeSymbolsColumn.add(j);
			}
		}

		// Option 1, there is a symbol in the row and in the column that is not
		// used
		// in both column and row

		HashSet<Integer> commonSymbols = new HashSet<Integer>(freeSymbolsRow);
		commonSymbols.retainAll(freeSymbolsColumn);

		if (!commonSymbols.isEmpty()) { 

			Key k = new Key(row, column);
			int symbol = 0;
			for (Integer i : commonSymbols) { //sceglie il primo simbolo di commonSymbols e lo assegna a symbol 
				symbol = i;
				break;
			}
			Cell elem = paullMatrix.get(k);
			elem.insert(symbol);
			return true;
		}

		// Option 2 there are no free symbols, the matrix needs to be
		// re-arranged

		else {
			// finding a symbol in column that is not found in row
			ArrayList<Integer> columnSymbol = new ArrayList<Integer>();

			for (Integer i : freeSymbolsRow) { //bdeg

				int flag = 1;
				for (Integer j : freeSymbolsColumn) { //a

					if (i == j) {
						flag = 0;

					}
				}
				if (flag == 1) { // se i è diverso da TUTTI i simboli j non presenti nella colonna, aggiungi a columnsymbol
								// columnSymbol contiene bdeg
					columnSymbol.add(i);

				}
			}

			// finding a symbol in row that is not found in column
			ArrayList<Integer> rowSymbol = new ArrayList<Integer>();

			for (Integer i : freeSymbolsColumn) { //freesymbolcolumn: a
				int flag = 1;
				for (Integer j : freeSymbolsRow) { //bdeg

					if (i == j) {
						flag = 0;

					}
				}
				if (flag == 1) { // se i è diverso da TUTTI i simboli j non presenti nella riga, aggiungi a rowsymbol
					rowSymbol.add(i); // nell'esempio, a diverso da b,d,e,g; rowsimbol contiene a
										//rowsymbol: simboli presenti nella riga e non presenti nella colonna
										//columnsymbol: simboli presenti nella colonna e non presenti nella riga
				}
			}

			if (rowSymbol.isEmpty() || columnSymbol.isEmpty()) {
				return false;
			}

			Random ran = new Random(); 
			int indexRowOrColumn;
			int indexSymbolToLookFor;
			int indexSymbolToSubstitute;
			int rowOrColumn;
			int indexSelectedSymbolRow;
			int indexSelectedSymbolColumn;

			int numRearrangementRow;
			int numRearrangementColumn;

			Hashtable<Key, Cell> rowMatrix;
			Hashtable<Key, Cell> columnMatrix;
			
			Hashtable<Key, Cell> best = null;
			
			switch (policy) {

			case 1: // policy 1

				// randomly selects row or column and randomly selects symbols
				// no pre-computation at all

				rowOrColumn = ran.nextInt(2);

				ArrayList<Integer> pointerArrayToLookFor;
				ArrayList<Integer> pointerArrayToSubstitute;

				if (rowOrColumn == 0) {
					indexRowOrColumn = row; //indice matrice ingresso connessione
					indexSymbolToLookFor = ran.nextInt(rowSymbol.size()); //rowSymbol: simboli trovati in row ma non in column, indice nell'arraylist del simbolo da sostituire nella riga row ad es 0(a)
					indexSymbolToSubstitute = ran.nextInt(columnSymbol.size()); //columnSymbol: simbolo trovato in column ma non in row, indice nell'arraylist del simbolo da sostituire nella colonna column, ad es 0(b)
					pointerArrayToLookFor = rowSymbol; // a
					pointerArrayToSubstitute = columnSymbol; //bdeg
				} else {
					indexRowOrColumn = column; //indice matrice uscita connessione
					indexSymbolToLookFor = ran.nextInt(columnSymbol.size()); //ad es 0 (b)
					indexSymbolToSubstitute = ran.nextInt(rowSymbol.size()); //ad es 0 (a)
					pointerArrayToLookFor = columnSymbol; //bdeg
					pointerArrayToSubstitute = rowSymbol; //a
				}

				numRearrangements.add(reArrange(rowOrColumn, indexRowOrColumn, //numRearr: arraylist
						pointerArrayToLookFor.get(indexSymbolToLookFor), //simbolo da cercare nella riga/colonna
						pointerArrayToSubstitute.get(indexSymbolToSubstitute), //simbolo che sostituisce il simbolToLookFor 
						0, paullMatrix, new Key(row, column)));
				break;

			case 2: // selects the combination that guarantees the minimum number of re-arrangements
				
				int bestNumRearrangements = 1000000;
				
				for(indexSelectedSymbolRow=0;indexSelectedSymbolRow<rowSymbol.size();indexSelectedSymbolRow++) {
					
					for(indexSelectedSymbolColumn=0;indexSelectedSymbolColumn<columnSymbol.size();indexSelectedSymbolColumn++) {
						
						rowMatrix = this.duplicateHashTable();
						columnMatrix = this.duplicateHashTable();

						numRearrangementRow = reArrange(0, row,
								rowSymbol.get(indexSelectedSymbolRow),
								columnSymbol.get(indexSelectedSymbolColumn), 0,
								rowMatrix, new Key(row, column));
						numRearrangementColumn = reArrange(1, column,
								columnSymbol.get(indexSelectedSymbolColumn),
								rowSymbol.get(indexSelectedSymbolRow), 0, columnMatrix,
								new Key(row, column));

						if (numRearrangementRow < bestNumRearrangements) {

							best = rowMatrix;
							bestNumRearrangements=numRearrangementRow;
								
						}
						if (numRearrangementColumn < bestNumRearrangements) {

							best = columnMatrix;
							bestNumRearrangements=numRearrangementColumn;
							
						}
						
					}
					
				}

				paullMatrix=best;
				numRearrangements.add(bestNumRearrangements);
				
				
				break;

			default: // randomly selects symbols, selects the best between row
						// and column

				indexSelectedSymbolRow = ran.nextInt(rowSymbol.size());
				indexSelectedSymbolColumn = ran.nextInt(columnSymbol.size());


				rowMatrix = this.duplicateHashTable();
				columnMatrix = this
						.duplicateHashTable();

				numRearrangementRow = reArrange(0, row,
						rowSymbol.get(indexSelectedSymbolRow),
						columnSymbol.get(indexSelectedSymbolColumn), 0,
						rowMatrix, new Key(row, column));
				numRearrangementColumn = reArrange(1, column,
						columnSymbol.get(indexSelectedSymbolColumn),
						rowSymbol.get(indexSelectedSymbolRow), 0, columnMatrix,
						new Key(row, column));

				if (numRearrangementRow <= numRearrangementColumn) {

					paullMatrix = rowMatrix;
					numRearrangements.add(numRearrangementRow);
				} else {

					paullMatrix = columnMatrix;
					numRearrangements.add(numRearrangementColumn);

				}

				break;

			}

			return true;

		}

	}

	/**
	 * Method which performs the rearrangement operations according to the rules of the algorithm. 
	 * @param: - rowOrColumn indicates if the rearrangement starts from a row (case 0) or a column
	 * (case 1) - index indicates the index of the row/column from which the alghoritm starts -
	 * - symbolToLookFor indicates the symbol to look for in the row/column -
	 * - symbolToAdd indicates the symbol which substitute the symbolToLookFor -
	 * - numRearrangements indicates the number of rearrangements performed so far -
	 * - paullMatrix is the hashtable on which rearrangements algorithm is performed -
	 * - keyToAvoid indicates a cell in the PaullMatrix on which the lookup procedure must
	 * 	 not be executed. 
	 * 
	 * @return the number of rearrangemets performed by the alghoritmo to establisg the new connection. 
	 */

	public int reArrange(int rowOrColumn, int index, int symbolToLookFor,
			int symbolToAdd, int numRearrangements,
			Hashtable<Key, Cell> paullMatrix, Key keyToAvoid) {

		int newRowOrColumn;
		int newIndex;
		int newSymbolToLook;
		int newSymbolToAdd;
		int newNumRearrangements;

		// going through the row

		boolean substituted = false;
		int i = 1;

		int bound = 0;
		if (rowOrColumn == 0) { //partiamo dalla riga

			bound = r3;

		} else {

			bound = r1;
		}

		while (!substituted && i <= bound) {

			Key k;
			if (rowOrColumn == 0) {
				k = new Key(index, i); //index è parametro passato: è l'indice di riga/colonna da cui parte il riarrangiamento
			} else {
				k = new Key(i, index);
			}

			if (numRearrangements == 0 && k.hashCode() == keyToAvoid.hashCode()) { // keyToAvoid:chiave ottenuta da (row, column) di partenza (nuova connessione)

				Cell elem = paullMatrix.get(k);
				elem.insert(symbolToLookFor);
			}

			if (k.hashCode() != keyToAvoid.hashCode()) {
				Cell elem = paullMatrix.get(k);

				if (elem.contains(symbolToLookFor)) {

					elem.remove(symbolToLookFor);
					elem.insert(symbolToAdd);
					substituted = true;
					keyToAvoid = k;
				}
			}

			if (!substituted) {

				i++;

			}

		}

		if (!substituted) {
			return numRearrangements;
		} else {

			newRowOrColumn = 1 - rowOrColumn;
			newIndex = i;
			newSymbolToLook = symbolToAdd;
			newSymbolToAdd = symbolToLookFor;
			newNumRearrangements = numRearrangements + 1;

			return reArrange(newRowOrColumn, newIndex, newSymbolToLook,
					newSymbolToAdd, newNumRearrangements, paullMatrix, keyToAvoid);

		}

	}

	/**
	 * Performs an average of the number of rearrangements required during the
	 * whole process
	 * 
	 * @return average number of rearrangements per new connection required
	 */
	public double getAverageNumRearrangements() { //TODO forse non serve

		int count = 0;
		
		int tot = 0;
		for (Integer i : numRearrangements) {

			tot = tot + i;
			count++;

		}

		return tot / count * 1.0;
	}
	
	public int getTotalRearrangements() {
		
		int total = 0;
		for(Integer i : numRearrangements) {
			total = total + i;
			
		}
		return total;
	}

	/**
	 * Returns a string representation of the Paull Matrix for representation
	 * purposes
	 * 
	 * @return a string containing the graphical representation of the Paull
	 *         Matrix
	 * 
	 */
	public String toString() {

		String[] line = new String[r1];

		int longest = 0;
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < r3; j++) {
				line[i] = new String("");
				String newLine = paullMatrix.get(new Key(i + 1, j + 1)).toString();

				if (newLine.length() > longest) {
					longest = newLine.length(); //trova la lunghezza dell'elemento più lungo nella paullMatrix
				}
			}
		}

		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < r3; j++) {
				String newLine = paullMatrix.get(new Key(i + 1, j + 1))
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

	
	/**
	 * It duplicates the Hashtable of the current element by creating a new hash
	 * table that is disjoint from the current.
	 * 
	 * @return a Hashtable, exact copy element by element of the current Hashtable
	 * 
	 */
	public Hashtable<Key, Cell> duplicateHashTable() {

		Hashtable<Key, Cell> paullMatrix = new Hashtable<Key, Cell>();

		for (int i = 1; i <= r1; i++) {

			for (int j = 1; j <= r3; j++) {

				Key id = new Key(i, j);
				Cell elem = this.paullMatrix.get(id).clone();

				paullMatrix.put(id, elem);

			}

		}

		return paullMatrix;

	}

}
