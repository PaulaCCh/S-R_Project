/**
 * 
 * The class PaullMatrix is the main part of the program and it contains all
 * the logic behind the PaullMatrix, it includes the algorithm for adding 
 * a new connection, quit a connection, rearrange the matrix.
 *
 * @author Maretti, Elnor, Huamani
 *
 */

package source;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

public class PaullMatrix {

	private Hashtable<Key, Element> paullMatrix = new Hashtable<Key, Element>();

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
	 * @param object
	 *            of the class Simulation Data containing the specifics for this
	 *            simulation (number of matrices for each stage)
	 */
	public PaullMatrix(SimulationData param) {

		for (int i = 1; i <= param.getNumStage1Matrices(); i++) {

			for (int j = 1; j <= param.getNumStage3Matrices(); j++) {

				Key id = new Key(i, j);
				Element elem = new Element();

				paullMatrix.put(id, elem);

			}

		}

		this.r1 = param.getNumStage1Matrices();
		this.r2 = param.getNumStage2Matrices();
		this.r3 = param.getNumStage3Matrices();

		this.maxSymbolsRow = Math.min(param.getNumInletsFirstStage(),
				param.getNumStage2Matrices());
		this.maxSymbolsColumn = Math.min(param.getNumStage2Matrices(),
				param.getNumOutletsThirdsStage());

		numRearrangements = new ArrayList<Integer>();

		this.policy = param.getPolicy();

	}

	/**
	 * Adds a new connection from the first stage matrix row to the third stage
	 * matrix column. If free second stage matrix are available then a new
	 * matrix is assigned to the new connection. If no new free matrices are
	 * available a rearrangement is done
	 * 
	 * @param the
	 *            row and the column index where the new connection has to be
	 *            deployed.
	 * @return true if the connections has been routed correctly, false
	 *         otherwise
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

				Element el = paullMatrix.get(k); //extracts the element(s) contained in this cell (row,i)

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

				Element el = paullMatrix.get(k);

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

		HashSet<Integer> intersection = new HashSet<Integer>(freeSymbolsRow);
		intersection.retainAll(freeSymbolsColumn);

		if (!intersection.isEmpty()) { 

			Key k = new Key(row, column);
			int symbol = 0;
			for (Integer i : intersection) { //sceglie il primo simbolo di intersection e lo assegna a symbol 
				symbol = i;
				break;
			}
			Element elem = paullMatrix.get(k);
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

			Hashtable<Key, Element> rowMatrix;
			Hashtable<Key, Element> columnMatrix;
			
			Hashtable<Key, Element> best = null;
			
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
	 * The procedure rearrange a PaullMatrix according to the rules of
	 * rearrangement.
	 * 
	 * @param - rowOrColum has value 0 if the symbol to be rearranged is
	 *        searched in the row, 1 in the column - index is the index of the
	 *        row or column in which the symbol has to be searched -
	 *        symbolToLookFor is the symbol that has to be searched in order to
	 *        be exchanged - symbolToAdd is the symbol that replaces the
	 *        symbolToLookFor - numRearrangements is the number of
	 *        rearrangements that has been done so far in the rearrangement
	 *        procedure - paullMatrix is the object of the rearrangement (the
	 *        object of the class is not used directly in order to allow
	 *        precomputations of the best rearrangement method - avoid is a key
	 *        parameter that has to be avoided in the look up procedure
	 * @return the number of rearrangements needed to arrange the new connection
	 * 
	 */

	public int reArrange(int rowOrColumn, int index, int symbolToLookFor,
			int symbolToAdd, int numRearrangements,
			Hashtable<Key, Element> paullMatrix, Key avoid) {

		int nextRowOrColumn;
		int nextIndex;
		int nextSymbolToLookFor;
		int nextSymbolToAdd;
		int nextNumRearrangements;

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

			if (numRearrangements == 0 && k.hashCode() == avoid.hashCode()) { // avoid:chiave ottenuta da (row, column) di partenza (nuova connessione)

				Element elem = paullMatrix.get(k);
				elem.insert(symbolToLookFor);
			}

			if (k.hashCode() != avoid.hashCode()) {
				Element elem = paullMatrix.get(k);

				if (elem.contains(symbolToLookFor)) {

					elem.remove(symbolToLookFor);
					elem.insert(symbolToAdd);
					substituted = true;
					avoid = k;
				}
			}

			if (!substituted) {

				i++;

			}

		}

		if (!substituted) {
			return numRearrangements;
		} else {

			nextRowOrColumn = 1 - rowOrColumn;
			nextIndex = i;
			nextSymbolToLookFor = symbolToAdd;
			nextSymbolToAdd = symbolToLookFor;
			nextNumRearrangements = numRearrangements + 1;

			return reArrange(nextRowOrColumn, nextIndex, nextSymbolToLookFor,
					nextSymbolToAdd, nextNumRearrangements, paullMatrix, avoid);

		}

	}

	/**
	 * Performs an average of the number of rearrangements required during the
	 * whole process
	 * 
	 * @return average number of rearrangements per new connection required
	 */
	public double getAverageNumRearrangements() {

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
				String newLine = paullMatrix.get(new Key(i + 1, j + 1))
						.toString();

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
	public Hashtable<Key, Element> duplicateHashTable() {

		Hashtable<Key, Element> paullMatrix = new Hashtable<Key, Element>();

		for (int i = 1; i <= r1; i++) {

			for (int j = 1; j <= r3; j++) {

				Key id = new Key(i, j);
				Element elem = this.paullMatrix.get(id).clone();

				paullMatrix.put(id, elem);

			}

		}

		return paullMatrix;

	}

	/**
	 * ONLY FOR TEST PURPOSES
	 * 
	 */
	public void setConnection(int row, int column, int symbol) {

		Key k = new Key(row, column);
		Element elem = paullMatrix.get(k);
		elem.insert(symbol);

	}

}