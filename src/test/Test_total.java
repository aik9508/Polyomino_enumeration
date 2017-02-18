package test;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test_total {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Tests list: ");
		System.out.println("Enumeration part:");
		System.out.println("(1) count fixed polyominoes by naive algorithm");
		System.out.println("(2) count free polyominoes by naive algorithm");
		System.out.println("(3) count fixed polyominoes by algorithm of Redelmeier");
		System.out.println("(4) test free polyominoes' symmetries by algorithm of Redelmeier");
		System.out.println("(5) test the transformations");
		System.out.println("(6) test the efficacity of memory of the binary array structure");
		System.out.println("Exact cover part:");
		System.out.println("(7) test the task 4\t(8) test the task 5 & 6");
		System.out.println("(9) test the task 8.1\t(10) test the task 8.2");
		System.out.println("(11) test the task 8.3\t(12) test the task 10.1");
		System.out.println("(13) test the task 10.2\t(14) test the task 10.3");
		System.out.println("(15) test the task 10.4\t(16) test of Sudoku");
		Scanner reader = new Scanner(System.in);
		int n = 0;
		while (n != -1){
			System.out.print("Please choose the test you want to do (1-16, -1 to exit): ");
			n = reader.nextInt();
			if (n==-1)	break;
			switch (n) {
			case 1:
				Test_countFixedNaive.main(args);
				break;
			case 2:
				Test_countFreeNaive.main(args);
				break;
			case 3:
				Test_countFixedRedelmeier.main(args);
				break;
			case 4:
				Test_countFreeRedelmeier.main(args);
				break;
			case 5:
				Test_transformations.main(args);
				break;
			case 6:
				Test_efficacityMemory.main(args);
				break;
			case 7:
				Test_task4.main(args);
				break;
			case 8:
				Test_task5and6.main(args);
				break;
			case 9:
				Test_task8_1.main(args);
				break;
			case 10:
				Test_task8_2.main(args);
				break;
			case 11:
				Test_task8_3.main(args);
				break;
			case 12:
				Test_task10_1.main(args);
				break;
			case 13:
				Test_task10_2.main(args);
				break;
			case 14:
				Test_task10_3.main(args);
				break;
			case 15:
				Test_task10_4.main(args);
				break;
			case 16:
				Test_sudoku.main(args);
				break;
			default:
				System.out.println("Please choose from 1 to 15, -1 to exit.");
				break;
			}
		}
		reader.close();
	}

}
