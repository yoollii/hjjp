package kingwant.hjjp.service.impl;

import kingwant.hjjp.entity.User;
import kingwant.hjjp.mapper.UserMapper;
import kingwant.hjjp.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
