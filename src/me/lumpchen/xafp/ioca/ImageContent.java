package me.lumpchen.xafp.ioca;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.lumpchen.xafp.AFPException;
import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ioca.ImageEncoding.BitOrder;
import me.lumpchen.xafp.ioca.ImageEncoding.CompressionAlgrithm;
import me.lumpchen.xafp.ioca.filter.CCITTFaxxFilter;
import me.lumpchen.xafp.ioca.filter.TIFFExtension;

public class ImageContent {
	
	public static final int Begin = 0x91;
	public static final int End = 0x93;
	
	private int OBJTYPE;
	
	private ImageSize size;
	private ImageEncoding encoding;
	private BandImage bandImage;
	private LUTID LUTID;
	private IDESize IDESize;
	private int[] bands;
	private IDEStructure ideStructure;
	private ExternalAlgorithmSpecification alg;
	private byte[] imageData;
	
	private List<Tile> tileImageList;

	
	public ImageContent() {
	}
	
	public boolean isTile() {
		if (this.tileImageList == null || this.tileImageList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public void setImageSize(ImageSize size) {
		this.size = size;
	}
	
	public void setImageEncoding(ImageEncoding encoding) {
		this.encoding = encoding;
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
	
	private static byte[] decodeData(ImageEncoding encoding, int col, int row, byte[] imageData) {
		CompressionAlgrithm compressionAlg = encoding.getAlgorhtim();
		BitOrder bitOrder = encoding.getBitOrder();
		
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
				InputStream src = new ByteArrayInputStream(imageData);
				byte[] dest = CCITTFaxxFilter.decode(src, col, row, compression, order, false, false);
				return dest;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (compressionAlg == CompressionAlgrithm.JPEG) {
			return imageData;
		} else if (compressionAlg == CompressionAlgrithm.None) {
			return imageData;
		}
		
		throw new java.lang.IllegalArgumentException("Unspported compression algoritm: " + compressionAlg);
	}
	
	private static BufferedImage getBufferedImage(ImageEncoding encoding, IDEStructure ideStructure,
			int col, int row, byte[] imageData) throws IOException {
		CompressionAlgrithm alg = encoding.getAlgorhtim();
		byte[] decoded = decodeData(encoding, col, row, imageData);
		
		if (alg == CompressionAlgrithm.G4) {
//			int row = this.size.getRow();
//			BufferedImage img = new BufferedImage(this.size.getCol(), row, BufferedImage.TYPE_INT_ARGB);
//			WritableRaster newRaster = img.getRaster();
//
//			int size = newRaster.getDataBuffer().getSize() / 8;
//			for (int i = 0; i < size; i++) {
//				if (decoded[i] != -1) {
//					for (int bit = 0; bit < 8; bit++) {
//						int b = (decoded[i] >> (7 - bit)) & 0x01;
//						if (b == 1) {
//							newRaster.getDataBuffer().setElem(8 * i + bit, 0xff000000);
//						}
//					}
//				}
//			}
//			img.setData(newRaster);
//			return img;
			
			BufferedImage img = new BufferedImage(col, row, BufferedImage.TYPE_BYTE_BINARY);
			WritableRaster newRaster = img.getRaster();

			int size = newRaster.getDataBuffer().getSize();
			for (int i = 0; i < size; i++) {
				newRaster.getDataBuffer().setElem(i, (decoded[i] & 0xFF));
			}
			img.setData(newRaster);
			return makeTransprency(img);
		} else if (alg == CompressionAlgrithm.JPEG) {
			BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(decoded));
			return bimg;
		} else if (alg == CompressionAlgrithm.None) {
			BufferedImage img = new BufferedImage(col, row, BufferedImage.TYPE_BYTE_BINARY);
			WritableRaster newRaster = img.getRaster();

			int size = newRaster.getDataBuffer().getSize();
			if (ideStructure != null && ideStructure.isAdditive()) {
				for (int i = 0; i < size; i++) {
					newRaster.getDataBuffer().setElem(i, (decoded[i] & 0xFF));
				}
			} else {
				for (int i = 0; i < size; i++) {
					newRaster.getDataBuffer().setElem(i, ((~decoded[i]) & 0xFF));
				}
			}

			img.setData(newRaster);
			return makeTransprency(img);
		}
		throw new java.lang.IllegalArgumentException("Unspported compression algoritm: " + alg);
	}
	
	private static BufferedImage makeTransprency(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		
		BufferedImage a = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int p = img.getRGB(i, j);
				if (p != -1) {
					a.setRGB(i, j, p);
				}
			}
		}
		return a;
	}

	public BufferedImage getBufferedImage() {
		try {
			if (!this.isTile()) {
				BufferedImage img = getBufferedImage(this.encoding, this.ideStructure,
						this.size.getCol(), this.size.getRow(), this.imageData);
//				ImageIO.write(img, "jpg", new File("C:/temp/afp/xpression/teset.jpg"));
				return img;
			} else {
				if (!this.tileImageList.isEmpty()) {
					for (Tile tile : this.tileImageList) {
						BufferedImage img = getBufferedImage(tile.getImageEncoding(), this.ideStructure,
								tile.getCol(), tile.getRow(), tile.getData());
						
						ImageIO.write(img, "jpg", new File("C:/temp/afp/xpression/teset.jpg"));
						return img;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Tile getTile(int tileIndex) {
		if (!this.isTile()) {
			throw new AFPException("Not a tile image.");
		}
		if (tileIndex < 0 || tileIndex >= this.tileImageList.size()) {
			throw new AFPException("Tile index out of boundary.");
		}
		return this.tileImageList.get(tileIndex);
	}
	
	public BufferedImage getBufferedImage(Tile tile) {
		BufferedImage img;
		try {
			img = getBufferedImage(tile.getImageEncoding(), this.ideStructure,
					tile.getCol(), tile.getRow(), tile.getData());
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
			} else if (id == IDESize.ID) {
				this.IDESize = new IDESize();
				this.IDESize.read(in);
			} else if (id == LUTID.ID) {
				this.LUTID = new LUTID();
				this.LUTID.read(in);
			} else if (id == BandImage.ID) {
				this.bandImage = new BandImage();
				this.bandImage.read(in);
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
				} else if (id_1 == TileTOC.ID_TILE_TOC) {
					TileTOC toc = new TileTOC();
					toc.read(in);
				}
			} else if (id == Tile.Begin_ID) {
				Tile tile = new Tile();
				tile.read(in);
				
				if (this.tileImageList == null) {
					this.tileImageList = new ArrayList<Tile>();
					this.tileImageList.add(tile);
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
