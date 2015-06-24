package com.getniteowl.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getniteowl.R;
import com.getniteowl.activities.AlbumActivity;
import com.getniteowl.fragments.AlbumPartyFragment;
import com.getniteowl.models.*;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
public class ScrapbookAdapter extends RecyclerView.Adapter<ScrapbookAdapter.ViewHolder> {
    private List<PartyPhoto> photos;
    private Context ctx;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_scrapbook_all, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        String photoURL = photos.get(i).getPhoto().getUrl();
        viewHolder.partyName.setText(photos.get(i).getParty().getName());

        Picasso.with(ctx).load(photoURL).resize(500,500).centerCrop().into(viewHolder.image, new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.partyName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });

        final String partyId = photos.get(i).getParty().getObjectId();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.image.setTransitionName("album-" + partyId);
        }

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Party curParty = photos.get(i).getParty();
                /*
                AlbumPartyFragment destination = AlbumPartyFragment.newInstance(curParty);

                FragmentTransaction ft = ((AppCompatActivity) ctx).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AlbumPartyFragment.newInstance(curParty))
                        .addToBackStack("Albums");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    destination.setEnterTransition(TransitionInflater.from(ctx).inflateTransition(android.R.transition.explode));
                    destination.setSharedElementEnterTransition(TransitionInflater.from(ctx).inflateTransition(android.R.transition.move));
                    ft.addSharedElement(v, "album-" + partyId);
                }

                ft.commit();
                */
                Intent toAlbum = new Intent(ctx, AlbumActivity.class);
                toAlbum.putExtra("partyId", partyId);
                toAlbum.putExtra("partyName", curParty.getName());
                ctx.startActivity(toAlbum, ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity)ctx
                        , new Pair(viewHolder.image, "albumTrans")).toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView partyName;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.list_scrapbook_image);
            partyName = (TextView) itemView.findViewById(R.id.list_album_partyname);
        }
    }

    public ScrapbookAdapter(Context context) {//, List<PartyPhoto> photoList) {
        this.ctx = context;
        this.photos = new ArrayList<PartyPhoto>();
        //this.photos = photoList;
    }

    public static void GetYourParties(final ScrapbookAdapter adapter) {
        //ParseQuery<Party> partyQuery = ParseQuery.getQuery(Party.class);

        ParseQuery<PartyMember> yourParties = ParseQuery.getQuery(PartyMember.class);
        yourParties.whereMatchesQuery("member", ParseUser.getQuery().whereEqualTo("username", "Mauricio"));
        yourParties.whereMatchesQuery("party", ParseQuery.getQuery(Party.class).whereLessThan("expiresAt", new Date()));
        //yourParties.whereEqualTo("member", ParseUser.getCurrentUser());
        yourParties.include("party");

        yourParties.findInBackground(new FindCallback<PartyMember>() {
            @Override
            public void done(List<PartyMember> list, ParseException e) {
                if(e != null || list.size() == 0) {

                }
                else {
                    new PartyFirstPhotoTask(adapter).execute(list);
                }
            }
        });/*.continueWith(new Continuation<List<PartyMember>, List<PartyPhoto>>() {
            @Override
            public List<PartyPhoto> then(Task<List<PartyMember>> task) throws Exception {
                if (task.isFaulted()) {
                    throw task.getError();
                }
                List<PartyPhoto> photoTasks = new ArrayList<PartyPhoto>();
                List<PartyMember> result = task.getResult();
                for (ParseObject pm : result) {
                    Party curParty = (Party) (pm.getParseObject("party"));
                    ParseQuery<PartyPhoto> photoQuery = ParseQuery.getQuery(PartyPhoto.class).whereEqualTo("party", curParty).orderByAscending("createdAt").include("party");
                    try {
                        photoTasks.add(photoQuery.getFirst());
                    }
                    catch(ParseException e) {
                        // nothing needed
                    }
                }

                return photoTasks;
            }
        }, Task.BACKGROUND_EXECUTOR);*/
    }

    static class PartyFirstPhotoTask extends AsyncTask<List<PartyMember>, Void, Void> {
        private ScrapbookAdapter adapter;

        public PartyFirstPhotoTask(ScrapbookAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(List<PartyMember>... params) {
            for (PartyMember pm : params[0]) {
                ParseQuery<PartyPhoto> firstPhotoQuery = ParseQuery.getQuery(PartyPhoto.class);
                firstPhotoQuery.whereEqualTo("party", pm.getParty());
                firstPhotoQuery.include("party");
                try {
                    PartyPhoto firstPhoto = firstPhotoQuery.getFirst();
                    if (firstPhoto != null) {
                        adapter.photos.add(firstPhoto);
                        publishProgress();
                    }
                } catch (ParseException e) {
                    // Probably null
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            adapter.notifyDataSetChanged();
        }
    }
}
