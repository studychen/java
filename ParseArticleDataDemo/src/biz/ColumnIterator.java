package biz;

import java.util.Iterator;

public class ColumnIterator implements Iterator {
	private int type;
	private int totalPage;

	public ColumnIterator(int type, int totalPage) {
		this.type = type;
		this.totalPage = totalPage;
	}

	public boolean hasNext() {
		return totalPage-- > 0;
	}

	public Object next() {
		return null;
	}

	public void remove() {
		throw new UnsupportedOperationException("not support remove");

	}

}
