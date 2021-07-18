package ir.taha7900.yadnegar.Adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Components.CommentButton;
import ir.taha7900.yadnegar.Components.LikeButton;
import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.Like;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private MainActivity context;
    private final ArrayList<Memory> memories;

    public MemoryAdapter(ArrayList<Memory> memories) {
        this.memories = memories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_memory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memory memory = memories.get(position);
        holder.usernameText.setText(memory.getCreator_user().getUsername());
        holder.fullNameText.setText(memory.getCreator_user().getFirst_name() + " " + memory.getCreator_user().getLast_name());
        holder.titleText.setText(memory.getTitle());
        holder.contentText.setText(memory.getText());
        holder.likeButton.setLiked(hasLiked(memory));
        holder.likeButton.setOnTouchListener(this::likeMemory);
        holder.commentButton.setOnTouchListener(this::openComments);
        if (memory.hasFiles())
            Glide.with(context).load(memory.getPost_files()[0]).into(holder.memoryImage);
        else
            holder.memoryImage.setVisibility(View.GONE);
    }

    private boolean openComments(View view, MotionEvent motionEvent) {
        //TODO: open comments page for the current memory
        return false;
    }

    private boolean likeMemory(View view, MotionEvent motionEvent) {
        //TODO: send toggle like request to server
        return false;
    }

    private boolean hasLiked(Memory memory) {
        for (Like like : memory.getLikes()) {
            if (like.getMemoUser().equals(User.getCurrentUser()))
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView usernameText;
        private final TextView fullNameText;
        private final TextView titleText;
        private final TextView contentText;
        private final LikeButton likeButton;
        private final ImageView memoryImage;
        private final CommentButton commentButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.fullNameText  = itemView.findViewById(R.id.fullNameText);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.titleText = itemView.findViewById(R.id.titleText);
            this.contentText = itemView.findViewById(R.id.contentText);
            this.likeButton = itemView.findViewById(R.id.likeButton);
            this.memoryImage = itemView.findViewById(R.id.memoryImage);
            this.commentButton = itemView.findViewById(R.id.commentButton);
        }
    }
}
