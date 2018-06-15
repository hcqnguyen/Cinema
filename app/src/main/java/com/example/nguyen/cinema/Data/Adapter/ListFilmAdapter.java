package com.example.nguyen.cinema.Data.Adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.R;

import java.io.File;
import java.util.List;

public class ListFilmAdapter  extends RecyclerView.Adapter<ListFilmAdapter.ViewHolder>{
    private String TAG = "LIST FILM ADAPTER";
    private List<ResponeApi.Movie> mFilms;
    private Context context;
    final String DOMAIN = "https://nam-cinema.herokuapp.com";
    public ListFilmAdapter(List<ResponeApi.Movie> mFilms, Context context) {
        this.mFilms = mFilms;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewFilmTitle, mTextViewFilmGenre, mTextViewFilmRelease, mTextViewUserId;
        ImageView mImageViewFilmCover;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewFilmGenre = itemView.findViewById(R.id.text_view_film_genre);
            mTextViewFilmTitle = itemView.findViewById(R.id.text_view_film_title);
            mTextViewFilmRelease = itemView.findViewById(R.id.text_view_film_release);
            mTextViewUserId = itemView.findViewById(R.id.text_movie_ID);
            mImageViewFilmCover  = itemView.findViewById(R.id.image_view_film_cover);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_film,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ResponeApi.Movie move = mFilms.get(position);
        TextView title = holder.mTextViewFilmTitle;
        TextView genre = holder.mTextViewFilmGenre;
        TextView release = holder.mTextViewFilmRelease;
        TextView userId = holder.mTextViewUserId;
        ImageView cover = holder.mImageViewFilmCover;

        Glide.with(context)
                .load(DOMAIN +move.getCover())
                .override(400, 400)
                .error(R.drawable.ic_launcher_background)
                .into(cover);

        title.setText(move.getTitle());
        genre.setText(move.getGenre());
        release.setText(move.getRelease());
        userId.setText(move.getId());

    }
    public void updateAnswers(List<ResponeApi.Movie> items) {
        mFilms = items;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mFilms.size();
    }


}
