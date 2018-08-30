package kingwant.hjjp.entity;

import java.util.List;
import java.util.Map;

/**
 * @author 黄河
 *
 * @date: 2018年5月18日 上午8:46:22 
 */
public class SubmitTask {
/**
 * 提交人
 */
private	String userName; 
/**
 * 当前节点
 */
private String taskId; 
/**
 * 下一个节点定义id
 */
private String nextactivitiId;
/**
 * 通过还是驳回
 */
private String floag;
/**
 * 分配任务类型
 */
private	String type; 
/**
 * 下一个任务分配用户
 */
private	List<String> users;

/**
 * 数据表名
 */
private String formCode;

/**
 * 数据表记录id
 */
private String formId;
/**
 * 提交或者驳回的数据
 */
private Map<String,String> formData;


private  String pricessInId;


private  int version;

/**
 * 审批意见
 */
private  String  operateOpinions;


/**
 * 当前任务名
 */
private  String  taskName;

/**
 * 是否是抢占式
 */
private  String isPreemptive;



public String getIsPreemptive() {
	return isPreemptive;
}
public void setIsPreemptive(String isPreemptive) {
	this.isPreemptive = isPreemptive;
}
public int getVersion() {
	return version;
}
public void setVersion(int version) {
	this.version = version;
}
public String getPricessInId() {
	return pricessInId;
}
public void setPricessInId(String pricessInId) {
	this.pricessInId = pricessInId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getTaskId() {
	return taskId;
}
public void setTaskId(String taskId) {
	this.taskId = taskId;
}
public String getNextactivitiId() {
	return nextactivitiId;
}
public void setNextactivitiId(String nextactivitiId) {
	this.nextactivitiId = nextactivitiId;
}
public String getFloag() {
	return floag;
}
public void setFloag(String floag) {
	this.floag = floag;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public List<String> getUsers() {
	return users;
}
public void setUsers(List<String> users) {
	this.users = users;
}
public Map<String, String> getFormData() {
	return formData;
}
public void setFormData(Map<String, String> formData) {
	this.formData = formData;
}
public String getFormCode() {
	return formCode;
}
public void setFormCode(String formCode) {
	this.formCode = formCode;
}
public String getFormId() {
	return formId;
}
public void setFormId(String formId) {
	this.formId = formId;
}
public String getOperateOpinions() {
	return operateOpinions;
}
public void setOperateOpinions(String operateOpinions) {
	this.operateOpinions = operateOpinions;
}
public String getTaskName() {
	return taskName;
}
public void setTaskName(String taskName) {
	this.taskName = taskName;
}



}
