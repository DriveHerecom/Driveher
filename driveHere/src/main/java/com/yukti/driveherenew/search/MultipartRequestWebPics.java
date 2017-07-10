package com.yukti.driveherenew.search;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;

/**
 * Created by Creadigol on 2/17/2016.
 */
public class MultipartRequestWebPics extends Request<String> {
    private HttpEntity mHttpEntity;
    private Response.Listener mListener;

    public MultipartRequestWebPics(String url,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity();
    }

    private HttpEntity buildMultipartEntity() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        FileBody fileBody;

        builder.addTextBody(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());

        builder.addTextBody(ParamsKey.KEY_type, Constant.APP_TYPE);

        builder.addTextBody(ParamsKey.KEY_carId, WebPicsActivity.carid);
        Log.e(ParamsKey.KEY_carId, WebPicsActivity.carid);


        if (WebPicsActivity.arrayImageAllPath != null
                && WebPicsActivity.arrayImageAllPath.size() > 0) {
            builder.addTextBody(ParamsKey.KEY_count, WebPicsActivity.arrayImageAllPath.size() + "");
            for (int k = 0; k < WebPicsActivity.arrayImageAllPath.size(); k++) {
                Log.e("path", WebPicsActivity.arrayImageAllPath.get(k));
                String final_path;
                if (WebPicsActivity.arrayImageAllPath.get(k).contains("file:")) {
                    final_path = WebPicsActivity.arrayImageAllPath.get(k).replace("file:", "");
                } else {
                    final_path = WebPicsActivity.arrayImageAllPath.get(k);
                }
                File file = new File(final_path);
//                Log.e("File" + k, file.length() + "");
                FileBody Fbody = new FileBody(file);
                builder.addPart(ParamsKey.KEY_imageAdd + (k + 1), Fbody);
                Log.e(ParamsKey.KEY_imageAdd, "=" + (k + 1));
            }
        }
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }


}
