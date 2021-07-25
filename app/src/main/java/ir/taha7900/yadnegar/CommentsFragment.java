package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;

import ir.taha7900.yadnegar.Adapters.CommentAdapter;
import ir.taha7900.yadnegar.Models.Comment;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {

    private Handler handler;
    private MainActivity context;
    private Memory memory;
    private RecyclerView commentsList;
    private TextInputEditText newCommentInput;
    private ImageButton sendButton;
    private CommentAdapter adapter;

    public static String PARAM_MEMORY = "memory";

    public CommentsFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MsgCode.COMMENT_ADDED:
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    public static CommentsFragment newInstance(Memory memory) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_MEMORY, memory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.memory = (Memory) getArguments().getSerializable(PARAM_MEMORY);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.clearTopAppBar();
        context.topAppBar.setTitle(R.string.comments);
        context.setShowNavigationIcon(false);
        context.setShowBackIcon(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        this.commentsList = view.findViewById(R.id.commentsList);
        this.newCommentInput = view.findViewById(R.id.newCommentInput);
        this.sendButton = view.findViewById(R.id.sendButton);
        this.sendButton.setOnClickListener(this::sendComment);
        adapter = new CommentAdapter(memory.getComments());
        commentsList.setLayoutManager(new LinearLayoutManager(context));
        commentsList.setAdapter(adapter);
        return view;
    }

    private void sendComment(View view) {
        if (newCommentInput.length() == 0 )
            return;
        String commentBody = newCommentInput.getText().toString().trim();
        if (commentBody.isEmpty())
            return;
        Comment comment = new Comment();
        comment.setSending(true);
        comment.setText(commentBody);
        comment.setMemoUser(User.getCurrentUser());
        memory.addComment(comment);
        Network.addComment(memory ,comment, handler);
        adapter.notifyDataSetChanged();
        newCommentInput.setText("");
    }
}