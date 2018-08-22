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
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.RoleMapper;
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
@RestController
@RequestMapping("/role")
public class RoleController {
	

	
	@Autowired
	private RoleMapper roleMapper;
	
	@PostMapping("/addRole")
	@ApiOperation(value = "添加角色", notes = "所需参数：name(名字);des(描述)")
	public PublicResult<Map<String, Object>> addUser(@RequestBody kingwant.hjjp.entity.Role role) {
		
        if (KwHelper.isNullOrEmpty(role.getName())) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("角色名不能为空！！", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        role.setId(KwHelper.newID());
        roleMapper.insert(role);
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	
	@DeleteMapping("/delRoleById")
	@ApiOperation(value = "删除角色", notes = "所需参数：id(角色id)")
	public PublicResult<Map<String, Object>> delUserById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			roleMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找角色", notes = "所需参数：id(角色id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
		kingwant.hjjp.entity.Role role=roleMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", role);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找用户列表", notes = "所需可选参数：name(角色名称) ")
	public PublicResult<Map<String, Object>> findByList(@RequestBody kingwant.hjjp.entity.Role role) {
        //参数校验
		EntityWrapper<kingwant.hjjp.entity.Role> ew=new EntityWrapper<kingwant.hjjp.entity.Role>();
	    ew.setEntity(new kingwant.hjjp.entity.Role());
		ew.like(!KwHelper.isNullOrEmpty(role.getName()), "name", role.getName());
		ew.like(!KwHelper.isNullOrEmpty(role.getDes()), "des", role.getDes());
		List<kingwant.hjjp.entity.Role> list = roleMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateRole")
	@ApiOperation(value = "修改角色", notes = "所需参数：id(必要，其他的为选填);name(名字);des(描述)")
	public PublicResult<Map<String, Object>> updateRole(@RequestBody kingwant.hjjp.entity.Role role) {
		User user4update=new User();
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(role.getId())) {
			Object message=false;
			map.put("用户信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			roleMapper.updateById(role);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
			// TODO: handle exception
		}

        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }



}

