package ir.taha7900.yadnegar.Utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import static ir.taha7900.yadnegar.Utils.MsgCode.*;
import ir.taha7900.yadnegar.Models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network {

    static class URL {
        static String LOGIN = "http://memoreminder.ir/api/v1/login/";
        static String REGISTER = "http://memoreminder.ir/api/v1/memo-user/";
    }

    static abstract class CustomCallback implements Callback {
        private Handler handler;
        public CustomCallback(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            handler.sendEmptyMessage(NETWORK_ERROR);
            e.printStackTrace();
        }
    }

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Gson gson = new Gson();

    private static Request.Builder getAuthorizedRequest() {
        return new Request.Builder()
                .addHeader("Authorization", "token 3386fb2b1433447606917b3b70c837d834d6f505");
    }

    public static void sendLoginRequest(String username, String password, Handler handler) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password).build();
        Request request = getAuthorizedRequest().url(URL.LOGIN).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code != 200) {
                    handler.sendEmptyMessage(LOGIN_FAILED);
                    return;
                }
                String body = response.body().string();
                User user = gson.fromJson(body, User.class);
                User.setCurrentUser(user);
                handler.sendEmptyMessage(LOGIN_SUCCESSFUL);
            }
        });
    }

    public static void sendRegisterRequest(HashMap<String, String> data, Handler handler) {
        FormBody.Builder body = new FormBody.Builder();
        for (String key : data.keySet())
            body.add(key, data.get(key));
        Request request = getAuthorizedRequest().url(URL.REGISTER).post(body.build()).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                String body = response.body().string();
                System.out.println(body);
                if (code != 201) {
                    HashMap<String, String[]> errors = gson.fromJson(body, new TypeToken<HashMap<String, String[]>>(){}.getType());
                    Message message = new Message();
                    message.what = REGISTER_ERROR;
                    message.obj = errors.values().iterator().next()[0];
                    handler.sendMessage(message);
                    return;
                }
                User.setCurrentUser(gson.fromJson(body, User.class));
                handler.sendEmptyMessage(REGISTER_SUCCESSFUL);
            }
        });
    }
}
