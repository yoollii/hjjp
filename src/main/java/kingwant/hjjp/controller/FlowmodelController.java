package kingwant.hjjp.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Flowmodel;
import kingwant.hjjp.mapper.FlowmodelMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;
import xyz.michaelch.mchtools.MCHException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
@Controller
@RequestMapping("/flowmodel")
public class FlowmodelController {
	
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
	private FlowmodelMapper flowmodelMapper;
	
	@RequestMapping(value = "create")
	public String create(String name,  String key, String description,
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

//			response.sendRedirect(
//					request.getContextPath() + "modeler.html?modelId=" + modelData.getId() + "&key=" + key);
			return "redirect:/modeler.html?modelId="+ modelData.getId() + "&key=" + key;

		} catch (Exception e) {
			throw new MCHException();
		}
	}

	@RequestMapping("/modeler")
	public String indexHtml() {
	  return "modeler";
	}
	
	@PostMapping("/addModel")
	@ApiOperation(value = "添加流程模板", notes = "所需参数：name(名字);state(状态),cruser(创建者),des(描述);orders(排序)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("name")@RequestBody Flowmodel flowmodel) {
		
        if (ComUtil.isEmpty(flowmodel.getName())) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("模板名称不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        flowmodel.setId(KwHelper.newID());
        flowmodelMapper.insert(flowmodel);
        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@DeleteMapping("/delFlowModelById")
	@ApiOperation(value = "删除模板", notes = "所需参数：id(模板id)")
	public PublicResult<Map<String, Object>> delUserById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			flowmodelMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找流程模板", notes = "所需参数：id(模板id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
        Flowmodel flowmodel=flowmodelMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", flowmodel);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找模板列表", notes = "所需可选参数：name(模板名称),state(模板状态),cruser(模板创建者)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody  Flowmodel flowmodel) {
        //参数校验
		EntityWrapper<Flowmodel> ew=new EntityWrapper<Flowmodel>();
	    ew.setEntity(new Flowmodel());
		ew.like(!KwHelper.isNullOrEmpty(flowmodel.getName()), "name", flowmodel.getName());
		ew.eq(!KwHelper.isNullOrEmpty(flowmodel.getState().toString()), "state", flowmodel.getState());
		ew.eq(!KwHelper.isNullOrEmpty(flowmodel.getCruser()), "cruser", flowmodel.getCruser());
		
		List<Flowmodel> list = flowmodelMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateFlowModel")
	@ApiOperation(value = "修改模板", notes = "所需参数：id(必要，其他的为选填);name(名字);state(状态);cruser(创建者);des(描述);oeders(排序)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody Flowmodel flowmodel) {
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(flowmodel.getId())) {
			map.put("模板信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			flowmodelMapper.updateById(flowmodel);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}

        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }
}

