package me.lumpchen.xafp.sf.triplet;

import me.lumpchen.xafp.AFPInputStream;

import java.io.IOException;

/**
 * The following triplets have been retired:
	Text Orientation Triplet X'1D'
	Resource Object Type Triplet X'21'
	Line Data Object Position Migration Triplet X'27'
	Object Checksum Triplet X'63'
	Object Origin Identifier Triplet X'64'
	IMM Insertion Triplet X'73'
*/
public class UnknownTriplet extends Triplet {
	byte[] payloadData;

	public UnknownTriplet(int ID) {
		super();
		this.identifier = ID;
		this.name = "Unknown triplet";
	}

	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		while (remain > 0) {
			payloadData = in.readBytes(remain);
			remain = 0;
		}
	}


}
