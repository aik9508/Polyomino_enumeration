package test;

import java.util.Scanner;

import polyform.FixedPolyomino;

public class Test_transformations {

	public static void main(String[] args) {
		FixedPolyomino p = new FixedPolyomino(new int[]{0,1,1,1}, new int[]{0,0,1,2});
		System.out.println("Transformation type: "
				+ "(-1) exit (1) translation (2) reflection (3) rotation.");
		Scanner reader = new Scanner(System.in);
		//System.out.print("Please choose the transformation type (1, 2 or 3): ");
		int type = 1;	
		while (true){
			System.out.print("Please choose the transformation type (-1, 1, 2 or 3): ");
			type = reader.nextInt();
			if (type == -1)	break;
			p.drawing();
			switch (type) {
			case 1:
				p.translation(1, 1).drawing();
				break;
			case 2:
				p.reflection('H').drawing();
				p.reflection('V').drawing();
				break;
			case 3:
				p.rotation(1).drawing();
				p.rotation(2).drawing();
				p.rotation(3).drawing();
				break;
			default:
				System.out.println("I don't know this transformation, please choose from 1, 2 and 3.");
				break;
			}
		}
		//reader.close();// Comment this line to avoid the NoSuchElementException in the Test_total class.
	}

}
