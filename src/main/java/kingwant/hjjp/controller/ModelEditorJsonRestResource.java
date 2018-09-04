/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
 
package kingwant.hjjp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiOperation;

/**
 * @author Tijs Rademakers
 */
@RestController
//@RequestMapping(value="/activiti-service")
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
  
  @Autowired
  private RepositoryService repositoryService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @RequestMapping(value="/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
  public ObjectNode getEditorJson(@PathVariable String modelId) {
    ObjectNode modelNode = null;
    
    Model model = repositoryService.getModel(modelId);
      
   if (model != null) {
      try {
        if (StringUtils.isNotEmpty(model.getMetaInfo())) {
          modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
        } else {
          modelNode = objectMapper.createObjectNode();
          modelNode.put(MODEL_NAME, model.getName());
        }
        modelNode.put(MODEL_ID, model.getId());
        ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
            new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
        modelNode.put("model", editorJsonNode);
        
      } catch (Exception e) {
        LOGGER.error("Error creating model JSON", e);
        throw new ActivitiException("Error creating model JSON", e);
      }
    }
    return modelNode;
  }
  
  @ApiOperation(value = "保存流程模型", notes = "model/modelid(根据需要保存的模型的id改变)/save")
  @RequestMapping(value="model/{modelId}/save", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.OK)
  public void saveModel(@PathVariable String modelId, @RequestParam("name") String name,
          @RequestParam("json_xml") String json_xml, @RequestParam("svg_xml") String svg_xml,
          @RequestParam("description") String description) {
	  try {

	      Model model = repositoryService.getModel(modelId);

	      ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

	      modelJson.put(MODEL_NAME, name);
	      modelJson.put(MODEL_DESCRIPTION, description);
	      model.setMetaInfo(modelJson.toString());
	      model.setName(name);

	      repositoryService.saveModel(model);

	      repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

	      InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
	      TranscoderInput input = new TranscoderInput(svgStream);

	      PNGTranscoder transcoder = new PNGTranscoder();
	      // Setup output
	      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	      TranscoderOutput output = new TranscoderOutput(outStream);

	      // Do the transformation
	      transcoder.transcode(input, output);
	      final byte[] result = outStream.toByteArray();
	      repositoryService.addModelEditorSourceExtra(model.getId(), result);
	      outStream.close();

	    } catch (Exception e) {
	      LOGGER.error("保存失败", e);
	      throw new ActivitiException("保存失败", e);
	    }
  }
  
  
//  /**
//   * 创建模型
//   */
//  @RequestMapping(value = "create1", method = RequestMethod.POST)
//  public void create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,
//                     HttpServletRequest request, HttpServletResponse response) {
//      try {
//          ObjectMapper objectMapper = new ObjectMapper();
//          ObjectNode editorNode = objectMapper.createObjectNode();
//          editorNode.put("id", "canvas");
//          editorNode.put("resourceId", "canvas");
//          ObjectNode stencilSetNode = objectMapper.createObjectNode();
//          stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
//          editorNode.put("stencilset", stencilSetNode);
//          Model modelData = repositoryService.newModel();
//
//          ObjectNode modelObjectNode = objectMapper.createObjectNode();
//          modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
//          modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
//          description = StringUtils.defaultString(description);
//          modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
//          modelData.setMetaInfo(modelObjectNode.toString());
//          modelData.setName(name);
//          modelData.setKey(StringUtils.defaultString(key));
//
//          repositoryService.saveModel(modelData);
//          repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
//
//          response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
//      } catch (Exception e) {
//      	System.out.println("创建失败《�?��?�原因未�?");
//      }
//  }
  
  @RequestMapping(value="/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public @ResponseBody String getStencilset() {
    InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("static/stencilset.json");
    try {
      return IOUtils.toString(stencilsetStream, "utf-8");
    } catch (Exception e) {
      throw new ActivitiException("Error while loading stencil set", e);
    }
  }
}
