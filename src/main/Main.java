package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame window = new JFrame("Chess");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //shuts down thw program when 'close' is executed
		window.setResizable(false); //user cannot resize the window by dragging the window
		
		//add GamePanel to the window
		GamePanel gp = new GamePanel(); //instantiate the GamePanel as gp
		window.add(gp); //and add the GamePanel to the window
		window.pack(); //by packing like this the window adjusts to the size of the GamePanel
		
		window.setLocationRelativeTo(null); //the window will be positioned to the center 
		window.setVisible(true); //to make  sure that the window is visible to the us	
		
		gp.launchGame();

	}

}
