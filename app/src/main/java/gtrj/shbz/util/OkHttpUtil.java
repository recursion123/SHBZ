package gtrj.shbz.util;


import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhang77555 on 2015/7/28.
 */
public class OkHttpUtil {
    public static OkHttpClient client;

    static {
        if (client == null) {
            client = new OkHttpClient();
        }
    }

    public static String Post(String method, Map<String, String> params) throws IOException, SessionOutOfTimeException {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formEncodingBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = formEncodingBuilder.build();
        Request request = new Request.Builder()
                .url(ContextString.SERVER + method).header("Cookie", "JSESSIONID=" + ContextString.SESSION)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        if (ContextString.SESSION == null)
            ContextString.SESSION = response.headers().toMultimap().get("Set-Cookie").get(0).split("=")[1];
        String result = response.body().string();
        if ("{\"IsLogin\":\"3\"}".equals(result)) {
            ContextString.SESSION = null;
            throw new SessionOutOfTimeException("session¹ýÆÚ");
        }
        return result;
    }

    public static class SessionOutOfTimeException extends Exception {
        public SessionOutOfTimeException(String e) {
            super(e);
        }
    }
}
