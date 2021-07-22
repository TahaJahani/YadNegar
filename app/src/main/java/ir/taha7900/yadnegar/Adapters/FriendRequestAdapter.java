package ir.taha7900.yadnegar.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.FriendRequest;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private MainActivity context;
    private ArrayList<FriendRequest> requests;

    public FriendRequestAdapter(ArrayList<FriendRequest> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest request = requests.get(position);
        holder.usernameText.setText(request.getFromUser().getUsername());
        holder.statusText.setText(request.getStatus());
        holder.dateText.setText(request.getCreated());
        holder.rejectButton.setOnClickListener(view -> rejectRequest(request));
        holder.acceptButton.setOnClickListener(view -> acceptRequest(request));
    }

    private void rejectRequest(FriendRequest request) {
        //todo
    }

    private void acceptRequest(FriendRequest request) {
        //todo
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView usernameText;
        private MaterialTextView statusText;
        private MaterialTextView dateText;
        private MaterialButton rejectButton;
        private MaterialButton acceptButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.statusText = itemView.findViewById(R.id.statusText);
            this.dateText = itemView.findViewById(R.id.dateText);
            this.rejectButton = itemView.findViewById(R.id.rejectButton);
            this.acceptButton = itemView.findViewById(R.id.acceptButton);
        }
    }
}
