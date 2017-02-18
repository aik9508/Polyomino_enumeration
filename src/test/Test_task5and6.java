package test;

import java.util.HashSet;
import java.util.LinkedList;

import count.Single;
import cover.DancingLinks;
import cover.Subset;

/**
 * ClassName: Test_task4 <br/>
 * Function: Implements the DLX algorithm and tests it on the exact cover
 * problem (1) mentioned in the project as well as on large instances (cover a
 * ground set with n elements with all its subsets)
 */
public class Test_task5and6 {
	public static void main(String[] args) {
		HashSet<Subset> cset = new HashSet<Subset>();
		cset.add(new Subset(new int[] { 2, 4, 5 }));
		cset.add(new Subset(new int[] { 0, 3, 6 }));
		cset.add(new Subset(new int[] { 1, 2, 5 }));
		cset.add(new Subset(new int[] { 0, 3 }));
		cset.add(new Subset(new int[] { 1, 6 }));
		cset.add(new Subset(new int[] { 3, 4, 6 }));
		DancingLinks d1 = new DancingLinks(7);
		d1.add(cset);
		Single<LinkedList<LinkedList<Integer>>> solution1 = d1.AllSolutions();
		System.out.println("The solution to the problem (1) mentioned in the project : ");
		System.out.println(solution1);
		// Test the algorithm on middle instance
		int n = 3;
		HashSet<Subset> middleCset = Subset.generateSubsets(n);
		DancingLinks d2 = new DancingLinks(n + 1);
		d2.add(middleCset);
		Single<LinkedList<LinkedList<Integer>>> solution2 = d2.AllSolutions();
		System.out.println(
				"The solutions to the problem (Cover a ground set {0,1,2,...,n} with all its subsets, where n = " + n
						+ " ).");
		System.out.println(solution2);
		// Test the algorithm on large instance
		n = 11;
		long t1 = System.currentTimeMillis();
		HashSet<Subset> largeCset = Subset.generateSubsets(n);
		DancingLinks d3 = new DancingLinks(n + 1);
		d3.add(largeCset);
		long solution3 = d3.NumberOfSolutions();
		System.out.println(
				"The number of solutions to the problem (Cover a ground set {0,1,2,...,n} with all its subsets, where n = "
						+ n + " ).");
		System.out.println(solution3);
		long t2 = System.currentTimeMillis();
		System.out.println("time elapsed : " + (t2 - t1) + " ms");
	}
}
