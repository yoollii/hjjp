package kingwant.hjjp.controller;

import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import xyz.michaelch.mchtools.MCHException;

@Api(tags = "Activiti控制器")
@Controller
@RequestMapping("/activiti2")
public class Activiti2Controller {
	
	@Autowired
	RepositoryService repositoryService;
	
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
	  return "modeler.html";
	}

}
