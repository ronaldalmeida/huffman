/* ALMEIDA RONALD cs610 PP 7118 */


import java.lang.reflect.Array;

public class HeapPriorityQueue7118<Type extends Comparable<Type>> {
	private Type[] heap;
	private int size;

	public HeapPriorityQueue7118(Type[] initial) {
		heap = initial;
		size = initial.length;
		for (int i = size / 2; i >= 0; i--) {
			minHeapify7118(i);
		}
	}

	private void minHeapify7118(int index) {
		int left = getLeft7118(index);
		int right = getRight7118(index);
		int min = 0;
		if (left < size && heap[left].compareTo(heap[index]) == -1) {

			min = left;
		} else {

			min = index;
		}
		if (right < size && heap[right].compareTo(heap[min]) == -1) {
			min = right;

		}
		if (min != index) {
			swap7118(min, index);

			minHeapify7118(min);
		}
	}

	public int size7118() {
		return size;
	}

	public Type peek7118() {
		return size == 0 ? null : heap[0];
	}

	public Type poll7118() {
		if (size == 0)
			return null;
		size--;
		Type temp = heap[0];
		heap[0] = heap[size];
		minHeapify7118(0);
		return temp;
	}

	private int getLeft7118(int index) {
		return 2 * index + 1;
	}

	private int getRight7118(int index) {
		return 2 * index + 2;
	}

	private int getParent7118(int index) {
		return (index - 1) / 2;
	}


	private void swap7118(int i, int j) {
		Type temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	public void add7118(Type t) {
		if (size == heap.length) {
			expand7118();
		}
		int index = size;
		heap[index] = t;
		while (index > 0 && heap[getParent7118(index)].compareTo(heap[index]) == 1) {
			swap7118(index, getParent7118(index));
			index = getParent7118(index);
		}
		size++;
	}

	public boolean isEmpty7118() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	private void expand7118() {
		Type[] newheap = (Type[]) Array.newInstance(Comparable.class, 2 * heap.length);
		for (int i = 0; i < size; i++) {
			newheap[i] = heap[i];
		}
		heap = newheap;
	}

}
