package test;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import count.Single;
import polyform.Polyforms;

/**
 * ClassName: Test_task10_4 <br/>
 * Function: Triangulomino tilings using the 19 one-sided triangulaminoes of
 * area 6.
 * 
 * @author wangke
 */
public class Test_task10_4 {
	public static void main(String[] args) throws FileNotFoundException {
		long t1 = System.currentTimeMillis();
		Polyforms pys = new Polyforms("/T6oneside.txt", 3, 1, false, false);
		pys.setHeight(10);
		pys.setWidth(16);
		Polyforms background = new Polyforms("/background.txt", 3, 0, false, false);
		pys.setBackground(background.polyforms);
		Single<LinkedList<Integer>> lst = pys.oneSolution();
		pys.printSolution(lst);
		// System.out.println("number of solutions : "+pys.NumberOfSolutions());
		long t2 = System.currentTimeMillis();
		System.out.println("time elapsed " + (t2 - t1) + "ms");
	}
}
