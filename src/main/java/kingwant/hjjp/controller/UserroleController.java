package kingwant.hjjp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import kingwant.hjjp.entity.User;
import kingwant.hjjp.entity.Userrole;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.mapper.UserroleMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;

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
@Api(tags = "用户角色关系")
@RestController
@RequestMapping("/userrole")
public class UserroleController {
	
	@Autowired
	private UserroleMapper userroleMapper;
	
//	@GetMapping("/test")
	//@GetMapping 查询
	//@PostMapping 新增
	//@DeleteMapping 删除
	//@PutMapping 修改
	
	@PostMapping("/addUserRole")
	@ApiOperation(value = "添加用户角色关系", notes = "所需参数：uid(用户id);rid(角色id)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("uid,rid")@RequestBody Userrole userRole) {

		Map<String, Object> map=new HashMap<>();
        if (ComUtil.isEmpty(userRole.getRid()) || ComUtil.isEmpty(userRole.getUid())) {
        	map.put("用户id和角色id均不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        EntityWrapper<Userrole> ew=new EntityWrapper<Userrole>();
	    ew.setEntity(new Userrole());
	    ew.eq(!KwHelper.isNullOrEmpty(userRole.getUid()), "uid", userRole.getUid());
	    ew.eq(!KwHelper.isNullOrEmpty(userRole.getRid()), "rid", userRole.getRid());
        List<Userrole> list=userroleMapper.selectList(ew);
        if(!KwHelper.isNullOrEmpty(list)&& list.size()>0) {
        	map.put("当前用户角色关系已存在！！", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        
        userRole.setId(KwHelper.newID());
        userroleMapper.insert(userRole);    
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }

	
	@PostMapping("/delUserRole")
	@ApiOperation(value = "添加用户角色关系", notes = "所需参数：uid(用户id);rid(角色id)")
	public PublicResult<Map<String, Object>> delUserRole(@ValidationParam("uid,rid")@RequestBody Userrole userRole) {

		Map<String, Object> map=new HashMap<>();
        if (ComUtil.isEmpty(userRole.getRid()) || ComUtil.isEmpty(userRole.getUid())) {
        	map.put("用户id和角色id均不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        EntityWrapper<Userrole> ew=new EntityWrapper<Userrole>();
	    ew.setEntity(new Userrole());
	    ew.eq(!KwHelper.isNullOrEmpty(userRole.getUid()), "uid", userRole.getUid());
	    ew.eq(!KwHelper.isNullOrEmpty(userRole.getRid()), "rid", userRole.getRid());
        List<Userrole> list=userroleMapper.selectList(ew);
        if(KwHelper.isNullOrEmpty(list)&& list.size()<1) {
        	map.put("不存在当前用户-角色关系！！", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        String id4del=list.get(0).getId();
        userroleMapper.deleteById(id4del);   
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找用户-角色关系列表", notes = "所需可选参数：rid(角色id),uid(用户id)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Userrole userRole) {
        //参数校验
		EntityWrapper<Userrole> ew=new EntityWrapper<Userrole>();
	    ew.setEntity(new Userrole());
		ew.eq(!KwHelper.isNullOrEmpty(userRole.getUid()), "uid", userRole.getUid());
		ew.eq(!KwHelper.isNullOrEmpty(userRole.getRid()), "rid", userRole.getRid());
		List<Userrole> list = userroleMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }
}

