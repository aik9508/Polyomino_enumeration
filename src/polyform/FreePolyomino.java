package polyform;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class inherits the abstract class Polyform. This class represent free polyominoes.
 *
 */
public class FreePolyomino extends Polyform {
	private static final int SCALE = 0;
	static final String[] symmetryLetter = {"N","X","R","R2","D","X","DR","X","V","X","X","X","X",
			"X","X","X","H","X","X","X","X","X","X","X","X","X","HVR","X","X","X","X","HVDR2"};
	/**
	 * multiple: the number of fixed polyominoes associated with the free polyomino.
	 */
	static final int[] multiple = {8,0,4,2,4,0,2,0,4,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,2,0,0,0,0,1};
	/**
	 * symmetry: the symmetry of free polyomino, represented by a 8-bits byte type number.
	 */
	private byte symmetry;
	//static int count;

	/**
	 * constructor of FreePolyomino
	 */
	public FreePolyomino() {
		size = 0;
		width = height = 0;
		symmetry = 0;
		coords = null;
	}

	/**
	 * Constructor of free polyomino
	 * @param size the number of cells 
	 * @param width the width of polyomino
	 * @param height the height of polyomino
	 * @param coords the binary array of this polyomino
	 */
	public FreePolyomino(int size, int width, int height, short[] coords) {
		this.size = size;	
		this.width = Math.min(width, height);
		this.height = Math.max(width, height);
		this.coords = new short[this.width];
		this.symmetry = calculSymmetry(coords, width, height, this.coords);
	}

	/**
	 * calculSymmetry: This function takes a free polyomino and determines its default representation
	 * by choose the default binary array, and calculates the symmetry at the same time.
	 * @param xdir the coords before the correction.
	 * @param width the width before the correction
	 * @param height the height before the correction
	 * @param coords the correct coords 
	 * @return the symmetry
	 */
	private static byte calculSymmetry(short[] xdir, int width, int height, short[] coords){
		assert (width == xdir.length);
		byte symmetry = 0;
		// Initialize 4 arrays.
		short[] xinv = new short[width], ydir = new short[height], yinv = new short[height];
		for (int i = 0; i < width; i++)
			xinv[i] = inverseshort(xdir[i], height);
		for (int j = 0; j < height; j++) {
			for (int i = width - 1; i >= 0; i--) {
				short xio = xdir[i];
				short xi = (short) (xio >> j & 1);
				ydir[j] <<= 1;
				if (xi == 1)
					ydir[j] += xi;
			}
		}
		for (int j = 0; j < height; j++)
			yinv[j] = inverseshort(ydir[j], width);
		// If width<height, choose from xdir and xinv.
		if (width < height) {
			determineCoords(xdir, xinv, ydir, yinv);
			symmetry = symmetry(xdir, ydir, yinv);
		}
		else if (height < width){
			short[] tmp = ydir;
			ydir = xdir;
			xdir = tmp;
			tmp = xinv;
			xinv = yinv;
			yinv = tmp;
			determineCoords(xdir, xinv, ydir, yinv);
			symmetry = symmetry(xdir, ydir, yinv);
		}
		else {
			determineCoords(xdir, xinv, ydir, yinv);
			symmetry = symmetry(xdir, ydir, yinv);
		}
		for (int i=0;i<xdir.length;i++)
			coords[i] = xdir[i];
		return symmetry;
	}

