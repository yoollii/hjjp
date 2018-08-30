package kingwant.hjjp.activity.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Pool;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverterUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import kingwant.hjjp.service.ACTTaskService;
import xyz.michaelch.mchtools.MCHException;
import xyz.michaelch.mchtools.hepler.BeanHelper;

@Controller
@RequestMapping(value = "/model")
public class FlowModelController {

	protected static final Logger logger = LoggerFactory.getLogger(FlowModelController.class);

	// @Autowired
	// private RepositoryService repositoryService;
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

	/**
	 * 创建模型
	 * 
	 * @throws MCHException
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public void create(@RequestParam("name") String name, @RequestParam("key") String key, String description,
			HttpServletRequest request, HttpServletResponse response) throws MCHException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			// modelObjectNode.put("FormId", formId);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

			response.sendRedirect(
					request.getContextPath() + "/modeler.html?modelId=" + modelData.getId() + "&key=" + key);

		} catch (Exception e) {
			throw new MCHException();
		}
	}

	// /**
	// * 删除模型
	// * @param id
	// * @return
	// */
	
	@RequiresPermissions("app:flow:model:del") 
	@RequestMapping(value = "delete")
	public String delete(@RequestParam("modelId") String modelId, org.springframework.ui.Model model) {
		String json = "{}";
		try {
			repositoryService.deleteModel(modelId);
			json = BeanHelper.getMsgJson("操作成功！！", true);
		} catch (Exception e) {
			json = BeanHelper.getMsgJson("操作失败！！", false);
			// TODO: handle exception
		}
		model.addAttribute("json", json);
		return "json";
	}

