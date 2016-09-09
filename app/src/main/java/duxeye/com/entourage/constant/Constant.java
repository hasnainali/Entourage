package duxeye.com.entourage.constant;

import java.util.ArrayList;

/**
 * Created by User on 18-07-2016.
 */
public class Constant {
    public static final String API_KEY = "LINK53116";
    //The development environment URL is
    public static String BASE_URL = "http://dev.ws.entourageyearbooks.com/";
    public static String LOGIN_URL = BASE_URL + "apis/MobileRequestHandler.asp?type=LOGIN&username=";
    public static String REGISTRATION_URL = BASE_URL + "apis/MobileRequestHandler.asp?type=REGISTER_NEW_USER&email=";
    public static String GET_STATE_URL = BASE_URL + "apis/MobileRequestHandler.asp?type=GEOGRAPHIES";
    public static String SEARCH_SCHOOL_URL = BASE_URL + "apis/MobileRequestHandler.asp?type=SEARCH&search=";
    public static String GET_YEAR_BOOK = BASE_URL + "apis/MobileRequestHandler.asp?type=MEMBER_YEARBOOKS&credential_key=";
    public static String MEMBER_PERMISSION = BASE_URL + "apis/MobileRequestHandler.asp?type=MEMBER_PERMISSIONS&yearbook_id=";
    public static String GET_CAROUSEL = BASE_URL + "apis/MobileRequestHandler.asp?type=CAROUSEL&yearbook_id=";
    public static String NEWSFEED = BASE_URL + "apis/MobileRequestHandler.asp?type=NEWSFEED&next_id=&num_items=30&yearbook_id=";
    public static String GETCATEGORY = BASE_URL + "apis/MobileRequestHandler.asp?type=PHOTO_CATEGORIES&yearbook_id=";
    public static String GETCATEGORY_PHOTO = BASE_URL + "apis/MobileRequestHandler.asp?type=LIST_CATEGORY_PHOTOS&yearbook_id=";
    public static String PHOTO_DETAILS = BASE_URL + "apis/MobileRequestHandler.asp?type=PHOTO_INFORMATION&category_photo_id=";
    public static String CREATE_CATEGORY = BASE_URL + "apis/MobileRequestHandler.asp?type=CREATE_CATEGORY&name=";
    public static String POST_MESSAGE = BASE_URL + "apis/MobileRequestHandler.asp?type=POST_MESSAGE&yearbook_id=";
    public static String UPDATE_MEMBER_INFO = BASE_URL + "apis/MobileRequestHandler.asp?type=UPDATE_MEMBER_INFO&member_name=";
    public static String MEMBER_INFO = BASE_URL + "apis/MobileRequestHandler.asp?type=MEMBER_INFO&credential_key=";
    public static String UPLOAD_IMAGE_URL = BASE_URL + "apis/MobileUploadRequestHandler.asp?yearbook_id=";
    public static String GETLINKACCOUNTINFORMATION = BASE_URL + "apis/MobileRequestHandler.asp?type=getLinkAccountInformation&link_id=";
    public static String YEARBOOK_LADDER = BASE_URL + "apis/MobileRequestHandler.asp?type=YEARBOOK_LADDER&yearbook_id=";
    public static String GET_OPTION_POLL_URL = BASE_URL + "apis/MobileRequestHandler.asp?type=POLL&id=";
    public static String SUBMIT_ANSWER = BASE_URL + "apis/MobileRequestHandler.asp?type=POST_POLL_ANSWER&question_id=";
    public static String ACCESS_YEARBOOK = BASE_URL + "apis/MobileRequestHandler.asp?type=ACCESS_YEARBOOK&yearbook_id=";
    public static String PAGE_INFORMATION = BASE_URL + "apis/MobileRequestHandler.asp?type=PAGE_INFORMATION&yearbook_id=";
    public static String PHOTO_CAPTION_UPDATE = BASE_URL + "apis/MobileRequestHandler.asp?type=PHOTO_CAPTION_UPDATE&category_photo_id=";
    public static String LIST_CATEGORY_PHOTO = BASE_URL + "apis/MobileRequestHandler.asp?type=LIST_CATEGORY_PHOTOS&yearbook_id=";


    public static ArrayList<String> uploadUrl = new ArrayList<>();
    /**
     * key
     */
    public static String MEMBERID = "MEMBERID";
    public static String CREDENTIALKEY = "CREDENTIALKEY";
    public static String YEARBOOKID = "YEARBOOKID";
    public static String YEARBOOK_NAME = "YEARBOOK_NAME";
    public static String SCHOOL_NAME = "SCHOOL_NAME";
    public static String CATEGORY_ID = "CATEGORY_ID";
    public static String PHOTO_ID = "PHOTOT_ID";
    public static String NEXT_PHOTO_ID = "NEXT_PHOTO_ID";
    public static String  PRIOR_PHOTO_ID = "PRIOR_PHOTO_ID";
    public static String  CATEGORY_PHOTO_ID = "CATEGORY_PHOTO_ID";
    public static String CURRENT_PAGE_INDEX = "CURRENT_PAGE_INDEX";
//    public static String LEFT_WIP_PAGE_ID = "LEFT_WIP_PAGE_ID";
//    public static String RIGHT_WIP_PAGE_ID = "RIGHT_WIP_PAGE_ID";
    public static String PHOTO_COUNT = "PHOTO_COUNT";
    public static String NUMBER_PAGES_PROD = "NUMBER_PAGES_PROD";
    public static String IMAGE_ARRAY_LIST = "IMAGE_ARRAY_LIST";

    /**
     * Member Permission key
     */
    public static String MEMBER_TYPE = "MEMBER_TYPE";
    public static String STATUS = "STATUS";
    public static String ALLOW_PHOTOS = "ALLOW_PHOTOS";
    public static String ALLOW_YEARBOOK = "ALLOW_YEARBOOK";
    public static String ALLOW_UPLOAD = "ALLOW_UPLOAD";
    public static String ALLOW_CREATE_CATEGORY = "ALLOW_CREATE_CATEGORY";
    public static String ALLOW_CAPTION = "ALLOW_CAPTION";
    public static String ALLOW_POST_MESSAGE = "ALLOW_POST_MESSAGE";
    public static String isBackFromImageUploadDetails = "isBackFromImageUploadDetails";


}
