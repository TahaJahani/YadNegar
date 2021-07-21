package ir.taha7900.yadnegar.Utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ir.taha7900.yadnegar.Models.Comment;
import ir.taha7900.yadnegar.Models.FriendRequest;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static ir.taha7900.yadnegar.Utils.MsgCode.COMMENT_ADDED;
import static ir.taha7900.yadnegar.Utils.MsgCode.COMMENT_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.CREATE_TAG_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_FRIEND_REQUEST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_FRIEND_REQUEST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USERS_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USERS_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.LOGIN_FAILED;
import static ir.taha7900.yadnegar.Utils.MsgCode.LOGIN_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.MEMORY_DATA_READY;
import static ir.taha7900.yadnegar.Utils.MsgCode.MEMORY_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.NETWORK_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.POST_LIKE_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.POST_LIKE_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.REGISTER_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.REGISTER_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.SEND_FRIEND_REQUEST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.SEND_FRIEND_REQUEST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.TAG_DATA_READY;
import static ir.taha7900.yadnegar.Utils.MsgCode.TAG_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.USER_MEMORY_DATA_READY;
import static ir.taha7900.yadnegar.Utils.MsgCode.USER_MEMORY_ERROR;

public class Network {

    static class URL {
        static String BASE = "https://memoreminder.ir";
        static String LOGIN = BASE + "/api/v1/login/";
        static String REGISTER = BASE + "/api/v1/memo-user/";
        static String TAG = BASE + "/api/v1/tag/";
        static String TOP_MEMO = BASE + "/api/v1/top-post/";
        static String USER_MEMOS = BASE + "/api/v1/post/";
        static String ADD_COMMENT = BASE + "/api/v1/comment/";
        static String LIKE_COMMENT = BASE + "/api/v1/comment-like/";
        static String LIKE_POST = BASE + "/api/v1/post-like/";
        static String FRIEND_REQUEST = BASE + "/api/v1/friend-request/";
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

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static Request.Builder getAuthorizedRequest() {
        return new Request.Builder()
                .addHeader("Authorization", "token 3386fb2b1433447606917b3b70c837d834d6f505")
                .addHeader("Accept-Language", "en");
    }

    private static String getAuthorizedUrl(String baseUrl) {
        User user = User.getCurrentUser();
        return baseUrl + "?token=" + user.getToken();
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
                    try {
                        Message msg = new Message();
                        JSONObject outerObj = new JSONObject(response.body().string());
                        msg.obj = outerObj.getString("detail");
                        msg.what = LOGIN_FAILED;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        handler.sendEmptyMessage(LOGIN_FAILED);
                    }
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
                    HashMap<String, String[]> errors = gson.fromJson(body, new TypeToken<HashMap<String, String[]>>() {
                    }.getType());
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

    public static void createTag(String name, String color, Handler handler) {
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("color", color).build();
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.TAG)).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(TAG_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                Tag tag = gson.fromJson(body, Tag.class);
                // todo
                handler.sendEmptyMessage(CREATE_TAG_SUCCESSFUL);
            }
        });
    }

    public static void getTags(Handler handler) {
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.TAG)).get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(TAG_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    ArrayList<Tag> tags = gson.fromJson(String.valueOf(outerObj.getJSONArray("results")), new TypeToken<ArrayList<Tag>>() {
                    }.getType());
                    Tag.setUserTags(tags);
                    handler.sendEmptyMessage(TAG_DATA_READY);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(TAG_ERROR);
                }
            }
        });
    }

    public static void getTopMemories(Handler handler) {
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.TOP_MEMO)).get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(MEMORY_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    ArrayList<Memory> topMemories = gson.fromJson(String.valueOf(outerObj.getJSONArray("results"))
                            , new TypeToken<ArrayList<Memory>>() {
                            }.getType());
                    Message msg = new Message();
                    msg.obj = topMemories;
                    msg.what = MEMORY_DATA_READY;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(MEMORY_ERROR);
                }
            }
        });
    }

    public static void addComment(Memory memory, Comment comment, Handler handler) {
        FormBody body = new FormBody.Builder()
                .add("post", String.valueOf(memory.getId()))
                .add("text", comment.getText()).build();
        Request request = getAuthorizedRequest()
                .url(getAuthorizedUrl(URL.ADD_COMMENT))
                .post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(COMMENT_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                System.out.println(body);
//                Comment addedComment = gson.fromJson(body, Comment.class);
//                comment.setId(addedComment.getId());
//                comment.setLikes(addedComment.getLikes()); TODO: change server
                comment.setSending(false);
                handler.sendEmptyMessage(COMMENT_ADDED);
            }
        });
    }

    public static void likeComment(Comment comment) {
        FormBody body = new FormBody.Builder()
                .add("comment", String.valueOf(comment.getId())).build();
        Request request = getAuthorizedRequest()
                .url(getAuthorizedUrl(URL.LIKE_COMMENT))
                .post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(null) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 2 == 100) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
