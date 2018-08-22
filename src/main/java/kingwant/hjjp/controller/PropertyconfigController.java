package kingwant.hjjp.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Propertyconfig;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.PropertyconfigMapper;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
@RestController
@RequestMapping("/propertyconfig")
public class PropertyconfigController {
	
	@Autowired
	private PropertyconfigMapper propertyconfigMapper;
	
	@PostMapping("/addProperty")
	@ApiOperation(value = "添加属性设置", notes = "所需参数：dateMap(流程数据映射);inConfig(输入配置);outConfig(输出配置);serId(服务id);modelId(模型id);flowId(流程id);taskId(taskId流程中需要)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("dateMap,inConfig,outConfig")@RequestBody Propertyconfig propertyconfig) {
        if (ComUtil.isEmpty(propertyconfig.getDataMap()) || ComUtil.isEmpty(propertyconfig.getInConfig())
        		|| ComUtil.isEmpty(propertyconfig.getOutConfig())) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("流程数据映射，输入配置，输出配置均不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        propertyconfig.setId(KwHelper.newID());
        propertyconfigMapper.insert(propertyconfig);
        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@DeleteMapping("/delPropertyConfigById")
	@ApiOperation(value = "删除属性设置", notes = "所需参数：id(属性id)")
	public PublicResult<Map<String, Object>> delPropertyConfigById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			propertyconfigMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找属性设置", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
        Propertyconfig Propertyconfig=propertyconfigMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", Propertyconfig);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找属性设置列表", notes = "所需参数：dateMap(流程数据映射);inConfig(输入配置);outConfig(输出配置);serId(服务id);modelId(模型id);flowId(流程id);taskId(taskId流程中需要)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Propertyconfig Propertyconfig) {
        //参数校验
		EntityWrapper<Propertyconfig> ew=new EntityWrapper<Propertyconfig>();
	    ew.setEntity(new Propertyconfig());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getDataMap()), "dateMap", Propertyconfig.getDataMap());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getInConfig()), "inConfig", Propertyconfig.getInConfig());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getOutConfig()), "outConfig", Propertyconfig.getOutConfig());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getSerId()), "serId", Propertyconfig.getSerId());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getModelId()), "modelId", Propertyconfig.getModelId());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getFlowId()), "flowId", Propertyconfig.getFlowId());
		ew.like(!KwHelper.isNullOrEmpty(Propertyconfig.getTaskId()), "taskId", Propertyconfig.getTaskId());
		
		List<Propertyconfig> list = propertyconfigMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateUser")
	@ApiOperation(value = "修改用户", notes = "所需参数：dateMap(流程数据映射);inConfig(输入配置);outConfig(输出配置);serId(服务id);modelId(模型id);flowId(流程id);taskId(taskId流程中需要)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody  Propertyconfig Propertyconfig) {
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(Propertyconfig.getId())) {
			Object message=false;
			map.put("属性设置信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			propertyconfigMapper.updateById(Propertyconfig);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}

        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }



}

