import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.awt.Graphics2D;
import java.awt.Font;

import java.util.ArrayList;

import javax.swing.JPanel;

import javax.swing.JPanel;

public class Apollo extends JFrame{
//public class Apollo {
	private static Board board = new Board();
	
	public Apollo () {
		board.getInstance();
		board.initialize();
		setSize(new Dimension (700,700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		add(board, BorderLayout.CENTER);
		board.DFS4(Board.grid[1][1]);
		board.traceback(Board.grid[7][7]);
	}

	public static void main(String[] args) {
		Apollo gui = new Apollo();
	
		gui.setVisible(true);
		
		

	}

}


public class Board extends JPanel{
//public class Board {
	public static BoardCell[][] grid;
	public BoardCell[][] oppogrid;
	public static Board instance = new Board();
	private static int rows;
	private static int columns;
	public static final int SQUARE_SIZE = 70;
	public boolean opposite = false;
	Stack<BoardCell>stack = new Stack<BoardCell>();	
	int vertical;
	int horizontal;

	public Board() {}

	public Board getInstance() {
		return instance;
	}

	public void initialize() {

		try {
			loadMazeConfig();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		calcAdjacencies();
		calcOppoAdjacencies();
		
		
	}
	
	public void DFS4(BoardCell current) {
		if (current.getColor().equals(Colors.BULLSEYE)) {
			stack.push(current);
			
		}
		current.setDone(true);
		
		ArrayList <BoardCell> adjList = new ArrayList<BoardCell>();
		
		adjList = current.getAdjacencies();
		for (int i = 0; i < adjList.size(); i++) {
			BoardCell temp = adjList.get(i);
			if (temp.isCircle()) {
				int row = temp.getRow();
				int col = temp.getColumn();
				if (temp.equals(grid[row][col])) {
					temp = oppogrid[row][col];
				}
				else {
					temp = grid[row][col];
				}
				
			}
			if (!temp.isDone()) {
				temp.setDone(true);
				temp.setParent(current);
				DFS4(temp);
				
			}
		}
	}

	private void calcOppoAdjacencies() {
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				ArrowDirection direction = oppogrid[i][j].getDirection();
				Colors color = oppogrid[i][j].getColor();
				switch (direction) {
				case NORTH:
					for (int k = i; k > 0; k--) {
						if (!color.equals(oppogrid[k][j].getColor()) && !oppogrid[k][j].getColor().equals(null)){
							oppogrid[i][j].setAdjacencies(oppogrid[k][j]);
						}
					}
					break;
				case SOUTH:
					for (int k = i; k <= rows; k++) {
						if (!color.equals(oppogrid[k][j].getColor())&& !oppogrid[k][j].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[k][j]);
						}
					}
					break;
				case EAST:
					for (int k = j; k <= rows; k++) {
						if (!color.equals(oppogrid[i][k].getColor()) && !oppogrid[i][k].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[i][k]);
						}
					}
					break;
				case WEST:
					for (int k = j; k > 0; k--) {
						if (!color.equals(oppogrid[i][k].getColor()) && !oppogrid[i][k].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[i][k]);
						}
					}
					break;
				case NORTHWEST:
					vertical = oppogrid[i][j].getRow();
					horizontal = oppogrid[i][j].getColumn();	

					while (horizontal >= 1 && vertical >= 1) {
						if (!color.equals(oppogrid[vertical][horizontal].getColor()) && !oppogrid[vertical][horizontal].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[vertical][horizontal]);
						}
						vertical--;
						horizontal--;
					}
					
					break;
				case NORTHEAST:
					vertical = oppogrid[i][j].getRow();
					horizontal = oppogrid[i][j].getColumn();
					
					while (horizontal <= columns && vertical >= 1) {
						if (!color.equals(oppogrid[vertical][horizontal].getColor()) && !oppogrid[vertical][horizontal].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[vertical][horizontal]);
						}
						vertical--;
						horizontal++;
					}
					
