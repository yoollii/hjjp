package kingwant.hjjp.activity.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.service.ACTTaskService;
import kingwant.hjjp.util.KwHelper;
import xyz.michaelch.mchtools.MCHException;
import xyz.michaelch.mchtools.hepler.BeanHelper;

@Api(tags = "Activiti流程模型")
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
	@Autowired
	ProcessEngine processEngine;

	/**
	 * 创建模型
	 * 
	 * @throws MCHException
	 */
	@ApiOperation(value = "创建流程模型(创建后自动跳转)", notes = "所需参数：name(名字);key(关键字);description(描述)")
	@GetMapping(value = "create")
	public String create(String name, String key, String description, HttpServletRequest request,
			HttpServletResponse response) throws MCHException {
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
			modelObjectNode.put("name", name);
			modelObjectNode.put("revision", 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put("description", description);
			modelObjectNode.put("modType", 1);
			// modelObjectNode.put("FormId", formId);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

			// response.sendRedirect(
			// request.getContextPath() + "modeler.html?modelId=" + modelData.getId() +
			// "&key=" + key);
			return "redirect:/modeler.html?modelId=" + modelData.getId() + "&key=" + key;

		} catch (Exception e) {
			throw new MCHException();
		}
	}

	@ApiOperation(value = "创建流程模型（前端手动加代码跳转）", notes = "所需参数：name(必填：名字);key(必填：关键字);description(选填：描述)")
	@PostMapping(value = "create2")
	@ResponseBody
	public PublicResult<String> create2(
			@kingwant.hjjp.annotation.ValidationParam("name,key") @RequestBody JSONObject requestJson)
			throws MCHException {
		try {
			String name = requestJson.getString("name");
			String key = requestJson.getString("key");
			String description = requestJson.getString("description");

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("revision", 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put("description", description);
			// modelObjectNode.put("FormId", formId);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

			// response.sendRedirect(
			// request.getContextPath() + "modeler.html?modelId=" + modelData.getId() +
			// "&key=" + key);
			return new PublicResult<String>(PublicResultConstant.SUCCESS,
					"/modeler.html?modelId=" + modelData.getId() + "&key=" + key);

		} catch (Exception e) {
			return new PublicResult<String>(PublicResultConstant.ERROR, e.getMessage());
		}
	}
	
	
	@ApiOperation(value = "创建流程模型(创建后自动跳转)", notes = "所需参数：name(名字);key(关键字);description(描述);modelModelId(模型的id)")
	@GetMapping(value = "create3")
	public String create3(String name, String key, String description,String modelModelId, HttpServletRequest request,
			HttpServletResponse response) throws MCHException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			//repositoryService.createModelQuery()
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("revision", 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put("description", description);
			modelObjectNode.put("modType", 2);
			// modelObjectNode.put("FormId", formId);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			
			
			 // Model model = repositoryService.getModel("123456");
			 //String resure = new String(repositoryService.getModelEditorSource(model.getId()), "utf-8");
			 
			// repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));
			//repositoryService.getBpmnModel(processDefinitionId)

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),repositoryService.getModelEditorSource(modelModelId));

			// response.sendRedirect(
			// request.getContextPath() + "modeler.html?modelId=" + modelData.getId() +
			// "&key=" + key);
			return "redirect:/modeler.html?modelId=" + modelData.getId() + "&key=" + key;

		} catch (Exception e) {
			throw new MCHException();
		}
	}
	
	
	@ApiOperation(value =  "创建流程模型(前端手动加代码跳转)", notes = "所需参数：name(名字);key(关键字);description(描述);modelModelId(模型的id)")
	@PostMapping(value = "create4")
	@ResponseBody
	public PublicResult<String> create4(
			@kingwant.hjjp.annotation.ValidationParam("name,key") @RequestBody JSONObject requestJson)
			throws MCHException {
		try {
			String name = requestJson.getString("name");
			String key = requestJson.getString("key");
			String description = requestJson.getString("description");
			String modelModelId=requestJson.getString("modelModelId");
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("revision", 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put("description", description);
			modelObjectNode.put("modType", 2);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),repositoryService.getModelEditorSource(modelModelId));

			return new PublicResult<String>(PublicResultConstant.SUCCESS,
					"/modeler.html?modelId=" + modelData.getId() + "&key=" + key);
		} catch (Exception e) {
			return new PublicResult<String>(PublicResultConstant.ERROR, e.getMessage());
		}
	}
	
	/*@ApiOperation(value = "创建流程模型(前端手动加代码跳转)", notes = "所需参数：name(名字);key(关键字);description(描述);modelModelId(模型的id)")
	@GetMapping(value = "create4")
	public PublicResult<String> create4(String name, String key, String description,String modelModelId, HttpServletRequest request,
			HttpServletResponse response) throws MCHException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			//repositoryService.createModelQuery()
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("revision", 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put("description", description);
			modelObjectNode.put("modType", 2);
			// modelObjectNode.put("FormId", formId);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			 // Model model = repositoryService.getModel("123456");
			 //String resure = new String(repositoryService.getModelEditorSource(model.getId()), "utf-8");
			// repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));
			//repositoryService.getBpmnModel(processDefinitionId)
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),repositoryService.getModelEditorSource(modelModelId));
			// response.sendRedirect(
			// request.getContextPath() + "modeler.html?modelId=" + modelData.getId() +
			// "&key=" + key);
//			return "redirect:/modeler.html?modelId=" + modelData.getId() + "&key=" + key;
			return new PublicResult<String>(PublicResultConstant.SUCCESS,
					"/modeler.html?modelId=" + modelData.getId() + "&key=" + key);

		} catch (Exception e) {
			return new PublicResult<String>(PublicResultConstant.ERROR, e.getMessage());
		}
	}*/

	@ApiOperation(value = "编辑流程模型", notes = "所需参数：modelId(模型的id);key(关键字)")
	@RequestMapping(value = "editModel")
	public String editModel(String modelId, String key, HttpServletRequest request, HttpServletResponse response)
			throws MCHException {
		try {
			if (KwHelper.isNullOrEmpty(modelId) || KwHelper.isNullOrEmpty(key)) {
				return "缺少关键参数！！";
			}
			// repositoryService.addModelEditorSource(modelData.getId(),
			// editorNode.toString().getBytes("utf-8"));

			// response.sendRedirect(
			// request.getContextPath() + "modeler.html?modelId=" + modelData.getId() +
			// "&key=" + key);
			return "redirect:/modeler.html?modelId=" + modelId + "&key=" + key;

		} catch (Exception e) {
			throw new MCHException();
		}
	}

	@ApiOperation(value = "删除流程模型", notes = "所需参数：modelId(模型的id)")
	@DeleteMapping(value = "delModel/{modelId}")
	@ResponseBody
	public PublicResult<Map<String, Object>> delModel(@PathVariable String modelId) throws MCHException {

		if (KwHelper.isNullOrEmpty(modelId)) {
			return new PublicResult<Map<String, Object>>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			repositoryService.deleteModel(modelId);
		} catch (Exception e) {
			return new PublicResult<Map<String, Object>>(PublicResultConstant.SQL_EXCEPTION, null);
		}
		return new PublicResult<Map<String, Object>>(PublicResultConstant.SUCCESS, null);

	}

	// /**
	// * 删除模型
	// * @param id
	// * @return
	// */
