package com.oct.ga.dao.search;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.oct.ga.dao.search.Criterion.CompareType;

public class CriterionUtil
{
	public List<Criterion> generateSearchCriteriaFromFilters(String filters)
	{
		List<Criterion> criteria = new ArrayList<Criterion>();

		JSONObject jsonObject = JSONObject.fromObject(filters);

		JSONArray rules = jsonObject.getJSONArray("rules");

		for (Object obj : rules) {
			JSONObject rule = (JSONObject) obj;

			String field = rule.getString("field");
			String op = rule.getString("op");
			String data = rule.getString("data");

			Criterion criterion = this.generateSearchCriterion(field, data, op);

			if (criterion != null) {
				criteria.add(criterion);
			}
		}

		return criteria;
	}

	// ͨ��searchField��searchString��searchOper����������Criterion�ķ���
	public Criterion generateSearchCriterion(String searchField, String searchString, String searchOper)
	{
		Criterion criterion = null;

		// ���searchField��searchString��searchOper��Ϊnull����searchString��Ϊ���ַ�ʱ���򴴽�Criterion
		if (searchField != null && searchString != null && searchString.length() > 0 && searchOper != null) {
			if ("eq".equals(searchOper)) {
				criterion = Criterion.getEqualCriterion(searchField, searchString, null);
			} else if ("ne".equals(searchOper)) {
				criterion = Criterion.getCompareCriterion(CompareType.NE, searchField, searchString, null);
			} else if ("lt".equals(searchOper)) {
				criterion = Criterion.getCompareCriterion(CompareType.LT, searchField, searchString, null);
			} else if ("le".equals(searchOper)) {
				criterion = Criterion.getCompareCriterion(CompareType.LTE, searchField, searchString, null);
			} else if ("gt".equals(searchOper)) {
				criterion = Criterion.getCompareCriterion(CompareType.GT, searchField, searchString, null);
			} else if ("ge".equals(searchOper)) {
				criterion = Criterion.getCompareCriterion(CompareType.GTE, searchField, searchString, null);
			} else if ("bw".equals(searchOper)) {
				criterion = Criterion.getLikeCriterion(searchField, searchString + "%", null);
			} else if ("bn".equals(searchOper)) {
				criterion = Criterion.getNotLikeCriterion(searchField, searchString + "%", null);
			} else if ("ew".equals(searchOper)) {
				criterion = Criterion.getLikeCriterion(searchField, "%" + searchString, null);
			} else if ("en".equals(searchOper)) {
				criterion = Criterion.getNotLikeCriterion(searchField, "%" + searchString, null);
			} else if ("cn".equals(searchOper)) {
				criterion = Criterion.getLikeCriterion(searchField, "%" + searchString + "%", null);
			} else if ("nc".equals(searchOper)) {
				criterion = Criterion.getNotLikeCriterion(searchField, "%" + searchString + "%", null);
			}
		}
		return criterion;
	}
}
