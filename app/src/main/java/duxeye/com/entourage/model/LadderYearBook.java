package duxeye.com.entourage.model;

/**
 * Created by User on 20-07-2016.
 */
public class LadderYearBook {
    private String leftPageNumber;
    private String leftPageLabel;
    private String leftPageImg;
    private String leftPageHeight;
    private String leftPageWidth;
    private String leftWipPageId;
    private String leftPageTitle;
    private String rightPageNumber;
    private String rightPageLabel;
    private String rightPageImg;
    private String rightPageHeight;
    private String rightPageWidth;
    private String rightWipPageId;
    private String rightPageTitle;

    public LadderYearBook(String leftPageNumber, String leftPageLabel, String leftPageImg, String leftPageHeight, String leftPageWidth, String leftWipPageId, String leftPageTitle, String rightPageNumber, String rightPageLabel, String rightPageImg, String rightPageHeight, String rightPageWidth, String rightWipPageId, String rightPageTitle) {
        this.leftPageNumber = leftPageNumber;
        this.leftPageLabel = leftPageLabel;
        this.leftPageImg = leftPageImg;
        this.leftPageHeight = leftPageHeight;
        this.leftPageWidth = leftPageWidth;
        this.leftWipPageId = leftWipPageId;
        this.leftPageTitle = leftPageTitle;
        this.rightPageNumber = rightPageNumber;
        this.rightPageLabel = rightPageLabel;
        this.rightPageImg = rightPageImg;
        this.rightPageHeight = rightPageHeight;
        this.rightPageWidth = rightPageWidth;
        this.rightWipPageId = rightWipPageId;
        this.rightPageTitle = rightPageTitle;
    }

    public String getLeftPageNumber() {
        return leftPageNumber;
    }

    public String getLeftPageLabel() {
        return leftPageLabel;
    }

    public String getLeftPageImg() {
        return leftPageImg;
    }

    public String getLeftPageHeight() {
        return leftPageHeight;
    }

    public String getLeftPageWidth() {
        return leftPageWidth;
    }

    public String getLeftWipPageId() {
        return leftWipPageId;
    }

    public String getLeftPageTitle() {
        return leftPageTitle;
    }

    public String getRightPageNumber() {
        return rightPageNumber;
    }

    public String getRightPageLabel() {
        return rightPageLabel;
    }

    public String getRightPageImg() {
        return rightPageImg;
    }

    public String getRightPageHeight() {
        return rightPageHeight;
    }

    public String getRightPageWidth() {
        return rightPageWidth;
    }

    public String getRightWipPageId() {
        return rightWipPageId;
    }

    public String getRightPageTitle() {
        return rightPageTitle;
    }
}
