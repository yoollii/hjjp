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
public class Applicationsystem extends Model<Applicationsystem> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Integer useFlag;
    private String des;
    private String baseUrl;
    private String modelId;
    private Integer state;


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

    public Integer getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Integer useFlag) {
        this.useFlag = useFlag;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Applicationsystem{" +
        "id=" + id +
        ", name=" + name +
        ", useFlag=" + useFlag +
        ", des=" + des +
        ", baseUrl=" + baseUrl +
        ", modelId=" + modelId +
        ", state=" + state +
        "}";
    }
}
