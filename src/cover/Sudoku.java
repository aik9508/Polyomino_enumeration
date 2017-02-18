package cover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

import count.Single;

public class Sudoku {
	DancingLinks d;

	/**
	 * Creates a new instance of Sudoku.
	 * 
	 */
	public Sudoku() {
		d = new DancingLinks(324);
		d.add(generateCset());
	}

	/**
	 * add: pre-covers the columns associated with the numbers that we have
	 * already known in a sudoku grid.<br/>
	 * 
	 * @param a
	 *            a sudoku grid
	 */
	public void add(int[][] a) {
		if (a == null)
			return;
		assert a.length == 9;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (a[i][j] > 0 && a[i][j] <= 9) {
					d.coverColumn(i * 9 + a[i][j] - 1);
					d.coverColumn(j * 9 + a[i][j] + 80);
					d.coverColumn(((i / 3) * 3 + (j / 3)) * 9 + a[i][j] + 161);
					d.coverColumn(i * 9 + j + 243);
				}
	}

	/**
	 * add: reads a sudoku grid from a file and pre-covers the columns
	 * associated with the numbers that have already been given<br/>
	 * 
	 * @param fileName
	 *            the file that stocks the information of a sudoku gird
	 * @return a sudoku grid
	 * @throws FileNotFoundException
	 */
	public int[][] add(String fileName) throws FileNotFoundException {
		int[][] a = new int[9][9];
		//File file = new File(fileName);
		InputStream in = getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					char c = (char) reader.read();
					int t = Integer.parseInt(c + "");
					if (t > 0 && t <= 9) {
						a[i][j] = t;
					}
				}
				reader.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		add(a);
		//reader.close();
		return a;
	}

	/**
	 * generateCset: generates the collection of subsets <br/>
	 * 
	 * @return the collection of subsets
	 */
	private int[][] generateCset() {
		int[][] cset = new int[729][4];
		for (int i = 0; i < 729; i++) {
			int row = i / 81, column = (i % 81) / 9, val = i % 9;
			int grid = (row / 3) * 3 + (column / 3);
			cset[i][0] = row * 9 + val;
			cset[i][1] = column * 9 + val + 81;
			cset[i][2] = grid * 9 + val + 162;
			cset[i][3] = row * 9 + column + 243;
		}
		return cset;
	}

	/**
	 * oneSolution: finds one solution for an empty sudoku grid<br/>
	 * 
	 */
	public void oneSolution() {
		oneSolution(null);
	}

	/**
	 * oneSolution: finds one solution to a sudoku problem<br/>
	 * 
	 * @param a
	 *            the initial sudoku grid
	 */
	public void oneSolution(int[][] a) {
		int[][] solution = new int[9][9];
		if (a != null) {
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++)
					solution[i][j] = a[i][j];
			for (int i = 0; i < 9; i++) {
				System.out.println("-------------------------------------");
				for (int j = 0; j < 9; j++) {
					if (a[i][j] != 0)
						System.out.print("| " + a[i][j] + " ");
					else
						System.out.print("|   ");
				}
				System.out.println("|");
			}
			System.out.println("-------------------------------------");
		}
		Single<LinkedList<Integer>> lst = d.OneSolution();
		while (lst != null && lst.val != null) {
			int[] tmp = new int[4];
			int i = 0;
			for (int e : lst.val)
				tmp[i++] = e;
			Arrays.sort(tmp);
			solution[(tmp[3] - 243) / 9][(tmp[3] - 243) % 9] = (tmp[0] % 9) + 1;
			lst = lst.next;
		}
		for (int i = 0; i < 9; i++) {
			System.out.println("-------------------------------------");
			for (int j = 0; j < 9; j++) {
				System.out.print("| " + solution[i][j] + " ");
			}
			System.out.println("|");
		}
		System.out.println("-------------------------------------");
	}

	/**
	 * nbsolutions: <br/>
	 * 
	 * @return number of solutions
	 */
	public long nbsolutions() {
		return d.NumberOfSolutions();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Sudoku sd = new Sudoku();
		int[][] a = sd.add("/sudoku.txt");
		sd.oneSolution(a);
	}
}
