package count;
import java.util.HashSet;
import java.util.LinkedList;

import polyform.FixedPolyomino;
import polyform.FreePolyomino;

/**
 * This class defines several functions pour generating all polyominoes of a specific size
 * or all polyominoes less than a specific size.
 */
public class AllPolyominoes {

	/**
	 * allFixedSizeLess: generates all fixed polyominoes of size less
	 * than or equals to the argument n by naive algorithm.
	 * @param n the maximum of size
	 * @return A LinkedList of all polyominoes of size less than or equal to n.
	 */
	static public LinkedList<FixedPolyomino> allFixedSizeLess(int n) {
		if (n == 0)
			return null;
		LinkedList<FixedPolyomino> allInList = new LinkedList<>();
		HashSet<FixedPolyomino> setPrev = new HashSet<FixedPolyomino>();
		FixedPolyomino polyOfSize1 = new FixedPolyomino(new int[] { 0 }, new int[] { 0 });
		setPrev.add(polyOfSize1);
		allInList.add(polyOfSize1);
		if (n > 1) {
			for (int i = 2; i <= n; i++) {
				HashSet<FixedPolyomino> sizeOfi = new HashSet<>();
				for (FixedPolyomino poly : setPrev) {
					poly.addOneCell(sizeOfi);
				}
				allInList.addAll(sizeOfi);
				setPrev = sizeOfi;
			}
		}
		return allInList;
	}

	/**
	 * allFixedSizeExact: generates all fixed polyominoes of size n by naive algorithm.
	 * @param n size of polyominoes
	 * @return An HashSet of all polyominoes of size exactly equal to n.
	 */
	static public HashSet<FixedPolyomino> allFixedSizeExact(int n) {
		assert n > 0;
		HashSet<FixedPolyomino> setPrev = new HashSet<FixedPolyomino>();
		FixedPolyomino polyOfSize1 = new FixedPolyomino(new int[] { 0 }, new int[] { 0 });
		setPrev.add(polyOfSize1);
		if (n > 1) {
			for (int i = 2; i <= n; i++) {
				HashSet<FixedPolyomino> sizeOfi = new HashSet<>();
				for (FixedPolyomino poly : setPrev) {
					poly.addOneCell(sizeOfi);
				}
				setPrev = sizeOfi;
			}
		}
		return setPrev;
	}

	/**
	 * allFreeSizeLess: generates all free polyominoes of size less than
	 * or equal to the argument n by naive algorithm.
	 * @param n the maximum of size
	 * @return A LinkedList of all free polyominoes of size less than or equal to n.
	 */
	static public LinkedList<FreePolyomino> allFreeSizeLess(int n) {
		assert n > 0;
		LinkedList<FreePolyomino> allInList = new LinkedList<>();
		HashSet<FreePolyomino> setPrev = new HashSet<FreePolyomino>();
		FreePolyomino polyOfSize1 = new FreePolyomino(new int[] { 0 }, new int[] { 0 });
		setPrev.add(polyOfSize1);
		allInList.add(polyOfSize1);
		if (n > 1) {
			for (int i = 2; i <= n; i++) {
				HashSet<FreePolyomino> sizeOfi = new HashSet<>();
				for (FreePolyomino poly : setPrev) {
					poly.addOneCell(sizeOfi);
				}
				allInList.addAll(sizeOfi);
				setPrev = sizeOfi;
			}
		}
		return allInList;
	}

	/**
	 * allFreeSizeExact: generates all free polyominoes of size equal to n by naive algorithm.
	 * @param n the size of polyominoes
	 * @return An HashSet of all free polyominoes of size equal exactly to n.
	 */
	static public HashSet<FreePolyomino> allFreeSizeExact(int n) {
		assert n > 0;
		HashSet<FreePolyomino> setPrev = new HashSet<>();
		FreePolyomino polyOfSize1 = new FreePolyomino(new int[] { 0 }, new int[] { 0 });
		setPrev.add(polyOfSize1);
		if (n > 1) {
			for (int i = 2; i <= n; i++) {
				HashSet<FreePolyomino> sizeOfi = new HashSet<>();
				for (FreePolyomino poly : setPrev) {
					poly.addOneCell(sizeOfi);
				}
				setPrev = sizeOfi;
			}
		}
		return setPrev;
	}

	/**
	 * fixedInBox: generates all fixed polyominoes which can be covered by a square of size 
	 * (width * height). By another word, generates all fixed polyominoes of width less than
	 * or equal to argument width, and of height less than or equal to argument height.
	 * @param width the upper limit of width of polyominoes
	 * @param height the upper limit of height of polyominoes
	 * @return A LinkedList of all fixed polyominoes with width <= argument width and height
	 * <= argument height.
	 */
	static public LinkedList<FixedPolyomino> fixedInBox(int width, int height) {
		if (width <=0 || height <=0)
			return null;
		LinkedList<FixedPolyomino> allInList = new LinkedList<>();
		HashSet<FixedPolyomino> setPrev = new HashSet<FixedPolyomino>();
		FixedPolyomino polyOfSize1 = new FixedPolyomino(new int[] { 0 }, new int[] { 0 });
		setPrev.add(polyOfSize1);
		allInList.add(polyOfSize1);
		int sizeMax = width*height;
		if (sizeMax > 1) {
			for (int i = 2; i <= sizeMax; i++) {
				HashSet<FixedPolyomino> sizeOfi = new HashSet<>();
				for (FixedPolyomino poly : setPrev) {
					poly.addOneCell(sizeOfi, width, height);
				}
				for (FixedPolyomino poly : sizeOfi){
					if (poly.width> width && poly.height > height)
						sizeOfi.remove(poly);
				}
				allInList.addAll(sizeOfi);
				setPrev = sizeOfi;
			}
		}
		return allInList;
	}
}
