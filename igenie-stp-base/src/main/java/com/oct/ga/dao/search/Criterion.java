package com.oct.ga.dao.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ͨ��searchField��searchString��searchOper����������Criterion�ķ�����
 * ��ǰ��ѧϰHibernate��ʱ������ͽ��ܹ�ʹ��Criterion�ķ�ʽ��
 * �е����Ѷ�Hibernate��������������һֱ��Hibernate�����Ȳ�̫���⣻ �������ֽ���ѯ�������������˼·����ֵ�ý��ġ�
 * ��ˣ�����Ȼ��̨ʹ�õ���Spring��JdbcTemplate����Ϊ�˳���Dao�ڵĲ����� ���Լ�д��һ�� Criterion�ࣨ��������ں��棩��
 * ʹ�����Criterion�б����򻯲�ѯ�����ڸ�����Ĵ��ݡ�
 * 
 * �Ұ�Criterion�ֳ�4�ࣺEqualCriterion��CompareCriterion��LikeCriterion��
 * NotLikeCriterion�������廮�ַ�ʽȫΪʹ�÷��㣩��
 *  ���⻹��������̬������convertToSql��getCriteriaValues��
 * ������Criterion�б�ת��ΪJdbcTemplate��Ҫ��SQL�Ӿ�Ͳ����б?
 * 
 * @author thomas
 */
public class Criterion
{
	public static enum CriterionType {
		EQUAL, LIKE, COMPARE, NOT_LIKE
	}

	public static enum CompareType {
		GT, GTE, LT, LTE, EQ, NE
	}

	private CriterionType criterionType;
	private String tableName;
	private String field;
	private Object value;

	// ��Criteriaת��ΪSQL�������
	public static String convertToSql(List<Criterion> criteria)
	{
		String criteriaString = "";
		StringBuilder sb = new StringBuilder();
		for (Criterion criterion : criteria) {
			String prefix = criterion.getFieldPrefix();
			switch (criterion.getCriterionType()) {
			case EQUAL:
				sb.append(prefix + criterion.getField() + "=? and ");
				break;
			case LIKE:
				sb.append(prefix + criterion.getField() + " like ? and ");
				break;

			case NOT_LIKE:
				sb.append(prefix + criterion.getField() + " not like ? and ");
				break;
			case COMPARE:
				CompareType compareType = ((CompareCriterion) criterion)
						.getCompareType();
				switch (compareType) {
				case EQ:
					sb.append(prefix + criterion.getField() + "=? and ");
					break;
				case NE:
					sb.append(prefix + criterion.getField() + "<>? and ");
					break;
				case GT:
					sb.append(prefix + criterion.getField() + ">? and ");
					break;
				case GTE:
					sb.append(prefix + criterion.getField() + ">=? and ");
					break;
				case LT:
					sb.append(prefix + criterion.getField() + "<? and ");
					break;
				case LTE:
					sb.append(prefix + criterion.getField() + "<=? and ");
					break;
				}
				break;
			}
		}
		int i = -1;
		if ((i = sb.lastIndexOf(" and ")) != -1) {
			criteriaString = sb.substring(0, i);
		}
		return criteriaString;
	}

	// ��Criteria��������ֵת��ΪList<Object>
	public static List<Object> getCriteriaValues(List<Criterion> criteria)
	{
		List<Object> criteriaValues = criteria.isEmpty() ? Collections
				.emptyList() : new ArrayList<Object>();
		for (Criterion criterion : criteria) {
			criteriaValues.add(criterion.getValue());
		}
		return criteriaValues;
	}

	public CriterionType getCriterionType()
	{
		return criterionType;
	}

	public void setCriterionType(CriterionType criterionType)
	{
		this.criterionType = criterionType;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public static Criterion getCompareCriterion(CompareType compareType,
			String field, Object value, String tableName)
	{
		CompareCriterion compareCriterion = new CompareCriterion();
		compareCriterion.setCriterionType(CriterionType.COMPARE);
		compareCriterion.setCompareType(compareType);
		compareCriterion.setField(field);
		compareCriterion.setValue(value);
		compareCriterion.setTableName(tableName);
		return compareCriterion;
	}

	public static Criterion getLikeCriterion(String field, Object value,
			String tableName)
	{
		LikeCriterion likeCriterion = new LikeCriterion();
		likeCriterion.setCriterionType(CriterionType.LIKE);
		likeCriterion.setField(field);
		likeCriterion.setValue(value);
		likeCriterion.setTableName(tableName);
		return likeCriterion;
	}

	public static Criterion getNotLikeCriterion(String field, Object value,
			String tableName)
	{
		NotLikeCriterion notLikeCriterion = new NotLikeCriterion();
		notLikeCriterion.setCriterionType(CriterionType.NOT_LIKE);
		notLikeCriterion.setField(field);
		notLikeCriterion.setValue(value);
		notLikeCriterion.setTableName(tableName);
		return notLikeCriterion;
	}

	public static Criterion getEqualCriterion(String field, Object value,
			String tableName)
	{
		EqualCriterion equalCriterion = new EqualCriterion();
		equalCriterion.setCriterionType(CriterionType.EQUAL);
		equalCriterion.setField(field);
		equalCriterion.setValue(value);
		equalCriterion.setTableName(tableName);
		return equalCriterion;
	}

	public static class LikeCriterion
			extends Criterion
	{
	}

	public static class NotLikeCriterion
			extends Criterion
	{
	}

	public static class EqualCriterion
			extends Criterion
	{
	}

	public static class CompareCriterion
			extends Criterion
	{
		private CompareType compareType;

		public CompareType getCompareType()
		{
			return compareType;
		}

		public void setCompareType(CompareType compareType)
		{
			this.compareType = compareType;
		}
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getFieldPrefix()
	{
		return (tableName == null || tableName.length() == 0) ? "" : tableName
				+ ".";
	}
}
