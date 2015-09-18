package com.oct.ga.comm.parser;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.account.ApplyBindPhoneResp;
import com.oct.ga.comm.cmd.account.ApplyPhoneRegisterVerificationCodeResp;
import com.oct.ga.comm.cmd.account.BindMargePhoneResp;
import com.oct.ga.comm.cmd.account.BindPhoneResp;
import com.oct.ga.comm.cmd.account.ChangePasswordResp;
import com.oct.ga.comm.cmd.account.ForgotPasswordResp;
import com.oct.ga.comm.cmd.account.QueryForgotPwdEmailResp;
import com.oct.ga.comm.cmd.account.ResetPwdResp;
import com.oct.ga.comm.cmd.account.SyncAccountBaseInfoResp;
import com.oct.ga.comm.cmd.account.SyncMyAccountResp;
import com.oct.ga.comm.cmd.account.UploadAccountResp;
import com.oct.ga.comm.cmd.apply.ApplicantInfosQueryResp;
import com.oct.ga.comm.cmd.apply.ApplicantInfosUploadResp;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateQueryResp;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateUploadResp;
import com.oct.ga.comm.cmd.apply.ApplyStateNotify;
import com.oct.ga.comm.cmd.apply.ModifyApproveStateResp;
import com.oct.ga.comm.cmd.apply.SyncApplyStateResp;
import com.oct.ga.comm.cmd.appver.CheckVersionUpdateResp;
import com.oct.ga.comm.cmd.auth.DeviceRegisterLoginResp;
import com.oct.ga.comm.cmd.auth.HeartbitReq;
import com.oct.ga.comm.cmd.auth.LoginResp;
import com.oct.ga.comm.cmd.auth.PhoneRegisterLoginResp;
import com.oct.ga.comm.cmd.auth.RegisterLoginReq;
import com.oct.ga.comm.cmd.auth.RegisterLoginResp;
import com.oct.ga.comm.cmd.auth.STP_ACF;
import com.oct.ga.comm.cmd.auth.SsoLoginResp;
import com.oct.ga.comm.cmd.badgenum.BadgeNumQueryResp;
import com.oct.ga.comm.cmd.club.ActivityCancelResp;
import com.oct.ga.comm.cmd.club.ActivityCreateResp;
import com.oct.ga.comm.cmd.club.ActivityJoinResp;
import com.oct.ga.comm.cmd.club.ActivityQueryAccountFuturePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryAccountFuturePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryAccountHistoryPaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryAccountHistoryPaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryDetailResp;
import com.oct.ga.comm.cmd.club.ActivityQueryFutureFilterByLocPaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryFuturePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryHistoryPaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryMyFuturePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMyFuturePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQueryMyHistoryPaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMyHistoryPaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribeFilterByTimeRangePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribeOrderByCreateTimePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribePaginationResp;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersResp;
import com.oct.ga.comm.cmd.club.ActivityRecommendResp;
import com.oct.ga.comm.cmd.club.ActivityUpdateResp;
import com.oct.ga.comm.cmd.club.ActivityUpdateSubscribersResp;
import com.oct.ga.comm.cmd.club.ClubCreateReq;
import com.oct.ga.comm.cmd.club.ClubCreateResp;
import com.oct.ga.comm.cmd.club.ClubQueryDetailResp;
import com.oct.ga.comm.cmd.club.ClubQueryMyListResp;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersReq;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersResp;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddResp;
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveResp;
import com.oct.ga.comm.cmd.club.ClubSubscribersUpdateReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersUpdateResp;
import com.oct.ga.comm.cmd.club.ClubUpdateResp;
import com.oct.ga.comm.cmd.club.KickoutMemberResp;
import com.oct.ga.comm.cmd.desc.ActivityCreateDescResp;
import com.oct.ga.comm.cmd.desc.ActivityModifyAllDescResp;
import com.oct.ga.comm.cmd.desc.ActivityModifyDescResp;
import com.oct.ga.comm.cmd.desc.ActivityQueryDescResp;
import com.oct.ga.comm.cmd.desc.ActivityRemoveDescResp;
import com.oct.ga.comm.cmd.following.FollowingResp;
import com.oct.ga.comm.cmd.following.QueryAccountResp;
import com.oct.ga.comm.cmd.following.SyncFollowingResp;
import com.oct.ga.comm.cmd.gatekeeper.GK_ACF;
import com.oct.ga.comm.cmd.gatekeeper.GK_ARQ;
import com.oct.ga.comm.cmd.group.DndQueryResp;
import com.oct.ga.comm.cmd.group.DndSetResp;
import com.oct.ga.comm.cmd.group.QueryMemberListResp;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskLogReq;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskLogResp;
import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteReq;
import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteResp;
import com.oct.ga.comm.cmd.invite.InviteFeedbackResp;
import com.oct.ga.comm.cmd.invite.InviteResp;
import com.oct.ga.comm.cmd.invite.QueryInvitedRegisterSemiIdResp;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.cmd.moment.AddCommentMomentResp;
import com.oct.ga.comm.cmd.moment.AddFavoriteMomentResp;
import com.oct.ga.comm.cmd.moment.AddMomentResp;
import com.oct.ga.comm.cmd.moment.DeleteMomentResp;
import com.oct.ga.comm.cmd.moment.QueryAllMomentsPaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentCommentPaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentFavoritePaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentLogPaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentPaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentPhotoFlowPaginationResp;
import com.oct.ga.comm.cmd.moment.QueryMomentResp;
import com.oct.ga.comm.cmd.msg.ConfirmMessageReadResp;
import com.oct.ga.comm.cmd.msg.QueryMessageBadgeNumberResp;
import com.oct.ga.comm.cmd.msg.QueryMessagePaginationResp;
import com.oct.ga.comm.cmd.msg.UploadMessageResp;
import com.oct.ga.comm.cmd.msgflow.QueryMsgFlowPaginationResp;
import com.oct.ga.comm.cmd.publish.ModifyPublishLocResp;
import com.oct.ga.comm.cmd.publish.QueryLocHotResp;
import com.oct.ga.comm.cmd.task.AddTaskMemberResp;
import com.oct.ga.comm.cmd.task.DeleteTaskMemberResp;
import com.oct.ga.comm.cmd.task.DeleteTaskNoteResp;
import com.oct.ga.comm.cmd.task.ModifyTaskMembersResp;
import com.oct.ga.comm.cmd.task.SyncCompletedProjectResp;
import com.oct.ga.comm.cmd.task.SyncTaskBaseResp;
import com.oct.ga.comm.cmd.task.SyncTaskChildResp;
import com.oct.ga.comm.cmd.task.SyncTaskDetailResp;
import com.oct.ga.comm.cmd.task.SyncTaskInfoResp;
import com.oct.ga.comm.cmd.task.SyncTaskMemberResp;
import com.oct.ga.comm.cmd.task.SyncTaskNoteResp;
import com.oct.ga.comm.cmd.task.SyncTodayTaskResp;
import com.oct.ga.comm.cmd.task.SyncTomorrowTaskResp;
import com.oct.ga.comm.cmd.task.SyncUncompletedProjectResp;
import com.oct.ga.comm.cmd.task.TaskCopyToResp;
import com.oct.ga.comm.cmd.task.TaskMoveToResp;
import com.oct.ga.comm.cmd.task.UpdateTaskStateResp;
import com.oct.ga.comm.cmd.task.UploadTaskNoteResp;
import com.oct.ga.comm.cmd.task.UploadTaskResp;
import com.oct.ga.comm.cmd.template.MakeProject2TemplateResp;
import com.oct.ga.comm.cmd.template.QueryTemplateDetailResp;
import com.oct.ga.comm.cmd.template.QueryTemplateListPaginationResp;
import com.oct.ga.comm.tlv.TlvObject;

