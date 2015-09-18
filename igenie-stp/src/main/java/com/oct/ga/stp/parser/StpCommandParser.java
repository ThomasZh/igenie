package com.oct.ga.stp.parser;

import java.io.UnsupportedEncodingException;

import com.oct.ga.account.cmd.ApplyBindPhoneAdapter;
import com.oct.ga.account.cmd.ApplyPhoneRegisterVerificationCodeAdapter;
import com.oct.ga.account.cmd.BindMargePhoneAdapter;
import com.oct.ga.account.cmd.BindPhoneAdapter;
import com.oct.ga.account.cmd.ChangePwdAdapter;
import com.oct.ga.account.cmd.DeviceRegisterLoginAdapter;
import com.oct.ga.account.cmd.DisconnectAdapter;
import com.oct.ga.account.cmd.ForgotPwdAdapter;
import com.oct.ga.account.cmd.HeartbitAdapter;
import com.oct.ga.account.cmd.LoginAdapter;
import com.oct.ga.account.cmd.LogoutAdapter;
import com.oct.ga.account.cmd.PhoneRegisterLoginAdapter;
import com.oct.ga.account.cmd.QueryForgotPwdEmailAdapter;
import com.oct.ga.account.cmd.RegisterLoginAdapter;
import com.oct.ga.account.cmd.ResetPwdAdapter;
import com.oct.ga.account.cmd.STP_ARQ_Adapter;
import com.oct.ga.account.cmd.SsoLoginAdapter;
import com.oct.ga.account.cmd.SyncAccountBaseAdapter;
import com.oct.ga.account.cmd.SyncMyAccountAdapter;
import com.oct.ga.account.cmd.UploadAccountAdapter;
import com.oct.ga.addrbook.cmd.SyncContactAdapter;
import com.oct.ga.addrbook.cmd.UploadContactAdapter;
import com.oct.ga.apply.cmd.ApplicantInfosQueryAdapter;
import com.oct.ga.apply.cmd.ApplicantInfosUploadAdapter;
import com.oct.ga.apply.cmd.ApplicantTemplateQueryAdapter;
import com.oct.ga.apply.cmd.ApplicantTemplateUploadAdapter;
import com.oct.ga.apply.cmd.ModifyApproveStateAdapter;
import com.oct.ga.apply.cmd.SyncApplyStateAdapter;
import com.oct.ga.appver.cmd.CheckVersionUpdateAdapter;
import com.oct.ga.badgenum.cmd.BadgeNumQueryAdapter;
import com.oct.ga.club.cmd.ActivityCancelAdapter;
import com.oct.ga.club.cmd.ActivityCreateAdapter;
import com.oct.ga.club.cmd.ActivityJoinAdapter;
import com.oct.ga.club.cmd.ActivityQueryAccountFuturePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryAccountHistoryPaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryDetailAdapter;
import com.oct.ga.club.cmd.ActivityQueryFutureFilterByLocPaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryFuturePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryHistoryPaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryImagesPaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryMemberAdapter;
import com.oct.ga.club.cmd.ActivityQueryMyAdapter;
import com.oct.ga.club.cmd.ActivityQueryMyFuturePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryMyHistoryOrderByLastUpdateTimePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQueryMyHistoryPaginationAdapter;
import com.oct.ga.club.cmd.ActivityQuerySubscribeFilterByTimeRangePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQuerySubscribeOrderByCreateTimePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQuerySubscribePaginationAdapter;
import com.oct.ga.club.cmd.ActivityQuerySubscriberAdapter;
import com.oct.ga.club.cmd.ActivityRecommendAdapter;
import com.oct.ga.club.cmd.ActivityUpdateAdapter;
import com.oct.ga.club.cmd.ActivityUpdateSubscribersAdapter;
import com.oct.ga.club.cmd.ClubCreateAdapter;
import com.oct.ga.club.cmd.ClubQueryDetailAdapter;
import com.oct.ga.club.cmd.ClubQueryMyListAdapter;
import com.oct.ga.club.cmd.ClubQuerySubcribersAdapter;
import com.oct.ga.club.cmd.ClubSubscribersAddAdapter;
import com.oct.ga.club.cmd.ClubSubscribersRemoveAdapter;
import com.oct.ga.club.cmd.ClubSubscribersUpdateAdapter;
import com.oct.ga.club.cmd.ClubUpdateAdapter;
import com.oct.ga.club.cmd.KickoutMemberAdapter;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.desc.cmd.ActivityCreateDescAdapter;
import com.oct.ga.desc.cmd.ActivityModifyAllDescAdapter;
import com.oct.ga.desc.cmd.ActivityModifyDescAdapter;
import com.oct.ga.desc.cmd.ActivityQueryDescAdapter;
import com.oct.ga.desc.cmd.ActivityRemoveDescAdapter;
import com.oct.ga.following.cmd.FollowingAdpter;
import com.oct.ga.following.cmd.ImportContactAdapter;
import com.oct.ga.following.cmd.QueryAccountAdapter;
import com.oct.ga.following.cmd.SyncFollowingAdapter;
import com.oct.ga.following.cmd.UnfollowingAdapter;
import com.oct.ga.group.cmd.DndQueryAdapter;
import com.oct.ga.group.cmd.DndSetAdapter;
import com.oct.ga.group.cmd.QueryMemberListAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastActivityJoinAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastApplyStateAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastInviteAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastInviteFeedbackAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastMessageAdapter;
import com.oct.ga.inlinecast.cmd.InlinecastTaskLogAdapter;
import com.oct.ga.invite.cmd.InviteAdapter;
import com.oct.ga.invite.cmd.InviteConfirmReceivedAdapter;
import com.oct.ga.invite.cmd.InviteFeedbackAdapter;
import com.oct.ga.invite.cmd.InviteSyncAdapter;
import com.oct.ga.invite.cmd.QueryInvitedRegisterSemiIdAdapter;
import com.oct.ga.message.cmd.ConfirmMessageReadAdapter;
import com.oct.ga.message.cmd.QueryMessageBadgeNumberAdapter;
import com.oct.ga.message.cmd.QueryMessagePaginationAdapter;
import com.oct.ga.message.cmd.UploadMessageAdapter;
import com.oct.ga.moment.cmd.AddMomentAdapter;
import com.oct.ga.moment.cmd.AddMomentCommentAdapter;
import com.oct.ga.moment.cmd.AddMomentFavoriteAdapter;
import com.oct.ga.moment.cmd.DeleteMomentAdapter;
import com.oct.ga.moment.cmd.QueryAllMomentsPaginationAdapter;
import com.oct.ga.moment.cmd.QueryClubMomentPhotoFlowPaginationAdapter;
import com.oct.ga.moment.cmd.QueryMomentAdapter;
import com.oct.ga.moment.cmd.QueryMomentCommentPaginationAdapter;
import com.oct.ga.moment.cmd.QueryMomentFavoritePaginationAdapter;
import com.oct.ga.moment.cmd.QueryMomentLogPaginationAdapter;
import com.oct.ga.moment.cmd.QueryMomentPaginationAdapter;
import com.oct.ga.moment.cmd.QueryMomentPhotoFlowPaginationAdapter;
import com.oct.ga.monitor.agent.CommandCounter;
import com.oct.ga.monitor.agent.cmd.MonitorContextResp;
import com.oct.ga.monitor.agent.cmd.MonitorSessionMapReq;
import com.oct.ga.msgflow.cmd.QueryMsgFlowPaginationAdapter;
import com.oct.ga.publish.cmd.ModifyPublishLocAdapter;
import com.oct.ga.publish.cmd.QueryLocHotAdapter;
import com.oct.ga.stp.cmd.QueryActivityBadgeNumberAdapter;
import com.oct.ga.task.cmd.DeleteTaskNoteAdapter;
import com.oct.ga.task.cmd.InlinecastTaskNotifyAdapter;
import com.oct.ga.task.cmd.ModifyTaskMembersAdapter;
import com.oct.ga.task.cmd.QueryTaskActivityPaginationAdapter;
import com.oct.ga.task.cmd.SyncChildTaskAdapter;
import com.oct.ga.task.cmd.SyncCompletedProjectAdapter;
import com.oct.ga.task.cmd.SyncTaskChildAdapter;
import com.oct.ga.task.cmd.SyncTaskCompletedAdapter;
import com.oct.ga.task.cmd.SyncTaskDetailAdapter;
import com.oct.ga.task.cmd.SyncTaskMemberAdapter;
import com.oct.ga.task.cmd.SyncTaskNoteAdapter;
import com.oct.ga.task.cmd.SyncTaskUncompletedAdapter;
import com.oct.ga.task.cmd.SyncTodayTaskAdapter;
import com.oct.ga.task.cmd.SyncTomorrowTaskAdapter;
import com.oct.ga.task.cmd.SyncUncompletedProjectAdapter;
import com.oct.ga.task.cmd.TaskCopyToAdapter;
import com.oct.ga.task.cmd.TaskMoveToAdapter;
import com.oct.ga.task.cmd.UpdateTaskStateAdapter;
import com.oct.ga.task.cmd.UploadTaskAdapter;
import com.oct.ga.task.cmd.UploadTaskNoteAdapter;
import com.oct.ga.template.cmd.MakeProject2TemplateAdapter;
import com.oct.ga.template.cmd.QueryTemplateDetailAdapter;
import com.oct.ga.template.cmd.QueryTemplateListPaginationAdapter;
import com.redoct.ga.sup.admin.adapter.ModifySupServerStateAdapter;

