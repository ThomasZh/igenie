package com.oct.ga.comm;

import com.oct.ga.comm.cmd.Command;

public class ErrorCode
{
	public static final short SUCCESS = 100;
	public static final short UNKNOWN_FAILURE = 200;
	public static final short CONNECTION_CLOSED = 300;
	public static final short NOT_ALLOW = 400; // operation not allowed
	public static final short SYNC_VER_NOT_SAME = 500; // operation not allowed
	public static final short SYNC_VER_SAME = 501; // operation allowed
	public static final short ENCODING_FAILURE = 600; // EncodingException
	
	public static final short LOGIN_NOT_EXIST = 1001; // account's login not exist
	public static final short GATE_TOKEN_NOT_EXIST = 1101;
	public static final short GATE_TOKEN_NOT_ON_DEVICE = 1102;

	public static final short GK_ARQ_NOT_ENOUGH_STP = Command.GK_ARQ * 10 + 1;

	public static final short STP_ARQ_NULL_SESSION = Command.STP_ARQ * 10 + 1;
	public static final short STP_ARQ_NOT_YOUR_SESSION = Command.STP_ARQ * 10 + 2;
	public static final short STP_ARQ_SESSION_TIMEOUT = Command.STP_ARQ * 10 + 3;

	public static final short APP_VERSION_UPGRADE_ADVICE = Command.CHECK_VERSION_UPGRADE_REQ * 10 + 1;
	public static final short APP_VERSION_UPGRADE_MUST = Command.CHECK_VERSION_UPGRADE_REQ * 10 + 2;

	public static final short REGISTER_EMAIL_EXIST = Command.REGISTER_REQ * 10 + 1;
	public static final short LOGIN_WRONG_PWD = Command.LOGIN_REQ * 10 + 1;
	public static final short NOT_MATCH_OLD_PASSWORD = Command.CHANGE_PASSWORD_REQ * 10 + 1;

	public static final short REGISTER_PHONE_EXIST = 6031;
	public static final short NOT_MATCH_VERIFICATION_CODE = 6032;
	public static final short APPLY_VERIFICATON_CODE_TOO_MOUCH_TIMES = 6033;
	public static final short VERIFICATON_CODE_TIMEOUT = 6034;
	public static final short NOT_MATCH_PHONE = 6035;
	public static final short REGISTER_DEVICE_EXIST = 6036;
	public static final short PHONE_ALREADY_BIND = 6037;
	
	public static final short ACTIVITY_ALREADY_CANCELED = Command.ACTIVITY_JOIN_REQ * 10 + 1;
	public static final short ACTIVITY_ALREADY_STARTED = Command.ACTIVITY_UPDATE_REQ * 10 + 1;

	public static final short MEMBER_NOT_GA_ACCOUNT = Command.ADD_TASK_MEMBER_REQ * 10 + 1;
	public static final short MEMBER_ALREADY_EXIST = Command.ADD_TASK_MEMBER_REQ * 10 + 2;

	public static final short CONTACT_ALREADY_EXIST = Command.FOLLOWING_REQ * 10 + 1;

	public static final short FILE_ALREADY_EXIST = Command.APPLY_UPLOAD_FILE_REQ * 10 + 1;

	public static final short INVITE_ALREADY_KICKOFF = Command.INVITE_REQ * 10 + 1;
	public static final short INVITE_NOT_TO_YOU = Command.INVITE_REQ * 10 + 2;
	public static final short INVITE_NOT_TASK = Command.INVITE_REQ * 10 + 3;

	public static final short INVITE_ALREADY_EXIST = Command.INVITE_REQ * 10 + 1;
	public static final short INVITE_EXPIRY_TIME = Command.INVITE_FEEDBACK_REQ * 10 + 1;
	public static final short INVITE_NOT_EXIST = Command.INVITE_FEEDBACK_REQ * 10 + 2;

	public static final short RESET_PWD_NOT_GA_ACCOUNT = Command.RESET_PASSWORD_REQ * 10 + 1;
	public static final short RESET_PWD_HAS_NO_EKEY = Command.RESET_PASSWORD_REQ * 10 + 2;
	public static final short RESET_PWD_NOT_YOUR_EKEY = Command.RESET_PASSWORD_REQ * 10 + 3;
	public static final short RESET_PWD_EKEY_NOT_THIS_TYPE = Command.RESET_PASSWORD_REQ * 10 + 4;
	public static final short RESET_PWD_EKEY_EXPIRY_TIME = Command.RESET_PASSWORD_REQ * 10 + 5;

}
