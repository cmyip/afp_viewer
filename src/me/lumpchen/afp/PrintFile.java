package me.lumpchen.afp;

import java.util.ArrayList;
import java.util.List;

public class PrintFile extends AFPContainer {

	private ResourceGroup resourceGroup;
	private List<Document> documents;

	public PrintFile() {
		super(null);
		this.documents = new ArrayList<Document>();
	}
	
	public ResourceGroup getResourceGroup() {
		return resourceGroup;
	}

	public List<Document> getDocuments() {
		return documents;
	}
	
	@Override
	public boolean addChild(AFPObject child) {
		boolean ret = super.addChild(child);
		
		if (child instanceof ResourceGroup) {
			this.resourceGroup = (ResourceGroup) child;
		}
		return ret;
	}
	
	@Override
	public String toString() {
		if (this.structField != null) {
			return this.structField.getStructureTag().getDesc();
		}
		return "";
	}
	
	@Override
	public boolean isBegin() {
		return true;
	}
	
	@Override
	public void collect() {
		for (AFPObject child : this.children) {
			if (child instanceof ResourceGroup) {
				this.resourceGroup = (ResourceGroup) child;
			} else if (child instanceof Document) {
				this.documents.add((Document) child);
			}
		}
	}
	
}
 