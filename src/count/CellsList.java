package count;

/**
 *This class represent a point in by the pair of its coordinates (x, y).<br/>
 *This class is similar to the class Pair in this project because they were written by 
 *two different people.
 */
class Cell {
	int x, y;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}

/**
 * This class implements a simple linked list of points which are represented by the class Cell.
 * The purpose of writing this class is to represent the list of untried cells in the algorithm
 * of Redelmeier.<br/>
 * This class is similar to the class Single in this project because they were written by two 
 * different people.
 */
public class CellsList {
	Cell point;
	CellsList next;
	
	public CellsList (Cell c) {
		this.point = c;
		this.next = null;
	}
	
	public CellsList (Cell c, CellsList next) {
		this.point = c;
		this.next = next;
	}
	
	public String toString () {
		CellsList it = this;
		StringBuffer sb = new StringBuffer();
		while (it != null) {
			sb.append(it.point + " ");
			it = it.next;
		}
		return sb.toString();
	}

}
