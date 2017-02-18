package count;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * ClassName: Pair <br/>
 * Function: Puts two integers in one data structure Pair in order to simplify
 * the programming.
 */
class Pair {
	int x, y;

	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object o) {
		Pair p = (Pair) o;
		return this.x == p.x && this.y == p.y;
	}

	public int hashCode() {
		return x * 19 + y * 31;
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}

/**
 * ClassName: Pointpair <br/>
 * Function: Two points form a Pointpair.
 */
class Pointpair {
	Pair point;
	Pair img;

	Pointpair(Pair point, Pair img) {
		this.point = point;
		this.img = img;
	}

	@Override
	public int hashCode() {
		return point.hashCode() + img.hashCode();
	}

	public boolean equals(Object o) {
		Pointpair that = (Pointpair) o;
		return this.point.equals(that.point) && this.img.equals(that.img)
				|| this.point.equals(that.img) && this.img.equals(that.point);
	}

	public String toString() {
		return "[" + point.toString() + " " + img.toString() + "]";
	}
}

public class CountFreePolyomino {
	private static final int NONE = 0, HI = 1, A = 2, RII = 3, RIX = 4, RXI = 5, RXX = 6, RI = 7, RX = 9;
	private static final int R2X = 0, R2I = 1, ADRX = 2, ADRI = 3, HVRII = 4, HVADR2I = 5;
	private static final int UP = 1, RIGHT = 2, DOWN = 4, LEFT = 8;
	/**
	 * ONRING: Indicates this point is on the ring.
	 */
	private static final int ONRING = -2;
	/**
	 * TOUCHRING: Indicates this point is outside of a ring and ouches the ring.
	 */
	private static final int TOUCHRING = -3;
	/**
	 * INSIDENEIGHBOUR: Indicates this untried point is inside of a ring but
	 * doesn't touch the ring.
	 */
	private static final int INSIDENEIGHBOUR = -6;
	/**
	 * OUTSIDENEIGHBOUR: Indicates this untried point is outside of a ring but
	 * doesn't touch the ring.
	 */
	private static final int OUTSIDENEIGHBOUR = -5;
	/**
	 * EXTENDED: Indicates this point is in a polyomino and it is outside of the
	 * ring of this polyomino.
	 */
	private static final int EXTENDED = -4;

	/**
	 * none: counts N'(n), in other words, counts all fixed polyominoes of size
	 * less than n <br/>
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i.
	 */
	private static long[] none(int n) {
		long[] t = new long[n + 1];
		if (n == 0)
			return t;
		Single<Pair> untried = new Single<Pair>(new Pair(n - 1, 1), null);
		Stack<Pair> cells = new Stack<Pair>();
		int[][] matrix = new int[2 * n - 1][n + 1];
		for (int i = 0; i < n; i++)
			matrix[i][1] = -1;
		for (int i = 0; i < 2 * n - 1; i++)
			matrix[i][0] = -1;
		countSymmetry(1, n, cells, untried, matrix, t, NONE);
		return t;
	}

	/**
	 * hx: counts HX'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least HX symmetry.
	 */
	private static long[] hx(int n) {
		long[] t = new long[n + 1];
		long[] tmp = none(n / 2);
		for (int i = 2; i <= n; i += 2) {
			t[i] = tmp[i / 2];
		}
		return t;
	}

	/**
	 * hi: counts HI'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least HI symmetry.
	 */
	private static long[] hi(int n) {
		long[] t = new long[n + 1];
		if (n == 0)
			return t;
		if (n == 1) {
			t[1] = 1;
			return t;
		}
		Single<Pair> untried = new Single<Pair>(new Pair(n - 1, 1), null);
		Stack<Pair> cells = new Stack<Pair>();
		int[][] matrix = new int[2 * n - 1][n + 1];
		for (int i = 0; i < n; i++)
			matrix[i][1] = -1;
		for (int i = 0; i < 2 * n - 1; i++)
			matrix[i][0] = -1;
		countSymmetry(1, n, cells, untried, matrix, t, HI);
		return t;
	}

	/**
	 * hvrxi: counts HVRXI'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least HVRXI symmetry.
	 */
	private static long[] hvrxi(int n) {
		long[] t = new long[n + 1];
		long[] tmp = hi(n / 2);
		for (int i = 2; i <= n; i += 2)
			t[i] = tmp[i / 2];
		return t;
	}

	/**
	 * hvrxx: counts HVRXX'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least HVRXX symmetry.
	 */
	private static long[] hvrxx(int n) {
		long[] t = new long[n + 1];
		long[] tmp = hx(n / 2);
		for (int i = 2; i <= n; i += 2)
			t[i] = tmp[i / 2];
		return t;
	}

	/**
	 * hvadr2x: counts HVADR2X'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least HVADR2X symmetry.
	 */
	private static long[] hvadr2x(int n) {
		long[] t = new long[n + 1];
		long[] tmp = a(n / 4);
		for (int i = 4; i <= n; i += 4)
			t[i] = tmp[i / 4];
		return t;
	}

	/**
	 * a: counts A'(n)
	 * 
	 * @param n:
	 *            the biggest size of the polyominoes that we want to count.
	 * @return an array of type long whose i th entry contains the number of the
	 *         fixed polyominoes of size i with at least A symmetry.
	 */
	private static long[] a(int n) {
		long[] t = new long[n + 1];
		if (n == 0)
			return t;
		if (n == 1) {
			t[1] = 1;
			return t;
		}
		Single<Pair> untried = new Single<Pair>(new Pair(n - 1, n - 1), null);
		Stack<Pair> cells = new Stack<Pair>();
		int[][] matrix = new int[2 * n - 1][2 * n - 1];
		/*
		 * All entries on the diagonal are -1.
		 */
		for (int i = 0; i < n - 1; i++)
			matrix[i][i] = -1;
		for (int i = n - 1; i < 2 * n - 1; i++)
			matrix[i][i - 1] = -1;
		countSymmetry(1, n, cells, untried, matrix, t, A);
		return t;
	}

