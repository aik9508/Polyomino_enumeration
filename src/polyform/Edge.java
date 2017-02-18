package polyform;

public class Edge {
	public int width;
	public int x1, x2, y1, y2;

	public Edge(int x1, int y1, int x2, int y2, int width) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.width = width;
	}

	public boolean equals(Object o) {
		Edge that = (Edge) o;
		return this.x1 == that.x1 && this.y1 == that.y1 && this.x2 == that.x2 && this.y2 == that.y2
				|| this.x1 == that.x2 && this.y1 == that.y2 && this.x2 == that.x1 && this.y2 == that.y1;
	}

	public int hashCode() {
		int h = (x1 + x2) * 13 + (y1 + y2) * 31;
		return h;
	}
}
