package org.bryadong.commons.pojo.dto;

/**
 * Hello world!
 *
 */
public class ResponseBody {

	private boolean success;
	private String responseCode;
	private String responseMessage;
	private Object responseData;
	private String timestamp;
	private String dataType;
	private Long size;
	private Long total;
	private Integer pageSize;
	private Integer pageNo;
	
	@Override
	public String toString() {
		return "ResponseBody [success=" + success + ", responseCode=" + responseCode + ", responseMessage="
				+ responseMessage + ", responseData=" + responseData + ", timestamp=" + timestamp + ", dataType="
				+ dataType + "]";
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

}
