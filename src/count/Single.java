package count;

/** 
 * ClassName: Single <br/> 
 * Function: A new simple linked list data structure. <br/> 
 * Reason: To avoid the current modification exception  <br/> 
 * 
 * @author wangke 
 */
public class Single<V> {
	public V val;
	public Single<V> next;

	public Single(V val, Single<V> next) {
		this.val = val;
		this.next = next;
	}

	public String toString() {
		Single<V> s = this;
		String str = "";
		while (true) {
			str += s.val + "->";
			if (s.next == null)
				break;
			s = s.next;
		}
		return str;
	}
}
