package me.lumpchen.afp.ioca;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.ioca.ImageEncoding.BitOrder;
import me.lumpchen.afp.ioca.ImageEncoding.CompressionAlgrithm;
import me.lumpchen.afp.ioca.filter.CCITTFaxxFilter;
import me.lumpchen.afp.ioca.filter.TIFFExtension;

public class ImageContent {
	
	public static final int Begin = 0x91;
	public static final int End = 0x93;
	
	private int OBJTYPE;
	
	private ImageSize size;
	private ImageEncoding encoding;
	private int IDESize;
	private int lutID;
	private int[] bands;
	private IDEStructure ideStructure;
	private ExternalAlgorithmSpecification alg;
	private byte[] imageData;
	
	public ImageContent() {
	}
	
	public void setImageSize(ImageSize size) {
		this.size = size;
	}
	
	public void setImageEncoding(ImageEncoding encoding) {
		this.encoding = encoding;
	}
	
	public void setIDESize(int size) {
		this.IDESize = size;
	}
	
	public void setLUTID(int lutID) {
		this.lutID = lutID;
	}
	
	public void setImageBands(int[] bands) {
		this.bands = bands;
	}
	
	public void setIDEStructure(IDEStructure ide) {
		this.ideStructure = ide;
	}
	
	public void setExternalAlgorithmSpecification(ExternalAlgorithmSpecification alg) {
		this.alg = alg;
	}
	
	public byte[] getImageData() {
		return this.imageData;
	}
	
	public byte[] decodeData() {
		CompressionAlgrithm compressionAlg = this.encoding.getAlgorhtim();
		BitOrder bitOrder = this.encoding.getBitOrder();
		
		try {
			byte[] src = CCITTFaxxFilter.decode(new ByteArrayInputStream(this.imageData), 
					this.size.getCol(), this.size.getRow(), TIFFExtension.COMPRESSION_CCITT_T6, 
					TIFFExtension.FILL_LEFT_TO_RIGHT, false, false);
			return src;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage getJavaImage() {
		byte[] src = this.decodeData();
		
		int col = (this.size.getCol() + 7) / 8;
		int row = this.size.getRow();
		BufferedImage img = new BufferedImage(col, row, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster newRaster = img.getRaster();
		newRaster.setDataElements(0, 0, col, row, src);
		img.setData(newRaster);
		return img;
	}
	
	public void read(AFPInputStream in) throws IOException {
		int id = in.readCode();
		if (id != ImageContent.Begin) {
			throw new IOException("Invalid Image Content begin mark(0x91): " + id);
		}
		int length = in.readUBin(1);
		if (length > 0) {
			this.OBJTYPE = in.readCode(); // must be 0xFF
		}
		
		ByteArrayOutputStream imageDataStream = new ByteArrayOutputStream();
		
		while (true) {
			id = in.readCode();
			
			if (id == ImageSize.ID) {
				ImageSize size = new ImageSize();
				size.read(in);
				this.setImageSize(size);
			} else if (id == ImageEncoding.ID) {
				ImageEncoding encoding = new ImageEncoding();
				encoding.read(in);
				this.setImageEncoding(encoding);
			} else if (id == ImageSegment.IDE_Size_ID) {
				length = in.readUBin(1);
				int IDESZ = in.readUBin(1);
				this.setIDESize(IDESZ);
			} else if (id == ImageSegment.IDE_LUT_ID) {
				length = in.readUBin(1);
				int LUTID = in.readCode(1);
				this.setLUTID(LUTID);
			} else if (id == ImageSegment.Band_Image_ID) {
				length = in.readUBin(1);
				int BCOUNT = in.readUBin(1);
				int[] BITCN = new int[BCOUNT];
				for (int i = 0; i < BCOUNT; i++) {
					BITCN[i] = in.readUBin(1);
				}
			} else if (id == IDEStructure.ID) {
				IDEStructure ide = new IDEStructure();
				ide.read(in);
				this.setIDEStructure(ide);
			} else if (id == ExternalAlgorithmSpecification.ID) {
				ExternalAlgorithmSpecification alg = new ExternalAlgorithmSpecification();
				alg.read(in);
				this.setExternalAlgorithmSpecification(alg);
			} else if (id == ImageSegment.Double_Byte_ID_0) {
				int id_1 = in.readCode();
				if (id_1 == ImageData.ID_1) {
					ImageData ipd = new ImageData();
					ipd.read(in);
					imageDataStream.write(ipd.getData());
				}
			} else if (id == ImageContent.End) {
				this.imageData = imageDataStream.toByteArray();
				length = in.readUBin(1);
				if (length != 0) {
					// throw exception
				}
				break;
			}
		}
		imageDataStream.close();
	}
	
}
