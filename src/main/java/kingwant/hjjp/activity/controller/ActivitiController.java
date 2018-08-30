package kingwant.hjjp.activity.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.service.ACTTaskService;
import kingwant.hjjp.util.Page;
import kingwant.hjjp.util.PageUtil;
import xyz.michaelch.mchtools.hepler.BeanHelper;

@Controller
@RequestMapping(value = "/activiti")
public class ActivitiController {

	@Autowired
	RepositoryService repositoryService;

	// 流程对象
	// private LeaveManager leaveManager;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	@Autowired
	ACTTaskService actTaskService;



	private static Logger logger = Logger.getLogger(ActivitiController.class);

	@RequestMapping("defList")
	public PublicResult<Map<String, Object>> defList(HttpServletRequest request, Model model ) {

		String processKey = request.getParameter("processKey");

		// 查询出全部的流程定义
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionKey().desc();
		List<String> doList = new ArrayList<String>();
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();

		for (ProcessDefinition processDefinition : processDefinitionList) {
			doList.add(processDefinition.getKey());
		}
		HashSet h = new HashSet(doList);
		doList.clear();
		doList.addAll(h);

		List<String> searchList = new ArrayList<>();
		if (processKey != null && !"".equals(processKey.replace(" ", ""))) {
			for (int i = 0; i < doList.size(); i++) {
				if (doList.get(i).toLowerCase().contains(processKey.toLowerCase())) {
					searchList.add(doList.get(i));
				}
			}
			doList.clear();
			doList.addAll(searchList);
		}

		Map<String, Object> map=new HashMap<>();
        map.put("data", doList);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		
//		model.addAttribute("list", doList);
		

//		return "activiti/list";
	}

	//版本列表
	@RequestMapping("verList")
	public PublicResult<Map<String, Object>> verList(@RequestParam(value = "key") String key, HttpServletRequest request, Model model) {
		List<Object[]> objects = new ArrayList<Object[]>();
		Page<Object[]> page = new Page<Object[]>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);
		ProcessDefinitionQuery processDefinitionQuery1 = repositoryService.createProcessDefinitionQuery()
				.orderByDeploymentId().desc();
		List<ProcessDefinition> processDefinitionList1 = processDefinitionQuery1.processDefinitionKey(key)
				.listPage(pageParams[0], pageParams[1]);
		for (ProcessDefinition processDefinition : processDefinitionList1) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			objects.add(new Object[] { processDefinition, deployment });
		}

		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.orderByProcessDefinitionVersion().desc();
		// long total = query.count();
		page.setTotalCount(query.count());
		page.setResult(objects);
		model.addAttribute("List", page.getResult());
		model.addAttribute("totalSize", page.getTotalCount());
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("pageIndex", page.getPageNo());
		
		Map<String, Object> map=new HashMap<>();
        map.put("data",  page.getResult());
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);

