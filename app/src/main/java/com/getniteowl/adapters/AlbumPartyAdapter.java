package com.getniteowl.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.getniteowl.R;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
public class AlbumPartyAdapter extends RecyclerView.Adapter<AlbumPartyAdapter.ViewHolder> {
    private Context ctx;
    private Party party;
    private List<PartyPhoto> photos;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_scrapbook_all, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        String imageURL = photos.get(i).getPhoto().getUrl();
        Picasso.with(ctx).load(imageURL).resize(500, 500).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public AlbumPartyAdapter(Context context, List<PartyPhoto> photos) {
        this.ctx = context;
        this.photos = photos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.list_scrapbook_image);
        }
    }
}
