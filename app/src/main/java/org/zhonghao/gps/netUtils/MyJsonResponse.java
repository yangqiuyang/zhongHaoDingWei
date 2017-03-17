package org.zhonghao.gps.netUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/3/17.
 */

public class MyJsonResponse extends Request<JSONArray> {
    private final Response.Listener<JSONArray> mListener;//数据请求回来接口回调
    private String data;

    public MyJsonResponse(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    public MyJsonResponse(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, String data) {
        this(Method.POST, url, listener, errorListener);
        this.data = data;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return this.data.getBytes();
    }

    // //解析数据，把网络请求，或者中缓存中获取的数据，解析成 String
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            /*** 得到返回的数据*/

            String jsons = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            /**转化成对象*/

            return Response.success(new JSONArray(jsons), HttpHeaderParser.parseCacheHeaders(response));//其实就是获取我们的返回流的编码
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(JSONArray response) {
        //数据解析成功后的回调
        mListener.onResponse(response);
    }
}
