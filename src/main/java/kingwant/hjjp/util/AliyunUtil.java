package kingwant.hjjp.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.base.Objects;

public class AliyunUtil {

	private Logger logger = LoggerFactory.getLogger(AliyunUtil.class);
	// 产品名称:云通信短信API产品,开发者无需替换
	private static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	private static final String domain = "dysmsapi.aliyuncs.com";
	
	private static AliyunUtil aliyunUtil;
	
	private AliyunUtil() {
		
	}

	/**
	 * 读配置文件
	 * 
	 * @param name
	 * @return
	 */
	public Properties getProMethod(String name) {
		Properties pop = null;
		try {
			pop = new Properties();
		InputStreamReader in = new InputStreamReader(AliyunUtil.class.getClassLoader().getResourceAsStream(name),"UTF-8");
		if(Objects.equal(null, in)){
			return null;
		}
			pop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return pop;
	}

	/**
	 * 
	 * @param accessKeyId 阿里key
	 * @param accessKeySecret 阿里secret
	 * @param signName 短信签名
	 * @param TemplateCode 短信模板id
	 * @param telphone 手机号
	 * @param code 验证码
	 * @return
	 * @throws ClientException
	 */
	public SendSmsResponse sendSms(String accessKeyId,String accessKeySecret,String signName, String TemplateCode, String telphone, String code)
			throws ClientException {
		// 设置超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
				domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 待发送的手机号
		request.setPhoneNumbers(telphone);
		// 短信签名
		request.setSignName(signName);
		// 短信模板ID
		request.setTemplateCode(TemplateCode);
		/*
		 * 可选:模板中的变量替换JSON串, 如模板内容为"亲爱的${name},您的验证码为${code}"时,
		 * 此处的值为{"name":"Tom","code":"1454"} \ 反斜杠为转义字符，使得输出双引号
		 */
		request.setTemplateParam("{\"code\":\"" + code + "\"}");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("1454");
		SendSmsResponse response = acsClient.getAcsResponse(request);
		if (response.getCode() != null && response.getCode().equals("OK")) {
			// 请求成功
			System.out.println("发送成功！");
			logger.info(telphone + "短信验证码发送成功");
		} else {
			System.out.println("发送失败！");
			logger.info(telphone + "短信验证码发送失败,错误码" + response.getCode());
		}
		return response;
	}
	
	public static AliyunUtil getInstance(){
		  if(aliyunUtil == null){
			  aliyunUtil = new AliyunUtil();
		  }
		  return aliyunUtil;
	}
}
