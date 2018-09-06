package kingwant.hjjp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kingwant.hjjp.annotation.ValidationParam;
import kingwant.hjjp.base.PublicResult;
import kingwant.hjjp.base.PublicResultConstant;
import kingwant.hjjp.entity.Userinstitution;
import kingwant.hjjp.mapper.UserinstitutionMapper;
import kingwant.hjjp.util.ComUtil;
import kingwant.hjjp.util.KwHelper;

@Api(tags = "用户机构关系")
@RestController
@RequestMapping("/userInstitu")
public class UserInstitutionController {
	
	@Autowired
	private UserinstitutionMapper userinstitutionMapper;
	
//	@GetMapping("/test")
	//@GetMapping 查询
	//@PostMapping 新增
	//@DeleteMapping 删除
	//@PutMapping 修改
	
	@PostMapping("/addUserInstitution")
	@ApiOperation(value = "添加用户机构关系", notes = "所需参数：uid(用户id);iid(机构id)")
	public PublicResult<Map<String, Object>> addUserRole(@ValidationParam("uid,iid")@RequestBody Userinstitution userinstitution) {

        if (ComUtil.isEmpty(userinstitution.getIid()) || ComUtil.isEmpty(userinstitution.getUid())) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        EntityWrapper<Userinstitution> ew=new EntityWrapper<Userinstitution>();
	    ew.setEntity(new Userinstitution());
	    ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getUid()), "uid", userinstitution.getUid());
	    ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getIid()), "iid", userinstitution.getIid());
        List<Userinstitution> list=userinstitutionMapper.selectList(ew);
        if(!KwHelper.isNullOrEmpty(list)&& list.size()>0) {
            return new PublicResult<>(PublicResultConstant.EXISTED_USER_ROLE_RELATIONSHIP, null);
        }
        userinstitution.setId(KwHelper.newID());
        userinstitutionMapper.insert(userinstitution);    
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }

	
	@PostMapping("/delUserInstitution")
	@ApiOperation(value = "删除用户机构关系", notes = "所需参数：uid(用户id);iid(机构id)")
	public PublicResult<Map<String, Object>> delUserRole(@ValidationParam("uid,iid")@RequestBody Userinstitution userinstitution) {
        if (ComUtil.isEmpty(userinstitution.getIid()) || ComUtil.isEmpty(userinstitution.getUid())) {
            return new PublicResult<>(PublicResultConstant.MiSSING_KEY_PARAMETERS_ERROR, null);
        }
        EntityWrapper<Userinstitution> ew=new EntityWrapper<Userinstitution>();
	    ew.setEntity(new Userinstitution());
	    ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getUid()), "uid", userinstitution.getUid());
	    ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getIid()), "iid", userinstitution.getIid());
        List<Userinstitution> list=userinstitutionMapper.selectList(ew);
        if(KwHelper.isNullOrEmpty(list)&& list.size()<1) {
            return new PublicResult<>(PublicResultConstant.NO_USER_ROLE_RELATIONSHIP, null);
        }
        String id4del=list.get(0).getId();
        userinstitutionMapper.deleteById(id4del);   
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);		
    }
	
	
	@PostMapping("/findList")
	@ApiOperation(value = "查找用户-机构关系列表", notes = "所需可选参数：rid(角色id),uid(用户id)")
	public PublicResult<Map<String, Object>> findByList(@RequestBody Userinstitution userinstitution) {
        //参数校验
		EntityWrapper<Userinstitution> ew=new EntityWrapper<Userinstitution>();
	    ew.setEntity(new Userinstitution());
		ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getUid()), "uid", userinstitution.getUid());
		ew.eq(!KwHelper.isNullOrEmpty(userinstitution.getIid()), "iid", userinstitution.getIid());
		List<Userinstitution> list = userinstitutionMapper.selectList(ew);        
        Map<String, Object> map=new HashMap<>();
        map.put("data", list);
        return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    }
}