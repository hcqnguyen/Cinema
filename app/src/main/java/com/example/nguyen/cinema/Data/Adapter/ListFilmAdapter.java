package com.example.nguyen.cinema.Data.Adapter;

import android.content.Context;
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
import com.example.nguyen.cinema.R;

import java.io.File;
import java.util.List;

public class ListFilmAdapter  extends RecyclerView.Adapter<ListFilmAdapter.ViewHolder>{
    private String TAG = "LIST FILM ADAPTER";
    private List<Film> mFilms;
    private Context context;

    public ListFilmAdapter(List<Film> mFilms, Context context) {
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
            mTextViewUserId = itemView.findViewById(R.id.text_view_user_ID);
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
        final Film film = mFilms.get(position);
        TextView title = holder.mTextViewFilmTitle;
        TextView genre = holder.mTextViewFilmGenre;
        TextView release = holder.mTextViewFilmRelease;
        TextView userId = holder.mTextViewUserId;
        ImageView cover = holder.mImageViewFilmCover;

        Glide.with(context)
                .load("http://androidcoban.com/wp-content/uploads/2016/07/hoc_lap_trinh_android.png")
                .override(100, 100)
                .error(R.drawable.ic_launcher_background)
                .into(cover);

        title.setText(film.getmTitle());
        genre.setText(film.getmGenre());
        release.setText(film.getmRelease());
        userId.setText(film.getmIdUser());

    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }


}
