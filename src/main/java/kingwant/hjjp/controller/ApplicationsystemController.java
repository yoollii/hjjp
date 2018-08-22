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

import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.ApplicationsystemMapper;
import kingwant.hjjp.mapper.UserMapper;
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
@RestController
@RequestMapping("/applicationsystem")
public class ApplicationsystemController {
	

	
	@Autowired
	private ApplicationsystemMapper appliMapper;
	
	@PostMapping("/addUser")
	@ApiOperation(value = "添加用户", notes = "所需参数：name(名字);state(状态),group(分组名)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("name,state,group")@RequestBody JSONObject requestJson) {
		String name = requestJson.getString("name");
        int state = requestJson.getInteger("state");
        String group = requestJson.getString("group");        
        //参数校验
        if (ComUtil.isEmpty(name) || ComUtil.isEmpty(state)|| ComUtil.isEmpty(group)) {
        	Object aObject="用户名，状态，分组名不能为空";
        	Map<String, Object> map=new HashMap<>();
        	map.put("用户名，状态，分组名均不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        User user=new User();
		user.setId(KwHelper.newID());
		user.setName(name);
		user.setState(state);
		user.setGroupName(group);
		userMapper.insert(user);
        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@DeleteMapping("/delUserById")
	@ApiOperation(value = "删除用户", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> delUserById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			userMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找用户", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
		User user=userMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", user);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找用户列表", notes = "所需可选参数：name(用户名称),state(用户状态),group(分组名称)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody User user) {
        //参数校验
		EntityWrapper<User> ew=new EntityWrapper<User>();
	    ew.setEntity(new User());
		ew.like(!KwHelper.isNullOrEmpty(user.getName()), "name", user.getName());
		try {
			ew.where(!KwHelper.isNullOrEmpty(user.getState().toString()), "state = {0}", user.getState())
			.andNew(!KwHelper.isNullOrEmpty(user.getGroupName()), "groupName = {0}", user.getGroupName());
			
			System.out.println(ew.getSqlSegment());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<User> list = userMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateUser")
	@ApiOperation(value = "修改用户", notes = "所需参数：id(必要，其他的为选填);name(名字);state(状态),group(分组名)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody User user) {
		User user4update=new User();
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(user.getId())) {
			Object message=false;
			map.put("用户信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			userMapper.updateById(user);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}

        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }



}

