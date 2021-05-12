package cn.edu.fzu.daoyun_app.Utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    /**
     * 网络访问要求singleton
     */
    private static OkHttpUtil instance;

    // 必须要用的okhttpclient实例,在构造器中实例化保证单一实例
    private OkHttpClient mOkHttpClient;


    public static final MediaType JSON = MediaType.
            parse("application/json; charset=utf-8");

    private Handler mHandler;


    private OkHttpUtil() {
        /**
         * okHttp3中超时方法移植到Builder中
         */
        mOkHttpClient = (new OkHttpClient()).newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtil.class) {
                if (instance == null) {
                    instance = new OkHttpUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 对外提供的Get方法访问
     *
     * @param url
     * @param callBack
     */
    public void Get(String url, Callback callBack) {
        /**
         * 通过url和GET方式构建Request
         */
        Request request = bulidRequestForGet(url);
        /**
         * 请求网络的逻辑
         */
        requestNetWork(request, callBack);
    }

    /**
     * 对外提供的Post方法访问
     *
     * @param url
     * @param parms:   提交内容为表单数据
     * @param callBack
     */
    public void PostWithFormData(String url, Map<String, String> parms, Callback callBack) {
        /**
         * 通过url和POST方式构建Request
         */
        Request request = bulidRequestForPostByForm(url, parms);
        /**
         * 请求网络的逻辑
         */
        requestNetWork(request, callBack);

    }

    /**
     * 对外提供的Post方法访问
     *
     * @param url
     * @param json:    提交内容为json数据
     * @param callBack
     */
    public void PostWithJson(String url, JSONObject json, Callback callBack) {
        /**
         * 通过url和POST方式构建Request
         */
        Request request = bulidRequestForPostByJson(url, String.valueOf(json));
        /**
         * 请求网络的逻辑
         */
        requestNetWork(request, callBack);

    }

    /**
     * POST方式构建Request {json}
     *
     * @param url
     * @param json
     * @return
     */
    private Request bulidRequestForPostByJson(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);

        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    /**
     * POST方式构建Request {Form}
     *
     * @param url
     * @param parms
     * @return
     */
    private Request bulidRequestForPostByForm(String url, Map<String, String> parms) {

        FormBody.Builder builder = new FormBody.Builder();

        if (parms != null) {
            for (Map.Entry<String, String> entry :
                    parms.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

        }
        FormBody body = builder.build();


        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }


    /**
     * GET方式构建Request
     *
     * @param url
     * @return
     */
    private Request bulidRequestForGet(String url) {

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }


    /**
     * 发送网络请求
     *
     * @param request
     * @param callBack
     */
    private void requestNetWork(Request request, Callback callBack) {
        //多线程运行
        new Thread(() -> mOkHttpClient.newCall(request).enqueue(callBack)).start();
    }


}
