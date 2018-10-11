package kingwant.hjjp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kingwant.hjjp.service.ACTFormService;
import kingwant.hjjp.service.ACTTaskService;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;

/**
 * @author 黄河 表单/数据渲染 流程操作，流转
 * @date: 2018年4月2日 下午3:50:20
 */
@Service
public class ACTFormServiceImpl implements ACTFormService {

	private static Logger logger = LoggerFactory.getLogger(ACTFormServiceImpl.class);

	// @Autowired
	// ProcessEngine processEngine;

	@Autowired
	FormService formServices;

	// 流程对象
	// private LeaveManager leaveManager;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	ACTTaskService actTaskService;

	@Override
	@Transactional(rollbackFor = { xyz.michaelch.mchtools.MCHException.class, Exception.class })
	public ProcessInstance startForm(String processDefinitionId) throws Exception {
		logger.debug("FormKey", processDefinitionId);
		// 表单实例id 业务实例id
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		// 添加业务表实例
		 try {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String processDefinitionKey = processDefinition.getKey();
		// 新建表单数据表
		// 如果有需要更新的内容——更新表单数据表
		//dynamicTableService.updata(processDefinitionKey, id, formValues);
		// 流程实例pi 启动 businessId
		Map<String, Object> userMap = new HashMap<String, Object>();
//		userMap.put("startUser", userName);
		ProcessInstance processInstance = runtimeService // 获取运行时Service
				.startProcessInstanceById(processDefinitionId, id, userMap);
		// 设置下一步执行者s
//		actTaskService.simpleAssignationTaskUser(processInstance.getId(), userName);
		return processInstance;
		  } catch (Exception e) {
			 return null;
		  }
		// return null;
	}
	@Override
	@Transactional(rollbackFor = { xyz.michaelch.mchtools.MCHException.class, Exception.class })
	public ProcessInstance startFormOfParm(String processDefinitionId, String userName,Map<String, String> maps) throws Exception {
		logger.debug("FormKey", processDefinitionId);
		// 表单实例id 业务实例id
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		// 添加业务表实例
		 try {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String processDefinitionKey = processDefinition.getKey();
		// 新建表单数据表
//		dynamicTableService.insert(processDefinitionKey, id);
		// 如果有需要更新的内容——更新表单数据表
//		dynamicTableService.updata(processDefinitionKey, id, maps);
		// 流程实例pi 启动 businessId
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("startUser", userName);
		ProcessInstance processInstance = runtimeService // 获取运行时Service
				.startProcessInstanceById(processDefinitionId, id, userMap);
		// 设置下一步执行者
		actTaskService.simpleAssignationTaskUser(processInstance.getId(), userName);
		return processInstance;
		  } catch (Exception e) {
			 return null;
		  }
		// return null;
	}
}
