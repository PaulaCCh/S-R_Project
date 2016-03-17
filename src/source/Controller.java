package source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class Controller {

	private NewGraphicalInterface graphicalInt; 
	private SimulationData simData; 
	private static Controller instance; 
	private PrintWriter writer;
	private PrintWriter averagesWriter;
	//File file = new File("files/output.dat");
	ArrayList<Integer> rearrangements;
	ArrayList<Double> simulationTimes; 

	public static Controller getInstance() {
		if (instance==null)
			instance=new Controller();

		return instance; 
	}

	public void setController(NewGraphicalInterface graphicalInt) {
		this.graphicalInt=graphicalInt; 
		this.graphicalInt.addStartButtonListener(new StartListener());
	}

	class StartListener implements ActionListener {

		@Override
		public void actionPerformed (ActionEvent e){
			int policy=0; 
			String s1=graphicalInt.getR1(); 
			String s2=graphicalInt.getR2(); 
			String s3=graphicalInt.getR3();
			String stringM=graphicalInt.getM(); 
			String stringN=graphicalInt.getN();
			String stringIterations=graphicalInt.getIterations(); 
			int r1=0; 
			int r2=0; 
			int r3=0; 
			int n=0; 
			int m=0; 
			int iterations=0; 
			rearrangements=new ArrayList<Integer>();
			simulationTimes=new ArrayList<Double>(); 



			if (graphicalInt.getPolicy().equalsIgnoreCase("Random")) {
				policy = 1;
			}
			else if (graphicalInt.getPolicy().equalsIgnoreCase("Best combination")){
				policy=2; 
			}
			else if (graphicalInt.getPolicy().equalsIgnoreCase("Default")){
				policy=3; 
			}



			if ( (isValidFormat(s1)) && (isValidFormat(s2)) && (isValidFormat(s3)) && (isValidFormat(stringN)) && (isValidFormat(stringM)) && (isValidFormat(stringIterations))) {
				//se non vuote e numeriche 

				r1=Integer.parseInt(s1); 
				r2=Integer.parseInt(s2); 
				r3=Integer.parseInt(s3); 
				n=Integer.parseInt(stringN); 
				m=Integer.parseInt(stringM); 
				iterations=Integer.parseInt(stringIterations); 
				simData=new SimulationData (r1,r2,r3,n,m,n*r1,m*r3,policy); 


				if (!simData.isInputDataCorrect()){
					graphicalInt.displayMessage("INPUT DATA NOT CORRECT, PLEASE RETRY!");
				}
				else if (simData.isInputDataCorrect() ) {


					try {
						writer=new PrintWriter (new File("/Users/ernestodesilva/Desktop/output.txt"));
					} catch (FileNotFoundException e2) {	
						e2.printStackTrace();
					}

					try {
						averagesWriter= new PrintWriter (new File ("/Users/ernestodesilva/Desktop/avgValues.txt"));
					} catch (FileNotFoundException e1) {
						
						e1.printStackTrace();
					}

					rearrangements = new ArrayList<Integer>();
					simulationTimes = new ArrayList<Double>();
					double startTime; 
					double finishTime; 

					int counterSim=1; 
					while (counterSim<=iterations){
						writer.print("SIMULATION " + counterSim +"\n");
						counterSim++;
						startTime=System.currentTimeMillis(); 
						PaullMatrix paull=new PaullMatrix(simData); 
						writer.println(paull.toString());

						ArrayList<Integer> totInletArray=new ArrayList<Integer>(); 
						ArrayList<Integer> totOutletArray=new ArrayList<Integer>(); 
						ArrayList<Integer> inletAssociatedMatrix=new ArrayList<Integer>(); 
						ArrayList<Integer> outletAssociatedMatrix=new ArrayList<Integer>();
						for(int j=1;j<=n*r1;j++) {		
							totInletArray.add(j);
							inletAssociatedMatrix.add((int) j/n +1);
						}
						inletAssociatedMatrix.add(0, 1);
						inletAssociatedMatrix.remove(inletAssociatedMatrix.size()-1); 

						for(int j=1;j<=m*r3;j++) {

							totOutletArray.add(j);
							outletAssociatedMatrix.add((int) j/m+1);
						}
						outletAssociatedMatrix.add(0, 1);
						outletAssociatedMatrix.remove(outletAssociatedMatrix.size()-1);

						while (totInletArray.size()!=0 && totOutletArray.size()!=0) { //starts 1 of the iterations
							Random ran=new Random(); 

							int randomIndexForN=ran.nextInt(totInletArray.size());
							int randomInlet=totInletArray.get(randomIndexForN); 
							totInletArray.remove(randomIndexForN); 

							int row=inletAssociatedMatrix.get(randomIndexForN); 
							inletAssociatedMatrix.remove(randomIndexForN); 

							int randomIndexForM=ran.nextInt(totOutletArray.size());
							int randomOutlet=totOutletArray.get(randomIndexForM); 
							totOutletArray.remove(randomIndexForM); 

							int column=outletAssociatedMatrix.get(randomIndexForM); 
							outletAssociatedMatrix.remove(randomIndexForM); 
							//System.out.println("Estabilishing a new connection between inlet "+randomInlet+" of matrix "+row+" and outlet "+randomOutlet+" of matrix "+column);
							writer.println("Estabilishing a new connection between inlet "+randomInlet+" of matrix "+row+" and outlet "+randomOutlet+" of matrix "+column);
							paull.addNewConnection(row, column); 
							writer.println(paull.toString());
							//System.out.println(paull.toString());

						}
						rearrangements.add(paull.getTotalRearrangements()); //aggiunge il numero TOTALE di riarrangiamenti della singola simulazione nell'array
						finishTime=System.currentTimeMillis(); 
						double executionTime=finishTime-startTime; 
						simulationTimes.add(executionTime); 

					}
					double averageRearrangements=getAverageNumRearrangements();//calcola la media di riarrangiamenti su tutte le simulazioni
					double averageExecutionTime=getAverageExecutionTime();
					String result=("NUMBER OF SIMULATIONS "+iterations+". Policy of rearrangement: "+graphicalInt.getPolicy()+"\nAverage number of rearrangements: "+averageRearrangements+". Average execution time "+averageExecutionTime+"[ms]");
					//averagesWriter.println("NUMBER OF SIMULATIONS "+iterations+". Policy of rearrangement: "+graphicalInt.getPolicy());
					//averagesWriter.println("Average number of rearrangements: "+averageRearrangements+". Average execution time "+averageExecutionTime+"[ms]");
					graphicalInt.setResultMessage(result);

					writer.close();
					averagesWriter.close();
				}
			}
			else 
				graphicalInt.displayMessage("INPUT DATA NOT CORRECT, PLEASE RETRY!");
		}

		public double getAverageNumRearrangements() {

			int count = 0;

			double tot = 0;

			for (Integer i : rearrangements) {

				tot = tot + i;
				count++;

			}

			return tot / count * 1.0;
		}

		public double getAverageExecutionTime() {

			int count = 0;

			double tot = 0;

			for (Double i : simulationTimes) {

				tot = tot + i;
				count++;

			}

			return tot / count * 1.0;
		}

		private boolean isValidFormat(String string){
			if (string.isEmpty())
				return false; 
			for (int i=0; i<string.length(); i++) {
				char c = string.charAt(i);
				if (Character.isAlphabetic(c))
					return false; 
			}
			return true; 
		}

	}
}
