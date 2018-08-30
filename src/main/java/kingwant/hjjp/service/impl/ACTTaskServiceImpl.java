package kingwant.hjjp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kingwant.hjjp.entity.SubmitTask;
import kingwant.hjjp.service.ACTTaskService;
import kingwant.hjjp.util.KwHelper;
import xyz.michaelch.mchtools.MCHException;

@Service
@Transactional(rollbackFor = { xyz.michaelch.mchtools.MCHException.class, Exception.class })
public class ACTTaskServiceImpl implements ACTTaskService {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	@Autowired
	RepositoryService repositoryService;

	// @Autowired
	// FormToFieldService formToFieldService;

	protected static final Logger logger = LoggerFactory.getLogger(ACTTaskServiceImpl.class);

	@Override
	public List<String> findNextUserTasks(String UserTaksId, String formCode, String formId) throws Exception {
		// 获取不到返回空
		if (KwHelper.isNullOrEmpty(UserTaksId)) {
			return null;
		}
		List<String> NextUserTasks = new ArrayList<String>();
		Task task = taskService.createTaskQuery().taskId(UserTaksId).singleResult();
		// 流程定义实例
		ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		// 全部节点定义
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) definition).findActivity(task.getTaskDefinitionKey());
		// ((ProcessDefinitionImpl)
		// definition).findActivity(task.getTaskDefinitionKey());
		// 可能的用户节点s
		findNextUserTaskss(activityImpl, task.getTaskDefinitionKey(), NextUserTasks, formCode, formId);
		return NextUserTasks;
	}

	@Override
	public TaskDefinition findNextUserTask(String UserTaksId) throws Exception {

		// 流程定义实例
		ProcessDefinitionEntity processDefinitionEntity = null;
		String id = null;
		// 任务定义实例
		TaskDefinition task = null;

		// 获取流程实例Id信息
		String processInstanceId = taskService.createTaskQuery().taskId(UserTaksId).singleResult()
				.getProcessInstanceId();
		// 获取流程发布Id信息
		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult().getProcessDefinitionId();
		// 获取流程定义实例
		processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(definitionId);
		// 流程执行实例
		ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 当前流程节点Id信息
		String activitiId = execution.getActivityId();
		// 获取流程所有节点信息
		List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
		// 遍历所有节点信息
		for (ActivityImpl activityImpl : activitiList) {
			id = activityImpl.getId();
			if (activitiId.equals(id)) {
				// 获取下一个节点信息
				task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
				break;
			}
		}
		return task;
	}

	@Override
	public List<String> findPersonalTaskAssignees(String processInstanceId, String TaskDefinitionId) throws Exception {

		List<String> users = new ArrayList<String>();
		// 获取流程发布Id信息
		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult().getProcessDefinitionId();
		// 获取流程定义实例
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(definitionId);
		// task 定义集合
		Map<String, TaskDefinition> map = new HashMap<String, TaskDefinition>();
		map = processDefinitionEntity.getTaskDefinitions();
		TaskDefinition task = null;
		if (map.get(TaskDefinitionId) != null) {
			task = map.get(TaskDefinitionId);
			if (task.getAssigneeExpression() != null
					&& !KwHelper.isNullOrEmpty(task.getAssigneeExpression().getExpressionText())) {
				users.add(task.getAssigneeExpression().getExpressionText());
			}
			if (!task.getCandidateUserIdExpressions().isEmpty()) {
				Set<Expression> userSet = task.getCandidateUserIdExpressions();

				for (Expression expression : userSet) {
					users.add(expression.getExpressionText());
				}
			}

			if (!task.getCandidateGroupIdExpressions().isEmpty()) {
				Set<Expression> delSet = task.getCandidateGroupIdExpressions();

				for (Expression expression : delSet) {
					users.add(expression.getExpressionText());
				}
			}

		}
		return users;
	}

	@Override
	public void assignationTaskUser(String processInstanceId, String taskId, String userName) throws Exception {
		try {
			TaskDefinition taskDefinition = findNextUserTask(taskId);
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
					.taskDefinitionKey(taskDefinition.getKey()).singleResult();
			taskService.claim(task.getId(), userName);
		} catch (Exception e) {
			throw new MCHException(e);
		}

	}

	@Override
	public boolean isCondition(String key, String el, String value) {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		context.setVariable(key, factory.createValueExpression(value, String.class));
		ValueExpression e = factory.createValueExpression(context, el, boolean.class);
		return (Boolean) e.getValue(context);
	}

	@Override
	public String getGatewayCondition(String gatewayId, String processInstanceId) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
		Object object = runtimeService.getVariable(execution.getId(), gatewayId);
		return object == null ? "" : object.toString();

	}

	@Override
	public TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString,
			String processInstanceId) {
		// 任务实例接口
		PvmActivity ac = null;
		//
		Object s = null;
		// 如果遍历节点为用户任务并且节点不是当前节点信息
		if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
			// 获取该节点下一个节点信息
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
					.getTaskDefinition();
			return taskDefinition;
		} else if ("exclusiveGateway".equals(activityImpl.getProperty("type"))) {
			// 获取节点所有流向线路信息
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			// List<PvmTransition> outTransitionsTemp = null;
			elString = getGatewayCondition(activityImpl.getId(), processInstanceId);

			if (outTransitions.size() == 1) {
				return nextTaskDefinition((ActivityImpl) outTransitions.get(0).getDestination(), activityId, elString,
						processInstanceId);
			} else if (outTransitions.size() > 1) {
				for (PvmTransition tr1 : outTransitions) {
					s = tr1.getProperty("conditionText");// 获取排他网关线路判断条件信息
					// 判断el表达式是否成立
					if (isCondition(activityImpl.getId(), s.toString().trim(), elString)) {
						return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
								processInstanceId);
					}
				}
			}

		} else {
			// 获取节点所有流向线路信息
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			List<PvmTransition> outTransitionsTemp = null;
			for (PvmTransition tr : outTransitions) {
				ac = tr.getDestination(); // 获取线路的终点节点
				// 如果流向线路为排他网关
				if ("exclusiveGateway".equals(ac.getProperty("type"))) {
					outTransitionsTemp = ac.getOutgoingTransitions();

					// 如果网关路线判断条件为空信息
					if (KwHelper.isNullOrEmpty(elString)) {
						// 获取流程启动时设置的网关判断条件信息
						elString = getGatewayCondition(ac.getId(), processInstanceId);
					}

					// 如果排他网关只有一条线路信息
					if (outTransitionsTemp.size() == 1) {
						return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
								elString, processInstanceId);
					} else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
						for (PvmTransition tr1 : outTransitionsTemp) {
							s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
							// 判断el表达式是否成立
							if (isCondition(ac.getId(), s.toString().trim(), elString)) {
								return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
										processInstanceId);
							}
						}
					}
				} else if ("userTask".equals(ac.getProperty("type"))) {
					return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
				} else {
				}
			}
			return null;
		}
		return null;
	}

	@Override
	public void simpleAssignationTaskUser(String processInstanceId, String userName) throws Exception {
		try {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();

			// 设置下一步执行者
			String taskId = processInstance.getActivityId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskId)
					.singleResult();
			runtimeService.setVariable(processInstance.getProcessInstanceId(), "startTask",
					task.getTaskDefinitionKey());
			taskService.claim(task.getId(), userName);
		} catch (Exception e) {
			throw new MCHException(e.getMessage());
		}

	}

	@Override
	public List<TaskDefinition> findNextTaskDefinition(String UserTaksId) throws Exception {
		// 任务定义实例
		List<TaskDefinition> tasks = new ArrayList<TaskDefinition>();
		/////////////////////////////////////////////////////////////////////
		try {

			// 当前正在执行的任务
			ActivityImpl activityImpl = this.getActivity(UserTaksId);

			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();// 获取从当前节点出来的所有线路

			// 遍历全部路线找到所有可能的下一个用户任务节点
			for (PvmTransition tr : outTransitions) {
				// PvmActivity nextActivity =tr.getDestination();
				// 判断是否是UserTask
				if ("userTask".equals(tr.getProperty("type"))) {
					tasks.add(
							((UserTaskActivityBehavior) ((ActivityImpl) tr).getActivityBehavior()).getTaskDefinition());
				} else if ("exclusiveGateway".equals(tr.getProperty("type"))) {
					// tr.getId()
					List<TaskDefinition> taskss = new ArrayList<TaskDefinition>();
					taskss = findNextTaskDefinition(tr.getId());
					tasks.addAll(taskss);
				}

			}
		} catch (Exception e) {
			throw new MCHException();
		}
		return tasks;
	}

	@Override
	public ActivityImpl getActivity(String taskId) throws Exception {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task != null) {
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(task.getProcessDefinitionId());
			return def.findActivity(task.getTaskDefinitionKey());
		}
		return null;
	}

	@Override
	public boolean isSigned(ActivityImpl actImpl) {
		// actImpl.set
		return false;
	}

	@Override
	public boolean isCountersigned(ActivityExecution execution) {
		// 已经完成次数
		Integer completeCounter = (Integer) execution.getVariable("nrOfCompletedInstances");
		// 总次数
		Integer instanceOfNumbers = (Integer) execution.getVariable("nrOfInstances");

		// 判断是否可以结束会签
		if ((instanceOfNumbers - completeCounter) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void complete(String taskId, String taskKey, List<String> users) {
		// 要被完成的任务
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// task.getAssignee()
		String instansId = task.getProcessInstanceId();

		// 完成任务
		// taskService.complete(taskId, asgMap);
		// 完成任务后
		// 获取当前实例的task
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instansId)
				.singleResult();

		///////////////////////////////////////////////////
		Map<String, Object> asgMap = new HashMap<String, Object>();
		if (users != null && !users.isEmpty()) {
			asgMap.put("Assignee", users);
		}

		if (!KwHelper.isNullOrEmpty(taskKey)) {
			taskService.complete(taskId, asgMap);
		} else {

		}

	}

	@Override
	public void turnTask(String taskId) throws Exception {
		// 当前节点
		// 将当前任务同等级任务全部完成或者挂起
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		// 被驳回的流程task实例
		HistoricTaskInstance taskInstance = findRwjectActivity(taskId, task);
		if (taskInstance == null) {
			throw new MCHException("流程驳回错误,无可驳回节点!!");
		}
		// 多实例任务
		List<Task> taskTemp = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskDefinitionKey(task.getTaskDefinitionKey()).list();
		// 删除多个多实例任务
		for (int i = 0; i < taskTemp.size(); i++) {
			// taskTemp.get(i)
			// 不删除当前任务
			if (taskTemp.get(i).getId().equals(task.getId())) {
				continue;
			}

			TaskEntity teskEn = (TaskEntity) taskService.createTaskQuery().processInstanceId(processInstanceId)
					.taskId(taskTemp.get(i).getId()).singleResult();
			// 先解绑，然后再删除
			// ProcessInstance instance =null;
			// teskEn.setExecutionId(null);
			// teskEn.setVariable(variableName, value);
			// taskService.saveTask(teskEn);
			// taskService.deleteTask(taskTemp.get(i).getId(),"bohuihuiqian");
			taskService.complete(taskTemp.get(i).getId());
		}
		// 驳回到的流程定义key
		String backActivtiKey = taskInstance.getTaskDefinitionKey();
		// 上一次的历史流程变量
		Map<String, Object> map = new HashMap<String, Object>();
		// 变量获取
		// local历史变量使用
		HistoricVariableInstance multiVar = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(taskInstance.getProcessInstanceId())
				// 历史任务对应的taskid
				.taskId(taskInstance.getId()).variableName("localMulti").singleResult();
		Object multi = null;
		if (multiVar != null && multiVar.getValue() != null) {
			multi = multiVar.getValue();
			map.put("Multi", multiVar.getValue());
		}
		// 被驳回的节点
		String assighee = taskInstance.getAssignee();

		// 流程跳转
		turnTransition(taskId, backActivtiKey, map);
		// 查询驳回后的任务状态
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskInstance.getProcessInstanceId()).list();
		// 如果驳回为普通任务,就把上一次的受理人给他
		if (tasks != null && tasks.size() == 1) {
			taskService.claim(tasks.get(0).getId(), assighee);
		} else if (tasks != null && tasks.size() > 1) {
			List<String> users = (List<String>) multi;
			allocationTask(processInstanceId, users);
		}
		// 查询被驳回节点的变量
	}
	// 待写方法功能 根据节点实例id,查询全部候选人的username
	// 排他网关规则设置 1 与网关id 无关，2前端控件选定下一步节点,3固定选择节点的id 如selectUserTask
	// 4连接线规则为 ${selectUserTask=userttaskId} userttaskId为连接线重点的taskid

	@Override
	public ActivityImpl findActivitiImpl(String taskId, String activityId) throws MCHException {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());
		if (KwHelper.isNullOrEmpty(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}

		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);
		return activityImpl;
	}

	@Override
	public TaskEntity findTaskById(String taskId) throws MCHException {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new MCHException("任务实例未找到!");
		}
		return task;
	}


	@Override
	public void allocationTask(String processInstanceId, List<String> userIds) throws MCHException {

		List<Task> newTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		if (newTasks != null && userIds != null) {
			if (newTasks.size() == userIds.size()) {
				for (int i = 0; i < newTasks.size(); i++) {
					taskService.claim(newTasks.get(i).getId(), userIds.get(i));
				}
			} else if (newTasks.size() == 1 && userIds.size() > 0) {
				taskService.claim(newTasks.get(0).getId(), userIds.get(0));
			} else {
				throw new MCHException("流程分配异常");
			}
		} else {
			throw new MCHException("流程已经结束");
		}
	}

	private void AssignmentTaskToMulti(String processInstanceId, List<String> userIds) throws MCHException {
		// 流程任务实例
		List<Task> newTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		// 当前任务存在，且当前选的有人
		if (newTasks != null && userIds != null && userIds.size() > 0) {
			// 数量必须得对应上
			if (newTasks.size() == userIds.size()) {
				for (int i = 0; i < newTasks.size(); i++) {
					if ("true".equals(newTasks.get(i).getFormKey())) {
						taskService.claim(newTasks.get(i).getId(), userIds.get(i));
					}
				}
			} else {
				throw new MCHException("会签流程分配异常：任务数量不匹配");
			}
		} else {
			throw new MCHException("会签流程分配异常：任务或处理人不存在");
		}
	}

	// 公用节点操作者指定操作者 activiImpl documentation
	@Override
	public void AssignmentTaskToUser(String processInstanceId, List<String> userIds) throws MCHException {
		// 流程任务实例
		List<Task> newTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		// 当前任务存在，且当前选的有人
		if (newTasks != null && userIds != null && userIds.size() > 0) {
			if (newTasks.size() == userIds.size()) {
				for (int i = 0; i < newTasks.size(); i++) {
					taskService.claim(newTasks.get(i).getId(), userIds.get(i));
				}
			} else if (newTasks.size() == 1 && userIds.size() > 0) {
				taskService.claim(newTasks.get(0).getId(), userIds.get(0));
			} else {
				throw new MCHException("流程分配异常");
			}
			// 当前有任务，但是没有选择候选人。
		} else if (newTasks != null) {
			if (newTasks.size() == 1) {
				// 当前节点
				ActivityImpl currActivity = findActivitiImpl(newTasks.get(0).getId(), null);
				if (currActivity.getProperties().get("documentation") != null) {
					// 与这个节点相同操作者的
					String oldActId = currActivity.getProperties().get("documentation").toString();
					// 查询本流程相应id的历史记录，
					List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
							.processInstanceId(processInstanceId).taskDefinitionKey(oldActId)
							.orderByHistoricTaskInstanceEndTime().desc().list();
					if (historicTaskInstances != null && historicTaskInstances.get(0) != null
							&& historicTaskInstances.get(0).getAssignee() != null) {
						taskService.claim(newTasks.get(0).getId(), historicTaskInstances.get(0).getAssignee());
					} else {
						throw new MCHException("未找到共享操作者的节点!!!");
					}
				}
				// 结束流程不在这里处理
				else {
					throw new MCHException("未设置任务操作者");
				}

			} else {
				throw new MCHException("流程未设置候选人");
			}
		} else {
			throw new MCHException("流程已经结束");
		}
	}

	@Override
	public HistoricTaskInstance findRwjectActivity(String taskId, Task task) throws Exception {
		// String backActivitiKey = null;
		// Task task =
		// taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		// 查询该流程全部已经完成的任务
		List<HistoricTaskInstance> taskssAll = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceEndTime().desc().list();

		List<String> nextTasks = BackNextUserTasks(taskId, null, null);// findBackUserTasks(taskId);
		List<HistoricTaskInstance> taskss = new ArrayList<HistoricTaskInstance>();
		taskss.addAll(taskssAll);
		// 排除已完成的当前任务
		for (int i = 0; i < taskssAll.size(); i++) {
			HistoricTaskInstance taskInstanceTemp = taskssAll.get(i);
			if (taskInstanceTemp.getTaskDefinitionKey().equals(task.getTaskDefinitionKey())) {
				taskss.remove(taskInstanceTemp);
			}
			// 排除当前任务的后面的任务
			if (nextTasks.contains(taskInstanceTemp.getTaskDefinitionKey())) {
				taskss.remove(taskInstanceTemp);
			}

			// 排除掉任务节点之后的任务

		}
		if (!taskss.isEmpty()) {
			return taskss.get(0);
		}
		return null;
	}

	// 上一部可能的用户任务//检验合格(●'◡'●)
	@Override
	public void findBackUserTask(ActivityImpl activityImpl, String nowActivityId, List<String> tasks) {

		// 当递归到用户节点时，且不是当前的节点，添加进集合
		if ("userTask".equals(activityImpl.getProperty("type")) && !nowActivityId.equals(activityImpl.getId())) {
			logger.debug("就是他" + activityImpl.getId());
			tasks.add(activityImpl.getId());
			return;
			// 当前不时用户任务节点时继续往上查找
		} else if ("startEvent".equals(activityImpl.getProperty("type"))) {
			logger.debug("开始节点退出" + activityImpl.getId());
			return;
		} else {
			// 被指向节点的线路
			logger.debug("有上级路线" + activityImpl.getId());
			List<PvmTransition> incomingTransitions = activityImpl.getIncomingTransitions();
			if (incomingTransitions == null) {
				logger.debug("不存在" + activityImpl.getId());
			}

			for (PvmTransition pvmTransition : incomingTransitions) {
				logger.debug("路线：" + pvmTransition.getSource());
				findBackUserTask((ActivityImpl) pvmTransition.getSource(), nowActivityId, tasks);
			}
			// (ActivityImpl) outTransitions.get(0).getDestination();
		}
	}

	// 下一个可能的节点
	@Override
	public void findNextUserTaskss(ActivityImpl activityImpl, String nowActivityId, List<String> Nexttasks,
			String formCode, String formId) {
		// 当递归到用户节点时，且不是当前的节点，添加进集合
		if ("userTask".equals(activityImpl.getProperty("type")) && !nowActivityId.equals(activityImpl.getId())) {
			logger.debug("就是他" + activityImpl.getId());
			// try{
			Nexttasks.add(activityImpl.getId());
			// }
			// catch(Exception e){

			// }
			return;
			// 当前不时用户任务节点时继续往上查找
		} else if ("endEvent".equals(activityImpl.getProperty("type"))) {
			logger.debug("结束节点退出" + activityImpl.getId());
			// Nexttasks.add(activityImpl.getId());
			return;
		} else {
			// 被指向节点的线路
			logger.debug("有下级路线" + activityImpl.getId());
			List<PvmTransition> outgoingTransitions = activityImpl.getOutgoingTransitions();
			int flag = 0;
			for (PvmTransition pvmTransition : outgoingTransitions) {
				// 需要表code ,表记录id 那个字段
				// 判断存不存在条件,如果有条件，如果满足条件后就走该条路线，break
				if (pvmTransition.getProperty("conditionText") != null
						&& !"".equals(pvmTransition.getProperty("conditionText").toString())) {
					String conditionText = pvmTransition.getProperty("conditionText").toString();
					// 按等号分割字符串
					String[] conditionTextarray = conditionText.split("=");
					// 当为key=value 形式时
					
						findNextUserTaskss((ActivityImpl) pvmTransition.getDestination(), nowActivityId, Nexttasks,
								formCode, formId);
				
				} else {
					findNextUserTaskss((ActivityImpl) pvmTransition.getDestination(), nowActivityId, Nexttasks,
							formCode, formId);
				}

			}
			// (ActivityImpl) outTransitions.get(0).getDestination();
		}
	}

	// 清空指定活动节点流向
	@Override
	public List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	// 还原指定活动节点流向
	@Override
	public void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	// 流程转向
	@Override
	public void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		taskService.complete(taskId, variables);
		// 流程转向，并且保存task为局部变量
		// taskService.complete(taskId, variables, true);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	@Override
	public List<String> findBackUserTasks(String UserTaksId) {
		// 获取不到返回空
		if (KwHelper.isNullOrEmpty(UserTaksId)) {
			return null;
		}
		List<String> BackUserTasks = new ArrayList<String>();

		HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(UserTaksId)
				.singleResult();
		// Task task =
		// taskService.createTaskQuery().taskId(UserTaksId).singleResult();
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(UserTaksId).singleResult();
		// 流程定义实例
		ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(taskInstance.getProcessDefinitionId());
		// 全部节点定义
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) definition)
				.findActivity(taskInstance.getTaskDefinitionKey());
		// ((ProcessDefinitionImpl)
		// definition).findActivity(task.getTaskDefinitionKey());

		findBackUserTask(activityImpl, task.getTaskDefinitionKey(), BackUserTasks);
		return BackUserTasks;
	}

	@Override
	public String findMyTaskId(String processInId, String userName) {
		List<HistoricTaskInstance> hisTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(userName)
				.processInstanceId(processInId).finished().orderByHistoricTaskInstanceEndTime().desc().list();
		if (hisTasks == null || hisTasks.size() <= 0) {
			return null;
		}
		return hisTasks.get(0).getId();
	}

	// 排除掉驳回后面的节点...
	@Override
	public List<String> BackNextUserTasks(String UserTaksId, String formCode, String formId) throws Exception {
		// 获取不到返回空
		if (KwHelper.isNullOrEmpty(UserTaksId)) {
			return null;
		}
		List<String> NextUserTasks = new ArrayList<String>();
		Task task = taskService.createTaskQuery().taskId(UserTaksId).singleResult();
		// 流程定义实例
		ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		// 全部节点定义
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) definition).findActivity(task.getTaskDefinitionKey());
		// ((ProcessDefinitionImpl)
		// definition).findActivity(task.getTaskDefinitionKey());
		// 后续节点
		int floag = 1;
		BackNextUserTaskss(activityImpl, task.getTaskDefinitionKey(), NextUserTasks, floag,
				task.getTaskDefinitionKey());
		return NextUserTasks;
	}

	@Override
	public void BackNextUserTaskss(ActivityImpl activityImpl, String nowActivityId, List<String> Nexttasks, int floag,
			String actId) {
		// 当递归到用户节点时，且不是当前的节点，添加进集合
		if (activityImpl == null) {
			return;
		}
		if (actId.equals(activityImpl.getId())) {
			floag++;
		}
		if (floag > 2) {
			return;
		}
		if ("userTask".equals(activityImpl.getProperty("type"))) {
			Nexttasks.add(activityImpl.getId());
		}
		/////////////////////////////////////////////////
		if ("endEvent".equals(activityImpl.getProperty("type"))) {
			return;
		} else {
			// 被指向节点的线路
			List<PvmTransition> outgoingTransitions = activityImpl.getOutgoingTransitions();
			for (PvmTransition pvmTransition : outgoingTransitions) {
				String nowid = pvmTransition.getDestination().getId();
				BackNextUserTaskss((ActivityImpl) pvmTransition.getDestination(), nowid, Nexttasks, floag, actId);
			}
			// (ActivityImpl) outTransitions.get(0).getDestination();
		}
	}

	@Override
	public void submitTask(SubmitTask submit, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public List<ProcessState> findstate(List<String> ids, String tableCode) throws Exception {
//
//		List<ProcessState> states = new ArrayList<ProcessState>();
//
//		List<String> idStrings = ids;
//		List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery().finished()
//				.processDefinitionKey(tableCode).list();
//
//		///// 流程历史实例
//		// 完成的流程
//		for (int i = 0; i < ids.size(); i++) {
//			for (int j = 0; j < instances.size(); j++) {
//				if (ids.get(i).equals(instances.get(j).getBusinessKey())) {
//					ProcessState pState = new ProcessState();
//					pState.setId(ids.get(i));
//					pState.setState("c");
//					pState.setTableCode(tableCode);
//					states.add(pState);
//					idStrings.remove(ids.get(i));
//					continue;
//				}
//			}
//		}
//
//		for (int i = 0; i < idStrings.size(); i++) {
//			List<Task> taks = taskService.createTaskQuery().processInstanceBusinessKey(idStrings.get(i)).list();
//			if (taks == null || taks.isEmpty()) {
//				ProcessState pState = new ProcessState();
//				pState.setId(idStrings.get(i));
//				pState.setState("e");
//				pState.setTableCode(tableCode);
//				states.add(pState);
//				continue;
//			}
//			HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
//					.processInstanceBusinessKey(idStrings.get(i)).singleResult();
//
//			// instance.getStartActivityId();
//			if (taks.size() == 1) {
//				String taskname = taks.get(0).getTaskDefinitionKey();
//				// if (((ExecutionEntity)
//				// taks).getProcessInstance().getVariable("startTask").equals(taskname))
//
//				HistoricVariableInstance sssd = historyService.createHistoricVariableInstanceQuery()
//						.processInstanceId(instance.getId()).variableName("startTask").singleResult();
//				String ActivityId = null;
//				if (sssd != null && sssd.getValue() != null) {
//					ActivityId = sssd.getValue().toString();
//				}
//
//				if (taskname != null && ActivityId != null) {
//
//					if (taskname.equals(ActivityId)) {
//						List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
//								.processInstanceBusinessKey(idStrings.get(i)).finished().list();
//						if (taskInstances != null && !taskInstances.isEmpty()) {
//							ProcessState pState = new ProcessState();
//							pState.setId(idStrings.get(i));
//							pState.setState("d");
//							pState.setTableCode(tableCode);
//							states.add(pState);
//							continue;
//						} else {
//							ProcessState pState = new ProcessState();
//							pState.setId(idStrings.get(i));
//							pState.setState("a");
//							pState.setTableCode(tableCode);
//							states.add(pState);
//							continue;
//						}
//					} else {
//						ProcessState pState = new ProcessState();
//						pState.setId(idStrings.get(i));
//						pState.setState("b");
//						pState.setTableCode(tableCode);
//						states.add(pState);
//						continue;
//					}
//					/// 错误情况
//				} else {
//					throw new Exception("流程异常错误");
//				}
//
//			} else {
//				ProcessState pState = new ProcessState();
//				pState.setId(idStrings.get(i));
//				pState.setState("b");
//				pState.setTableCode(tableCode);
//				states.add(pState);
//				continue;
//			}
//
//		}
//
//		return states;
//	}

}