//		return "activiti/content";
	}

	/**
	 * @Title: modelList
	 * @Description: 返回模型列表
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "modelList")
	public String modelList(HttpServletRequest request, Model model) {

		// 差一个条件

		Page<org.activiti.engine.repository.Model> page = new Page<org.activiti.engine.repository.Model>(
				PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);
		ModelQuery query2 = repositoryService.createModelQuery();
		ModelQuery query = repositoryService.createModelQuery();

		String keys = "";
		String namesss = "";
		if (request.getParameter("key") != null) {
			keys = request.getParameter("key").toString();
		}
		if (request.getParameter("name") != null) {
			namesss = request.getParameter("name").toString();
		}
		if (!"".equals(keys) && keys != null) {
			query2.modelKey(keys);
			query.modelKey(keys);
		}
		if (!"".equals(namesss) && namesss != null) {
			query2.modelNameLike(namesss);
			query.modelNameLike(namesss);
		}
		query.orderByModelKey().desc();
		List<org.activiti.engine.repository.Model> list = query2.listPage(pageParams[0], pageParams[1]);
		page.setTotalCount(query.count());
		page.setResult(list);
		model.addAttribute("list", page.getResult());
		model.addAttribute("totalSize", page.getTotalCount());
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("pageIndex", page.getPageNo());
		return "activiti/modelList2";
	}

	@RequestMapping(value = "getActivitiImag")
	public void getActivitiImag(@RequestParam("processInstanceId") String processInstanceId, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			// 1.创建核心引擎流程对象processEngine
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();

			TaskService taskService = processEngine.getTaskService();
			List<Task> task = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			// 流程定义
			BpmnModel bpmnModel = processEngine.getRepositoryService()
					.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

			// 正在活动节点
			List<String> activeActivityIds = null;
			InputStream inputStream = null;
			ProcessDiagramGenerator pdg = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
			if (task != null && task.size() > 0) {
				activeActivityIds = processEngine.getRuntimeService()
						.getActiveActivityIds(task.get(0).getExecutionId());
				// 生成流图片
				inputStream = pdg.generateDiagram(bpmnModel, "PNG", activeActivityIds, activeActivityIds,
						processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getLabelFontName(),
						processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getProcessEngineConfiguration().getClassLoader(),
						1.0);
			} else {
				inputStream = pdg.generateDiagram(bpmnModel, "PNG",
						processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getLabelFontName(),
						processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getProcessEngineConfiguration().getClassLoader(),
						1.0);
			}

			// SecurityHelper.encodeBase64(inputStream)
			response.setContentType("image/png");
			OutputStream os = response.getOutputStream();
			try {
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				inputStream.close();
			} finally {
				os.close();
				inputStream.close();
			}

		} catch (Exception e) {
			// throw new MCHException(e.getMessage());
		}
	}
	
	
//	@RequestMapping("startActivitiOfParm")
//	public String startActivitiOfParm(@RequestParam("processDefId") String processDefId, @RequestParam("dataParm") String dataParm,HttpServletRequest request,
//			Model model) throws Exception {
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//
//		Map<String, String>maps=(Map<String, String>) JSON.parse(dataParm);
//		ProcessInstance processInstance =null;
//		if(maps.isEmpty()){
//			processInstance = actFormService.startForm(processDefId, user.getUsername());
//		}else{
//			processInstance = actFormService.startFormOfParm(processDefId, user.getUsername(),maps);
//		}
//		
//		if (processInstance != null) {
//
//			String formCode = processInstance.getProcessDefinitionKey();
//			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//					.processDefinitionId(processDefId).singleResult();
//
//			int processver = definition.getVersion();
//
//			List<TaskFieldPoint> points = pointService.seleteFFPByKeyAndVer(formCode, processver);
//			if (points != null && points.size() > 0) {
//				int fromVer = points.get(0).getFormVersion();
//				model.addAttribute("fromVer", fromVer);
//			} else {
//				KwForm kwForm = new KwForm();
//				kwForm.setFormCode(formCode);
//				kwForm.setState(1);
//				kwForm = kwFormDao.findOneByBean(kwForm);
//				model.addAttribute("fromVer", kwForm.getFormVer());
//				// model.addAttribute("fromVer", 1);
//			}
//			// .get(0).getFormVersion();
//
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
//					.taskDefinitionKey(processInstance.getActivityId()).singleResult();
//			// 当前执行任务的定义id/key
//			String taskKey = processInstance.getActivityId();
//			String ProcessInId = processInstance.getId();
//			// 当前正在执行任务的id
//			String taskId = task.getId();
//			// 表名
//			model.addAttribute("fomeCode", processInstance.getProcessDefinitionKey());
//			// 表id
//			model.addAttribute("id", processInstance.getBusinessKey());
//			// 流程版本and节点权限版本
//			model.addAttribute("procDefVer", definition.getVersion());
//			// 用户任务key
//			model.addAttribute("taskKey", taskKey);
//			// 流程实例id
//			model.addAttribute("processInId", processInstance.getId());
//			// 当前任务实例id
//			model.addAttribute("taskId", taskId);
//			// 启动时下一步可能的任务id集合
//			List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
//			// 下一步可能的流程Code
//			List<String> taskCodes = actTaskService.findNextUserTasks(taskId, formCode,
//					processInstance.getBusinessKey());
//			// 流程定义实例
//			ProcessDefinitionEntity definitionsss = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//					.getDeployedProcessDefinition(task.getProcessDefinitionId());
//			Map<String, TaskDefinition> taskDefinitionsAll = definitionsss.getTaskDefinitions();
//			for (String string : taskCodes) {
//				taskDefinitions.add(taskDefinitionsAll.get(string));
//			}
//			model.addAttribute("processInId", ProcessInId);
//			model.addAttribute("nextTask", taskDefinitions);
//			model.addAttribute("userPower", true);
//
//			// 流程变量
//			HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(processInstance.getId()).variableName("startUser").singleResult();
//			String userName = "";
//			if (historicVariableInstance != null && historicVariableInstance.getValue() != null) {
//				userName = historicVariableInstance.getValue().toString();
//			}
//			if (user != null && user.getUsername().equals(userName)) {
//				model.addAttribute("interrupt", "enable");
//			} else {
//				model.addAttribute("interrupt", "unable");
//			}
//			// 当前待处理人
//			List<Task> users = new ArrayList<Task>();
//			task.setAssignee(user.getTruename());
//			users.add(task);
//			model.addAttribute("userList", users);
//			
//			String isPreemptive = definition.getDescription();
//			model.addAttribute("isPreemptive", isPreemptive);
//			return "activiti/instance/FormInstance";
//		} else {
//			throw new MCHException("任务创建失败,请稍后重试");
//		}
//	}
	

	/**
	 * @Title: startActiviti
	 * @Description:表单起单
	 * @param processDefId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 *             String
	 */
