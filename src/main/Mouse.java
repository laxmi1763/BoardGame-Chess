/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Class that handles the functioning of mouse in the game where to call the method when the mouse is clicked or hovered, etc.
 */
package main;

import java.awt.event.MouseAdapter; // Importing MouseAdapter to handle mouse events
import java.awt.event.MouseEvent; // Importing MouseEvent to use mouse event properties

// Class that extends MouseAdapter to handle mouse events
public class Mouse extends MouseAdapter {
    public int x, y; // Variables to store the current mouse coordinates
    public boolean pressed; // Flag to indicate if the mouse button is pressed

    // Override the mousePressed method to handle mouse press events
    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true; // Set the pressed flag to true when the mouse is pressed
    }

    // Override the mouseReleased method to handle mouse release events
    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false; // Set the pressed flag to false when the mouse is released
    }

    // Override the mouseDragged method to handle mouse drag events
    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX(); // Update the x coordinate of the mouse
        y = e.getY(); // Update the y coordinate of the mouse
    }

    // Override the mouseMoved method to handle mouse movement events
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX(); // Update the x coordinate of the mouse
        y = e.getY(); // Update the y coordinate of the mouse
    }
}
