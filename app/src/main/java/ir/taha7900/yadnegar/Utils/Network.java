package ir.taha7900.yadnegar.Utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ir.taha7900.yadnegar.Models.Comment;
import ir.taha7900.yadnegar.Models.FriendRequest;
import ir.taha7900.yadnegar.Models.Like;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static ir.taha7900.yadnegar.Utils.MsgCode.CHANGE_FRIEND_REQUEST_STATUS_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.CHANGE_FRIEND_REQUEST_STATUS_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.COMMENT_ADDED;
import static ir.taha7900.yadnegar.Utils.MsgCode.COMMENT_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.CREATE_POST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.CREATE_POST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.CREATE_TAG_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_COMMENT_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_COMMENT_LIKE_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_COMMENT_LIKE_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_COMMENT_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_POST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_POST_LIKE_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_POST_LIKE_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_POST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_TAG_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.DELETE_TAG_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_POST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_POST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_TAG_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_TAG_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_USER_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.EDIT_USER_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_FRIEND_REQUEST_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_FRIEND_REQUEST_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USERS_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USERS_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USER_DATA_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.GET_USER_DATA_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.LOGIN_FAILED;
import static ir.taha7900.yadnegar.Utils.MsgCode.LOGIN_SUCCESSFUL;
import static ir.taha7900.yadnegar.Utils.MsgCode.MEMORY_DATA_READY;
import static ir.taha7900.yadnegar.Utils.MsgCode.MEMORY_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.NETWORK_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.POST_FILE_ERROR;
import static ir.taha7900.yadnegar.Utils.MsgCode.POST_FILE_SUCCESSFUL;
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
        static String LOGOUT = BASE + "/api/v1/logout/";
        static String REGISTER = BASE + "/api/v1/memo-user/";
        static String TAG = BASE + "/api/v1/tag/";
        static String TOP_MEMO = BASE + "/api/v1/top-post/";
        static String USER_MEMOS = BASE + "/api/v1/post/";
        static String ADD_COMMENT = BASE + "/api/v1/comment/";
        static String LIKE_COMMENT = BASE + "/api/v1/comment-like/";
        static String LIKE_POST = BASE + "/api/v1/post-like/";
        static String FRIEND_REQUEST = BASE + "/api/v1/friend-request/";
        static String POST_FILE = BASE + "/api/v1/post-file/";
        static String SEARCH_USER = BASE + "/api/v1/memo-user/?username__contains=";
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

    private static Request.Builder addMemoTokenToHeader(Request.Builder builder) {
        User user = User.getCurrentUser();
        if (user == null)
            return builder;
        return builder.addHeader("Memouser-Token", user.getToken());
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.TAG).post(body).build();
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
                Tag.addUserTag(tag);
                handler.sendEmptyMessage(CREATE_TAG_SUCCESSFUL);
            }
        });
    }

    public static void getTags(Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.TAG).get().build();
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

    public static void deleteTag(Handler handler, Tag tag) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.TAG + tag.getId() + "/").delete().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(DELETE_TAG_ERROR);
                    return;
                }
                Tag.removeUserTag(tag);
                handler.sendEmptyMessage(DELETE_TAG_SUCCESSFUL);
            }
        });
    }

    public static void editTag(Handler handler, Tag tag, HashMap<String, String> changed_fields) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String s : changed_fields.keySet()) {
            builder.add(s, Objects.requireNonNull(changed_fields.get(s)));
        }
        RequestBody body = builder.build();
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.TAG + tag.getId() + "/").patch(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(EDIT_TAG_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                Tag newTag = gson.fromJson(body, Tag.class);
                Tag.removeUserTag(tag);
                Tag.addUserTag(newTag);
                handler.sendEmptyMessage(EDIT_TAG_SUCCESSFUL);
            }
        });
    }

    public static void getTopMemories(Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.TOP_MEMO).get().build();
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest())
                .url(URL.ADD_COMMENT)
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest())
                .url(URL.LIKE_COMMENT)
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.LIKE_POST).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    if (handler != null)
                        handler.sendEmptyMessage(POST_LIKE_ERROR);
                    return;
                }
                if (handler != null)
                    handler.sendEmptyMessage(POST_LIKE_SUCCESSFUL);
            }
        });
    }

    public static void getUserMemories(Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.USER_MEMOS).get().build();
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
                    Memory.setUserMemories(memories);
                    handler.sendEmptyMessage(USER_MEMORY_DATA_READY);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(USER_MEMORY_ERROR);
                }
            }
        });
    }

    public static void getUsers(Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.REGISTER).get().build();
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.FRIEND_REQUEST).post(body).build();
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
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.FRIEND_REQUEST).get().build();
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
                    FriendRequest.setUserFriendRequests(friend_requests);
                    handler.sendEmptyMessage(GET_FRIEND_REQUEST_SUCCESSFUL);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(GET_FRIEND_REQUEST_ERROR);
                }
            }
        });
    }

    public static void changeFriendRequestStatus(FriendRequest friendRequest, String new_status) {
        RequestBody body = new FormBody.Builder()
                .add("status", new_status)
                .build();
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.FRIEND_REQUEST + friendRequest.getId() + "/").patch(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(null) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                friendRequest.setStatus(new_status);
                if (new_status.equals(FriendRequest.STATUS_ACCEPTED)){
                    User.getCurrentUser().getFriends().add(friendRequest.getFromUser().getId());
                }
            }
        });
    }

    public static void getUserDetail(long id, Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.REGISTER + id + "/").get().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(GET_USER_DATA_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                User user = gson.fromJson(body, User.class);
                User.setCurrentUser(user);
                handler.sendEmptyMessage(GET_USER_DATA_SUCCESSFUL);
            }
        });
    }

    public static void logout() {
        RequestBody body = new FormBody.Builder().build();
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.LOGOUT).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(null) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                User.setCurrentUser(null);
                Memory.setUserMemories(null);
                Tag.setUserTags(null);
            }
        });
    }

    public static void editUser(Handler handler, HashMap<String, String> changed_fields) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String s : changed_fields.keySet()) {
            builder.add(s, Objects.requireNonNull(changed_fields.get(s)));
        }
        RequestBody body = builder.build();
        User user = User.getCurrentUser();
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.REGISTER + user.getId() + "/").patch(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    System.out.println(response.body().string());
                    handler.sendEmptyMessage(EDIT_USER_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                User newUser = gson.fromJson(body, User.class);
                User.setCurrentUser(newUser);
                handler.sendEmptyMessage(EDIT_USER_SUCCESSFUL);
            }
        });
    }

    public static void createPost(Handler handler, String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.USER_MEMOS).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    System.out.println(response.body().string());
                    handler.sendEmptyMessage(CREATE_POST_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                Memory post = gson.fromJson(body, Memory.class);
                Memory.addUserMemory(post);
                handler.sendEmptyMessage(CREATE_POST_SUCCESSFUL);
            }
        });
    }

    public static void editPost(Handler handler, String json, Memory memory) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.USER_MEMOS + memory.getId() + "/").patch(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(EDIT_POST_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                Memory post = gson.fromJson(body, Memory.class);
                Memory.removeUserMemory(memory);
                Memory.addUserMemory(post);
                handler.sendEmptyMessage(EDIT_POST_SUCCESSFUL);
            }
        });
    }

    public static void deletePost(Handler handler, Memory memory) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.USER_MEMOS + memory.getId() + "/").delete().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(DELETE_POST_ERROR);
                    return;
                }
                Memory.removeUserMemory(memory);
                handler.sendEmptyMessage(DELETE_POST_SUCCESSFUL);
            }
        });
    }

    public static void deleteComment(Handler handler, Comment comment) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.ADD_COMMENT + comment.getId() + "/").delete().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(DELETE_COMMENT_ERROR);
                    return;
                }
                for (Memory userMemory : Memory.getUserMemories()) {
                    if (userMemory.getId() == comment.getId()) {
                        userMemory.getComments().remove(comment);
                        break;
                    }
                }
                //todo check if correct <taha>
                handler.sendEmptyMessage(DELETE_COMMENT_SUCCESSFUL);
            }
        });
    }

    public static void addFileToPost(Handler handler, Memory memory, File file) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.POST_FILE + "?post=" + memory.getId()).post(body).build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code / 100 != 2) {
                    handler.sendEmptyMessage(POST_FILE_ERROR);
                    return;
                }
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject outerObj = new JSONObject(body);
                    memory.addPostFile(outerObj.getString("file"));
                    handler.sendEmptyMessage(POST_FILE_SUCCESSFUL);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(POST_FILE_ERROR);
                }
            }
        });
    }

    public static void deleteLikeForMemory(Handler handler, Like like, Memory memory) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.LIKE_POST + like.getId() + "/").delete().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    if (handler != null)
                        handler.sendEmptyMessage(DELETE_POST_LIKE_ERROR);
                    return;
                }
                memory.removeLike(like);
                if (handler != null)
                    handler.sendEmptyMessage(DELETE_POST_LIKE_SUCCESSFUL);
            }
        });
    }

    public static void deleteLikeForComment(Handler handler, Like like, Comment comment) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.LIKE_COMMENT + like.getId() + "/").delete().build();
        httpClient.newCall(request).enqueue(new CustomCallback(handler) {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code / 100 != 2) {
                    if (handler != null)
                        handler.sendEmptyMessage(DELETE_COMMENT_LIKE_ERROR);
                    return;
                }
                comment.removeLike(like);
                if (handler != null)
                    handler.sendEmptyMessage(DELETE_COMMENT_LIKE_SUCCESSFUL);
            }
        });
    }

    public static void searchUsername(String username, Handler handler) {
        Request request = addMemoTokenToHeader(getAuthorizedRequest()).url(URL.SEARCH_USER + username).get().build();
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
                    User.setAllUsers(users);
                    handler.sendEmptyMessage(GET_USERS_SUCCESSFUL);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(GET_USERS_ERROR);
                }
            }
        });
    }

}