//	@RequestMapping("startACT")
//	public String startActiviti(@RequestParam("processDefId") String processDefId, HttpServletRequest request,
//			Model model) throws Exception {
//		String jsons = "{}";
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		// String useraName = "useraName";
//
//		ProcessInstance processInstance = actFormService.startForm(processDefId, user.getUsername());
//
//		// 这里需要五个或者六个参数
//		if (processInstance != null) {
//
//			String formCode = processInstance.getProcessDefinitionKey();
//			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//					.processDefinitionId(processDefId).singleResult();
//
//			int processver = definition.getVersion();
//
//			List<TaskFieldPoint> points = pointService.seleteFFPByKeyAndVer(formCode, processver);
//			if (points != null && points.size() > 0) {
//				int fromVer = points.get(0).getFormVersion();
//				model.addAttribute("fromVer", fromVer);
//			} else {
//				KwForm kwForm = new KwForm();
//				kwForm.setFormCode(formCode);
//				kwForm.setState(1);
//				kwForm = kwFormDao.findOneByBean(kwForm);
//				model.addAttribute("fromVer", kwForm.getFormVer());
//				// model.addAttribute("fromVer", 1);
//			}
//			// .get(0).getFormVersion();
//
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
//					.taskDefinitionKey(processInstance.getActivityId()).singleResult();
//			// 当前执行任务的定义id/key
//			String taskKey = processInstance.getActivityId();
//			String ProcessInId = processInstance.getId();
//			// 当前正在执行任务的id
//			String taskId = task.getId();
//			// 表名
//			model.addAttribute("fomeCode", processInstance.getProcessDefinitionKey());
//			// 表id
//			model.addAttribute("id", processInstance.getBusinessKey());
//			// 流程版本and节点权限版本
//			model.addAttribute("procDefVer", definition.getVersion());
//			// 用户任务key
//			model.addAttribute("taskKey", taskKey);
//			// 流程实例id
//			model.addAttribute("processInId", processInstance.getId());
//			// 当前任务实例id
//			model.addAttribute("taskId", taskId);
//			// 启动时下一步可能的任务id集合
//			List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
//			// 下一步可能的流程Code
//			List<String> taskCodes = actTaskService.findNextUserTasks(taskId, formCode,
//					processInstance.getBusinessKey());
//			// 流程定义实例
//			ProcessDefinitionEntity definitionsss = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//					.getDeployedProcessDefinition(task.getProcessDefinitionId());
//			Map<String, TaskDefinition> taskDefinitionsAll = definitionsss.getTaskDefinitions();
//			for (String string : taskCodes) {
//				taskDefinitions.add(taskDefinitionsAll.get(string));
//			}
//			model.addAttribute("processInId", ProcessInId);
//			model.addAttribute("nextTask", taskDefinitions);
//			model.addAttribute("userPower", true);
//
//			// 流程变量
//			HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(processInstance.getId()).variableName("startUser").singleResult();
//			String userName = "";
//			if (historicVariableInstance != null && historicVariableInstance.getValue() != null) {
//				userName = historicVariableInstance.getValue().toString();
//			}
//			if (user != null && user.getUsername().equals(userName)) {
//				model.addAttribute("interrupt", "enable");
//			} else {
//				model.addAttribute("interrupt", "unable");
//			}
//			// 当前待处理人
//			List<Task> users = new ArrayList<Task>();
//			task.setAssignee(user.getTruename());
//			users.add(task);
//			model.addAttribute("userList", users);
//			
//			String isPreemptive = definition.getDescription();
//			model.addAttribute("isPreemptive", isPreemptive);
//			return "activiti/instance/FormInstance";
//		} else {
//			throw new MCHException("任务创建失败,请稍后重试");
//		}
//
//	}

	/**
	 * @Title: SaveTempform
	 * @Description:
	 * @param fomoCode
	 * @param id
	 * @param parms
	 *            void
	 */
//	@RequestMapping("saveTempForm")
//	public String SaveTempform(@RequestParam("formCode") String formCode, @RequestParam("id") String id,
//			@RequestParam("tempFormData") String tempFormData, @RequestParam("taskKey") String taskKey,
//			String operateOpinions, HttpServletRequest request, Model model) {
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		// Map<String, String> maps = new HashMap<String, String>();
//		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
//		Map<String, Object> map = (Map<String, Object>) json.parse(tempFormData);
//		Map<String, Object> datamap = map;
//		KwDraft draft = new KwDraft();
//		draft.setObjCode(formCode);
//		draft.setObjId(id);
//		draft.setOperator(user.getUsername());
//		// 任务节点key
//		draft.setClassfy1(taskKey);
//
//		// 审批意见
//		draft.setClassfy2(operateOpinions);
//		draft.setDraft(datamap);
//		try {
//			KwDraft draftold = new KwDraft();
//			draftold.setObjCode(formCode);
//			draftold.setClassfy1(taskKey);
//			draftold.setObjId(id);
//			// 操作节点
//			kwDraftDao.deleteByBean(draftold);
//			kwDraftDao.store(draft);
//			model.addAttribute("json", "保存草稿成功");
//		} catch (Exception e) {
//			model.addAttribute("json", "保存草稿失败");
//		}
//		return "json";
//	}

	// @Transactional(rollbackFor={xyz.michaelch.mchtools.MCHException.class,Exception.class})
