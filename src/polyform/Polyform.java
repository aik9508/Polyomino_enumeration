package polyform;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import cover.Subset;
/**
 * This class is the abstract class of all kinds of polyform (polyomino, 
 * triangulamino, hexagonamino, etc).
 */
public abstract class Polyform {
	/**
	 * size: number of cells of a polyform/polyomino
	 */
	public int size;
	/**
	 * width: the horizontal distance between the rightmost cell and the leftmost one.
	 * height: the vertical distance between the top cell and the bottom one.
	 */
	public int width, height;
	/**
	 * coords: the binary array representation of a polyform/polyomino.
	 */
	public short[] coords;
	/**
	 * SCALE: number of pixels of cell's edge when drawing the polyform/polyomino.
	 */
	private final static int SCALE = 30;

	/**
	 * fullCoords: calculate coordinates of all cells of a polyomino by the binary array.
	 * @return A 2-dimensional array of coordinates of all cells.
	 */
	abstract int[][] fullCoords();

	/**
	 * translation: translates the polyomino by (dx, dy)
	 * @param dx distance of translation in the direction of x-axis
	 * @param dy distance of translation in the direction of y-axis
	 * @return The new polyomino after the translation.
	 */
	abstract Polyform translation(int dx, int dy);

	/**
	 * rotation: rotates the polyomino by one quarter-turn, one half-turn or 3 quater-turn.
	 * @param angle angle=1 means rotation of one quarter-turn; angle=2 means rotation of 
	 * one half-turn; angle=3 means rotation of 3 quarter-turn.
	 * @return The new polyomino after the rotation.
	 */
	abstract Polyform rotation(int angle);
	
	/**
	 * reflection: reflects the polyomino by the x-axis or y-axis.
	 * @param HV HV='H' means reflection by the x-axis; HV='V' means reflection by the y-axis.
	 * @return The new polyomino after the reflection.
	 */
	abstract Polyform reflection(char HV); 
	
	/**
	 * dilate: dilates the polyomino by a factor of k.
	 * @param k the factor of dilation.
	 * @return The new polyomino after the dilation.
	 */
	public abstract Polyform dilate(int k);
	
