package kingwant.hjjp.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * @author brk
 * @since 2018-05-03
 */
public class FastJsonHttpMessageConverterEx extends FastJsonHttpMessageConverter {
    public FastJsonHttpMessageConverterEx() {
    	 FastJsonConfig fastjosn=new FastJsonConfig();
    	 fastjosn.setDateFormat("yyyy/MM/dd hh:mm:ss");
    	 this.setFastJsonConfig(fastjosn);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }
}