	/**
	 * rodd: construct necessary parameters for the counting of odd-sized
	 * rotationally symmetric polyominoes.<br/>
	 * 
	 * @param n
	 *            the biggest size of the polyominoes that we want to count
	 * @param composite
	 *            an array of dimension 6*n which contains the number of 6 kinds
	 *            of polyominoes with composite symmetries that cannot be
	 *            counted directly. The number of such polyominoes with RII
	 *            symmetry and an odd size will also be computed in this
	 *            function by modifying the array composite.
	 * @return an array of type long whose 2k+1 th entry is the number of
	 *         polyomioes of size 2k+1 with RII symmetry
	 */
	private static long[] rodd(int n, long[][] composite) {
		long[] t = new long[n + 1];
		if (n == 0)
			return t;
		if (n % 2 == 0)
			n--;
		if (n == 1) {
			t[1] = 1;
			return t;
		}
		int centre = n / 2 + 1;
		Single<Pointpair> untried = new Single<Pointpair>(
				new Pointpair(new Pair(centre + 1, centre), new Pair(centre - 1, centre)), null);
		untried = new Single<Pointpair>(new Pointpair(new Pair(centre, centre + 1), new Pair(centre, centre - 1)),
				untried);
		Stack<Pointpair> cells = new Stack<Pointpair>();
		int[][] matrix = new int[n + 2][n + 2];
		matrix[centre][centre] = -1;
		matrix[centre + 1][centre] = 1;
		matrix[centre - 1][centre] = 1;
		matrix[centre][centre + 1] = 1;
		matrix[centre][centre - 1] = 1;
		extendRodd(3, n, cells, untried, matrix, t, composite);
		return t;
	}

	/**
	 * extendRodd: counts odd-sized rotationally symmetric polyominoes.<br/>
	 * 
	 * @param p
	 *            : the size of the current polyomino
	 * @param n
	 *            : the biggest size of the polyominoes that we want to count
	 * @param cells
	 *            : the points occupied by the current polyomino
	 * @param untried
	 *            : the reachable points adjacent to the current polyomino that
	 *            haven't been tried yet
	 * @param matrix
	 *            : the matrix that stocks both free informations and successor
	 *            informations
	 * @param counts
	 *            : an array of type long whose 2k+1 th entry is the number of
	 *            polyominoes of size 2k+1 with at least RII symmetry.
	 * @param composite:
	 *            see the definition in the function rodd.
	 */
	private static void extendRodd(int p, int n, Stack<Pointpair> cells, Single<Pointpair> untried, int[][] matrix,
			long[] counts, long[][] composite) {
		while (untried != null) {
			Pointpair ppair = untried.val;
			untried = untried.next;
			Pair point = ppair.point;
			cells.push(ppair);
			counts[p]++;
			countComposite(cells, new Pair(n / 2 + 1, n / 2 + 1), composite);
			if (p < n) {
				matrix[point.x][point.y] = -1;
				matrix[ppair.img.x][ppair.img.y] = -1;
				Single<Pointpair> res = untried;
				for (Pointpair neighbour : neighbours(ppair))
					if (matrix[neighbour.point.x][neighbour.point.y] == 0) {
						matrix[neighbour.point.x][neighbour.point.y] = p;
						matrix[neighbour.img.x][neighbour.img.y] = p;
						untried = new Single<Pointpair>(neighbour, untried);
					}
				extendRodd(p + 2, n, cells, untried, matrix, counts, composite);
				while (untried != res) {
					Pointpair tmp = untried.val;
					untried = untried.next;
					matrix[tmp.point.x][tmp.point.y] = 0;
					matrix[tmp.img.x][tmp.img.y] = 0;
				}
			}
			cells.pop();
		}
	}

	/**
	 * rx: construct necessary parameters for the counting even-sized
	 * rotationally symmetric polyominoes with at least RXX or RXI symmetry.
	 * <br/>
	 * 
	 * @param n
	 *            the biggest size of the polyominoes that we want to count
	 * @param composite
	 *            an array of dimension 6*n which contains the number of 6 kinds
	 *            of polyominoes with composite symmetries that cannot be
	 *            counted directly. The number of such polyominoes with RXX or
	 *            RXI symmetry and an even size will also be computed in this
	 *            function by modifying the array composite.
	 * @return an array of dimension 2*n 'rx'. rx[0][2k]=number of polyominoes
	 *         of size 2k with at least RXX symmetry. rx[1][2k]=number of
	 *         polyominoes of size 2k with at least RXI symmetry.
	 */
	private static long[][] rx(int n, long[][] composite) {
		long[][] t = new long[2][n + 1];
		if (n <= 1)
			return t;
		if (n == 2) {
			t[1][2] = 1;
			return t;
		}
		n = n / 2 * 2;
		int centre = (n - 1) / 2;
		Single<Pair> untried = new Single<Pair>(new Pair(centre, centre + 1), null);
		Stack<Pair> cells = new Stack<Pair>();
		int[][] matrix = new int[n][n];
		for (int i = 0; i < matrix.length; i++)
			matrix[i][centre] = -1;
		for (int i = 0; i < centre; i++)
			matrix[i][centre + 1] = -1;
		countRing(2, n, cells, untried, matrix, t, RX, composite);
		return t;
	}

	/**
	 * ri: construct necessary parameters for the counting even-sized
	 * rotationally symmetric polyominoes with at least RII or RIX symmetry.
	 * <br/>
	 * 
	 * @param n
	 *            the biggest size of the polyominoes that we want to count
	 * @param composite
	 *            an array of dimension 6*n which contains the number of 6 kinds
	 *            of polyominoes with composite symmetries that cannot be
	 *            counted directly. The number of such polyominoes with RIX or
	 *            RII symmetry and an even size will also be computed in this
	 *            function by modifying the array composite.
	 * @return an array of dimension 2*n 'ri'. ri[0][2k]=number of polyominoes
	 *         of size 2k with at least RIX symmetry. rx[1][2k]=number of
	 *         polyominoes of size 2k with at least RII symmetry.
	 */
	private static long[][] ri(int n, long[][] composite) {
		long[][] t = new long[2][n + 1];
		if (n <= 1)
			return t;
		if (n == 2) {
			t[0][2] = 1;
			return t;
		}
		n = n / 2 * 2;
		int centre = (n - 1) / 2;
		Single<Pair> untried = new Single<Pair>(new Pair(centre, centre), null);
		Stack<Pair> cells = new Stack<Pair>();
		int[][] matrix = new int[n][n];
		for (int i = 0; i < matrix.length; i++)
			matrix[i][centre - 1] = -1;
		for (int i = 0; i < centre; i++)
			matrix[i][centre] = -1;
		countRing(2, n, cells, untried, matrix, t, RI, composite);
		return t;
	}

