package test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import count.AllPolyominoes;
import polyform.FixedPolyomino;
import polyform.Polyforms;

/**
 * ClassName: Test_task8_2 <br/>
 * Function: Finds all tilings of a rectangle of size w*h by all fixed polyominoes of
 * area n.
 */
public class Test_task8_2 {
	public static void main(String[] args) throws FileNotFoundException {
		int n = 5;
		int widthOfRectangle=6, heightOfRectangle=5;
		HashSet<FixedPolyomino> lst=AllPolyominoes.allFixedSizeExact(n);
		Polyforms pys = new Polyforms(4, 0, false, false);
		pys.setHeight(heightOfRectangle);
		pys.setWidth(widthOfRectangle);
		for (FixedPolyomino fp : lst)
			pys.add(fp);
		pys.printSolution(pys.oneSolution());
		long t1=System.currentTimeMillis();
		System.out.printf("tiling a rectangle of size %d * %d by all fixed polyominoes of size %d",widthOfRectangle, heightOfRectangle,n);
		System.out.println();
		System.out.println("number of solutions : " + pys.NumberOfSolutions());
		long t2=System.currentTimeMillis();
		System.out.println("time of counting : "+ (t2-t1)+" ms");
	}
}
