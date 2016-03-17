package source;

public class StartSimulation {
private static NewGraphicalInterface gi; 
private static Controller controller; 

public static void main(String[] args) {
	gi=NewGraphicalInterface.getInstance(); 
	controller=Controller.getInstance(); 
	controller.setController(gi);
}



}
