package kingwant.hjjp.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
public class Flowtask extends Model<Flowtask> {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer type;
    private String currentLink;
    private String currentUser;
    private Date handleTime;
    private String dealAppliSys;
    private Integer state;    
    private String des;
    private String appliSysId;
    private Date crtime;
    
    public Date getCrtime() {
		return crtime;
	}
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
	private String id;
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCurrentLink() {
		return currentLink;
	}
	public void setCurrentLink(String currentLink) {
		this.currentLink = currentLink;
	}
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getDealAppliSys() {
		return dealAppliSys;
	}
	public void setDealAppliSys(String dealAppliSys) {
		this.dealAppliSys = dealAppliSys;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getAppliSysId() {
		return appliSysId;
	}
	public void setAppliSysId(String appliSysId) {
		this.appliSysId = appliSysId;
	}
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}    
}
