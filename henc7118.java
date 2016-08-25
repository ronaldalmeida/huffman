/* ALMEIDA RONALD cs610 PP 7118 */


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;

public class henc7118 {

	// hold huffman code and lengths of this patterns
	private CharCode[] charCodes;
	private String srcFileName;
	// freq[i] indicate number of times char having ASCII = i appear in file
	private long[] freq;
	//private Map<Character,Integer> mapF = new HashMap<Character, Integer>();

	public henc7118(String fileName) throws IOException {
		// buffer to read chunks
		byte[] buffer = new byte[10 * 1024];
		freq = new long[256];
		
		srcFileName = fileName;
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(srcFileName)));

		int offset;
		while ((offset = inputStream.read(buffer)) != -1) {
			for (int i = 0; i < offset; i++) {
				freq[buffer[i] + 128]++;
			}
		}
		inputStream.close();
		int countSize = 0;
		// count number of freqs that not equal zero
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0)
				countSize++;
		}

		HuffmanNode7118[] nodesList = new HuffmanNode7118[countSize];
		int listIndex = 0;

		// select only freq that have values > 0
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0)
				nodesList[listIndex++] = new HuffmanNode7118((byte) (i - 128), freq[i], true);
		}
		// create heap with nodes
		HeapPriorityQueue7118<HuffmanNode7118> nodesHeap = new HeapPriorityQueue7118<>(nodesList);

		charCodes = new CharCode[256];

		// create huffman tree
		HuffmanNode7118 root = HuffmanNode7118.buildHuffmanTree7118(nodesHeap);

		// set huffman codes
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				charCodes[i] = new CharCode();
				charCodes[i].code = huffmanEncode7118(root, (byte) (i - 128), 0);
			}
		}
	}

	private int huffmanEncode7118(HuffmanNode7118 node, byte charCode, int byteCount) {
		if (node.isLeafNode7118()) {
			if (node.getCharCode7118() == charCode) {
				// set length of code
				charCodes[charCode + 128].codeLength = byteCount;
				return 0;
			} else {
				// invalid solution
				return -1000000;
			}
		} else {
			// recursively select left/right to get right solution
			return Math.max(huffmanEncode7118(node.getLeft7118(), charCode, byteCount + 1) * 2,
					huffmanEncode7118(node.getRight7118(), charCode, byteCount + 1) * 2 + 1);
		}
		
	}
	
//	private void build7118(char ch)
//	{
//		if( mapF.containsKey(ch))
//    	{   Integer value = mapF.get(ch);
//    		mapF.put(ch, value+1);
//    	}
//    	else
//    	{ 	mapF.put(ch, 1);}
//    		
//	}

	private void compress7118() throws IOException {
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(srcFileName)));

		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(new File(srcFileName + ".huf")));

		StringBuilder builder = new StringBuilder();

		// append source file name at start of commpresed file
		builder.append(srcFileName + (char) 0);

		// append freq array to build tree when decompress
		for (int i = 0; i < freq.length; i++) {
			builder.append(freq[i] + ",");
		}
		
		outputStream.write(builder.toString().getBytes());

		// buffers to increase read/write speed
		byte[] inBuffer = new byte[10 * 1024];
		byte[] outBuffer = new byte[10 * 1024];

		int inOffset, outOffset = 0, bitCounter = 0;
		int lastChar = 0;
		// read source file
		 FileReader inputStream1 = null;
	        try {
	            inputStream1 = new FileReader(srcFileName);
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
	            
	         //begin   

		while ((inOffset = inputStream.read(inBuffer)) != -1) {
			for (int i = 0; i < inOffset; i++) {

				byte currentChar = inBuffer[i];

				// get code and its length
				int code = charCodes[currentChar + 128].code;
				int codeLength = charCodes[currentChar + 128].codeLength;

				for (int j = 0; j < codeLength; j++) {
					// shift bits left and add new bit
					outBuffer[outOffset] = (byte) (2 * outBuffer[outOffset] + (code % 2));
					lastChar = outOffset + 1;
					bitCounter++;
					code /= 2;
					if (bitCounter == 8) {
						// reach to end of current byte
						outOffset++;
						lastChar = outOffset;
						bitCounter = 0;
						if (outOffset == outBuffer.length) {
							// flush when buffer full
							outputStream.write(outBuffer);
							outOffset = 0;
						}
					}
				}
			}
		}
		// read last bits
		while (bitCounter != 8 && lastChar > 0) {
			outBuffer[lastChar - 1] = (byte) (outBuffer[lastChar - 1] * 2);
			bitCounter++;
		}

		outputStream.write(outBuffer, 0, lastChar);
		outputStream.close();
		inputStream.close();

	}

	public static void main(String[] args) throws IOException {

		henc7118 hEnc = new henc7118(args[0]);
		hEnc.compress7118();
		System.out.println("Compression Completed");

	}

	private class CharCode {
		int code;
		int codeLength;
	}
}
