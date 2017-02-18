package test;

import polyform.Polyforms;

/** 
 * ClassName: Test_task8_3 <br/> 
 * Function: We look for all polyominoes P of size n that can cover its own dilate kP with n=8 and k=4.
 */
public class Test_task8_3 {
	public static void main(String[] args) {
		System.out.println(Polyforms.selfCover(8, 4));
	}
}
