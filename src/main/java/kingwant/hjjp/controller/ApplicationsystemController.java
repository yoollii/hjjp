package kingwant.hjjp.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Applicationsystem;
import kingwant.hjjp.mapper.ApplicationsystemMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
@Api(tags = "应用系统")
@RestController
@RequestMapping("/applicationsystem")
public class ApplicationsystemController {
	
	@Autowired
	private ApplicationsystemMapper appliMapper;
	
	@PostMapping("/addAppli")
	@ApiOperation(value = "添加应用系统", notes = "所需参数：name(名字，必填);userFlag(应用标识);des(描述);baseUrl(基础地址);modelId(流程模板，必填);state(状态)")
	public PublicResult<Map<String, Object>> addAppli(@ValidationParam("name,modelId")@RequestBody Applicationsystem applicationsystem) {
		        
        if (ComUtil.isEmpty(applicationsystem.getName()) || ComUtil.isEmpty(applicationsystem.getModelId())||
        		ComUtil.isEmpty(applicationsystem.getState())) {
        	//Object aObject="名称，模型id，状态均不能为空";
//        	Map<String, Object> map=new HashMap<>();
//        	map.put("flag", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        applicationsystem.setCrtime(new Date());
        applicationsystem.setId(KwHelper.newID());
        appliMapper.insert(applicationsystem);
        
        Map<String, Object> map=new HashMap<>();
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@DeleteMapping("/delAppliById")
	@ApiOperation(value = "删除应用系统", notes = "所需参数：id(应用系统id)")
	public PublicResult<Map<String, Object>> delAppliById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			appliMapper.deleteById(id);
			map.put("flag", true);
		} catch (Exception e) {
			map.put("flag", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "根据id查找应用系统", notes = "所需参数：id(应用系统id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
		Applicationsystem applicationsystem=appliMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", applicationsystem);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找应用系统列表", notes = "所需可选参数：name(用户名称),useFlag(应用标识),modelId(模型id),state(状态)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Applicationsystem applicationsystem) {
        //参数校验
		EntityWrapper<Applicationsystem> ew=new EntityWrapper<Applicationsystem>();
	    ew.setEntity(new Applicationsystem());
		ew.like(!KwHelper.isNullOrEmpty(applicationsystem.getName()), "name", applicationsystem.getName());
		try {
			ew.eq(!KwHelper.isNullOrEmpty(applicationsystem.getUseFlag().toString()), "useFlag", applicationsystem.getUseFlag());
			ew.eq(!KwHelper.isNullOrEmpty(applicationsystem.getState().toString()), "state", applicationsystem.getState());
		} catch (Exception e) {
			// TODO: handle exception
		}
		ew.eq(!KwHelper.isNullOrEmpty(applicationsystem.getModelId()), "modelId", applicationsystem.getModelId());
		
		ew.orderBy("crtime", false);
		List<Applicationsystem> list = appliMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateAppli")
	@ApiOperation(value = "修改应用系统", notes = "所需参数：id(必要，其他的为选填);name(名字);useFlag(用户标识);des(备注);baseUrl(基本地址);modelId(模型id),state(状态)")
	public PublicResult<Map<String, Object>> updateAppli(@RequestBody Applicationsystem applicationsystem) {
		if(KwHelper.isNullOrEmpty(applicationsystem.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			appliMapper.updateById(applicationsystem);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
}

