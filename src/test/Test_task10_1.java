package test;

import java.io.FileNotFoundException;
import polyform.Polyforms;

/** 
 * ClassName: Test_task10_1 <br/> 
 * Function: Hexagonamino tilings using the 44 fixed triangulaminoes of
 * area 4.
 * 
 * @author wangke 
 */
public class Test_task10_1 {
	public static void main(String[] args) throws FileNotFoundException {
		Polyforms pys=new Polyforms("/H4free.txt",6,2,false, false);
		pys.setWidth(16);
		pys.setHeight(11);
		long t1=System.currentTimeMillis();
		pys.printSolution(pys.oneSolution());
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
	}
}
