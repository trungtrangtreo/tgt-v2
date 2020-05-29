package ca.TransCanadaTrail.TheGreatTrail.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import ca.TransCanadaTrail.TheGreatTrail.R;
import ca.TransCanadaTrail.TheGreatTrail.activities.AchievementDetailsActivity;
import ca.TransCanadaTrail.TheGreatTrail.item.Archive;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.MyViewHolder> {

    private Context context;
    private List<Archive> list;

    public ArchiveAdapter(Context context) {
        this.context = context;
        this.list = initList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_archive, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(list.get(position));

        if (position == 0) {
            holder.ivArchiveResource.setImageResource(R.drawable.ic_download_archive_yellow);
            holder.ivArchiveResource.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_circle_yellow));
        }
        if (position == 4) {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLabelArchive;
        public ImageView ivArchiveResource;

        public MyViewHolder(View view) {
            super(view);
            ivArchiveResource = view.findViewById(R.id.ivArchiveResource);
            tvLabelArchive = view.findViewById(R.id.tvLabelArchive);
        }

        void bindData(Archive archive) {
            ivArchiveResource.setImageResource(archive.getImageResource());
            tvLabelArchive.setText(archive.getLabel());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AchievementDetailsActivity.newIntent(context, archive.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Archive> initList() {
        List<Archive> archives = new ArrayList<>();
        archives.add(new Archive(1,R.drawable.ic_download_archive_yellow, "Download"));
        archives.add(new Archive(2,R.drawable.ic_navigation, "Navigation"));
        archives.add(new Archive(3,R.drawable.ic_experience, "Experience"));
        archives.add(new Archive(4,R.drawable.ic_explorer, "Explorer"));
        archives.add(new Archive(0,R.drawable.ic_explorer, ""));
        archives.add(new Archive(5,R.drawable.ic_adventure, "Adventure"));
        archives.add(new Archive(6,R.drawable.ic_tracker, "Tracker"));
        archives.add(new Archive(7,R.drawable.ic_champion, "Champion"));
        archives.add(new Archive(8,R.drawable.ic_elevation, "Elevation"));
        return archives;
    }

}
