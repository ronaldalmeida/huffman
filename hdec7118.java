
/* ALMEIDA RONALD cs610 PP 7118 */


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class hdec7118 {

	private HuffmanNode7118 root;

	private long fileLength;

	private String srcFileName;
	
	public hdec7118(String fileName) throws IOException {

		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(fileName)));

		long[] freq = new long[256];
		srcFileName = "";
		fileLength = 0;

		int countSize = 0;
		int x;

		while ((x = inputStream.read()) != 0) {
			srcFileName = srcFileName + (char) (x);
		}
		 FileReader inputStream1 = null;
	        try {
	            inputStream1 = new FileReader(srcFileName+".huf");
	            int c;
	            int k=0;
	            while ((c = inputStream1.read()) != -1) {
	                k++;
	            }
	            char[] infile=new char[k];
	            while ((c = inputStream1.read()) != -1) {
	                infile[k]=(char) c;
	                //build7118(infile[k]);
	            }
	        }
	        finally {
             if (inputStream1 != null) {
                 inputStream1.close();
             }
	        }

		for (int i = 0; i < freq.length;) {
			x = inputStream.read();
			if (x == (byte) ',') {
				i++;
				if (freq[i - 1] != 0)
					countSize++;
			} else {
				freq[i] = freq[i] * 10 + (byte) (x - '0');
			}
		}

		HuffmanNode7118[] nodesList = new HuffmanNode7118[countSize];
		int listIndex = 0;

		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0)
				nodesList[listIndex++] = new HuffmanNode7118((byte) (i - 128), freq[i], true);
			fileLength += freq[i];
		}

		HeapPriorityQueue7118<HuffmanNode7118> nodesHeap = new HeapPriorityQueue7118<>(nodesList);

		root = HuffmanNode7118.buildHuffmanTree7118(nodesHeap);

		decompress7118(inputStream);
		inputStream.close();
	}


	private void decompress7118(BufferedInputStream inputStream) throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(srcFileName)));

		byte[] inBuffer = new byte[10 * 1024];
		byte[] outBuffer = new byte[10 * 1024];

		int inOffset, outOffset = 0, bitCounter = 7;
		HuffmanNode7118 pointer = root;

		long decompSize = 0;

		while ((inOffset = inputStream.read(inBuffer)) != -1) {
			for (int i = 0; i < inOffset;) {

				byte currentChar = inBuffer[i];

				if ((currentChar & (1 << bitCounter)) == 0) {
					pointer = pointer.getLeft7118();
					if (pointer.isLeafNode7118()) {
						outBuffer[outOffset] = pointer.getCharCode7118();
						outOffset++;
						decompSize++;
						pointer = root;

						if (outOffset == outBuffer.length) {
							// flush when buffer full
							outputStream.write(outBuffer, 0, outOffset);
							outOffset = 0;
						}
					}
				} else {
					pointer = pointer.getRight7118();
					if (pointer.isLeafNode7118()) {
						outBuffer[outOffset] = pointer.getCharCode7118();
						outOffset++;
						decompSize++;
						pointer = root;

						if (outOffset == outBuffer.length) {
							// flush when buffer full
							outputStream.write(outBuffer, 0, outOffset);
							outOffset = 0;
						}
					}
				}
				bitCounter--;
				if (bitCounter < 0) {
					bitCounter = 7;
					i++;
				}

				if (decompSize == fileLength) {
					break;
				}
			}
		}

		outputStream.write(outBuffer, 0, outOffset);
		outputStream.close();
	}

	public static void main(String[] args) throws IOException {

		new hdec7118(args[0]);
		System.out.println("Decompress Completed");

	}
}
