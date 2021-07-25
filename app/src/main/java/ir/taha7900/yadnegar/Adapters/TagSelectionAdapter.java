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
import ir.taha7900.yadnegar.Utils.AndroidUtilities;

public class TagSelectionAdapter extends TagAdapter {

    private ArrayList<Long> selected;

    public TagSelectionAdapter(ArrayList<Tag> tags, ArrayList<Long> selected) {
        super(tags);
        this.selected = selected;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.titleText.setBackgroundResource(R.drawable.background_tag);
        int padding = (int) Math.floor(AndroidUtilities.dp(6.0f));
        holder.titleText.setPadding(0, padding, 0, padding);
        GradientDrawable background = (GradientDrawable) holder.titleText.getBackground();
        int backgroundColor = Color.parseColor("#" + tag.getColor());
        int elevation = (int) AndroidUtilities.dp(4);
        if (selected.contains(tag.getId())){
            elevation = 0;
            float[] hsv = new float[3];
            Color.colorToHSV(backgroundColor, hsv);
            hsv[2] *= 0.6f;
            backgroundColor = Color.HSVToColor(hsv);
        }
        background.setColor(backgroundColor);
        holder.titleText.setElevation(elevation);
        holder.titleText.setText(tag.getName());
        holder.titleText.setOnClickListener(view -> {
            if (selected.contains(tag.getId()))
                selected.remove(tag.getId());
            else
                selected.add(tag.getId());
            notifyDataSetChanged();
        });
    }
}
