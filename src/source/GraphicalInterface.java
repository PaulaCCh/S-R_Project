package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GraphicalInterface {

	private static GraphicalInterface instance; 
	private JFrame frame=new JFrame ("Slepian-Duguid Simulator "); 
	private JLabel r1Label=new JLabel ("Number of matrices in the first stage"); 
	private JLabel r2Label=new JLabel ("Number of matrices in the second stage"); 
	private JLabel r3Label=new JLabel ("Number of matrices in the third stage");  
	private JLabel nLabel=new JLabel ("Number of inlet of each matrix"); 
	private JLabel mLabel=new JLabel ("Number of outlet of each matrix"); 
	private JLabel policyLabel=new JLabel ("Select policy");
	private JLabel iterationsLabel=new JLabel("Insert the number of simulation");
	private JLabel bg=new JLabel (new ImageIcon("images/background.jpg"));
	private JLabel title=new JLabel("SLEPIAN-DUGUID"); 
	private JLabel subTitle=new JLabel("SIMULATOR");
	private JTextField r1Field=new JTextField(5); 
	private JTextField r2Field=new JTextField (5); 
	private JTextField r3Field=new JTextField (5); 
	private JTextField nField=new JTextField (5); 
	private JTextField mField=new JTextField (5); 
	private JTextField iterationsField=new JTextField(5); 
	private JTextArea resultArea=new JTextArea(); 
	private JScrollPane scrollPane=new JScrollPane (resultArea);
	@SuppressWarnings("rawtypes")
	private JComboBox policyBox=new JComboBox<String>(); 
	private JOptionPane messagePopUp=new JOptionPane();
	private JButton startButton=new JButton (new ImageIcon("images/runButton.png"));


	public static GraphicalInterface getInstance() {
		if (instance==null)
			instance=new GraphicalInterface();

		return instance; 
	}

	@SuppressWarnings("unchecked")
	public GraphicalInterface(){

		frame.setResizable(false);
		frame.setBounds(100,100,780,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		bg.setBounds(0, 0, 780, 780);

		title.setBounds(470, 20, 300, 30);
		title.setFont(new Font("HELVETICA", Font.ITALIC+Font.BOLD, 25));
		title.setForeground(Color.WHITE);

		subTitle.setBounds(500,50,150,30);
		subTitle.setFont(new Font("HELVETICA", Font.ITALIC+Font.BOLD, 25));
		subTitle.setForeground(Color.WHITE);

		r1Label.setBounds(10,50,275,14);
		r1Label.setForeground(Color.WHITE);
		r1Label.setFont(new Font("HELVETICA", Font.BOLD, 15));
		r1Field.setBounds( 330, 47, 42, 20);



		r2Label.setBounds(10,100,285,14);
		r2Field.setBounds(330, 97, 42, 20);
		r2Label.setForeground(Color.WHITE);
		r2Label.setFont(new Font("HELVETICA", Font.BOLD, 15));


		r3Label.setBounds(10,150,275,14);
		r3Field.setBounds(330, 147, 42, 20);
		r3Label.setForeground(Color.WHITE);
		r3Label.setFont(new Font("HELVETICA", Font.BOLD, 15));


		nLabel.setBounds(10,200 ,275,14);
		nField.setBounds(330, 197, 42, 20); 
		nLabel.setForeground(Color.WHITE);
		nLabel.setFont(new Font("HELVETICA", Font.BOLD, 15));


		mLabel.setBounds(10,250,275,14);
		mField.setBounds(330, 247, 42, 20);
		mLabel.setForeground(Color.WHITE);
		mLabel.setFont(new Font("HELVETICA", Font.BOLD, 15));


		iterationsLabel.setBounds(10, 300, 275, 14);
		iterationsField.setBounds(330, 297, 42, 20);
		iterationsLabel.setForeground(Color.WHITE);
		iterationsLabel.setFont(new Font("HELVETICA", Font.BOLD, 15));

		policyLabel.setBounds(10,350,255,14);
		policyLabel.setForeground(Color.WHITE);
		policyLabel.setFont(new Font("HELVETICA", Font.BOLD, 15));

		policyBox.addItem("Random");
		policyBox.addItem("Best combination");
		policyBox.addItem("Default"); 
		policyBox.setBackground(Color.WHITE);

		policyBox.setBounds(310, 330, 190, 60); 

		startButton.setBounds(475, 150, 200, 100);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);

		scrollPane.setBounds(10, 385, 760, 180);

		frame.getContentPane().add(title);
		frame.getContentPane().add(subTitle);
		frame.getContentPane().add(policyBox);
		frame.getContentPane().add(scrollPane);
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
		frame.add(messagePopUp);
		frame.getContentPane().add(startButton);	
		frame.getContentPane().add(bg);	

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


	public void setResultMessage(String stringMessage){
		resultArea.append(stringMessage);
		resultArea.setFont(new Font("HELVETICA", Font.ITALIC, 15));

	}
}
