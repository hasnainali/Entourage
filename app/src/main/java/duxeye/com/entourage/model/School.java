package duxeye.com.entourage.model;

/**
 * Created by User on 20-07-2016.
 */
public class School {
    private String yearBookId;
    private String orgName;

    public School(String yearBookId, String orgName) {
        this.yearBookId = yearBookId;
        this.orgName = orgName;
    }

    public String getYearBookId() {
        return yearBookId;
    }

    public String getOrgName() {
        return orgName;
    }
}
