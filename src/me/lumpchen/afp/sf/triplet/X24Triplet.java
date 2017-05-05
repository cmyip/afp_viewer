package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X24Triplet extends Triplet {

	public static final int ID = 0x24;
	
	/**
	 * Specifies the resource type: 
	 * X'00' Usage-dependent 
	 * X'02' Page Overlay 
	 * X'05' Coded Font 
	 * X'07' Color Attribute Table
	 * */
	private int ResType;
	
	// Specifies the resource local ID
	private int ResLID;
	
	public X24Triplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		
		this.ResType = in.readCode();
		remain -= 1;
		
		this.ResLID = in.readCode();
		remain -= 1;
	}

	public int getResType() {
		return ResType;
	}

	public void setResType(int resType) {
		ResType = resType;
	}

	public int getResLID() {
		return ResLID;
	}

	public void setResLID(int resLID) {
		ResLID = resLID;
	}

}
