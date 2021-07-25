package ir.taha7900.yadnegar.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Components.LikeButton;
import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.MemoryFragment;
import ir.taha7900.yadnegar.Models.Like;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;
import ir.taha7900.yadnegar.Utils.Network;

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
        View view = inflater.inflate(R.layout.new_row_memory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memory memory = memories.get(position);
        holder.titleText.setText(memory.getTitle());
        holder.likeButton.setLiked(hasLiked(memory));
        holder.likeButton.setOnClickListener(view -> toggleLike(holder.likeButton.isLiked(), memory));
        holder.nameAndDateText.setText(memory.getCreatorUser().getFirst_name() + " - " + (memory.getCreated().contains("T") ? memory.getCreated().split("T")[0] : memory.getCreated()));
        holder.seeMoreButton.setOnClickListener(view -> {
            context.getSupportFragmentManager().beginTransaction()
                    .addToBackStack("memoryPage")
                    .replace(R.id.mainFrame, MemoryFragment.newInstance(memory))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        });
    }

    private void toggleLike(boolean like, Memory memory) {
        if (like) {
            Network.likePost(memory, null);
            Like likeObject = new Like();
            likeObject.setMemo_user(User.getCurrentUser());
            memory.addLike(likeObject);
        }else {
            for (Like likeObject : memory.getLikes()) {
                if (likeObject.getMemoUser().equals(User.getCurrentUser())) {
                    Network.deleteLikeForMemory(null, likeObject, memory);
                    break;
                }
            }
        }
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
            this.numberOfLikesText = itemView.findViewById(R.id.newRowMemory_numberOfLikesTextView);
            this.seeMoreButton = itemView.findViewById(R.id.newRowMemory_seeMoreButton);
            this.likeButton = itemView.findViewById(R.id.newRowMemory_likeButton);
        }
    }
}