	/**
	 * determineCoords: chooses the default binary array of a free polyomino.
	 */
	private static void determineCoords(short[] xdir, short[] xinv, short[] ydir, short[] yinv) {
		int width = xdir.length, height = ydir.length;
		if (width != height){
			boolean inv_xdir = determineDirection(xdir);
			boolean inv_xinv = determineDirection(xinv);
			boolean choose_xdir = chooseBetween(xdir, xinv);
			if (!inv_xdir && choose_xdir){
				swapArrays(ydir, yinv);
			}
			else if (inv_xinv && !choose_xdir){
				inverseArray(yinv);
				inverseArray(ydir);
			}
			else if (!inv_xinv && !choose_xdir){
				inverseArray(yinv);
				inverseArray(ydir);
				swapArrays(ydir, yinv);
			}
		}
		else {// width = height
			short[] xdir_origin = xdir.clone(),
					ydir_origin = ydir.clone(),
					xinv_origin = xinv.clone(),
					yinv_origin = yinv.clone();
			boolean inv_xdir = determineDirection(xdir);
			boolean inv_xinv = determineDirection(xinv);
			boolean inv_ydir = determineDirection(ydir);
			boolean inv_yinv = determineDirection(yinv);
			boolean choose_xdir = chooseBetween(xdir, xinv);
			boolean choose_ydir = chooseBetween(ydir, yinv);
			boolean choose_x = chooseBetween(xdir, ydir);
			if (choose_x){
				if (choose_xdir){
					if (inv_xdir){
						copyArray(ydir, ydir_origin);
						copyArray(yinv, yinv_origin);
					}
					else {
						copyArray(ydir, yinv_origin);
						copyArray(yinv, ydir_origin);
					}
				}
				else {
					if (inv_xinv){
						inverseArray(ydir_origin);
						copyArray(ydir, ydir_origin);
						inverseArray(yinv_origin);
						copyArray(yinv, yinv_origin);
					}
					else {
						inverseArray(yinv_origin);
						copyArray(ydir, yinv_origin);
						inverseArray(ydir_origin);
						copyArray(yinv, ydir_origin);
					}
				}
			}
			else {
				if (choose_ydir){
					if (inv_ydir){
						copyArray(ydir, xdir_origin);
						copyArray(yinv, xinv_origin);
					}
					else {
						copyArray(ydir, xinv_origin);
						copyArray(yinv, xdir_origin);
					}
				}
				else {
					if (inv_yinv){
						inverseArray(xdir_origin);
						copyArray(ydir, xdir_origin);
						inverseArray(xinv_origin);
						copyArray(yinv, xinv_origin);
					}
					else {
						inverseArray(xinv_origin);
						copyArray(ydir, xinv_origin);
						inverseArray(xdir_origin);
						copyArray(yinv, xdir_origin);
					}
				}
			}
		}
	}
	
	/**
	 * copyArray: copies the contents of t2 to t1.
	 */
	private static void copyArray(short[] t1, short[] t2) {
		for (int i=0;i<t1.length;i++)
			t1[i] = t2[i];
	}
	
	/**
	 * symmetry: calculates the symmetry.
	 */
	private static byte symmetry(short[] xdir, short[] ydir, short[] yinv) {
		byte symmetry = 0;
		int width = xdir.length, height = ydir.length;
		int i = 0;
		boolean yes;
		for (i = 0, yes = true; i < height / 2; i++) {// Test 'H'
			if (ydir[i] != ydir[height - 1 - i]) {
				yes = false;
				break;
			}
		}
		if (yes)
			symmetry += (1 << 4);
		for (i = 0, yes = true; i < width / 2; i++) {// Test 'V'
			if (xdir[i] != xdir[width - 1 - i]) {
				yes = false;
				break;
			}
		}
		if (yes)
			symmetry += (1 << 3);
		// width=height is the necessary condition to have symmetry 'A', 'D' and
		// 'R2'.
		if (width == height) {
			for (i = 0, yes = true; i < width; i++) {// Test 'D'
					if (yinv[height - 1 - i] != xdir[i]) {
						yes = false;
						break;
					}
				}
				if (yes)
					symmetry += (1 << 2);
			else {
				for (i = 0, yes = true; i < width; i++) {// Test 'A'
				if (xdir[i] != ydir[i]) {
					yes = false;
					break;
				}
			}
			if (yes){
				symmetry += (1 << 2);
			}
			}
			for (i = 0, yes = true; i < width; i++) {// Test 'R2'
				if (xdir[i] != yinv[i]) {
					yes = false;
					break;
				}
			}
			if (yes)
				symmetry += 1;
		}
		for (i = 0, yes = true; i < height; i++) {// Test 'R'
			if (ydir[i] != yinv[height - i - 1]) {
				yes = false;
				break;
			}
		}
		if (yes)
			symmetry += (1 << 1);
		return symmetry;
	}

