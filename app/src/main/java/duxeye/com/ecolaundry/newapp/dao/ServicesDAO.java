package duxeye.com.ecolaundry.newapp.dao;

/**
 * Created by Duxeye on 17-11-2015.
 */
public class ServicesDAO {
    private String servicesTitle;
    private String description;
    private int resourceID;

    public ServicesDAO(String servicesTitle, String description, int resourceID) {
        this.servicesTitle = servicesTitle;
        this.description = description;
        this.resourceID = resourceID;
    }

    public String getServicesTitle() {
        return servicesTitle;
    }

    public String getDescription() {
        return description;
    }

    public int getResourceID() {
        return resourceID;
    }
}
