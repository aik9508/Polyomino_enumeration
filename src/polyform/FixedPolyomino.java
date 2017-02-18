package polyform;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class inherits the abstract class Polyform. This class represent fixed polyominoes.
 *
 */
public class FixedPolyomino extends Polyform {
	/**
	 * xo is the minimum of all x-coordinates
	 * yo is the minimum of all y-coordinates
	 */
	int xo, yo;
	/**
	 * SCALE is related to the drawing configuration
	 */
	private final static int SCALE = 30;

	/**
	 * Constructor of fixed polyomino
	 * @param size the number of cells 
	 * @param width the width of polyomino
	 * @param height the height of polyomino
	 * @param xo the minimum of all x-coordinates
	 * @param yo the minimum of all y-coordinates
	 * @param coords the binary array of this polyomino
	 */
	public FixedPolyomino(int size, int width, int height, int xo, int yo, short[] coords) {
		this.size = size;
		this.width = width;
		this.height = height;
		this.xo = xo;
		this.yo = yo;
		this.coords = coords;
	}

	/**
	 * Constructor of fixed polyomino
	 * @param xs x-coordinates of all cells of polyomino
	 * @param ys y-coordinates of all cells of polyomino
	 */
	public FixedPolyomino(int[] xs, int[] ys) {
		assert xs.length > 0;
		assert xs.length == ys.length;
		size = xs.length;
		xo = min(xs);
		yo = min(ys);
		width = max(xs) - xo + 1;
		height = max(ys) - yo + 1;
		coords = new short[width];
		for (int i = 0; i < size; i++) {
			int xr = xs[i] - xo, yr = ys[i] - yo;
			coords[xr] += (1 << yr);
		}
	}

