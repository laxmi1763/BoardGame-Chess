/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Inherited from Piece class and handles the movements of the Queen
 */
package piece;

import main.GamePanel; // Importing GamePanel for game constants and pieces
import main.Type; // Importing Type for piece type enumeration

// Class representing the Queen chess piece, which extends the Piece class
public class Queen extends Piece {

    // Constructor for the Queen class
    public Queen(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);
        
        // Set the type of this piece to QUEEN
        type = Type.QUEEN;

        // Load the appropriate image based on the color of the queen
        if (color == GamePanel.WHITE) { // If the queen is white
            image = getImage("/piece/w-queen"); // Load white queen image
        } else { // If the queen is black
            image = getImage("/piece/b-queen"); // Load black queen image
        }
    }

    // Method to check if the queen can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits and not the same as the current square
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Vertical and Horizontal movement
            if (targetCol == preCol || targetRow == preRow) {
                // Ensure the target square is valid and there are no pieces blocking the path
                if (isValidSquare(targetCol, targetRow) && 
                        !pieceIsOnStraightLine(targetCol, targetRow)) {
                    return true; // Move is valid for vertical/horizontal movement
                }
            }
            
            // Diagonal movement
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                // Check if the target square is valid and that there are no pieces blocking the diagonal path
                if (isValidSquare(targetCol, targetRow) && 
                        pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
                    return true; // Move is valid for diagonal movement
                }
            }
        }
        return false; // Move is not valid for the queen
    }
}
