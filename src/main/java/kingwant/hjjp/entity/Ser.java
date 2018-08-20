package kingwant.hjjp.entity;

import java.io.Serializable;

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
public class Ser extends Model<Ser> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Integer state;
    private String des;
    private Integer orders;
    private Integer urlFlag;
    private String url;


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

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Integer getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(Integer urlFlag) {
        this.urlFlag = urlFlag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Ser{" +
        "id=" + id +
        ", name=" + name +
        ", state=" + state +
        ", des=" + des +
        ", orders=" + orders +
        ", urlFlag=" + urlFlag +
        ", url=" + url +
        "}";
    }
}
