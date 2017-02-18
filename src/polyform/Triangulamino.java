package polyform;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Triangulamino extends Polyform {
	private final static int XSCALE = 16, YSCALE = 28;
	public int xmodification;
	public int xo, yo;

	Triangulamino(String str) {
		LinkedList<String> l = new LinkedList<String>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			l.add(m.group());
		}
		size = l.size() / 2;
		int[] xs = new int[size];
		int[] ys = new int[size];
		int index = 0;
		for (String s : l) {
			int sToi = Integer.parseInt(s);
			if (index % 2 == 0)
				xs[index / 2] = sToi;
			else
				ys[index / 2] = sToi;
			index++;
		}
		this.init(xs, ys);
	}

	Triangulamino(int[] xs, int[] ys) {
		this.size = xs.length;
		this.init(xs, ys);
	}

	Triangulamino(short[] coords, int width, int height, int size, int xo, int yo, int xmodification) {
		this.coords = coords;
		this.width = width;
		this.height = height;
		this.size = size;
		this.xo = xo;
		this.yo = yo;
		this.xmodification = xmodification;
	}

	private void init(int[] xs, int[] ys) {
		xo = min(xs);
		yo = min(ys);
		xmodification = (xo + yo) % 2 == 0 ? 0 : 1;
		width = max(xs) - xo + 1;
		height = max(ys) - yo + 1;
		coords = new short[width];
		for (int i = 0; i < size; i++)
			coords[xs[i] - xo] = (short) (coords[xs[i] - xo] + (1 << (ys[i] - yo)));
	}

	@Override
	void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges) {
		int[] next = new int[] { 1, 2, 0 };
		for (int i = 0; i < this.width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) != 0) {
					int xlattice = i + xo, ylattice = j + yo + (i + j + xmodification) % 2;
					int[] xs = new int[] { xlattice * XSCALE, (xlattice + 2) * XSCALE, (xlattice + 1) * XSCALE, };
					int[] ys = new int[] { ylattice * YSCALE, ylattice * YSCALE,
							(ylattice + ((i + j + xmodification) % 2 == 0 ? 1 : -1)) * YSCALE };
					realCoords.add(xs);
					realCoords.add(ys);
					for (int k = 0; k < 3; k++) {
						Edge e = new Edge(xs[k], ys[k], xs[next[k]], ys[next[k]], 2);
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
	Polyform rotation(int angle) {
		angle = angle % 6;
		if (angle < 0)
			angle += 6;
		if (angle == 0)
			return this;
		int[] xs = new int[size], ys = new int[size];
		int[] xtmp = new int[size], ytmp = new int[size];
		getxy(xs, ys);
		// We compute minimum values of xs and ys in order to translate our new
		// triangle to the origin of coordinates. However, a translation is not
		// authorized unless the sum of two translation components is even. So
		// we modify the value of minx when necessary.
		if (angle == 1)
			for (int i = 0; i < size; i++) {
				int ud = (xs[i] + ys[i]) % 2;
				// ud = 0 means it is an inverted triangle. ud = 1 means it is a
				// positive triangle
				xtmp[i] = (xs[i] - 3 * ys[i] - 2 - ud) / 2;
				ytmp[i] = (xs[i] + ys[i] + ud) / 2;
			}
		else if (angle == 2)
			for (int i = 0; i < size; i++) {
				int ud = (xs[i] + ys[i]) % 2;
				xtmp[i] = -(xs[i] + 3 * ys[i] + 4 + ud) / 2;
				ytmp[i] = (xs[i] - ys[i] - ud) / 2;
			}
		else if (angle == 3)
			for (int i = 0; i < size; i++) {
				xtmp[i] = -xs[i] - 2;
				ytmp[i] = -ys[i] - 1;
			}
		else if (angle == 4)
			for (int i = 0; i < size; i++) {
				int ud = (xs[i] + ys[i]) % 2;
				xtmp[i] = -(xs[i] - 3 * ys[i] + 2 - ud) / 2;
				ytmp[i] = -(xs[i] + ys[i] + 2 + ud) / 2;
			}
		else
			for (int i = 0; i < size; i++) {
				int ud = (xs[i] + ys[i]) % 2;
				xtmp[i] = (xs[i] + 3 * ys[i] + ud) / 2;
				ytmp[i] = -(xs[i] - ys[i] + 2 - ud) / 2;
			}
		int minx = min(xtmp);
		int miny = min(ytmp);
		int dx = (minx + miny) % 2 == 0 ? 0 : -1;
		for (int i = 0; i < size; i++) {
			xtmp[i] -= minx + dx;
			ytmp[i] -= miny;
		}
		return new Triangulamino(xtmp, ytmp);
	}

	@Override
	Polyform reflection(char xy) {
		short[] newcoords = new short[width];
		if (xy == 'V') {
			int newxmodification = (width + xmodification + 1) % 2 == 0 ? 0 : 1;
			for (int i = 0; i < width; i++)
				newcoords[i] = coords[width - i - 1];
			return new Triangulamino(newcoords, width, height, size, newxmodification, 0, newxmodification);
		} else {
			int newxmodification = (xmodification + height + 1) % 2 == 0 ? 1 : 0;
			for (int i = 0; i < width; i++)
				newcoords[i] = Polyform.inverseshort(coords[i], height);
			return new Triangulamino(newcoords, width, height, size, newxmodification, 0, newxmodification);
		}
	}

	@Override
	public HashSet<Polyform> freePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		Polyform py = this.reflection('H');
		hst.add(this);
		hst.add(py);
		for (int i = 1; i < 6; i++) {
			hst.add(this.rotation(i));
			hst.add(py.rotation(i));
		}
		return hst;
	}

	@Override
	public HashSet<Polyform> oneSidePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		hst.add(this);
		for (int i = 1; i < 6; i++)
			hst.add(this.rotation(i));
		return hst;
	}

	@Override
	Polyform translation(int dx, int dy) {
		if ((dx + dy) % 2 == 1)
			throw new Error();
		return new Triangulamino(coords, width, height, size, dx, dy, xmodification);
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
	void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges, int width) {
		computeCoordinates(realCoords, edges);

	}

	static int min(short[] x) {
		int min = x[0];
		for (int i = 1; i < x.length; i++)
			min = Math.min(x[i], min);
		return min;
	}

	static int max(short[] x) {
		int max = x[0];
		for (int i = 1; i < x.length; i++)
			max = Math.max(x[i], max);
		return max;
	}

	public int getWidth() {
		return width + xmodification;
	}

	public boolean equals(Object o) {
		Triangulamino that = (Triangulamino) o;
		if ((this.xmodification != that.xmodification) || (this.size != that.size) || (this.width != that.width)
				|| (this.height != that.height))
			return false;
		for (int i = 0; i < width; i++)
			if (this.coords[i] != that.coords[i])
				return false;
		return true;
	}
	
	public void getxy(int[] xs, int[] ys) {
		int ind = 0;
		for (int i = 0; i < width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) == 1) {
					xs[ind] = i+xmodification;
					ys[ind] = j;
					ind++;
				}
				j++;
			}
		}
	}

	@Override
	public Polyform dilate(int k) {
		// TODO Auto-generated method stub
		return null;
	}
}