public class StpCommandParser
		extends CommandParser
{
	// ///////////////////////////////////////////////////////////////////////////////////////
	// encode to send...

	public static TlvObject encode(StpCommand cmd)
			throws UnsupportedEncodingException
	{
		// monitor counter
		CommandCounter commandCounter = GenericSingleton.getInstance(CommandCounter.class);
		commandCounter.increase(cmd.getTag(), 1);

		return CommandParser.encode(cmd);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// decode to handle

	public static ReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		// monitor counter
		CommandCounter commandCounter = GenericSingleton.getInstance(CommandCounter.class);
		commandCounter.increase(tlv.getTag(), 1);

		switch (tlv.getTag()) {
		case Command.STP_ARQ:
			return new STP_ARQ_Adapter().decode(tlv);
		case Command.LOGIN_REQ:
			return new LoginAdapter().decode(tlv);
		case Command.REGISTER_LOGIN_REQ:
			return new RegisterLoginAdapter().decode(tlv);
		case Command.FORGOT_PASSWORD_REQ:
			return new ForgotPwdAdapter().decode(tlv);
		case Command.CHANGE_PASSWORD_REQ:
			return new ChangePwdAdapter().decode(tlv);
		case Command.QUERY_FORGOT_PASSWORD_EMAIL_REQ:
			return new QueryForgotPwdEmailAdapter().decode(tlv);
		case Command.RESET_PASSWORD_REQ:
			return new ResetPwdAdapter().decode(tlv);
		case Command.SYNC_MY_ACCOUNT_REQ:
			return new SyncMyAccountAdapter().decode(tlv);
		case Command.HEARTBIT_REQ:
			return new HeartbitAdapter().decode(tlv);
		case Command.LOGOUT_REQ:
			return new LogoutAdapter().decode(tlv);
		case Command.DISCONNECT_REQ:
			return new DisconnectAdapter().decode(tlv);
		case Command.UPLOAD_MY_ACCOUNT_REQ:
			return new UploadAccountAdapter().decode(tlv);
		case Command.SYNC_ACCOUNT_BASE_INFO_REQ:
			return new SyncAccountBaseAdapter().decode(tlv);
		case Command.CHECK_VERSION_UPGRADE_REQ:
			return new CheckVersionUpdateAdapter().decode(tlv);
		case Command.SSO_LOGIN_REQ:
			return new SsoLoginAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Following
		case Command.FOLLOWING_REQ:
			return new FollowingAdpter().decode(tlv);
		case Command.UNFOLLOW_REQ:
			return new UnfollowingAdapter().decode(tlv);
		case Command.UPLOAD_CONTACT_REQ:
			return new UploadContactAdapter().decode(tlv);
		case Command.IMPORT_FOLLOWING_REQ:
			return new ImportContactAdapter().decode(tlv);
		case Command.SYNC_CONTACT_REQ:
			return new SyncContactAdapter().decode(tlv);
		case Command.SYNC_FOLLOWING_REQ:
			return new SyncFollowingAdapter().decode(tlv);
		case Command.QUERY_ACCOUNT_REQ:
			return new QueryAccountAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Moment
		case Command.ADD_MOMENT_REQ:
			return new AddMomentAdapter().decode(tlv);
		case Command.QUERY_MOMENT_PAGINATION_REQ:
			return new QueryMomentPaginationAdapter().decode(tlv);
		case Command.QUERY_MOMENT_PHOTOFLOW_PAGINATION_REQ:
			return new QueryMomentPhotoFlowPaginationAdapter().decode(tlv);
		case Command.QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ:
			return new QueryClubMomentPhotoFlowPaginationAdapter().decode(tlv);
		case Command.DELETE_MOMENT_REQ:
			return new DeleteMomentAdapter().decode(tlv);
		case Command.QUERY_ALL_MOMENT_PAGINATION_REQ:
			return new QueryAllMomentsPaginationAdapter().decode(tlv);
		case Command.ADD_MOMENT_FAVORITE_REQ:
			return new AddMomentCommentAdapter().decode(tlv);
		case Command.ADD_MOMENT_COMMENT_REQ:
			return new AddMomentFavoriteAdapter().decode(tlv);
		case Command.QUERY_MOMENT_FAVORITE_PAGINATION_REQ:
			return new QueryMomentFavoritePaginationAdapter().decode(tlv);
		case Command.QUERY_MOMENT_COMMENT_PAGINATION_REQ:
			return new QueryMomentCommentPaginationAdapter().decode(tlv);
		case Command.QUERY_MOMENT_LOG_PAGINATION_REQ:
			return new QueryMomentLogPaginationAdapter().decode(tlv);
		case Command.QUERY_MOMENT_REQ:
			return new QueryMomentAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Group member
		case Command.QUERY_MEMBER_LIST_REQ:
			return new QueryMemberListAdapter().decode(tlv);

		case Command.DND_QUERY_REQ:
			return new DndQueryAdapter().decode(tlv);
		case Command.DND_SET_REQ:
			return new DndSetAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Upload message
		case Command.UPLOAD_MESSAGE_REQ:
			return new UploadMessageAdapter().decode(tlv);
		case Command.CONFIRM_MESSAGE_READ_REQ:
			return new ConfirmMessageReadAdapter().decode(tlv);
		case Command.INVITE_REQ:
			return new InviteAdapter().decode(tlv);
		case Command.INVITE_FEEDBACK_REQ:
			return new InviteFeedbackAdapter().decode(tlv);
		case Command.INVITE_SYNC_REQ:
			return new InviteSyncAdapter().decode(tlv);
		case Command.INVITE_CONFIRM_RECEIVED_REQ:
			return new InviteConfirmReceivedAdapter().decode(tlv);
		case Command.INVITE_QUERY_REGISTER_SEMIID_REQ:
			return new QueryInvitedRegisterSemiIdAdapter().decode(tlv);
		case Command.INLINECAST_ACTIVITY_JOIN_REQ:
			return new InlinecastActivityJoinAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// invite log
		case Command.SYNC_APPLY_STATE_REQ:
			return new SyncApplyStateAdapter().decode(tlv);
		case Command.UPLOAD_APPLICANT_TEMPLATE_REQ:
			return new ApplicantTemplateUploadAdapter().decode(tlv);
		case Command.QUERY_APPLICANT_TEMPLATE_REQ:
			return new ApplicantTemplateQueryAdapter().decode(tlv);
		case Command.UPLOAD_APPLICANTS_REQ:
			return new ApplicantInfosUploadAdapter().decode(tlv);
		case Command.QUERY_APPLICANTS_REQ:
			return new ApplicantInfosQueryAdapter().decode(tlv);
		case Command.MODIFY_APPROVE_STATE_REQ:
			return new ModifyApproveStateAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// TaskPro
		case Command.SYNC_TASKPRO_UNCOMPLETED_REQ:
			return new SyncTaskUncompletedAdapter().decode(tlv);
		case Command.SYNC_TASKPRO_COMPLETED_REQ:
			return new SyncTaskCompletedAdapter().decode(tlv);
		case Command.SYNC_TASKPRO_DETAIL_REQ:
			return new SyncTaskDetailAdapter().decode(tlv);
		case Command.SYNC_TASKPRO_MEMBER_REQ:
			return new SyncTaskMemberAdapter().decode(tlv);
		case Command.SYNC_TASKPRO_NOTE_REQ:
			return new SyncTaskNoteAdapter().decode(tlv);
		case Command.SYNC_TASKPRO_CHILD_REQ:
			return new SyncTaskChildAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// TaskExt
		case Command.SYNC_PROJECT_UNCOMPLETED_REQ:
			return new SyncUncompletedProjectAdapter().decode(tlv);
		case Command.SYNC_PROJECT_COMPLETED_REQ:
			return new SyncCompletedProjectAdapter().decode(tlv);
		case Command.SYNC_CHILD_TASK_REQ:
			return new SyncChildTaskAdapter().decode(tlv);
		case Command.SYNC_TODAY_TASK_REQ:
			return new SyncTodayTaskAdapter().decode(tlv);
		case Command.SYNC_TOMORROW_TASK_REQ:
			return new SyncTomorrowTaskAdapter().decode(tlv);

		case Command.MESSAGE_FLOW_QUERY_PAGINATION_REQ:
			return new QueryMsgFlowPaginationAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Monitor
		case Command.MONITOR_SESSION_MAP_REQ:
			return new MonitorSessionMapReq().decode(tlv);
		case Command.MONITOR_CONTEXT_RESP:
			return new MonitorContextResp().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Task
		case Command.UPLOAD_TASK_REQ:
			return new UploadTaskAdapter().decode(tlv);
		case Command.UPDATE_TASK_STATE_REQ:
			return new UpdateTaskStateAdapter().decode(tlv);
		case Command.UPLOAD_TASK_NOTE_REQ:
			return new UploadTaskNoteAdapter().decode(tlv);
		case Command.DELETE_TASK_NOTE_REQ:
			return new DeleteTaskNoteAdapter().decode(tlv);
			// case Command.ADD_FILELINK_REQ:
			// return new AddFileLinkAdapter().decode(tlv);
			// case Command.DELETE_FILELINK_REQ:
			// return new DeleteFileLinkAdapter().decode(tlv);
		case Command.QUERY_MESSAGE_PAGINATION_REQ:
			return new QueryMessagePaginationAdapter().decode(tlv);
		case Command.QUERY_TASK_ACTIVITY_PAGINATION_REQ:
			return new QueryTaskActivityPaginationAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// Template
		case Command.QUERY_TEMPLATE_LIST_PAGINATION_REQ:
			return new QueryTemplateListPaginationAdapter().decode(tlv);
		case Command.QUERY_TEMPLATE_DETAIL_REQ:
			return new QueryTemplateDetailAdapter().decode(tlv);
		case Command.MAKE_PROJECT_TO_TEMPLATE_REQ:
			return new MakeProject2TemplateAdapter().decode(tlv);
		case Command.TASK_MOVE_TO_REQ:
			return new TaskMoveToAdapter().decode(tlv);
		case Command.TASK_COPY_TO_REQ:
			return new TaskCopyToAdapter().decode(tlv);

		case Command.INLINECAST_MESSAGE_REQ:
			return new InlinecastMessageAdapter().decode(tlv);
		case Command.INLINECAST_INVITE_REQ:
			return new InlinecastInviteAdapter().decode(tlv);
		case Command.INLINECAST_INVITE_FEEDBACK_REQ:
			return new InlinecastInviteFeedbackAdapter().decode(tlv);
		case Command.INLINECAST_TASK_ACTIVITY_REQ:
			return new InlinecastTaskNotifyAdapter().decode(tlv);
		case Command.INLINECAST_APPLY_STATE_REQ:
			return new InlinecastApplyStateAdapter().decode(tlv);
		case Command.INLINECAST_TASK_LOG_REQ:
			return new InlinecastTaskLogAdapter().decode(tlv);

		case Command.QUERY_MESSAGE_BADGE_NUMBER_REQ:
			return new QueryMessageBadgeNumberAdapter().decode(tlv);
		case Command.QUERY_ACTIVITY_BADGE_NUMBER_REQ:
			return new QueryActivityBadgeNumberAdapter().decode(tlv);
		case Command.QUERY_BADGE_NUMBER_REQ:
			return new BadgeNumQueryAdapter().decode(tlv);

			// /////////////////////////////////////////////////////////
			// ClubMasterInfo
		case Command.ACTIVITY_CREATE_REQ:
			return new ActivityCreateAdapter().decode(tlv);
		case Command.ACTIVITY_UPDATE_REQ:
			return new ActivityUpdateAdapter().decode(tlv);
		case Command.ACTIVITY_CANCEL_REQ:
			return new ActivityCancelAdapter().decode(tlv);
		case Command.ACTIVITY_JOIN_REQ:
			return new ActivityJoinAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_HISTORY_PAGINATION_REQ:
			return new ActivityQueryHistoryPaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_IMAGES_PAGINATION_REQ:
			return new ActivityQueryImagesPaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_MEMBER_REQ:
			return new ActivityQueryMemberAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBERS_REQ:
			return new ActivityQuerySubscriberAdapter().decode(tlv);
		case Command.ACTIVITY_RECOMMEND_REQ:
			return new ActivityRecommendAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_DETAIL_REQ:
			return new ActivityQueryDetailAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_REQ:
			return new ActivityQueryMyAdapter().decode(tlv);
		case Command.TASKPRO_MODIFY_MEMBERS_REQ:
			return new ModifyTaskMembersAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_REQ:
			return new ActivityQuerySubscribePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_HISTORY_PAGINATION_REQ:
			return new ActivityQueryMyHistoryPaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_FUTURE_PAGINATION_REQ:
			return new ActivityQueryMyFuturePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_REQ:
			return new ActivityQueryAccountHistoryPaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_ACCOUNT_FUTURE_PAGINATION_REQ:
			return new ActivityQueryAccountFuturePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_UPDATE_SUBSCRIBERS_REQ:
			return new ActivityUpdateSubscribersAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_ORDER_BY_CREATRE_TIME_PAGINATION_REQ:
			return new ActivityQuerySubscribeOrderByCreateTimePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_MY_HISTORY_ORDERBY_LAST_UPDATE_TIME_PAGINATION_REQ:
			return new ActivityQueryMyHistoryOrderByLastUpdateTimePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_REQ:
			return new ActivityQueryFutureFilterByLocPaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_REQ:
			return new ActivityQuerySubscribeFilterByTimeRangePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_QUERY_FUTURE_PAGINATION_REQ:
			return new ActivityQueryFuturePaginationAdapter().decode(tlv);
		case Command.ACTIVITY_KICKOUT_MEMBER_REQ:
			return new KickoutMemberAdapter().decode(tlv);

		case Command.PUBLISH_QUERY_LOC_HOT_PAGINATION_REQ:
			return new QueryLocHotAdapter().decode(tlv);
		case Command.PUBLISH_MODIFY_LOC_REQ:
			return new ModifyPublishLocAdapter().decode(tlv);

		case Command.ACTIVITY_DESC_CREATE_REQ:
			return new ActivityCreateDescAdapter().decode(tlv);
		case Command.ACTIVITY_DESC_MODIFY_REQ:
			return new ActivityModifyDescAdapter().decode(tlv);
		case Command.ACTIVITY_DESC_MODIFY_ALL_REQ:
			return new ActivityModifyAllDescAdapter().decode(tlv);
		case Command.ACTIVITY_DESC_QUERY_REQ:
			return new ActivityQueryDescAdapter().decode(tlv);
		case Command.ACTIVITY_DESC_REMOVE_REQ:
			return new ActivityRemoveDescAdapter().decode(tlv);

		case Command.CLUB_CREATE_REQ:
			return new ClubCreateAdapter().decode(tlv);
		case Command.CLUB_UPDATE_REQ:
			return new ClubUpdateAdapter().decode(tlv);
		case Command.CLUB_QUERY_MYLIST_REQ:
			return new ClubQueryMyListAdapter().decode(tlv);
		case Command.CLUB_QUERY_SUBSCRIBER_REQ:
			return new ClubQuerySubcribersAdapter().decode(tlv);
		case Command.CLUB_QUERY_DETAIL_REQ:
			return new ClubQueryDetailAdapter().decode(tlv);
		case Command.CLUB_SUBSCRIBER_ADD_REQ:
			return new ClubSubscribersAddAdapter().decode(tlv);
		case Command.CLUB_SUBSCRIBER_REMOVE_REQ:
			return new ClubSubscribersRemoveAdapter().decode(tlv);
		case Command.CLUB_SUBSCRIBER_UPDATE_REQ:
			return new ClubSubscribersUpdateAdapter().decode(tlv);

		case Command.MODIFY_SUP_STATE_REQ:
			return new ModifySupServerStateAdapter().decode(tlv);

		case Command.APPLY_PHONE_REGISTER_VERIFICATION_CODE_REQ:
			return new ApplyPhoneRegisterVerificationCodeAdapter().decode(tlv);
		case Command.PHONE_REGISTER_LOGIN_REQ:
			return new PhoneRegisterLoginAdapter().decode(tlv);
		case Command.APPLY_BIND_PHONE_REQ:
			return new ApplyBindPhoneAdapter().decode(tlv);
		case Command.BIND_PHONE_REQ:
			return new BindPhoneAdapter().decode(tlv);
		case Command.BIND_MARGE_PHONE_REQ:
			return new BindMargePhoneAdapter().decode(tlv);
		case Command.DEVICE_REGISTER_LOGIN_REQ:
			return new DeviceRegisterLoginAdapter().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
