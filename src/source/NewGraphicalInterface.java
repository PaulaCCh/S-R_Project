package source;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NewGraphicalInterface {
	
	private static NewGraphicalInterface instance; 
	private JFrame frame=new JFrame ("Slepian-Duguid Simulator "); 
	private JLabel r1Label=new JLabel ("Number of matrices in the first stage"); 
	private JLabel r2Label=new JLabel ("Number of matrices in the second stage"); 
	private JLabel r3Label=new JLabel ("Number of matrices in the third stage");  
	private JLabel nLabel=new JLabel ("Number of inlet of each matrix"); 
	private JLabel mLabel=new JLabel ("Number of outlet of each matrix"); 
	private JLabel policyLabel=new JLabel ("Select policy");
	private JLabel iterationsLabel=new JLabel("Insert the number of simulation");
	private JTextField r1Field=new JTextField(5); 
	private JTextField r2Field=new JTextField (5); 
	private JTextField r3Field=new JTextField (5); 
	private JTextField nField=new JTextField (5); 
	private JTextField mField=new JTextField (5); 
	private JTextField iterationsField=new JTextField(5); 
	private JTextArea resultArea=new JTextArea(); 
	@SuppressWarnings("rawtypes")
	private JComboBox policyBox=new JComboBox<String>(); 
	private JOptionPane messagePopUp=new JOptionPane();
	private JButton startButton=new JButton ("Start Simulation");
	
	public static NewGraphicalInterface getInstance() {
		if (instance==null)
			instance=new NewGraphicalInterface();

		return instance; 
	}
	
	@SuppressWarnings("unchecked")
	public NewGraphicalInterface(){
	
		frame.setResizable(false);
		frame.setBounds(100,100,780,727);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		//TODO BACKGROUND
		
		r1Label.setBounds(10,50,255,14);
		r1Field.setBounds( 310, 47, 42, 20);
		
		
		r2Label.setBounds(10,100,255,14);
		r2Field.setBounds(310, 97, 42, 20);
		
		
		r3Label.setBounds(10,150,255,14);
		r3Field.setBounds(310, 147, 42, 20);
		
		
		nLabel.setBounds(10,200 ,255,14);
		nField.setBounds(310, 197, 42, 20); 
		
		
		mLabel.setBounds(10,250,255,14);
		mField.setBounds(310, 247, 42, 20);
		
		
		iterationsLabel.setBounds(10, 300, 255, 14);
		iterationsField.setBounds(310, 297, 42, 20);
		
		
		policyLabel.setBounds(10,350,255,14);
		
		policyBox.addItem("Random");
		policyBox.addItem("Best combination");
		policyBox.addItem("Default"); 
		policyBox.setBackground(Color.WHITE);
		
		policyBox.setBounds(310, 330, 190, 60); 
		

		startButton.setBounds(500, 150, 150, 50);
		
		resultArea.setBounds(10, 425, 760, 270);
		
		
		frame.getContentPane().add(policyBox);
		frame.getContentPane().add(resultArea);
		frame.getContentPane().add(r1Label);
		frame.getContentPane().add(r1Field);
		frame.getContentPane().add(r2Label);
		frame.getContentPane().add(r2Field);
		frame.getContentPane().add(r3Label);
		frame.getContentPane().add(r3Field); 
		frame.getContentPane().add(nLabel);
		frame.getContentPane().add(nField);
		frame.getContentPane().add(mLabel); 
		frame.getContentPane().add(mField);
		frame.getContentPane().add(iterationsLabel);
		frame.getContentPane().add(iterationsField); 
		frame.getContentPane().add(policyLabel);
		frame.getContentPane().add(startButton);
		frame.add(messagePopUp);
		
		frame.setVisible(true);
		
}
	public String getR1(){
		return (r1Field.getText()); 
	}
	
	public String getR2(){
		return (r2Field.getText()); 
	}
	
	public String getR3(){
		return (r3Field.getText()); 
	}
	
	public String getN(){
		return (nField.getText());
	}
	
	public String getM(){
		return (mField.getText()); 
	}
	
	public String getPolicy(){
		return (String)policyBox.getSelectedItem();
	}
	
	public String getIterations(){
		return (iterationsField.getText()); 
	}
	public void addStartButtonListener (ActionListener listenerForStartButton){
		startButton.addActionListener(listenerForStartButton);
	}
	
	public void displayMessage(String message){
		JOptionPane.showMessageDialog(frame, message);	
	}
	
	public void setInvisible(){
		frame.setVisible(false);
	}
	public void setVisible(){
		frame.setVisible(true);
	}
	
	public void setResultMessage(String stringMessage){
		resultArea.setText(stringMessage);
	}
}
