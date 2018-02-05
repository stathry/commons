package org.stathry.commons.log;

import java.util.Set;

/**
 * 日志配置
 * 
 * @date 2018年1月27日
 */
public class LogConfig {

	/** 主日志级别 */
	private String mainLevel;
	/** 第三方日志级别 */
	private String orgLevel;
	/** 主日志保存天数 */
	private Integer mainMaxHistory;
	/** 第三方日志保存天数 */
	private Integer orgMaxHistory;
	/** 日志文件目录 */
	private String logDir;
	/** 日志文件名称前缀 */
	private String filenamePrefix;
	/** 日志文件名称后缀 */
	private String filenameSuffix;
	/** 第三方机构码列表 */
	private Set<String> orgCodes;

	@Override
	public String toString() {
		return "LogConfig [mainLevel=" + mainLevel + ", orgLevel=" + orgLevel + ", mainMaxHistory=" + mainMaxHistory
				+ ", orgMaxHistory=" + orgMaxHistory + ", logDir=" + logDir + ", filenamePrefix=" + filenamePrefix
				+ ", filenameSuffix=" + filenameSuffix + ", orgCodes=" + orgCodes + "]";
	}

	public Set<String> getOrgCodes() {
		return orgCodes;
	}

	public void setOrgCodes(Set<String> orgCodes) {
		this.orgCodes = orgCodes;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	public void setFilenamePrefix(String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}

	public String getFilenameSuffix() {
		return filenameSuffix;
	}

	public void setFilenameSuffix(String filenameSuffix) {
		this.filenameSuffix = filenameSuffix;
	}

	public String getMainLevel() {
		return mainLevel;
	}

	public void setMainLevel(String mainLevel) {
		this.mainLevel = mainLevel;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public Integer getMainMaxHistory() {
		return mainMaxHistory;
	}

	public void setMainMaxHistory(Integer mainMaxHistory) {
		this.mainMaxHistory = mainMaxHistory;
	}

	public Integer getOrgMaxHistory() {
		return orgMaxHistory;
	}

	public void setOrgMaxHistory(Integer orgMaxHistory) {
		this.orgMaxHistory = orgMaxHistory;
	}

}
