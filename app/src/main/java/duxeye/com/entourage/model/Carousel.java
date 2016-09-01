package duxeye.com.entourage.model;

/**
 * Created by User on 23-07-2016.
 */
public class Carousel {
    private String id;
    private String imageUrl;
    private String linkValue;
    private String linkType;

    public Carousel(String id, String imageUrl, String linkValue, String linkType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.linkValue = linkValue;
        this.linkType = linkType;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public String getLinkType() {
        return linkType;
    }
}
