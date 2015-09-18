package com.oct.ga.appver.dao.spring;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.appver.dao.GaAppVersionDao;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;

public class AppVersionDaoImpl
		extends JdbcDaoSupport
		implements GaAppVersionDao
{
	@Override
	public short queryPriority(String clientVersion)
	{
		int count = ErrorCode.SUCCESS;

		try {
			String sql = "SELECT priority FROM ga_app_version WHERE client_version=?";
			logger.debug("SELECT priority FROM ga_app_version WHERE client_version=" + clientVersion);

			Object[] params = new Object[] { clientVersion };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.debug("SELECT priority FROM ga_app_version WHERE client_version=" + clientVersion);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

}
