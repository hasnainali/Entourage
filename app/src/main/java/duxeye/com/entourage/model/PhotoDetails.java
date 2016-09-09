package duxeye.com.entourage.model;

import java.io.Serializable;

/**
 * Created by Ondoor (Hasnain) on 9/7/2016.
 */
public class PhotoDetails implements Serializable{
    private String imageName;
    private String uploadedBy;
    private String photoCaption;
    private String size;
    private String date;
    private String nextPhotoId;
    private String priorPhotoId;
    private String categoryPhotoId;
    private String imageUrl;

    public PhotoDetails(String imageName, String uploadedBy, String photoCaption, String size, String date, String nextPhotoId, String priorPhotoId, String categoryPhotoId, String imageUrl) {
        this.imageName = imageName;
        this.uploadedBy = uploadedBy;
        this.photoCaption = photoCaption;
        this.size = size;
        this.date = date;
        this.nextPhotoId = nextPhotoId;
        this.priorPhotoId = priorPhotoId;
        this.categoryPhotoId = categoryPhotoId;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getPhotoCaption() {
        return photoCaption;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getNextPhotoId() {
        return nextPhotoId;
    }

    public String getPriorPhotoId() {
        return priorPhotoId;
    }

    public String getCategoryPhotoId() {
        return categoryPhotoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