	/**
	 * Constructor of fixed polyomino
	 * @param str coordinates of all cells of polyomino in the form of String
	 */
	public FixedPolyomino(String str) {
		LinkedList<String> l = new LinkedList<String>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			l.add(m.group());
		}
		size = l.size() / 2;
		// assert (size % 2 == 0);
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
		xo = min(xs);
		yo = min(ys);
		width = max(xs) - xo + 1;
		height = max(ys) - yo + 1;
		coords = new short[width];
		for (int i = 0; i < size; i++) {
			int xr = xs[i] - xo, yr = ys[i] - yo;
			coords[xr] += (1 << yr);
		}
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
					truecoords[0][count] = i + xo;
					truecoords[1][count] = yposition + yo;
					count++;
				}
				ys >>= 1;
				yposition++;
			}
		}
		return truecoords;
	}

	@Override
	public FixedPolyomino translation(int dx, int dy) {
		return new FixedPolyomino(size, width, height, xo + dx, yo + dy, coords);
	}

	@Override
	public FixedPolyomino rotation(int angle) {
		short[] initcoords = new short[width];
		if (angle == 1) {
			short[] newcoords = new short[height];
			initcoords = Arrays.copyOf(coords, width);
			for (int i = 0; i < height; i++) {
				short newy = 0;
				for (int j = width - 1; j >= 0; j--) {
					int xjy = (initcoords[j] & 1);
					newy = (short) ((newy << 1) + xjy);
					initcoords[j] >>= 1;
				}
				newcoords[height - 1 - i] = newy;
			}
			return new FixedPolyomino(size, height, width, 0, 0, newcoords);
		} else if (angle == 3) {
			short[] newcoords = new short[height];
			initcoords = Arrays.copyOf(coords, width);
			for (int i = 0; i < height; i++) {
				short newy = 0;
				for (int j = 0; j < width; j++) {
					int xjy = (initcoords[j] & 1);
					newy = (short) ((newy << 1) + xjy);
					initcoords[j] >>= 1;
				}
				newcoords[i] = newy;
			}
			return new FixedPolyomino(size, height, width, 0, 0, newcoords);
		} else if (angle == 2) {
			for (int i = 0; i < width; i++) {
				int xi = coords[i];
				initcoords[width - 1 - i] = inverseshort(xi, height);
			}
			return new FixedPolyomino(size, width, height, 0, 0, initcoords);
		} else
			throw new IllegalArgumentException(angle + " is not a legal argument, please choose 1 or 2 or 3.");
	}

	@Override
	public FixedPolyomino reflection(char HV) {
		short[] newcoords = new short[width];
		if (HV == 'H') {
			for (int i = 0; i < width; i++) {
				int xi = coords[i];
				newcoords[i] = inverseshort(xi, height);
			}
			return new FixedPolyomino(size, width, height, 0, 0, newcoords);
		} else if (HV == 'V') {
			for (int i = 0; i < width; i++) {
				newcoords[i] = coords[width - 1 - i];
			}
			return new FixedPolyomino(size, width, height, 0, 0, newcoords);
		} else
			throw new IllegalArgumentException("Only 'H' and 'V' are accepted.");
	}

	/**
	 * addOneCell: This method tries all attachable cells of this polyomino and attach them one by one
	 * to the polyomino to generat polyominoes of size+1. This edition aims to find all polyominoes which
	 * could be covered by a square of specific size.
	 * @param hst the HashSet of found polyominoes
	 * @param w the width of the covering square
	 * @param h the height of the covering square
	 */
	public void addOneCell(HashSet<FixedPolyomino> hst, int w, int h) {
		for (int i = 0; i < width; i++) {
			int ys = coords[i];
			for (int j = 0; ys != 0; j++) {
				if ((ys & 1) == 1)
					add(i, j, hst, w, h);
				ys >>= 1;
			}
		}
	}

	/**
	 * addOneCell: This method tries all attachable cells of this polyomino and attach them one by one
	 * to the polyomino to generat polyominoes of size+1.
	 * @param hst the HashSet of found polyominoes
	 */
	public void addOneCell(HashSet<FixedPolyomino> hst) {
		for (int i = 0; i < width; i++) {
			int ys = coords[i];
			for (int j = 0; ys != 0; j++) {
				if ((ys & 1) == 1)
					add(i, j, hst, Integer.MAX_VALUE, Integer.MAX_VALUE);
				ys >>= 1;
			}
		}
	}

	/**
	 * add: try all attachable cells around the cell (x, y)
	 */
	private void add(int x, int y, HashSet<FixedPolyomino> hst, int w, int h) {
		if (y == 0) {
			if (this.height < h)
				addDown(x, y, hst);
		} else if ((coords[x] & (1 << (y - 1))) == 0)
			addDown(x, y, hst);

		if (x == 0) {
			if (this.width < w)
				addLeft(x, y, hst);
		} else if ((coords[x - 1] & (1 << y)) == 0)
			addLeft(x, y, hst);

		if (y == this.height - 1) {
			if (this.height < h)
				addUp(x, y, hst);
		} else if (coords[x] >> (y + 1) == 0)
			addUp(x, y, hst);

		if (x == this.width - 1) {
			if (this.width < w)
				addRight(x, y, hst);
		} else if ((coords[x + 1] & (1 << y)) == 0)
			addRight(x, y, hst);
	}

	/**
	 * addDown: tries the cell under (x, y).
	 */
	private void addDown(int x, int y, HashSet<FixedPolyomino> hst) {
		short[] newcoords = new short[width];
		if (y == 0) {
			for (int i = 0; i < width; i++)
				newcoords[i] = (short) (coords[i] << 1);
			newcoords[x] += 1;
			hst.add(new FixedPolyomino(size + 1, width, height + 1, xo, yo - 1, newcoords));
		} else {
			for (int i = 0; i < width; i++)
				newcoords[i] = coords[i];
			newcoords[x] += (1 << (y - 1));
			hst.add(new FixedPolyomino(size + 1, width, height, xo, yo, newcoords));
		}
	}

	/**
	 * addUp: tries the cell above (x, y).
	 */
	private void addUp(int x, int y, HashSet<FixedPolyomino> hst) {
		short[] newcoords = Arrays.copyOf(coords, width);
		newcoords[x] += 1 << (y + 1);
		if (y + 1 == height) {
			hst.add(new FixedPolyomino(size + 1, width, height + 1, xo, yo, newcoords));
		} else {
			hst.add(new FixedPolyomino(size + 1, width, height, xo, yo, newcoords));
		}
	}

	/**
	 * addLeft: tries the cell on the left of (x, y).
	 */
	private void addLeft(int x, int y, HashSet<FixedPolyomino> hst) {
		short[] newcoords;
		if (x == 0) {
			newcoords = new short[width + 1];
			for (int i = 1; i <= width; i++)
				newcoords[i] = coords[i - 1];
			newcoords[0] = (short) (1 << y);
			hst.add(new FixedPolyomino(size + 1, width + 1, height, xo - 1, yo, newcoords));
		} else {
			newcoords = Arrays.copyOf(coords, width);
			newcoords[x - 1] += (1 << y);
			hst.add(new FixedPolyomino(size + 1, width, height, xo, yo, newcoords));
		}
	}

	/**
	 * addLeft: tries the cell on the right of (x, y).
	 */
	private void addRight(int x, int y, HashSet<FixedPolyomino> hst) {
		short[] newcoords;
		if (x + 1 == width) {
			newcoords = Arrays.copyOf(coords, width + 1);
			newcoords[width] = (short) (1 << y);
			hst.add(new FixedPolyomino(size + 1, width + 1, height, xo, yo, newcoords));
		} else {
			newcoords = Arrays.copyOf(coords, width);
			newcoords[x + 1] += (1 << y);
			hst.add(new FixedPolyomino(size + 1, width, height, xo, yo, newcoords));
		}
	}

	@Override
	public boolean equals(Object obj) {
		FixedPolyomino that = (FixedPolyomino) obj;
		if ((this.size != that.size) || (this.width != that.width) || (this.height != that.height))
			return false;
		for (int i = 0; i < width; i++)
			if (this.coords[i] != that.coords[i])
				return false;
		return true;
	}

	@Override
	public void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges) {
		for (int i = 0; i < width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) != 0) {
					realCoords.add(new int[] { (i + xo) * SCALE, (i + xo + 1) * SCALE, (i + xo + 1) * SCALE,
							(i + xo) * SCALE });
					realCoords.add(new int[] { (j + yo) * SCALE, (j + yo) * SCALE, (j + yo + 1) * SCALE,
							(j + yo + 1) * SCALE });
					Edge[] fourEdges = new Edge[4];
					fourEdges[0] = new Edge((i + xo) * SCALE, (j + yo) * SCALE, (i + xo + 1) * SCALE, (j + yo) * SCALE,
							2);
					fourEdges[1] = new Edge((i + xo + 1) * SCALE, (j + yo) * SCALE, (i + xo + 1) * SCALE,
							(j + yo + 1) * SCALE, 2);
					fourEdges[2] = new Edge((i + xo + 1) * SCALE, (j + yo + 1) * SCALE, (i + xo) * SCALE,
							(j + yo + 1) * SCALE, 2);
					fourEdges[3] = new Edge((i + xo) * SCALE, (j + yo + 1) * SCALE, (i + xo) * SCALE, (j + yo) * SCALE,
							2);
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
	public void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges, int width) {
		computeCoordinates(realCoords, edges);
	}

	@Override
	public HashSet<Polyform> freePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		hst.add(this);
		Polyform py = this.reflection('H');
		hst.add(py);
		for (int i = 1; i < 4; i++) {
			hst.add(this.rotation(i));
			hst.add(py.rotation(i));
		}
		return hst;
	}

	@Override
	public HashSet<Polyform> oneSidePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		hst.add(this);
		for (int i = 1; i < 4; i++)
			hst.add(this.rotation(i));
		return hst;
	}

	@Override
	public Polyform dilate(int k) {
		int[] xs = new int[k * k * size], ys = new int[k * k * size];
		int ind=0;
		for (int i = 0; i < width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) != 0) {
					for(int dx=i*k;dx<(i+1)*k;dx++){
						for(int dy=j*k;dy<(j+1)*k;dy++){
							xs[ind]=dx;
							ys[ind]=dy;
							ind++;
						}
					}
				}
				j++;
			}
		}
		return new FixedPolyomino(xs, ys);
	}
}
