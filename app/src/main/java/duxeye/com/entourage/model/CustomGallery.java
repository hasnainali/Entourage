package duxeye.com.entourage.model;

public class CustomGallery {
	private String sdcardPath;
	private boolean isSelected = false;

	public CustomGallery(String sdcardPath, boolean isSelected) {
		this.sdcardPath = sdcardPath;
		this.isSelected = isSelected;
	}

	public String getSdcardPath() {
		return sdcardPath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}