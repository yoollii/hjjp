package kingwant.hjjp.service;

import java.util.Map;
import org.activiti.engine.runtime.ProcessInstance;

public interface ACTFormService {

	
	/**
	 * @Title: startForm 
	 * @Description: 启动流程
	 * @param processDefinitionKey 流程关键字
	 * @param formValues 流程键值队
	 * @param useraName 起单人用户名
	 * @return
	 * @throws Exception
	 * ProcessInstance
	 */
	ProcessInstance startForm(String processDefinitionKey) throws Exception;
	ProcessInstance startFormOfParm(String processDefinitionId, String userName,Map<String, String> maps)throws Exception;
//	ProcessInstance startFormOfParm2(String processDefinitionId, String userName,Map<String, String> maps,String id)throws Exception;
    
	/**
     * @Title: saveFormData 
     * @Description: 保存表单数据
     * @param processInstanceId 流程实例id
     * @param properties 表单字段与其值对应
     * void
     * @throws Exception 
     */
//    void saveFormData(String processnstanceId, Map<String, String> properties) throws Exception;
    
    
    /**
     * @Title: findFormOnPower 
     * @Description:  渲染表单页面根据流程节点的权限和已经取得已经获取的值
     * @param processDefinitionId 流程实例id
     * @return
     * String
     */
//    String findFormOnPower(String processDefinitionId);

	/**
	 * @Title: getStartForm 
	 * @Description: 根据流程定义的Key回最新的表单模板（html）
	 * @param processDefinitionKey
	 * @return
	 * String
	 */
//	String getStartForm(String processDefinitionKey);
	
	/**
	 * @Title: findLatestProcessDefinitionBykey 
	 * @Description: 查找最新版本的流程定义，返回流程定义吗，找不到返回null
	 * @param processDefinitionKey 
	 * @return
	 * ProcessDefinition
	 */
//	ProcessDefinition findLatestProcessDefinitionBykey(String processDefinitionKey);
	
	
	/**
	 * @Title: findRunningProcessInstacesByKey 
	 * @Description: 查找当前表单运行中的流程
	 * @param key
	 * @return
	 * List<Map<String,String>>
	 */
//	List<Map<String,String>> findRunningProcessInstacesByKey(String key);
	
	/**
	 * @Title: findRunningProcessInstacesByUserId 
	 * @Description: 查找当前用户待处理的流程
	 * @param UserId
	 * @return
	 * List<Map<String,String>>
	 */
//	List<Map<String,String>> findRunningProcessInstacesByUserId(String UserId);
	
	
	
	/**
	 * @Title: completeTask 
	 * @Description: 完成任务（任务实例）（用户任务）
	 * @param id 表单实例id
	 * @param key 表code
	 * @param taskId 
	 * @param vlaues
	 * @throws Exception
	 * void
	 */
//	void completeTask(String id, String key,String taskId,Map<String, String> vlaues) throws Exception;
	
	/**
	 * @Title: findTaskByUserOfUndone 
	 * @Description: 查询当前用户未完成的该表单任务
	 * @param userId
	 * @return
	 * List<Map<String,String>>
	 */
//	List<Map<String, String>>  findTaskByUserOfUndone(String code,String userId);
	
	/**
	 * @Title: findTaskByUserOfDone 
	 * @Description: 查询当前用户已完成的该表单任务
	 * @param userId
	 * @return
	 * List<Map<String,String>>
	 */
//	List<Map<String, String>>  findTaskByUserOfDone(String code,String userId);
	
	
	
}
