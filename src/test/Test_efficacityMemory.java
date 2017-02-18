package test;

import java.util.HashSet;
import java.util.Scanner;

import count.AllPolyominoes;
import count.CountFreePolyomino;
import polyform.FixedPolyomino;

/**
 * This class calculates the efficacity of memory of the binary array date-structure.
 *
 */
public class Test_efficacityMemory {

	public static void main(String[] args) {
		System.out.println("We are testing the efficacity of memory of binary array structure.");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Enter the size of polyominoes (-1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			if (n>15){
				System.out.println("SORRY. It will be too long to test "+n+"!");
				continue;
			}
			HashSet<FixedPolyomino> allpoly = AllPolyominoes.allFixedSizeExact(n);
			int[] widthDistribution = new int[n+1];
			for (FixedPolyomino p:allpoly){
				widthDistribution[p.width]++;
			}
			double totalmemory = 0.;
			for (int i=1;i<n+1;i++){
				//System.out.println("width = "+i+" Nbr: "+widthDistribution[i]);
				totalmemory += (i*16.+160.)*widthDistribution[i];
			}
			double eff = totalmemory/(n*64.*allpoly.size());
			System.out.format("EFF(%d) = %.3f\n ",n,eff);
			System.out.println();
		}
	}
}
