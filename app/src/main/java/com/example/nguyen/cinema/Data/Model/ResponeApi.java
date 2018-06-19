package com.example.nguyen.cinema.Data.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponeApi implements Serializable{

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("movies")
    private ArrayList<Movie> movies;

    public class Movie implements Serializable{
        @SerializedName("_id")
        private String id;

        @SerializedName("title")
        private String title;
        @SerializedName("genre")

        private String genre;
        @SerializedName("release")

        private String release;
        @SerializedName("description")

        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }



        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        @SerializedName("cover")


        private String cover;
        @SerializedName("creator")

        private Creator creator;
        @SerializedName("createdAt")

        private String createdAt;
        @SerializedName("__v")

        private Integer v;

        public Creator getCreator() {

            return creator;
        }

        public void setCreator(Creator creator) {

            this.creator = creator;
        }

        public Movie clone(){
            Movie p = new Movie();
            p.setId(this.id);
            p.setTitle(this.title);
            p.setGenre(this.genre);
            p.setRelease(this.release);
            p.setDescription(this.description);
            p.setCover(this.cover);
            p.setCreatedAt(this.createdAt);
            p.setV(this.v);
            if(this.getCreator() == null){
                p.creator = null;
            }else {
                p.creator = new Creator(this.getCreator().getAvatar(), this.getCreator().getId(),
                        this.getCreator().getEmail(), this.getCreator().getUsername());
            }
            return p;
        }
        public class Creator implements Serializable{
            @SerializedName("avatar")

            private String avatar;
            @SerializedName("_id")

            private String id;
            @SerializedName("email")

            private String email;
            @SerializedName("username")

            private String username;

            public Creator(String avatar, String id, String email, String username){
                this.avatar = avatar;
                this.id = id;
                this.email = email;
                this.username = username;
            }
            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
