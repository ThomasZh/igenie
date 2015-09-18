package com.oct.ga.club.dao.spring;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.oct.ga.club.dao.ItineraryDao;
import com.oct.ga.comm.domain.club.ItineraryInfo;

@Repository
public class ItineraryDaoImpl extends JdbcDaoSupport implements ItineraryDao {
	private static final RowMapper ITINERARY_INFO_ROW_MAPPER = new RowMapper() {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ItineraryInfo itineraryInfo = new ItineraryInfo();
			itineraryInfo.setActivityId(rs.getString("ACTIVITY_ID"));
			itineraryInfo.setIdx(rs.getInt("IDX"));
			itineraryInfo.setBeginTime(rs.getInt("BEGIN_TIME"));
			itineraryInfo.setEndTime(rs.getInt("END_TIME"));
			itineraryInfo.setTitle(rs.getString("TITLE"));
			itineraryInfo.setLocation(rs.getString("LOCATION"));
			Blob blob = rs.getBlob("DESC_");
			if (blob != null && blob.length() > 0) {
				byte[] bytes = blob.getBytes(1, (int) blob.length());
				String desc = new String(bytes, Charset.forName("UTF-8"));
				itineraryInfo.setDesc(desc);
			}
			itineraryInfo.setImageUrls(rs.getString("IMAGE_URLS"));
			itineraryInfo.setGeoX(rs.getString("GEO_X"));
			itineraryInfo.setGeoY(rs.getString("GEO_Y"));
			return itineraryInfo;
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public List<ItineraryInfo> find(String activityId) {
		String sql = "select * from GA_ITINERARY_INFO i where i.ACTIVITY_ID = ? order by i.IDX asc";
		return getJdbcTemplate().query(sql, new Object[] { activityId }, ITINERARY_INFO_ROW_MAPPER);
	}

	@Override
	public void create(final List<ItineraryInfo> itineraryInfos) {
		String sql = "insert into GA_ITINERARY_INFO(ACTIVITY_ID, IDX, BEGIN_TIME, END_TIME, TITLE,"
				+ " LOCATION, DESC_, IMAGE_URLS, GEO_X, GEO_Y) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		final Iterator<ItineraryInfo> iterator = itineraryInfos.iterator();
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int j = 1;
				ItineraryInfo itineraryInfo = iterator.next();
				ps.setString(j++, itineraryInfo.getActivityId());
				ps.setInt(j++, itineraryInfo.getIdx());
				ps.setInt(j++, itineraryInfo.getBeginTime());
				ps.setInt(j++, itineraryInfo.getEndTime());
				ps.setString(j++, itineraryInfo.getTitle());
				ps.setString(j++, itineraryInfo.getLocation());
				String desc = itineraryInfo.getDesc();
				if (desc != null && desc.length() > 0) {
					byte[] bytes = desc.getBytes(Charset.forName("UTF-8"));
					Blob blob = new SerialBlob(bytes);
					ps.setBlob(j++, blob);
				} else {
					ps.setObject(j++, null);
				}
				ps.setString(j++, itineraryInfo.getImageUrls());
				ps.setString(j++, itineraryInfo.getGeoX());
				ps.setString(j++, itineraryInfo.getGeoY());
			}

			@Override
			public int getBatchSize() {
				return itineraryInfos.size();
			}
		});
	}

	@Override
	public int delete(String activityId) {
		String sql = "delete from GA_ITINERARY_INFO where ACTIVITY_ID = ?";
		return getJdbcTemplate().update(sql, new Object[] { activityId });
	}

	@Autowired
	public void setGaDataSource(@Qualifier("gaDataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

}
