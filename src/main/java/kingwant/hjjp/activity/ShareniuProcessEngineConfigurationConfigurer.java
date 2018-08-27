package kingwant.hjjp.activity;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;

public class ShareniuProcessEngineConfigurationConfigurer implements ProcessEngineConfigurationConfigurer{
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");
		processEngineConfiguration.setAnnotationFontName("宋体");
		System.out.println("ShareniuProcessEngineConfigurationConfigurer#############");
		System.out.println(processEngineConfiguration.getActivityFontName());
	}

}
