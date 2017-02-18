package polyform;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import count.AllPolyominoes;
import count.Single;
import cover.DancingLinks;
import cover.ExactCoverNaif;
import cover.Subset;

/**
 * ClassName: Polyforms <br/>
 * Function: Constructs a list of polyforms from a file. <br/>
 * 
 * @author wangke
 */
public class Polyforms {
	/**
	 * polyforms: the collection of polyforms(subsets).
	 */
	public LinkedList<Polyform> polyforms;
	/**
	 * background: In a tiling problem, we always have an area to cover. The
	 * field "background" represents the complement of this area. In other word,
	 * we will not cover the polyforms that appear in "background". So if the
	 * area to cover is a rectangle, "background" will be an empty list.
	 */
	private LinkedList<Polyform> background;
	private int width, height;
	/**
	 * type: If type=0, the polyforms in this class are fixed. If type=1, they
	 * are one-side. If type=2, they are free.
	 */
	public int type;
	/**
	 * shape: If shape=4, the polyforms in this class are polyomino. If shape=3,
	 * they are triangulamino. If shape=6, they are hexagonamino.
	 */
	public int shape;
	private boolean once;
	private int extraColumn;
	private final static int SCALE = 30, XSCALE = 8, YSCALE = 14;
	private final static int FIXED = 0, ONESIDE = 1, FREE = 2;

	/**
	 * Creates a new instance of Polyforms.
	 * 
	 * @param fileName
	 *            the file from which we create a list of polyforms
	 * @param shape
	 *            the shape of polyform, if shape=4, the polyforms in this class
	 *            are polyomino. If shape=3, they are triangulamino. If shape=6,
	 *            they are hexagonamino.
	 * @param type
	 *            the type of polyform. 0 means fixed. 1 means one-side. 2 means
	 *            free.
	 * @param inverse
	 *            if inverse=true, we need to inverse the polyform.
	 * @param once
	 *            if all polyforms can only be used once;
	 * @throws FileNotFoundException
	 */
	public Polyforms(String fileName, int shape, int type, boolean inverse, boolean once) throws FileNotFoundException {
		this.polyforms = new LinkedList<Polyform>();
		this.shape = shape;
		this.type = type;
		this.once = once;
		this.extraColumn = 0;
		//File file = new File(fileName);
		InputStream in = getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String str;
		try {
			while ((str = reader.readLine()) != null) {
				if (inverse)
					polyforms.add(Polyform.generatePolyform(str, shape).reflection('H'));
				else
					polyforms.add(Polyform.generatePolyform(str, shape));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.background = new LinkedList<Polyform>();
		this.width = 5;
		this.height = 5;
	}

	/**
	 * Creates a new empty 'Polyforms' without adding any polyform in it.
	 */
	public Polyforms(int shape, int type, boolean inverse, boolean once) {
		this.polyforms = new LinkedList<Polyform>();
		this.shape = shape;
		this.type = type;
		this.once = once;
		this.extraColumn = 0;
		this.background = new LinkedList<Polyform>();
		this.width = 5;
		this.height = 5;
	}

	/**
	 * Creates a new instance of Polyforms from a polyomino and its dilation.
	 * The polymoino itself constitutes the collection of polyforms(subsets).
	 * And its dilation is the polyomino to tile (the ground set to cover).
	 * 
	 * @param py
	 *            the only polyomino in this Polyforms
	 * @param k
	 *            dilation rate
	 */
	public Polyforms(Polyform py, int k) {
		this.polyforms = new LinkedList<Polyform>();
		this.background = new LinkedList<Polyform>();
		this.shape = 4;
		this.type = FREE;
		this.once = false;
		this.extraColumn = 0;
		Polyform dilation = py.dilate(k);
		this.width = dilation.width;
		this.height = dilation.height;
		this.polyforms.add(py);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if ((dilation.coords[i] & (1 << j)) == 0)
					this.setBackground(Polyform.generatePolyform(new int[] { i }, new int[] { j }, shape));
	}

	/**
	 * add: adds a polyform in the collection of subsets<br/>
	 * 
	 * @param py
	 *            a polyform
	 */
	public void add(Polyform py) {
		this.polyforms.add(py);
	}

	/**
	 * selfCover: the number of polyominoes of size n that can cover their own
	 * k-dilation <br/>
	 * 
	 * @param n
	 *            the size of polyomino
	 * @param k
	 *            dilation rate
	 * @return
	 */
	public static long selfCover(int n, int k) {
		HashSet<FreePolyomino> lst = AllPolyominoes.allFreeSizeExact(n);
		long nb = 0;
		for (FreePolyomino fp : lst) {
			Polyforms pys = new Polyforms(fp.toFixed(), k);
			Single<LinkedList<Integer>> solution = pys.oneSolution();
			if (solution != null) {
				pys.printSolution(solution);
				nb++;
			}
		}
		return nb;
	}

	public void setBackground(Polyform py) {
		this.background.add(py);
	}

	public void setBackground(Collection<Polyform> pys) {
		this.background.addAll(pys);
	}

	public void setBackgroundOctagonal(int l) {
		if (l <= 0)
			return;
		int modified = 0;
		if (l > (Math.min(width, height) - 1) / 2)
			throw new Error("l must be smaller than half of the minimum between the width and the height");
		if (shape == 3) {
			if (width % 2 != 1)
				throw new Error("To generate an octagonal backgroud for triangulaminos, the width should be odd.");
			if (height % 4 == 2) {
				if (l % 2 == 1)
					throw new Error(
							"To generate an octagonal backgroud for this exact cover problem, l should be even.");
				modified = 1;
				setWidth(width + 1);
				for (int i = 0; i < height; i++) {
					setBackground(new Triangulamino(new int[] { 0 }, new int[] { i }));
				}
			} else if (l % 2 == 0)
				throw new Error("To generate an octagonal backgroud for this exact cover problem, l should be odd.");
		}
		int n = l * (l + 1) / 2;
		int[] lux = new int[n], ldx = new int[n], rux = new int[n], rdx = new int[n];
		int[] luy = new int[n], ldy = new int[n], ruy = new int[n], rdy = new int[n];
		int index = 0;
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < l - i; j++) {
				ldx[index] = lux[index] = i + modified;
				luy[index] = j;
				ldy[index] = height - l + i + j;
				index++;
			}
		}
		for (int i = 0; i < n; i++) {
			rux[i] = rdx[i] = width - lux[n - i - 1] - 1 + modified;
			ruy[i] = luy[n - i - 1];
			rdy[i] = ldy[n - i - 1];
		}
		setBackground(Polyform.generatePolyform(lux, luy, shape));
		setBackground(Polyform.generatePolyform(ldx, ldy, shape));
		setBackground(Polyform.generatePolyform(rux, ruy, shape));
		setBackground(Polyform.generatePolyform(rdx, rdy, shape));
	}

