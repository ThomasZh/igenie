package com.redoct.ga.sup.device;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.device.adapter.ModifyDeviceClientVersionAdapter;
import com.redoct.ga.sup.device.adapter.ModifyDeviceOsVersionAdapter;
import com.redoct.ga.sup.device.adapter.QueryDeviceBasicInfoAdapter;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionResp;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionResp;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoResp;

public class SupDeviceCommandParser
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
		case SupCommandTag.QUERY_DEVICE_INFO_REQ:
			return new QueryDeviceBasicInfoAdapter().decode(tlv);
		case SupCommandTag.QUERY_DEVICE_INFO_RESP:
			return new QueryDeviceBasicInfoResp().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_REQ:
			return new ModifyDeviceClientVersionAdapter().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_RESP:
			return new ModifyDeviceClientVersionResp().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_OS_VERSION_REQ:
			return new ModifyDeviceOsVersionAdapter().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_OS_VERSION_RESP:
			return new ModifyDeviceOsVersionResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
