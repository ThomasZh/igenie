package com.oct.ga.comm.domain.template;

import java.util.List;

public class GaTemplateBase {

	private String templateId; // ģ��ID
	private String templateName;
	private String templateIcon;// icon: check
	private String templateDesc;// ģ������
	private String templateNote;// ģ�汸ע
	private List<String> templateImages;// ģ����ʾͼƬ��
	private int templateType;// task,checklist,checklistclwc,input
	private int templateStatue;// ����״̬: private,public
	private int createTime;// ģ�洴��ʱ��
	private int lastUpdateTime;// ģ���޸�ʱ��
	private String accountId;// ģ������
	private String accountName;// ģ������
	private String templatePid;// ģ�游�ο�ģ��ID
	private List<String> tags;// TAG
	private int startTime;// ������������ʱ��
	private String startTimeAlertRule;// ��������
	private int endTime;// �������ʱ��
	private String endTimeAlertRule;// ��������
	private String repeatTimeRule;// �����ظ�����:h,d,w,m

	// �����ǰ������
	private List<String> proTaskId; // �����ǰ������
	private List<String> proTaskRelation;// ������ǰ������Ĺ�ϵ
	private List<String> proTaskDelayTime;// 
	// ss2h, se-+2d, es4d, ee

	// GIS
	private String location;
	private boolean isOnLocationAlertType;// �ص㴥�����ѿ���
	private int locationAlertType;// ��������: in,out
	private boolean isSendLocationToGroup;// ���Ŷӷ���GIS��Ϣ
	private boolean isNeedGroupLocation; // Ҫ���Ŷӷ���GIS��Ϣ
	private int sendLocationRepeatTimes; // ����GIS��Ϣ��Ƶ��

	// ģ��Ĭ��ACL
	private int permissionMode;// ģ��Ĭ��ACL: NormalTask:774;Trip:770
	private int filePermissionMode;// ����Ĭ��ACL:NormalTask:760;Trip:760
	private boolean feedbackInvite;// �Ƿ�������״̬
	private boolean feedbackModifyTask;// �Ƿ�����������������״̬�仯
	private boolean isCompletedByCreater;// ������ɶ��壺��������������
	private boolean splitForEachMember;// ��������ʱ���ղμ��߲��ɶ�����Ŀ

	// attachment
	private List<String> filenameList;// �������
	private int fileLimitedTime;// ������Чʱ���޶�

	// toolsbar
	private List<String> toolName;// �������
	private List<String> toolImage;// ����icon
	private List<String> toolAction;// ��Ŀ�İ�ť

	// TemplateType: checklist
	private int selectedType;// ѡ������:multi check,radio,none
	private List<String> checkList;// ����ѡ����嵥
	private boolean isDisplayStepper; // ��������ѡ����,amount
	private boolean isDiaplayUnit;// ��λ�����
	private String unit;// ��λ�����
	private boolean isDisplayUnitInput; // ��λ�����
	private boolean isDisplayQut;// ��λ�ɱ������
	private boolean isDisplaySubTotal;// ��ʾ��Ŀ����
	private boolean isDisplayTotal;// ��ʾ�ܵĻ���
	private boolean isDisplaySubmitButton;// mailto://url
	private boolean submitUrl;

	private String backgroupImage;// ��������
	private String backgroupMusic;// ��������
}