//	@RequestMapping("submission")
//	public String sumbit(SubmitTask subtask, String tempFormData, HttpServletRequest request, Model model)
//			throws Exception {
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		String jsons = "{}";
//		Task task = taskService.createTaskQuery().taskId(subtask.getTaskId()).singleResult();
//		subtask.setTaskName(task.getName());
//		// 跑动流程
//		try {
//			// 查询该节点的权限
//			// 流程定义
//			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//					.processDefinitionId(task.getProcessDefinitionId()).singleResult();
//			Map<String, List<Integer>> powerMap = pointService.findFields(subtask.getFormCode(), subtask.getTaskId(),
//					null, definition.getVersion());
//			// 任务--》--》
//			// actTaskService.submitTask(subtask);
//			logger.debug(tempFormData);findNextUserTaskss
//			Map<String, String> map = (Map<String, String>) json.parse(tempFormData);
//			Set<String> set = map.keySet();
//			Iterator<String> it = set.iterator();
//			// 取的全部表单字段项
//			List<FormToField> formToFields = formToFieldService.findFieldListByForm(subtask.getFormCode());
//			// 获取全部值
//			for (int i = 0; i < formToFields.size(); i++) {
//				String fieldCode = formToFields.get(i).getFieldCode();
//				// 是不是获必填项
//				if (powerMap.get(fieldCode) != null && powerMap.get(fieldCode).contains(4)) {
//					if (map.get(fieldCode) == null || "".equals(map.get(fieldCode))) {
//						JsonBean jsonBean = new JsonBean();
//						jsonBean.setMgasStr("操作失败！必填项未填");
//						jsons = BeanHelper.getMsgJson(jsonBean, false);
//						model.addAttribute("json", jsons);
//						return "json";
//					}
//				}
//				if (map.get(fieldCode) != null && !"".equals(map.get(fieldCode))) {
//					if (map.get(fieldCode) != null && powerMap.get(fieldCode) != null
//							&& (powerMap.get(fieldCode).contains(2) || powerMap.get(fieldCode).contains(4))) {
//						maps.put(fieldCode, map.get(fieldCode));
//					} else if (map.get(fieldCode) != null && powerMap.get(fieldCode) == null) {
//						maps.put(fieldCode, map.get(fieldCode));
//					}
//				}
//			}
//			// 保存数据
//			if ("1".equals(subtask.getFloag())) {
//				if (!maps.isEmpty()) {
//					dynamicTableService.updata(subtask.getFormCode(), subtask.getFormId(), maps);
//					KwDraft draft = new KwDraft();
//					draft.setObjCode(subtask.getFormCode());
//					draft.setObjId(subtask.getFormId());
//					draft.setOperator(user.getUsername());
//					// 操作节点
//					draft.setClassfy1(task.getTaskDefinitionKey());
//					kwDraftDao.deleteByBean(draft);
//				}
//			} else {
//				dynamicTableService.rejectUpdate(subtask.getFormCode(), subtask.getVersion(), subtask.getTaskId(),
//						subtask.getFormId());
//			}
//
//			String taskId = "";
//			// 流程跑起来
//			actTaskService.submitTask(subtask, request);
//			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//					.processInstanceBusinessKey(subtask.getFormId()).singleResult();
//			List<Task> tasks = null;
//			if (processInstance != null) {
//				tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
//						.processInstanceId(processInstance.getId()).list();
//				taskId = tasks.get(0).getId();
//			} else {
//				HistoricProcessInstance processInstance2 = historyService.createHistoricProcessInstanceQuery()
//						.processInstanceBusinessKey(subtask.getFormId()).singleResult();
//				List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
//						.processInstanceId(processInstance2.getId()).list();
//				taskId = taskInstances.get(0).getId();
//			}
//
//			JsonBean jsonBean = new JsonBean();
//			jsonBean.setMgasStr("操作成功:");
//			jsonBean.setNextId(taskId);
//			jsons = BeanHelper.getMsgJson(jsonBean, true);
//			model.addAttribute("json", jsons);
//			return "json";
//		} catch (Exception e) {
//			JsonBean jsonBean = new JsonBean();
//			jsonBean.setMgasStr("操作失败:" + KwHelper.formatExceptionMsg(e.getMessage()));
//			jsons = BeanHelper.getMsgJson(jsonBean, false);
//			model.addAttribute("json", jsons);
//			return "json";
//		}
//	}

