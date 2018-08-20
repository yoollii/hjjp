package kingwant.hjjp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.util.KwHelper;

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
@RequestMapping("/userrole")
public class UserroleController {
	
	@Autowired
	private UserMapper userMapper;
	
	@GetMapping("/test")
	//@GetMapping 查询
	//@PostMapping 新增
	//@DeleteMapping 删除
	//@PutMapping 修改

	public void test() {
		System.err.println("hahahahahahahmp");
		User user=new User();
		user.setId(KwHelper.newID());
		user.setName("chenjing");
		user.setState(1);
		user.setGroup("分组1");
		userMapper.insert(user);
		
    }

}

