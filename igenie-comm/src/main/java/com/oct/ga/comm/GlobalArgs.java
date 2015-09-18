package com.oct.ga.comm;

public class GlobalArgs
{
	public static final short FALSE = 0;
	public static final short TRUE = 1;

	public static final short CONTENT_TYPE_TXT = 0;
	public static final short CONTENT_TYPE_PIC = 1;
	public static final short CONTENT_TYPE_AUDIO = 2;
	public static final short CONTENT_TYPE_VIDEO = 3;
	public static final short CONTENT_TYPE_TITLE = 4;
	public static final short CONTENT_TYPE_OTHERS = 9;
	public static final short CONTENT_TYPE_GOMOKU_JOIN = 11;
	public static final short CONTENT_TYPE_GOMOKU_GIVEUP = 12;
	public static final short CONTENT_TYPE_GOMOKU_PLAY_NEXT_STEP = 13;
	public static final short CONTENT_TYPE_GOMOKU_CONGRATULATE = 14;

	public static final short FILE_TRANS_STATE_UPLOADING = 21;
	public static final short FILE_TRANS_STATE_COMPLETE = 22;
	public static final short FILE_TRANS_STATE_DELETE = 29;

	public static final short NOTE_STATE_NORMAL = 31;
	public static final short NOTE_STATE_DELETED = 32;

	public static final short MSG_GROUP_TYPE_GROUP = 41;
	public static final short MSG_GROUP_TYPE_QUESTION = 42;
	public static final short DND_NO = 43;
	public static final short DND_YES = 44;

	public static final short SYNC_STATE_NOT_RECEIVED = 51;
	public static final short SYNC_STATE_RECEIVED = 52;
	public static final short SYNC_STATE_READ = 53;

	public static final short ACCOUNT_STATE_NOT_GA_USER = 61;
	public static final short ACCOUNT_STATE_FRIEND_INVITE = 62;
	public static final short ACCOUNT_STATE_REGISTER = 63;
	public static final short ACCOUNT_STATE_ACTIVE = 64;
	public static final short ACCOUNT_STATE_INACTIVE = 65;

	public static final short CONTACT_STATE_NOT_GA_USER = 71;
	public static final short CONTACT_STATE_NOT_FRIEND = 72;
	public static final short CONTACT_STATE_FRIEND = 73;
	public static final short CONTACT_STATE_BLACK = 79;

	public static final short USER_FOLLOWING = 81;
	public static final short USER_UNFOLLOW = 82;
	public static final short FRIEND_INVITE_APPLY = 83;
	public static final short FRIEND_INVITE_ACCEPT = 84;

	public static final short DEVICE_STATE_ACTIVE = 91;
	public static final short DEVICE_STATE_INACTIVE = 92;

	public static final short CHANNEL_TYPE_NONE = 100;
	public static final short CHANNEL_TYPE_TASK = 101;
	public static final short CHANNEL_TYPE_CREATE_QUESTION = 102;
	public static final short CHANNEL_TYPE_QUESTION = 103;
	public static final short CHANNEL_TYPE_INVITE = 104;
	public static final short CHANNEL_TYPE_TASK_ACTIVITY = 105;
	public static final short CHANNEL_TYPE_NEWS = 106;
	public static final short CHANNEL_TYPE_VOTE = 107;
	public static final short CHANNEL_TYPE_GOMOKU = 108;
	public static final short CHANNEL_TYPE_CLUB = 109;
	public static final short CHANNEL_TYPE_ACTIVITY = 110;
	public static final short CHANNEL_TYPE_CLUB_TITLE_BACKGROUND = 111;
	public static final short CHANNEL_TYPE_PERSON_FACE = 112;
	public static final short CHANNEL_TYPE_APPLY = 113;

	public static final short INVITE_STATE_QUIT = 120;
	public static final short INVITE_STATE_APPLY = 121;
	public static final short INVITE_STATE_ACCPET = 122;
	public static final short INVITE_STATE_REJECT = 123;
	public static final short INVITE_STATE_KICKOFF = 124;
	public static final short INVITE_STATE_DISPATCH = 125;
	public static final short INVITE_STATE_REFILL = 126;
	public static final short INVITE_STATE_JOIN = 127;

