package com.oct.ga.activity.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.oct.ga.activity.dao.ItineraryDao;
import com.oct.ga.activity.domain.ActivityConstants;
import com.oct.ga.activity.domain.DayItinerary;
import com.oct.ga.activity.domain.EquipmentItinerary;
import com.oct.ga.activity.domain.GeoItinerary;
import com.oct.ga.activity.domain.Itinerary;
import com.oct.ga.activity.domain.RouteItinerary;

@Repository
public class ItineraryDaoImpl extends JdbcDaoSupport implements ItineraryDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItineraryDaoImpl.class);

	@Override
	public List<Itinerary> findByActivityId(String activityId, int idx, int pageSize) {
		String sql = "select * from APLAN_ITINERARY where ACTIVITY_ID = ? and IDX > ? order by IDX asc limit 0, ?";
		return getJdbcTemplate().query(sql, new RowMappperImpl(), activityId, idx, pageSize);
	}

	@Override
	public void create(Itinerary itinerary) {
		itinerary.setId(Util.generateUUID());
		itinerary.setCreateTime(Util.currentTimeMillis());
		String sql;
		Object[] args;
		int type = itinerary.getType();
		switch (type) {
		case ActivityConstants.ITINERARY_TYPE_GEO:
			sql = "insert into APLAN_ITINERARY (ID_, ACTIVITY_ID, TITLE, DESC_, TYPE_, IDX, CREATE_TIME,"
					+ " BEGIN_TIME, END_TIME, DEST_LOCATION, DESC_GEO_X, DESC_GEO_Y, IMAGE_URLS)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			GeoItinerary geoItinerary = (GeoItinerary) itinerary;
			args = new Object[] { itinerary.getId(), itinerary.getActivityId(), itinerary.getTitle(),
					itinerary.getDesc(), itinerary.getType(), itinerary.getIdx(), itinerary.getCreateTime(),
					geoItinerary.getBeginTime(), geoItinerary.getEndTime(), geoItinerary.getDestLocation(),
					geoItinerary.getDestGeoX(), geoItinerary.getDestGeoY(),
					geoItinerary.getImageUrls() == null ? null : Util.toJson(geoItinerary.getImageUrls()) };
			break;
		case ActivityConstants.ITINERARY_TYPE_ROUTE:

			sql = "insert into APLAN_ITINERARY (ID_, ACTIVITY_ID, TITLE, DESC_, TYPE_, IDX, CREATE_TIME,"
					+ " BEGIN_TIME, END_TIME, ORIGINAL_LOCATION, ORIGINAL_GEO_X, ORIGINAL_GEO_Y, DEST_LOCATION, DESC_GEO_X, DESC_GEO_Y, IMAGE_URLS)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			RouteItinerary routeItinerary = (RouteItinerary) itinerary;
			args = new Object[] { itinerary.getId(), itinerary.getActivityId(), itinerary.getTitle(),
					itinerary.getDesc(), itinerary.getType(), itinerary.getIdx(), itinerary.getCreateTime(),
					routeItinerary.getBeginTime(), routeItinerary.getEndTime(), routeItinerary.getOriginalLocation(),
					routeItinerary.getOriginalGeoX(), routeItinerary.getOriginalGeoY(),
					routeItinerary.getDestLocation(), routeItinerary.getDestGeoX(), routeItinerary.getDestGeoY(),
					routeItinerary.getImageUrls() == null ? null : Util.toJson(routeItinerary.getImageUrls()) };
			break;
		case ActivityConstants.ITINERARY_TYPE_DAY:
			sql = "insert into APLAN_ITINERARY (ID_, ACTIVITY_ID, TITLE, DESC_, TYPE_, IDX, CREATE_TIME, BEGIN_TIME, END_TIME)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			DayItinerary dayItinerary = (DayItinerary) itinerary;
			args = new Object[] { itinerary.getId(), itinerary.getActivityId(), itinerary.getTitle(),
					itinerary.getDesc(), itinerary.getType(), itinerary.getIdx(), itinerary.getCreateTime(),
					dayItinerary.getBeginTime(), dayItinerary.getEndTime() };
			break;
		case ActivityConstants.ITINERARY_TYPE_EQUIPMENT:
			sql = "insert into APLAN_ITINERARY (ID_, ACTIVITY_ID, TITLE, DESC_, TYPE_, IDX, CREATE_TIME, BEGIN_TIME, END_TIME)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			EquipmentItinerary equipmentItinerary = (EquipmentItinerary) itinerary;
			args = new Object[] { itinerary.getId(), itinerary.getActivityId(), itinerary.getTitle(),
					itinerary.getDesc(), itinerary.getType(), itinerary.getIdx(), itinerary.getCreateTime(),
					equipmentItinerary.getImageUrls() == null ? null : Util.toJson(equipmentItinerary.getImageUrls()) };
			break;
		default:
			LOGGER.error("Illegal type: {}, [activityId = ?, title = ?, desc = ?, idx = ?]", new Object[] { type,
					itinerary.getActivityId(), itinerary.getTitle(), itinerary.getDesc(), itinerary.getIdx() });
			return;
		}
		getJdbcTemplate().update(sql, args);
	}

	@Override
	public int deleteByActivityId(String activityId) {
		String sql = "delete from APLAN_ITINERARY where ACTIVITY_ID = ?";
		return getJdbcTemplate().update(sql, activityId);
	}

	private class RowMappperImpl implements RowMapper<Itinerary> {

		@Override
		public Itinerary mapRow(ResultSet rs, int rowNum) throws SQLException {
			Itinerary itinerary;
			String imageUrls;
			int type = rs.getInt("TYPE_");
			switch (type) {
			case ActivityConstants.ITINERARY_TYPE_GEO:
				GeoItinerary geoItinerary = new GeoItinerary();
				geoItinerary.setBeginTime(rs.getLong("BEGIN_TIME"));
				geoItinerary.setEndTime(rs.getLong("END_TIME"));
				geoItinerary.setDestGeoX(rs.getString("DEST_GEO_X"));
				geoItinerary.setDestGeoY(rs.getString("DESC_GEO_Y"));
				geoItinerary.setDestLocation(rs.getString("DEST_LOCATION"));
				imageUrls = rs.getString("IMAGE_URLS");
				if (imageUrls != null) {
					geoItinerary.setImageUrls(Util.jsonToList(imageUrls, String.class));
				}
				itinerary = geoItinerary;
				break;
			case ActivityConstants.ITINERARY_TYPE_ROUTE:
				RouteItinerary routeItinerary = new RouteItinerary();
				routeItinerary.setBeginTime(rs.getLong("BEGIN_TIME"));
				routeItinerary.setEndTime(rs.getLong("END_TIME"));
				routeItinerary.setOriginalGeoX(rs.getString("ORIGINAL_GEO_X"));
				routeItinerary.setOriginalGeoY(rs.getString("ORIGINAL_GEO_Y"));
				routeItinerary.setOriginalLocation(rs.getString("ORIGINAL_LOCATION"));
				routeItinerary.setDestGeoX(rs.getString("DEST_GEO_X"));
				routeItinerary.setDestGeoY(rs.getString("DESC_GEO_Y"));
				routeItinerary.setDestLocation(rs.getString("DEST_LOCATION"));
				imageUrls = rs.getString("IMAGE_URLS");
				if (imageUrls != null) {
					routeItinerary.setImageUrls(Util.jsonToList(imageUrls, String.class));
				}
				itinerary = routeItinerary;
				break;
			case ActivityConstants.ITINERARY_TYPE_EQUIPMENT:
				EquipmentItinerary equipmentItinerary = new EquipmentItinerary();
				imageUrls = rs.getString("IMAGE_URLS");
				if (imageUrls != null) {
					equipmentItinerary.setImageUrls(Util.jsonToList(imageUrls, String.class));
				}
				itinerary = equipmentItinerary;
				break;
			case ActivityConstants.ITINERARY_TYPE_DAY:
				DayItinerary dayItinerary = new DayItinerary();
				dayItinerary.setBeginTime(rs.getLong("BEGIN_TIME"));
				dayItinerary.setEndTime(rs.getLong("END_TIME"));
				itinerary = dayItinerary;
				break;
			default:
				LOGGER.error("Illegal type value: {}, [activityId = {}, id = {}]",
						new Object[] { type, rs.getString("ACTIVITY_ID"), rs.getString("ID") });
				geoItinerary = new GeoItinerary();
				geoItinerary.setBeginTime(rs.getLong("BEGIN_TIME"));
				geoItinerary.setEndTime(rs.getLong("END_TIME"));
				geoItinerary.setDestGeoX(rs.getString("DEST_GEO_X"));
				geoItinerary.setDestGeoY(rs.getString("DESC_GEO_Y"));
				geoItinerary.setDestLocation(rs.getString("DEST_LOCATION"));
				imageUrls = rs.getString("IMAGE_URLS");
				if (imageUrls != null) {
					geoItinerary.setImageUrls(Util.jsonToList(imageUrls, String.class));
				}
				itinerary = geoItinerary;
				break;
			}
			itinerary.setId(rs.getString("ID_"));
			itinerary.setActivityId(rs.getString("ACTIVITY_ID"));
			itinerary.setTitle(rs.getString("TITLE"));
			itinerary.setDesc(rs.getString("DESC_"));
			itinerary.setIdx(rs.getInt("IDX"));
			itinerary.setCreateTime(rs.getLong("CREATE_TIME"));
			return itinerary;
		}

	}

}
