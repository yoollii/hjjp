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
public class Propertyconfig extends Model<Propertyconfig> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String dataMap;
    private String inConfig;
    private String outConfig;
    private String serId;
    private String modelId;
    private String flowId;
    private String taskId;
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

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }

    public String getInConfig() {
        return inConfig;
    }

    public void setInConfig(String inConfig) {
        this.inConfig = inConfig;
    }

    public String getOutConfig() {
        return outConfig;
    }

    public void setOutConfig(String outConfig) {
        this.outConfig = outConfig;
    }

    public String getSerId() {
        return serId;
    }

    public void setSerId(String serId) {
        this.serId = serId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Propertyconfig{" +
        "id=" + id +
        ", dataMap=" + dataMap +
        ", inConfig=" + inConfig +
        ", outConfig=" + outConfig +
        ", serId=" + serId +
        ", modelId=" + modelId +
        ", flowId=" + flowId +
        ", taskId=" + taskId +
        "}";
    }
}
