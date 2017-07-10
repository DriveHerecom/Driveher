package com.yukti.utils;

public class AppUrl
{
	public String URL_BASE1 = "http://drivehere.com/inventory/api/";

	public String URL_BASE = "http://drivehere.com/inventory2/api/";

	public String URL_BASE_NEW = "http://drivehere.com/api/"; // /live api

//    public String URL_BASE_NEW = "http://drivehere.com/test_api/";    //   test api

	public String URL_PHOTO_BASE = "http://drivehere.com/inventory2/files/";

	public String TAG_DATA_ONE = "TAG_DATA_ONE";

	public String UPDATE_URL = "http://drivehere.com/api/";

	public String CONTRACT_SEARCH=URL_BASE_NEW + "dv_contract_search.php";

	// Goldstar API

	public static String LOGIN_URL = "https://track.goldstarcms.com/session/requestToken?";

	public static String SESSION_URL = "https://track.goldstarcms.com/auth/tokenLogin?";

	public static String GOLDSTAR_VEHICLE_INFO = "https://track.goldstarcms.com/api/vehicle/";

	public static String GET_SERIALNUMBER = "http://drivehere.com/api/goldstar_detail.php";

	public String URL_DATA_ONE = "https://api.dataonesoftware.com/webservices/vindecoder/decode";

	// public String URL_FORGOT_PASS =
	// "http://mywelcome.net/kajem/drive_here/users/forgot_password";

	public String URL_FORGOT_PASS = "https://drivehere.com/inventory2/users/forgot_password";

	public String URL_LOCAL_DATA_ONE_DATA = URL_BASE_NEW + "data1.php";

	//Complete
	public String URL_SEARCH_VEHICLE_NEW = URL_BASE_NEW + "search.php";

	//Complete
	public String URL_FIND_VEHICLE_NEW = URL_BASE_NEW + "find.php";

	//Complete
	public String URL_ADD_VEHICLE_NEW = URL_BASE_NEW + "addvehical.php";

	/// Not used
	public String URL_OFFICER = URL_BASE_NEW + "officerreport.php";

//	public String URL_ADD_VEHICLE_NEW_PHOTOS = URL_BASE_NEW + "addimages.php";

	public String URL_UPDATE_VEHICLE_NEW = URL_BASE_NEW + "updatecar.php";

	public String URL_CHECK_UPDATE = UPDATE_URL + "appversion.php";

	public String URL_LOGIN = URL_BASE + "user_login";

	public String URL_GET_FACE = URL_BASE_NEW + "getface.php?";

	public String URL_SIGN_UP = URL_BASE + "user_signup";

	public String URL_FACE = URL_BASE_NEW + "updateface.php";

	public String URL_EDIT_CAR_STAGES = URL_BASE_NEW + "updatestage.php";

	public String URL_EDIT_DONE_DATE = URL_BASE_NEW + "car_done.php";

	public String URL_SEND_MAIL = URL_BASE_NEW + "sendstagereport.php";

//	public String URL_UPDATE_AUCTION = URL_BASE_NEW + "badpicture.php";

	public String URL_FINAL_LONER_OUT_DETAIL = URL_BASE_NEW + "finalloaneroutdetails.php";

	public String URL_UPDATE_CAR_EXP_DATE = URL_BASE_NEW + "updatereturndate.php";

	public String URL_GET_REPORT_DETAIL = URL_BASE_NEW + "missingreport.php";

	public String URL_GET_STAGE_DETAIL = URL_BASE_NEW + "stagereport.php";

	public String URL_GET_STAGE_CAR_DETAIL = URL_BASE_NEW + "stagedetails.php";

	public String URL_GET_MISSING_CAR_DETAIL = URL_BASE_NEW + "missingcar.php";

	public String URL_LEASEHISTORY = URL_BASE_NEW + "autostart.php";

	public String URL_UPDATE_DATA_ONE_DATA = URL_BASE_NEW + "updatedata1.php";

	public String URL_REG_ID = URL_BASE_NEW + "setgcmid.php";

	public String URL_ADD_TITLES_NEW = URL_BASE_NEW + "addtitlephotos.php";

	public String URL_TITLES_HISTORY = URL_BASE_NEW + "history_titlephoto.php";

	public String URL_AUCTION_NAME_COUNT = URL_BASE_NEW + "auctionnamecount.php";

	public String URL_AUCTION_NAME_LOTCODE_COUNT = URL_BASE_NEW + "auctionnamedetail.php";

	public String URL_AUCTION_NAME_LOTCODE_VACANCY_COUNT = URL_BASE_NEW + "auction_vacancycount.php";

	public String URL_UPDATE_VACANCY_NEW = URL_BASE_NEW + "updatebidsaledata.php";

	public String URL_STORE_INQUIRY_DATA = URL_BASE_NEW + "inquiry.php";

	public String URL_WEB_PICS_HISTORY = URL_BASE_NEW + "history_webpictures.php";

	public String URL_ADD_WEB_PICS = URL_BASE_NEW + "add_webpictures.php";

	public static String BASE_URL = "http://drivehere.com/admin/admin/inventory/API/v1/";

	public static String URL_ALL_CAR_LIST = BASE_URL + "carlist.php";
	public static String URL_REPORT_MISSING_CAR = BASE_URL +  "reportmissing.php";
	public static String URL_REPORT_MISSING_CAR_LIST = BASE_URL +  "missingcarlist.php";
	public static String URL_AUCTION_LIST = BASE_URL +  "auctionlist.php";
	public static String URL_CAR_DEATAIL = BASE_URL +  "cardetail.php";
	public static String URL_AUCTION_CAR_DETAIL = BASE_URL +  "auction_car_list.php";
	public static String URL_SEARCH_CONTRACT = BASE_URL +  "searchcontract.php";
	public static String URL_CONTRACT_DETAIL = BASE_URL +  "contractdetail.php";
	public static String URL_STAGE_REPORT = BASE_URL +  "stagereport.php";
	public static String URL_STAGE_REPORT_LIST = BASE_URL +  "stagecarlist.php";
	static public String URL_AUCTION_DETAIL = BASE_URL +  "auctioncardetail.php";
	public static String URL_ADD_NEW_CAR_DEAILS = BASE_URL +  "addcar.php";
	public static String URL_UPDATEDONEDATE = BASE_URL +  "donedate.php";
	public static String URL_GETTITLEHISTORY = BASE_URL +  "titlehistory.php";
	public static String URL_ADD_TITLEPHOTO = BASE_URL +  "addtitlephoto.php";
	public static String URL_GET_WEbPITCURE = BASE_URL +  "historywebpicture.php";
	public static String URL_ADD_WEbPITCURE = BASE_URL +  "addwebpicture.php";
	public static String URL_UPDATESTAGE = BASE_URL +  "updatestage.php";
	public static String URL_DATA_ONE_NEW = BASE_URL +  "dataoneinformation.php";
	public static String URL_CHECK_APPVERSION = BASE_URL +  "appversion.php";
	public static String URL_AUCTION_INQUIRY = BASE_URL +  "inquiry.php";
	public static String URL_UPDATE_DATAONE = BASE_URL +  "updatedataoneinformation.php";
	public static String URL_UPDATE_AUCTION = BASE_URL +  "updateauction.php";
	public static String URL_UPDATE_AUCTION_VACANCY = BASE_URL +  "updateauctionvacancy.php";
}
