package com.oct.ga.addrbook.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.oct.ga.addrbook.dao.GaContactDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.Contact;

/**
 * (Friends) Data Access Object.
 * 
 * @author Thomas.Zhang
 */
public class ContactDaoImpl
		extends JdbcDaoSupport
		implements GaContactDao
{
	@Override
	public void updateMyStateInFriendsContact(final String accountId, final String email, final int timestamp)
	{
		String sql = "UPDATE cscart_ga_contact SET AccountId=?,State=?,LastUpdateTime=? WHERE Email=?";
		logger.debug(sql + accountId + "," + email);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
				ps.setShort(2, GlobalArgs.CONTACT_STATE_NOT_FRIEND);
				ps.setInt(3, timestamp);
				ps.setString(4, email);
			}
		});
	}

	@Override
	public Contact queryContactByAccountId(final String myAccountId, final String contactAccountID)
	{
		final Contact data = new Contact();

		String sql = "SELECT c.ContactId,c.Nickname,c.Email,c.Telephone,a.FacePhoto,c.CreateTime,c.LastUpdateTime,c.State,c.MyAccountID,c.AccountID FROM cscart_ga_contact c,cscart_ga_account a WHERE c.AccountId=a.AccountId AND c.MyAccountId=? AND c.AccountId=?";
		logger.debug(sql + myAccountId + "," + contactAccountID);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myAccountId);
				ps.setString(2, contactAccountID);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setContactId(rs.getString(1));
				data.setNickname(rs.getString(2));
				data.setEmail(rs.getString(3));
				data.setTelephone(rs.getString(4));
				data.setFacePhoto(rs.getString(5));
				data.setState(rs.getShort(8));
				data.setMyAccountID(rs.getString(9));
				data.setAccountId(rs.getString(10));
			}
		});

		return data;
	}

	@Override
	public List<Contact> queryLastUpdateContact(final String myAccountId, final int timestamp)
	{
		final List<Contact> array = new ArrayList<Contact>();

		String sql = "SELECT c.ContactId,c.Nickname,c.Email,c.Telephone,a.FacePhoto,c.CreateTime,c.LastUpdateTime,c.State,c.MyAccountId,c.AccountId FROM cscart_ga_contact c LEFT JOIN cscart_ga_account a ON (c.AccountId=a.AccountId) WHERE c.MyAccountId=? AND c.LastUpdateTime>?";
		logger.debug(sql + myAccountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, myAccountId);
				ps.setInt(2, timestamp);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				Contact data = new Contact();
				data.setContactId(rs.getString(1));
				data.setNickname(rs.getString(2));
				data.setEmail(rs.getString(3));
				data.setTelephone(rs.getString(4));
				data.setFacePhoto(rs.getString(5));
				data.setState(rs.getShort(8));
				data.setMyAccountID(rs.getString(9));
				data.setAccountId(rs.getString(10));

				array.add(data);
			}
		});

		return array;
	}

	@Override
	public Contact queryContact(final String contactId)
	{
		final Contact data = new Contact();

		String sql = "SELECT c.ContactId,c.Nickname,c.Email,c.Telephone,a.FacePhoto,c.CreateTime,c.LastUpdateTime,c.State,c.MyAccountId,c.AccountId FROM cscart_ga_contact c,cscart_ga_account a WHERE c.AccountId=a.AccountId AND ContactId=?";
		logger.debug(sql + contactId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, contactId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				data.setContactId(rs.getString(1));
				data.setNickname(rs.getString(2));
				data.setEmail(rs.getString(3));
				data.setTelephone(rs.getString(4));
				data.setFacePhoto(rs.getString(5));
				data.setState(rs.getShort(8));
				data.setMyAccountID(rs.getString(9));
				data.setAccountId(rs.getString(10));
			}
		});

		return data;
	}

	@Override
	public void add(final Contact data)
	{
		String sql = "INSERT INTO cscart_ga_contact (ContactId,Nickname,Email,Telephone,FacePhoto,CreateTime,LastUpdateTime,State,MyAccountId,AccountId) VALUES (?,?,?,?,?,?,?,?,?,?)";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getContactId());
				ps.setString(2, data.getNickname());
				ps.setString(3, data.getEmail());
				ps.setString(4, data.getTelephone());
				ps.setString(5, data.getFacePhoto());
				ps.setShort(8, data.getState());
				ps.setString(9, data.getMyAccountID());
				ps.setString(10, data.getAccountId());
			}
		});
	}

	@Override
	public void update(final Contact data)
	{
		String sql = "UPDATE cscart_ga_contact SET Nickname=?,Email=?,Telephone=?,FacePhoto=?,LastUpdateTime=?,State=?,MyAccountId=?,AccountId=? WHERE ContactId=?";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, data.getNickname());
				ps.setString(2, data.getEmail());
				ps.setString(3, data.getTelephone());
				ps.setString(4, data.getFacePhoto());
				ps.setShort(6, data.getState());
				ps.setString(7, data.getMyAccountID());
				ps.setString(8, data.getAccountId());
				ps.setString(9, data.getContactId());
			}
		});
	}

	@Override
	public void remove(final String contactId)
	{
		String sql = "DELETE FROM cscart_ga_contact WHERE ContactId=?";
		logger.debug(sql);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, contactId);
			}
		});
	}

	@Override
	public void updateMyInfoInFriendsContact(final String accountId, final String accountName, final String facePhoto,
			final int timestamp)
	{
		if (facePhoto != null && facePhoto.length() > 0) {
			String sql = "UPDATE cscart_ga_contact SET FacePhoto=?,LastUpdateTime=? WHERE AccountId=?";
			logger.debug(sql + accountId + "," + accountName);

			this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
			{
				public void setValues(PreparedStatement ps)
						throws SQLException
				{
					ps.setString(1, facePhoto);
					ps.setInt(2, timestamp);
					ps.setString(3, accountId);
				}
			});
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ContactDaoImpl.class);
}
