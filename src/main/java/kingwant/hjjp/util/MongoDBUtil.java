package kingwant.hjjp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSONObject;

import xyz.michaelch.mchtools.beans.ClassInfo;
import xyz.michaelch.mchtools.hepler.BeanHelper;

/**
 * mongodb相关工具类
 * @author 陈豪
 *
 */
public class MongoDBUtil {
	//私有构造方法，工具类
	private MongoDBUtil(){};
	
	/**
	 * 构建类似mybatis的xml的动态查询条件，bean中有值则添加这一列的条件。<br>
	 * String类型为模糊查询（false）还是精确匹配（true）参考参数strExactlyMatch<br>
	 * 数值类型（Integer、Long、Double等）为等值查询<br>
	 * 其他类型未处理<br>
	 * 参数bean不允许有基本数据类型！
	 * @param entity 参数bean
	 * @param strExactlyMatch String类型是否为精确匹配
	 * @return
	 */
	public static Query buildEntityQueryCriteria(Object entity,boolean strExactlyMatch) {
		Query query=new Query();
		if (entity!=null) {
			Class<?> clz=entity.getClass();
			//获取所有属性
			Field[] fs=clz.getDeclaredFields();
			if (fs!=null&&fs.length>0) {
				//方法对象
				Method method = null;
				Object val=null;
				for (int i = 0; i < fs.length; i++) {
					try {
						//调用属性的get方法
						method=clz.getMethod("get"+KwHelper.captureName(fs[i].getName()));
						val=method.invoke(entity);
						//System.out.println(fs[i].getName()+"\t"+fs[i].getType()+"\t"+method.invoke(entity));
						//空，则不处理该属性
						if (val==null) {
							continue;
						}
						
						//字符串类型
						if (fs[i].getType()==String.class) {
							if (!"".equals(val.toString())) {
								//id用等于
								if (fs[i].getName().toLowerCase().equals("id")||strExactlyMatch) {
									query.addCriteria(Criteria.where(fs[i].getName()).is(val));
								}else {
									query.addCriteria(Criteria.where(fs[i].getName()).regex(val.toString()));
								}
							}
						}
						//数值类型
						else if (fs[i].getType()==Integer.class||fs[i].getType()==Long.class||fs[i].getType()==Double.class) {
							query.addCriteria(Criteria.where(fs[i].getName()).is(val));
						}
						//其他类型
						else {
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}		
		return query;
	}
	
	/**
	 * 将bean转换为mongodb所需要的map集合
	 * @param o 对象
	 * @return
	 */
	public static Map<String, Object> beanToMongoMap(Object o) {
		if (o==null) {
			return null;
		}
		Map<String, Object> map=new HashMap<>();		
		
		if (ClassInfo.isKnownType(o.getClass())) {
			map.put("returnVal", o);
		}else {
			try {				
				String json=BeanHelper.toJson(o,"yyyy-MM-dd HH:mm:ss");
				JSONObject jobj=JSONObject.parseObject(json);
				map.putAll(jobj);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return map;
	}
	

}
