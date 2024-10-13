/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: This class helps to draw the chessboard
 */

package main;

import java.awt.Color;
import java.awt.Graphics2D;

// Class representing the game board
public class Board {
    // Constants for maximum number of columns and rows on the board
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    
    // Size of each square on the board in pixels
    public static int SQUARE_SIZE = 100; // 1 square is 100x100 pixels big
    public static int HALF_SQUARE_SIZE = SQUARE_SIZE / 2; // Half the size of a square

    // Method to draw the game board
    public void draw(Graphics2D g2) {
        int c = 0; // Variable to toggle colors for the squares
        
        // Loop through each row
        for (int row = 0; row < MAX_ROW; row++) {
            // Loop through each column
            for (int col = 0; col < MAX_COL; col++) {
                
                // Set color based on the current state of c
                if (c == 0) {
                    g2.setColor(new Color(224, 214, 255)); // Light purple color
                    c = 1; // Toggle c to switch color for the next square
                } else {
                    g2.setColor(new Color(170, 152, 169)); // Grayish color
                    c = 0; // Toggle c to switch color for the next square
                }
                
                // Fill the square at the current column and row
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            
            // After finishing a row, toggle c for the starting color of the next row
            if (c == 0) {
                c = 1; // Switch to the second color for the next row
            } else {
                c = 0; // Switch to the first color for the next row
            }
        }
    }
}