public abstract class CommandParser
{
	private final static Logger logger = LoggerFactory.getLogger(CommandParser.class);

	// command object encode network transfer object.
	public static TlvObject encode(StpCommand cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	// network transfer object decode to command object.
	public static StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {

		case Command.GK_ARQ:
			return new GK_ARQ().decode(tlv);
		case Command.HEARTBIT_REQ:
			return new HeartbitReq().decode(tlv);
		case Command.GK_ACF:
			return new GK_ACF().decode(tlv);// lwz7512@2014/09/24
		case Command.LOGIN_RESP:
			return new LoginResp().decode(tlv);// lwz7512@2014/09/25
		case Command.STP_ACF:
			return new STP_ACF().decode(tlv);
		case Command.REGISTER_LOGIN_REQ:
			return new RegisterLoginReq().decode(tlv);
		case Command.REGISTER_LOGIN_RESP:
			return new RegisterLoginResp().decode(tlv);
		case Command.FORGOT_PASSWORD_RESP:
			return new ForgotPasswordResp().decode(tlv);
		case Command.QUERY_FORGOT_PASSWORD_EMAIL_RESP:
			return new QueryForgotPwdEmailResp().decode(tlv);
		case Command.RESET_PASSWORD_RESP:
			return new ResetPwdResp().decode(tlv);
		case Command.QUERY_MEMBER_LIST_RESP:
			return new QueryMemberListResp().decode(tlv);// lwz7512@2014/10/16
		case Command.SYNC_MY_ACCOUNT_RESP:
			return new SyncMyAccountResp().decode(tlv);// lwz7512@2014/10/27
		case Command.UPLOAD_MY_ACCOUNT_RESP:
			return new UploadAccountResp().decode(tlv);// lwz7512@2014/10/28
		case Command.SYNC_ACCOUNT_BASE_INFO_RESP:
			return new SyncAccountBaseInfoResp().decode(tlv);
		case Command.CHECK_VERSION_UPGRADE_RESP:
			return new CheckVersionUpdateResp().decode(tlv);
		case Command.SSO_LOGIN_RESP:
			return new SsoLoginResp().decode(tlv);

			// ////////////////////////////////////////
			// club
		case Command.CLUB_UPDATE_RESP:
			return new ClubUpdateResp().decode(tlv);// lwz7512/2014/11/26
		case Command.CLUB_CREATE_REQ:
			return new ClubCreateReq().decode(tlv);
		case Command.CLUB_CREATE_RESP:
			return new ClubCreateResp().decode(tlv);
		case Command.CLUB_QUERY_SUBSCRIBER_REQ:
			return new ClubQuerySubcribersReq().decode(tlv);
		case Command.CLUB_QUERY_SUBSCRIBER_RESP:
			return new ClubQuerySubcribersResp().decode(tlv);
		case Command.CLUB_SUBSCRIBER_UPDATE_REQ:
			return new ClubSubscribersUpdateReq().decode(tlv);
		case Command.CLUB_SUBSCRIBER_UPDATE_RESP:
			return new ClubSubscribersUpdateResp().decode(tlv);
		case Command.CLUB_SUBSCRIBER_ADD_REQ:
			return new ClubSubscribersAddReq().decode(tlv);
		case Command.CLUB_SUBSCRIBER_ADD_RESP:
			return new ClubSubscribersAddResp().decode(tlv);
		case Command.CLUB_SUBSCRIBER_REMOVE_REQ:
			return new ClubSubscribersRemoveReq().decode(tlv);
		case Command.CLUB_SUBSCRIBER_REMOVE_RESP:
			return new ClubSubscribersRemoveResp().decode(tlv);
		case Command.CLUB_QUERY_MYLIST_RESP:
			return new ClubQueryMyListResp().decode(tlv);// lwz7512@2014/10/28
		case Command.CLUB_QUERY_DETAIL_RESP:
			return new ClubQueryDetailResp().decode(tlv);// lw7512@2014/10/30

		case Command.ACTIVITY_QUERY_SUBSCRIBERS_REQ:
			return new ActivityQuerySubscribersReq().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBERS_RESP:
			return new ActivityQuerySubscribersResp().decode(tlv);
		case Command.ACTIVITY_QUERY_DETAIL_RESP:
			return new ActivityQueryDetailResp().decode(tlv);// lwz7512@2014/10/11
		case Command.ACTIVITY_JOIN_RESP:
			return new ActivityJoinResp().decode(tlv);// lwz7512@2014/10/15
		case Command.ACTIVITY_QUERY_HISTORY_PAGINATION_RESP:
			return new ActivityQueryHistoryPaginationResp().decode(tlv);// lwz7512@2014/10/30
		case Command.ACTIVITY_CREATE_RESP:
			return new ActivityCreateResp().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_RESP:
			return new ActivityQuerySubscribePaginationResp().decode(tlv);
		case Command.ACTIVITY_RECOMMEND_RESP:
			return new ActivityRecommendResp().decode(tlv);
		case Command.ACTIVITY_CANCEL_RESP:
			return new ActivityCancelResp().decode(tlv);
		case Command.ACTIVITY_UPDATE_RESP:
			return new ActivityUpdateResp().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_HISTORY_PAGINATION_REQ:
			return new ActivityQueryMyHistoryPaginationReq().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_HISTORY_PAGINATION_RESP:
			return new ActivityQueryMyHistoryPaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_FUTURE_PAGINATION_REQ:
			return new ActivityQueryMyFuturePaginationReq().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_FUTURE_PAGINATION_RESP:
			return new ActivityQueryMyFuturePaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_REQ:
			return new ActivityQueryAccountHistoryPaginationReq().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_RESP:
			return new ActivityQueryAccountHistoryPaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_FUTURE_PAGINATION_REQ:
			return new ActivityQueryAccountFuturePaginationReq().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_FUTURE_PAGINATION_RESP:
			return new ActivityQueryAccountFuturePaginationResp().decode(tlv);
		case Command.ACTIVITY_UPDATE_SUBSCRIBERS_RESP:
			return new ActivityUpdateSubscribersResp().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_ORDER_BY_CREATRE_TIME_PAGINATION_RESP:
			return new ActivityQuerySubscribeOrderByCreateTimePaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_HISTORY_ORDERBY_LAST_UPDATE_TIME_PAGINATION_RESP:
			return new ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_RESP:
			return new ActivityQueryFutureFilterByLocPaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_RESP:
			return new ActivityQuerySubscribeFilterByTimeRangePaginationResp().decode(tlv);
		case Command.ACTIVITY_QUERY_FUTURE_PAGINATION_RESP:
			return new ActivityQueryFuturePaginationResp().decode(tlv);
		case Command.ACTIVITY_KICKOUT_MEMBER_RESP:
			return new KickoutMemberResp().decode(tlv);

		case Command.MESSAGE_FLOW_QUERY_PAGINATION_RESP:
			return new QueryMsgFlowPaginationResp().decode(tlv);

		case Command.PUBLISH_QUERY_LOC_HOT_PAGINATION_RESP:
			return new QueryLocHotResp().decode(tlv);
		case Command.PUBLISH_MODIFY_LOC_RESP:
			return new ModifyPublishLocResp().decode(tlv);

		case Command.ACTIVITY_DESC_CREATE_RESP:
			return new ActivityCreateDescResp().decode(tlv);
		case Command.ACTIVITY_DESC_MODIFY_RESP:
			return new ActivityModifyDescResp().decode(tlv);
		case Command.ACTIVITY_DESC_MODIFY_ALL_RESP:
			return new ActivityModifyAllDescResp().decode(tlv);
		case Command.ACTIVITY_DESC_QUERY_RESP:
			return new ActivityQueryDescResp().decode(tlv);
		case Command.ACTIVITY_DESC_REMOVE_RESP:
			return new ActivityRemoveDescResp().decode(tlv);

		case Command.ADD_MOMENT_RESP:
			return new AddMomentResp().decode(tlv);
		case Command.QUERY_MOMENT_PHOTOFLOW_PAGINATION_RESP:
			return new QueryMomentPhotoFlowPaginationResp().decode(tlv);
		case Command.QUERY_MOMENT_PAGINATION_RESP:
			return new QueryMomentPaginationResp().decode(tlv);
		case Command.DELETE_MOMENT_RESP:
			return new DeleteMomentResp().decode(tlv);
		case Command.QUERY_ALL_MOMENT_PAGINATION_RESP:
			return new QueryAllMomentsPaginationResp().decode(tlv);
		case Command.ADD_MOMENT_FAVORITE_RESP:
			return new AddCommentMomentResp().decode(tlv);
		case Command.ADD_MOMENT_COMMENT_RESP:
			return new AddFavoriteMomentResp().decode(tlv);
		case Command.QUERY_MOMENT_FAVORITE_PAGINATION_RESP:
			return new QueryMomentFavoritePaginationResp().decode(tlv);
		case Command.QUERY_MOMENT_COMMENT_PAGINATION_RESP:
			return new QueryMomentCommentPaginationResp().decode(tlv);
		case Command.QUERY_MOMENT_LOG_PAGINATION_RESP:
			return new QueryMomentLogPaginationResp().decode(tlv);
		case Command.QUERY_MOMENT_RESP:
			return new QueryMomentResp().decode(tlv);

		case Command.FOLLOWING_RESP:
			return new FollowingResp().decode(tlv);// lwz7512@2014/11/03
		case Command.QUERY_ACCOUNT_RESP:
			return new QueryAccountResp().decode(tlv);// lwz7512@2014/11/05
		case Command.CHANGE_PASSWORD_RESP:
			return new ChangePasswordResp().decode(tlv);
		case Command.SYNC_FOLLOWING_RESP:
			return new SyncFollowingResp().decode(tlv);

		case Command.DND_QUERY_RESP:
			return new DndQueryResp().decode(tlv);
		case Command.DND_SET_RESP:
			return new DndSetResp().decode(tlv);

		case Command.ADD_TASK_MEMBER_RESP:
			return new AddTaskMemberResp().decode(tlv);
		case Command.DELETE_TASK_MEMBER_RESP:
			return new DeleteTaskMemberResp().decode(tlv);
		case Command.DELETE_TASK_NOTE_RESP:
			return new DeleteTaskNoteResp().decode(tlv);
		case Command.TASKPRO_MODIFY_MEMBERS_RESP:
			return new ModifyTaskMembersResp().decode(tlv);
		case Command.SYNC_CHILD_TASK_RESP:
			return new SyncTaskChildResp().decode(tlv);
		case Command.SYNC_PROJECT_COMPLETED_RESP:
			return new SyncCompletedProjectResp().decode(tlv);
		case Command.SYNC_TASKPRO_BASE_RESP:
			return new SyncTaskBaseResp().decode(tlv);
		case Command.SYNC_TASKPRO_CHILD_RESP:
			return new SyncTaskChildResp().decode(tlv);
		case Command.SYNC_TASKPRO_DETAIL_RESP:
			return new SyncTaskDetailResp().decode(tlv);
		case Command.SYNC_TASK_INFO_RESP:
			return new SyncTaskInfoResp().decode(tlv);
		case Command.SYNC_TASKPRO_MEMBER_RESP:
			return new SyncTaskMemberResp().decode(tlv);
		case Command.SYNC_TASKPRO_NOTE_RESP:
			return new SyncTaskNoteResp().decode(tlv);
		case Command.SYNC_TODAY_TASK_RESP:
			return new SyncTodayTaskResp().decode(tlv);
		case Command.SYNC_TOMORROW_TASK_RESP:
			return new SyncTomorrowTaskResp().decode(tlv);
		case Command.SYNC_PROJECT_UNCOMPLETED_RESP:
			return new SyncUncompletedProjectResp().decode(tlv);
		case Command.UPDATE_TASK_STATE_RESP:
			return new UpdateTaskStateResp().decode(tlv);
		case Command.UPLOAD_TASK_NOTE_RESP:
			return new UploadTaskNoteResp().decode(tlv);
		case Command.UPLOAD_TASK_RESP:
			return new UploadTaskResp().decode(tlv);
		case Command.TASK_MOVE_TO_RESP:
			return new TaskMoveToResp().decode(tlv);
		case Command.TASK_COPY_TO_RESP:
			return new TaskCopyToResp().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Template
		case Command.QUERY_TEMPLATE_LIST_PAGINATION_RESP:
			return new QueryTemplateListPaginationResp().decode(tlv);
		case Command.QUERY_TEMPLATE_DETAIL_RESP:
			return new QueryTemplateDetailResp().decode(tlv);
		case Command.MAKE_PROJECT_TO_TEMPLATE_RESP:
			return new MakeProject2TemplateResp().decode(tlv);

		case Command.INVITE_RESP:
			return new InviteResp().decode(tlv);
		case Command.INVITE_CONFIRM_RECEIVED_RESP:
			return new ConfirmReceivedInviteResp().decode(tlv);
		case Command.INVITE_FEEDBACK_RESP:
			return new InviteFeedbackResp().decode(tlv);
		case Command.INVITE_SYNC_RESP:
			return new SyncInviteResp().decode(tlv);
		case Command.INVITE_QUERY_REGISTER_SEMIID_RESP:
			return new QueryInvitedRegisterSemiIdResp().decode(tlv);
		case Command.INVITE_CONFIRM_RECEIVED_REQ:
			return new ConfirmReceivedInviteReq().decode(tlv);
		case Command.INLINECAST_TASK_LOG_REQ:
			return new InlinecastTaskLogReq().decode(tlv);
		case Command.INLINECAST_TASK_LOG_RESP:
			return new InlinecastTaskLogResp().decode(tlv);

		case Command.SYNC_APPLY_STATE_RESP:
			return new SyncApplyStateResp().decode(tlv);
		case Command.APPLY_STATE_NOTI:
			return new ApplyStateNotify().decode(tlv);
		case Command.UPLOAD_APPLICANT_TEMPLATE_RESP:
			return new ApplicantTemplateUploadResp().decode(tlv);
		case Command.QUERY_APPLICANT_TEMPLATE_RESP:
			return new ApplicantTemplateQueryResp().decode(tlv);
		case Command.UPLOAD_APPLICANTS_RESP:
			return new ApplicantInfosUploadResp().decode(tlv);
		case Command.QUERY_APPLICANTS_RESP:
			return new ApplicantInfosQueryResp().decode(tlv);
		case Command.MODIFY_APPROVE_STATE_RESP:
			return new ModifyApproveStateResp().decode(tlv);

		case Command.QUERY_BADGE_NUMBER_RESP:
			return new BadgeNumQueryResp().decode(tlv);
		case Command.UPLOAD_MESSAGE_RESP:
			return new UploadMessageResp().decode(tlv);
		case Command.CONFIRM_MESSAGE_READ_RESP:
			return new ConfirmMessageReadResp().decode(tlv);
		case Command.QUERY_MESSAGE_PAGINATION_RESP:
			return new QueryMessagePaginationResp().decode(tlv);
		case Command.QUERY_MESSAGE_BADGE_NUMBER_RESP:
			return new QueryMessageBadgeNumberResp().decode(tlv);

		case Command.APPLY_PHONE_REGISTER_VERIFICATION_CODE_RESP:
			return new ApplyPhoneRegisterVerificationCodeResp().decode(tlv);
		case Command.PHONE_REGISTER_LOGIN_RESP:
			return new PhoneRegisterLoginResp().decode(tlv);
		case Command.APPLY_BIND_PHONE_RESP:
			return new ApplyBindPhoneResp().decode(tlv);
		case Command.BIND_PHONE_RESP:
			return new BindPhoneResp().decode(tlv);
		case Command.BIND_MARGE_PHONE_RESP:
			return new BindMargePhoneResp().decode(tlv);
		case Command.DEVICE_REGISTER_LOGIN_RESP:
			return new DeviceRegisterLoginResp().decode(tlv);

		default:
			logger.warn("Unknow command tag: " + tlv.getTag());
			return null;
		}
	}

}