	/**
	 * findEndPoints: finds the two endpoints of a given half-ring.<br/>
	 * 
	 * @param matrix
	 *            an two dimensioned array that stocks the informations of the
	 *            half-ring.
	 * @param y_axis
	 *            the minimal ordinate of all points on the half-ring.
	 * @param n
	 *            the width of matrix
	 * @param symmetry
	 *            the symmetry of the ring
	 * @return Pair p. p.x=the abscissa of the left endpoint, p.y=the abscissa
	 *         of the right endpoint. Return null if the half-ring is invalid.
	 */
	private static Pair findEndPoints(int[][] matrix, int y_axis, int n, int symmetry) {
		int xleft = (n - 1) / 2, xright = n - 1;
		if (matrix[xright][y_axis] != ONRING)
			while (true) {
				if (matrix[xright - 1][y_axis] != ONRING)
					xright--;
				else {
					xright--;
					break;
				}
			}
		if (symmetry == RX && matrix[xleft][y_axis + 1] == ONRING)
			while (true) {
				if (xleft >= xright - 1)
					break;
				else if (matrix[xleft + 1][y_axis] == ONRING)
					xleft++;
				else
					break;
			}
		if (xright - xleft == 1 && xleft != (n - 1) / 2)
			return null;
		if (symmetry == RI && xright - xleft > 1) {
			int xl = xleft + 1, xr = xright - 1;
			while (xl <= xr) {
				if (matrix[xl][y_axis] == ONRING && matrix[xr][y_axis] == ONRING)
					return null;
				xl++;
				xr--;
			}
		}
		if (symmetry == RX && matrix[xright][y_axis + 1] == ONRING && xleft == (n - 1) / 2)
			while (true) {
				if (matrix[xright - 1][y_axis] == ONRING)
					xright--;
				else
					break;
			}
		return new Pair(xleft, xright);
	}

	/**
	 * countRing: finds all suitable rings to grow the even-sized polyominoes
	 * with rotational symmetry <br/>
	 * 
	 * @param p
	 *            : the size of the current half-ring*2
	 * @param n
	 *            : the biggest size of the polyominoes that we want to count
	 * @param cells
	 *            : the points occupied by the current half-ring
	 * @param untried
	 *            : the reachable points adjacent to the current half-ring that
	 *            haven't been tried yet
	 * @param matrix
	 *            : the matrix that stocks both free informations and successor
	 *            informations
	 * @param symmetry
	 *            : the symmetry of the polyominoes we want to count. If
	 *            symmetry =RX, we will count RXX'(2k) and RXI'(2k). If
	 *            symmetry=RI, we will count RIX'(2k) and RII'(2k) for all
	 *            1<=k<=n/2
	 * @param composite
	 *            :an array of dimension 6*n which contains the number of 6
	 *            kinds of polyominoes with composite symmetries that cannot be
	 *            counted directly.
	 */
	private static void countRing(int p, int n, Stack<Pair> cells, Single<Pair> untried, int[][] matrix,
			long[][] counts, int symmetry, long[][] composite) {
		int y_axis = (n - 1) / 2 + (symmetry == RI ? 0 : 1);
		/*
		 * the minimal ordinate of all points on the half-ring. If this
		 * half-ring can be extended into a valid ring. Then y_axis is the
		 * ordinate of its axis of symmetry
		 */
		while (untried != null) {
			Pair point = untried.val;
			untried = untried.next;
			if (matrix[point.x][point.y] > 1)
				continue;
			cells.push(point);
			if (p == n + (symmetry == RI ? 2 : 0) && point.y == y_axis) {
				// if the polyomino itself is a ring
				int tmp = matrix[point.x][point.y];
				matrix[point.x][point.y] = ONRING;
				Pair leftRight = findEndPoints(matrix, y_axis, n, symmetry);
				// find the two endpoints of the half-ring
				matrix[point.x][point.y] = tmp;
				if (leftRight != null) {
					// if the half-ring is valid.
					int newsymmetry = 0;
					Single<Pointpair> newuntried = null;
					Stack<Pointpair> newcells = new Stack<Pointpair>();
					int[][] ring = new int[n][n];
					if ((leftRight.y - leftRight.x) % 2 == 1)
						newsymmetry = symmetry == RI ? RIX : RXX;
					else
						newsymmetry = symmetry == RI ? RII : RXI;
					// determine the exact symmetry of the ring (polyomino)
					newuntried = generateParameters(n, cells, newcells, ring, leftRight, newsymmetry);
					// complete the half-ring on a whole ring.
					if (newuntried != null) {
						// if the whole ring is valid
						counts[symmetry - newsymmetry - 3][p - (symmetry == RI ? 2 : 0)]++;
						if (newsymmetry == RII || newsymmetry == RXX)
							// verify if this polyomino has any other composite
							// symmetry
							countComposite(newcells, new Pair((n - 1) / 2, (n - 1) / 2), composite, newsymmetry);
					}
				}
			}
			if (p < n + (symmetry == RI ? 2 : 0)) {
				matrix[point.x][point.y] = ONRING;
				Single<Pair> res = untried;
				if (matrix[point.x + 1][point.y] == 0) {
					untried = new Single<Pair>(new Pair(point.x + 1, point.y), untried);
					matrix[point.x + 1][point.y]++;
				}
				if (matrix[point.x - 1][point.y] == 0) {
					untried = new Single<Pair>(new Pair(point.x - 1, point.y), untried);
					matrix[point.x - 1][point.y]++;
				}
				if (matrix[point.x][point.y + 1] == 0) {
					untried = new Single<Pair>(new Pair(point.x, point.y + 1), untried);
					matrix[point.x][point.y + 1]++;
				}
				if (matrix[point.x][point.y - 1] == 0) {
					untried = new Single<Pair>(new Pair(point.x, point.y - 1), untried);
					matrix[point.x][point.y - 1]++;
				}
				if (point.y == y_axis && !(symmetry == RI && cells.size() == 1)) {
					// if this is a half-ring
					Pair leftRight = findEndPoints(matrix, y_axis, n, symmetry);
					// find the two endpoints of the half-ring
					if (leftRight != null) {
						// if the half-ring is valid.
						Stack<Pointpair> newcells = new Stack<Pointpair>();
						Single<Pointpair> newuntried = null;
						int[][] ring = new int[n][n];
						int newsymmetry = 0;
						if ((leftRight.y - leftRight.x) % 2 == 1)
							newsymmetry = symmetry == RI ? RIX : RXX;
						else
							newsymmetry = symmetry == RI ? RII : RXI;
						// determine the exact symmetry of the ring (polyomino)
						newuntried = generateParameters(n, cells, newcells, ring, leftRight, newsymmetry);
						// complete the half-ring on a whole ring.
						if (newuntried != null) {
							// if the whole ring is valid
							counts[symmetry - newsymmetry - 3][p - ((symmetry == RI) ? 2 : 0)]++;
							if (newsymmetry == RXX || newsymmetry == RII)
								countComposite(newcells, new Pair((n - 1) / 2, (n - 1) / 2), composite, newsymmetry);
							// verify if this polyomino has any other composite
							// symmetry
							extendRing(p + (symmetry == RX ? 2 : 0), n, newcells, newuntried, ring,
									counts[symmetry - newsymmetry - 3], new HashSet<Pointpair>(),
									new HashSet<Pointpair>(), newsymmetry, new Pair((n - 1) / 2, (n - 1) / 2),
									composite);
							// grow polyominoes from the ring
						}
					}
				}
				countRing(p + 2, n, cells, untried, matrix, counts, symmetry, composite);
				while (untried != res) {
					Pair tmp = untried.val;
					untried = untried.next;
					matrix[tmp.x][tmp.y] = 0;
				}
			}
			cells.pop();
			matrix[point.x][point.y] = -3;
		}
	}

