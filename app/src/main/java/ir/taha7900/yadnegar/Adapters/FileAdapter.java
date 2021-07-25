package ir.taha7900.yadnegar.Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.R;
import ir.taha7900.yadnegar.WebViewFragment;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private static enum FileType {
        IMAGE,
        MOVIE,
        TEXT,
        UNKNOWN,
    }

    private ArrayList<String> files;
    private MainActivity context;

    public FileAdapter(ArrayList<String> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String file = files.get(position);
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, getTypeIcon(guessFileType(file))));
        holder.filePathText.setText(file);
        holder.mainLayout.setOnClickListener(view -> context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("webView")
                .replace(R.id.mainFrame, WebViewFragment.newInstance(file))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());
    }

    private FileType guessFileType(String filePath) {
        String[] splitPath = filePath.split("\\.");
        String type = splitPath[splitPath.length - 1];
        if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpg")
                || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("gif")){
            return FileType.IMAGE;
        }else if (type.equalsIgnoreCase("mov") || type.equalsIgnoreCase("mp4")
                || type.equalsIgnoreCase("wmv") || type.equalsIgnoreCase("FLV")
                || type.equalsIgnoreCase("F4V") || type.equalsIgnoreCase("avi")){
            return FileType.MOVIE;
        }else if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")
                || type.equalsIgnoreCase("pdf") || type.equalsIgnoreCase("txt")
                || type.equalsIgnoreCase("html") || type.equalsIgnoreCase("text")){
            return FileType.TEXT;
        }else
            return FileType.UNKNOWN;
    }

    private int getTypeIcon(FileType type) {
        switch (type){
            case UNKNOWN:
                return R.drawable.ic_baseline_attach_file_24;
            case TEXT:
                return R.drawable.ic_baseline_text_24;
            case MOVIE:
                return R.drawable.ic_baseline_movie_24;
            case IMAGE:
                return R.drawable.ic_baseline_photo_24;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mainLayout;
        private ImageView icon;
        private TextView filePathText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            icon = itemView.findViewById(R.id.icon);
            filePathText = itemView.findViewById(R.id.filePathText);
        }
    }
}
