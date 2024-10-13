/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Main Class where drawing of the window is written and where objects are called so the game starts.
 */
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