	/**
	 * generateParameters: complete a half-ring on a whole ring<br/>
	 * 
	 * @param n
	 *            the biggest size of the polyominoes that we want to count
	 * @param cells
	 *            the points occupied by the half-ring.
	 * @param newcells
	 *            the point-pairs that will be occupied by the ring
	 * @param ring
	 *            the new matrix that stocks free informations and successor
	 *            informations
	 * @param point
	 *            point.x : the abscissa of the left endpoint of the half-ring.
	 *            point.y : the abscissa of the right endpoint of the half-ring
	 * @param symmetry
	 *            the symmetry of the current ring
	 * @return the set of reachable point-pairs (the point-pairs adjacent to the
	 *         ring). Return null if the ring is invalid.
	 */
	private static Single<Pointpair> generateParameters(int n, Stack<Pair> cells, Stack<Pointpair> newcells,
			int[][] ring, Pair point, int symmetry) {
		Single<Pointpair> newuntried = null;
		Pair centre = new Pair((point.x + point.y) / 2, (n - 1) / 2);
		// the centre of the ring.
		Pair newcentre = new Pair((n - 1) / 2, (n - 1) / 2);
		// the new centre of the ring which coincides with the center of the
		// matrix.
		if (symmetry == RXX || symmetry == RXI)
			for (Pair p : cells) {
				p = new Pair(p.x + newcentre.x - centre.x, p.y);
				Pointpair ppair = new Pointpair(p, getImage(p, symmetry, newcentre));
				if (cells.size() != 1 && neighbours(p).contains(ppair.img))
					return null;
				/*
				 * If a point on the ring is adjacent to its image and the size
				 * of the ring is bigger than 2, the ring is invalid.
				 */
				newcells.push(ppair);
				ring[ppair.point.x][ppair.point.y] = ring[ppair.img.x][ppair.img.y] = ONRING;
			}
		if (symmetry == RIX || symmetry == RII)
			for (Pair p : cells) {
				if (p.x == point.y && p.y == newcentre.y)
					continue;
				/*
				 * If the ring has RIX symmetry or RII symmetry, then the image
				 * of the left endpoint of its half ring is exactly the right
				 * endpoint of its half ring. So we should avoid adding this
				 * pair of point twice
				 */
				p = new Pair(p.x + newcentre.x - centre.x, p.y);
				Pair imgp = getImage(p, symmetry, newcentre);
				Pointpair ppair = new Pointpair(p, imgp);
				newcells.push(ppair);
				ring[ppair.point.x][ppair.point.y] = ring[ppair.img.x][ppair.img.y] = ONRING;
			}
		for (Pair tmppoint : cells) {
			tmppoint = new Pair(tmppoint.x + (n - 1) / 2 - centre.x, tmppoint.y);
			int tmp = 0;
			for (Pair neighbour : neighbours(tmppoint))
				if (neighbour.x >= 0 && neighbour.x < ring.length && neighbour.y >= 0 && neighbour.y < ring.length
						&& ring[neighbour.x][neighbour.y] == ONRING)
					tmp++;
			if (tmp != 2 && newcells.size() > 1)
				return null;
		}
		/*
		 * Verify if the ring is valid by examining if each point on the ring
		 * has exactly two neighbors that are also on the ring.
		 */
		if (newcells.size() <= 3) {
			/*
			 * When the size of the ring is smaller than 8, there is no point
			 * inside of the ring.
			 */
			for (Pointpair ppair : newcells) {
				for (Pair pext : neighbours(ppair.point))
					if (ring[pext.x][pext.y] == 0) {
						ring[pext.x][pext.y] = TOUCHRING;
						Pair imgext = getImage(pext, symmetry, newcentre);
						ring[imgext.x][imgext.y] = TOUCHRING;
						newuntried = new Single<Pointpair>(new Pointpair(pext, imgext), newuntried);
					}
			}
		} else {
			/*
			 * When the size of the ring is larger than 8 or equals 8, we need
			 * to divide the set of untried point-pairs into two parts according
			 * to their relative position to the ring. That is to say, we need
			 * to mark in the matrix 'ring' all points adjacent to the ring and
			 * use different colors for the points inside the ring and outside
			 * the ring.
			 */
			int minx = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, miny = Integer.MAX_VALUE, maxy = Integer.MIN_VALUE;
			for (Pointpair ppair : newcells) {
				minx = Math.min(minx, ppair.point.x);
				minx = Math.min(minx, ppair.img.x);
				miny = Math.min(miny, ppair.point.y);
				miny = Math.min(miny, ppair.img.y);
				maxx = Math.max(maxx, ppair.point.x);
				maxx = Math.max(maxx, ppair.img.x);
				maxy = Math.max(maxy, ppair.point.y);
				maxy = Math.max(maxy, ppair.img.y);
			}
			for (int y = miny + 1; y < maxy; y++) {
				boolean inside = false;
				for (int x = minx - 1; x <= maxx + 1; x++) {
					if (ring[x][y] != ONRING) {
						if (!inside) {
							for (Pair neighbour : neighbours(new Pair(x, y)))
								if (ring[neighbour.x][neighbour.y] == ONRING) {
									ring[x][y] = TOUCHRING;
									if (y < newcentre.y || y == newcentre.y
											&& (symmetry == RXX || symmetry == RXI || x <= newcentre.x)) {
										Pair tmp = new Pair(x, y);
										newuntried = new Single<Pointpair>(
												new Pointpair(tmp, getImage(tmp, symmetry, newcentre)), newuntried);
									}
									break;
								}
						} else {
							getMatrixValue(new Pair(x, y), ring);
							if (y < newcentre.y
									|| y == newcentre.y && (symmetry == RXX || symmetry == RXI || x <= newcentre.x))
								for (int i = 1; i <= 8; i *= 2) {
									if (ring[x][y] == i) {
										Pair tmp = new Pair(x, y);
										newuntried = new Single<Pointpair>(
												new Pointpair(tmp, getImage(tmp, symmetry, newcentre)), newuntried);
										break;
									}
								}
						}
					} else if (ring[x - 1][y] != ONRING)
						inside = !inside;
				}
			}
			int[] res = new int[] { miny - 1, maxy + 1, miny, maxy };
			for (int y : res) {
				for (int x = minx - 1; x <= maxx + 1; x++) {
					if (ring[x][y] == 0)
						for (Pair neighbour : neighbours(new Pair(x, y)))
							if (ring[neighbour.x][neighbour.y] == ONRING) {
								ring[x][y] = TOUCHRING;
								if (y < newcentre.y || y == newcentre.y
										&& (symmetry == RXX || symmetry == RXI || x <= newcentre.x)) {
									Pair tmp = new Pair(x, y);
									newuntried = new Single<Pointpair>(
											new Pointpair(tmp, getImage(tmp, symmetry, newcentre)), newuntried);
								}
								break;
							}
				}
			}
		}
		return newuntried;
	}

