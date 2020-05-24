package com.will.lib_network.okhttp.listener;

/**
 * 包装类
 */
public class DisposeDataHandler
{
	public DisposeDataListener mListener = null;
	public Class<?> mClass = null;
	public String mSource = null;

	public DisposeDataHandler(DisposeDataListener listener)
	{
		this.mListener = listener;
	}

	public DisposeDataHandler(DisposeDataListener listener, Class<?> clazz)
	{
		this.mListener = listener;
		this.mClass = clazz;
	}

	public DisposeDataHandler(DisposeDataListener listener, String source)
	{
		this.mListener = listener;
		this.mSource = source;
	}
}