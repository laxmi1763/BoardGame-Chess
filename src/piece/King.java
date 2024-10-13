/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Inherited from Piece class and handles the movements of the King
 */
package piece;

import main.GamePanel; // Importing GamePanel for game constants and pieces
import main.Type; // Importing Type for piece type enumeration

// Class representing the King chess piece, which extends the Piece class
public class King extends Piece {

    // Constructor for the King class
    public King(int color, int col, int row) {
        // Call the superclass constructor to initialize color, column, and row
        super(color, col, row);

        // Set the type of this piece to KING
        type = Type.KING;

        // Load the appropriate image based on the color of the king
        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-king"); // Load white king image
        } else {
            image = getImage("/piece/b-king"); // Load black king image
        }
    }

    // Method to check if the king can move to the target square
    public boolean canMove(int targetCol, int targetRow) {
        // Check if the target square is within board limits
        if (isWithinBoard(targetCol, targetRow)) {
            // Check for normal movement (one square in any direction)
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1 ||
                    Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1) {
                // Check if the target square is valid (not occupied by friendly piece)
                if (isValidSquare(targetCol, targetRow)) {
                    return true; // Move is valid for the king
                }
            }

            // Check for castling if the king has not moved yet
            if (moved == false) {
                // Right Castling (King moves two squares to the right)
                if (targetCol == preCol + 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    for (Piece piece : GamePanel.simpieces) {
                        // Check if the rook on the right is available for castling (not moved)
                        if (piece.col == preCol + 3 && piece.row == preRow && piece.moved == false) {
                            GamePanel.castlingP = piece; // Set the rook for castling
                            return true; // Castling is valid
                        }
                    }
                }

                // Left Castling (King moves two squares to the left)
                if (targetCol == preCol - 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    Piece[] p = new Piece[2]; // Array to hold potential rooks for castling
                    for (Piece piece : GamePanel.simpieces) {
                        // Check for the left rook
                        if (piece.col == preCol - 3 && piece.row == targetRow) {
                            p[0] = piece; // Left rook found
                        }
                        // Check for the leftmost rook
                        if (piece.col == preCol - 4 && piece.row == targetRow) {
                            p[1] = piece; // Leftmost rook found
                        }
                        // If the left rook is not found but the leftmost rook is valid and not moved
                        if (p[0] == null && p[1] != null && p[1].moved == false) {
                            GamePanel.castlingP = p[1]; // Set the leftmost rook for castling
                            return true; // Castling is valid
                        }
                    }
                }
            }
        }
        return false; // Move is not valid for the king
    }
}

