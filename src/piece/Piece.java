/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: The `Piece` class serves as a base for all chess pieces, encapsulating common properties like position, color, and type. 
It provides methods for movement validation, path checking, and rendering on the game board.
 */
package piece;

import java.awt.Graphics; // Import for Graphics class
import java.awt.Graphics2D; // Import for Graphics2D class
import java.awt.image.BufferedImage; // Import for handling buffered images
import java.io.IOException; // Import for handling IO exceptions

import javax.imageio.ImageIO; // Import for reading images

import main.Board; // Importing the Board class for game constants
import main.GamePanel; // Importing GamePanel for game state management
import main.Type; // Importing Type for piece type enumeration

// Class representing a chess piece, serving as a superclass for all specific piece types (e.g., Pawn, Rook, Knight, etc.)
public class Piece {

    public Type type; // The type of the piece (e.g., Pawn, Rook)
    public BufferedImage image; // The image representing the piece
    public int x, y; // Current position of the piece in pixels
    public int col, row, preCol, preRow; // Current and previous column/row positions
    public int color; // Color of the piece (e.g., white or black)
    public Piece hittingP; // A piece that may be hit (captured)
    public boolean moved, twoStepped; // Flags for tracking movement state

    // Constructor to initialize the piece's color, column, and row
    public Piece(int color, int col, int row) {
        this.color = color; // Set the piece's color
        this.col = col; // Set the initial column
        this.row = row; // Set the initial row
        x = getX(col); // Convert column to pixel position
        y = getY(row); // Convert row to pixel position
        preCol = col; // Initialize previous column
        preRow = row; // Initialize previous row
    }

    // Method to load an image from a given file path
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null; // Initialize the image
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png")); // Load the image
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for any IO exceptions
        }
        return image; // Return the loaded image
    }

    // Convert a column index to pixel x-coordinate
    public int getX(int col) {
        return col * Board.SQUARE_SIZE; // Return x-coordinate based on column
    }

    // Convert a row index to pixel y-coordinate
    public int getY(int row) {
        return row * Board.SQUARE_SIZE; // Return y-coordinate based on row
    }

    // Convert a pixel x-coordinate to a column index
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE; // Return column index based on pixel x
    }

    // Convert a pixel y-coordinate to a row index
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE; // Return row index based on pixel y
    }

    // Get the index of the piece in the list of pieces
    public int getIndex() {
        for (int index = 0; index < GamePanel.simpieces.size(); index++) {
            if (GamePanel.simpieces.get(index) == this) { // Check if this piece matches the one in the list
                return index; // Return the index if found
            }
        }
        return 0; // Default return value (if not found)
    }

    // Update the piece's position after a move
    public void updatePosition() {
        // Check for En Passant condition for Pawns
        if (type == Type.PAWN) {
            if (Math.abs(row - preRow) == 2) {
                twoStepped = true; // Mark that the pawn has moved two steps
            }
        }

        // Update the pixel position based on the new column and row
        x = getX(col); 
        y = getY(row);
        // Update previous column and row to current values
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true; // Mark that the piece has moved
    }

    // Reset the piece's position to its previous state
    public void resetPosition() {
        col = preCol; // Reset to previous column
        row = preRow; // Reset to previous row
        x = getX(col); // Update pixel position
        y = getY(row); // Update pixel position
    }

    // Placeholder method to check if the piece can move to a specific square
    public boolean canMove(int targetCol, int targetRow) {
        return false; // Default implementation returns false (override in subclasses)
    }

    // Check if the target square is within the board limits
    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7; // Return true if within bounds
    }

    // Check if the target square is the same as the current position
    public boolean isSameSquare(int targetCol, int targetRow) {
        return targetCol == preCol && targetRow == preRow; // Return true if the same square
    }

    // Get the piece located at the target square (if any)
    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simpieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece; // Return the piece at the target square if found
            }
        }
        return null; // Return null if no piece is found
    }

    // Check if the target square is valid for moving (vacant or an opponent's piece)
    public boolean isValidSquare(int targetCol, int targetRow) {
        hittingP = getHittingP(targetCol, targetRow); // Get any piece at the target square
        
        if (hittingP == null) { // If the square is vacant
            return true; // Valid move
        } else { // If the square is occupied
            if (hittingP.color != this.color) { // Check if the piece is of a different color
                return true; // Valid move (can capture)
            } else {
                hittingP = null; // Invalid move (same color piece)
            }
        }
        return false; // Default return value (not valid)
    }

    // Check if any piece is blocking the path in a straight line
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {
        // Check for movement to the left
        if (targetCol < preCol && targetRow == preRow) {
            for (int c = preCol - 1; c > targetCol; c--) {
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == targetRow) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }
        }

        // Check for movement to the right
        if (targetCol > preCol && targetRow == preRow) {
            for (int c = preCol + 1; c < targetCol; c++) {
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == targetRow) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }
        }

        // Check for upward movement
        if (targetRow < preRow && targetCol == preCol) {
            for (int r = preRow - 1; r > targetRow; r--) {
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == targetCol && piece.row == r) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }
        }

        // Check for downward movement
        if (targetRow > preRow && targetCol == preCol) {
            for (int r = preRow + 1; r < targetRow; r++) {
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == targetCol && piece.row == r) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }
        }

        // No pieces are blocking the path
        return false; 
    }

    // Check if any piece is blocking the path diagonally
    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {
        if (targetRow < preRow) { // Moving up
            // Check for upward left movement
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol); // Calculate difference in column
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }

            // Check for upward right movement
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol); // Calculate difference in column
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }

        }

        if (targetRow > preRow) { // Moving down
            // Check for downward left movement
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol); // Calculate difference in column
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == preRow + diff) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }

            // Check for downward right movement
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol); // Calculate difference in column
                for (Piece piece : GamePanel.simpieces) {
                    if (piece.col == c && piece.row == preRow + diff) {
                        hittingP = piece; // A piece is blocking the path
                        return true; // Return true (path is blocked)
                    }
                }
            }

        }
        return false; // No pieces are blocking the path
    }

    // Method to draw the piece on the board
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null); // Draw the piece image
    }
}
