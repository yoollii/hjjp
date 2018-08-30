package kingwant.hjjp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 历史审批记录Bean
 * @author Wzy
 *
 */
public class RecordOfApproval implements Serializable{
	
	private static final long serialVersionUID = -6177466962916224050L;

	private String operatorName; //操作者姓名	
	
	private String taskName;  // 操作类型
	
	private String operateResult;  // 操作结果
	
	private Date operateTime;  // 操作时间
	
	private String operateOpinions; // 处理意见
	
	private List<String> fileSrcName; // 文件上传时的名称
	
	private List<String> fileWebPath; // 文件的web下载地址

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(String operateResult) {
		this.operateResult = operateResult;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateOpinions() {
		return operateOpinions;
	}

	public void setOperateOpinions(String operateOpinions) {
		this.operateOpinions = operateOpinions;
	}


	public List<String> getFileSrcName() {
		return fileSrcName;
	}

	public void setFileSrcName(List<String> fileSrcName) {
		this.fileSrcName = fileSrcName;
	}

	public List<String> getFileWebPath() {
		return fileWebPath;
	}

	public void setFileWebPath(List<String> fileWebPath) {
		this.fileWebPath = fileWebPath;
	}

	@Override
	public String toString() {
		return "RecordOfApproval [operatorName=" + operatorName + ", taskName=" + taskName + ", operateResult="
				+ operateResult + ", operateTime=" + operateTime + ", operateOpinions=" + operateOpinions
				+ ", fileSrcName=" + fileSrcName + ", fileWebPath=" + fileWebPath + "]";
	}

	
	
	
	
	
}