	public static final short INVITE_TYPE_REGISTER_BY_EMAIL = 131;
	public static final short INVITE_TYPE_REGISTER_BY_PHONE = 132;
	public static final short INVITE_TYPE_JOIN_ACTIVITY = 141;
	public static final short INVITE_TYPE_JOIN_TASK = 142;
	public static final short INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_ACTIVITY = 143;
	public static final short INVITE_TYPE_REGISTER_BY_PHONE_AND_JOIN_ACTIVITY = 144;
	public static final short INVITE_TYPE_REGISTER_BY_EMAIL_AND_JOIN_TASK = 145;
	public static final short INVITE_TYPE_REGISTER_BY_PHONE_AND_JOIN_TASK = 146;
	public static final short INVITE_TYPE_FOLLOW_ME = 147;

	public static final short FRIEND_INVITE_TYPE_APPLY = 151;
	public static final short FRIEND_INVITE_TYPE_ACCEPT = 152;

	public static final short INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY = 161;
	public static final short INVITE_TYPE_REGISTER_AND_JOIN_CLUB_BY_KEY = 162;
	public static final short INVITE_TYPE_REGISTER_AND_JOIN_ACTIVITY_BY_KEY = 163;
	public static final short INVITE_TYPE_REGISTER_AND_JOIN_TASK_BY_KEY = 164;

	public static final short MEMBER_RANK_NONE = 200;
	public static final short MEMBER_RANK_NORMAL = 201;
	public static final short MEMBER_RANK_ELDER = 202;
	public static final short MEMBER_RANK_CO_LEADER = 203;
	public static final short MEMBER_RANK_LEADER = 204;

	public static final short TASK_ACTION_ADD = 211;
	public static final short TASK_ACTION_MODIFY = 212;
	public static final short TASK_ACTION_DELETE = 213;
	public static final short TASK_ACTION_COMPLETED = 214;
	public static final short TASK_ACTION_CHANGE_TIME = 215;
	public static final short TASK_ACTION_UNCOMPLETED = 216;
	public static final short TASK_ACTION_ADD_MEMBER = 217;
	public static final short TASK_ACTION_KICKOUT_MEMBER = 218;
	public static final short TASK_ACTION_ADD_ATTACH = 219;
	public static final short TASK_ACTION_ADD_NOTE = 220;
	public static final short TASK_ACTION_MODIFY_NOTE = 221;
	public static final short TASK_ACTION_REMOVE_NOTE = 222;
	public static final short TASK_ACTION_MOVTTO_PROJECT = 223;
	public static final short TASK_ACTION_REMOVE_ATTACH = 224;
	public static final short TASK_ACTION_CANCELED = 225;
	public static final short TASK_ACTION_JOIN = 226;
	public static final short TASK_ACTION_QUIT = 227;
	public static final short TASK_ACTION_RECOMMEND = 228;
	public static final short TASK_ACTION_APPLY = 229;
	public static final short TASK_ACTION_ACCEPT = 230;
	public static final short TASK_ACTION_REJECT = 231;
	public static final short TASK_ACTION_REFILL = 232;
	public static final short TASK_ACTION_MOMENT_FAVORITE = 233;
	public static final short TASK_ACTION_MOMENT_COMMENT = 234;

	public static final short TEMPLATE_TYPE_PROJECT = 301;
	public static final short TEMPLATE_TYPE_TASK = 302;
	public static final short TEMPLATE_EXTATTR_TYPE_NORMAL_TASK = 311;
	public static final short TEMPLATE_EXTATTR_TYPE_CHECK_LIST = 312;
	public static final short TEMPLATE_EXTATTR_TYPE_CLWC_LIST = 313;

	public static final short TEMPLATE_SUPPLIER_TYPE_RECOMMEND = 321;
	public static final short TEMPLATE_SUPPLIER_TYPE_VENDOR = 322;
	public static final short TEMPLATE_SUPPLIER_TYPE_MINE = 323;

	// //////////////////////////////////////////////////////////////
	// Gomoku
	public static final short GOMOKU_BLACK_STONE = 1001;
	public static final short GOMOKU_WHITE_STONE = 1002;
	public static final short GOMOKU_GAME_PENDDING = 1003;
	public static final short GOMOKU_GAME_PLAYING = 1004;
	public static final short GOMOKU_GAME_COMPLETED = 1005;
	public static final short GOMOKU_GAME_WIN = 1006;
	public static final short GOMOKU_GAME_LOST = 1007;
	public static final short GOMOKU_PLAYER_CREATOR = 1101;
	public static final short GOMOKU_PLAYER_NORMAL = 1102;

