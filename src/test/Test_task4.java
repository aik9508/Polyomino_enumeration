package test;

import java.util.HashSet;
import java.util.LinkedList;

import cover.ExactCoverNaif;
import cover.Subset;

/**
 * ClassName: Test_task4 <br/>
 * Function: Implements the naive algorithm of exact cover and tests it on the
 * exact cover problem (1) mentioned in the project as well as on large
 * instances (cover a ground set with n elements with all its subsets)
 */
public class Test_task4 {
	public static void main(String[] args) {
		HashSet<Subset> cset = new HashSet<Subset>();
		cset.add(new Subset(new int[] { 2, 4, 5 }));
		cset.add(new Subset(new int[] { 0, 3, 6 }));
		cset.add(new Subset(new int[] { 1, 2, 5 }));
		cset.add(new Subset(new int[] { 0, 3 }));
		cset.add(new Subset(new int[] { 1, 6 }));
		cset.add(new Subset(new int[] { 3, 4, 6 }));
		ExactCoverNaif e1 = new ExactCoverNaif(7, cset);
		LinkedList<LinkedList<Subset>> solution1 = e1.allSolutionsNaif();
		System.out.println("The solution to the problem (1) mentioned in the project : ");
		System.out.println(solution1);
		// Test the algorithm on middle instance
		int n = 3;
		HashSet<Subset> middleCset = Subset.generateSubsets(n);
		ExactCoverNaif e2 = new ExactCoverNaif(n + 1, middleCset);
		LinkedList<LinkedList<Subset>> solution2 = e2.allSolutionsNaif();
		System.out.println(
				"The solutions to the problem (Cover a ground set {0,1,2,...,n} with all its subsets, where n = " + n
						+ " ).");
		System.out.println(solution2);
		// Test the algorithm on large instance
		n = 9;
		long t1 = System.currentTimeMillis();
		HashSet<Subset> largeCset = Subset.generateSubsets(n);
		ExactCoverNaif e3 = new ExactCoverNaif(n + 1, largeCset);
		long solution3 = e3.numberOfSolutionsNaif();
		System.out.println(
				"The number of solutions to the problem (Cover a ground set {0,1,2,...,n} with all its subsets, where n = "
						+ n + " ).");
		System.out.println(solution3);
		long t2 = System.currentTimeMillis();
		System.out.println("Running Time:" + (t2 - t1)+" ms");
	}
}
