package test;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import count.Single;
import polyform.Polyforms;

/**
 * ClassName: Test_task10_2 <br/>
 * Function: Triangulomino tilings using the 6 fixed triangulaminoes of area 3.
 * 
 * @author wangke
 */
public class Test_task10_2 {
	public static void main(String[] args) throws FileNotFoundException {
		long t1 = System.currentTimeMillis();
		Polyforms pys = new Polyforms("/T3fixed.txt", 3, 2, false, false);
		pys.setHeight(6);
		pys.setWidth(5);
		pys.setBackgroundLozenge();
		Single<LinkedList<Integer>> lst = pys.oneSolution();
		pys.printSolution(lst);
		System.out.println("number of solutions : "+ pys.NumberOfSolutions());
		long t2 = System.currentTimeMillis();
		System.out.println("time elapsed : " + (t2 - t1) + " ms");
	}
}
