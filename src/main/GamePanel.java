/* Author Name: Laxmi Chari
Roll No: 22
Title: Program to implement Board Game called Chess using Java
Start Date: 24/08/2024
Modified Date: 17/09/2024
Description: Handles the main Functioning of the game, most of the main controls are written here.
 */
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;

public class GamePanel extends JPanel implements Runnable{
	
	//setting the game screen
	public static final int WIDTH = 1100;
	public static final int HEIGHT = 800;
	final int FPS = 60; 
	Thread gameThread; //thread is used to run the gameloop and implementing runnable is imp
	Board board = new Board();
	Mouse mouse = new Mouse();
	
	//pieces
	public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> simpieces = new ArrayList<>();
	ArrayList<Piece> promoPieces = new ArrayList<>();
	Piece activeP, checkingP;//Active piece
	public static Piece castlingP;; 
	
	//color
	public static final int WHITE =0;
	public static final int BLACK =1;
	int currentColor = WHITE; //because the game starts from white pieces
	
	//booleans
	boolean canMove;
	boolean validSquare;
	boolean promotion;
	boolean gameover;
	boolean stalemate;
	
	//constructor
	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		
		setPieces();
		copyPieces(pieces,simpieces);
		
	}
	
	public void launchGame() { //to instantiate the thread
		gameThread = new Thread(this);
		gameThread.start(); //start() method calls the run() method. Starting the thread means calling the run method
	}
	
	public void setPieces() {
		//white team
		pieces.add(new Pawn(WHITE,0,6));
		pieces.add(new Pawn(WHITE,1,6));
		pieces.add(new Pawn(WHITE,2,6));
		pieces.add(new Pawn(WHITE,3,6));
		pieces.add(new Pawn(WHITE,4,6));
		pieces.add(new Pawn(WHITE,5,6));
		pieces.add(new Pawn(WHITE,6,6));
		pieces.add(new Pawn(WHITE,7,6));
		pieces.add(new Rook(WHITE,0,7));
		pieces.add(new Rook(WHITE,7,7));
		pieces.add(new Knight(WHITE,1,7));
		pieces.add(new Knight(WHITE,6,7));
		pieces.add(new Bishop(WHITE,2,7));
		pieces.add(new Bishop(WHITE,5,7));
		pieces.add(new Queen(WHITE,3,7));
		pieces.add(new King(WHITE,4,7));
		
		
		//Black team
		pieces.add(new Pawn(BLACK,0,1));
		pieces.add(new Pawn(BLACK,1,1));
		pieces.add(new Pawn(BLACK,2,1));
		pieces.add(new Pawn(BLACK,3,1));
		pieces.add(new Pawn(BLACK,4,1));
		pieces.add(new Pawn(BLACK,5,1));
		pieces.add(new Pawn(BLACK,6,1));
		pieces.add(new Pawn(BLACK,7,1));
		pieces.add(new Rook(BLACK,0,0));
		pieces.add(new Rook(BLACK,7,0));
		pieces.add(new Knight(BLACK,1,0));
		pieces.add(new Knight(BLACK,6,0));
		pieces.add(new Bishop(BLACK,2,0));
		pieces.add(new Bishop(BLACK,5,0));
		pieces.add(new Queen(BLACK,3,0));
		pieces.add(new King(BLACK,4,0));
	}
	
	private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
		target.clear(); //clear the target list
		for(int i =0; i<source.size();i++) {
			//adding everything in the source list to the target list
			target.add(source.get(i));
		}
	}
	
	@Override
	public void run() {
		//Game Loop
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
		
	}
	
	//update() method will handle all the updating that is needed eg:Positions of the pieces 
	private void update() {
		
		if(promotion) {
			promoting();
		}
		else if(gameover == false && stalemate == false){
			//Mouse button pressed
			if (mouse.pressed) { 
				if (activeP == null){
					//player is not holding the piece right now 
					//if the activeP is null, check if you can pick up a piece
					for (Piece piece : simpieces) {
						//If the mouse is on an ally piece, pick it up the activeP
						if(piece.color == currentColor &&
								piece.col == mouse.x/Board.SQUARE_SIZE &&
								piece.row == mouse.y/Board.SQUARE_SIZE) {
							activeP = piece;
						}
					}
				}
				else {
					//If the player is holding a piece, simulate the move
					simulate();
				}
			}
			//Mouse Button Released
			if (mouse.pressed == false) {
				if(activeP != null) {
					if (validSquare) {
						
						//Move Confirmed
						//update the piece list in case a piece has beem captured and removed during the simulation
						copyPieces(simpieces, pieces);
						activeP.updatePosition();
						if(castlingP != null) {
							castlingP.updatePosition();
						}
						
						if(isKingInCheck() && isCheckmate()) {
							gameover = true;
						}
						else if(isStalemate() && isKingInCheck() == false) {
							stalemate = true;
						}
						else { // the game is still going on
							if(canPromote()) {
								promotion = true;
							}
							else {
								changePlayer();
							}
						}
						
					}
					else {
						//the move is not valid so reset everything
						copyPieces(pieces, simpieces);
						activeP.resetPosition();
						activeP = null; // the player has released this piece
					}	
				}
			}
		}
		
		
			
	}
	
	private void simulate() {	    
		canMove = false;
		validSquare = false;
		
		//reset the piece list in every loop
		//this is basically for restoring the removed pieces during the simulation
		copyPieces(pieces, simpieces);
		
		//reset the castling piece's position
		if(castlingP != null) {
			castlingP.col = castlingP.preCol;
			castlingP.x = castlingP.getX(castlingP.col);
			castlingP = null;
		}
		
		
	    // If the piece is being held, update its position
	    activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
	    activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
	    activeP.col = activeP.getCol(activeP.x);
	    activeP.row = activeP.getRow(activeP.y);
	    
	    //check if the piece is hovering over a reachable square
	    if(activeP.canMove(activeP.col, activeP.row)) {
	    	canMove = true;
	    	
	    	//If hitting a piece, reove it from the list
	    	if(activeP.hittingP != null) {
	    		simpieces.remove(activeP.hittingP.getIndex());
	    	}
	    	
	    	checkCastling();
	    	
	    	if(isIllegal(activeP) == false && opponentCanCaptureKing() == false) {
	    		validSquare = true;
	    	}
	    	
	    }
	    
	}
	
	private boolean isIllegal(Piece king) {
		
		if(king.type == Type.KING) {
			for(Piece piece : simpieces) {
				if(piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean opponentCanCaptureKing() {
		Piece king = getKing(false);
		for(Piece piece : simpieces) {
			if(piece.color != king.color && piece.canMove(king.col, king.row)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isKingInCheck() {
		Piece king = getKing(true);
		if(activeP.canMove(king.col, king.row)) {
			checkingP = activeP;
			return true;
		}
		else {
			checkingP = null;
		}
		
		return false;
	}
	private Piece getKing(boolean opponent) {
		Piece king  = null;
		for(Piece piece : simpieces) {
			if(opponent) {
				if(piece.type == Type.KING && piece.color != currentColor) {
					king = piece;
				}
			}
			else {
				if(piece.type == Type.KING && piece.color == currentColor) {
					king = piece;
				}
			}
		}
		return king;
	}
	
	private boolean isCheckmate() {
		Piece king = getKing(true);
		
		if(kingCanMove(king)) {
			return false;
		}
		else {
			//But you still have a chance!
			//Check if you can block the attack with your piece
			
			//Check the position of the checking piece and the king in check 
			int colDiff = Math.abs(checkingP.col - king.col);
			int rowDiff = Math.abs(checkingP.row - king.row);
			
			if(colDiff == 0) {
				//The Checking piece is attacking vertically
				if(checkingP.row < king.row) {
					//The checking piece is above the king
					for(int row = checkingP.row; row < king.row; row++) {
						for(Piece piece : simpieces) {
							if(piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
								return false;
							}
						}
					}
				}
				if(checkingP.row > king.row) {
					//The checking piece is below the king
					for(int row = checkingP.row; row > king.row; row--) {
						for(Piece piece : simpieces) {
							if(piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
								return false;
							}
						}
					}
				}
			}
			else if(rowDiff == 0) {
				//the Checking piece is attacking horizontally
				if(checkingP.col < king.col) {
					//the checking piece is to the left
					for(int col = checkingP.col; col < king.col; col++) {
						for(Piece piece : simpieces) {
							if(piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
								return false;
							}
						}
					}
				}
				if(checkingP.col > king.col) {
					//the checking piece is to the right
					for(int col = checkingP.col; col > king.col; col--) {
						for(Piece piece : simpieces) {
							if(piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
								return false;
							}
						}
					}
				}
			}
			else if(colDiff == rowDiff) {
				//The checking piece is attacking diagonally
				if(checkingP.row < king.row) {
					//the checking piece is above the king
					if(checkingP.col < king.col) {
						//the checking piece is in the upper left
						for(int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
							for(Piece piece : simpieces) {
								if(piece != king && piece.color != currentColor && piece.canMove(col, row)) {
									return false;
								}
							}
						}
					}
					if(checkingP.col > king.col) {
						//the checkin piece is in the upper right
						for(int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
							for(Piece piece : simpieces) {
								if(piece != king && piece.color != currentColor && piece.canMove(col, row)) {
									return false;
								}
							}
						}
					}
				}
				if(checkingP.row > king.row) {
					//the checking piece is below the king 
					if(checkingP.col < king.col) {
						//the checking piece is in the lower left
						for(int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
							for(Piece piece : simpieces) {
								if(piece != king && piece.color != currentColor && piece.canMove(col, row)) {
									return false;
								}
							}
						}
					}
					if(checkingP.col > king.col) {
						//the checkin piece is in the lower right
						for(int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
							for(Piece piece : simpieces) {
								if(piece != king && piece.color != currentColor && piece.canMove(col, row)) {
									return false;
								}
							}
						}
					}
				}
			}
			
		}
		return true;
	}
	private boolean kingCanMove(Piece king) {
		if(isValidMove(king, -1, -1)) {return true;}
		if(isValidMove(king, 0, -1)) {return true;}
		if(isValidMove(king, 1, -1)) {return true;}
		if(isValidMove(king, -1, 0)) {return true;}
		if(isValidMove(king, 1, 0)) {return true;}
		if(isValidMove(king, -1, 1)) {return true;}
		if(isValidMove(king, 0, 1)) {return true;}
		if(isValidMove(king, 1, 1)) {return true;}
		
		return false;
	}
	private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
		boolean isValidMove = false;
		//update  the king's position for a second
		king.col += colPlus;
		king.row += rowPlus;
		
		if(king.canMove(king.col, king.row)) {
			if(king.hittingP != null) {
				simpieces.remove(king.hittingP.getIndex());
			}
			if(isIllegal(king) == false) {
				isValidMove = true;
			}
		}
		//reset the king's position and restore the removed piece
		king.resetPosition();
		copyPieces(pieces, simpieces);
		
		return isValidMove;
	}
	
	private boolean isStalemate() {
		int count = 0;
		//count the number of pieces
		for(Piece piece : simpieces) {
			if(piece.color != currentColor) {
				count++;
			}
		}
		
		//if only 1 piece(king) is left
		if(count == 1) {
			if(kingCanMove(getKing(true)) == false) {
				return true;
			}
		}
		
		return false;
	}
	
	public void checkCastling() {
		if (castlingP != null) {
			if(castlingP.col == 0) {
				castlingP.col += 3;
			}
			else if(castlingP.col == 7) {
				castlingP.col -= 2;
			}
			castlingP.x = castlingP.getX(castlingP.col);
		}
	}
	
	public void changePlayer() {
		if(currentColor == WHITE) {
			currentColor = BLACK;
			//reset black's two stepped status
			for(Piece piece : pieces) {
				if(piece.color == BLACK) {
					piece.twoStepped = false;
				}
			}
		}
		else {
			currentColor = WHITE;
			//reset white's two stepped status
			for(Piece piece : pieces) {
				if(piece.color == WHITE) {
					piece.twoStepped = false;
				}
			}
		}
		activeP = null;
	}
	
	private boolean canPromote() {
		//promotion of the pawn
		if(activeP.type == Type.PAWN) {
			if(currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7) {
				promoPieces.clear();
				promoPieces.add(new Rook(currentColor,9,2));
				promoPieces.add(new Knight(currentColor,9,3));
				promoPieces.add(new Bishop(currentColor,9,4));
				promoPieces.add(new Queen(currentColor,9,5));
				return true;
			}
		}
		
		return false;
	}
	
	private void promoting() {
		if(mouse.pressed) {
			for(Piece piece : promoPieces) {
				if(piece.col == mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE) {
					switch(piece.type) {
					case ROOK:simpieces.add(new Rook(currentColor, activeP.col, activeP.row)); break;
					case KNIGHT:simpieces.add(new Knight(currentColor, activeP.col, activeP.row)); break;
					case BISHOP:simpieces.add(new Bishop(currentColor, activeP.col, activeP.row)); break;
					case QUEEN:simpieces.add(new Queen(currentColor, activeP.col, activeP.row)); break;
					default: break;
					}
					simpieces.remove(activeP.getIndex());
					copyPieces(simpieces, pieces);
					activeP = null;
					promotion = false;
					changePlayer();
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) { //drawing chess-board, pieces, on-screen messages
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//board
		board.draw(g2);
		
		//pieces
		for (Piece p:simpieces) {
			p.draw(g2);
		}
		
		if (activeP != null) {
			if(canMove) {
				if(isIllegal(activeP) || opponentCanCaptureKing()) {
					g2.setColor(Color.RED); //change tile colour when it is selected
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f)); //opacity
					g2.fillRect(activeP.col*Board.SQUARE_SIZE, activeP.row*Board.SQUARE_SIZE, 
							Board.SQUARE_SIZE, Board.SQUARE_SIZE);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
				}
				else {
					g2.setColor(Color.DARK_GRAY); //change tile colour when it is selected
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f)); //opacity
					g2.fillRect(activeP.col*Board.SQUARE_SIZE, activeP.row*Board.SQUARE_SIZE, 
							Board.SQUARE_SIZE, Board.SQUARE_SIZE);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
				}	
			}			
			
			//draw the active piece in the end so it won't be hidden by the board or the square
			activeP.draw(g2);
		}
		
		//Status message
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(new Font("Book Antiqua", Font.PLAIN, 40));;
		g2.setColor(Color.WHITE);
		
		if(promotion) {
			g2.drawString("Promote to:", 840, 150);
			for(Piece piece : promoPieces) {
				g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row),
						Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
			}
		}
		else {
			if(currentColor == WHITE) {
				g2.drawString("White's Turn", 840, 550);
				if(checkingP != null && checkingP.color == BLACK) {
					g2.setColor(Color.red);
					g2.drawString("The King",840,650);
					g2.drawString("is in Check!", 840, 700);
				}
			}
			else {
				g2.drawString("Black's Turn", 840, 250);
				if(checkingP != null && checkingP.color == WHITE) {
					g2.setColor(Color.red);
					g2.drawString("The King",840,100);
					g2.drawString("is in Check!", 840, 150);
				}
			}
		}
		if(gameover) {
			String s = "";
			if(currentColor == WHITE) {
				s = "White Wins!!";
			}
			else {
				s = "Black Wins!!";
			}
			g2.setFont(new Font("Arial", Font.PLAIN, 90));
			g2.setColor(Color.green);
			g2.drawString(s, 200, 420);
		}
		if(stalemate) {
			g2.setFont(new Font("Arial", Font.PLAIN, 90));
			g2.setColor(Color.DARK_GRAY);
			g2.drawString("Stalemate", 200, 420);
		}
	}
}
