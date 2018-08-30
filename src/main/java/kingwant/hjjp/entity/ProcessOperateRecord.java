package kingwant.hjjp.entity;

import java.io.Serializable;
import java.util.Date;

/** 
 * @author Michael.C.H
 * date:2018-05-11 15:18:36
 */
public class ProcessOperateRecord implements Serializable {
    //串行版本ID
    private static final long serialVersionUID = -1567752427838658206L;

    private String id;

    private String objId;

    private String objCode;

    private String operatorId;

    private String operatorName;

    private Integer operatorType;

    private String taskId;

    private String taskName;

    private String operateResult;

    private Date operateTime;

    private String operateOpinions;

    private String memo;
    

	/** 
     * 获取 processOperateRecord.id
     * @return processOperateRecord.id
     */
    public String getId() {
        return id;
    }

    /** 
     * 设置 processOperateRecord.id
     * @param id processOperateRecord.id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /** 
     * 获取 processOperateRecord.objId
     * @return processOperateRecord.objId
     */
    public String getObjId() {
        return objId;
    }

    /** 
     * 设置 processOperateRecord.objId
     * @param objId processOperateRecord.objId
     */
    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    /** 
     * 获取 processOperateRecord.objCode
     * @return processOperateRecord.objCode
     */
    public String getObjCode() {
        return objCode;
    }

    /** 
     * 设置 processOperateRecord.objCode
     * @param objCode processOperateRecord.objCode
     */
    public void setObjCode(String objCode) {
        this.objCode = objCode == null ? null : objCode.trim();
    }

    /** 
     * 获取 processOperateRecord.operatorId
     * @return processOperateRecord.operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /** 
     * 设置 processOperateRecord.operatorId
     * @param operatorId processOperateRecord.operatorId
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    /** 
     * 获取 processOperateRecord.operatorName
     * @return processOperateRecord.operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /** 
     * 设置 processOperateRecord.operatorName
     * @param operatorName processOperateRecord.operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /** 
     * 获取 processOperateRecord.operatorType
     * @return processOperateRecord.operatorType
     */
    public Integer getOperatorType() {
        return operatorType;
    }

    /** 
     * 设置 processOperateRecord.operatorType
     * @param operatorType processOperateRecord.operatorType
     */
    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    /** 
     * 获取 processOperateRecord.taskId
     * @return processOperateRecord.taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /** 
     * 设置 processOperateRecord.taskId
     * @param taskId processOperateRecord.taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /** 
     * 获取 processOperateRecord.taskName
     * @return processOperateRecord.taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /** 
     * 设置 processOperateRecord.taskName
     * @param taskName processOperateRecord.taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /** 
     * 获取 processOperateRecord.operateResult
     * @return processOperateRecord.operateResult
     */
    public String getOperateResult() {
        return operateResult;
    }

    /** 
     * 设置 processOperateRecord.operateResult
     * @param operateResult processOperateRecord.operateResult
     */
    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult == null ? null : operateResult.trim();
    }

    /** 
     * 获取 processOperateRecord.operateTime
     * @return processOperateRecord.operateTime
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /** 
     * 设置 processOperateRecord.operateTime
     * @param operateTime processOperateRecord.operateTime
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /** 
     * 获取 processOperateRecord.operateOpinions
     * @return processOperateRecord.operateOpinions
     */
    public String getOperateOpinions() {
        return operateOpinions;
    }

    /** 
     * 设置 processOperateRecord.operateOpinions
     * @param operateOpinions processOperateRecord.operateOpinions
     */
    public void setOperateOpinions(String operateOpinions) {
        this.operateOpinions = operateOpinions == null ? null : operateOpinions.trim();
    }

    /** 
     * 获取 processOperateRecord.memo
     * @return processOperateRecord.memo
     */
    public String getMemo() {
        return memo;
    }

    /** 
     * 设置 processOperateRecord.memo
     * @param memo processOperateRecord.memo
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
	public String toString() {
		return "ProcessOperateRecord [id=" + id + ", objId=" + objId + ", objCode=" + objCode + ", operatorId="
				+ operatorId + ", operatorName=" + operatorName + ", operatorType=" + operatorType + ", taskId="
				+ taskId + ", taskName=" + taskName + ", operateResult=" + operateResult + ", operateTime="
				+ operateTime + ", operateOpinions=" + operateOpinions + ", memo=" + memo + "]";
	}
}