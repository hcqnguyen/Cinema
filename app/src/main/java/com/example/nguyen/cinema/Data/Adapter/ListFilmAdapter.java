package com.example.nguyen.cinema.Data.Adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.VNCharacterUtils;
import com.example.nguyen.cinema.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListFilmAdapter  extends RecyclerView.Adapter<ListFilmAdapter.ViewHolder>{
    private String TAG = "LIST FILM ADAPTER";
    private ArrayList<ResponeApi.Movie> mFilms;
    private ArrayList<ResponeApi.Movie> mListBackup;
    private Context context;
    final String DOMAIN = "https://nam-cinema.herokuapp.com";
    onClickRecyclerView mOnClickRecyclerView;

    public ListFilmAdapter(ArrayList<ResponeApi.Movie> mFilms, Context context,onClickRecyclerView mOnClickRecyclerView) {
        this.mFilms = mFilms;
        this.context = context;
        this.mOnClickRecyclerView = mOnClickRecyclerView;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickRecyclerView.onClickItem(getAdapterPosition());
                }
            });

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        backupData(mFilms);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_film,parent,false);
        return new ViewHolder(itemView);

    }
    public void backupData(ArrayList<ResponeApi.Movie> data) {
        mListBackup = new ArrayList<>() ;
        for(ResponeApi.Movie movie:data)
            mListBackup.add(movie.clone());
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
    public void updateAnswers(ArrayList<ResponeApi.Movie> items) {
        mFilms = items;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    public interface onClickRecyclerView{
        void onClickItem(int position);
    }
    public void filter(String charText){
        if (TextUtils.isEmpty(charText)) {
            mFilms.clear();
            mFilms.addAll(mListBackup);
            notifyDataSetChanged();
        }
        else{
            charText = charText.toLowerCase(Locale.getDefault());
            mFilms.clear();
            for (ResponeApi.Movie s : mListBackup) {
                if (s.getTitle().toLowerCase(Locale.getDefault()).contains(charText)
                        || VNCharacterUtils.removeAccent(s.getTitle().toLowerCase(Locale.getDefault())).contains(charText)) {
                    mFilms.add(s.clone());
                }
            }
            notifyDataSetChanged();
        }
    }


}
