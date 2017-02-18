package test;

import java.util.Scanner;
import count.CountFreePolyomino;

/**
 * This class is the test of counts all symmetric free polyominoes wiht the algorithm
 * of Redelemier.
 */
public class Test_countFreeRedelmeier {
	public static void main(String[] args) {
		System.out.println("We are testing the symmetries of free polyominoes "
				+ "with the algorithm of Redelmeier.");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Enter the size of polyominoes to enumerate (-1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			if (n>25){
				System.out.println("SORRY. It will be too long to test "+n+"!");
				continue;
			}
			CountFreePolyomino.count(n);
			System.out.println();
		}
		//reader.close();	// Comment this line to avoid the NoSuchElementException in the Test_total class.
	}
}
