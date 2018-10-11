package kingwant.hjjp.base;

/**
 * @author brk
 * @since 2018-05-03
 */
public enum PublicResultConstant {
    /**
     * 成功
     */
    SUCCESS("0000", "success"),
    /**
     * 异常
     */
    FAILED("1001", "系统错误"),
    
    /**
     * 未登录/token过期
     */
    UNAUTHORIZED("1002", "获取登录用户信息失败"),
    /**
     * 失败
     */
    ERROR("1003", "操作失败"),

    /**
     * 用户名或密码错误
     */
    INVALID_USERNAME_PASSWORD("2001", "用户名或密码错误"),
    /**
     *
     */
    INVALID_RE_PASSWORD("2002", "两次输入密码不一致"),
    /**
     * 用户名或密码错误
     */
    INVALID_PASSWORD("2003", "旧密码错误"),
    /**
     * 用户名重复
     */
    USERNAME_ALREADY_IN("2004", "用户已存在"),
    /**
     * 用户不存在
     */
    INVALID_USER("2005", "用户不存在"),
    /**
     * 角色不存在
     */
    INVALID_ROLE("3001", "角色不存在"),

    /**
     * 角色不存在
     */
    ROLE_USER_USED("3002", "角色使用中，不可删除"),
    /**
     * 没有权限
     */
    USER_NO_PERMITION("3003", "当前用户无该接口权限"),
    /**
     * 失败
     */
    PARAM_ERROR("4001", "参数错误"),

    /**
     * 参数错误-已存在
     */
    INVALID_PARAM_EXIST("4002", "请求参数已存在"),
    /**
     * 参数错误
     */
    INVALID_PARAM_EMPTY("4003", "请求参数为空"),
    /**
     * 技师工号错误
     */
    INVALID_WORKERNUM_ERROR("5001","技师工号错误"),
    /**
     * 技师已经有师傅
     */
    INVALID_WORKERMASTER_EXIST("5002","您已经有师傅,不能再拜师"),
    /**
     * 要收的徒弟已经有师傅
     */
    INVALID_FOLLOWERMASTER_EXIST("5003","技师已经有师傅,您不能收他为徒"),
    /**
     * 拜师或者收徒关系等待确认中，又重复写一条数据
     */
    INVALID_REPEATSSUBMIT_EXIST("5004","等待审核，请勿重复提交"),
    /**
     * 用户在拜师或者收徒接口输入自己的工号
     */
    INVALID_REPEARSELF_ERROR("5005","请勿提交自己的工号"),
    
    /**
     * 用户不能拜自己的徒弟为师
     */
    INVALID_FOLLOWERTOMASTER_ERROR("5006","您不能拜自己的徒弟为师"),
    /**
     * 用户不能收自己的师傅为徒
     */
    INVALID_MASTERTOFOLLOWER_ERROR("5007","您不能收自己的师傅为徒"),
    /**
     * 缺少关键参数
     */
    MiSSING_KEY_PARAMETERS_ERROR("5008","缺少关键参数"),
    /**
     * 已存在该角色用户关系
     */
    EXISTED_USER_ROLE_RELATIONSHIP("5010","已存在当前关系"),
    /**
     * 不已存在当前角色用户关系
     */
    NO_USER_ROLE_RELATIONSHIP("5010","不已存在当前关系"),
    /**
     * 操作数据库失败
     */
    SQL_EXCEPTION("5009","操作数据库失败"),
    /**
     * 不已存在当前角色用户关系
     */
    NO_MUST_SERVICE("5011","操作失败,流程文件不符合规范,缺少必要服务"),
    ;
    
    

    public String result;
    public String msg;

    PublicResultConstant(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
