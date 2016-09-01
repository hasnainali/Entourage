package duxeye.com.entourage.model;

/**
 * Created by User on 23-07-2016.
 */
public class PhotoGrid {
    private String photoId;
    private String photName;
    private String imageUrl;
    private String height;
    private String width;

    public PhotoGrid(String photoId, String photName, String imageUrl, String height, String width) {
        this.photoId = photoId;
        this.photName = photName;
        this.imageUrl = imageUrl;
        this.height = height;
        this.width = width;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getPhotName() {
        return photName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }
}