	private static void getMatrixValue(Pair p, int[][] matrix) {
		if (matrix[p.x][p.y - 1] == ONRING)
			matrix[p.x][p.y] += DOWN;
		if (matrix[p.x][p.y + 1] == ONRING)
			matrix[p.x][p.y] += UP;
		if (matrix[p.x - 1][p.y] == ONRING)
			matrix[p.x][p.y] += LEFT;
		if (matrix[p.x + 1][p.y] == ONRING)
			matrix[p.x][p.y] += RIGHT;
	}

	/**
	 * extendRing: grows polyominoes from a ring. <br/>
	 */
	private static void extendRing(int p, int n, Stack<Pointpair> cells, Single<Pointpair> untried, int[][] matrix,
			long[] counts, HashSet<Pointpair> touchPoint, HashSet<Pointpair> pairTouchPoint, int symmetry, Pair centre,
			long[][] composite) {
		while (untried != null) {
			Pointpair ppair = untried.val;
			untried = untried.next;
			Pair point = ppair.point;
			int potentialMatrixValue = 0;
			int potentialImageMatrixValue = 0;
			if (symmetry == RII && point.equals(centre))
				continue;
			if (neighbours(point).contains(ppair.img))
				continue;
			if (matrix[point.x][point.y] == INSIDENEIGHBOUR) {
				Pair neighbour = cells.peek().point;
				if (matrix[neighbour.x][neighbour.y] > 0) {
					Pair imageOfNeighbour = getImage(neighbour, symmetry, centre);
					potentialMatrixValue = -(neighbour.x * 100 + neighbour.y + 10);
					potentialImageMatrixValue = -(imageOfNeighbour.x * 100 + imageOfNeighbour.y + 10);
				} else if (matrix[neighbour.x][neighbour.y] < -10) {
					Pair imageOfNeighbour = getImage(neighbour, symmetry, centre);
					potentialMatrixValue = matrix[neighbour.x][neighbour.y];
					potentialMatrixValue = matrix[imageOfNeighbour.x][imageOfNeighbour.y];
				}
			}
			if (symmetry == RXX && matrix[centre.x][centre.y] != ONRING
					&& (point.equals(new Pair(centre.x + 1, centre.y))
							|| ppair.img.equals(new Pair(centre.x + 1, centre.y)))
					&& cells.contains(new Pointpair(centre, getImage(centre, symmetry, centre)))) {
				continue;
			}
			if (symmetry == RXX && matrix[centre.x][centre.y] != ONRING
					&& cells.contains(new Pointpair(new Pair(centre.x + 1, centre.y), new Pair(centre.x, centre.y + 1)))
					&& (ppair.point.equals(centre) || ppair.img.equals(centre))) {
				continue;
			}
			if (potentialMatrixValue != 0 && !isLegal(ppair, matrix, potentialMatrixValue, potentialImageMatrixValue))
				continue;
			if (matrix[point.x][point.y] <= 0 || isLegal(ppair, matrix, touchPoint, pairTouchPoint, symmetry, centre))
				cells.push(ppair);
			else
				continue;
			counts[p]++;
			countComposite(cells, centre, composite, symmetry);
			if (p < n) {
				if (matrix[point.x][point.y] <= 0 && potentialMatrixValue == 0) {
					matrix[point.x][point.y] = EXTENDED;
					matrix[ppair.img.x][ppair.img.y] = EXTENDED;
				}
				Single<Pointpair> res = untried;
				for (Pair neighbour : neighbours(point))
					if (matrix[neighbour.x][neighbour.y] == 0) {
						Pair imageOfNeighbour = getImage(neighbour, symmetry, centre);
						if (matrix[point.x][point.y] > 0 || matrix[point.x][point.y] < -10) {
							matrix[neighbour.x][neighbour.y] = INSIDENEIGHBOUR;
							matrix[imageOfNeighbour.x][imageOfNeighbour.y] = INSIDENEIGHBOUR;
						} else {
							matrix[neighbour.x][neighbour.y] = OUTSIDENEIGHBOUR;
							matrix[imageOfNeighbour.x][imageOfNeighbour.y] = OUTSIDENEIGHBOUR;
						}
						untried = new Single<Pointpair>(new Pointpair(neighbour, imageOfNeighbour), untried);
					}
				extendRing(p + 2, n, cells, untried, matrix, counts, touchPoint, pairTouchPoint, symmetry, centre,
						composite);
				while (untried != res) {
					Pointpair tmp = untried.val;
					untried = untried.next;
					matrix[tmp.point.x][tmp.point.y] = 0;
					matrix[tmp.img.x][tmp.img.y] = 0;
				}
			}
			cells.pop();
			if (matrix[point.x][point.y] > 0) {
				if (touchPoint.contains(ppair))
					touchPoint.remove(ppair);
				else if (pairTouchPoint.contains(ppair)) {
					pairTouchPoint.remove(ppair);
					for (Pointpair ptmp : neighbours(ppair)) {
						if (pairTouchPoint.contains(ptmp)) {
							pairTouchPoint.remove(ptmp);
							touchPoint.add(ptmp);
							break;
						}
					}
				}
			} else if (matrix[point.x][point.y] > -10) {
				matrix[point.x][point.y] = OUTSIDENEIGHBOUR;
				matrix[ppair.img.x][ppair.img.y] = OUTSIDENEIGHBOUR;
			} else {
				matrix[point.x][point.y] = INSIDENEIGHBOUR;
				matrix[ppair.img.x][ppair.img.y] = INSIDENEIGHBOUR;
			}
		}
	}

