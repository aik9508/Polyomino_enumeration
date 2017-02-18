package test;

import java.util.HashSet;
import java.util.Scanner;

import count.AllPolyominoes;
import polyform.FixedPolyomino;

/**
 * This class is the test of enumeration of fixed polyominoes with the naive algorithm.
 *
 */
public class Test_countFixedNaive {

	public static void main(String[] args) {
		System.out.println("We are testing the enumeration of fixed polyominoes "
				+ "with the naive algorithm.");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Enter the size of polyominoes to enumerate (-1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			if (n>15){
				System.out.println("SORRY. "+n+" is too big to enumerate !");
				continue;
			}
			long start = System.currentTimeMillis();
			HashSet<FixedPolyomino> allpoly = AllPolyominoes.allFixedSizeExact(n);
			long stop = System.currentTimeMillis();
			System.out.println("Number of fixed polyominoes of size "+n+": "+allpoly.size());
			System.out.println("Running time: "+(stop-start)+" ms");
			System.out.println();
		}
		//reader.close(); // Comment this line to avoid the NoSuchElementException in the Test_total class.
	}

}
