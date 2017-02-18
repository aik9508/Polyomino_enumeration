package cover;
import java.util.LinkedList;

class Column extends Linkedstructure {
	int size,name;

	Column(Linkedstructure up, Linkedstructure down, Linkedstructure left, Linkedstructure right, int name) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.column = this;
		this.name = name;
		this.size = 0;
	}
	
	public String toString(){
		return this.name+"";
	}
}

class Data extends Linkedstructure {
	Data(Linkedstructure up, Linkedstructure down, Linkedstructure left, Linkedstructure right,
			Column column) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.column = column;
	}	
}

public abstract class Linkedstructure {
	Linkedstructure up, down, left, right;
	Column column;
	
	LinkedList<Integer> toList() {
		LinkedList<Integer> l=new LinkedList<Integer>();
		l.add(this.column.name);
		Linkedstructure t=this.right;
		while(t!=this){
			l.add(t.column.name);
			t=t.right;
		}
		return l;
	}
}
