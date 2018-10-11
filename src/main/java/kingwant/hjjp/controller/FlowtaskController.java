package kingwant.hjjp.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Flowmodel;
import kingwant.hjjp.entity.Flowtask;
import kingwant.hjjp.mapper.FlowmodelMapper;
import kingwant.hjjp.mapper.FlowtaskMapper;
import kingwant.hjjp.mapper.PowersMapper;
import kingwant.hjjp.mapper.RoleMapper;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.mapper.UserinstitutionMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
@Api(tags = "任务监控")
@RestController
@RequestMapping("/flowtask")
public class FlowtaskController {
	
	@Autowired
	private FlowtaskMapper flowtaskMapper;
	
	@PostMapping("/add")
	@ApiOperation(value = "添加任务", notes = "所需参数:name(名字,必填);type(类型),currentLink(当前环节),currentUser(当前执行者);"
			+ "handleTime(处理时间);dealAppliSys(处理应用系统);state(运行状态);des(描述);appliSysId(应用系统id,必填)")
	public PublicResult<Map<String, Object>> add(@ValidationParam("name,appliSysId")@RequestBody Flowtask flowtask) {
		
        if (ComUtil.isEmpty(flowtask.getName()) || ComUtil.isEmpty(flowtask.getAppliSysId())) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR,null);
        }
        flowtask.setId(KwHelper.newID());
        flowtask.setCrtime(new Date());
        flowtask.setState(1);
        try {
        	flowtaskMapper.insert(flowtask);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@DeleteMapping("/delById")
	@ApiOperation(value = "删除任务", notes = "所需参数：id(任务id)")
	public PublicResult<Map<String, Object>> delById(String id) {
		try {
			flowtaskMapper.deleteById(id);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找任务", notes = "所需参数：id(任务id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
        Flowtask flowtask=flowtaskMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", flowtask);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找任务列表", notes = "所需可选参数：name(任务名称),state(模板状态),cruser(模板创建者)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody JSONObject requestJson) {
        //参数校验
		EntityWrapper<Flowtask> ew=new EntityWrapper<Flowtask>();
	    ew.setEntity(new Flowtask());
	    try {
	    	ew.eq(!KwHelper.isNullOrEmpty(requestJson.getString("state")), "state", requestJson.getString("state"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		ew.like(!KwHelper.isNullOrEmpty(requestJson.getString("name")), "name",requestJson.getString("name"));
		
		ew.eq(!KwHelper.isNullOrEmpty(requestJson.getString("cruser")), "cruser",requestJson.getString("cruser"));
		ew.orderBy("crtime", false);
		List<Flowtask> list = flowtaskMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateById")
	@ApiOperation(value = "修改任务", notes = "所需参数:id(必填);name(名字);type(类型),currentLink(当前环节),currentUser(当前执行者);" + 
			"handleTime(处理时间);dealAppliSys(处理应用系统);state(运行状态);des(描述);appliSysId(应用系统id)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody Flowtask flowtask) {
		if(KwHelper.isNullOrEmpty(flowtask.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			flowtaskMapper.updateById(flowtask);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
}