//	@RequestMapping("findForm2")
//	public String findForm2(HttpServletRequest request, Model model, @RequestParam("taskId") String retaskId,
//			@RequestParam("flag") String flag) throws Exception {
//		// 当前的登陆用户
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		// 历史任务
//		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(retaskId).singleResult();
//		// 流程定义
//		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//				.processDefinitionId(task.getProcessDefinitionId()).singleResult();
//		// 流程实例
//		HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
//				.processInstanceId(task.getProcessInstanceId()).singleResult();
//		// 表单名称
//		String formCode = definition.getKey();
//		// 表单记录id
//		String formInId = instance.getBusinessKey();
//		// 流程实例id
//		String ProcessInId = instance.getId();
//		// 任务Id
//		String taskId = "";
//		// 表单
//		List<TaskFieldPoint> points = pointService.seleteFFPByKeyAndVer(formCode, definition.getVersion());
//		if (points != null && points.size() > 0) {
//			int fromVer = points.get(0).getFormVersion();
//			model.addAttribute("fromVer", fromVer);
//		} else {
//			KwForm kwForm = new KwForm();
//			kwForm.setFormCode(formCode);
//			kwForm.setState(1);
//			kwForm = kwFormDao.findOneByBean(kwForm);
//			model.addAttribute("fromVer", kwForm.getFormVer());
//		}
//		// 判断传递过的为未完成时
//		if (flag.equals("1") || flag.equals("3") || flag.equals("5")) {
//			// 判断传递过的为未完成时
//			Task newtask = taskService.createTaskQuery().taskId(retaskId).singleResult();
//			// 当前任务执行人
//			String taskUser = newtask.getAssignee().toString();
//			// 当前用户有没有流程操作权限,没有操作权限,屏蔽全部的控件
//			if (user != null && user.getUsername().equals(taskUser)) {
//				model.addAttribute("userPower", true);
//			} else {
//				model.addAttribute("userPower", false);
//			}
//			model.addAttribute("taskKey", newtask.getTaskDefinitionKey());
//			taskId = newtask.getId();
//			// 启动时下一步可能的任务id集合
//			List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
//			// 下一步可能的流程Code
//			List<String> taskCodes = actTaskService.findNextUserTasks(taskId, formCode, formInId);
//			// 流程定义实例
//			ProcessDefinitionEntity definitionsss = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//					.getDeployedProcessDefinition(task.getProcessDefinitionId());
//			Map<String, TaskDefinition> taskDefinitionsAll = definitionsss.getTaskDefinitions();
//			if (taskCodes != null) {
//				for (String string : taskCodes) {
//					taskDefinitions.add(taskDefinitionsAll.get(string));
//				}
//			}
//			if (instance.getEndTime() != null) {
//				model.addAttribute("nextTask", null);
//			} else {
//				model.addAttribute("nextTask", taskDefinitions);
//			}
//			// 第一步節點不能駁回
//			if (actTaskService.findBackUserTasks(taskId).size() > 0) {
//				model.addAttribute("Reject", "trueR");
//			} else {
//				model.addAttribute("Reject", "falseR");
//			}
//			model.addAttribute("processInId", ProcessInId);
//			// 流程变量
//			HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(instance.getId()).variableName("startUser").singleResult();
//			String userName = "";
//			if (historicVariableInstance != null && historicVariableInstance.getValue() != null) {
//				userName = historicVariableInstance.getValue().toString();
//			}
//			if (user != null && user.getUsername().equals(userName)) {
//				model.addAttribute("interrupt", "enable");
//			} else {
//				model.addAttribute("interrupt", "unable");
//			}
//
//			///////////////////////////////////
//		}
//		// 已完成
//		else if (flag.equals("2") || flag.equals("4") || flag.equals("6")) {
//
//		}
//		// 全部未完和全部已完成
//		else if (flag.equals("7") || flag.equals("8")) {
//
//		} else {
//
//		}
//		model.addAttribute("fomeCode", formCode);
//		// 表id
//		model.addAttribute("id", formInId);
//		// 流程版本and节点权限版本
//		model.addAttribute("procDefVer", definition.getVersion());
//		// 查询获得
//		// 用户任务key
//		// model.addAttribute("taskKey", taskKey);
//
//		return "";
//	}

