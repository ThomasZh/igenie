package com.oct.ga.template.dao.spring;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import net.sf.json.JSONArray;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.template.ChecknameJsonBean;
import com.oct.ga.comm.domain.template.ClwcJsonBean;
import com.oct.ga.comm.domain.template.GaTemplateMaster;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.stp.utility.PaginationHelper;
import com.oct.ga.template.dao.GaTemplateDao;

/**
 * (GaTemplateDao) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class TemplateDaoImpl
		extends JdbcDaoSupport
		implements GaTemplateDao
{
	@Override
	public Page<GaTemplateMaster> queryRecommendPagination(final short templateType, int pageNum, int pageSize)
	{
		PaginationHelper<GaTemplateMaster> ph = new PaginationHelper<GaTemplateMaster>();
		String countSql = "SELECT count(TemplateId) FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND TemplatePid is null";
		String sql = "SELECT TemplateId,TemplateName,LastUpdateTime,AccountName,Copys,MAX(Version) FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND TemplatePid is null GROUP BY TemplateId ORDER BY Copys";
		logger.debug(sql + templateType);

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { templateType,
				GlobalArgs.TEMPLATE_SUPPLIER_TYPE_RECOMMEND }, pageNum, pageSize,
				new ParameterizedRowMapper<GaTemplateMaster>()
				{
					public GaTemplateMaster mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaTemplateMaster data = new GaTemplateMaster();
						data.setTemplateId(rs.getString(1));
						data.setTemplateName(rs.getString(2));
						data.setLastUpdateTime(rs.getInt(3));
						data.setAccountName(rs.getString(4));
						data.setCopys(rs.getInt(5));

						return data;
					}
				});
	}

	@Override
	public Page<GaTemplateMaster> queryVendorPagination(final short templateType, final String taskId, int pageNum,
			int pageSize)
	{
		PaginationHelper<GaTemplateMaster> ph = new PaginationHelper<GaTemplateMaster>();
		String countSql = "SELECT count(TemplateId) FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND TaskId=? AND TemplatePid is null";
		String sql = "SELECT TemplateId,TemplateName,LastUpdateTime,AccountName,Copys,MAX(Version) FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND TaskId=? AND TemplatePid is null GROUP BY TemplateId ORDER BY Copys";
		logger.debug(sql + templateType + "," + taskId);

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { templateType,
				GlobalArgs.TEMPLATE_SUPPLIER_TYPE_VENDOR, taskId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaTemplateMaster>()
				{
					public GaTemplateMaster mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaTemplateMaster data = new GaTemplateMaster();
						data.setTemplateId(rs.getString(1));
						data.setTemplateName(rs.getString(2));
						data.setLastUpdateTime(rs.getInt(3));
						data.setAccountName(rs.getString(4));
						data.setCopys(rs.getInt(5));

						return data;
					}
				});
	}

	@Override
	public Page<GaTemplateMaster> queryMinePagination(final short templateType, final String accountId, int pageNum,
			int pageSize)
	{
		PaginationHelper<GaTemplateMaster> ph = new PaginationHelper<GaTemplateMaster>();
		String countSql = "SELECT count(distinct TemplateId) FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND AccountId=? AND TemplatePid is null";
		String sql = "SELECT TemplateId,TemplateName,LastUpdateTime,AccountName,Copys,MAX(Version),TaskId FROM cscart_ga_template WHERE TemplateType=? AND SupplierType=? AND AccountId=? AND TemplatePid is null GROUP BY TaskId ORDER BY LastUpdateTime";
		logger.debug(sql + templateType + "," + accountId);

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { templateType,
				GlobalArgs.TEMPLATE_SUPPLIER_TYPE_MINE, accountId }, pageNum, pageSize,
				new ParameterizedRowMapper<GaTemplateMaster>()
				{
					public GaTemplateMaster mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GaTemplateMaster data = new GaTemplateMaster();
						data.setTemplateId(rs.getString(1));
						data.setTemplateName(rs.getString(2));
						data.setLastUpdateTime(rs.getInt(3));
						data.setAccountName(rs.getString(4));
						data.setCopys(rs.getInt(5));

						return data;
					}
				});
	}

	@Override
	public TemplateDefineJsonBean queryMaxVersion(final String templateId)
	{
		final TemplateDefineJsonBean data = new TemplateDefineJsonBean();

		String sql = "SELECT TemplateId,TemplateName,TemplateType,SupplierType,MAX(Version),TemplatePid,ExtAttrType,ExtAttr,TemplateDesc,StartTime,EndTime,CreateTime,LastUpdateTime,PermissionMode,SplitForEachMember,FeedbackInvite,FeedbackUpdate,Copys,AccountId,AccountName,TaskId FROM cscart_ga_template WHERE TemplateId=? GROUP BY TemplateId";
		logger.debug(sql + templateId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, templateId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setTemplateId(rs.getString(1));
				data.setTemplateName(rs.getString(2));
				data.setTemplateType(rs.getShort(3));
				data.setSupplierType(rs.getShort(4));
				data.setVersion(rs.getInt(5));
				data.setTemplatePid(rs.getString(6));
				data.setExtAttrType(rs.getShort(7));

				String jsonStr = null;
				try {

					Blob blob = rs.getBlob(8);
					if (blob != null && blob.length() > 0) {
						byte[] bytes = blob.getBytes(1, (int) blob.length());
						jsonStr = (new String(bytes, "UTF-8"));
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				switch (data.getExtAttrType()) {
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST:
					List<ChecknameJsonBean> checklist = JsonParser.json2Checklist(jsonStr);
					for (ChecknameJsonBean check : checklist) {
						data.addChildExtAttr(check);
					}
					data.setExtAttr(checklist);
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CLWC_LIST:
					List<ClwcJsonBean> clwclist = JsonParser.json2Clwclist(jsonStr);
					for (ClwcJsonBean clwc : clwclist) {
						data.addChildExtAttr(clwc);
					}
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK:
				default:
					break;
				}

				data.setTemplateDesc(rs.getString(9));
				data.setStartTime(rs.getInt(10));
				data.setEndTime(rs.getInt(11));
				data.setCreateTime(rs.getInt(12));
				data.setLastUpdateTime(rs.getInt(13));
				data.setPermissionMode(rs.getInt(14));
				data.setSplitForEachMember(rs.getInt(15) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackInvite(rs.getInt(16) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackUpdate(rs.getInt(17) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setCopys(rs.getInt(18));
				data.setAccountId(rs.getString(19));
				data.setAccountName(rs.getString(20));
				data.setTaskId(rs.getString(21));
			}
		});

		return data;
	}

	@Override
	public TemplateDefineJsonBean query(final String templateId, final int version)
	{
		final TemplateDefineJsonBean data = new TemplateDefineJsonBean();

		String sql = "SELECT TemplateId,TemplateName,TemplateType,SupplierType,MAX(Version),TemplatePid,ExtAttrType,ExtAttr,TemplateDesc,StartTime,EndTime,CreateTime,LastUpdateTime,PermissionMode,SplitForEachMember,FeedbackInvite,FeedbackUpdate,Copys,AccountId,AccountName,TaskId FROM cscart_ga_template WHERE TemplateId=? AND Version=? GROUP BY TemplateId";
		logger.debug(sql + templateId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, templateId);
				ps.setInt(2, version);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setTemplateId(rs.getString(1));
				data.setTemplateName(rs.getString(2));
				data.setTemplateType(rs.getShort(3));
				data.setSupplierType(rs.getShort(4));
				data.setVersion(rs.getInt(5));
				data.setTemplatePid(rs.getString(6));
				data.setExtAttrType(rs.getShort(7));

				String jsonStr = null;
				try {

					Blob blob = rs.getBlob(8);
					if (blob != null && blob.length() > 0) {
						byte[] bytes = blob.getBytes(1, (int) blob.length());
						jsonStr = (new String(bytes, "UTF-8"));
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				switch (data.getExtAttrType()) {
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST:
					List<ChecknameJsonBean> checklist = JsonParser.json2Checklist(jsonStr);
					for (ChecknameJsonBean check : checklist) {
						data.addChildExtAttr(check);
					}
					data.setExtAttr(checklist);
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CLWC_LIST:
					List<ClwcJsonBean> clwclist = JsonParser.json2Clwclist(jsonStr);
					for (ClwcJsonBean clwc : clwclist) {
						data.addChildExtAttr(clwc);
					}
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK:
				default:
					break;
				}

				data.setTemplateDesc(rs.getString(9));
				data.setStartTime(rs.getInt(10));
				data.setEndTime(rs.getInt(11));
				data.setCreateTime(rs.getInt(12));
				data.setLastUpdateTime(rs.getInt(13));
				data.setPermissionMode(rs.getInt(14));
				data.setSplitForEachMember(rs.getInt(15) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackInvite(rs.getInt(16) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackUpdate(rs.getInt(17) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setCopys(rs.getInt(18));
				data.setAccountId(rs.getString(19));
				data.setAccountName(rs.getString(20));
				data.setTaskId(rs.getString(21));
			}
		});

		return data;
	}

	@Override
	public List<TemplateDefineJsonBean> queryChildren(final String templatePid)
	{
		final List<TemplateDefineJsonBean> array = new ArrayList<TemplateDefineJsonBean>();

		String sql = "SELECT TemplateId,TemplateName,TemplateType,SupplierType,Version,ExtAttrType,ExtAttr,TemplateDesc,StartTime,EndTime,CreateTime,LastUpdateTime,PermissionMode,SplitForEachMember,FeedbackInvite,FeedbackUpdate,Copys,AccountId,AccountName,TaskId FROM cscart_ga_template WHERE TemplatePid=? ORDER BY StartTime";
		logger.debug(sql + templatePid);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, templatePid);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				final TemplateDefineJsonBean data = new TemplateDefineJsonBean();

				data.setTemplateId(rs.getString(1));
				data.setTemplateName(rs.getString(2));
				data.setTemplateType(rs.getShort(3));
				data.setSupplierType(rs.getShort(4));
				data.setVersion(rs.getInt(5));
				data.setTemplatePid(templatePid);
				data.setExtAttrType(rs.getShort(6));

				String jsonStr = null;
				try {

					Blob blob = rs.getBlob(7);
					if (blob != null && blob.length() > 0) {
						byte[] bytes = blob.getBytes(1, (int) blob.length());
						jsonStr = (new String(bytes, "UTF-8"));
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				switch (data.getExtAttrType()) {
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST:
					List<ChecknameJsonBean> checklist = JsonParser.json2Checklist(jsonStr);
					for (ChecknameJsonBean check : checklist) {
						data.addChildExtAttr(check);
					}
					data.setExtAttr(checklist);
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CLWC_LIST:
					List<ClwcJsonBean> clwclist = JsonParser.json2Clwclist(jsonStr);
					for (ClwcJsonBean clwc : clwclist) {
						data.addChildExtAttr(clwc);
					}
					break;
				case GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK:
				default:
					break;
				}

				data.setTemplateDesc(rs.getString(8));
				data.setStartTime(rs.getInt(9));
				data.setEndTime(rs.getInt(10));
				data.setCreateTime(rs.getInt(11));
				data.setLastUpdateTime(rs.getInt(12));
				data.setPermissionMode(rs.getInt(13));
				data.setSplitForEachMember(rs.getInt(14) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackInvite(rs.getInt(15) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setFeedbackUpdate(rs.getInt(16) == ErrorCode.UNKNOWN_FAILURE ? false : true);
				data.setCopys(rs.getInt(17));
				data.setAccountId(rs.getString(18));
				data.setAccountName(rs.getString(19));
				data.setTaskId(rs.getString(20));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public void add(final TemplateDefineJsonBean data)
	{
		String sql = "INSERT INTO cscart_ga_template (TemplateId,TemplateName,TemplateType,SupplierType,TemplatePid,Version,ExtAttrType,ExtAttr,TemplateDesc,StartTime,EndTime,CreateTime,LastUpdateTime,PermissionMode,SplitForEachMember,FeedbackInvite,FeedbackUpdate,AccountId,AccountName,TaskId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getTemplateId());
				ps.setString(2, data.getTemplateName());
				ps.setShort(3, data.getTemplateType());
				ps.setShort(4, data.getSupplierType());
				ps.setString(5, data.getTemplatePid());
				ps.setInt(6, data.getVersion());
				ps.setShort(7, data.getExtAttrType());

				JSONArray jsonArray = JSONArray.fromObject(data.getExtAttr());
				String extAttrStr = jsonArray.toString();

				try {

					Blob blob = null;
					if (extAttrStr != null && extAttrStr.length() > 0) {
						blob = new SerialBlob(extAttrStr.getBytes("UTF-8"));
					}
					ps.setBlob(8, blob);// mysql

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ps.setString(9, data.getTemplateDesc());
				ps.setInt(10, data.getStartTime());
				ps.setInt(11, data.getEndTime());
				ps.setInt(12, data.getCreateTime());
				ps.setInt(13, data.getLastUpdateTime());
				ps.setInt(14, data.getPermissionMode());
				ps.setInt(15, data.isSplitForEachMember() ? ErrorCode.SUCCESS : ErrorCode.UNKNOWN_FAILURE);
				ps.setInt(16, data.isFeedbackInvite() ? ErrorCode.SUCCESS : ErrorCode.UNKNOWN_FAILURE);
				ps.setInt(17, data.isFeedbackUpdate() ? ErrorCode.SUCCESS : ErrorCode.UNKNOWN_FAILURE);
				ps.setString(18, data.getAccountId());
				ps.setString(19, data.getAccountName());
				ps.setString(20, data.getTaskId());
			}
		});
	}

	@Override
	public int queryMaxVersionByTask(final String taskId)
	{
		String sql = "SELECT MAX(Version) FROM cscart_ga_template WHERE TaskId=?";
		logger.debug(sql + taskId);

		Object[] params = new Object[] { taskId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count;
	}

	@Override
	public String queryTemplateId(String taskId)
	{
		String templateId = null;

		String sql = "SELECT distinct coalesce(TemplateId,'') FROM cscart_ga_template WHERE TaskId=?";
		logger.debug("SELECT distinct coalesce(TemplateId,'') FROM cscart_ga_template WHERE TaskId=" + taskId);

		try {
			Object[] params = new Object[] { taskId };
			templateId = (String) this.getJdbcTemplate().queryForObject(sql, params, String.class);
		} catch (Exception e) {
			logger.warn(LogErrorMessage.getFullInfo(e));
		}
		return templateId;
	}

}
