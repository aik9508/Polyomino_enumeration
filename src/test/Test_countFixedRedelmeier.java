package test;

import java.util.Scanner;
import count.CountFixedPolyomino;

/**
 * This class is the test of enumeration of fixed polyominoes with the algorithm of Redelmeier.
 *
 */
public class Test_countFixedRedelmeier {

	public static void main(String[] args) {
		System.out.println("We are testing the enumeration of fixed polyominoes "
				+ "with the algorithm of Redelmeier.");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Enter the size of polyominoes to enumerate (-1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			if (n>20){
				System.out.println("SORRY. It will be too long to enumerate "+n+"!");
				continue;
			}
			long start = System.currentTimeMillis();
			long[] count = CountFixedPolyomino.countFixed(n);
			long stop = System.currentTimeMillis();
			System.out.format("%4s Number\n","Size");
			for (int i=1;i<=n;i++){
				System.out.format("%2d   %d\n",i,count[i]);
			}
			System.out.println("Running time: "+(stop-start)+" ms");
			System.out.println();
		}
		//reader.close();// Comment this line to avoid the NoSuchElementException in the Test_total class.
	}

}
