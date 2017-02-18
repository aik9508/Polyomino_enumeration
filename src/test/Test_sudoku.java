package test;

import java.io.FileNotFoundException;

import cover.Sudoku;

public class Test_sudoku {

	public static void main(String[] args) throws FileNotFoundException {
		Sudoku sd = new Sudoku();
		int[][] a = sd.add("/sudoku.txt");
		sd.oneSolution(a);
	}
}