//	@ApiOperation(value = "删除流程模型", notes = "所需参数：modelId(流程模型id) ")
//	@DeleteMapping(value = "delete11")
//	public PublicResult<Map<String, Object>> delete11(String modelId) {
//		if (KwHelper.isNullOrEmpty(modelId)) {
//			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
//		}
//		try {
//			repositoryService.deleteModel(modelId);
//		} catch (Exception e) {
//			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
//		}
//		return new PublicResult<>(PublicResultConstant.SUCCESS, null);
//	}

	/**
	 * @Title: deployOne
	 * @Description: 流程模型部署
	 * @param modelId
	 * @return String
	 * @throws MCHException
	 */
	// @RequestMapping("deploy")
	// public String deployOne(@RequestParam(value = "modelId") String modelId)
	// throws MCHException {
	//
	// // 获取流程中的表单信息
	// Model modelData = repositoryService.getModel(modelId);
	//
	// ObjectNode modelNode;
	// try {
	// modelNode = (ObjectNode) new ObjectMapper()
	// .readTree(repositoryService.getModelEditorSource(modelData.getId()));
	//
	// byte[] bpmnBytes = null;
	// BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
	//
	// bpmnBytes = new BpmnXMLConverter().convertToXML(model);
	// DeploymentBuilder dBuilder =
	// repositoryService.createDeployment().name(modelData.getName());
	// } catch (JsonProcessingException e) {
	// throw new MCHException(e.getMessage());
	//
	// } catch (IOException e) {
	// throw new MCHException(e.getMessage());
	// }
	//
	// return "ok";
	// }

	// /**
	// * @Title: getStartForm
	// * @Description: 返回初始话的流程表单 没找到返回错误界面
	// * @param deploymentId
	// * 流程实例id
	// * @param ProcessId
	// * 流程启用id
	// * @return Object
	// */
	// @RequestMapping(value = "/getStartForm", method = RequestMethod.POST,
	// produces = "application/json;charset=utf-8")
	// @ResponseBody
	// public Object getStartForm(@RequestBody String deploymentId, @RequestBody
	// String ProcessId) {
	// Map<String, String> map = new HashMap<String, String>();
	// String deString = null;
	// deString = deploymentId.replaceAll("=", "");
	// String form = ""; // this.getStartForm1(deString);
	// map.put("form", form);
	// return map;
	// }

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
	// @RequiresPermissions("app:flow:model:deploy")
	// @RequestMapping("deploy2")
	// public String deploymentProcessDefinition(@RequestParam(value = "modelId")
	// String modelId,
	// org.springframework.ui.Model model1) throws JsonProcessingException,
	// IOException {
	// String json = "{}";
	// try {
	// // 获取流程中的表单信息
	// // Model
	// //
	// modelData=repositoryService.createModelQuery().modelKey(key).singleResult();
	// // getModel(key);
	// Model modelData =
	// repositoryService.createModelQuery().modelId(modelId).singleResult();
	// String key = modelData.getKey();
	// ObjectNode modelNode;
	// modelNode = (ObjectNode) new ObjectMapper()
	// .readTree(repositoryService.getModelEditorSource(modelData.getId()));
	// ObjectNode processs = (ObjectNode) modelNode.findValue("properties");
	// processs.put("process_id", key);
	// byte[] bpmnBytes = null;
	// // 扩展属性最差的做法
	// BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
	// bpmnBytes = new BpmnXMLConverter().convertToXML(model);
	// DeploymentBuilder db =
	// repositoryService.createDeployment().name(modelData.getName());
	// // 停用前面的流程
	// List<ProcessDefinition> definitions =
	// repositoryService.createProcessDefinitionQuery()
	// .processDefinitionKey(key).list();
	// // 挂起全部定义
	// for (ProcessDefinition Def : definitions) {
	// if (!Def.isSuspended()) {
	// repositoryService.suspendProcessDefinitionById(Def.getId());
	// }
	// }
	// // repositoryService.suspendProcessDefinitionByKey(key);
	//
	// Deployment deployment = db.addString(modelData.getName() + ".bpmn20.xml", new
	// String(bpmnBytes, "utf-8"))
	// .deploy();
	//
	// logger.debug("部署名称1:" + deployment.getId());
	// // 判断是否部署成功
	// if (deployment != null && deployment.getId() != null) {
	// // 挂起全部定义
	// logger.debug("部署名称2:" + deployment.getId());
	// ProcessDefinition definition =
	// repositoryService.createProcessDefinitionQuery()
	// .processDefinitionKey(key).active().singleResult();
	// // 激活特定定义
	// // repositoryService.activateProcessDefinitionById(processDefinitionId);
	// }
	// json = BeanHelper.getMsgJson("操作成功", true);
	// } catch (Exception e) {
	// json = BeanHelper.getMsgJson("操作失败,流程文件不符合规范", false);
	// // TODO: handle exception
	// }
	// model1.addAttribute("json", json);
	// return "json";
	// }
	//
	// @RequestMapping("deploy3")
	// public String oooo(@RequestParam("key") String key, @RequestParam("id")
	// String id,
	// org.springframework.ui.Model model) throws Exception {
	// ProcessInstance processInstance = runtimeService // 获取运行时Service
	// // .startProcessInstanceById("process:1:17507","1234567890");
	// .startProcessInstanceByKey(key, id);
	//
	// // 设置下一步执行者
	// String taskId = processInstance.getActivityId();
	// Task task =
	// taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(taskId)
	// .singleResult();
	// model.addAttribute("processId", processInstance.getId());
	// model.addAttribute("taskId", task.getId());
	//
	// // 分配到个人
	// taskService.claim(task.getId(), "wangbaoqiang");
	//
	// // actTaskService.assignationTaskUser(processInstance.getId(), taskId,
	// // "张三");
	// return "activiti/process/edit";
	// }
	/**
	 * 导出model对象为指定类型
	 *
	 * @param modelId
	 *            模型ID
	 * @param type
	 *            导出文件类型(bpmn\json)
	 */

	// @RequiresPermissions("app:flow:model:export")
	// @RequestMapping(value = "export/{modelId}/{type}")
	// public void export(@PathVariable("modelId") String modelId,
	// @PathVariable("type") String type,
	// HttpServletResponse response) {
	// try {
	// Model modelData = repositoryService.getModel(modelId);
	// BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
	// byte[] modelEditorSource =
	// repositoryService.getModelEditorSource(modelData.getId());
	//
	// JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
	// BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
	//
	// // 处理异常
	// if (bpmnModel.getMainProcess() == null) {
	// response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
	// response.getOutputStream().println("no main process, can't export for type: "
	// + type);
	// response.flushBuffer();
	// return;
	// }
	//
	// String filename = "";
	// byte[] exportBytes = null;
	//
	// String mainProcessId = bpmnModel.getMainProcess().getId();
	//
	// if (type.equals("bpmn")) {
	//
	// BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
	// exportBytes = xmlConverter.convertToXML(bpmnModel);
	//
	// filename = mainProcessId + ".bpmn20.xml";
	// } else if (type.equals("json")) {
	// exportBytes = modelEditorSource;
	// filename = mainProcessId + ".json";
	// }
	//
	// ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
	// IOUtils.copy(in, response.getOutputStream());
	//
	// response.setHeader("Content-Disposition", "attachment; filename=" +
	// filename);
	// response.flushBuffer();
	// } catch (Exception e) {
	// logger.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
	// }
	// }
	//
	// @RequestMapping("DefEnable")
	// public String enableDef(@RequestParam("processDefinitionId") String
	// processDefinitionId) {
	// ProcessDefinition processDefinition =
	// repositoryService.getProcessDefinition(processDefinitionId);
	//
	// String key = processDefinition.getKey();
	//
	// List<ProcessDefinition> definitions =
	// repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
	// .list();
	// // 挂起全部定义
	// for (ProcessDefinition Def : definitions) {
	// if (!Def.isSuspended()) {
	// repositoryService.suspendProcessDefinitionById(Def.getId());
	// }
	// }
	// // repositoryService.suspendProcessDefinitionByKey(key);
	// // 激活特定定义
	// repositoryService.activateProcessDefinitionById(processDefinitionId);
	// return "json";
	// }

	// 流程定于id 生成的流程图
	@ApiOperation(value = "获取流程定义列表", notes = "所需参数：processKey(过滤关键参数条件)")
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

	// 流程定于id 生成模型定义的流程图
	@ApiOperation(value = "生成模型定义的流程图", notes = "所需参数：modelId(模型id)")
	@RequestMapping(value = "/resource/readModel")
	public void imgModel(@RequestParam("modelId") String modelId, HttpServletResponse response) throws Exception {

		Model modelData = repositoryService.getModel(modelId);
		ObjectNode modelNode = null;

		try {
			modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
		InputStream inputStream = processDiagramGenerator.generatePngDiagram(model);

		byte[] b = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}

		// OutputStream out = response.getOutputStream();
		// for (int b = -1; (b = inputStream.read()) != -1;) {
		// out.write(b);
		// }
		// out.close();
		inputStream.close();
		/////
	}
	///////////
	// @RequestMapping(value = "/resource/read2")
	// public String loadByDeployment2(@RequestParam("processDefinitionId") String
	// processDefinitionId,
	// @RequestParam("resourceType") String resourceType, HttpServletResponse
	// response,
	// org.springframework.ui.Model model) throws Exception {
	// ProcessDefinition processDefinition =
	// repositoryService.createProcessDefinitionQuery()
	// .processDefinitionId(processDefinitionId).singleResult();
	// String resourceName = "";
	// if (resourceType.equals("image")) {
	// resourceName = processDefinition.getDiagramResourceName();
	// } else if (resourceType.equals("xml")) {
	// resourceName = processDefinition.getResourceName();
	// }
	// InputStream resourceAsStream =
	// repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
	// resourceName);
	// byte[] b = new byte[1024];
	// int len = -1;
	// while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	// response.getOutputStream().write(b, 0, len);
	// }
	// return "";
	// }

	@ApiOperation(value = "部署流程", notes = "所需参数：modelId(模型id)")
	@RequestMapping("deploy4")
	public String deploy4(@RequestParam(value = "modelId") String modelId) throws JsonProcessingException, IOException {
		String json = "{}";
		try {
			// 获取流程中的表单信息
			// 流程model
			Model modelData = repositoryService.createModelQuery().modelId(modelId).singleResult();
			// 流程KEY
			String key = modelData.getKey();
			// 读取model

			// ObjectNode modelNode;
			// modelNode = (ObjectNode) new ObjectMapper()
			// .readTree(repositoryService.getModelEditorSource(modelData.getId()));
			// ObjectNode processs = (ObjectNode) modelNode.findValue("properties");
			// processs.put("process_id", key);
			////////////////////////////////////////
			/////////////////////////////////////////
			// JSONArray[] chards= //JSON.parseArray(modelNode.childShapes)
			// 获取全部子流程的用户节点，然后读取数据，拼装
			List<JSONObject> usertasks = new ArrayList<JSONObject>();

			String jsontext = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()))
					.toString();
			JSONObject jsonObject = JSONObject.parseObject(jsontext);

			JSONArray jsonArrs = jsonObject.getJSONArray("childShapes");
			for (int i = 0; i < jsonArrs.size(); i++) {
				JSONObject chart = jsonArrs.getJSONObject(i);
				// chart.get("stencil")
				if ("SubProcess".equals(chart.getJSONObject("stencil").get("id"))) {
					JSONArray Arrs = chart.getJSONArray("childShapes");
					for (int j = 0; j < Arrs.size(); j++) {
						JSONObject ustasks = Arrs.getJSONObject(j);
						if ("UserTask".equals(ustasks.getJSONObject("stencil").get("id"))) {
							usertasks.add(ustasks);
						}
					}
				}
			}

			/////////////////////////////////////////////////
			// 生成新的model
			BpmnModel newModel = new BpmnModel();
			Process process = new Process();
			// newModel.addProcess(process);
			process.setId(key);
			// 开始节点的属性
			StartEvent startEvent = new StartEvent();
			startEvent.setId("start");
			startEvent.setName("开始");
			// 结束节点属性
			EndEvent endEvent = new EndEvent();
			endEvent.setId("end");
			endEvent.setName("结束");
			// 用户任务
			// documentation overrideid //name
			List<UserTask> tasks = new ArrayList<UserTask>();
			for (int i = 0; i < usertasks.size(); i++) {
				JSONObject chart = usertasks.get(i).getJSONObject("properties");
				UserTask task = new UserTask();
				if (chart.get("overrideid") != null && "".equals(chart.get("overrideid"))) {
					task.setId(chart.get("overrideid").toString());
				} else {
					// String id = UUID.randomUUID().toString().replaceAll("-", "");
					task.setId("utask" + i);
				}

				task.setName(chart.get("name").toString());
				task.setDocumentation(chart.get("documentation").toString());
				tasks.add(task);
			}
			if (tasks.isEmpty()) {
				return "操作失败,流程文件不符合规范,缺少必要服务";
			}
			// 连线信息
			List<SequenceFlow> sequenceFlows = new ArrayList<SequenceFlow>();
			for (int i = 0; i < tasks.size(); i++) {

				SequenceFlow s1 = new SequenceFlow();
				// String id = UUID.randomUUID().toString().replaceAll("-", "");
				s1.setId("line" + i);
				// s1.setName("");
				if (i == 0) {
					s1.setSourceRef("start");
					s1.setTargetRef(tasks.get(i).getId());
				} else {
					// System.out.println(tasks.get((i-1)).getId());
					s1.setSourceRef(tasks.get((i - 1)).getId());
					s1.setTargetRef(tasks.get(i).getId());
				}

				if (i == (tasks.size() - 1)) {
					SequenceFlow send = new SequenceFlow();
					// String endid = UUID.randomUUID().toString().replaceAll("-", "");
					send.setId("endline" + i);
					send.setSourceRef(tasks.get(i).getId());
					send.setTargetRef("end");
					sequenceFlows.add(send);
				}

				sequenceFlows.add(s1);
			}
			// Process对象
			process.addFlowElement(startEvent);
			for (int i = 0; i < tasks.size(); i++) {
				process.addFlowElement(tasks.get(i));
			}
			for (int i = 0; i < sequenceFlows.size(); i++) {
				process.addFlowElement(sequenceFlows.get(i));
			}
			process.addFlowElement(endEvent);
			newModel.addProcess(process);
			///////////////////////////////////////////////////
			// byte[] bpmnBytes = null;
			//
			// BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			// bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			// DeploymentBuilder db =
			// repositoryService.createDeployment().name(modelData.getName());
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
			// 3. 部署这个BPMN模型
			Deployment deployment = repositoryService.createDeployment().addBpmnModel("dynamic-model.bpmn", newModel)
					.name("Dynamic process deployment").deploy();
			// Deployment deployment = db.addString(modelData.getName() + ".bpmn20.xml", new
			// String(bpmnBytes, "utf-8"))
			// .deploy();

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
		// model1.addAttribute("json", json);
		return "json";
	}

}
