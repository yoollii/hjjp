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
import kingwant.hjjp.entity.Institutions;
import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.InstitutionMapper;
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
//@Controller
//@RequestMapping("/user")
@Api(tags = "所属机构")
@RestController
@RequestMapping("/institu")
public class InstitutionController {
		
	@Autowired
	private InstitutionMapper institutionMapper;
	
	@PostMapping("/addInstitution")
	@ApiOperation(value = "添加机构", notes = "所需参数：name(名字必填);des(描述),orders(排序),code(机构代码必填),tel(机构电话)")
	public PublicResult<Map<String, Object>> addInstitution(@ValidationParam("name,code")@RequestBody JSONObject requestJson) {
		String name = requestJson.getString("name");
        String des = requestJson.getString("des");
        Integer orders = requestJson.getInteger("orders");  
        String code=requestJson.getString("code");
        String tel=requestJson.getString("tel");
        //参数校验
        Institutions institutions=new Institutions();
        institutions.setId(KwHelper.newID());
        institutions.setName(name);
        institutions.setDes(des);
        institutions.setOrders(orders);
        institutions.setCode(code);
        institutions.setTel(tel);
        institutions.setCrtime(new Date());
        institutionMapper.insert(institutions);
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@DeleteMapping("/delById")
	@ApiOperation(value = "删除机构", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> delById(String id) {
		if(KwHelper.isNullOrEmpty(id)) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			institutionMapper.deleteById(id);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
		
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "查找机构", notes = "所需参数：id(用户id)")
	public PublicResult<Map<String, Object>> findById( String id) {
        if (ComUtil.isEmpty(id)  ) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
        Institutions institution=institutionMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", institution);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找机构列表", notes = "所需可选参数：name(机构名称(模糊)),code(机构代码（精确）)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Institutions institutions) {
        //参数校验
		EntityWrapper<Institutions> ew=new EntityWrapper<Institutions>();
	    ew.setEntity(new Institutions());
		ew.like(!KwHelper.isNullOrEmpty(institutions.getName()), "name", institutions.getName());
		ew.eq(!KwHelper.isNullOrEmpty(institutions.getCode()), "code", institutions.getCode());
		ew.orderBy("crtime", false);
		List<Institutions> list = institutionMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateInstitutions")
	@ApiOperation(value = "修改机构", notes = "所需参数：id(必要，其他的为选填);name(名字);des(描述),orders(排序),code(代码),tel(电话)")
	public PublicResult<Map<String, Object>> updateUser(@RequestBody Institutions institutions) {
		if(KwHelper.isNullOrEmpty(institutions.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			institutionMapper.updateById(institutions);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

}

