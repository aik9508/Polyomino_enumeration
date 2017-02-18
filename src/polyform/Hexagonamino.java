package polyform;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hexagonamino extends Polyform {
	private static final int XSCALE = 8, YSCALE = 14;
	int xo, yo;

	Hexagonamino(String str) {
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

	Hexagonamino(int[] xs, int[] ys) {
		this.size = xs.length;
		this.init(xs, ys);
	}

	Hexagonamino(short[] coords, int width, int height, int size, int xo, int yo) {
		this.coords = coords;
		this.width = width;
		this.height = height;
		this.size = size;
		this.xo = xo;
		this.yo = yo;
	}

	void init(int[] xs, int[] ys) {
		xo = min(xs);
		yo = min(ys);
		width = max(xs) - xo + 1;
		height = max(ys) - yo + 1;
		coords = new short[width];
		for (int i = 0; i < size; i++)
			coords[xs[i] - xo] = (short) (coords[xs[i] - xo] + (1 << (ys[i] - yo)));
	}

	public void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges, int width) {
		int translation = 0;
		if (width <= 0)
			translation = this.width;
		else
			translation = width;
		int[] next = new int[] { 1, 2, 3, 4, 5, 0 };
		for (int i = 0; i < this.width; i++) {
			int yss = coords[i];
			int j = 0;
			for (; yss != 0; yss = (yss >> 1)) {
				if ((yss & 1) != 0) {
					int xlattice = (i + j + xo + yo) * 3, ylattice = j - i + translation + yo - xo;
					int[] xs = new int[] { xlattice * XSCALE, (xlattice + 1) * XSCALE, (xlattice + 3) * XSCALE,
							(xlattice + 4) * XSCALE, (xlattice + 3) * XSCALE, (xlattice + 1) * XSCALE };
					int[] ys = new int[] { ylattice * YSCALE, (ylattice + 1) * YSCALE, (ylattice + 1) * YSCALE,
							ylattice * YSCALE, (ylattice - 1) * YSCALE, (ylattice - 1) * YSCALE };
					realCoords.add(xs);
					realCoords.add(ys);
					for (int k = 0; k < 6; k++) {
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

	Polyform translation(int dx, int dy) {
		return new Hexagonamino(coords, width, height, size, dx, dy);
	}

	@Override
	Polyform rotation(int angle) {
		angle %= 6;
		if (angle < 0)
			angle += 6;
		if (angle == 0)
			return this;
		int[] xs = new int[size], ys = new int[size];
		int[] xtmp = new int[size], ytmp = new int[size];
		getxy(xs, ys);
		if (angle == 1)
			for (int i = 0; i < size; i++) {
				xtmp[i] = (-ys[i]);
				ytmp[i] = xs[i] + ys[i];
			}
		else if (angle == 2)
			for (int i = 0; i < size; i++) {
				xtmp[i] = -xs[i] - ys[i];
				ytmp[i] = xs[i];
			}
		else if (angle == 3)
			for (int i = 0; i < size; i++) {
				xtmp[i] = -xs[i];
				ytmp[i] = -ys[i];
			}
		else if (angle == 4)
			for (int i = 0; i < size; i++) {
				xtmp[i] = ys[i];
				ytmp[i] = -xs[i] - ys[i];
			}
		else
			for (int i = 0; i < size; i++) {
				xtmp[i] = xs[i] + ys[i];
				ytmp[i] = -xs[i];
			}
		int minx = min(xtmp);
		int miny = min(ytmp);
		for (int i = 0; i < size; i++) {
			xtmp[i] -= minx;
			ytmp[i] -= miny;
		}
		return new Hexagonamino(xtmp, ytmp);
	}

	Polyform reflection(char xy) {
		short[] newcoords = new short[height];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if ((coords[j] & (1 << i)) != 0)
					newcoords[i] = (short) (newcoords[i] + (1 << j));
		return new Hexagonamino(newcoords, height, width, size, 0, 0);
	}

	public HashSet<Polyform> freePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		Polyform py = this.reflection('H');
		hst.add(py);
		hst.add(this);
		for (int i = 1; i < 6; i++) {
			hst.add(this.rotation(i));
			hst.add(py.rotation(i));
		}
		return hst;
	}

	public HashSet<Polyform> oneSidePolyform() {
		HashSet<Polyform> hst = new HashSet<Polyform>();
		hst.add(this);
		for (int i = 1; i < 6; i++)
			hst.add(this.rotation(i));
		return hst;
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
	void computeCoordinates(LinkedList<int[]> realCoords, HashSet<Edge> edges) {
		computeCoordinates(realCoords, edges, -1);
	}

	static int min(short[] x) {
		int min = x[0];
		for (int i = 1; i < x.length; i++)
			min = Math.min(x[i], min);
		return min;
	}

	@Override
	public Polyform dilate(int k) {
		return null;
	}
}
