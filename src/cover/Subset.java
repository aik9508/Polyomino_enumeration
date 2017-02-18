package cover;

import java.util.Arrays;
import java.util.HashSet;
import count.Single;

/**
 * ClassName: Subset <br/>
 * Function: This is a data structure that can represent a polyform in a tiling
 * problem <br/>
 * Reason: To implement a tiling problem to an exact cover problem, we need to
 * be able to transfer the coordinates of a polyform into a subset of
 * corresponding columns to cover. <br/>
 * 
 * @author wangke
 */
public class Subset implements Comparable<Subset> {
	/**
	 * t: an array stocking columns to cover.
	 */
	public int[] t;

	/**
	 * Creates a new instance of Subset.
	 */
	public Subset(int[] t) {
		// In order to make the comparison between two subsets easier,
		// we ensure that indexes of columns are arranged in increasing order
		Arrays.sort(t);
		this.t = t;
	}

	/**
	 * Creates a new instance of Subset.
	 * 
	 * @param t
	 *            an array to put into a subset
	 * @param ordered
	 *            this parameter means the array is already ordered, so we don't
	 *            need to sort it.
	 */
	public Subset(int[] t, boolean ordered) {
		this.t = t;
	}

	@Override
	public int compareTo(Subset o) {
		if (this.t.length != o.t.length)
			return this.t.length - o.t.length;
		for (int i = 0; i < this.t.length; i++) {
			int k = this.t[i] - o.t[i];
			if (k != 0)
				return k;
		}
		return 0;
	}

	public boolean equals(Object o) {
		Subset that = (Subset) o;
		return this.compareTo(that) == 0;
	}

	public int hashCode() {
		int h = 0;
		for (int i : t)
			h += (i + 1) * (i + 1);
		return h;
	}

	public String toString() {
		String str = "{";
		for (int i = 0; i < t.length - 1; i++)
			str = str + t[i] + ", ";
		str = str + t[t.length - 1] + "}";
		return str;
	}

	/**
	 * generateSubsets: generates all subsets of the ground set {0,2,3, ... , n}
	 * <br/>
	 * 
	 * @param n
	 *            The size of the ground set.
	 * @return A list of all subsets.
	 */
	public static HashSet<Subset> generateSubsets(int n) {
		Single<Subset> cset = null;
		cset = generateSubsets(n, n+1, cset);
		HashSet<Subset> hashCset=new HashSet<>();
		while(cset!=null){
			hashCset.add(cset.val);
			cset=cset.next;
		}
		return hashCset;
	}

	private static Single<Subset> generateSubsets(int n, int size, Single<Subset> cset) {
		if (size == 1) {
			for (int i = 0; i <= n; i++)
				cset = new Single<Subset>(new Subset(new int[] { i }), cset);
			return cset;
		} else {
			cset=generateSubsets(n, size - 1, cset);
			Single<Subset> pointer = cset;
			while (pointer!=null) {
				int[] tmp = pointer.val.t;
				if (tmp.length != size - 1)
					break;
				for (int i = tmp[size - 2] + 1; i <= n; i++) {
					int[] a = Arrays.copyOf(tmp, size);
					a[size - 1] = i;
					cset = new Single<Subset>(new Subset(a, true), cset);
				}
				pointer = pointer.next;
			}
		}
		return cset;
	}
	
}
