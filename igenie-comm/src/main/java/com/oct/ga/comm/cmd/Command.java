package com.oct.ga.comm.cmd;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;

public interface Command
		extends java.io.Serializable
{

	// ////////////////////////////////////////////////////////
	public static final short GK_ARQ = 1001;
	public static final short GK_ACF = 1002;
	public static final short STP_ARQ = 1003;
	public static final short STP_ACF = 1004;
	public static final short GK_MODIFY_STP_STATE_REQ = 1005;
	public static final short GK_MODIFY_STP_STATE_RESP = 1006;
	public static final short GK_QUERY_STP_STATES_REQ = 1007;
	public static final short GK_QUERY_STP_STATES_RESP = 1008;

	// ////////////////////////////////////////////////////////
	// account
	public static final short REGISTER_LOGIN_REQ = 1009;
	public static final short REGISTER_LOGIN_RESP = 1010;

	public static final short REGISTER_REQ = 1011;
	public static final short REGISTER_RESP = 1012;

	public static final short LOGIN_REQ = 1013;
	public static final short LOGIN_RESP = 1014;

	public static final short FORGOT_PASSWORD_REQ = 1015;
	public static final short FORGOT_PASSWORD_RESP = 1016;

	public static final short CHANGE_PASSWORD_REQ = 1017;
	public static final short CHANGE_PASSWORD_RESP = 1018;

	public static final short UPLOAD_MY_ACCOUNT_REQ = 1019;
	public static final short UPLOAD_MY_ACCOUNT_RESP = 1020;

	public static final short SYNC_MY_ACCOUNT_REQ = 1021;
	public static final short SYNC_MY_ACCOUNT_RESP = 1022;

	public static final short ACTIVE_ACCOUNT_REQ = 1023;
	public static final short ACTIVE_ACCOUNT_RESP = 1024;

	public static final short RESET_PASSWORD_REQ = 1025;
	public static final short RESET_PASSWORD_RESP = 1026;
	public static final short QUERY_FORGOT_PASSWORD_EMAIL_REQ = 1027;
	public static final short QUERY_FORGOT_PASSWORD_EMAIL_RESP = 1028;

	public static final short HEARTBIT_REQ = 1031;
	public static final short HEARTBIT_RESP = 1032;
	public static final short LOGOUT_REQ = 1033;
	public static final short LOGOUT_RESP = 1034;
	public static final short SYNC_TIMESTAMP_RESP = 1036;
	public static final short SYNC_TASK_TIMESTAMP_RESP = 1038;

	public static final short SYNC_ACCOUNT_BASE_INFO_REQ = 1039;
	public static final short SYNC_ACCOUNT_BASE_INFO_RESP = 1040;

	public static final short DISCONNECT_REQ = 1041;

	public static final short CHECK_VERSION_UPGRADE_REQ = 1043;
	public static final short CHECK_VERSION_UPGRADE_RESP = 1044;

	public static final short SSO_LOGIN_REQ = 1045;
	public static final short SSO_LOGIN_RESP = 1046;

	// ////////////////////////////////////////////////////////
	// Contact: following
	public static final short UPLOAD_CONTACT_REQ = 1051;
	public static final short UPLOAD_CONTACT_RESP = 1052;
	public static final short UNFOLLOW_REQ = 1053;
	public static final short UNFOLLOW_RESP = 1054;
	public static final short SYNC_CONTACT_REQ = 1055;
	public static final short SYNC_CONTACT_RESP = 1056;
	public static final short IMPORT_FOLLOWING_REQ = 1057;
	public static final short IMPORT_FOLLOWING_RESP = 1058;
	public static final short FOLLOWING_REQ = 1059;
	public static final short FOLLOWING_RESP = 1060;
	public static final short SYNC_FOLLOWING_REQ = 1061;
	public static final short SYNC_FOLLOWING_RESP = 1062;
	public static final short QUERY_ACCOUNT_REQ = 1063;
	public static final short QUERY_ACCOUNT_RESP = 1064;

	// ////////////////////////////////////////////////////////
	// ftp: upload
	public static final short APPLY_UPLOAD_FILE_REQ = 1101;
	public static final short APPLY_UPLOAD_FILE_RESP = 1102;
	public static final short QUERY_LAST_CHANGED_FILE_REQ = 1103;
	public static final short QUERY_LAST_CHANGED_FILE_RESP = 1104;
	public static final short QUERY_FILEINFO_REQ = 1105;
	public static final short QUERY_FILEINFO_RESP = 1106;
	public static final short UPLOAD_FILE_COMPLETED_REQ = 1107;
	public static final short UPLOAD_FILE_COMPLETED_RESP = 1108;
	public static final short QUERY_FILE_URL_REQ = 1109;
	public static final short QUERY_FILE_URL_RESP = 1110;

	// ////////////////////////////////////////////////////////
	// moment
	public static final short ADD_MOMENT_REQ = 1111;
	public static final short ADD_MOMENT_RESP = 1112;
	public static final short QUERY_MOMENT_PAGINATION_REQ = 1113;
	public static final short QUERY_MOMENT_PAGINATION_RESP = 1114;
	public static final short QUERY_MOMENT_PHOTOFLOW_PAGINATION_REQ = 1115;
	public static final short QUERY_MOMENT_PHOTOFLOW_PAGINATION_RESP = 1116;
	public static final short QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ = 1117;
	public static final short QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_RESP = 1118;
	public static final short DELETE_MOMENT_REQ = 1119;
	public static final short DELETE_MOMENT_RESP = 1120;
	public static final short QUERY_ALL_MOMENT_PAGINATION_REQ = 1121;
	public static final short QUERY_ALL_MOMENT_PAGINATION_RESP = 1122;
	public static final short ADD_MOMENT_FAVORITE_REQ = 1123;
	public static final short ADD_MOMENT_FAVORITE_RESP = 1124;
	public static final short ADD_MOMENT_COMMENT_REQ = 1125;
	public static final short ADD_MOMENT_COMMENT_RESP = 1126;
	public static final short QUERY_MOMENT_FAVORITE_PAGINATION_REQ = 1127;
	public static final short QUERY_MOMENT_FAVORITE_PAGINATION_RESP = 1128;
	public static final short QUERY_MOMENT_COMMENT_PAGINATION_REQ = 1129;
	public static final short QUERY_MOMENT_COMMENT_PAGINATION_RESP = 1130;
	public static final short QUERY_MOMENT_LOG_PAGINATION_REQ = 1131;
	public static final short QUERY_MOMENT_LOG_PAGINATION_RESP = 1132;
	public static final short QUERY_MOMENT_REQ = 1133;
	public static final short QUERY_MOMENT_RESP = 1134;

	// ////////////////////////////////////////////////////////
	// group: member
	public static final short QUERY_MEMBER_LIST_REQ = 1151;
	public static final short QUERY_MEMBER_LIST_RESP = 1152;
	public static final short DND_SET_REQ = 1153;
	public static final short DND_SET_RESP = 1154;
	public static final short DND_QUERY_REQ = 1155;
	public static final short DND_QUERY_RESP = 1156;

	// ////////////////////////////////////////////////////////
	// message
	public static final short UPLOAD_MESSAGE_REQ = 1201;
	public static final short UPLOAD_MESSAGE_RESP = 1202;
	public static final short SYNC_MESSAGE_REQ = 1203;
	public static final short SYNC_MESSAGE_RESP = 1204;
	public static final short QUERY_MESSAGE_PAGINATION_REQ = 1205;
	public static final short QUERY_MESSAGE_PAGINATION_RESP = 1206;
	public static final short CONFIRM_MESSAGE_RECEIVED_REQ = 1207;
	public static final short CONFIRM_MESSAGE_RECEIVED_RESP = 1208;
	public static final short CONFIRM_MESSAGE_READ_REQ = 1209;
	public static final short CONFIRM_MESSAGE_READ_RESP = 1210;
	public static final short MULTICAST_MESSAGE_READ_RESP = 1212;

	// ////////////////////////////////////////////////////////
	// invite
	public static final short CONFIRM_INVITE_RECEIVED_REQ = 1215;
	public static final short CONFIRM_INVITE_READ_REQ = 1217;
	public static final short MULTICAST_INVITE_READ_RESP = 1218;

	// ////////////////////////////////////////////////////////
	// invite log
	public static final short SYNC_APPLY_STATE_REQ = 1221;
	public static final short SYNC_APPLY_STATE_RESP = 1222;
	public static final short APPLY_STATE_NOTI = 1224;
	public static final short UPLOAD_APPLICANT_TEMPLATE_REQ = 1225;
	public static final short UPLOAD_APPLICANT_TEMPLATE_RESP = 1226;
	public static final short QUERY_APPLICANT_TEMPLATE_REQ = 1227;
	public static final short QUERY_APPLICANT_TEMPLATE_RESP = 1228;
	public static final short UPLOAD_APPLICANTS_REQ = 1229;
	public static final short UPLOAD_APPLICANTS_RESP = 1230;
	public static final short QUERY_APPLICANTS_REQ = 1231;
	public static final short QUERY_APPLICANTS_RESP = 1232;
	public static final short MODIFY_APPROVE_STATE_REQ = 1233;
	public static final short MODIFY_APPROVE_STATE_RESP = 1234;

	// ////////////////////////////////////////////////////////
	// vote
	public static final short CREATE_VOTE_REQ = 1241;
	public static final short CREATE_VOTE_RESP = 1242;
	public static final short CAST_VOTE_REQ = 1243;
	public static final short CAST_VOTE_RESP = 1244;
	public static final short CONFIRM_VOTE_RECEIVED_REQ = 1245;

	// ////////////////////////////////////////////////////////
	// task
	public static final short UPLOAD_TASK_REQ = 1301;
	public static final short UPLOAD_TASK_RESP = 1302;
	public static final short DELETE_TASK_REQ = 1303;
	public static final short DELETE_TASK_RESP = 1304;
	public static final short UPDATE_TASK_STATE_REQ = 1305;
	public static final short UPDATE_TASK_STATE_RESP = 1306;
	public static final short ADD_TASK_MEMBER_REQ = 1311;
	public static final short ADD_TASK_MEMBER_RESP = 1312;
	public static final short DELETE_TASK_MEMBER_REQ = 1313;
	public static final short DELETE_TASK_MEMBER_RESP = 1314;
	public static final short UPLOAD_TASK_NOTE_REQ = 1321;
	public static final short UPLOAD_TASK_NOTE_RESP = 1322;
	public static final short DELETE_TASK_NOTE_REQ = 1323;
	public static final short DELETE_TASK_NOTE_RESP = 1324;
	public static final short SYNC_TASK_ACTIVITY_REQ = 1341;
	public static final short SYNC_TASK_ACTIVITY_RESP = 1342;
	public static final short SYNC_TASK_INFO_RESP = 1344;
	public static final short QUERY_TASK_ACTIVITY_PAGINATION_REQ = 1361;
	public static final short QUERY_TASK_ACTIVITY_PAGINATION_RESP = 1362;
	public static final short SYNC_PROJECT_UNCOMPLETED_REQ = 1371;
	public static final short SYNC_PROJECT_UNCOMPLETED_RESP = 1372;
	public static final short SYNC_CHILD_TASK_REQ = 1373;
	public static final short SYNC_CHILD_TASK_RESP = 1374;
	public static final short SYNC_TODAY_TASK_REQ = 1375;
	public static final short SYNC_TODAY_TASK_RESP = 1376;
	public static final short SYNC_PROJECT_COMPLETED_REQ = 1377;
	public static final short SYNC_PROJECT_COMPLETED_RESP = 1378;
	public static final short SYNC_TOMORROW_TASK_REQ = 1379;
	public static final short SYNC_TOMORROW_TASK_RESP = 1380;
	public static final short SYNC_TASKPRO_UNCOMPLETED_REQ = 1381;
	public static final short SYNC_TASKPRO_BASE_RESP = 1382;
	public static final short SYNC_TASKPRO_COMPLETED_REQ = 1383;
	public static final short SYNC_TASKPRO_DETAIL_REQ = 1385;
	public static final short SYNC_TASKPRO_DETAIL_RESP = 1386;
	public static final short SYNC_TASKPRO_MEMBER_REQ = 1387;
	public static final short SYNC_TASKPRO_MEMBER_RESP = 1388;
	public static final short SYNC_TASKPRO_ATTACH_REQ = 1389;
	public static final short SYNC_TASKPRO_ATTACH_RESP = 1390;
	public static final short SYNC_TASKPRO_NOTE_REQ = 1391;
	public static final short SYNC_TASKPRO_NOTE_RESP = 1392;
	public static final short SYNC_TASKPRO_CHILD_REQ = 1393;
	public static final short SYNC_TASKPRO_CHILD_RESP = 1394;
	public static final short TASKPRO_MODIFY_MEMBERS_REQ = 1395;
	public static final short TASKPRO_MODIFY_MEMBERS_RESP = 1396;
	public static final short TASK_MOVE_TO_REQ = 1411;
	public static final short TASK_MOVE_TO_RESP = 1412;
	public static final short TASK_COPY_TO_REQ = 1413;
	public static final short TASK_COPY_TO_RESP = 1414;

	// ////////////////////////////////////////////////////////
	// template
	public static final short QUERY_TEMPLATE_LIST_PAGINATION_REQ = 1401;
	public static final short QUERY_TEMPLATE_LIST_PAGINATION_RESP = 1402;
	public static final short QUERY_TEMPLATE_DETAIL_REQ = 1403;
	public static final short QUERY_TEMPLATE_DETAIL_RESP = 1404;
	public static final short MAKE_PROJECT_TO_TEMPLATE_REQ = 1405;
	public static final short MAKE_PROJECT_TO_TEMPLATE_RESP = 1406;

	// ////////////////////////////////////////////////////////
	// admin
	public static final short INLINECAST_MESSAGE_REQ = 1501;
	public static final short INLINECAST_INVITE_REQ = 1503;
	public static final short INLINECAST_INVITE_FEEDBACK_REQ = 1505;
	public static final short INLINECAST_TASK_ACTIVITY_REQ = 1507;
	public static final short INLINECAST_GOMOKU_NOTIFY_REQ = 1508;
	public static final short MULTICAST_GOMOKU_NOTIFY_RESP = 1509;
	public static final short INLINECAST_APPLY_STATE_REQ = 1511;
	public static final short INLINECAST_ACTIVITY_JOIN_REQ = 1513;
	public static final short INLINECAST_TASK_LOG_REQ = 1515;
	public static final short INLINECAST_TASK_LOG_RESP = 1516;

	// ////////////////////////////////////////////////////////
	// invite
	public static final short INVITE_REQ = 1601;
	public static final short INVITE_RESP = 1602;
	public static final short INVITE_SYNC_REQ = 1603;
	public static final short INVITE_SYNC_RESP = 1604;
	public static final short INVITE_CONFIRM_RECEIVED_REQ = 1605;
	public static final short INVITE_CONFIRM_RECEIVED_RESP = 1606;
	// public static final short INVITE_CONFIRM_READ_REQ = 1607;
	// public static final short INVITE_CONFIRM_READ_RESP = 1608;
	public static final short INVITE_QUERY_REGISTER_SEMIID_REQ = 1609;
	public static final short INVITE_QUERY_REGISTER_SEMIID_RESP = 1610;

	public static final short INVITE_FEEDBACK_REQ = 1611;
	public static final short INVITE_FEEDBACK_RESP = 1612;
	// public static final short INVITE_FEEDBACK_SYNC_REQ = 1613;
	public static final short INVITE_FEEDBACK_SYNC_RESP = 1614;
	public static final short INVITE_FEEDBACK_CONFIRM_RECEIVED_REQ = 1615;
	// public static final short INVITE_FEEDBACK_CONFIRM_RECEIVED_RESP = 1616;
	// public static final short INVITE_FEEDBACK_CONFIRM_READ_REQ = 1617;
	// public static final short INVITE_FEEDBACK_CONFIRM_READ_RESP = 1618;

	// ////////////////////////////////////////////////////////
	// init
	public static final short QUERY_MESSAGE_BADGE_NUMBER_REQ = 2001;
	public static final short QUERY_MESSAGE_BADGE_NUMBER_RESP = 2002;
	public static final short QUERY_ACTIVITY_BADGE_NUMBER_REQ = 2003;
	public static final short QUERY_ACTIVITY_BADGE_NUMBER_RESP = 2004;
	public static final short QUERY_BADGE_NUMBER_REQ = 2005;
	public static final short QUERY_BADGE_NUMBER_RESP = 2006;

	// ////////////////////////////////////////////////////////
	// club
	public static final short CLUB_CREATE_REQ = 3001;
	public static final short CLUB_CREATE_RESP = 3002;
	public static final short CLUB_UPDATE_REQ = 3003;
	public static final short CLUB_UPDATE_RESP = 3004;
	public static final short CLUB_QUERY_MYLIST_REQ = 3005;
	public static final short CLUB_QUERY_MYLIST_RESP = 3006;
	public static final short CLUB_QUERY_SUBSCRIBER_REQ = 3007;
	public static final short CLUB_QUERY_SUBSCRIBER_RESP = 3008;
	public static final short CLUB_QUERY_DETAIL_REQ = 3009;
	public static final short CLUB_QUERY_DETAIL_RESP = 3010;

	public static final short CLUB_SUBSCRIBER_ADD_REQ = 3031;
	public static final short CLUB_SUBSCRIBER_ADD_RESP = 3032;
	public static final short CLUB_SUBSCRIBER_REMOVE_REQ = 3033;
	public static final short CLUB_SUBSCRIBER_REMOVE_RESP = 3034;
	public static final short CLUB_SUBSCRIBER_UPDATE_REQ = 3035;
	public static final short CLUB_SUBSCRIBER_UPDATE_RESP = 3036;

	// ////////////////////////////////////////////////////////
	// activity
	public static final short ACTIVITY_CREATE_REQ = 3011;
	public static final short ACTIVITY_CREATE_RESP = 3012;
	public static final short ACTIVITY_UPDATE_REQ = 3013;
	public static final short ACTIVITY_UPDATE_RESP = 3014;
	public static final short ACTIVITY_JOIN_REQ = 3015;
	public static final short ACTIVITY_JOIN_RESP = 3016;
	public static final short ACTIVITY_QUERY_MEMBER_REQ = 3019;
	public static final short ACTIVITY_QUERY_MEMBER_RESP = 3020;
	public static final short ACTIVITY_CANCEL_REQ = 3021;
	public static final short ACTIVITY_CANCEL_RESP = 3022;
	public static final short ACTIVITY_QUERY_SUBSCRIBERS_REQ = 3023;
	public static final short ACTIVITY_QUERY_SUBSCRIBERS_RESP = 3024;
	public static final short ACTIVITY_RECOMMEND_REQ = 3025;
	public static final short ACTIVITY_RECOMMEND_RESP = 3026;
	public static final short ACTIVITY_QUERY_DETAIL_REQ = 3027;
	public static final short ACTIVITY_QUERY_DETAIL_RESP = 3028;
	public static final short ACTIVITY_QUERY_MY_REQ = 3029;
	public static final short ACTIVITY_QUERY_MY_RESP = 3030;

	public static final short ACTIVITY_QUERY_HISTORY_PAGINATION_REQ = 3041;
	public static final short ACTIVITY_QUERY_HISTORY_PAGINATION_RESP = 3042;
	public static final short ACTIVITY_QUERY_IMAGES_PAGINATION_REQ = 3043;
	public static final short ACTIVITY_QUERY_IMAGES_PAGINATION_RESP = 3044;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_REQ = 3045;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_RESP = 3046;
	public static final short ACTIVITY_QUERY_MY_HISTORY_PAGINATION_REQ = 3047;
	public static final short ACTIVITY_QUERY_MY_HISTORY_PAGINATION_RESP = 3048;
	public static final short ACTIVITY_QUERY_MY_FUTURE_PAGINATION_REQ = 3049;
	public static final short ACTIVITY_QUERY_MY_FUTURE_PAGINATION_RESP = 3050;
	public static final short ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_REQ = 3051;
	public static final short ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_RESP = 3052;
	public static final short ACTIVITY_QUERY_ACCOUNT_FUTURE_PAGINATION_REQ = 3053;
	public static final short ACTIVITY_QUERY_ACCOUNT_FUTURE_PAGINATION_RESP = 3054;
	public static final short ACTIVITY_UPDATE_SUBSCRIBERS_REQ = 3055;
	public static final short ACTIVITY_UPDATE_SUBSCRIBERS_RESP = 3056;
	public static final short ACTIVITY_QUERY_OWNER_HISTORY_PAGINATION_REQ = 3057;
	public static final short ACTIVITY_QUERY_OWNER_HISTORY_PAGINATION_RESP = 3058;
	public static final short ACTIVITY_QUERY_OWNER_FUTURE_PAGINATION_REQ = 3059;
	public static final short ACTIVITY_QUERY_OWNER_FUTURE_PAGINATION_RESP = 3060;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_ORDER_BY_CREATRE_TIME_PAGINATION_REQ = 3061;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_ORDER_BY_CREATRE_TIME_PAGINATION_RESP = 3062;
	public static final short ACTIVITY_QUERY_MY_HISTORY_ORDERBY_LAST_UPDATE_TIME_PAGINATION_REQ = 3063;
	public static final short ACTIVITY_QUERY_MY_HISTORY_ORDERBY_LAST_UPDATE_TIME_PAGINATION_RESP = 3064;
	public static final short ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_REQ = 3065;
	public static final short ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_RESP = 3066;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_REQ = 3067;
	public static final short ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_RESP = 3068;
	public static final short ACTIVITY_QUERY_FUTURE_PAGINATION_REQ = 3069;
	public static final short ACTIVITY_QUERY_FUTURE_PAGINATION_RESP = 3070;
	public static final short ACTIVITY_KICKOUT_MEMBER_REQ = 3071;
	public static final short ACTIVITY_KICKOUT_MEMBER_RESP = 3072;

	// ////////////////////////////////////////////////////////
	// management for message flow
	public static final short MESSAGE_FLOW_QUERY_PAGINATION_REQ = 3091;
	public static final short MESSAGE_FLOW_QUERY_PAGINATION_RESP = 3092;

	// ////////////////////////////////////////////////////////
	// management for publish
	public static final short PUBLISH_QUERY_LOC_HOT_PAGINATION_REQ = 3101;
	public static final short PUBLISH_QUERY_LOC_HOT_PAGINATION_RESP = 3102;
	public static final short PUBLISH_MODIFY_LOC_REQ = 3103;
	public static final short PUBLISH_MODIFY_LOC_RESP = 3104;

	// ////////////////////////////////////////////////////////
	// management for publish
	public static final short ACTIVITY_DESC_CREATE_REQ = 3201;
	public static final short ACTIVITY_DESC_CREATE_RESP = 3202;
	public static final short ACTIVITY_DESC_QUERY_REQ = 3203;
	public static final short ACTIVITY_DESC_QUERY_RESP = 3204;
	public static final short ACTIVITY_DESC_MODIFY_REQ = 3205;
	public static final short ACTIVITY_DESC_MODIFY_RESP = 3206;
	public static final short ACTIVITY_DESC_REMOVE_REQ = 3207;
	public static final short ACTIVITY_DESC_REMOVE_RESP = 3208;
	public static final short ACTIVITY_DESC_MODIFY_ALL_REQ = 3209;
	public static final short ACTIVITY_DESC_MODIFY_ALL_RESP = 3210;

	// ////////////////////////////////////////////////////////
	// management for admin
	public static final short MODIFY_SUP_STATE_REQ = 4001;
	public static final short MODIFY_SUP_STATE_RESP = 4002;

	// ////////////////////////////////////////////////////////
	// management for monitor
	public static final short MONITOR_SESSION_MAP_REQ = 5001;
	public static final short MONITOR_SESSION_NUMBER_REQ = 5002;
	public static final short MONITOR_PACKAGE_SEND_REQ = 5003;
	public static final short MONITOR_PACKAGE_RECV_REQ = 5004;
	public static final short MONITOR_BYTE_SEND_REQ = 5005;
	public static final short MONITOR_BYTE_RECV_REQ = 5005;
	public static final short MONITOR_CONTEXT_RESP = 5099;

	// ////////////////////////////////////////////////////////
	// phone
	public static final short APPLY_PHONE_REGISTER_VERIFICATION_CODE_REQ = 6001;
	public static final short APPLY_PHONE_REGISTER_VERIFICATION_CODE_RESP = 6002;
	public static final short PHONE_REGISTER_LOGIN_REQ = 6003;
	public static final short PHONE_REGISTER_LOGIN_RESP = 6004;
	public static final short APPLY_BIND_PHONE_REQ = 6005;
	public static final short APPLY_BIND_PHONE_RESP = 6006;
	public static final short BIND_PHONE_REQ = 6007;
	public static final short BIND_PHONE_RESP = 6008;
	public static final short BIND_MARGE_PHONE_REQ = 6009;
	public static final short BIND_MARGE_PHONE_RESP = 6010;
	public static final short DEVICE_REGISTER_LOGIN_REQ = 6011;
	public static final short DEVICE_REGISTER_LOGIN_RESP = 6012;

	/**
	 * handling the decoded java object, may be save in DB
	 * 
	 * @return base command
	 * @throws Exception
	 */
	public Command execute()
			throws Exception;

	/**
	 * encode command to send...
	 * 
	 * @return TlvObject
	 * @throws UnsupportedEncodingException
	 * @author liwenzhi@2014/04/04
	 */
	public TlvObject encode()
			throws UnsupportedEncodingException;

	/**
	 * decode package to handle...
	 * 
	 * @param tlv
	 * @return command
	 * @throws UnsupportedEncodingException
	 * @author liwenzhi@2014/04/04
	 */
	public Command decode(TlvObject tlv)
			throws UnsupportedEncodingException;

}
