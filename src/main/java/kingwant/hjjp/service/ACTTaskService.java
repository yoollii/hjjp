package kingwant.hjjp.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;

import kingwant.hjjp.entity.SubmitTask;
import xyz.michaelch.mchtools.MCHException;

public interface ACTTaskService {

	/**
	 * @Title: findNextUserTask
	 * @Description: 查看当前流程将会执行到的下一用户任务信息
	 * @param UserTaksId
	 *            当前正在执行的任务Id
	 * @return TaskDefinition
	 */
	TaskDefinition findNextUserTask(String UserTaksId) throws Exception;

	/**
	 * @Title: nextTaskDefinition
	 * @Description: 获取将会执行到的下一个用户任务节点
	 * @param activityImpl
	 *            流程节点信息
	 * @param activityId
	 *            当前流程节点Id信息
	 * @param elString
	 *            排他网关顺序流线段判断条件
	 * @param processInstanceId
	 *            流程实例Id信息
	 * @return TaskDefinition
	 */
	TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString,
			String processInstanceId);

	/**
	 * @Title: findNextTaskDefinition
	 * @Description: 下一个节点将会执行到的全部可能
	 * @param UserTaksId
	 *            当前执行节点id
	 * @return
	 * @throws Exception
	 *             List<TaskDefinition>
	 */
	List<TaskDefinition> findNextTaskDefinition(String UserTaksId) throws Exception;

	/**
	 * @Title: findPersonalTaskAssignees
	 * @Description: 查询该流程下个用户节点全部代办人，如果存在处理人则只返回处理人name
	 * @param TaskId
	 *            候选任务节点定义id
	 * @param processInstanceId
	 *            流程实例id
	 * @return
	 * @throws Exception
	 *             List<String>
	 */
	List<String> findPersonalTaskAssignees(String processInstanceId, String TaskDefinitionId) throws Exception;

	// 分配任务到个人（username）
	// processEngine.getTaskService().claim(taskId, userId);
	/**
	 * @Title: assignationTaskUser
	 * @Description: 将流程的下一节点分配给指定用户（由上一节点执行者手动让下个人签收任务）
	 * @param processInstanceId
	 * @param taskId
	 *            当前执行taskid
	 * @param userName
	 *            void
	 * @throws Exception
	 */
	void assignationTaskUser(String processInstanceId, String taskId, String userName) throws Exception;

	/**
	 * @Title: simpleAssignationTaskUser
	 * @Description: 简单的任务分配。将正在执行的任务分配给某用户 用于先提交任务后确定好下一步执行者
	 * @param processInstanceId
	 * @param userName
	 * @throws Exception
	 *             void
	 */
	void simpleAssignationTaskUser(String processInstanceId, String userName) throws Exception;
	// 由上一节点手动让下一节点签收任务（分配下一任务给指定用户）
	// void

	/**
	 * @Title: isCondition
	 * @Description: 根据key和value判断el表达式是否通过信息
	 * @param String
	 *            key el表达式key信息
	 * @param String
	 *            el el表达式信息
	 * @param String
	 *            value el表达式传入值信息
	 * @return boolean
	 */
	boolean isCondition(String key, String el, String value);

	// boolean isCondition(String key, String el);
	/**
	 * @Title: getGatewayCondition
	 * @Description: 查询流程启动时设置排他网关判断条件信息（当流程线设置的变量值为排他网管的ID时可用）
	 * @param gatewayId
	 *            排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
	 * @param processInstanceId
	 *            流程实例Id信息
	 * @return String
	 */
	String getGatewayCondition(String gatewayId, String processInstanceId);

	/**
	 * @Title: getActivity
	 * @Description: 根据TaskId获取流程定义环节对象
	 * @param taskId
	 * @return
	 * @throws Exception
	 *             ActivityImpl
	 */
	ActivityImpl getActivity(String taskId) throws Exception;
	// void setNextUserTaskAssignee();

	/**
	 * @Title: isSigned
	 * @Description: 判断当前节点是否是会签节点
	 * @param actImpl
	 * @return boolean
	 */
	public boolean isSigned(ActivityImpl actImpl);

	/**
	 * @Title: isCountersigned
	 * @Description: 会签完成判断
	 * @param execution
	 *            流程活动执行流
	 * @return boolean
	 */
	public boolean isCountersigned(ActivityExecution execution);

	/**
	 * @Title: complete
	 * @Description: 完成任务
	 * @param taskId
	 *            任务id
	 * @param users
	 *            下一个用户任务的受理人 void
	 */
	/**
	 * @Title: complete
	 * @Description: 完成任务
	 * @param taskId
	 *            任务id
	 * @param users
	 *            下一个用户任务的受理人
	 * @param taskKey
	 *            下一个用户任务定义id void
	 */
	public void complete(String taskId, String taskKey, List<String> users);

	/**
	 * @Title: findActivitiImpl
	 * @Description: 根据任务ID和节点ID获取活动节点
	 * @param taskId
	 *            任务id
	 * @param activitiId
	 *            流程节点id
	 * @return ActivityImpl
	 * @throws MCHException
	 */
	public ActivityImpl findActivitiImpl(String taskId, String activitiId) throws MCHException;

	/**
	 * @Title: findTaskById
	 * @Description: 根据任务ID获得任务实例
	 * @param taskId
	 * @return
	 * @throws MCHException
	 *             TaskEntity
	 */
	public TaskEntity findTaskById(String taskId) throws MCHException;

	// public void rejectTask(String taskId,String endActivityId);

	// public ActivityImpl findRwjectActivity(String taskId,);
	/**
	 * @Title: findBackUserTask
	 * @Description: 递归查找可用的上一个节点
	 * @param activityImpl
	 *            当前节点
	 * @param nowActivityId
	 * @param tasks
	 *            void
	 */
	void findBackUserTask(ActivityImpl activityImpl, String nowActivityId, List<String> tasks);

	/**
	 * @Title: turnTask
	 * @Description: 驳回上一个节点
	 * @param taskId
	 *            当前节点
	 * @throws Exception
	 *             void
	 */
	public void turnTask(String taskId) throws Exception;

	/**
	 * @Title: submitTask
	 * @Description: 提交用户任务流程// 数据处理放在??
	 * @param submit
	 *            提交任务对象
	 * @throws Exception
	 *             void
	 */
	public void submitTask(SubmitTask submit, HttpServletRequest request) throws Exception;

	/**
	 * @Title: allocationTask
	 * @Description: 流程分配
	 * @param processInstanceId
	 * @param userIds
	 * @throws MCHException
	 *             void
	 */
	public void allocationTask(String processInstanceId, List<String> userIds) throws MCHException;

	/**
	 * @Title: findRwjectActivity
	 * @Description: 查询该驳回的节点
	 * @param taskId
	 * @return HistoricActivityInstance
	 * @throws Exception
	 */
	public HistoricTaskInstance findRwjectActivity(String taskId, Task task) throws Exception;

	/**
	 * @Title: clearTransition
	 * @Description: 清空指定活动节点流向
	 * @param activityImpl
	 * @return List<PvmTransition>
	 */
	List<PvmTransition> clearTransition(ActivityImpl activityImpl);

	/**
	 * @Title: restoreTransition
	 * @Description: 还原指定活动节点流向
	 * @param activityImpl
	 * @param oriPvmTransitionList
	 *            void
	 */
	void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList);

	/**
	 * @Title: turnTransition
	 * @Description: 流程跳转到
	 * @param taskId
	 * @param activityId
	 * @param variables
	 * @throws Exception
	 *             void
	 */
	void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception;

	/**
	 * @Title: findNextUserTasks
	 * @Description: 下一步用户任务集合
	 * @param UserTaksId
	 * @return List<TaskDefinition>
	 * @throws Exception
	 */
	List<String> findNextUserTasks(String UserTaksId, String formCode, String formId) throws Exception;

	/**
	 * @Title: findNextUserTasks
	 * @Description: 下一步用户任务递归查询
	 *               <font color="red">条件路线判定规则，指定表单数据字段的英文名和起数值，以方式“xingming=张三
	 *               ”的形式填写</font>
	 * @param activityImpl
	 * @param nowActivityId
	 * @param Nexttasks
	 *            void
	 */
	void findNextUserTaskss(ActivityImpl activityImpl, String nowActivityId, List<String> Nexttasks, String formCode,
			String formId);

	/**
	 * @Title: AssignmentTaskToUser
	 * @Description: 设置下一任务的操作者和共享task操作责
	 * @param processInstanceId
	 * @param userIds
	 * @throws MCHException
	 *             void
	 */
	public void AssignmentTaskToUser(String processInstanceId, List<String> userIds) throws MCHException;

	/**
	 * @Title: findBackUserTasks
	 * @Description: 获取当前任务的前面任务节点id集合
	 * @param UserTaksId
	 * @return List<String>
	 */
	List<String> findBackUserTasks(String UserTaksId);

	/**
	 * @Title: findMyTaskId
	 * @Description: 找出该流程这个用户处理过的最后一个任务
	 * @param processInId
	 * @param userName
	 * @return String
	 */
	String findMyTaskId(String processInId, String userName);

	/**
	 * @Title: BackNextUserTasks
	 * @Description: 驳回清除后续节点
	 * @param UserTaksId
	 * @param formCode
	 * @param formId
	 * @return
	 * @throws Exception
	 *             List<String>
	 */
	public List<String> BackNextUserTasks(String UserTaksId, String formCode, String formId) throws Exception;

	/**
	 * @Title: BackNextUserTaskss
	 * @Description: 驳回后续节点递归获取
	 * @param activityImpl
	 * @param nowActivityId
	 * @param Nexttasks
	 * @param formCode
	 * @param formId
	 *            void
	 */
	public void BackNextUserTaskss(ActivityImpl activityImpl, String nowActivityId, List<String> Nexttasks, int floag,
			String actId);

}