//	@RequestMapping("findForm")
//	public String findForm(HttpServletRequest request, Model model, @RequestParam("taskId") String retaskId,
//			@RequestParam("flag") String flag) throws Exception {
//		// 当前的登陆用户
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		////////////////////////////////////
//		// 查询历史执行任务是否存在
//		// 判断当前任务不存在
//		// if(activityInstance.getActivityType().equals("endEvent")){
//		//
//		// }
//		// 如果传递过来的是不是全部
//		if ((!"7".equals(flag)) && (!"8".equals(flag))) {
//			// 历史任务
//			HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(retaskId)
//					.singleResult();
//			// 流程实例
//			HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
//					.processInstanceId(task.getProcessInstanceId()).singleResult();
//
//			// instance.get
//			// 流程定义
//			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//					.processDefinitionId(task.getProcessDefinitionId()).singleResult();
//			// 提交任务时
//			if ("16".equals(flag)) {
//				if (user != null && task.getAssignee()!=null && !user.getUsername().equals(task.getAssignee().toString())) {
//					retaskId = actTaskService.findMyTaskId(instance.getId(), user.getUsername());
//					if (retaskId != null) {
//						task = historyService.createHistoricTaskInstanceQuery().taskId(retaskId).singleResult();
//						instance = historyService.createHistoricProcessInstanceQuery()
//								.processInstanceId(task.getProcessInstanceId()).singleResult();
//					} else {
//						/// ？？？？？？？？
//					}
//				}
//			}
//
//			// 任务定义id
//			String taskKey = task.getTaskDefinitionKey();
//			// 当前任务为活动任务
//			Task nowtask = taskService.createTaskQuery().taskId(retaskId).singleResult();
//			// 当前任务负责人
//			String taskUser = "";
//			if (nowtask != null && nowtask.getAssignee()!=null) {
//				// 当前任务负责人
//				taskUser = nowtask.getAssignee().toString();
//			}
//
//			// 表单名称
//			String formCode = definition.getKey();
//
//			// 表单记录id
//			String formInId = instance.getBusinessKey();
//			// 流程实例id
//			String ProcessInId = instance.getId();
//			// 当前用户有没有流程进行权限
//			if (user != null && user.getUsername().equals(taskUser)) {
//				model.addAttribute("userPower", true);
//			} else {
//				model.addAttribute("userPower", false);
//			}
//			if("10".equals(flag)){
//				model.addAttribute("userPower", false);
//			}
//			// 表单版版本，，对应流程版本
//			List<TaskFieldPoint> points = pointService.seleteFFPByKeyAndVer(formCode, definition.getVersion());
//			if (points != null && points.size() > 0) {
//				int fromVer = points.get(0).getFormVersion();
//				model.addAttribute("fromVer", fromVer);
//			} else {
//				KwForm kwForm = new KwForm();
//				kwForm.setFormCode(formCode);
//				kwForm.setState(1);
//				kwForm = kwFormDao.findOneByBean(kwForm);
//				model.addAttribute("fromVer", kwForm.getFormVer());
//				// model.addAttribute("fromVer", 1);
//			}
//			model.addAttribute("fomeCode", formCode);
//			// 表id
//			model.addAttribute("id", formInId);
//			// 流程版本and节点权限版本
//			model.addAttribute("procDefVer", definition.getVersion());
//			// 用户任务key
//			model.addAttribute("taskKey", taskKey);
//			// 当前任务实例id
//			Task Newtask = taskService.createTaskQuery().taskId(retaskId).singleResult();
//			String taskId = "";
//			if (Newtask != null) {
//				taskId = retaskId;
//			} else {
//				List<Task> tasksin = taskService.createTaskQuery().processInstanceId(ProcessInId).list();
//				if (tasksin != null && tasksin.size() > 0) {
//					taskId = tasksin.get(0).getId();
//				}
//			}
//			// 当前任务已完成时,taskId=""
//			model.addAttribute("taskId", taskId);
//			// 启动时下一步可能的任务id集合
//			List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
//			// 下一步可能的流程Code
//			List<String> taskCodes = actTaskService.findNextUserTasks(taskId, formCode, formInId);
//			// 流程定义实例
//			ProcessDefinitionEntity definitionsss = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//					.getDeployedProcessDefinition(task.getProcessDefinitionId());
//			Map<String, TaskDefinition> taskDefinitionsAll = definitionsss.getTaskDefinitions();
//			if (taskCodes != null) {
//				for (String string : taskCodes) {
//					taskDefinitions.add(taskDefinitionsAll.get(string));
//					// taskDefinitions.get(0)
//				}
//			}
//			// ActivityImpl currActivity = findActivitiImpl(, null);
//			if (instance.getEndTime() != null) {
//				model.addAttribute("nextTask", null);
//
//			} else {
//				model.addAttribute("nextTask", taskDefinitions);
//			}
//
//			// 第一步節點不能駁回
//			if (actTaskService.findBackUserTasks(task.getId()).size() > 0) {
//				model.addAttribute("Reject", "trueR");
//			} else {
//				model.addAttribute("Reject", "falseR");
//			}
//			model.addAttribute("processInId", ProcessInId);
//			// 流程变量
//			HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(instance.getId()).variableName("startUser").singleResult();
//			String userName = "";
//			if (historicVariableInstance != null && historicVariableInstance.getValue() != null) {
//				userName = historicVariableInstance.getValue().toString();
//			}
//			if (user != null && user.getUsername().equals(userName)) {
//				model.addAttribute("interrupt", "enable");
//			} else {
//				model.addAttribute("interrupt", "unable");
//			}
//
//			// 已完成的单
//			if (flag.equals("2") || flag.equals("4") || flag.equals("6")) {
//				model.addAttribute("userPower", false);
//				// 查询已完成后的单子如果单子未结束显示单子最新状态，
//				// 如果单子结束，显示单子结束状态
//
//			}
//			//////////////////////////////////////////////////////////
//			// 待处理人查询
//			List<Task> nowTasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < nowTasks.size(); i++) {
//				if (!KwHelper.isNullOrEmpty(nowTasks.get(i).getAssignee())) {
//					if (i > 0) {
//						sb.append(",");
//					}
//					sb.append("'");
//					sb.append(nowTasks.get(i).getAssignee());
//					sb.append("'");
//				}
//			}
//			String userStr = "";
//			userStr = sb.toString();
//			List<String> userss = null;
//				try{	
//			       userss=	userserice.selectTrueNameByUserNames(userStr);
//				}catch(Exception e){
//					
//				}
//			// 当前待处理人
//			if (userss != null) {
//				for (int j = 0; j < userss.size(); j++) {
//					nowTasks.get(j).setAssignee(userss.get(j));
//				}
//			}
//			model.addAttribute("userList", nowTasks);
//			//// 标识是否为抢占式
//			String isPreemptive = definition.getDescription();
//			model.addAttribute("isPreemptive", isPreemptive);
//		} else if ("7".equals(flag) || "8".equals(flag)) {
//			HistoricProcessInstance instance = null;
//			if ("7".equals(flag)) {
//				Task unTask = taskService.createTaskQuery().taskId(retaskId).singleResult();
//				instance = historyService.createHistoricProcessInstanceQuery()
//						.processInstanceId(unTask.getProcessInstanceId()).singleResult();
//			} else {
//				// 历史流程实例
//				instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(retaskId)
//						.singleResult();
//			}
//
//			// 已经完成的流程没有啥可以操作的
//			model.addAttribute("userPower", false);
//			// 流程定义
//			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
//					.processDefinitionId(instance.getProcessDefinitionId()).singleResult();
//			// 表单名称
//			String formCode = definition.getKey();
//			// 表单
//
//			List<TaskFieldPoint> points = pointService.seleteFFPByKeyAndVer(formCode, definition.getVersion());
//
//			if (points != null && points.size() > 0) {
//				int fromVer = points.get(0).getFormVersion();
//				model.addAttribute("fromVer", fromVer);
//			} else {
//				KwForm kwForm = new KwForm();
//				kwForm.setFormCode(formCode);
//				kwForm.setState(1);
//				kwForm = kwFormDao.findOneByBean(kwForm);
//				model.addAttribute("fromVer", kwForm.getFormVer());
//			}
//
//			model.addAttribute("fomeCode", formCode);
//			// 表id
//			model.addAttribute("id", instance.getBusinessKey());
//			// 流程版本and节点权限版本
//			model.addAttribute("procDefVer", definition.getVersion());
//			// 用户任务key
//			// 流程实例id
//			model.addAttribute("processInId", instance.getId());
//			// 当前任务已完成时,taskId=""
//			model.addAttribute("taskId", "");
//			// 美的用户任务key
//			model.addAttribute("taskKey", "");
//			// model.addAttribute("processInId", ProcessInId);
//			model.addAttribute("nextTask", null);
//			model.addAttribute("interrupt", "unable");
//
//			// 待处理人查询
//			List<Task> nowTasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < nowTasks.size(); i++) {
//				if (!KwHelper.isNullOrEmpty(nowTasks.get(i).getAssignee())) {
//					if (i > 0) {
//						sb.append(",");
//					}
//					sb.append("'");
//					sb.append(nowTasks.get(i).getAssignee());
//					sb.append("'");
//				}
//			}
//			String userStr = "";
//			userStr = sb.toString();
//			List<String> userss = userserice.selectTrueNameByUserNames(userStr);
//			// 当前待处理人
//			if (userss != null) {
//				for (int j = 0; j < userss.size(); j++) {
//					nowTasks.get(j).setAssignee(userss.get(j));
//				}
//			}
//			model.addAttribute("userList", nowTasks);
//			//// 标识是否为抢占式
//			String isPreemptive = definition.getDescription();
//			model.addAttribute("isPreemptive", isPreemptive);
//		}
//		// 需要：表单名，表单记录id,流程id,当前taskid ,taskkey,当前任务操作人员，
//		return "activiti/instance/FormInstance";
//	}

