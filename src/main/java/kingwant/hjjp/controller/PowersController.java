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
import kingwant.hjjp.entity.Powers;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.PowersMapper;
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
@RequestMapping("/powers")
public class PowersController {
	
	@Autowired
	private PowersMapper powersMapper;
	
	@PostMapping("/addPower")
	@ApiOperation(value = "添加权限", notes = "所需参数：name(名称);code(代码),weight(权重),pid(父节点id,根节点为-1)")
	public PublicResult<Map<String, Object>> addUser(@ValidationParam("name,code,weight,pid")@RequestBody Powers powers) {
        //参数校验
        if (ComUtil.isEmpty(powers.getName()) || ComUtil.isEmpty(powers.getCode())|| ComUtil.isEmpty(powers.getWeight())
        		|| ComUtil.isEmpty(powers.getPid())) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("名称，代码，权重，父节点均不能为空", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }
        powers.setId(KwHelper.newID());
        powersMapper.insert(powers);
        Map<String, Object> map=new HashMap<>();
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);		
    }
	
	@DeleteMapping("/delPower")
	@ApiOperation(value = "删除权限", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> delUserById(String id) {
		Map<String, Object> map=new HashMap<>();
		try {
			powersMapper.deleteById(id);
			map.put("删除成功！！", true);
		} catch (Exception e) {
			map.put("删除失败！！", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找权限", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> findById( String id) {
        if (ComUtil.isEmpty(id)  ) {
        	Map<String, Object> map=new HashMap<>();
        	map.put("缺少关键参数", false);
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
        }        
		Powers powers=powersMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", powers);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找权限列表", notes = "所需可选参数：name(权限名称),code(权限代码),pid(父节点id)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Powers powers) {
		EntityWrapper<Powers> ew=new EntityWrapper<Powers>();
	    ew.setEntity(new Powers());
		ew.like(!KwHelper.isNullOrEmpty(powers.getName()), "name", powers.getName());
		ew.eq(!KwHelper.isNullOrEmpty(powers.getCode()), "code", powers.getCode());
		ew.eq(!KwHelper.isNullOrEmpty(powers.getPid()), "pid", powers.getPid());

		List<Powers> list = powersMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("成功", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updatePowers")
	@ApiOperation(value = "修改权限", notes = "所需参数：id(必要，其他的为选填);name(名字);code(代码),weight(权重),pid(父节点id)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody Powers powers) {
		Map<String, Object> map=new HashMap<>();
		if(KwHelper.isNullOrEmpty(powers.getId())) {
			map.put("权限信息id不能为空", false);
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, map);
		}
		try {
			powersMapper.updateById(powers);
		} catch (Exception e) {
			map.put("操作数据失败", false);
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, map);
		}
        map.put("成功", true);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

}