	// //////////////////////////////////////////////////////////////
	// Club
	public static final short CLUB_ACTIVITY_STATE_COMPLETED = 1201;
	public static final short CLUB_ACTIVITY_STATE_CANCELED = 1202;
	public static final short CLUB_ACTIVITY_STATE_OPENING = 1203;
	public static final short CLUB_ACTIVITY_STATE_COMING_SOON = 1204;
	public static final short CLUB_ACTIVITY_STATE_NOT_EXIST = 1205;

	public static final short CLUB_ACTIVITY_PUBLISH_TYPE_PRIVATE = 1211;
	public static final short CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC = 1212;
	public static final short CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_1 = 1213;
	public static final short CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_2 = 1214;
	public static final short CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_3 = 1215;

	// //////////////////////////////////////////////////////////////
	// Version type
	public static final short SYNC_VERSION_TYPE_TASK_INFO = 1301;
	public static final short SYNC_VERSION_TYPE_TASK_CHILD = 1302;
	public static final short SYNC_VERSION_TYPE_TASK_MEMBER = 1303;
	public static final short SYNC_VERSION_TYPE_TASK_FILE = 1304;
	public static final short SYNC_VERSION_TYPE_TASK_NOTE = 1305;

	// //////////////////////////////////////////////////////////////
	// ftp sp type
	public static final short FTP_SP_ALIYUN = 1401;
	public static final short FTP_SP_UPYUN = 1402;

	// //////////////////////////////////////////////////////////////
	// account system id
	public static final short ACCOUNT_SYS_GA = 1501;
	public static final short ACCOUNT_SYS_CSCART = 1502;
	public static final short ACCOUNT_LOGIN_BY_EMAIL = 1601;
	public static final short ACCOUNT_LOGIN_BY_PHONE = 1602;
	public static final short ACCOUNT_LOGIN_BY_SSO = 1603;
	public static final short ACCOUNT_LOGIN_BY_WECHAT = 1604;
	public static final short ACCOUNT_LOGIN_BY_WEIBO = 1605;
	public static final short ACCOUNT_LOGIN_BY_FACEBOOK = 1606;
	public static final short ACCOUNT_LOGIN_BY_TWITTER = 1607;
	public static final short ACCOUNT_LOGIN_BY_DEVICE = 1608;

	public static final short SUP_TYPE_ACCOUNT_SERVER = 1701;
	public static final short SUP_TYPE_SESSION_SERVER = 1702;
	public static final short SUP_TYPE_DEVICE_SERVER = 1703;
	public static final short SUP_TYPE_MAIL_SERVER = 1704;
	public static final short SUP_TYPE_SMS_SERVER = 1705;
	public static final short SUP_TYPE_MESSAGE_SERVER = 1706;

	public static final short VERIFICATION_TYPE_PHONE_REGISTER = 1801;
	public static final short VERIFICATION_TYPE_PHONE_LOST_PWD = 1802;
	public static final short VERIFICATION_TYPE_BIND_PHONE = 1803;
	public static final short VERIFICATION_TYPE_BIND_EMAIL = 1804;

	// //////////////////////////////////////////////////////////////
	// comm
	public static final String ID_DEFAULT_NONE = "00000000-0000-0000-0000-000000000000";
	public static final String SUPPORT_SERVICE_EMAIL = "support@planc2c.com";
	public static final String SUPPORT_SERVICE_ACCOUNT_ID = "930d76db-47a4-43df-9bce-123efafd49b0";
	public static final int BUFFER_SIZE = 16384; // 16k
	public static final String UPYUN_DEFAULT_URL = "http://tripc2c-default.b0.upaiyun.com";
	public static final String UPYUN_FACE_PHOTO_URL = "http://tripc2c-person-face.b0.upaiyun.com";
	public static final String UPYUN_PROJECT_PIC_URL = "http://tripc2c-project-pic.b0.upaiyun.com";
	public static final String UPYUN_CLUB_TITLE_URL = "http://tripc2c-club-title.b0.upaiyun.com";
	public static final String UPYUN_THUMB_NAIL = "!200x200";

}
