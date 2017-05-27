package me.lumpchen.afp.ioca.filter;

import java.io.IOException;
import java.io.InputStream;

public class CCITTFaxxFilter {

	public byte[] decode(InputStream inputStream, int cols, int rows) throws IOException {
		int compression = 0; //g4?
		boolean encodedByteAlign = false;
		int arraySize = (cols + 7) / 8 * rows;
		byte[] decompressed = new byte[arraySize];
		CCITTFaxDecoderStream s;
		int type;
		long tiffOptions;

		if (compression == 0) {
			tiffOptions = encodedByteAlign ? TIFFExtension.GROUP3OPT_BYTEALIGNED : 0;
			type = TIFFExtension.COMPRESSION_CCITT_MODIFIED_HUFFMAN_RLE;
		} else {
			if (compression > 0) {
				tiffOptions = encodedByteAlign ? TIFFExtension.GROUP3OPT_BYTEALIGNED : 0;
				tiffOptions |= TIFFExtension.GROUP3OPT_2DENCODING;
				type = TIFFExtension.COMPRESSION_CCITT_T4;
			} else {
				// k < 0
				tiffOptions = encodedByteAlign ? TIFFExtension.GROUP4OPT_BYTEALIGNED : 0;
				type = TIFFExtension.COMPRESSION_CCITT_T6;
			}
		}

		s = new CCITTFaxDecoderStream(inputStream, cols, type, TIFFExtension.FILL_LEFT_TO_RIGHT, tiffOptions);
		readFromDecoderStream(s, decompressed);

		// invert bitmap
		boolean blackIsOne = false;
		if (!blackIsOne) {
			invertBitmap(decompressed);
		}
		
		return decompressed;
	}

	void readFromDecoderStream(CCITTFaxDecoderStream decoderStream, byte[] result) throws IOException {
		int pos = 0;
		int read;
		while ((read = decoderStream.read(result, pos, result.length - pos)) > -1) {
			pos += read;
			if (pos >= result.length) {
				break;
			}
		}
		decoderStream.close();
	}

	private void invertBitmap(byte[] bufferData) {
		for (int i = 0, c = bufferData.length; i < c; i++) {
			bufferData[i] = (byte) (~bufferData[i] & 0xFF);
		}
	}
}
