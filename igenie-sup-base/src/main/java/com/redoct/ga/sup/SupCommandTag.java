package com.redoct.ga.sup;

public class SupCommandTag
{
	// //////////////////////////////////////////////////
	// account
	public static final short VERIFY_LOGIN_EXIST_REQ = 10101;
	public static final short VERIFY_LOGIN_EXIST_RESP = 10102;
	public static final short VERIFY_LOGIN_REQ = 10103;
	public static final short VERIFY_LOGIN_RESP = 10104;
	public static final short MODIFY_PWD_REQ = 10105;
	public static final short MODIFY_PWD_RESP = 10106;
	public static final short CREATE_ACCOUNT_REQ = 10107;
	public static final short CREATE_ACCOUNT_RESP = 10108;
	public static final short CREATE_LOGIN_REQ = 10109;
	public static final short CREATE_LOGIN_RESP = 10110;
	public static final short MODIFY_ACCOUNT_INFO_REQ = 10111;
	public static final short MODIFY_ACCOUNT_INFO_RESP = 10112;
	public static final short CREATE_LOST_PWD_EKEY_REQ = 10113;
	public static final short CREATE_LOST_PWD_EKEY_RESP = 10114;
	public static final short QUERY_LOST_PWD_EKEY_INFO_REQ = 10115;
	public static final short QUERY_LOST_PWD_EKEY_INFO_RESP = 10116;
	public static final short QUERY_ACCOUNT_INFO_REQ = 10117;
	public static final short QUERY_ACCOUNT_INFO_RESP = 10118;
	public static final short QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_REQ = 10119;
	public static final short QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_RESP = 10120;
	public static final short APPLY_VERIFICATION_CODE_REQ = 10121;
	public static final short APPLY_VERIFICATION_CODE_RESP = 10122;
	public static final short QUERY_VERIFICATION_CODE_REQ = 10123;
	public static final short QUERY_VERIFICATION_CODE_RESP = 10124;
	public static final short MODIFY_ACCOUNT_LOGIN_REQ = 10125;
	public static final short MODIFY_ACCOUNT_LOGIN_RESP = 10126;
	public static final short QUERY_LOGIN_NAME_REQ = 10127;
	public static final short QUERY_LOGIN_NAME_RESP = 10128;
	public static final short QUERY_ACCOUNT_MASTER_INFO_REQ = 10129;
	public static final short QUERY_ACCOUNT_MASTER_INFO_RESP = 10130;
	public static final short QUERY_ALL_ACCOUNT_BASIC_REQ = 10131;
	public static final short QUERY_ALL_ACCOUNT_BASIC_RESP = 10132;
	public static final short QUERY_ACCOUNTS_DETAIL_REQ = 10133;
	public static final short QUERY_ACCOUNTS_DETAIL_RESP = 10134;

	// //////////////////////////////////////////////////
	// device
	public static final short QUERY_DEVICE_INFO_REQ = 10201;
	public static final short QUERY_DEVICE_INFO_RESP = 10202;
	public static final short MODIFY_DEVICE_CLIENT_VERSION_REQ = 10203;
	public static final short MODIFY_DEVICE_CLIENT_VERSION_RESP = 10204;
	public static final short MODIFY_DEVICE_OS_VERSION_REQ = 10205;
	public static final short MODIFY_DEVICE_OS_VERSION_RESP = 10206;

	// //////////////////////////////////////////////////
	// session
	public static final short APPLY_GATE_TOKEN_REQ = 10301;
	public static final short APPLY_GATE_TOKEN_RESP = 10302;
	public static final short VERIFY_GATE_TOKEN_REQ = 10303;
	public static final short VERIFY_GATE_TOKEN_RESP = 10304;
	public static final short APPLY_SESSION_TICKET_REQ = 10305;
	public static final short APPLY_SESSION_TICKET_RESP = 10306;
	public static final short QUERY_STP_SESSION_REQ = 10307;
	public static final short QUERY_STP_SESSION_RESP = 10308;
	public static final short REMOVE_STP_SESSION_REQ = 10309;
	public static final short REMOVE_STP_SESSION_RESP = 10310;
	public static final short INACTIVE_STP_SESSION_REQ = 10311;
	public static final short INACTIVE_STP_SESSION_RESP = 10312;
	public static final short ACTIVE_STP_SESSION_BY_TICKET_REQ = 10313;
	public static final short ACTIVE_STP_SESSION_BY_TICKET_RESP = 10314;
	public static final short QUERY_GATE_SESSION_REQ = 10315;
	public static final short QUERY_GATE_SESSION_RESP = 10316;
	public static final short QUERY_STP_SESSION_BY_TICKET_REQ = 10317;
	public static final short QUERY_STP_SESSION_BY_TICKET_RESP = 10318;
	public static final short REMOVE_STP_SESSION_BY_TICKET_REQ = 10319;
	public static final short REMOVE_STP_SESSION_BY_TICKET_RESP = 10320;
	public static final short INACTIVE_STP_SESSION_BY_TICKET_REQ = 10321;
	public static final short INACTIVE_STP_SESSION_BY_TICKET_RESP = 10322;

	// //////////////////////////////////////////////////
	// mail
	public static final short SEND_HTML_EMAIL_REQ = 10401;
	public static final short SEND_HTML_EMAIL_RESP = 10402;
	public static final short SEND_FORGOT_PWD_EMAIL_REQ = 10403;
	public static final short SEND_FORGOT_PWD_EMAIL_RESP = 10404;
	public static final short SEND_FRIEND_INVTE_EMAIL_REQ = 10405;
	public static final short SEND_FRIEND_INVTE_EMAIL_RESP = 10406;

	// //////////////////////////////////////////////////
	// sms
	public static final short SEND_SMS_VERIFICATION_REQ = 10501;
	public static final short SEND_SMS_VERIFICATION_RESP = 10502;

	// //////////////////////////////////////////////////
	// message
	public static final short MULTCAST_MESSAGE_REQ = 10601;
	public static final short MULTCAST_MESSAGE_RESP = 10602;
	public static final short MULTCAST_INVITE_REQ = 10603;
	public static final short MULTCAST_INVITE_RESP = 10604;
	public static final short MULTCAST_INVITE_FEEDBACK_REQ = 10605;
	public static final short MULTCAST_INVITE_FEEDBACK_RESP = 10606;
	public static final short MULTCAST_MODIFY_APPLY_STATE_REQ = 10607;
	public static final short MULTCAST_MODIFY_APPLY_STATE_RESP = 10608;
	public static final short MULTCAST_APPLY_STATE_REQ = 10609;
	public static final short MULTCAST_APPLY_STATE_RESP = 10610;
	public static final short MULTCAST_ACTIVITY_JOIN_REQ = 10611;
	public static final short MULTCAST_ACTIVITY_JOIN_RESP = 10612;
	public static final short MULTCAST_TASK_LOG_REQ = 10613;
	public static final short MULTCAST_TASK_LOG_RESP = 10614;

}