	/**
	 * @Title: deployOne
	 * @Description: 流程模型部署
	 * @param modelId
	 * @return String
	 * @throws MCHException
	 */
	@RequestMapping("deploy")
	public String deployOne(@RequestParam(value = "modelId") String modelId) throws MCHException {

		// 获取流程中的表单信息
		Model modelData = repositoryService.getModel(modelId);
		ObjectNode modelNode;
		try {
			modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));

			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);

			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			DeploymentBuilder dBuilder = repositoryService.createDeployment().name(modelData.getName());
		} catch (JsonProcessingException e) {
			throw new MCHException(e.getMessage());

		} catch (IOException e) {
			throw new MCHException(e.getMessage());
		}

		return "ok";
	}

	/**
	 * @Title: getStartForm
	 * @Description: 返回初始话的流程表单 没找到返回错误界面
	 * @param deploymentId
	 *            流程实例id
	 * @param ProcessId
	 *            流程启用id
	 * @return Object
	 */
	@RequestMapping(value = "/getStartForm", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object getStartForm(@RequestBody String deploymentId, @RequestBody String ProcessId) {
		Map<String, String> map = new HashMap<String, String>();
		String deString = null;
		deString = deploymentId.replaceAll("=", "");
		String form = ""; // this.getStartForm1(deString);
		map.put("form", form);
		return map;
	}

	/**
	 * @Title: deploymentProcessDefinition
	 * @Description: 部署
	 * @param modelId
	 * @param model1
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 *             String
	 */
	@RequiresPermissions("app:flow:model:deploy") 
	@RequestMapping("deploy2")
	public String deploymentProcessDefinition(@RequestParam(value = "modelId") String modelId,
			org.springframework.ui.Model model1) throws JsonProcessingException, IOException {
		String json = "{}";
		try {
			// 获取流程中的表单信息
			// Model
			// modelData=repositoryService.createModelQuery().modelKey(key).singleResult();
			// getModel(key);
			Model modelData = repositoryService.createModelQuery().modelId(modelId).singleResult();
			String key = modelData.getKey();
			ObjectNode modelNode;
			modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			ObjectNode processs = (ObjectNode) modelNode.findValue("properties");
			processs.put("process_id", key);
			byte[] bpmnBytes = null;
			// 扩展属性最差的做法
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			DeploymentBuilder db = repositoryService.createDeployment().name(modelData.getName());
			// 停用前面的流程
			List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey(key).list();
			// 挂起全部定义
			for (ProcessDefinition Def : definitions) {
				if (!Def.isSuspended()) {
					repositoryService.suspendProcessDefinitionById(Def.getId());
				}
			}
			// repositoryService.suspendProcessDefinitionByKey(key);

			Deployment deployment = db.addString(modelData.getName() + ".bpmn20.xml", new String(bpmnBytes, "utf-8"))
					.deploy();

			logger.debug("部署名称1:" + deployment.getId());
			// 判断是否部署成功
			if (deployment != null && deployment.getId() != null) {
				// 挂起全部定义
				logger.debug("部署名称2:" + deployment.getId());
				ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
						.processDefinitionKey(key).active().singleResult();
				// 激活特定定义
				// repositoryService.activateProcessDefinitionById(processDefinitionId);
			}
			json = BeanHelper.getMsgJson("操作成功", true);
		} catch (Exception e) {
			json = BeanHelper.getMsgJson("操作失败,流程文件不符合规范", false);
			// TODO: handle exception
		}
		model1.addAttribute("json", json);
		return "json";
	}

	@RequestMapping("deploy3")
	public String oooo(@RequestParam("key") String key, @RequestParam("id") String id,
			org.springframework.ui.Model model) throws Exception {
		ProcessInstance processInstance = runtimeService // 获取运行时Service
				// .startProcessInstanceById("process:1:17507","1234567890");
				.startProcessInstanceByKey(key, id);

		// 设置下一步执行者
		String taskId = processInstance.getActivityId();
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(taskId)
				.singleResult();
		model.addAttribute("processId", processInstance.getId());
		model.addAttribute("taskId", task.getId());

		// 分配到个人
		taskService.claim(task.getId(), "wangbaoqiang");

		// actTaskService.assignationTaskUser(processInstance.getId(), taskId,
		// "张三");
		return "activiti/process/edit";
	}
	/**
	 * 导出model对象为指定类型
	 *
	 * @param modelId
	 *            模型ID
	 * @param type
	 *            导出文件类型(bpmn\json)
	 */
	
	@RequiresPermissions("app:flow:model:export") 
	@RequestMapping(value = "export/{modelId}/{type}")
	public void export(@PathVariable("modelId") String modelId, @PathVariable("type") String type,
			HttpServletResponse response) {
		try {
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

			JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

			// 处理异常
			if (bpmnModel.getMainProcess() == null) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				response.getOutputStream().println("no main process, can't export for type: " + type);
				response.flushBuffer();
				return;
			}

			String filename = "";
			byte[] exportBytes = null;

			String mainProcessId = bpmnModel.getMainProcess().getId();

			if (type.equals("bpmn")) {

				BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
				exportBytes = xmlConverter.convertToXML(bpmnModel);

				filename = mainProcessId + ".bpmn20.xml";
			} else if (type.equals("json")) {
				exportBytes = modelEditorSource;
				filename = mainProcessId + ".json";
			}

			ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
			IOUtils.copy(in, response.getOutputStream());

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
		}
	}

	@RequestMapping("DefEnable")
	public String enableDef(@RequestParam("processDefinitionId") String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);

		String key = processDefinition.getKey();

		List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.list();
		// 挂起全部定义
		for (ProcessDefinition Def : definitions) {
			if (!Def.isSuspended()) {
				repositoryService.suspendProcessDefinitionById(Def.getId());
			}
		}
		// repositoryService.suspendProcessDefinitionByKey(key);
		// 激活特定定义
		repositoryService.activateProcessDefinitionById(processDefinitionId);
		return "json";
	}

	

	@RequestMapping(value = "/resource/read")
	public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("resourceType") String resourceType, HttpServletResponse response) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	///////////
	@RequestMapping(value = "/resource/read2")
	public String loadByDeployment2(@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("resourceType") String resourceType, HttpServletResponse response,
			org.springframework.ui.Model model) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
		return "";
	}

}
