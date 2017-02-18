package cover;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import count.Single;

public class DancingLinks {
	private Column head;
	private int nbcolumn;
	private Column[] columns;
	/**
	 * allSolutions: stock all solutions of an exact cover problem.
	 */
	private Single<LinkedList<LinkedList<Integer>>> allSolutions;
	/**
	 * oneSolution: stock one solution of an exact cover problem.
	 */
	private Single<LinkedList<Integer>> oneSolution;
	/**
	 * nbSolutions: stock the number of solutions.
	 */
	private long nbSolutions;
	private static final int ALL = 0, ONE = 1, NB = 2;

	/**
	 * Creates a new instance of DancingLinks.
	 * 
	 * @param n
	 *            is the number of columns.
	 */
	public DancingLinks(int n) {
		this.nbcolumn = n;
		this.columns = new Column[n];
		this.head = new Column(null, null, null, null, -1);
		Linkedstructure c = this.head;
		for (int i = 0; i < n; i++) {
			c.right = new Column(null, null, c, null, i);
			c = c.right;
			c.up = c;
			c.down = c;
			this.columns[i] = c.column;
		}
		c.right = this.head;
		this.head.left = c;
		nbSolutions = -1;
		allSolutions = null;
		oneSolution = null;
	}

	/**
	 * cut: Cuts the link between the (n-1) th column and the n th column. We
	 * will use this function if we hope that each polyform is used at most once
	 * in a tiling problem. In this case, n equals usually the number of cells
	 * that we want to cover. If we require that each polyform is used exactly
	 * once, we don't need to cut any link.
	 * 
	 * @param n
	 *            where the link need to be cut.
	 */
	public void cut(int n) {
		if (this.nbcolumn <= n)
			return;
		this.columns[n - 1].right = this.head;
		this.head.left = this.columns[n - 1];
		this.columns[n].left = this.columns[this.nbcolumn - 1];
		this.columns[this.nbcolumn - 1].right = this.columns[n - 1];
	}

	/**
	 * Adds a polyomino which is represented by a Subset-type variant. <br/>
	 * 
	 * @param s
	 *            a polyomino whose coordinates are converted to the
	 *            corresponding columns of this DancingLinks data structure.
	 */
	public void add(Subset s) {
		add(s.t);
	}

	/**
	 * Adds several polyominoes at the same time <br/>
	 * 
	 * @param cset
	 *            a set of polyominoes whose coordinates are converted to the
	 *            corresponding colums of this DancingLinks data structure.
	 */
	public void add(Set<Subset> cset) {
		for (Subset s : cset)
			add(s.t);
	}

	public void add(int[] a) {
		for (int i : a) {
			columns[i].up = new Data(columns[i].up, columns[i], null, null, columns[i]);
			columns[i].up.up.down = columns[i].up;
			columns[i].size++;
		}
		int l = a.length;
		for (int i = 0; i < a.length; i++) {
			columns[a[i]].up.left = columns[a[(i + l - 1) % l]].up;
			columns[a[i]].up.right = columns[a[(i + 1) % l]].up;
		}
	}

	public void add(int[][] a) {
		for (int i = 0; i < a.length; i++)
			add(a[i]);
	}

	/**
	 * Covers a column <br/>
	 * 
	 * @param x
	 *            the index of the column to cover
	 */
	public void coverColumn(int x) {
		assert x >= 0 && x < this.nbcolumn;
		coverColumn(columns[x]);
	}

	/**
	 * Covers a column <br/>
	 * 
	 * @param x
	 *            the column to cover
	 */
	public void coverColumn(Column x) {
		x.right.left = x.left;
		x.left.right = x.right;
		for (Linkedstructure t = x.down; t != x; t = t.down)
			for (Linkedstructure y = t.right; y != t; y = y.right) {
				y.down.up = y.up;
				y.up.down = y.down;
				y.column.size--;
			}
	}

	/**
	 * Uncovers a column <br/>
	 * 
	 * @param x
	 *            the index of the column to uncover
	 */
	public void uncoverColumn(int x) {
		assert x >= 0 && x < this.nbcolumn;
		uncoverColumn(columns[x]);
	}

