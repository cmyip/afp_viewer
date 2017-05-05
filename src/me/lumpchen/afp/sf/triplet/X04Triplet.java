package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X04Triplet extends Triplet {

	public static final int ID = 0x04;
	
	/**
	 * X'00' Position 
	 * X'10' Position and trim 
	 * X'20' Scale to fit 
	 * X'30' Center and trim 
	 * X'41' Migration mapping 
	 * X'42' Migration mapping 
	 * X'50' Migration mapping 
	 * X'60' Scale to fill 
	 * X'70' UP3i Print Data mapping
	 * */
	private int MapValue;
	
	public X04Triplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		this.MapValue = in.readCode();
	}

	public int getMapValue() {
		return MapValue;
	}

}
