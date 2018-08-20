package kingwant.hjjp.util;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.quartz.impl.triggers.CronTriggerImpl;




public class KwHelper {

	/**
	 * 判断字符串是否为空或空字符串
	 *  空或空字符串 返回true  不是返回false
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if ( str == null){
			return true;
		}
		if ( "".equals(str.trim())){
			return true;
		}
		return false;
	}

	/**
	 * 首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	/**
	 * 集合是否为null或没元素
	 * 
	 * @param col
	 *            所有Collection的子类
	 * @return
	 */
	public static boolean isNullOrEmpty(Collection col) {
		boolean f = true;
		if ( (col != null) && (!col.isEmpty())){
			f = false;
		}
		return f;
	}
	/**
	 * 直接将list内的String评成sql的in条件
	 * 
	 * @param list
	 * @return
	 */
	public static String assemblyStringToSql(List<String> list) {
		// 如果集合为空，则直接返回空字符串
		if ( isNullOrEmpty(list)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for ( int i = 0 ; i < list.size() ; i++){
			if ( !isNullOrEmpty(list.get(i))){
				if ( i > 0){
					sb.append(",");
				}
				sb.append("'");
				sb.append(list.get(i));
				sb.append("'");
			}

		}
		return sb.toString();
	}

	/**
	 * 为sql的in查询处理ids（处理多逗号，没单引号）
	 * 
	 * @param ids
	 *            要处理的参数
	 * @return 处理完成的字符串或空字符串
	 */
	public static String dealIdsForInQuery(String ids) {
		String ret = "";
		if ( !KwHelper.isNullOrEmpty(ids)){
			// 去除第一个逗号
			if ( ids.startsWith(",")){
				ids = new String(ids.substring(1, ids.length()));
			}
			// 去除最后一个逗号
			if ( ids.endsWith(",")){
				ids = new String(ids.substring(0, ids.length() - 1));
			}
			// 如果没有单引号
			if ( !ids.contains("'")){
				// 如果只有多个id
				if ( ids.contains(",")){
					ids = "'" + ids.replaceAll(",", "','") + "'";
				}
				// 如果只有一个id
				else{
					ids = "'" + ids + "'";
				}
			}
			ret = ids;
		}

		return ret;
	}
	
	
	/**
	 * 创建一个32位没有"-"的UUID
	 * 
	 * @return
	 */
	public static String newID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 格式化异常信息
	 * @param eMsg e.getMessage()
	 * @return
	 */
	public static String formatExceptionMsg(String eMsg){
		String msg=eMsg;
		try {
			if (eMsg.lastIndexOf("Exception:")!=-1) {
				msg=eMsg.substring(eMsg.lastIndexOf("Exception:")+10);				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return msg;
	}
	
	/**
	 * cron表达式验证
	 * @param cronExpression
	 * @return
	 */
	public static boolean cronValid(final String cronExpression){
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
	
	/**
	 * 判断是否为真正的数值（包含负数，小数）
	 * @param num
	 * @return
	 */
	public static boolean isRealNum(String num) {
		return num.matches("-?[0-9]+(\\.[0-9]+)?");  
	}	 
	 
	 /**
		 * 发送邮件
		 * @param dest 收件人 可多个，以“;”隔开
		 * @param subject 主题
		 * @param content html邮件内容
		 * @param conf 邮件配置：<br>
		 * hostName:发件服务器地址<br>
		 * ssl:是否开启ssl<br>
		 * port:smtp端口<br>
		 * sender:发件人地址<br>
		 * username:用户名<br>
		 * password:密码<br>
		 * 
		 * @throws EmailException 
		 */
		public static void sendEmail(String dest, String subject, String content,Map<String, String> conf) throws EmailException {
			MultiPartEmail email = new MultiPartEmail();		
			// 这里是发送服务器的名字：
			email.setHostName(conf.get("hostName"));
			// email.setHostName("smtp.qq.com");
			// 编码集的设置
			email.setCharset("utf-8");
			// 收件人的邮箱 （判断是否为多用户）
			if (dest.contains(";")) {
				String[] target = dest.split(";");
				for (int i = 0; i < target.length; i++) {
					if (!target[i].contains("@")) {
						continue;
					}
					email.addTo(target[i]);
				}
			} else {
				email.addTo(dest);
			}
			//设置是否为ssl
			boolean isSsl=Boolean.parseBoolean(conf.get("ssl"));
			int port=Integer.parseInt(conf.get("port"));
			email.setSmtpPort(port);
			email.setSSLOnConnect(isSsl);
			
			// 发送人的邮箱
			email.setFrom(conf.get("sender"));
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(conf.get("username"), conf.get("password"));
			email.setSubject(subject);
			// 要发送的信息
			// email.setMsg(content);
			email.setContent(content, "text/html; charset=utf-8");

			// 发送
			email.send();
			
		}

		/**
		 * 校验邮箱格式
		 */
		public static boolean checkEmail(String value) {
			boolean flag = false;
			Pattern p1 = null;
			Matcher m = null;
			p1 = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			m = p1.matcher(value);
			flag = m.matches();
			return flag;
		}

}
