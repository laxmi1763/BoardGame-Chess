/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Inherited from Piece class and handles the movements of the Knight
 */
package piece;

import main.GamePanel; // Importing GamePanel for game constants and pieces
import main.Type; // Importing Type for piece type enumeration

// Class representing the Knight chess piece, which extends the Piece class
public class Knight extends Piece {

    // Constructor for the Knight class
    public Knight(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);
        
        // Set the type of this piece to KNIGHT
        type = Type.KNIGHT;

        // Load the appropriate image based on the color of the knight
        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-knight"); // Load white knight image
        } else {
            image = getImage("/piece/b-knight"); // Load black knight image
        }
    }

    // Method to check if the knight can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits
        if (isWithinBoard(targetCol, targetRow)) {
            // Knight can move if its movement ratio of column and row is 1:2 or 2:1
            // This is calculated using the product of the absolute differences
            if (Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2) {
                // Check if the target square is valid (not occupied by friendly piece)
                if (isValidSquare(targetCol, targetRow)) {
                    return true; // Move is valid for the knight
                }
            }
        }
        return false; // Move is not valid for the knight
    }
}
