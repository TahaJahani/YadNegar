package ir.taha7900.yadnegar.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Random;

import ir.taha7900.yadnegar.CommentsFragment;
import ir.taha7900.yadnegar.Components.CommentButton;
import ir.taha7900.yadnegar.Components.LikeButton;
import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.Comment;
import ir.taha7900.yadnegar.Models.Like;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private MainActivity context;
    private final ArrayList<Memory> memories;
    private String[] colorsList;

    public MemoryAdapter(ArrayList<Memory> memories) {
        this.memories = memories;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        this.colorsList = context.getResources().getStringArray(R.array.profileColors);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.new_row_memory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memory memory = memories.get(position);
        holder.titleText.setText(memory.getTitle());
        holder.likeButton.setLiked(hasLiked(memory));
        holder.likeButton.setOnTouchListener(this::likeMemory);
        holder.nameAndDateText.setText(memory.getCreator_user().getFirst_name() + " - " + memory.getCreated());
//        holder.numberOfLikesText.setText(memory.getLikes().length);
        holder.seeMoreButton.setOnClickListener(view -> {
            // TODO: Open memory page!
        });
    }

    private boolean likeMemory(View view, MotionEvent motionEvent) {
        //TODO: send toggle like request to server
        return false;
    }

    private void openComments(Memory memory) {
        CommentsFragment fragment = CommentsFragment.newInstance(memory);
        context.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("comments")
                .replace(R.id.mainFrame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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

        private final TextView nameAndDateText;
        private final TextView titleText;
        private final TextView numberOfLikesText;
        private final Button seeMoreButton;
        private final LikeButton likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameAndDateText  = itemView.findViewById(R.id.newRowMemory_nameAndDateText);
            this.titleText = itemView.findViewById(R.id.newRowMemory_titleText);
            this.numberOfLikesText = itemView.findViewById(R.id.newRowMemory_numberOfLikesText);
            this.seeMoreButton = itemView.findViewById(R.id.newRowMemory_seeMoreButton);
            this.likeButton = itemView.findViewById(R.id.newRowMemory_likeButton);
        }
    }
}