	/**
	 * Constructor of free polyomino.
	 * @param x x-coordinates of all cells
	 * @param y y-coordinates of all cells
	 */
	public FreePolyomino(int[] x, int[] y) {
		assert x.length > 0;
		assert x.length == y.length;
		size = x.length;
		int xmin = min(x), ymin = min(y);
		int[] xs = new int[size], ys = new int[size];
		for (int i = 0; i < size; i++) {
			xs[i] = x[i] - xmin;
			ys[i] = y[i] - ymin;
		}
		int lx = max(xs) + 1, ly = max(ys) + 1;
		width = Math.min(lx, ly);
		height = Math.max(lx, ly);
		short[] xdir = new short[lx];
		for (int i = 0; i < size; i++) {
			xdir[xs[i]] += (1 << ys[i]);
		}
		coords = new short[this.width];
		symmetry = calculSymmetry(xdir, width, height, coords);
	}

	/**
	 * Constructor of free polyomino
	 * @param str coordinates of all cells of polyomino in the form of String
	 */
	public FreePolyomino(String str) {
		LinkedList<String> l = new LinkedList<String>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			l.add(m.group());
		}
		int lsize = l.size();
		assert lsize % 2 == 0;
		size = lsize / 2;
		int[] xs = new int[size], ys = new int[size];
		int index = 0;
		for (String s : l) {
			int sToi = Integer.parseInt(s);
			if (index % 2 == 0)
				xs[index / 2] = sToi;
			else
				ys[index / 2] = sToi;
			index++;
		}
		int xmin = min(xs), ymin = min(ys);
		for (int i = 0; i < size; i++) {
			xs[i] -= xmin;
			ys[i] -= ymin;
		}
		int lx = max(xs) + 1, ly = max(ys) + 1;
		width = Math.min(lx, ly);
		height = Math.max(lx, ly);
		short[] xdir = new short[lx];
		for (int i = 0; i < size; i++)
			xdir[xs[i]] += (1 << ys[i]);
		coords = new short[this.width];
		symmetry = calculSymmetry(xdir, width, height, coords);
	}

	@Override
	public int[][] fullCoords() {
		int[][] truecoords = new int[2][size];
		int count = 0;
		for (int i = 0; i < width; i++) {
			int ys = coords[i];
			int yposition = 0;
			while (ys > 0) {
				if ((ys & 1) == 1) {
					truecoords[0][count] = i;
					truecoords[1][count] = yposition;
					count++;
				}
				ys >>= 1;
				yposition++;
			}
		}
		return truecoords;
	}

	@Override
	public FreePolyomino translation(int dx, int dy) {
		FreePolyomino transformed = new FreePolyomino();
		transformed.size = this.size;
		transformed.width = this.width;
		transformed.height = this.height;
		transformed.coords = Arrays.copyOf(this.coords, this.width);
		transformed.symmetry = this.symmetry;
		return transformed;
	}

	@Override
	public Polyform rotation(int angle) {
		FreePolyomino transformed = new FreePolyomino();
		transformed.size = this.size;
		transformed.width = this.width;
		transformed.height = this.height;
		transformed.coords = Arrays.copyOf(this.coords, this.width);
		transformed.symmetry = this.symmetry;
		return transformed;
	}

	@Override
	public Polyform reflection(char HV) {
		FreePolyomino transformed = new FreePolyomino();
		transformed.size = this.size;
		transformed.width = this.width;
		transformed.height = this.height;
		transformed.coords = Arrays.copyOf(this.coords, this.width);
		transformed.symmetry = this.symmetry;
		return transformed;
	}

	/**
	 * The gettor of symmetry.
	 * @return the symmetry of this polyomino in number
	 */
	public byte getSymmetry() {
		return this.symmetry;
	}
	
	/**
	 * The gettor of symmetry.
	 * @return the symmetry of this polyomino in String
	 */
	public String getSymmetryLetter() {
		return symmetryLetter[symmetry];
	}

	/**
	 * addOneCell: This method tries all attachable cells of this polyomino and attach them one by one
	 * to the polyomino to generat polyominoes of size+1.
	 * @param hst the HashSet of found polyominoes
	 */
	public void addOneCell(HashSet<FreePolyomino> hst) {
		boolean[][] addedCells = new boolean[width+2][height+2];
		for (int i = 0; i < width; i++) {
			short ys = coords[i];
			for (int j = 0; ys != 0; j++) {
				if ((ys & 1) == 1)
					add(i, j, hst, addedCells);
				ys >>= 1;
			}
		}
	}

	/**
	 * add: try all attachable cells around the cell (x, y)
	 */
	private void add(int x, int y, HashSet<FreePolyomino> hst, boolean[][] addedCells) {
		if ((y == 0 || (coords[x] & (1 << (y - 1))) == 0) && (symmetry & 18) == 0) {
			if (!addedCells[x + 1][y]) {
				addedCells[x + 1][y] = true;
				addDown(x, y, hst);
			}
		}
		if ((x == 0 || (coords[x - 1] & (1 << y)) == 0) && (symmetry & 15) == 0) {
			if (!addedCells[x][y + 1]) {
				addedCells[x][y + 1] = true;
				addLeft(x, y, hst);
			}
		}
		if (coords[x] >> (y + 1) == 0) {
			if (!addedCells[x + 1][y + 2]) {
				addedCells[x + 1][y + 2] = true;
				addUp(x, y, hst);
			}
		}
		if ((x == width - 1 || (coords[x + 1] & (1 << y)) == 0) && (symmetry & 5) == 0)
			if (!addedCells[x + 2][y + 1]) {
				addedCells[x + 2][y + 1] = true;
				addRight(x, y, hst);
			}
	}

	/**
	 * addDown: tries the cell under (x, y).
	 */
	private void addDown(int x, int y, HashSet<FreePolyomino> hst) {
		//count ++;
		short[] newcoords = new short[width];
		if (y == 0) {
			for (int i = 0; i < width; i++)
				newcoords[i] = (short) (coords[i] << 1);
			newcoords[x] += 1;
			hst.add(new FreePolyomino(size + 1, width, height + 1, newcoords));
		} else {
			for (int i = 0; i < width; i++)
				newcoords[i] = coords[i];
			newcoords[x] += (1 << (y - 1));
			hst.add(new FreePolyomino(size + 1, width, height, newcoords));
		}
	}

	/**
	 * addUp: tries the cell above (x, y).
	 */
	private void addUp(int x, int y, HashSet<FreePolyomino> hst) {
		//count ++;
		short[] newcoords = Arrays.copyOf(coords, width);
		newcoords[x] += 1 << (y + 1);
		if (y + 1 == height) {
			hst.add(new FreePolyomino(size + 1, width, height + 1, newcoords));
		} else {
			hst.add(new FreePolyomino(size + 1, width, height, newcoords));
		}
	}

	/**
	 * addLeft: tries the cell on the left of (x, y).
	 */
	private void addLeft(int x, int y, HashSet<FreePolyomino> hst) {
		//count ++;
		short[] newcoords;
		if (x == 0) {
			newcoords = new short[width + 1];
			for (int i = 1; i <= width; i++)
				newcoords[i] = coords[i - 1];
			newcoords[0] = (short) (1 << y);
			hst.add(new FreePolyomino(size + 1, width + 1, height, newcoords));
		} else {
			newcoords = Arrays.copyOf(coords, width);
			newcoords[x - 1] += (1 << y);
			hst.add(new FreePolyomino(size + 1, width, height, newcoords));
		}
	}

	/**
	 * addLeft: tries the cell on the right of (x, y).
	 */
	private void addRight(int x, int y, HashSet<FreePolyomino> hst) {
		//count ++;
		short[] newcoords;
		if (x + 1 == width) {
			newcoords = Arrays.copyOf(coords, width + 1);
			newcoords[width] = (short) (1 << y);
			hst.add(new FreePolyomino(size + 1, width + 1, height, newcoords));
		} else {
			newcoords = Arrays.copyOf(coords, width);
			newcoords[x + 1] += (1 << y);
			hst.add(new FreePolyomino(size + 1, width, height, newcoords));
		}
	}

	@Override
	public boolean equals(Object obj) {
		FreePolyomino that = (FreePolyomino) obj;
		if ((this.size != that.size) || (this.width != that.width) || (this.height != that.height))
			return false;
		for (int i = 0; i < width; i++)
			if (this.coords[i] != that.coords[i])
				return false;
		return true;
	}

	public FixedPolyomino toFixed() {
		return new FixedPolyomino(size, width, height, 0, 0, Arrays.copyOf(coords, width));
	}

	@Override
	void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges) {
		for (int i = 0; i < width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) != 0) {
					realCoords.add(new int[] { (i) * SCALE, (i + 1) * SCALE, (i + 1) * SCALE, (i) * SCALE });
					realCoords.add(new int[] { (j) * SCALE, (j) * SCALE, (j + 1) * SCALE, (j + 1) * SCALE });
					Edge[] fourEdges = new Edge[4];
					fourEdges[0] = new Edge((i) * SCALE, (j) * SCALE, (i + 1) * SCALE, (j) * SCALE, 2);
					fourEdges[1] = new Edge((i + 1) * SCALE, (j) * SCALE, (i + 1) * SCALE, (j + 1) * SCALE, 2);
					fourEdges[2] = new Edge((i + 1) * SCALE, (j + 1) * SCALE, (i) * SCALE, (j + 1) * SCALE, 2);
					fourEdges[3] = new Edge((i) * SCALE, (j + 1) * SCALE, (i) * SCALE, (j) * SCALE, 2);
					for (Edge e : fourEdges) {
						if (edges.contains(e))
							edges.remove(e);
						else
							edges.add(e);
					}
				}
				j++;
			}
		}
	}

	@Override
	void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges, int width) {
		computeCoordinates(realCoords, edges, width);
	}

	@Override
	public HashSet<Polyform> freePolyform() {
		return this.toFixed().freePolyform();
	}

	@Override
	public HashSet<Polyform> oneSidePolyform() {
		return this.toFixed().freePolyform();
	}

	/****
	 * Inverse array t if t's tail is heavier than head.
	 * @param t
	 * @return false if changed, true if not.
	 */

	private static boolean determineDirection(short[] t) {
		for (int i = 0; i < t.length / 2; i++) {
			if (t[i] < t[t.length - 1 - i])
				return true;
			if (t[i] > t[t.length - 1 - i]) {
				for (int j = i; j < t.length / 2; j++) {
					short tmp = t[j];
					t[j] = t[t.length - 1 - j];
					t[t.length - 1 - j] = tmp;
				}
				return false;
			}
		}
		return true;
	}

	/*****
	 * Choose the lighter one between A and B. If B is lighter than A,
	 * exchange their name.
	 * @param A
	 * @param B
	 * @return false if exchanged, true if not.
	 */
	private static boolean chooseBetween(short[] A, short[] B) {
		for (int i = 0; i < A.length; i++) {
			if (A[i] > B[i]) {
				for (int j=i;j<A.length;j++){
					short tmp = A[j];
					A[j] = B[j];
					B[j] = tmp;
				}
				return false;
			}
			if (A[i] < B[i])
				return true;
		}
		return true;
	}

	@Override
	public Polyform dilate(int k) {
		return this.toFixed().dilate(k);
	}
}