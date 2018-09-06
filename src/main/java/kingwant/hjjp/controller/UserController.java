package kingwant.hjjp.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.entity.Userinstitution;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.mapper.UserinstitutionMapper;
import kingwant.hjjp.util.KwHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
//@Controller
//@RequestMapping("/user")
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {
		
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserinstitutionMapper userinstitutionMapper;
	
	@PostMapping("/addUser")
	@ApiOperation(value = "添加用户", notes = "所需参数：name(名字);state(状态),group(分组名),rid(角色id),institutionsId(机构id)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("name,state,group,rid")@RequestBody JSONObject requestJson) {
		String name = requestJson.getString("name");
        int state = requestJson.getInteger("state");
        String group = requestJson.getString("group");  
        String rid = requestJson.getString("rid");
        //参数校验
        if (ComUtil.isEmpty(name) || ComUtil.isEmpty(state)|| ComUtil.isEmpty(group)) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        User user=new User();
		user.setId(KwHelper.newID());
		user.setName(name);
		user.setState(state);
		user.setGroupName(group);
		user.setRid(rid);
		user.setCrtime(new Date());
		userMapper.insert(user);
		
		String institutionsId=requestJson.getString("institutionsId");
		if(!KwHelper.isNullOrEmpty(institutionsId) && !institutionsId.equals("-1")) {
			Userinstitution userinstitution=new Userinstitution();
			userinstitution.setIid(institutionsId);
			userinstitution.setUid(user.getId());
			userinstitution.setId(KwHelper.newID());
			userinstitutionMapper.insert(userinstitution);
		}
        
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@DeleteMapping("/delById")
	@ApiOperation(value = "删除用户", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> delById(String id) {
		if(KwHelper.isNullOrEmpty(id)) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			userMapper.deleteById(id);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找用户", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> findById( String id) {
        if (ComUtil.isEmpty(id)  ) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
		User user=userMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", user);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找用户列表", notes = "所需可选参数：name(用户名称),state(用户状态),groupName(分组名称),rid(角色id)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody User user) {
        //参数校验
		EntityWrapper<User> ew=new EntityWrapper<User>();
	    ew.setEntity(new User());
		ew.like(!KwHelper.isNullOrEmpty(user.getName()), "name", user.getName());
		try {
			ew.where(!KwHelper.isNullOrEmpty(user.getState().toString()), "state = {0}", user.getState())
			.andNew(!KwHelper.isNullOrEmpty(user.getGroupName()), "groupName = {0}", user.getGroupName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		ew.orderBy("crtime", false);
		ew.eq(!KwHelper.isNullOrEmpty(user.getRid()), "rid", user.getRid());
		List<User> list = userMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateUser")
	@ApiOperation(value = "修改用户", notes = "所需参数：id(必要，其他的为选填);name(名字);state(状态),groupName(分组名),rid(角色id)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody User user) {
		User user4update=new User();
		if(KwHelper.isNullOrEmpty(user.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			userMapper.updateById(user);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

}

