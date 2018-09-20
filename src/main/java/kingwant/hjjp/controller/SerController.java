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
import kingwant.hjjp.entity.Ser;
import kingwant.hjjp.mapper.SerMapper;
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
//服务
@Api(tags = "服务")
@RestController
@RequestMapping("/ser")
public class SerController {
	
	@Autowired
	private SerMapper serMapper;
	
	@PostMapping("/addSer")
	@ApiOperation(value = "添加服务", notes = "所需参数：name(名字);state(状态);des(描述);orders(排序);urlFlag(路径标识);url(路径地址);inNum;outNum")
	public PublicResult<Map<String, Object>> addSer(@ValidationParam("name,state,group")@RequestBody Ser ser) {
		        
        if (ComUtil.isEmpty(ser.getName())) {
        	//Object aObject="名称，模型id，状态均不能为空";
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        ser.setCrtime(new Date());
        ser.setId(KwHelper.newID());
        serMapper.insert(ser);
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@DeleteMapping("/delById")
	@ApiOperation(value = "删除服务", notes = "所需参数：id(服务id)")
	public PublicResult<Map<String, Object>> delById(String id) {
		try {
			serMapper.deleteById(id);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
			// TODO: handle exception
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
	
	@GetMapping("/findById")
	@ApiOperation(value = "根据id查找服务", notes = "所需参数：id(服务id)")
	public PublicResult<Map<String, Object>> findById( String id) {
        if (ComUtil.isEmpty(id)) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }        
		Ser ser=serMapper.selectById(id);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", ser);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);	
    }
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找服务列表", notes = "所需可选参数：name(服务名称);urlFlag(服务路径标识);state(状态)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Ser ser) {
        //参数校验
		EntityWrapper<Ser> ew=new EntityWrapper<Ser>();
	    ew.setEntity(new Ser());
		ew.like(!KwHelper.isNullOrEmpty(ser.getName()), "name", ser.getName());
		try {
			ew.eq(!KwHelper.isNullOrEmpty(ser.getUrlFlag().toString()), "useFlag", ser.getUrlFlag());
			ew.eq(!KwHelper.isNullOrEmpty(ser.getState().toString()), "state", ser.getState());
		} catch (Exception e) {
//			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
		ew.orderBy("crtime", false);
		List<Ser> list = serMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }

	@PutMapping("/updateSer")
	@ApiOperation(value = "修改服务", notes = "所需参数：id(必要，其他的为选填);name(名字);state(状态);des(备注);orders(排序);urlFlag(连接标识),url(路径),inNum,outNum")
	public PublicResult<Map<String, Object>> updateSer(@RequestBody Ser ser) {
		if(KwHelper.isNullOrEmpty(ser.getId())) {
			return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
		}
		try {
			serMapper.updateById(ser);
		} catch (Exception e) {
			return new PublicResult<>(PublicResultConstant.SQL_EXCEPTION, null);
		}
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }
}

