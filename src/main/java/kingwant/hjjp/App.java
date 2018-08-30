package kingwant.hjjp;

import javax.security.auth.message.config.AuthConfigFactory;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@MapperScan("kingwant.hjjp.mapper")
@EnableSwagger2
@EnableCaching
public class App{
    public static void main( String[] args ){
    	if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
    	SpringApplication.run(App.class, args);
		//SpringApplication.run(App.class, args);
    }
}
