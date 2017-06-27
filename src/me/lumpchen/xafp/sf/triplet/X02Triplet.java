package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class X02Triplet extends Triplet {

	public static final int ID = 0x02;
	
	/**
	 * Specifies how the GID will be used: 
			X'01' Replace First GID name 
			X'07' Font Family Name 
			X'08' Font Typeface Name 
			X'09' MO:DCA Resource Hierarchy Reference 
			X'0A' Begin Resource Group Reference 
			X'0B' Attribute GID 
			X'OC' Process Element GID 
			X'0D' Begin Page Group Reference 
			X'11' Media Type Reference 
			X'41' Color Management Resource (CMR) Reference 
			X'6E' Data-object Font Base Font Identifier 
			X'7E' Data-object Font Linked Font Identifier 
			X'83' Begin Document Reference 
			X'84' Resource Object Reference 
			X'85' Code Page Name Reference 
			X'86' Font Character Set Name Reference 
			X'87' Begin Page Reference 
			X'8D' Begin Medium Map Reference 
			X'8E' Coded Font Name Reference 
			X'98' Begin Document Index Reference 
			X'B0' Begin Overlay Reference 
			X'BE' Data Object Internal Resource Reference 
			X'CA' Index Element GID 
			X'CE' Other Object Data Reference 
			X'DE' Data Object External Resource Reference
	 * */
	private int FQNType;
	
	/**
	 * Specifies the GID format: 
	 * X'00' Character string 
	 * X'10' OID 
	 * X'20' URL
	 * */
	/**
	 * 
	 */
	private int FQNFmt;
	
	/**
	 * GID of the MO:DCA construct. Can be up to 250 bytes in length. 
	 * The data type is format-dependent. See the semantic description of the FQNFmt parameter.
	 * */ 
	/**
	 * 
	 */
	private byte[] FQName;
	
	public X02Triplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		this.FQNType = in.readCode();
		remain -= 1;
		this.FQNFmt = in.readCode();
		remain -= 1;
		this.FQName = in.readBytes(remain);
	}

	public int getFQNType() {
		return FQNType;
	}

	public void setFQNType(int fQNType) {
		FQNType = fQNType;
	}

	public int getFQNFmt() {
		return FQNFmt;
	}

	public void setFQNFmt(int fQNFmt) {
		FQNFmt = fQNFmt;
	}

	public byte[] getFQName() {
		return FQName;
	}

	public void setFQName(byte[] fQName) {
		FQName = fQName;
	}

}
