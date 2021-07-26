package ir.taha7900.yadnegar.Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Models.User;

public class DataProvider {
    private static final String USER_FILE = "user_file";
    private static final String MEMORY_FILE = "memory_file";
    private static final String TAG_FILE = "tag_file";

    public static void saveData(Context context) {
        Gson gson = new Gson();
        saveUser(context, gson);
        saveMemories(context, gson);
        saveTags(context, gson);
    }

    public static void saveUser(Context context, Gson gson) {
        try {
            ObjectOutputStream userOutputStream = new ObjectOutputStream(context
                    .openFileOutput(USER_FILE, Context.MODE_PRIVATE));
            userOutputStream.writeUTF(gson.toJson(User.getCurrentUser()));
            userOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void saveMemories(Context context, Gson gson) {
        try {
            ObjectOutputStream memoryOutputStream = new ObjectOutputStream(context
                    .openFileOutput(MEMORY_FILE, Context.MODE_PRIVATE));
            memoryOutputStream.writeUTF(gson.toJson(Memory.getUserMemories()));
            memoryOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void saveTags(Context context, Gson gson) {
        try {
            ObjectOutputStream tagOutputStream = new ObjectOutputStream(context
                    .openFileOutput(TAG_FILE, Context.MODE_PRIVATE));
            tagOutputStream.writeUTF(gson.toJson(Tag.getUserTags()));
            tagOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean loadData(Context context) {
        try {
            Gson gson = new Gson();
            File userFile = new File(context.getFilesDir(), USER_FILE);
            File tagFile = new File(context.getFilesDir(), TAG_FILE);
            File memoryFile = new File(context.getFilesDir(), MEMORY_FILE);
            if (userFile.exists()) {
                ObjectInputStream userInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userFile)));
                String userJson = userInputStream.readUTF();
                userInputStream.close();
                User.setCurrentUser(gson.fromJson(userJson, User.class));

                ObjectInputStream tagInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tagFile)));
                String tagJson = tagInputStream.readUTF();
                tagInputStream.close();
                Tag.setUserTags(gson.fromJson(tagJson, new TypeToken<ArrayList<Tag>>() {
                }.getType()));

                ObjectInputStream memoryInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(memoryFile)));
                String memoryJson = memoryInputStream.readUTF();
                memoryInputStream.close();
                Memory.setUserMemories(gson.fromJson(memoryJson, new TypeToken<ArrayList<Memory>>() {
                }.getType()));
                return true;
            } else
                return false;
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
