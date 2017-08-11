package me.lumpchen.xafp.ioca;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.xafp.AFPInputStream;

public class Tile {

	public static final int Begin_ID = 0x8C;
	public static final int End_ID = 0x8D;
	
	public static final int BeginTransparencyMask_ID = 0x8E;
	public static final int EndTransparencyMask_ID = 0x8F;
	
	private byte[] data;
	
	private TilePosition tilePosition;
	private TileSize tileSize;
	private ImageEncoding encoding;
	private IDESize IDESize;
	private LUTID LUTID;
	private BandImage bandImage;
	private IDEStructure IDEStructure;
	private TileSetColor tileSetColor;
	private IncludeTile includeTile;
	
	private TransparencyMask transparencyMask;
	private List<ImageData> imageDataList;
	private BandImageData[] bandImageDataArray;
	
	public Tile() {
	}
	
	public int getCol() {
		return (int) this.tileSize.THSIZE;
	}
	
	public int getRow() {
		return (int) this.tileSize.TVSIZE;
	}
	
	public ImageEncoding getImageEncoding() {
		return this.encoding;
	}
	
	public byte[] getData() {
		if (this.data != null) {
			return this.data;
		}
		try {
			if (this.bandImage == null) {
				ByteArrayOutputStream imageDataStream = new ByteArrayOutputStream();
				for (ImageData data : this.imageDataList) {
					imageDataStream.write(data.getData());
				}
				imageDataStream.flush();
				imageDataStream.close();
				
				this.data = imageDataStream.toByteArray();
			} else {
				ByteArrayOutputStream imageDataStream = new ByteArrayOutputStream();
				for (BandImageData data : this.bandImageDataArray) {
					imageDataStream.write(data.getData());
				}
				
				imageDataStream.flush();
				imageDataStream.close();
				
				this.data = imageDataStream.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.data;
	}
	
	public void read(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);

		while (true) {
			int id = in.readCode();
			
			if (id == TilePosition.ID) {
				this.tilePosition = new TilePosition();
				this.tilePosition.read(in);
			} else if (id == TileSize.ID) {
				this.tileSize = new TileSize();
				this.tileSize.read(in);
			} else if (id == ImageEncoding.ID) {
				this.encoding = new ImageEncoding();
				this.encoding.read(in);
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
				this.IDEStructure = new IDEStructure();
				this.IDEStructure.read(in);
			} else if (id == TileSetColor.ID) {
				this.tileSetColor = new TileSetColor();
				this.tileSetColor.read(in);
			} else if (id == IncludeTile.ID_0 || id == ImageData.ID_0 || id == BandImageData.ID_0) {
				int id_1 = in.readCode();
				if (id_1 == IncludeTile.ID_1) {
					this.includeTile = new IncludeTile();
					this.includeTile.read(in);
				} else if (id_1 == ImageData.ID_1) {
					if (this.imageDataList == null) {
						this.imageDataList = new ArrayList<ImageData>();
					}
					ImageData imageData = new ImageData();
					imageData.read(in);
					this.imageDataList.add(imageData);
				} else if (id_1 == BandImageData.ID_1) {
					if (this.bandImageDataArray == null) {
						this.bandImageDataArray = new BandImageData[this.bandImage.getBandNumber()];
					}
					BandImageData bandImageData = new BandImageData();
					bandImageData.read(in);
					this.bandImageDataArray[bandImageData.getBandNum() - 1] = bandImageData;
				}
			} else if (id == TransparencyMask.Begin_ID) {
				this.transparencyMask = new TransparencyMask();
				this.transparencyMask.read(in);
			} else if (id == End_ID) {
				in.readUBin(1);
				break;
			}
		}
	}
	
	static class TilePosition {
		public static final int ID = 0xB5;
		
		public long XOFFSET;
		public long YOFFSET;
		
		public void read(AFPInputStream in) throws IOException {
			int length = in.readUBin(1);
			this.XOFFSET = in.readUnsignedInt();
			this.YOFFSET = in.readUnsignedInt();
			
			length -= 8;
			if (length != 0) {
				throw new IOException("Tile position reading error: " + length);
			}
		}
	}
	
	static class TileSize {
		public static final int ID = 0xB6;
		
		public long THSIZE;
		public long TVSIZE;
		public int RELRES;
		
		public void read(AFPInputStream in) throws IOException {
			int length = in.readUBin(1);
			
			this.THSIZE = in.readUnsignedInt();
			this.TVSIZE = in.readUnsignedInt();
			
			length -= 8;
			if (length > 0) {
				this.RELRES = in.readUBin(1);
			}
			length -= 1;
			if (length != 0) {
				throw new IOException("Tile size reading error: " + length);
			}
		}
	}
	
	static class TileSetColor {
		public static final int ID = 0xB7;
		
		public int CSPACE;
		public int SIZE1;
		public int SIZE2;
		public int SIZE3;
		public int SIZE4;
		public byte[] Color;
		
		public void read(AFPInputStream in) throws IOException {
			int length = in.readUBin(1);
			
			this.CSPACE = in.readCode();
			in.readBytes(3);
			
			this.SIZE1 = in.readUBin(1);
			this.SIZE2 = in.readUBin(1);
			this.SIZE3 = in.readUBin(1);
			this.SIZE4 = in.readUBin(1);
			
			length -= 8;
			if (length > 0) {
				this.Color = in.readBytes(length);
			}
		}
	}
	
	static class IncludeTile {
		public static final int ID_0 = 0xFE;
		public static final int ID_1 = 0xB8;
		
		public int TIRID;
		
		public void read(AFPInputStream in) throws IOException {
			int length = in.readUBin(1);
			this.TIRID = in.readInt();
			length -= 4;
			
			if (length != 0) {
				throw new IOException("Include Tile reading error: " + length);
			}
		}
	}

}
