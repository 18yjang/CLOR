package org.ewha5.clorapp;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URIBuilder;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/*윤주*/
// 서버로 업로드


public class subThread extends Thread {
    public static void sendHttpWithMsg(String url) {

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        URIBuilder newBuilder = new URIBuilder(post.getURI());
        post.setHeader("Content-type", "application/json; charset=utf-8");

        //Json파일 만들기
        JSONObject obj = new JSONObject();
        try {
            obj.put("image", Fragment1.getStringFromBitmap(Fragment1.resultPhotoBitmap));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            StringEntity se;
            se = new StringEntity(obj.toString());
            HttpEntity he = se;
            post.setEntity(he);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String line = null;
        String result = "";

        try {
            HttpResponse response = client.execute(post);   // 문제
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
        } catch (ClientProtocolException protocolException) {
            protocolException.printStackTrace();
            //return "Error"+protocolException.toString();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            //return "Error"+ioException.toString();
        }

        //return result;


    }


}