					break;
				case SOUTHEAST:
					vertical = oppogrid[i][j].getRow();
					horizontal = oppogrid[i][j].getColumn();
					while (horizontal <= columns && vertical <= rows) {
						if (!color.equals(oppogrid[vertical][horizontal].getColor()) && !oppogrid[vertical][horizontal].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[vertical][horizontal]);
						
						}
						vertical++;
						horizontal++;
					}
					break;
				case SOUTHWEST:
					vertical = oppogrid[i][j].getRow();
					horizontal = oppogrid[i][j].getColumn();
					while (horizontal >=  1 && vertical <= rows) {
						if (!color.equals(oppogrid[vertical][horizontal].getColor()) && !oppogrid[vertical][horizontal].getColor().equals(null)) {
							oppogrid[i][j].setAdjacencies(oppogrid[vertical][horizontal]);
						}
						vertical++;
						horizontal--;
					}
					break;
				}
			}


		}


	}

	private void calcAdjacencies() {
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				ArrowDirection direction = grid[i][j].getDirection();
				Colors color = grid[i][j].getColor();
				switch (direction) {
				case NORTH:
					for (int k = i; k > 0; k--) {
						if (!color.equals(grid[k][j].getColor()) && !grid[k][j].getColor().equals(null)){
							grid[i][j].setAdjacencies(grid[k][j]);
						}
					}
					break;
				case SOUTH:
					for (int k = i; k <= rows; k++) {
						if (!color.equals(grid[k][j].getColor())&& !grid[k][j].getColor().equals(null)) {
							grid[i][j].setAdjacencies(grid[k][j]);
						}
					}
					break;
				case EAST:
					for (int k = j; k <= rows; k++) {
						if (!color.equals(grid[i][k].getColor()) && !grid[i][k].getColor().equals(null)) {
							grid[i][j].setAdjacencies(grid[i][k]);
						}
					}
					break;
				case WEST:
					for (int k = j; k > 0; k--) {
						if (!color.equals(grid[i][k].getColor()) && !grid[i][k].getColor().equals(null)) {
							grid[i][j].setAdjacencies(grid[i][k]);
						}
					}
					break;
				case NORTHWEST:
					vertical = grid[i][j].getRow();
					horizontal = grid[i][j].getColumn();	

					while (horizontal >= 1 && vertical >= 1) {
						if (!color.equals(grid[vertical][horizontal].getColor()) && !grid[vertical][horizontal].getColor().equals(null)) {
							grid[i][j].setAdjacencies(grid[vertical][horizontal]);
						}
						vertical--;
						horizontal--;
					}
					
					break;
				case NORTHEAST:
					vertical = grid[i][j].getRow();
					horizontal = grid[i][j].getColumn();
					
					while (horizontal <= columns && vertical >= 1) {
						if (!color.equals(grid[vertical][horizontal].getColor()) && !grid[vertical][horizontal].getColor().equals(null)) {
							grid[i][j].setAdjacencies(grid[vertical][horizontal]);
						}
						vertical--;
						horizontal++;
					}
					
					break;
				case SOUTHEAST:
					vertical = grid[i][j].getRow();
					horizontal = grid[i][j].getColumn();
					while (horizontal <= columns && vertical <= rows) {
						if (!color.equals(grid[vertical][horizontal].getColor()) && !grid[vertical][horizontal].getColor().equals(null)) {
						grid[i][j].setAdjacencies(grid[vertical][horizontal]);
						
						}
						vertical++;
						horizontal++;
					}
					break;
				case SOUTHWEST:
					vertical = grid[i][j].getRow();
					horizontal = grid[i][j].getColumn();
					while (horizontal >=  1 && vertical <= rows) {
						if (!color.equals(grid[vertical][horizontal].getColor()) && !grid[vertical][horizontal].getColor().equals(null)) {
						grid[i][j].setAdjacencies(grid[vertical][horizontal]);
						}
						vertical++;
						horizontal--;
					}
					break;
				}
			}


		}


	}
	
	

	private void loadMazeConfig() throws FileNotFoundException {
		FileReader reader;
		Scanner in;

		reader = new FileReader("input.txt");
		in = new Scanner(reader);

		int i = 0;
		while (in.hasNextLine()){

			String[] temp = in.nextLine().split(" ");

			if (i == 0) {
				rows = Integer.parseInt(temp[0]);
				columns = Integer.parseInt(temp[1]);
				grid = new BoardCell[rows + 1][columns + 1];
				oppogrid = new BoardCell[rows + 1][columns + 1];
				for (int r = 0; r < rows + 1; r++) {
					grid[r][0] = new BoardCell(r, 0, "n", 'n', "Y");
					oppogrid[r][0] = new BoardCell(r, 0, "n", 'n', "Y");
				}
				for (int c = 0; c < columns + 1; c++) {
					grid[0][c] = new BoardCell(0, c, "n", 'n', "Y");
					oppogrid[0][c] = new BoardCell(0, c, "n", 'n', "Y");
				}
				i++;
			}
			else {
				grid[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = new BoardCell(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), temp[2], temp[3].charAt(0), temp[4]);
				oppogrid[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = new BoardCell(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), temp[2], temp[3].charAt(0), temp[4], 1);
				//System.out.println(Integer.parseInt(temp[0]) + " " +  Integer.parseInt(temp[1]) + " " + temp[2] + " " + temp[3].charAt(0) + " " + temp[4]);
			}

		}
		in.close();
	}

	@Override
	public void paintComponent(Graphics g) {
		
		this.setBackground(Color.LIGHT_GRAY);
		super.paintComponent(g);
		for (int i = 0; i < rows + 1; i++) {
			BoardCell arr[] = grid[i];
			BoardCell oppoarr[] = oppogrid[i];
			for (int j = 0; j < columns + 1; j++) {
				
				arr[j].draw(g);
				if (oppoarr[j].isPath()) {
					oppoarr[j].draw(g);
				}

			}
		}
	}

	public void traceback(BoardCell end) {
		
		if (!(end.getParent() == null)) {
			stack.push(end.getParent());
			traceback(end.getParent());
			
		}
		
		else {
			int num = 1;
			while (!stack.isEmpty()) {
				
				BoardCell path = stack.pop();
				path.setPathNum(num);

				path.setPath(true);
				num++;
				System.out.print(path);
				System.out.print(" ");
			}
		}

	}




}





