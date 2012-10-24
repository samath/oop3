import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	private final int[][] grid;
	private int count = 0;
	private boolean solved = false;
	private long startTime;
	
	private String solution = "";
	private String original;
	
	private class Spot {
		private int x, y;
		
		private Spot(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		private void set(int newValue) {
			grid[x][y] = newValue;
		}
		
		private Set<Integer> getValidNumbers() {
			HashSet<Integer> s = new HashSet<Integer>(
					Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			for(int i = 0; i < SIZE; i++) {
				if(i != x) s.remove(get(i, y));
			}
			for(int j = 0; j < SIZE; j++) {
				if(j != y) s.remove(get(x, j));
			}
			for(int i = 0; i < PART; i++) {
				for(int j = 0; j < PART; j++) {
					int newX = PART * (x / PART) + i;
					int newY = PART * (y / PART) + j;
					if(newX != x || newY != y)
						s.remove(get(newX, newY));
				}
			}
			return s;
		}
	}
	
	private int get(int x, int y) {
		return grid[x][y];
	}
	
	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);	
		System.out.println(sudoku); // print the raw problem
		
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	@Override
	public String toString() {
		return original;
	}
	
	public static String gridToText(int[][] g) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < g.length; i++) {
			for(int j = 0; j < g[0].length; j++) {
				sb.append(g[i][j] + " ");
			}
			if(i < SIZE - 1) sb.append("\n");
		}
		return sb.toString();
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = deepCopy(ints);
		startTime = System.currentTimeMillis();
		original = gridToText(grid);
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		if(solved) return count;
		List<Spot> spots = sortedBlankCells();
		int maxSpots = spots.size();
		trySpot(spots, 0, maxSpots);		
		return count; // YOUR CODE HERE
	}
	
	/**
	 * Recursive helper function for solve.
	 * Assumes that every spot in spots before index is non-zero, 
	 * everything after is 0.
	 * Iterates over options in getValidNumbers() and follows recursively.
	 */
	private void trySpot(List<Spot> spots, int index, int length) {
		if(count >= MAX_SOLUTIONS) return;
		if(index == length) {
			if(count == 0) solution = gridToText(grid);
			count++;
			return;
		}
		Spot s = spots.get(index);
		for(Integer i : s.getValidNumbers()) {
			s.set(i);
			trySpot(spots, index + 1, length);
		}
		s.set(0);
	}
	
	/**
	 * Iterates over all the cells in the grid and selects those with 0
	 * by calling the Spot constructor.
	 * Uses the Comparator class and the Collections sort function to sort
	 * the list based on which cell has the fewest options.
	 */
	private List<Spot> sortedBlankCells() {
		List<Spot> list = new ArrayList<Spot>();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				if(get(i, j) == 0) list.add(new Spot(i, j));
			}
		}
		
		Collections.sort(list, new Comparator<Spot>() {
			public int compare(Spot s1, Spot s2) {
				return s1.getValidNumbers().size() - s2.getValidNumbers().size();
			}
		});		
		
		return list;
				
	}
	
	public String getSolutionText() {
		return solution;
	}
	
	public long getElapsed() {
		return System.currentTimeMillis() - startTime;
	}
	
	
	/**
	 * Check if the grid is in a valid starting state.
	 */
	public boolean validate() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				int num = grid[i][j];
				if(num != 0) {
					for(int k = i + 1; k < SIZE; k++) {
						if(num == grid[k][j]) {
							return false;
						}
					}
					for(int l = j + 1; l < SIZE; l++) {
						if(num == grid[i][l]) {
							return false;
						}
					}
					for(int k = 0; k < PART; k++) {
						for(int l = 0; l < PART; l++) {
							int x = PART * (i / PART) + k, y = PART * (j / PART) + l;
							if(!(x == i && y == j) && num == grid[x][y]) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	//Unsolvable -- top left corner has no choices	
	public static final int[][] unsolvableGrid = Sudoku.stringsToGrid(
	"0 1 2 3 0 0 0 0 0",
	"4 5 6 0 0 0 0 0 0",
	"7 8 9 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0");
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}
	
	private static int[][] deepCopy(int[][] ints) {
		if(ints.length == 0) return new int[0][0];
		int length = ints.length, width = ints[0].length;
		int[][] n = new int[length][width];
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				n[i][j] = ints[i][j];
			}
		}
		return n;
	}

}