package kingwant.hjjp.annotation;


import java.lang.annotation.*;

/**
 * 在Controller方法上加入改注解会自动记录日志
 * @author : brk
 * @date : 2018/05/08
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Log {
	/**
	 * 描述.
	 */
	String description() default "";
	
	/**
	 * 级别：1：重要  2：信息   3：调试
	 * <br>默认为调试信息级别
	 * <br>日志会根据系统配置的级别来进行记录，包含关系为：
	 * <br>调试--》信息--》重要
	 */
	int level() default 3;

}
