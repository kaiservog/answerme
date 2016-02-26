package com.answerme.http.access;

import com.answerme.http.access.request.CheckPackage;
import com.answerme.http.access.request.RequestPackage;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by kaiservog on 25/02/2016.
 */
public class BackendSender {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String SERVER = "http://192.168.0.5:4666";

    public static String sendPost (AnswermeUrl url, RequestPackage rPackage)  {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SERVER + url.toString())
                .post(RequestBody.create(JSON, rPackage.toJson()))
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