	public void setBackgroundLozenge() {
		if (shape == 3) {
			if (this.height - this.width != 1)
				throw new Error(
						"To generate a lozenge-shaped background, the difference between the height and the width should be 1");
		} else if (shape != 4)
			return;
		int l = (Math.min(height, width) - 1) / 2;
		if (l == 0)
			return;
		this.setBackgroundOctagonal(l);
	}

	public void setBackgroundTriangle() {
		if (shape != 4)
			return;
		if (width != height)
			return;
		int n = (width + 1) / 2;
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < height - 2 * i; j++) {
				this.setBackground(Polyform.generatePolyform(new int[] { i - 1 }, new int[] { j }, shape));
				this.setBackground(Polyform.generatePolyform(new int[] { width - i }, new int[] { j }, shape));
			}
		}
	}

	public void setBackgroundParallelogram() {
		if (shape != 4)
			return;
		if (height * 2 <= width)
			return;
		int n = height / 2;
		for (int j = n - 1; j >= 0; j--)
			for (int i = 0; i < width - 2 * (n - j); i++)
				this.setBackground(Polyform.generatePolyform(new int[] { i }, new int[] { j + n }, shape));
		for (int j = 0; j < n; j++)
			for (int i = 2 * j + 2; i < width; i++)
				this.setBackground(Polyform.generatePolyform(new int[] { i }, new int[] { j }, shape));
	}

	public void clearBackground() {
		this.background.clear();
	}

	public void setWidth(int width) {
		if (width <= 0)
			return;
		clearBackground();
		this.width = width;
	}

	public void setHeight(int height) {
		if (height <= 0)
			return;
		clearBackground();
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * @return an instance of Image2d whose width and height are computed from
	 *         this polyforms' width and height
	 */
	Image2d generateImage2d() {
		Image2d img;
		if (shape == 4)
			img = new Image2d(this.width * SCALE, this.height * SCALE);
		else if (shape == 6)
			img = new Image2d(((this.width + this.height) * 3 - 2) * XSCALE, (this.height + this.width) * YSCALE);
		else if (shape == 3)
			img = new Image2d((this.width + 1) * 2 * XSCALE, this.height * 2 * YSCALE);
		else
			throw new IllegalArgumentException();
		for (Polyform py : this.background)
			putIntoImage(img, py, width, Color.white);
		return img;
	}

	/**
	 * putIntoImage: puts a polyform into an image <br/>
	 */
	private static void putIntoImage(Image2d img, Polyform py, int width, Color color) {
		LinkedList<int[]> realCoords = new LinkedList<>();
		HashSet<Edge> edges = new HashSet<>();
		py.computeCoordinates(realCoords, edges, width);
		while (!realCoords.isEmpty())
			img.addPolygon(realCoords.poll(), realCoords.poll(), color);
		for (Edge e : edges)
			img.addEdge(e.x1, e.y1, e.x2, e.y2, e.width);
	}

	/**
	 * When the area we want to cover is not the whole rectangle, we need to
	 * pre-cover several columns in the DancingLinks data structure associated
	 * with our tiling problem before beginning to solve it.
	 * 
	 * @return the columns to cover
	 */
	private HashSet<Integer> columnsToCover() {
		HashSet<Integer> hst = new HashSet<Integer>();
		for (Polyform py : this.background) {
			int[][] fullCoords = py.fullCoords();
			for (int i = 0; i < py.size; i++)
				hst.add(fullCoords[0][i] * this.height + fullCoords[1][i]);
		}
		return hst;
	}

	/**
	 * @return a set of all possible polyforms that may appear in solutions of a
	 *         tiling problem
	 */
	private HashSet<Subset> computeSubsets() {
		HashSet<Subset> hst = new HashSet<Subset>();
		extraColumn = 0;
		for (Polyform py : polyforms) {
			if (this.type == FIXED) {
				extraColumn++;
				computeSubsets(hst, py, shape);
			}
			if (this.type == ONESIDE)
				for (Polyform pyo : py.oneSidePolyform()) {
					extraColumn++;
					computeSubsets(hst, pyo, shape);
				}
			if (this.type == FREE)
				for (Polyform pyo : py.freePolyform()) {
					extraColumn++;
					computeSubsets(hst, pyo, shape);
				}
		}
		return hst;
	}

	/**
	 * all possible translations of a polyform that may appear in solutions of a
	 * tiling problem
	 */
	private void computeSubsets(HashSet<Subset> hst, Polyform py, int shape) {
		int[] xs = new int[py.size], ys = new int[py.size];
		py.getxy(xs, ys);
		if (shape == 4 || shape == 6) {
			for (int i = 0; i <= width - py.width; i++) {
				for (int j = 0; j <= height - py.height; j++) {
					hst.add(toSubset(xs, ys, i, j));
				}
			}
		} else if (shape == 3) {
			for (int i = (py.getWidth() == py.width) ? 0 : -1; i <= width - py.getWidth(); i++)
				for (int j = ((i + 2) & 1); j <= height - py.height; j += 2) {
					hst.add(toSubset(xs, ys, i, j));
				}
		}
	}

	/**
	 * toSubset: transfers a polyform to a subset<br/>
	 * 
	 * @param xs
	 *            initial abscissas of points in a polyform.
	 * @param ys
	 *            initial ordinates of points in a polyform.
	 * @param dx
	 *            x-translation of the polyform
	 * @param dy
	 *            y-translation of the polyform
	 * @return a subset associated to the translation(dx,dy) of a polyform whose
	 *         initial coordinates are described by {(xs[i]; ys[i]),
	 *         0<=i<=xs.length}.
	 */
	private Subset toSubset(int[] xs, int[] ys, int dx, int dy) {
		int n = xs.length;
		if (once)
			n++;
		int[] t = new int[n];
		for (int i = 0; i < xs.length; i++) {
			t[i] = (ys[i] + dy) + (xs[i] + dx) * height;
		}
		if (once)
			t[n - 1] = extraColumn + width * height - 1;
		return new Subset(t);
	}

	/**
	 * generateExactCoverNaif: transforms a tiling problem to an exact cover
	 * problem <br/>
	 * 
	 * @return a solver to an exact cover problem.
	 */
	private ExactCoverNaif generateExactCoverNaif() {
		HashSet<Subset> cset = computeSubsets();
		ExactCoverNaif e = new ExactCoverNaif(width * height, columnsToCover(), cset);
		return e;
	}

	/**
	 * allSolutionsNaif: finds all solutions to a tiling problem with a naïf
	 * algorithm<br/>
	 * 
	 * @return all solutions
	 */
	public LinkedList<LinkedList<Subset>> allSolutionsNaif() {
		ExactCoverNaif e = generateExactCoverNaif();
		return e.allSolutionsNaif();
	}

	/**
	 * oneSolutionNaif: fins one solution to a tiling problem with a naïf
	 * algorithm<br/>
	 * 
	 * @return one solution
	 */
	public LinkedList<Subset> oneSolutionNaif() {
		ExactCoverNaif e = generateExactCoverNaif();
		return e.oneSolutionNaif();
	}

	/**
	 * numberOfSolutionsNaif: finds the number of solutions to a tiling problem
	 * with a naïf algorithm<br/>
	 * 
	 * @return number of solutions
	 */
	public int numberOfSolutionsNaif() {
		ExactCoverNaif e = generateExactCoverNaif();
		return e.numberOfSolutionsNaif();
	}

	/**
	 * allSolutions: finds all solutions to a tiling problem with DLX algorithm
	 * <br/>
	 * 
	 * @return all solutions
	 */
	public Single<LinkedList<LinkedList<Integer>>> allSolutions() {
		DancingLinks d = generateDancingLinks();
		return d.AllSolutions();
	}

	/**
	 * oneSolution: finds one solution to a tiling problem with DLX algorithm
	 * <br/>
	 * 
	 * @return one solution
	 */
	public Single<LinkedList<Integer>> oneSolution() {
		DancingLinks d = generateDancingLinks();
		return d.OneSolution();
	}

	/**
	 * NumberOfSolutions: finds the number of solutions to a tiling problem with
	 * DLX algorithm<br/>
	 * 
	 * @return number of solutions
	 */
	public long NumberOfSolutions() {
		DancingLinks d = generateDancingLinks();
		return d.NumberOfSolutions();
	}

	/**
	 * generateDancingLinks: transforms a tiling problem to an exact cover
	 * problem<br/>
	 * 
	 * @return a solver to an exact cover problem
	 */
	private DancingLinks generateDancingLinks() {
		int n = width * height;
		HashSet<Subset> cset = computeSubsets();
		if (once)
			n += extraColumn;
		DancingLinks d = new DancingLinks(n);
		// if (once) d.cut(width * height);
		d.add(cset);
		for (int i : columnsToCover())
			d.coverColumn(i);
		return d;
	}

	public void printSolution(Single<LinkedList<Integer>> solution) {
		if (solution == null || solution.val == null) {
			System.out.println("No solution");
			return;
		}
		Image2d img = generateImage2d();
		while (solution.val != null) {
			float[] rgb = Polyform.randomColor();
			Color color = new Color(rgb[0], rgb[1], rgb[2]);
			if (once) {
				Collections.sort(solution.val);
				solution.val.removeLast();
			}
			Polyform py = Polyform.generatePolyform(solution.val, height, shape);
			putIntoImage(img, py, width, color);
			solution = solution.next;
		}
		new Image2dViewer(img);
	}

	public void printSolution(LinkedList<LinkedList<Integer>> solution) {
		if (solution == null)
			System.out.println("No solution");
		Image2d img = generateImage2d();
		for (LinkedList<Integer> l : solution) {
			float[] rgb = Polyform.randomColor();
			Color color = new Color(rgb[0], rgb[1], rgb[2]);
			if (once) {
				Collections.sort(l);
				l.removeLast();
			}
			Polyform py = Polyform.generatePolyform(l, height, shape);
			putIntoImage(img, py, width, color);
		}
		new Image2dViewer(img);
	}

	public void printSolutionNaif(LinkedList<Subset> solution) {
		if (solution == null)
			System.out.println("No solution");
		Image2d img = generateImage2d();
		for (Subset s : solution) {
			float[] rgb = Polyform.randomColor();
			Color color = new Color(rgb[0], rgb[1], rgb[2]);
			Polyform py = Polyform.generatePolyform(s, height, shape);
			putIntoImage(img, py, width, color);
		}
		new Image2dViewer(img);
	}

	static void afficher(int[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println();
	}
}
