package com.oct.ga.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class JQPaginationHelper<E>
{
	public JQGridPTO<E> fetchPage(final JdbcTemplate jt,
			final String sqlCountRows, final String sqlFetchRows,
			final Object args[], final int pageNo, final int pageSize,
			final ParameterizedRowMapper<E> rowMapper)
	{
		// determine how many rows are available
		final int rowCount = jt.queryForInt(sqlCountRows, args);

		// calculate the number of pages
		int pageCount = rowCount / pageSize;
		if (rowCount > pageSize * pageCount) {
			pageCount++;
		}

		// create the page object
		final JQGridPTO<E> pto = new JQGridPTO<E>();
		pto.setPage(pageNo);
		pto.setTotal(pageCount);
		pto.setRecords(rowCount);

		// fetch a single page of results
		final int startRow = (pageNo - 1) * pageSize;
		jt.query(sqlFetchRows, args, new ResultSetExtractor()
		{
			@Override
			public Object extractData(java.sql.ResultSet rs)
					throws SQLException, DataAccessException
			{
				final List pageItems = pto.getRows();
				int currentRow = 0;
				while (rs.next() && currentRow < startRow + pageSize) {
					if (currentRow >= startRow) {
						pageItems.add(rowMapper.mapRow(rs, currentRow));
					}
					currentRow++;
				}
				return pto;
			}
		});
		return pto;
	}
}