public class BoardCell extends JPanel implements Comparable {
//public class BoardCell implements Comparable {
	private int row;
	private int column;
	private Colors color;
	private ArrowDirection direction;
	private String directionChar;
	private boolean isCircle;
	private boolean isDone;
	private BoardCell parent;
	private boolean isPath;
	private ArrayList <BoardCell> adjacencies = new ArrayList<BoardCell>(); 
	private int pathNum;

	public ArrayList<BoardCell> getAdjacencies() {
		return adjacencies;
	}

	public void setAdjacencies(BoardCell adjacencies) {
		this.adjacencies.add(adjacencies);
	}

	public BoardCell getParent() {
		return parent;
	}

	public void setParent(BoardCell parent) {
		this.parent = parent;
	}

	public BoardCell(){
		
	}

	public BoardCell(int r, int c, String redorblue, char o, String i) {
		row = r;
		column = c;
		directionChar = i;
		isDone = false;
		isPath = false;
		
		if (i.equals("N")) {
			directionChar = "\u2191";
			direction = ArrowDirection.NORTH;
		}
		else if (i.equals("S")) {
			directionChar = "\u2193";
			direction = ArrowDirection.SOUTH;
		}
		else if (i.equals("E")) {
			directionChar = "\u2192";
			direction = ArrowDirection.EAST;
		}
		else if (i.equals("W")) {
			directionChar = "\u2190";
			direction = ArrowDirection.WEST;
		}
		else if (i.equals("NE")) {
			directionChar = "\u2197";
			direction = ArrowDirection.NORTHEAST;
		}
		else if (i.equals("SE")) {
			directionChar = "\u2198";
			direction = ArrowDirection.SOUTHEAST;
		}
		else if (i.equals("NW")) {
			directionChar = "\u2196";
			direction = ArrowDirection.NORTHWEST;
		}
		else if (i.equals("SW")) {
			directionChar = "\u2199";
			direction = ArrowDirection.SOUTHWEST;
		}
		else if (i.equals("X")) {
			directionChar = "\u2605";
			direction = ArrowDirection.BULLSEYE;
		}
		else {
			directionChar = "\u003F";
			direction = ArrowDirection.NONE;
		}
		
		if (o == 'C') {
			isCircle = true;
		}
		else {
			isCircle = false;
		}
		
		if (redorblue.equals("R")) {
			color = Colors.RED;
		}
		else if (redorblue.equals("B")) {
			color = Colors.BLUE;
		}
		else if (redorblue.equals("X")){
			color = Colors.BULLSEYE;
		}
		else {
			color = Colors.NONE;
		}
	}
	