	private static Pair getImage(Pair point, int symmetry, Pair centre) {
		if (symmetry == RII)
			return new Pair(2 * centre.x - point.x, 2 * centre.y - point.y);
		else if (symmetry == RIX)
			return new Pair(2 * centre.x - point.x + 1, 2 * centre.y - point.y);
		else if (symmetry == RXI)
			return new Pair(2 * centre.x - point.x, 2 * centre.y - point.y + 1);
		else if (symmetry == RXX)
			return new Pair(2 * centre.x - point.x + 1, 2 * centre.y - point.y + 1);
		else
			throw new IllegalArgumentException();
	}

	private static LinkedList<Pair> neighbours(Pair point) {
		LinkedList<Pair> l = new LinkedList<Pair>();
		l.add(new Pair(point.x + 1, point.y));
		l.add(new Pair(point.x - 1, point.y));
		l.add(new Pair(point.x, point.y + 1));
		l.add(new Pair(point.x, point.y - 1));
		return l;
	}

	private static LinkedList<Pointpair> neighbours(Pointpair ppair) {
		LinkedList<Pointpair> l = new LinkedList<Pointpair>();
		l.add(new Pointpair(new Pair(ppair.point.x + 1, ppair.point.y), new Pair(ppair.img.x - 1, ppair.img.y)));
		l.add(new Pointpair(new Pair(ppair.point.x - 1, ppair.point.y), new Pair(ppair.img.x + 1, ppair.img.y)));
		l.add(new Pointpair(new Pair(ppair.point.x, ppair.point.y + 1), new Pair(ppair.img.x, ppair.img.y - 1)));
		l.add(new Pointpair(new Pair(ppair.point.x, ppair.point.y - 1), new Pair(ppair.img.x, ppair.img.y + 1)));
		return l;
	}

	/**
	 * isLegal: checks if adding an inner point in the current polyomino will
	 * destroy its ring. <br/>
	 */
	private static boolean isLegal(Pointpair ppair, int[][] matrix, int potentialMatrixValue,
			int potentialImageMatrixValue) {
		Pair point = ppair.point;
		matrix[point.x][point.y] = potentialMatrixValue;
		matrix[ppair.img.x][ppair.img.y] = potentialImageMatrixValue;
		for (Pair neighbour : neighbours(point))
			if (matrix[neighbour.x][neighbour.y] < -10 && matrix[neighbour.x][neighbour.y] != potentialMatrixValue) {
				matrix[point.x][point.y] = INSIDENEIGHBOUR;
				matrix[ppair.img.x][ppair.img.y] = INSIDENEIGHBOUR;
				return false;
			}
		return true;
	}

	/**
	 * isLegal: checks if adding an inner point in the current polyomino will
	 * destroy its ring. <br/>
	 */
	private static boolean isLegal(Pointpair ppair, int[][] matrix, HashSet<Pointpair> touchPoint,
			HashSet<Pointpair> pairTouchPoint, int symmetry, Pair centre) {
		Pair point = ppair.point;
		boolean connectedAtOneEdge = false;
		for (int i = 1; i <= 8; i *= 2)
			if (matrix[point.x][point.y] == i) {
				connectedAtOneEdge = true;
				break;
			}
		if (!connectedAtOneEdge)
			return false;
		boolean adjacentToTouchPoint = false;
		Pair potentialNeighbourTouchPoint = null;
		for (Pointpair neighbour : neighbours(ppair))
			if (matrix[neighbour.point.x][neighbour.point.y] > 0) {
				if (touchPoint.contains(neighbour)) {
					if (matrix[neighbour.point.x][neighbour.point.y] == matrix[point.x][point.y])
						if (adjacentToTouchPoint)
							return false;
						else {
							adjacentToTouchPoint = true;
							potentialNeighbourTouchPoint = neighbour.point;
						}
					else
						return false;
				}
				if (pairTouchPoint.contains(neighbour))
					return false;
			}
		for (Pair neighbour : neighbours(point))
			if (matrix[neighbour.x][neighbour.y] < -10)
				if (potentialNeighbourTouchPoint == null)
					return false;
				else {
					Pair originOfThisNeighbour;
					int originx = -matrix[potentialNeighbourTouchPoint.x][potentialNeighbourTouchPoint.y] / 100;
					int originy = -matrix[potentialNeighbourTouchPoint.x][potentialNeighbourTouchPoint.y] % 100 - 10;
					originOfThisNeighbour = new Pair(originx, originy);
					if (!originOfThisNeighbour.equals(potentialNeighbourTouchPoint))
						return false;
				}
		if (potentialNeighbourTouchPoint == null)
			touchPoint.add(ppair);
		else {
			Pair imageOfNeighbourTouchPoint = getImage(potentialNeighbourTouchPoint, symmetry, centre);
			Pointpair NeighbourTouchPointPair = new Pointpair(potentialNeighbourTouchPoint, imageOfNeighbourTouchPoint);
			touchPoint.remove(NeighbourTouchPointPair);
			pairTouchPoint.add(NeighbourTouchPointPair);
			pairTouchPoint.add(ppair);
		}
		return true;
	}

