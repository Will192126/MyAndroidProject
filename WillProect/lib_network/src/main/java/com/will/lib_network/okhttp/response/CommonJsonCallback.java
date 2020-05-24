package com.will.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONObject;
import com.will.lib_network.okhttp.exception.OkHttpException;
import com.will.lib_network.okhttp.listener.DisposeDataHandler;
import com.will.lib_network.okhttp.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理JSON类型响应
 */
public class CommonJsonCallback implements Callback {

    protected final String EMPTY_MSG = "";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    private DisposeDataListener mDisposeDataListener;
    private Class<?> mClass;
    private Handler mDeliveryHandler;

    public CommonJsonCallback(DisposeDataHandler handler) {
        mDisposeDataListener = handler.mListener;
        mClass = handler.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mDisposeDataListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(String result) {
        if (result == null || result.trim().equals("")) {
            mDisposeDataListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            if (mClass == null) {
                // 不需解析
                mDisposeDataListener.onSuccess(result);
            } else {
                Object obj = JSONObject.parse(result);
                if (obj != null) {
                    mDisposeDataListener.onSuccess(obj);
                } else {
                    mDisposeDataListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mDisposeDataListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
        }
    }
}
