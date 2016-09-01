package duxeye.com.entourage.model;

/**
 * Created by User on 20-07-2016.
 */
public class YearBook {
    private String yearBookId;
    private String yearBookName;

    public YearBook(String yearBookId, String yearBookName) {
        this.yearBookId = yearBookId;
        this.yearBookName = yearBookName;
    }

    public String getYearBookName() {
        return yearBookName;
    }

    public String getYearBookId() {
        return yearBookId;
    }

}