	/**
	 * countSymmetry: counts the polyominoes with A,HX,HI symmetry.
	 */
	private static void countSymmetry(int p, int n, Stack<Pair> cells, Single<Pair> untried, int[][] matrix,
			long[] counts, int symmetry) {
		int memop = p;
		while (untried != null) {
			p = memop;
			Pair point = untried.val;
			untried = untried.next;
			if (symmetry == HI && point.y != 1 || symmetry == A && point.x != point.y)
				p++;
			if (p > n)
				continue;
			cells.push(point);
			counts[p]++;
			if (p < n) {
				matrix[point.x][point.y] = -1;
				Single<Pair> res = untried;
				if (matrix[point.x + 1][point.y] == 0) {
					untried = new Single<Pair>(new Pair(point.x + 1, point.y), untried);
					matrix[point.x + 1][point.y] = p;
				}
				if (matrix[point.x - 1][point.y] == 0) {
					untried = new Single<Pair>(new Pair(point.x - 1, point.y), untried);
					matrix[point.x - 1][point.y] = p;
				}
				if (matrix[point.x][point.y + 1] == 0) {
					untried = new Single<Pair>(new Pair(point.x, point.y + 1), untried);
					matrix[point.x][point.y + 1] = p;
				}
				if (matrix[point.x][point.y - 1] == 0) {
					untried = new Single<Pair>(new Pair(point.x, point.y - 1), untried);
					matrix[point.x][point.y - 1] = p;
				}
				countSymmetry(p + 1, n, cells, untried, matrix, counts, symmetry);
				while (untried != res) {
					Pair tmp = untried.val;
					untried = untried.next;
					matrix[tmp.x][tmp.y] = 0;
				}
			}
			cells.pop();
		}
	}

	/**
	 * countComposite: verifies if a given odd-sized polyomino has any composite
	 * symmetry
	 */
	private static void countComposite(Stack<Pointpair> cells, Pair centre, long[][] composite) {
		HashSet<Pointpair> hst = new HashSet<Pointpair>();
		hst.addAll(cells);
		boolean r2i = true, adri = true, hvrii = true, hvadr2i = true;
		if (r2i)
			for (Pointpair p : cells)
				if (!hst.contains(rotation2(p, centre, RII))) {
					r2i = false;
					break;
				}
		for (Pointpair p : cells)
			if (!hst.contains(imgDiag(p, centre))) {
				adri = false;
				break;
			}
		for (Pointpair p : cells)
			if (!hst.contains(imgX(p, centre))) {
				hvrii = false;
				break;
			}
		hvadr2i = adri && hvrii;
		if (r2i)
			composite[R2I][cells.size() * 2 + 1]++;
		if (adri)
			composite[ADRI][cells.size() * 2 + 1]++;
		if (hvrii)
			composite[HVRII][cells.size() * 2 + 1]++;
		if (hvadr2i)
			composite[HVADR2I][cells.size() * 2 + 1]++;
	}

	/**
	 * countComposite: verifies if a given even-sized polyomino has any
	 * composite symmetry
	 */
	private static boolean countComposite(Stack<Pointpair> cells, Pair centre, long[][] composite, int symmetry) {
		HashSet<Pointpair> hst = new HashSet<Pointpair>();
		hst.addAll(cells);
		boolean r2x = true, r2i = true, adrx = true, adri = true, hvrii = true, hvadr2i = true;
		if (symmetry != RII)
			r2i = adri = hvrii = hvadr2i = false;
		if (symmetry != RXX)
			r2x = adrx = false;
		if (r2i || r2x)
			for (Pointpair ppair : cells) {
				if (!hst.contains(rotation2(ppair, centre, symmetry))) {
					r2i = r2x = false;
					break;
				}
			}
		if (adrx || adri)
			for (Pointpair ppair : cells) {
				if (!hst.contains(imgDiag(ppair, centre))) {
					adrx = adri = false;
					break;
				}
			}
		if (hvrii) {
			for (Pointpair ppair : cells) {
				if (!hst.contains(imgX(ppair, centre))) {
					hvrii = false;
					break;
				}
			}
		}
		hvadr2i = hvrii && r2i;
		if (r2x)
			composite[R2X][cells.size() * 2]++;
		if (r2i)
			composite[R2I][cells.size() * 2]++;
		if (adrx)
			composite[ADRX][cells.size() * 2]++;
		if (adri)
			composite[ADRI][cells.size() * 2]++;
		if (hvrii)
			composite[HVRII][cells.size() * 2]++;
		if (hvadr2i)
			composite[HVADR2I][cells.size() * 2]++;
		return false;
	}

