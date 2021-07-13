package ir.taha7900.yadnegar.Utils;

import android.os.Handler;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                //TODO: show network error problem
                handler.sendEmptyMessage(MsgCode.LOGIN_FAILED);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code != 200) {
                    handler.sendEmptyMessage(MsgCode.LOGIN_FAILED);
                    return;
                }
                String body = response.body().toString();
                User user = gson.fromJson(body, User.class);
                User.setCurrentUser(user);
                handler.sendEmptyMessage(MsgCode.LOGIN_SUCCESSFUL);
            }
        });
    }
}
