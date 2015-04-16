package com.rick.base.context;

public class ResponseMsg {
	//执行状态
	private boolean status;
	
	//消息
	private String msg;
	
	//数据
	private Object data;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
