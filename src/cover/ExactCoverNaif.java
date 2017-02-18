package cover;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * ClassName: ExactCoverNaif <br/>
 * Function: Solve exact cover problems with naïf data structures.
 */
public class ExactCoverNaif {
	/**
	 * xset: ground set
	 */
	private HashSet<Integer> xset;
	/**
	 * cset: collection of subsets
	 */
	private HashSet<Subset> cset;
	/**
	 * hmp: a map that maps an element of the ground set to all subsets
	 * containing it.
	 */
	private HashMap<Integer, HashSet<Subset>> hmp;
	/**
	 * size: the size of the ground set.
	 */
	private int size;

	/**
	 * Creates a new instance of ExactCoverNaif.
	 * 
	 * @param l
	 *            the size of the ground set;
	 * @param cset
	 *            the collection of subsets
	 */
	public ExactCoverNaif(int l, HashSet<Subset> cset) {
		init(l, null, cset);
	}

	/**
	 * Creates a new instance of ExactCoverNaif.
	 * 
	 * @param l
	 *            the size of the ground set;
	 * @param columnstocover
	 *            columns to pre-cover (the numbers in columstocover will be
	 *            deleted from the ground set)
	 * @param cset
	 *            the collection of subsets
	 */
	public ExactCoverNaif(int l, HashSet<Integer> columnstocover, HashSet<Subset> cset) {
		init(l, columnstocover, cset);
	}

	/**
	 * init: initializes the ground set and the map.
	 */
	private void init(int l, HashSet<Integer> columnstocover, HashSet<Subset> cset) {
		this.size = l;
		this.cset = cset;
		this.hmp = new HashMap<Integer, HashSet<Subset>>();
		this.xset = new HashSet<Integer>();
		for (int i = 0; i < l; i++) {
			xset.add(i);
			hmp.put(i, new HashSet<Subset>());
		}
		for (Subset s : cset)
			for (int i : s.t)
				hmp.get(i).add(s);
		if (columnstocover != null)
			modifyXsetAndCset(columnstocover);
	}

	/**
	 * modifyXsetAndCset: <br/>
	 * 
	 * @param columnstocover
	 *            this function will delete all elements in columnstocover from
	 *            the ground set and delete all subsets containing it.
	 */
	private void modifyXsetAndCset(HashSet<Integer> columnstocover) {
		for (int i : columnstocover) {
			xset.remove(i);
			for (Subset s : hmp.get(i))
				cset.remove(s);
			hmp.remove(i);
		}
	}

	/**
	 * allSolutionsNaif: <br/>
	 * 
	 * @return all solutions
	 */
	public LinkedList<LinkedList<Subset>> allSolutionsNaif() {
		int[] column = new int[size];
		for (Subset s : cset)
			for (int i : s.t) {
				column[i]++;
			}
		return allSolutionsNaif(xset, cset, hmp, column);
	}

	/**
	 * peek: <br/>
	 * 
	 * @param s
	 *            a set
	 * @return an element in s.
	 */
	private static int peek(Set<Integer> s) {
		for (int i : s) {
			return i;
		}
		return -1;
	}

	private static LinkedList<LinkedList<Subset>> allSolutionsNaif(HashSet<Integer> xset, HashSet<Subset> cset,
			HashMap<Integer, HashSet<Subset>> hmp, int[] column) {
		LinkedList<LinkedList<Subset>> solutions = new LinkedList<LinkedList<Subset>>();
		if (xset.isEmpty()) {
			solutions.add(new LinkedList<Subset>());
			return solutions;
		}
		int x = peek(xset);
		for (int i : xset)
			if (column[i] < column[x])
				x = i;
		for (Subset s : hmp.get(x)) {
			if (!cset.contains(s))
				continue;
			LinkedList<Integer> tmpxset = new LinkedList<Integer>();
			LinkedList<Subset> tmpcset = new LinkedList<Subset>();
			int[] tmpcolumn = Arrays.copyOf(column, column.length);
			for (int i : s.t) {
				if (xset.remove(i))
					tmpxset.add(i);
				for (Subset e : hmp.get(i))
					if (cset.remove(e)) {
						tmpcset.add(e);
						for (int j : e.t)
							tmpcolumn[j]--;
					}
			}
			for (LinkedList<Subset> l : allSolutionsNaif(xset, cset, hmp, tmpcolumn)) {
				l.add(s);
				solutions.add(l);
			}
			xset.addAll(tmpxset);
			cset.addAll(tmpcset);
		}
		return solutions;
	}

	/**
	 * oneSolutionNaif: <br/>
	 * 
	 * @return one solution
	 */
	public LinkedList<Subset> oneSolutionNaif() {
		int[] column = new int[size];
		for (Subset s : cset)
			for (int i : s.t) {
				column[i]++;
			}
		return oneSolutionNaif(xset, cset, hmp, column);
	}

	private static LinkedList<Subset> oneSolutionNaif(HashSet<Integer> xset, HashSet<Subset> cset,
			HashMap<Integer, HashSet<Subset>> hmp, int[] column) {
		if (xset.isEmpty())
			return new LinkedList<Subset>();
		int x = peek(xset);
		for (int i : xset)
			if (column[i] < column[x])
				x = i;
		if (column[x] == 0)
			return null;
		for (Subset s : hmp.get(x)) {
			if (!cset.contains(s))
				continue;
			LinkedList<Integer> tmpxset = new LinkedList<Integer>();
			LinkedList<Subset> tmpcset = new LinkedList<Subset>();
			int[] tmpcolumn = Arrays.copyOf(column, column.length);
			for (int i : s.t) {
				if (xset.remove(i))
					tmpxset.add(i);
				for (Subset e : hmp.get(i))
					if (cset.remove(e)) {
						tmpcset.add(e);
						for (int j : e.t)
							tmpcolumn[j]--;
					}
			}
			LinkedList<Subset> l = oneSolutionNaif(xset, cset, hmp, tmpcolumn);
			if (l != null) {
				l.add(s);
				return l;
			}
			xset.addAll(tmpxset);
			cset.addAll(tmpcset);
		}
		return null;
	}

	/**
	 * numberOfSolutionsNaif: <br/>
	 * 
	 * @return number of solutions
	 */
	public int numberOfSolutionsNaif() {
		int[] column = new int[size];
		for (Subset s : cset)
			for (int i : s.t) {
				column[i]++;
			}
		return numberOfSolutionsNaif(xset, cset, hmp, column);
	}

	private static int numberOfSolutionsNaif(HashSet<Integer> xset, HashSet<Subset> cset,
			HashMap<Integer, HashSet<Subset>> hmp, int[] column) {
		int solution = 0;
		if (xset.isEmpty())
			return 1;
		int x = peek(xset);
		for (int i : xset)
			if (column[i] < column[x])
				x = i;
		if (column[x] == 0)
			return solution;
		for (Subset s : hmp.get(x)) {
			if (!cset.contains(s))
				continue;
			LinkedList<Integer> tmpxset = new LinkedList<Integer>();
			LinkedList<Subset> tmpcset = new LinkedList<Subset>();
			int[] tmpcolumn = Arrays.copyOf(column, column.length);
			for (int i : s.t) {
				if (xset.remove(i))
					tmpxset.add(i);
				for (Subset e : hmp.get(i))
					if (cset.remove(e)) {
						tmpcset.add(e);
						for (int j : e.t)
							tmpcolumn[j]--;
					}
			}
			solution += numberOfSolutionsNaif(xset, cset, hmp, tmpcolumn);
			xset.addAll(tmpxset);
			cset.addAll(tmpcset);
		}
		return solution;
	}
}
