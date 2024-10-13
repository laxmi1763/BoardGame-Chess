/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Inherited from Piece class and handles the movements of the Rook
 */
package piece;

import main.GamePanel; // Importing GamePanel for game constants and pieces
import main.Type; // Importing Type for piece type enumeration

// Class representing the Rook chess piece, which extends the Piece class
public class Rook extends Piece {

    // Constructor for the Rook class
    public Rook(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);
        
        // Set the type of this piece to ROOK
        type = Type.ROOK;

        // Load the appropriate image based on the color of the rook
        if (color == GamePanel.WHITE) { // If the rook is white
            image = getImage("/piece/w-rook"); // Load white rook image
        } else { // If the rook is black
            image = getImage("/piece/b-rook"); // Load black rook image
        }
    }

    // Method to check if the rook can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits and not the same as the current square
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Rook can move if either the column or row matches its current position
            if (targetCol == preCol || targetRow == preRow) {
                // Ensure the target square is valid and there are no pieces blocking the path
                if (isValidSquare(targetCol, targetRow) && 
                        !pieceIsOnStraightLine(targetCol, targetRow)) {
                    return true; // Move is valid
                }
            }
        }
        return false; // Move is not valid for the rook
    }
}