//                        Like like = new Like();
//                        like.setMemo_user(User.getCurrentUser());
//                        like.setId(jsonObject.getLong("id"));
//                        comment.addLike(like);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(response.body().string()); //TODO: debug and complete
            }
        });
    }

    public static void likePost(Memory memory, Handler handler) {
        RequestBody body = new FormBody.Builder()
                .add("post", String.valueOf(memory.getId()))
                .build();
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.LIKE_POST)).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(POST_LIKE_ERROR);
                    return;
                }
                handler.sendEmptyMessage(POST_LIKE_SUCCESSFUL);
            }
        });
    }

    public static void getUserMemories(Handler handler) {
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.USER_MEMOS)).get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(USER_MEMORY_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    ArrayList<Memory> memories = gson.fromJson(String.valueOf(outerObj.getJSONArray("results"))
                            , new TypeToken<ArrayList<Memory>>() {
                            }.getType());
                    Message msg = new Message();
                    msg.obj = memories;
                    msg.what = USER_MEMORY_DATA_READY;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(USER_MEMORY_ERROR);
                }
            }
        });
    }

    public static void getUsers(Handler handler) {
        Request request = getAuthorizedRequest().url(URL.REGISTER).get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(GET_USERS_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    ArrayList<User> users = gson.fromJson(String.valueOf(outerObj.getJSONArray("results"))
                            , new TypeToken<ArrayList<User>>() {
                            }.getType());
                    Message msg = new Message();
                    msg.obj = users;
                    msg.what = GET_USERS_SUCCESSFUL;
                    User.setAllUsers(users);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(GET_USERS_ERROR);
                }
            }
        });
    }

    public static void sendFriendRequest(long to_user_id, Handler handler) {
        RequestBody body = new FormBody.Builder()
                .add("to_user", String.valueOf(to_user_id))
                .build();
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.FRIEND_REQUEST)).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(SEND_FRIEND_REQUEST_ERROR);
                    return;
                }
                handler.sendEmptyMessage(SEND_FRIEND_REQUEST_SUCCESSFUL);
            }
        });
    }

    public static void getFriendRequests(Handler handler) {
        Request request = getAuthorizedRequest().url(getAuthorizedUrl(URL.FRIEND_REQUEST)).get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(GET_FRIEND_REQUEST_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    ArrayList<FriendRequest> friend_requests = gson.fromJson(String.valueOf(outerObj.getJSONArray("results"))
                            , new TypeToken<ArrayList<FriendRequest>>() {
                            }.getType());
                    Message msg = new Message();
                    msg.obj = friend_requests;
                    msg.what = GET_FRIEND_REQUEST_SUCCESSFUL;
                    FriendRequest.setUserFriendRequests(friend_requests);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(GET_FRIEND_REQUEST_ERROR);
                }
            }
        });
    }

}
