package slepianDuguidSim;

public class StartSimulation {
private static GraphicalInterface gi; 
private static Controller controller; 

public static void main(String[] args) {
	gi=GraphicalInterface.getInstance(); 
	controller=Controller.getInstance(); 
	controller.setController(gi);
}



}
