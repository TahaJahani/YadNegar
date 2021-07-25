package ir.taha7900.yadnegar.Adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Components.CommentLikeButton;
import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.Comment;
import ir.taha7900.yadnegar.Models.Like;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;
import ir.taha7900.yadnegar.Utils.Network;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private MainActivity context;
    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.usernameText.setText(comment.getMemoUser().getUsername());
        holder.contentText.setText(comment.getText());
        holder.likeButton.setLiked(hasLiked(comment));
        holder.likeButton.setOnClickListener(view -> toggleLike(holder.likeButton.isLiked(), comment));
        holder.progressBar.setVisibility(comment.isSending() ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    private boolean hasLiked(Comment comment) {
        if (comment.getLikes() == null)
            return false;
        for (Like like : comment.getLikes()) {
            if (like.getMemoUser().equals(User.getCurrentUser()))
                return true;
        }
        return false;
    }

    private void toggleLike(boolean isLiked, Comment comment) {
        if (isLiked) {
            Network.likeComment(comment);
            Like like = new Like();
            like.setMemo_user(User.getCurrentUser());
            comment.addLike(like);
        }else {
            for (Like like : comment.getLikes()) {
                if (like.getMemoUser().equals(User.getCurrentUser())){
                    Network.deleteLikeForComment(null, like, comment);
                    break;
                }
            }
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        private CommentLikeButton likeButton;
        private ShapeableImageView profileImage;
        private TextView usernameText;
        private TextView contentText;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.likeButton = itemView.findViewById(R.id.likeButton);
            this.profileImage = itemView.findViewById(R.id.profileImage);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.contentText = itemView.findViewById(R.id.contentText);
            this.progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
