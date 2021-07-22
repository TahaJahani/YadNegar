package ir.taha7900.yadnegar.Adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.R;
import ir.taha7900.yadnegar.Utils.AndroidUtilities;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    protected ArrayList<Tag> tags;
    protected MainActivity context;

    public TagAdapter(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        TextView titleText = new TextView(context);
        return new ViewHolder(titleText);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.titleText.setBackgroundResource(R.drawable.background_tag);
        GradientDrawable background = (GradientDrawable) holder.titleText.getBackground();
        int backgroundColor = Color.parseColor("#" + tag.getColor());
        int elevation = (int) AndroidUtilities.dp(4);
        background.setColor(backgroundColor);
        holder.titleText.setElevation(elevation);
        holder.titleText.setText(tag.getName());
    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView titleText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleText = (TextView) itemView;
            titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }
}
