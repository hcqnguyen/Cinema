package com.example.nguyen.cinema.Data.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.R;

import java.util.List;

public class UserListFilmAdapter extends RecyclerView.Adapter<UserListFilmAdapter.ViewHolder> {

    private String TAG = "USER LIST FILM ";



    private List<ResponeApi.Movie> mFilms;
    private Context context;
    final String DOMAIN = "https://nam-cinema.herokuapp.com";

    public UserListFilmAdapter(List<ResponeApi.Movie> mFils, Context context) {
        this.mFilms = mFils;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewFilmTitle;
        public ImageView mImageViewFilmCover;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewFilmTitle = itemView.findViewById(R.id.text_view_user_film_title);
            mImageViewFilmCover = itemView.findViewById(R.id.image_view_user_film_cover);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_user_film,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ResponeApi.Movie movie = mFilms.get(position);
        TextView title = holder.mTextViewFilmTitle;
        ImageView cover = holder.mImageViewFilmCover;
        Glide.with(context)
                .load(DOMAIN +movie.getCover())
                .override(200, 200)
                .error(R.drawable.ic_launcher_background)
                .into(cover);

        title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
