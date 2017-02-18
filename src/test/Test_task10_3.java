package test;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import count.Single;
import polyform.Polyforms;

/** 
 * ClassName: Test_task10_3 <br/> 
 * Function: Triangulomino tilings using the 12 free triangulaminoes of area 6.
 * 
 * @author wangke 
 */
public class Test_task10_3 {
	public static void main(String[] args) throws FileNotFoundException {
		long t1 = System.currentTimeMillis();
		Polyforms pys = new Polyforms("/T6free.txt", 3, 2, false, false);
		pys.setHeight(12);
		pys.setWidth(11);
		pys.setBackgroundLozenge();
		Single<LinkedList<Integer>> lst = pys.oneSolution();
		pys.printSolution(lst);
		// System.out.println("number of solutions : "+ pys.NumberOfSolutions());
		long t2 = System.currentTimeMillis();
		System.out.println("time elapsed : "+(t2 - t1)+" ms");
	}
}
