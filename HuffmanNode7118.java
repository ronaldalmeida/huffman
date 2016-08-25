/* ALMEIDA RONALD cs610 PP 7118 */


public class HuffmanNode7118 implements Comparable<HuffmanNode7118> {
	private HuffmanNode7118 left;
	private HuffmanNode7118 right;
	private byte charCode;
	private long freqency;
	private boolean leafNode;

	public HuffmanNode7118(byte code, long freq, boolean leafNode) {
		charCode = code;
		freqency = freq;
		this.leafNode = leafNode;
	}


	public HuffmanNode7118 getLeft7118() {
		return left;
	}

	public void setLeft7118(HuffmanNode7118 left) {
		this.left = left;
	}


	public HuffmanNode7118 getRight7118() {
		return right;
	}

	public void setRight7118(HuffmanNode7118 right) {
		this.right = right;
	}

	public byte getCharCode7118() {
		return charCode;
	}

	public void setCharCode7118(byte charCode) {
		this.charCode = charCode;
	}

	public long getFreqency7118() {
		return freqency;
	}

	public void setFreqency7118(long freqency) {
		this.freqency = freqency;
	}

	public boolean isLeafNode7118() {
		return leafNode;
	}

	public int compareTo(HuffmanNode7118 o) {
		if (freqency < o.freqency)
			return -1;
		if (freqency > o.freqency)
			return 1;
		return 0;
	}

	public static HuffmanNode7118 buildHuffmanTree7118(HeapPriorityQueue7118<HuffmanNode7118> nodesHeap) {
		while (nodesHeap.size7118() > 1) {
			HuffmanNode7118 node1 = nodesHeap.poll7118();
			HuffmanNode7118 node2 = nodesHeap.poll7118();
			HuffmanNode7118 parentNode = new HuffmanNode7118((byte) 0, node1.getFreqency7118() + node2.getFreqency7118(), false);
			parentNode.setLeft7118(node1);
			parentNode.setRight7118(node2);
			nodesHeap.add7118(parentNode);
		}
		return nodesHeap.poll7118();
	}
}


