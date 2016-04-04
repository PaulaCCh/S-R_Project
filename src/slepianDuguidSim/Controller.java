/**
 * CONTROLLER OF THE MVC DESIGN PATTERN
 * This class accepts input data from the view, convert them to commands for the model 
 * and consequently update the view displaying results.
 * 
 * @author De Silva, Pébrier, Caballero 
 *
 */package slepianDuguidSim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class Controller {

	private GraphicalInterface graphicalInt; 
	private Data simData; 
	private static Controller instance; 
	private PrintWriter writer;
	ArrayList<Integer> rearrangements;
	ArrayList<Double> simulationTimes; 

	public static Controller getInstance() {
		if (instance==null)
			instance=new Controller();

		return instance; 
	}

	public void setController(GraphicalInterface graphicalInt) {
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
			String stringM=graphicalInt.getNumOutlet(); 
			String stringN=graphicalInt.getNumInlet();
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
				//if not empty and numeric

				r1=Integer.parseInt(s1); 
				r2=Integer.parseInt(s2); 
				r3=Integer.parseInt(s3); 
				n=Integer.parseInt(stringN); 
				m=Integer.parseInt(stringM); 
				iterations=Integer.parseInt(stringIterations); 
				simData=new Data (r1,r2,r3,n,m,n*r1,m*r3,policy); 


				if (!simData.isInputDataCorrect()){
					graphicalInt.displayMessage("INPUT DATA NOT CORRECT, PLEASE RETRY!");
				}
				else if (simData.isInputDataCorrect() ) {


					try {
						writer=new PrintWriter (new File("/Users/ernestodesilva/Desktop/output.txt"));
					} catch (FileNotFoundException e2) {	
						e2.printStackTrace();
					}

					rearrangements = new ArrayList<Integer>();
					simulationTimes = new ArrayList<Double>();
					double startTime; 
					double finishTime; 

					int counterSim=1; 
					while (counterSim<=iterations){
						writer.print("SIMULATION " + counterSim +"\n");
						System.out.println("------------------------New simulation------------------------");
						counterSim++;
						startTime=System.currentTimeMillis(); 
						Matrix paull=new Matrix(simData); 
						writer.println(paull.matrixToString());

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

						while (totInletArray.size()!=0 && totOutletArray.size()!=0) { //start of simulation
							Random ran=new Random(); 

							int randomIndexForN=ran.nextInt(totInletArray.size());
							int randomInlet=totInletArray.get(randomIndexForN); 
							totInletArray.remove(randomIndexForN); 

							int rowMatrix=inletAssociatedMatrix.get(randomIndexForN); 
							inletAssociatedMatrix.remove(randomIndexForN); 

							int randomIndexForM=ran.nextInt(totOutletArray.size());
							int randomOutlet=totOutletArray.get(randomIndexForM); 
							totOutletArray.remove(randomIndexForM); 

							int columnMatrix=outletAssociatedMatrix.get(randomIndexForM); 
							outletAssociatedMatrix.remove(randomIndexForM); 
							writer.println("Establishing a new connection between inlet "+randomInlet+" of matrix "+rowMatrix+" and outlet "+randomOutlet+" of matrix "+columnMatrix);
							paull.establishConnection(rowMatrix, columnMatrix); 
							writer.println(paull.matrixToString());


						}
						rearrangements.add(paull.getTotalRearrangements()); //add the number of rearrangemets of the simulation to the array
						finishTime=System.currentTimeMillis(); 
						double executionTime=finishTime-startTime; 
						simulationTimes.add(executionTime); 

					}
					double averageRearrangements=getAverageNumRearrangements(); //AVG of all rearrangements of all simulations
					double averageExecutionTime=getAverageExecutionTime();
					int N=Integer.parseInt(graphicalInt.getNumInlet())*Integer.parseInt(graphicalInt.getR1()); 
					int M=Integer.parseInt(graphicalInt.getNumOutlet())*Integer.parseInt(graphicalInt.getR3()); 
					String result=(N+"X"+M+" FC NETWORK. NUMBER OF SIMULATIONS "+iterations+". Policy of rearrangement: "+graphicalInt.getPolicy()+
							"\nAverage number of rearrangements: "+averageRearrangements
							+"\nAverage execution time "+averageExecutionTime+" [ms]"
							+ "\nPercentage of rearranged conection over all the connection established: " + Math.round(getAverageNumRearrangements()/Math.min(n*r1, r3*m)*100*100)/100.0+"%\n\n");
					graphicalInt.setResultMessage(result);

					writer.close();
				}
			}
			else 
				graphicalInt.displayMessage("INPUT DATA NOT CORRECT, PLEASE RETRY!");
		}

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

}