	/**
	 * Uncovers a column <br/>
	 * 
	 * @param x
	 *            the column to uncover
	 */
	public void uncoverColumn(Column x) {
		x.right.left = x;
		x.left.right = x;
		for (Linkedstructure t = x.up; t != x; t = t.up)
			for (Linkedstructure y = t.left; y != t; y = y.left) {
				y.down.up = y;
				y.up.down = y;
				y.column.size++;
			}
	}

	/**
	 * Finds one solution for an exact cover problem
	 * 
	 * @return One solution, or null if there does not exist any solution.
	 */
	public Single<LinkedList<Integer>> OneSolution() {
		if (this.oneSolution == null)
			exactCover(ONE, true);
		return this.oneSolution;
	}

	/**
	 * Finds all solutions for an exact cover problem
	 * 
	 * @return All solutions, or null if there does not exist any solution.
	 */
	public Single<LinkedList<LinkedList<Integer>>> AllSolutions() {
		if (this.allSolutions == null)
			exactCover(ALL, false);
		return this.allSolutions;
	}

	/**
	 * Finds the number of solutions for an exact cover problem
	 * 
	 * @return The number of solutions.
	 */
	public long NumberOfSolutions() {
		if (this.nbSolutions == -1) {
			this.nbSolutions = 0;
			exactCover(NB, false);
		}
		return this.nbSolutions;
	}

	/**
	 * Finds one or all solutions for an exact cover problem. <br/>
	 * 
	 * @param parameter
	 *            If parameter=0, this method will modify the field
	 *            "allSolutions" by putting all solutions into it. If
	 *            parameter=1, this method will modify the field "oneSolution"
	 *            by putting the first solution into it. Otherwise, this method
	 *            will modify the field "nbSolutions" by incrementing it for
	 *            each solution the method finds.
	 * @param random
	 *            We will start with covering a random column.
	 */
	private void exactCover(int parameter, boolean random) {
		if (this.head.right == this.head) {
			if (parameter == ALL)
				allSolutions = new Single<LinkedList<LinkedList<Integer>>>(new LinkedList<LinkedList<Integer>>(),
						allSolutions);
			else if (parameter == ONE)
				oneSolution = new Single<LinkedList<Integer>>(null, oneSolution);
			nbSolutions++;
			return;
		}
		Column x = head.right.column;
		/*
		 * When the argument "random" is true, instead of choosing the column
		 * with a minimal size as the next column to cover, we will select a
		 * column randomly. So if we only want to find one solution of an exact
		 * cover problem, we can easily get a different solution every time we
		 * launch the program by setting the argument "random" as true.
		 */
		if (random) {
			Random rnd = ThreadLocalRandom.current();
			int r = rnd.nextInt(this.nbcolumn);
			for (int i = 0; i < r; i++)
				x = x.right.column;
			if (x == head)
				x = x.right.column;
		} else {
			for (Column c = x.right.column; c != head; c = c.right.column)
				if (c.size < x.size)
					x = c;
		}
		if (x.size == 0)
			return;
		coverColumn(x);
		for (Linkedstructure t = x.up; t != x; t = t.up) {
			for (Linkedstructure y = t.left; y != t; y = y.left)
				coverColumn(y.column);
			if (parameter == ALL) {
				Single<LinkedList<LinkedList<Integer>>> res = allSolutions;
				exactCover(parameter, false);
				for (Single<LinkedList<LinkedList<Integer>>> pointer = allSolutions; pointer != res; pointer = pointer.next)
					pointer.val.add(t.toList());
			} else if (parameter == ONE) {
				exactCover(parameter, false);
				if (oneSolution != null) {
					oneSolution = new Single<LinkedList<Integer>>(t.toList(), oneSolution);
					return;
				}
			} else
				exactCover(parameter, false);
			for (Linkedstructure y = t.right; y != t; y = y.right)
				uncoverColumn(y.column);
		}
		uncoverColumn(x);
	}
}
