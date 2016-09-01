package duxeye.com.entourage.model;

/**
 * Created by User on 23-07-2016.
 */
public class Category {
    private String id;
    private String count;
    private String icon;
    private String name;

    public Category(String id, String count, String icon, String name) {
        this.id = id;
        this.count = count;
        this.icon = icon;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
