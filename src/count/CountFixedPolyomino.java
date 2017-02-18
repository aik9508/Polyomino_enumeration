package count;

/**
 * This class implement the fixed polyominoes enumeration algorithm of Redelmeier.
 */
public class CountFixedPolyomino {

	/**
	 * countFixedRecu: The recursive function of countFixed(int P). This function count all
	 * fixed polyominoes on the p-th level in a branch of the Depth-first Search algorithm, 
	 * and add the counting result to array count. After the execution of this function, the
	 * toppest element of untried will be removed and marked as occupied in the matrix occupied. 
	 * @param untried the list of all untried points
	 * @param occupied a matrix which marks all occupied or blocked points
	 * @param p the current level of search
	 * @param P the deepest level of search, that is the size of polyominoes
	 * @param count the counting result container
	 */
	static private void countFixedRecu(CellsList untried, boolean[][] occupied, int p, int P, long[] count) {
		assert count.length == P+1;
		if (p == P) {
			while (untried != null){
				count[p] ++;
				untried = untried.next;
			}
			return;
		}
		while (untried != null) {
			Cell trythis = untried.point;
			untried = untried.next;
			count[p] ++;
			int xInArray = trythis.x+P-1, yInArray = trythis.y+1;
			occupied[xInArray][yInArray] = true;
			CellsList untriedwithnewcells = untried;
			if (!occupied[xInArray-1][yInArray]){
				occupied[xInArray-1][yInArray] = true;
				untriedwithnewcells = new CellsList(new Cell(trythis.x-1, trythis.y), untriedwithnewcells);
			}
			if (!occupied[xInArray][yInArray+1]){
				occupied[xInArray][yInArray+1] = true;
				untriedwithnewcells = new CellsList(new Cell(trythis.x, trythis.y+1), untriedwithnewcells);
			}
			if (!occupied[xInArray+1][yInArray]){
				occupied[xInArray+1][yInArray] = true;
				untriedwithnewcells = new CellsList(new Cell(trythis.x+1, trythis.y), untriedwithnewcells);
			}
			if (!occupied[xInArray][yInArray-1]){
				occupied[xInArray][yInArray-1] = true;
				untriedwithnewcells = new CellsList(new Cell(trythis.x, trythis.y-1), untriedwithnewcells);
			}
			CellsList deleteStart = untriedwithnewcells;
			countFixedRecu(untriedwithnewcells, occupied, p + 1, P, count);
			while (deleteStart != untried) {
				Cell deletethis = deleteStart.point;
				occupied[deletethis.x+P-1][deletethis.y+1] = false;
				deleteStart = deleteStart.next;
			}
		}
		return;
	}
	
	/**
	 * countFixed: counts the number of polyominoes of size 0, 1, ... , P.
	 * @param P the maximum of size
	 * @return An array which contains the number of polyominoes of all size from 0 to P.
	 */
	static public long[] countFixed(int P) {
		if (P == 0) return new long[]{0};	
		if (P == 1) return new long[]{0,1};
		Cell origin = new Cell(0, 0);
		long[] count = new long[P+1];
		CellsList untried = new CellsList(origin);
		boolean[][] occupied = new boolean[2*P-1][P+1];
		for (int i=0;i<2*P-1;i++)
			occupied[i][0] = true;
		for (int i=0;i<P;i++)
			occupied[i][1] = true;
		countFixedRecu(untried, occupied, 1, P, count);
		return count;
	}
}
