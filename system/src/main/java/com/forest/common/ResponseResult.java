package com.forest.common;

import java.io.Serializable;

public class ResponseResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Meta meta;
	private Object data;
	
	public ResponseResult success(String message) {
		this.meta = new Meta(true,message);
		return this;
	}
	
	public ResponseResult success(String message,Object data)
	{
		this.meta = new Meta(true,message);
		this.data = data;
		return this;
	}
	
	public ResponseResult failure(String message) {
		this.meta = new Meta(false,message);
		return this;
	}
	
	public Meta getMeta() {
		return meta;
	}
	
	public Object getData() {
		return data;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
