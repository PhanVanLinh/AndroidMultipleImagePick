package com.toong.multipleimagepick;

public class GalleryImage {

	public String sdcardPath;
	private boolean isSelected = false;

	public String getSdcardPath() {
		return sdcardPath;
	}

	public void setSdcardPath(String sdcardPath) {
		this.sdcardPath = sdcardPath;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean isSelected() {
		return isSelected;
	}
}
