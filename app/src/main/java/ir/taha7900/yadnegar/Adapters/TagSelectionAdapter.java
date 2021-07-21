package ir.taha7900.yadnegar.Adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.R;

public class TagSelectionAdapter extends RecyclerView.Adapter<TagSelectionAdapter.ViewHolder> {

    private ArrayList<Tag> tags;
    private MainActivity context;

    public TagSelectionAdapter(ArrayList<Tag> tags) {
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
        background.setColor(Color.parseColor("#" + tag.getColor()));
        holder.titleText.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleText = (TextView) itemView;
        }
    }
}
