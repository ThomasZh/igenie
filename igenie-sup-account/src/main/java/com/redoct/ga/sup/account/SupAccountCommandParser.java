package com.redoct.ga.sup.account;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.account.adapter.ApplyVerificationCodeAdapter;
import com.redoct.ga.sup.account.adapter.CreateAccountAdapter;
import com.redoct.ga.sup.account.adapter.CreateLoginAdapter;
import com.redoct.ga.sup.account.adapter.CreateLostPwdEkeyAdapter;
import com.redoct.ga.sup.account.adapter.ModifyAccountBasicInfoAdapter;
import com.redoct.ga.sup.account.adapter.ModifyAccountId4LoginAdapter;
import com.redoct.ga.sup.account.adapter.ModifyPwdAdapter;
import com.redoct.ga.sup.account.adapter.QueryAccountBasicInfoAdapter;
import com.redoct.ga.sup.account.adapter.QueryAccountBasicInfoByLoginNameAdapter;
import com.redoct.ga.sup.account.adapter.QueryAccountDetailsAdapter;
import com.redoct.ga.sup.account.adapter.QueryAccountMasterInfoAdapter;
import com.redoct.ga.sup.account.adapter.QueryAllAccountBasicAdapter;
import com.redoct.ga.sup.account.adapter.QueryLoginNameAdapter;
import com.redoct.ga.sup.account.adapter.QueryLostPwdEkeyInfoAdapter;
import com.redoct.ga.sup.account.adapter.QueryVerificationCodeAdapter;
import com.redoct.ga.sup.account.adapter.VerifyLoginAdapter;
import com.redoct.ga.sup.account.adapter.VerifyLoginExistAdapter;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.CreateAccountResp;
import com.redoct.ga.sup.account.cmd.CreateLoginResp;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginResp;
import com.redoct.ga.sup.account.cmd.ModifyPwdResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsResp;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicResp;
import com.redoct.ga.sup.account.cmd.QueryLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoResp;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginResp;

public class SupAccountCommandParser
{
	// ///////////////////////////////////////////////////////////////////////////////////////
	// encode to send...

	public static TlvObject encode(SupCommand cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// decode to handle

	public static SupCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {
		case SupCommandTag.VERIFY_LOGIN_EXIST_REQ:
			return new VerifyLoginExistAdapter().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_EXIST_RESP:
			return new VerifyLoginExistResp().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_REQ:
			return new VerifyLoginAdapter().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_RESP:
			return new VerifyLoginResp().decode(tlv);
		case SupCommandTag.MODIFY_PWD_REQ:
			return new ModifyPwdAdapter().decode(tlv);
		case SupCommandTag.MODIFY_PWD_RESP:
			return new ModifyPwdResp().decode(tlv);
		case SupCommandTag.CREATE_ACCOUNT_REQ:
			return new CreateAccountAdapter().decode(tlv);
		case SupCommandTag.CREATE_ACCOUNT_RESP:
			return new CreateAccountResp().decode(tlv);
		case SupCommandTag.CREATE_LOGIN_REQ:
			return new CreateLoginAdapter().decode(tlv);
		case SupCommandTag.CREATE_LOGIN_RESP:
			return new CreateLoginResp().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_LOGIN_REQ:
			return new ModifyAccountId4LoginAdapter().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_LOGIN_RESP:
			return new ModifyAccountId4LoginResp().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_INFO_REQ:
			return new ModifyAccountBasicInfoAdapter().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_INFO_RESP:
			return new ModifyAccountBasicInfoResp().decode(tlv);
		case SupCommandTag.CREATE_LOST_PWD_EKEY_REQ:
			return new CreateLostPwdEkeyAdapter().decode(tlv);
		case SupCommandTag.CREATE_LOST_PWD_EKEY_RESP:
			return new CreateLostPwdEkeyResp().decode(tlv);
		case SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_REQ:
			return new QueryLostPwdEkeyInfoAdapter().decode(tlv);
		case SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_RESP:
			return new QueryLostPwdEkeyInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_REQ:
			return new QueryAccountBasicInfoAdapter().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_RESP:
			return new QueryAccountBasicInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_REQ:
			return new QueryAccountBasicInfoByLoginNameAdapter().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_RESP:
			return new QueryAccountBasicInfoByLoginNameResp().decode(tlv);
		case SupCommandTag.APPLY_VERIFICATION_CODE_REQ:
			return new ApplyVerificationCodeAdapter().decode(tlv);
		case SupCommandTag.APPLY_VERIFICATION_CODE_RESP:
			return new ApplyVerificationCodeResp().decode(tlv);
		case SupCommandTag.QUERY_VERIFICATION_CODE_REQ:
			return new QueryVerificationCodeAdapter().decode(tlv);
		case SupCommandTag.QUERY_VERIFICATION_CODE_RESP:
			return new QueryVerificationCodeResp().decode(tlv);
		case SupCommandTag.QUERY_LOGIN_NAME_REQ:
			return new QueryLoginNameAdapter().decode(tlv);
		case SupCommandTag.QUERY_LOGIN_NAME_RESP:
			return new QueryLoginNameResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_MASTER_INFO_REQ:
			return new QueryAccountMasterInfoAdapter().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_MASTER_INFO_RESP:
			return new QueryAccountMasterInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_REQ:
			return new QueryAllAccountBasicAdapter().decode(tlv);
		case SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_RESP:
			return new QueryAllAccountBasicResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNTS_DETAIL_REQ:
			return new QueryAccountDetailsAdapter().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNTS_DETAIL_RESP:
			return new QueryAccountDetailsResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
