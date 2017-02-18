package test;

import java.util.HashSet;
import java.util.Scanner;
import count.AllPolyominoes;
import polyform.FreePolyomino;

/**
 * This class is the test of enumeration of free polyominoes with the naive algorithe.
 *
 */
public class Test_countFreeNaive {

	public static void main(String[] args) {
		System.out.println("We are testing the enumeration of free polyominoes "
				+ "with the naive algorithm.");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Enter the size of polyominoes to enumerate (-1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			if (n>16){
				System.out.println("SORRY. "+n+" is too big to enumerate !");
				continue;
			}
			long start = System.currentTimeMillis();
			HashSet<FreePolyomino> allpoly = AllPolyominoes.allFreeSizeExact(n);
			long stop = System.currentTimeMillis();
			System.out.println("Number of free polyominoes of size "+n+": "+allpoly.size());
			System.out.println("Running time: "+(stop-start)+" ms");
			System.out.println();
		}
		//reader.close();// Comment this line to avoid the NoSuchElementException in the Test_total class.
	}
}
