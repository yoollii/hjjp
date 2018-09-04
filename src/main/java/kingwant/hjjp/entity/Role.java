package kingwant.hjjp.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ch123
 * @since 2018-08-15
 */
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String des;
    private Date crtime;


    public Date getCrtime() {
		return crtime;
	}

	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}


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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Role{" +
        "id=" + id +
        ", name=" + name +
        ", des=" + des +
        "}";
    }
}
