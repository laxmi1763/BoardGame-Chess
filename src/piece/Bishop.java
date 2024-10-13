/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: This Class is inherited from the Piece class which handles the functioning of Bishop
 */
package piece; // Package containing chess piece classes

import main.GamePanel; // Importing GamePanel for game constants
import main.Type; // Importing Type for piece type enumeration

// Class representing the Bishop chess piece, which extends the Piece class
public class Bishop extends Piece {

    // Constructor for the Bishop class
    public Bishop(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);

        // Set the type of this piece to BISHOP
        type = Type.BISHOP;

        // Load the appropriate image based on the color of the bishop
        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-bishop"); // Load white bishop image
        } else {
            image = getImage("/piece/b-bishop"); // Load black bishop image
        }
    }

    // Method to check if the bishop can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits and not the same square
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
            // Check if the move is diagonal (same difference in columns and rows)
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                // Check if the target square is valid and not blocked by other pieces
                if (isValidSquare(targetCol, targetRow) && 
                        pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                    return true; // Move is valid for the bishop
                }
            }
        }
        return false; // Move is not valid
    }
}
