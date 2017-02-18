package test;

import java.util.HashSet;
import java.util.LinkedList;

import count.AllPolyominoes;
import count.Single;
import polyform.FreePolyomino;
import polyform.Polyforms;

/** 
 * ClassName: Test_task8_1 <br/> 
 * Function: The answer to the first question in task 8.
 */
public class Test_task8_1 {
	public static void main(String[] args) {
		HashSet<FreePolyomino> lst=AllPolyominoes.allFreeSizeExact(5);
		Polyforms pys = new Polyforms(4,2,false,false);
		for(FreePolyomino fp:lst)
			pys.add(fp.toFixed());
		pys.setWidth(10);
		pys.setHeight(10);
		//pys.setBackgroundTriangle();
		//pys.setBackgroundParallelogram();
		pys.setBackgroundLozenge();
		Single<LinkedList<Integer>> l=pys.oneSolution();
		pys.printSolution(l);
		//System.out.println(pys.NumberOfSolutions());
	}
}