//	@RequestMapping("findFormAct")
//	@ResponseBody
//	public Map<String, Object> findForm(HttpServletRequest request, Model model, String formCode, int fromVer,
//			String DataId, String taskKey, int procDefVer) {
//		FormTable formTable = formServices.findFormByCode(formCode);
//		// 当前的登陆用户
//		Subject subject = SecurityUtils.getSubject();
//		KwUser user = (KwUser) subject.getPrincipal();
//		String title = "";
//		if (formTable != null) {
//			title = formTable.getFormTitle();
//		}
//		// 查询表单内容和权限
//		KwForm kwForm = new KwForm();
//		kwForm.setFormCode(formCode);
//		if (fromVer != 0) {
//			kwForm.setFormVer(fromVer);
//		} else {
//			// 没找到版本就用启用版本
//			kwForm.setState(1);
//		}
//		kwForm = kwFormDao.findOneByBean(kwForm);
//
//		// 数据
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap = dynamicTableService.selectDataById(formCode, DataId);
//		// 临时数据
//		KwDraft draft = new KwDraft();
//		draft.setObjCode(formCode);
//		draft.setObjId(DataId);
//		draft.setClassfy1(taskKey);
//		draft.setOperator(user.getUsername());
//		draft = kwDraftDao.findOneByBean(draft);
//		Map<String, Object> tempDataMap = new HashMap<>();
//		String operateOpinions = "";
//		if (draft != null) {
//			tempDataMap = draft.getDraft();
//			operateOpinions = draft.getClassfy2();
//		}
//		// 节点权限
//		Map<String, String> power = new HashMap<>();
//		power = pointService.getFieldsPower(formCode, taskKey, procDefVer);
//
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//
//		List<FormToField> list = formToFieldService.findFieldListByForm(formCode);
//		resultMap.put("content", kwForm.getFormContent());
//		resultMap.put("datamap",
//				com.alibaba.fastjson.JSON.toJSONString(dataMap, SerializerFeature.WriteDateUseDateFormat));
//		resultMap.put("draftmap",
//				com.alibaba.fastjson.JSON.toJSONString(tempDataMap, SerializerFeature.WriteDateUseDateFormat));
//		resultMap.put("powermap", com.alibaba.fastjson.JSON.toJSONString(power));
//		resultMap.put("list", com.alibaba.fastjson.JSON.toJSONString(list));
//		resultMap.put("title", title);
//		resultMap.put("operateOpinions", operateOpinions);
//		// resultMap.put("richCode", fieldCodes);
//		// com.alibaba.fastjson.JSON.t
//
//		return resultMap;
//	}

	/**
	 * 查询审批记录
	 * 
	 * @param por
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "findData", method = { RequestMethod.POST }, produces = {
//			"application/json;charset=utf-8" })
//	public List<RecordOfApproval> findData(ProcessOperateRecord por, HttpServletRequest request) {
//		String json = "{}";
//		List<RecordOfApproval> roas = null;
//		try {
//			roas = processOperateRecordServiceImpl.findRecordOfApprovalList(por, request);
//			json = BeanHelper.getMsgJson("操作成功！", true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			json = BeanHelper.getMsgJson("操作失败：" + KwHelper.formatExceptionMsg(e.getMessage()), false);
//		}
//		return roas;
//	}


	private Map getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	@RequestMapping("interrupt")
	public String interrupt(@RequestParam("processInId") String processInId,User user, Model model) {
		try {
			// 当前的登陆用户
			Subject subject = SecurityUtils.getSubject();
			//KwUser user = (KwUser) subject.getPrincipal();
			// 流程变量
			HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(processInId).variableName("startUser").singleResult();
			String userName = "";
			if (historicVariableInstance != null && historicVariableInstance.getValue() != null) {
				userName = historicVariableInstance.getValue().toString();
			}
			if (user != null && user.getName().equals(userName)) {
				runtimeService.suspendProcessInstanceById(processInId);
				/////////// 添加审批记录.......//////////////
				// operateRecordService.addOPera(submit, request);
				model.addAttribute("json", "操作成功");
			} else {
				model.addAttribute("json", "不具备操作权限");
			}

		} catch (ActivitiObjectNotFoundException e) {
			model.addAttribute("json", "该流程已经结束,不能终止");
		} catch (ActivitiException e) {
			model.addAttribute("json", "该流程已经处于终止状态");
		}

		return "json";
	}

	@RequestMapping("signUpTask")   /* 签收 */
	public String  signUpTask(@RequestParam("taskId") String taskId,User user, Model mode){
		String json = "{}";
		try {
			Subject subject = SecurityUtils.getSubject();
			
			//KwUser user = (KwUser) subject.getPrincipal();
			taskService.claim(taskId, user.getName());
			json = BeanHelper.getMsgJson("签收成功!", true);
		}catch (ActivitiObjectNotFoundException acte){
			json = BeanHelper.getMsgJson("签收失败，任务已完成或不存在！", false);
		}
		catch (ActivitiTaskAlreadyClaimedException acte){
			json = BeanHelper.getMsgJson("签收失败，任务已被其他人签收！", false);
		}
		catch (Exception e) {
			json = BeanHelper.getMsgJson("签收失败，"+e.getMessage(), false);
		}
		mode.addAttribute("json",json);
		return "json";
	}
	
	@RequestMapping("assignmentTask")
	public String  assignmentTask(@RequestParam("taskId") String taskId,@RequestParam("username") String username, Model mode){
		try {
			taskService.delegateTask(taskId, username);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "json";
	}

	
	
}