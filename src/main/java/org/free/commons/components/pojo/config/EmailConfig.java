/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.pojo.config;

/**
 * @author dongdaiming@free.com
 *
 *         2016年8月16日
 */
public class EmailConfig {

	private String serverHostName;
	private int serverSmtpPort;
	private String serverUsername;
	private String serverPassword;
	private String serverFrom;
	private boolean serverUseSSL;

	public String getServerHostName() {
		return serverHostName;
	}

	public void setServerHostName(String serverHostName) {
		this.serverHostName = serverHostName;
	}

	public int getServerSmtpPort() {
		return serverSmtpPort;
	}

	public void setServerSmtpPort(int serverSmtpPort) {
		this.serverSmtpPort = serverSmtpPort;
	}

	public String getServerUsername() {
		return serverUsername;
	}

	public void setServerUsername(String serverUsername) {
		this.serverUsername = serverUsername;
	}

	public String getServerPassword() {
		return serverPassword;
	}

	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	public String getServerFrom() {
		return serverFrom;
	}

	public void setServerFrom(String serverFrom) {
		this.serverFrom = serverFrom;
	}

	public boolean isServerUseSSL() {
		return serverUseSSL;
	}

	public void setServerUseSSL(boolean serverUseSSL) {
		this.serverUseSSL = serverUseSSL;
	}

}
