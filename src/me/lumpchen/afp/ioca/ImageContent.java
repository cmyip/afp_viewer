package me.lumpchen.afp.ioca;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
		
		int compression = TIFFExtension.COMPRESSION_CCITT_T6;
		if (compressionAlg == CompressionAlgrithm.G4) {
			compression = TIFFExtension.COMPRESSION_CCITT_T6;
			int order = TIFFExtension.FILL_LEFT_TO_RIGHT;
			if (bitOrder == BitOrder.Left_to_right) {
				order = TIFFExtension.FILL_LEFT_TO_RIGHT;
			} else if (bitOrder == BitOrder.Right_to_left) {
				order = TIFFExtension.FILL_RIGHT_TO_LEFT;
			}
			try {
				InputStream src = new ByteArrayInputStream(this.imageData);
				int col = this.size.getCol();
				int row = this.size.getRow();
				byte[] dest = CCITTFaxxFilter.decode(src, col, row, compression, order, false, false);
				return dest;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (compressionAlg == CompressionAlgrithm.JPEG) {
			return this.imageData;
		}
		
		throw new java.lang.IllegalArgumentException("Unspported compression algoritm: " + compressionAlg);
	}
	
	private BufferedImage getBufferedImage() throws IOException {
		CompressionAlgrithm alg = this.encoding.getAlgorhtim();
		byte[] decoded = this.decodeData();
		
		if (alg == CompressionAlgrithm.G4) {
			int row = this.size.getRow();
			BufferedImage img = new BufferedImage(this.size.getCol(), row, BufferedImage.TYPE_BYTE_BINARY);
			WritableRaster newRaster = img.getRaster();

			int size = newRaster.getDataBuffer().getSize();
			for (int i = 0; i < size; i++) {
				newRaster.getDataBuffer().setElem(i, (decoded[i] & 0xFF));
			}
			img.setData(newRaster);
			return img;
		} else if (alg == CompressionAlgrithm.JPEG) {
			BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(decoded));
			return bimg;
		}
		throw new java.lang.IllegalArgumentException("Unspported compression algoritm: " + alg);
	}
	
	public BufferedImage getJavaImage() {
		try {
			BufferedImage img = this.getBufferedImage();
//			ImageIO.write(img, "jpg", new File("C:/temp/afp/xpression/teset.jpg"));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
