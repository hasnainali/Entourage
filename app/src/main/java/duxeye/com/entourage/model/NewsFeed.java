package duxeye.com.entourage.model;

/**
 * Created by User on 23-07-2016.
 */
public class NewsFeed {
    private String id;
    private String icon;
    private String title;
    private String authorMemberId;
    private String createdDate;
    private String authorName;
    private String linkValue;
    private String type;
    private String notes;

    public NewsFeed(String id, String icon, String title, String authorMemberId, String createdDate, String authorName, String linkValue, String type, String notes) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.authorMemberId = authorMemberId;
        this.createdDate = createdDate;
        this.authorName = authorName;
        this.linkValue = linkValue;
        this.type = type;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorMemberId() {
        return authorMemberId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public String getType() {
        return type;
    }

    public String getNotes() {
        return notes;
    }
}
