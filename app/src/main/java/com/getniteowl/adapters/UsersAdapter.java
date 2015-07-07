package com.getniteowl.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getniteowl.R;
import com.getniteowl.activities.ProfileActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<ParseUser> friends;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_friends, viewGroup, false);
        rootView.setClickable(true);
        return new ViewHolder(rootView, context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        /*
        ParseUser friend = friends.get(i).getParseUser("user");
        if(friend == ParseUser.getCurrentUser()) {
            friend = friends.get(i).getParseUser("friend");
        }*/
        final ParseUser friend = friends.get(i);
        viewHolder.friendUsername.setText(friend.getUsername());

        ParseFile profile = friend.getParseFile("profile_photo");
        if(profile != null) {
            /*
            Picasso.with(context).load(profile.getUrl()).resize(96, 96).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable profileDrawable = new BitmapDrawable(context.getResources(), bitmap);
                    viewHolder.friendUsername.setCompoundDrawablesWithIntrinsicBounds(profileDrawable, null, null, null);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            */
            Picasso.with(context).load(profile.getUrl()).resize(256, 256).centerCrop().into(viewHolder.friendPhoto);
        }
        else {
            //Picasso.with(context).load(android.R.drawable.ic_lock_power_off).resize(256, 256).centerCrop().into(viewHolder.friendPhoto);
            viewHolder.friendPhoto.setImageResource(android.R.drawable.ic_lock_power_off);
            //viewHolder.friendUsername.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(android.R.drawable.ic_lock_power_off), null, null, null);
        }
        //viewHolder.friendUsername.setCompoundDrawablePadding(16);

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(context, ProfileActivity.class);
                toProfile.putExtra("userId", friend.getObjectId());

                context.startActivity(toProfile,
                        ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity) context
                                , new Pair(viewHolder.friendPhoto, "profilePhoto")).toBundle()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public UsersAdapter(Context ctx, List<ParseUser> friends) {
        this.context = ctx;
        this.friends = friends;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView friendUsername;
        ImageView friendPhoto;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            rootView = itemView;
            friendUsername = (TextView) itemView.findViewById(R.id.friend_username);
            friendPhoto = (ImageView) itemView.findViewById(R.id.friend_photo);
        }
    }

}
