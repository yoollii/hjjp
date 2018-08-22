package kingwant.hjjp.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Applicationsystem;
import kingwant.hjjp.entity.Ser;
import kingwant.hjjp.mapper.ApplicationsystemMapper;
import kingwant.hjjp.mapper.SerMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//服务
@RestController
@RequestMapping("/ser")
public class SerController {
	
	@Autowired
	private SerMapper serMapper;
	
	@PostMapping("/addSer")
	@ApiOperation(value = "添加服务", notes = "所需参数：name(名字);state(状态);des(描述);oeders(排序);urlFlag(路径标识);url(路径地址)")
	public PublicResult<Map<String, Object>> addSer(@ValidationParam("name,state,group")@RequestBody Ser ser) {
		        
        if (ComUtil.isEmpty(ser.getName())) {
        	//Object aObject="名称，模型id，状态均不能为空";
        	Map<String, Object> map=new HashMap<>();
        	map.put("服务的名称不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        ser.setId(KwHelper.newID());
        serMapper.insert(ser);
        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@DeleteMapping("/delSerById")
	@ApiOperation(value = "删除服务", notes = "所需参数：id(服务id)")
	public PublicResult<Map<String, Object>> delSerById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			serMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "根据id查找服务", notes = "所需参数：id(服务id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
		Ser ser=serMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", ser);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找服务列表", notes = "所需可选参数：name(服务名称);urlFlag(服务路径标识);state(状态)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Ser ser) {
        //参数校验
		EntityWrapper<Ser> ew=new EntityWrapper<Ser>();
	    ew.setEntity(new Ser());
		ew.like(!KwHelper.isNullOrEmpty(ser.getName()), "name", ser.getName());
		ew.eq(!KwHelper.isNullOrEmpty(ser.getUrlFlag().toString()), "useFlag", ser.getUrlFlag());
		ew.eq(!KwHelper.isNullOrEmpty(ser.getState().toString()), "state", ser.getState());
		
		List<Ser> list = serMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateAppli")
	@ApiOperation(value = "修改服务", notes = "所需参数：id(必要，其他的为选填);name(名字);state(状态);des(备注);orders(排序);urlFlag(连接标识),url(路径)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody Ser ser) {
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(ser.getId())) {
			map.put("服务信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			serMapper.updateById(ser);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }


}

