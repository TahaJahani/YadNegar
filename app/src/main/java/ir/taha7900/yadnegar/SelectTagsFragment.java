package ir.taha7900.yadnegar;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.TagSelectionAdapter;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class SelectTagsFragment extends Fragment {

    private ArrayList<Long> selectedTags;
    private MaterialButton doneButton;
    private RecyclerView tagsList;
    private TagSelectionAdapter adapter;
    private Handler handler;
    private MainActivity context;

    public SelectTagsFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.TAG_DATA_READY:
                        showTags();
                        break;
                }
            }
        };
    }

    public static SelectTagsFragment newInstance(ArrayList<Long> tags) {
        SelectTagsFragment fragment = new SelectTagsFragment();
        fragment.selectedTags = tags;
        return fragment;
    }

    private void showTags() {
        context.showLoading(false);
        adapter = new TagSelectionAdapter(Tag.getUserTags(), selectedTags);
        tagsList.setLayoutManager(new GridLayoutManager(context, 4));
        tagsList.addItemDecoration(new SpacesItemDecoration(4));
        tagsList.setAdapter(adapter);
    }

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}