	public BoardCell(int r, int c, String redorblue, char o, String i, int oppo) {
		row = r;
		column = c;
		directionChar = i;
		isDone = false;
		isPath = false;
		
		if (i.equals("N")) {
			directionChar = "\u2191";
			direction = ArrowDirection.SOUTH;
		}
		else if (i.equals("S")) {
			directionChar = "\u2193";
			direction = ArrowDirection.NORTH;
		}
		else if (i.equals("E")) {
			directionChar = "\u2192";
			direction = ArrowDirection.WEST;
		}
		else if (i.equals("W")) {
			directionChar = "\u2190";
			direction = ArrowDirection.EAST;
		}
		else if (i.equals("NE")) {
			directionChar = "\u2197";
			direction = ArrowDirection.SOUTHWEST;
		}
		else if (i.equals("SE")) {
			directionChar = "\u2198";
			direction = ArrowDirection.NORTHWEST;
		}
		else if (i.equals("NW")) {
			directionChar = "\u2196";
			direction = ArrowDirection.SOUTHEAST;
		}
		else if (i.equals("SW")) {
			directionChar = "\u2199";
			direction = ArrowDirection.NORTHEAST;
		}
		else if (i.equals("X")) {
			directionChar = "\u2605";
			direction = ArrowDirection.BULLSEYE;
		}
		else {
			directionChar = "\u003F";
			direction = ArrowDirection.NONE;
		}
		
		if (o == 'C') {
			isCircle = true;
		}
		else {
			isCircle = false;
		}
		
		if (redorblue.equals("R")) {
			color = Colors.RED;
		}
		else if (redorblue.equals("B")) {
			color = Colors.BLUE;
		}
		else if (redorblue.equals("X")){
			color = Colors.BULLSEYE;
		}
		else {
			color = Colors.NONE;
		}
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public Colors getColor() {
		return color;
	}

	public void setColor(Colors color) {
		this.color = color;
	}

	public String getDirectionChar() {
		return directionChar;
	}

	public void setDirectionChar(String directionChar) {
		this.directionChar = directionChar;
	}

	public boolean isCircle() {
		return isCircle;
	}


	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setDirection(ArrowDirection direction) {
		this.direction = direction;
	}

	public void draw (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Font font = new Font("Lucida Sans Regular", Font.PLAIN, 25);
		g2.setFont(font);
		if (color.equals(Colors.RED)) {
			
			g.setColor(Color.RED);
			g.fillOval(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			g.setColor(Color.BLACK);
			g.drawOval(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			
			if (isCircle) {
				g.setColor(Color.MAGENTA);
			}
			
			g2.drawString(directionChar, column * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 4), row * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2));
	
			
		}
		else if (color.equals(Colors.BLUE)) {
			g.setColor(Color.BLUE);
			g.fillOval(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			g.setColor(Color.BLACK);
			g.drawOval(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			if (isCircle) {
				g.setColor(Color.MAGENTA);
			}
			g.drawString(directionChar, column * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 4), row * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2));
			
			
		}
		else if (color.equals(Colors.BULLSEYE)){
			g.setColor(Color.DARK_GRAY);
			g.fillOval(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			g.setColor(Color.WHITE);
			g.drawString(directionChar, column * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2) - 20, row * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2));
			
		}
		if (isPath){
			//g.setColor(Color.CYAN);
			//g.fillRect(column * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
			g.setColor(Color.YELLOW);
			g2.drawString(Integer.toString(pathNum), column * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2), row * Board.SQUARE_SIZE + (int)(Board.SQUARE_SIZE / 2) + 10);
			
		}

		
	}


	public int getPathNum() {
		return pathNum;
	}

	public void setPathNum(int pathNum) {
		this.pathNum = pathNum;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ")";
	}

	public boolean isDoorway() {
		if (direction == ArrowDirection.NONE) return false;
		return true;
	}


	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public ArrowDirection getDirection() {
		return direction;
	}

	@Override
	public int compareTo(Object o) {
		
		return 0;
	}


}





