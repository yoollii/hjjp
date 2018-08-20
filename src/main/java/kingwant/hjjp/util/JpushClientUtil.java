package kingwant.hjjp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JpushClientUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(JpushClientUtil.class);
	private static String APP_KEY = "50ceb845d683d0f2e09a3c44";
	private static String MASTER_SECRET = "fb6b3b67d64164ec056a9969";

	private static JpushClientUtil jpushClientUtil;

	private JpushClientUtil() {

	}

	/**
	 * 
	 * @param title
	 *            标题
	 * @param msgContent
	 *            发送内容
	 */
	public PushResult pushMessage(String title, String content,String type) {
		JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null,
				ClientConfig.getInstance());
			PushResult result = null;
			// 别名推送
			try {
				PushPayload pushPayload = this.buildPushObject_android_and_ios(title, content,type);
				result = jPushClient.sendPush(pushPayload);
			} catch (APIConnectionException e) {
				e.printStackTrace();
				logger.error("推送失败"+e.getMessage());
				return null;
			} catch (APIRequestException e) {
				e.printStackTrace();
				logger.info("HTTP Status: " + e.getStatus());
				logger.info("Error Code: " + e.getErrorCode());
				logger.info("推送失败 Error Message: " + e.getErrorMessage());
				logger.info("Msg ID: " + e.getMsgId());
				return null;
			}
			return result;
	}

	/**
	 * 
	 * @param alias 别名 推送
	 * @param title 标题
	 * @param content 推送内容
	 * @param type 前端跳转页面的参数
	 * @return
	 */
	public PushResult pushMessage(String alias,String title, String content,String type) {
		JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null,
				ClientConfig.getInstance());
			PushResult result = null;
			// 别名推送
			try {
				PushPayload pushPayload = this.buildPushObject_android_and_iosByAlias(alias,title, content,type);
				result = jPushClient.sendPush(pushPayload);
			} catch (APIConnectionException e) {
				e.printStackTrace();
				logger.error("推送失败"+e.getMessage());
				return null;
			} catch (APIRequestException e) {
				e.printStackTrace();
				logger.info("HTTP Status: " + e.getStatus());
				logger.info("Error Code: " + e.getErrorCode());
				logger.info("推送失败 Error Message: " + e.getErrorMessage());
				logger.info("Msg ID: " + e.getMsgId());
				return null;
			}
			return result;
	}

	/**
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            推送内容
	 * @param type 前端跳转页面的参数
	 * @return
	 */
	public PushPayload buildPushObject_android_and_ios(String title,
			String content,String type) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.all())
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(content)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle(title).addExtra("type",type).build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1)
												.addExtra(title, content).addExtra("type", type)
												.build()).build()).build();
	}

	/**
	 * 生成别名极光推送对象PushPayload（采用java SDK）
	 * 
	 * @param alias
	 *            别名
	 * @param alert
	 * @return PushPayload
	 */
	public PushPayload buildPushObject_android_and_iosByAlias(
			String alias, String title, String content,String type) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.alias(alias))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(content)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle(title).addExtra("type", type).build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1)
												.addExtra(title, content).addExtra("type", type)
												.build()).build()).build();
	}

	/**
	 * 生成registrationId极光推送对象PushPayload（采用java SDK）
	 * 
	 * @param registrationId
	 * @param notification_title
	 * @param msg_title
	 * @param msg_content
	 * @param extrasparam
	 * @return
	 */
	@SuppressWarnings("unused")
	private PushPayload buildPushObject_all_registrationId_alertWithTitle(
			String registrationId, String notification_title, String msg_title,
			String msg_content, String extrasparam) {

		System.out.println("----------buildPushObject_all_all_alert");
		// 创建一个IosAlert对象，可指定APNs的alert、title等字段
		return PushPayload
				.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(Platform.all())
				// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
				// id
				.setAudience(Audience.registrationId(registrationId))
				// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
				.setNotification(
						Notification
								.newBuilder()
								// 指定当前推送的android通知
								.addPlatformNotification(
										AndroidNotification.newBuilder()

												.setAlert(notification_title)
												.setTitle(notification_title)
												// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
												.addExtra(
														"androidNotification extras key",
														extrasparam)

												.build())
								// 指定当前推送的iOS通知
								.addPlatformNotification(
										IosNotification
												.newBuilder()
												// 传一个IosAlert对象，指定apns
												// title、title、subtitle等
												.setAlert(notification_title)
												.incrBadge(1)
												.setSound("sound.caf")
												.addExtra(
														"iosNotification extras key",
														extrasparam)

												.build()).build())
				.setMessage(
						Message.newBuilder().setMsgContent(msg_content)
								.setTitle(msg_title)
								.addExtra("message extras key", extrasparam)
								.build())
				.setOptions(
						Options.newBuilder().setApnsProduction(false)
								.setSendno(1).setTimeToLive(86400).build())
				.build();

	}

	/**
	 * 生成 标签 极光推送对象PushPayload（采用java SDK）
	 * 
	 * @param tag
	 *            标签
	 * @param title
	 * @param content
	 * @return
	 */
	public PushPayload buildPushObject_android_and_iosByTag(String tag,
			String title, String content) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag(tag))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(content)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle(title).build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1)
												.addExtra(title, content)
												.build()).build()).build();
	}

	
	/**
	 * 单例
	 * @return
	 */
	public static JpushClientUtil getInstance() {
		if (jpushClientUtil == null) {
			jpushClientUtil = new JpushClientUtil();
		}
		return jpushClientUtil;
	}
}
