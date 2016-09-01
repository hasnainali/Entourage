package duxeye.com.entourage.model;

/**
 * Created by Ondoor (Hasnain) on 7/26/2016.
 */
public class UploadFileData {
    private String fileName;
    private String UUID;
    private String imageUrl;

    public UploadFileData(String UUID , String fileName, String imageUrl) {
        this.fileName = fileName;
        this.imageUrl = imageUrl;
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