	/**
	 * computeCoordinates: calculates the coordinates of the polyomino on the canvas.
	 * @param realCoords the coordinates of all cells of the polyomino
	 * @param edges the set of edges of the polyomino
	 */
	abstract void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges);

	/**
	 * computeCoordinates: calculates the coordinates of the polyomino on the canvas.
	 * @param realCoords the coordinates of all cells of the polyomino
	 * @param edges the set of edges of the polyomino
	 * @param width the width of edge when drawing it
	 */
	abstract void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges, int width);

	/**
	 * freePolyform: generates all free polyforms.
	 * @return An HashSet of all free polyforms.
	 */
	public abstract HashSet<Polyform> freePolyform();

	/**
	 * oneSidePolyform: generates all one-side polyforms.
	 * @return An HashSet of all one-side polyforms.
	 */
	public abstract HashSet<Polyform> oneSidePolyform();

	/**
	 * generatePolyform: generates a polyform described by a String of a specific shape.
	 * @param str coordinates of cells in the String form
	 * @param shape shape=4 means polyomino; shape=3 means triangulamino; shape=6 means hexagonamino
	 * @return A polyform described by argument str and of a specific shape 
	 * described by argument shape.
	 */
	static Polyform generatePolyform(String str, int shape) {
		if (shape == 4)
			return new FixedPolyomino(str);
		else if (shape == 6)
			return new Hexagonamino(str);
		else if (shape == 3)
			return new Triangulamino(str);
		else
			throw new IllegalArgumentException();
	}

	/**
	 * generatePolyform: generates a polyform described by a Subset of a specific shape.
	 * @param s the subset which describes the polyform 
	 * @param shape shape=4 means polyomino; shape=3 means triangulamino; shape=6 means hexagonamino
	 * @return A polyform described by argument s and of a specific shape 
	 * described by argument shape.
	 */
	static Polyform generatePolyform(Subset s, int height, int shape) {
		int n = s.t.length;
		int[] xs = new int[n];
		int[] ys = new int[n];
		for (int i = 0; i < n; i++) {
			xs[i] = s.t[i] / height;
			ys[i] = s.t[i] % height;
		}
		if (shape == 4)
			return new FixedPolyomino(xs, ys);
		else if (shape == 6)
			return new Hexagonamino(xs, ys);
		else if (shape == 3)
			return new Triangulamino(xs, ys);
		else
			throw new IllegalArgumentException();
	}

	/**
	 * generatePolyform: generates a polyform described by a Collection of integers 
	 * of a specific shape.
	 * @param c the collection of integer which describes the polyform
	 * @param shape shape=4 means polyomino; shape=3 means triangulamino; shape=6 means hexagonamino
	 * @return A polyform described by argument c and of a specific shape 
	 * described by argument shape.
	 */
	static Polyform generatePolyform(Collection<Integer> c, int height, int shape) {
		int n = c.size();
		int[] xs = new int[n], ys = new int[n], tmp = new int[n];
		int i = 0;
		for (Integer e : c)
			tmp[i++] = e;
		Arrays.sort(tmp);
		for (int j = 0; j < n; j++) {
			xs[j] = tmp[j] / height;
			ys[j] = tmp[j] % height;
		}
		if (shape == 4)
			return new FixedPolyomino(xs, ys);
		else if (shape == 6)
			return new Hexagonamino(xs, ys);
		else if (shape == 3)
			return new Triangulamino(xs, ys);
		else
			throw new IllegalArgumentException();
	}

	/**
	 * generatePolyform: generates a polyform described by coordinates of cells of a specific shape.
	 * @param xs the x-coordinates of all cells
	 * @param ys the y-coordinates of all cells
	 * @param shape shape=4 means polyomino; shape=3 means triangulamino; shape=6 means hexagonamino
	 * @return A polyform described by argument xs and ys and of a specific shape 
	 * described by argument shape.
	 */
	static Polyform generatePolyform(int[] xs, int[] ys, int shape) {
		if (shape == 4)
			return new FixedPolyomino(xs, ys);
		else if (shape == 6)
			return new Hexagonamino(xs, ys);
		else if (shape == 3)
			return new Triangulamino(xs, ys);
		else
			throw new IllegalArgumentException();
	}

	/**
	 * drawing: draws the polyomino
	 */
	public void drawing() {
		LinkedList<int[]> realCoords = new LinkedList<>();
		HashSet<Edge> edges = new HashSet<>();
		this.computeCoordinates(realCoords, edges);
		Image2d img = new Image2d(width * SCALE, height * SCALE);
		float[] rgb = randomColor();
		while (!realCoords.isEmpty())
			img.addPolygon(realCoords.poll(), realCoords.poll(), new Color(rgb[0], rgb[1], rgb[2]));
		for (Edge e : edges)
			img.addEdge(e.x1, e.y1, e.x2, e.y2, e.width);
		new Image2dViewer(img);
	}

	/**
	 * randomColor: chooses a color randomly.
	 * @return
	 */
	static float[] randomColor() {
		float[] rgb = new float[3];
		Random rnd = ThreadLocalRandom.current();
		rgb[0] = 1;
		rgb[1] = 0;
		rgb[2] = rnd.nextFloat();
		for (int i = 2; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			float tmp = rgb[index];
			rgb[index] = rgb[i];
			rgb[i] = tmp;
		}
		return rgb;
	}

	/**
	 * hashCode: calculates the hashCode of a polyomino.
	 */
	public int hashCode() {
		int code = width * 313 + height;
		for (int i = 0; i < width; i++)
			code = code * 313 + coords[i];
		return code;
	}

	/**
	 * 2 polyomino are equal if and only if their coords array are identical.
	 */
	public boolean equals(Object obj) {
		Polyform that = (Polyform) obj;
		if ((this.size != that.size) || (this.width != that.width) || (this.height != that.height))
			return false;
		for (int i = 0; i < width; i++)
			if (this.coords[i] != that.coords[i])
				return false;
		return true;
	}

	/**
	 * Output all coordinations of all points of a polyomino.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		int[][] fullCoords = fullCoords();
		sb.append("(" + fullCoords[0][0] + "," + fullCoords[1][0] + ")");
		for (int i = 1; i < this.size; i++) {
			sb.append(", (" + fullCoords[0][i] + "," + fullCoords[1][i] + ")");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * max: looks for the biggest element of array
	 * @return the biggest element
	 */
	static protected int max(int[] t) {
		int l = t.length;
		assert l > 0;
		int max = t[0];
		for (int i = 1; i < l; i++)
			if (t[i] > max)
				max = t[i];
		return max;
	}

	/**
	 * min: looks for the smallest element of array
	 * @return the smallest element
	 */
	static protected int min(int[] t) {
		int l = t.length;
		assert l > 0;
		int min = t[0];
		for (int i = 1; i < l; i++)
			if (t[i] < min)
				min = t[i];
		return min;
	}
	
	/**
	 * inverseArray: inverses an array
	 * @param t
	 */
	protected static void inverseArray (short[] t) {
		for (int i=0;i<t.length/2;i++){
			short tmp = t[i];
			t[i] = t[t.length-1-i];
			t[t.length-1-i] = tmp;
		}
	}
	
	/**
	 * swapArray: exchanges elements of 2 arrays.
	 * @param t1
	 * @param t2
	 */
	protected static void swapArrays (short[] t1, short[] t2) {
		for (int i=0;i<t1.length;i++){
			short tmp = t1[i];
			t1[i] = t2[i];
			t2[i] = tmp;
		}
	}

	/**
	 * invershort: inverses the first l binary bits of a short number. 
	 * For example: 001101 --> 001011
	 * @param s the number waits to be inversed
	 * @param l the length of binary bits
	 * @return the inversed number
	 */
	protected static short inverseshort(int s, int l) {
		short newx = (short) (s & 1);
		s >>= 1;
		for (int j = 1; j < l; j++) {
			int tmp = (s & 1);
			s >>= 1;
			newx = (short) ((newx << 1) + tmp);
		}
		return newx;
	}
	
	/**
	 * getWidth: gets the width of this polyomino.
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * getxy: gets the relative coordinates of cells of the polyomino.
	 * This method has the same function of fullCoords, except that this one calculates only the
	 * relative coordinates. 
	 */
	public void getxy(int[] xs, int[] ys) {
		int ind = 0;
		for (int i = 0; i < width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) == 1) {
					xs[ind] = i;
					ys[ind] = j;
					ind++;
				}
				j++;
			}
		}
	}

}