	/**
	 * count: prints the results of the counting
	 */
	public static void count(int n) {
		long[][] t = new long[6][n + 1];
		if (n == 0)
			return;
		for (int i = 0; i < 6; i++)
			t[i][1] = 1;
		t[R2X][1] = 0;
		t[ADRX][2] = 0;
		if (n == 1)
			return;
		long[] h_x = hx(n);
		long[] h_i = hi(n);
		long[] a_d = a(n);
		long[] r_odd = rodd(n, t);
		long[][] r_x = rx(n, t);
		long[][] r_i = ri(n, t);
		long[] r_hvrxx = hvrxx(n);
		long[] r_hvrxi = hvrxi(n);
		long[] r_hvadr2x = hvadr2x(n);
		long[] axis2 = new long[n + 1], diag2 = new long[n + 1], r2 = new long[n + 1], r = new long[n + 1],
				axis = new long[n + 1], all = new long[n + 1];
		for (int i = 1; i <= n; i++) {
			all[i] = r_hvadr2x[i] + t[HVADR2I][i];
			t[HVRII][i] = (t[HVRII][i] - t[HVADR2I][i]) / 2;
			r_hvrxx[i] = (r_hvrxx[i] - r_hvadr2x[i]) / 2;
			t[R2I][i] = (t[R2I][i] - t[HVADR2I][i]) / 2;
			t[R2X][i] = (t[R2X][i] - r_hvadr2x[i]) / 2;
			t[ADRX][i] = (t[ADRX][i] - r_hvadr2x[i]) / 2;
			t[ADRI][i] = (t[ADRI][i] - t[HVADR2I][i]) / 2;
			r_x[0][i] = (r_x[0][i] - 2 * (t[ADRX][i] + t[R2X][i] + r_hvrxx[i]) - r_hvadr2x[i]) / 4;
			r_x[1][i] = (r_x[1][i] - r_hvrxi[i]) / 2;
			r_i[0][i] = (r_i[0][i] - r_hvrxi[i]) / 2;
			if (r_i[1][i] != 0)
				r_i[1][i] = (r_i[1][i] - 2 * (t[ADRI][i] + t[R2I][i] + t[HVRII][i]) - t[HVADR2I][i]) / 4;
			else
				r_odd[i] = (r_odd[i] - 2 * (t[ADRI][i] + t[R2I][i] + t[HVRII][i]) - t[HVADR2I][i]) / 4;
			a_d[i] = (a_d[i] - all[i] - 2 * t[ADRI][i] - 2 * t[ADRX][i]) / 2;
			h_x[i] = (h_x[i] - 2 * r_hvrxx[i] - r_hvrxi[i] - r_hvadr2x[i]) / 2;
			h_i[i] = (h_i[i] - 2 * t[HVRII][i] - r_hvrxi[i] - t[HVADR2I][i]) / 2;
			axis2[i] = r_hvrxx[i] + t[HVRII][i] + r_hvrxi[i];
			diag2[i] = t[ADRX][i] + t[ADRI][i];
			r2[i] = t[R2X][i] + t[R2I][i];
			axis[i] = (h_x[i] + h_i[i]);
			r[i] = r_x[0][i] + r_x[1][i] + r_i[1][i] + r_odd[i];
		}
		/*
		 * long[][] tmp=new long[16][25]; for(int i=0;i<25;i++){ tmp[0][i]=i; }
		 * tmp[1]=h_x; tmp[2]=h_i; tmp[3]=a_d; tmp[4]=r_x[0]; tmp[5]=r_x[1];
		 * tmp[6]=r_i[1]; for(int i=0;i<25;i++) tmp[6][i]+=r_odd[i];
		 * tmp[7]=t[R2X]; tmp[8]=t[R2I]; tmp[9]=t[ADRX]; tmp[10]=t[ADRI];
		 * tmp[11]=r_hvrxx; tmp[12]=r_hvrxi; tmp[13]=t[HVRII];
		 * tmp[14]=t[HVADR2I]; tmp[15]=r_hvadr2x; for(int j=1;j<25;j++){ for(int
		 * i=0;i<15;i++){ System.out.print(tmp[i][j]+" &"); }
		 * System.out.println(tmp[15][j]+"\\\\"); }
		 */
		System.out.println("HX : ");
		afficher(h_x);
		System.out.println("HI : ");
		afficher(h_i);
		System.out.println("VX : ");
		afficher(h_x);
		System.out.println("VI : ");
		afficher(h_i);
		System.out.println("A : ");
		afficher(a_d);
		System.out.println("D : ");
		afficher(a_d);
		System.out.println("RODD : ");
		afficher(r_odd);
		System.out.println("RXX : ");
		afficher(r_x[0]);
		System.out.println("RXI : ");
		afficher(r_x[1]);
		System.out.println("RIX : ");
		afficher(r_i[0]);
		System.out.println("RII : ");
		afficher(r_i[1]);
		System.out.println("R2X : ");
		afficher(t[R2X]);
		System.out.println("R2I : ");
		afficher(t[R2I]);
		System.out.println("ADRX : ");
		afficher(t[ADRX]);
		System.out.println("ADRI : ");
		afficher(t[ADRI]);
		System.out.println("HVRXX : ");
		afficher(r_hvrxx);
		System.out.println("HVRXI : ");
		afficher(r_hvrxi);
		System.out.println("HVRIX : ");
		afficher(r_hvrxi);
		System.out.println("HVRII : ");
		afficher(t[HVRII]);
		System.out.println("HVADR2I : ");
		afficher(t[HVADR2I]);
		System.out.println("HVADR2X : ");
		afficher(r_hvadr2x);

		System.out.println("All : ");
		afficher(all);
		System.out.println("Axis2 : ");
		afficher(axis2);
		System.out.println("Rotate2 : ");
		afficher(r2);
		System.out.println("Diag2 : ");
		afficher(diag2);
		System.out.println("Axis : ");
		afficher(axis);
		System.out.println("Rotate : ");
		afficher(r);
		System.out.println("diag : ");
		afficher(a_d);
	}

	private static Pointpair imgX(Pointpair p, Pair centre) {
		return new Pointpair(new Pair(p.point.x, 2 * centre.y - p.point.y), new Pair(p.img.x, 2 * centre.y - p.img.y));
	}

	private static Pointpair imgDiag(Pointpair p, Pair centre) {
		return new Pointpair(new Pair(p.point.y + centre.x - centre.y, p.point.x - centre.x + centre.y),
				new Pair(p.img.y + centre.x - centre.y, p.img.x - centre.x + centre.y));
	}

	private static Pointpair rotation2(Pointpair p, Pair centre, int symmetry) {
		if (symmetry == RII)
			return new Pointpair(new Pair(centre.x + p.point.y - centre.y, centre.y - p.point.x + centre.x),
					new Pair(centre.x + p.img.y - centre.y, centre.y - p.img.x + centre.x));
		else if (symmetry == RXX)
			return new Pointpair(new Pair(centre.x + p.point.y - centre.y, centre.y - p.point.x + centre.x + 1),
					new Pair(centre.x + p.img.y - centre.y, centre.y - p.img.x + centre.x + 1));
		else
			throw new IllegalArgumentException();
	}

	private static void afficher(long[] a) {
		for (int i = 1; i < a.length; i++){
			if (i<8)	System.out.format("%1d | ", a[i]);
			else if (i<11)	System.out.format("%2d | ", a[i]);
			else if (i<14)	System.out.format("%3d | ", a[i]);
			else if (i<18)	System.out.format("%4d | ", a[i]);
			else if (i<21)	System.out.format("%5d | ", a[i]);
			else	System.out.format("%6d | ", a[i]);
		}
		System.out.println();
	}
}
