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
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.RoleMapper;
import kingwant.hjjp.util.ComUtil;
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
@Api(tags = "角色")
@RestController
@RequestMapping("/role")
public class RoleController {
	

	
	@Autowired
	private RoleMapper roleMapper;
	
	@PostMapping("/addRole")
	@ApiOperation(value = "添加角色", notes = "所需参数：name(名字);des(描述);powers(权限)")
	public PublicResult<Map<String, Object>> addRole(@RequestBody kingwant.hjjp.entity.Role role) {
		
        if (KwHelper.isNullOrEmpty(role.getName())) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        role.setCrtime(new Date());
        role.setId(KwHelper.newID());
        roleMapper.insert(role);
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	@DeleteMapping("/delRoleById")
	@ApiOperation(value = "删除角色", notes = "所需参数：id(角色id)")
	public PublicResult<Map<String, Object>> delRoleById(String id) {
		if (ComUtil.isEmpty(id)  ) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
		try {
			roleMapper.deleteById(id);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找角色", notes = "所需参数：id(角色id)")
	public PublicResult<Map<String, Object>> findById( String id) {
		//String name = requestJson.getString("name");
		//参数校验
        if (ComUtil.isEmpty(id)  ) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
		kingwant.hjjp.entity.Role role=roleMapper.selectById(id);
        Map<String, Object> map=new HashMap<>();
        map.put("data", role);
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
		ew.orderBy("crtime", false);
		List<kingwant.hjjp.entity.Role> list = roleMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateRole")
	@ApiOperation(value = "修改角色", notes = "所需参数：id(必要，其他的为选填);name(名字);des(描述);powers(权限)")
	public PublicResult<Map<String, Object>> updateRole(@RequestBody kingwant.hjjp.entity.Role role) {
		User user4update=new User();
		if(KwHelper.isNullOrEmpty(role.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			roleMapper.updateById(role);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

	@PostMapping("/changePower")
	@ApiOperation(value = "添加或者取消权限", notes = "所需参数：rid(角色id);powerId(权限id或者权限code);flag(布尔型，添加或者取消)")
	public PublicResult<Map<String, Object>> changePower(String rid,String powerId,Boolean flag) {
		kingwant.hjjp.entity.Role role4temp=roleMapper.selectById(rid);
		if(role4temp==null) {
			 return new PublicResult<>("无当前角色", null);
		}
		String power=role4temp.getPowers();
		if(flag) {
			//新增该权限
			if(!KwHelper.isNullOrEmpty(power) && power.indexOf(powerId)>=0) {
				return new PublicResult<>("该角色已拥有该权限", null);
			}else {
				String newPower="";
				if(!KwHelper.isNullOrEmpty(power) && power.length()>0) {
					newPower=power+","+powerId;
				}else {
					newPower=powerId;
				}
				power+=power+","+powerId;
				role4temp.setPowers(newPower);
				roleMapper.updateById(role4temp);
				return new PublicResult<>(PublicResultConstant.SUCCESS, null);
			}
		}else {
			//移除该权限
			int deleteFlag=power.indexOf(powerId);
			switch (deleteFlag)
			{
			    case -1:return new PublicResult<>("该角色当前无该权限", null);
			    case 0:if(power.indexOf(",")>0) {
			    	   String newPower=power.replace(powerId+",", "");
			           role4temp.setPowers(newPower);
			           roleMapper.updateById(role4temp);
			           return new PublicResult<>(PublicResultConstant.SUCCESS, null);
			    }else {
			    	   String newPower=power.replace(powerId, "");
			           role4temp.setPowers(newPower);
			           roleMapper.updateById(role4temp);
			           return new PublicResult<>(PublicResultConstant.SUCCESS, null);
					
				}
			    default:String newPower1=power.replace(","+powerId, "");
			           role4temp.setPowers(newPower1);
			           roleMapper.updateById(role4temp);
		               return new PublicResult<>(PublicResultConstant.SUCCESS, null);
			}
		}
    }


}

