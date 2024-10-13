/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Inherited from Piece class and handles the movements of the Pawn
 */
package piece;

import main.GamePanel; // Importing GamePanel for game constants and pieces
import main.Type; // Importing Type for piece type enumeration

// Class representing the Pawn chess piece, which extends the Piece class
public class Pawn extends Piece {

    // Constructor for the Pawn class
    public Pawn(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);
        
        // Set the type of this piece to PAWN
        type = Type.PAWN;

        // Load the appropriate image based on the color of the pawn
        if (color == GamePanel.WHITE) { // If the pawn is white
            image = getImage("/piece/w-pawn"); // Load white pawn image
        } else { // If the pawn is black
            image = getImage("/piece/b-pawn"); // Load black pawn image
        }
    }

    // Method to check if the pawn can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits
        if (isWithinBoard(targetCol, targetRow)) {
            // Define the move value based on the pawn's color
            int moveValue;
            if (color == GamePanel.WHITE) { // White pawns move up
                moveValue = -1;
            } else { // Black pawns move down
                moveValue = 1;
            }
            
            // Checking for a piece being hit
            hittingP = getHittingP(targetCol, targetRow);
            
            // 1 square movement
            // Check if the pawn moves one square forward to an empty square
            if (targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
                return true; // Move is valid
            }
            
            // 2 square move
            // Check if the pawn can move two squares forward from its initial position
            if (targetCol == preCol && targetRow == preRow + moveValue * 2 && hittingP == null &&
                    moved == false && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                return true; // Move is valid
            }
            
            // Diagonal Movement & capture
            // Check if the pawn can capture an opponent's piece diagonally
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null &&
                    hittingP.color != color) {
                return true; // Capture move is valid
            }
            
            // En Passant
            // Check if the pawn can perform an "en passant" capture
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue) {
                for (Piece piece : GamePanel.simpieces) {
                    // Check if the pawn can capture an opponent's pawn that has just moved two squares
                    if (piece.col == targetCol && piece.row == preRow && piece.twoStepped == true) {
                        hittingP = piece; // Mark the piece being captured
                        return true; // En passant move is valid
                    }
                }
            }
        }
        return false; // Move is not valid for the pawn
    }
}

