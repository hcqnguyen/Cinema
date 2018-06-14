package com.example.nguyen.cinema.Data.Model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponeApi {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("movies")
    private ArrayList<Movie> movies;

    public class Movie {
        @SerializedName("_id")
        private String id;

        @SerializedName("title")
        private String title;
        @SerializedName("genre")
        @Expose
        private String genre;
        @SerializedName("release")
        @Expose
        private String release;
        @SerializedName("description")
        @Expose
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
        @Expose

        private String cover;
        @SerializedName("creator")
        @Expose
        private Creator creator;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("__v")
        @Expose
        private Integer v;

        public Creator getCreator() {

            return creator;
        }

        public void setCreator(Creator creator) {

            this.creator = creator;
        }
        public class Creator{
            @SerializedName("avatar")
            @Expose
            private String avatar;
            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("username")
            @Expose
            private String username;

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
