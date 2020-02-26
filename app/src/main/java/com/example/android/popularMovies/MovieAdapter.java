package com.example.android.popularMovies;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.popularMovies.databinding.MovieGridItemBinding;
import com.example.android.popularMovies.databinding.MovieReviewItemBinding;
import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


       private static final String TAG = MovieAdapter.class.getSimpleName();

        private List<Movie> mMovieList;
        private final MovieAdapterClickListener mAdapterListener;
        private final int mViewHolderItemHeight;


        public MovieAdapter(MovieAdapterClickListener listener, int height) {
            mAdapterListener = listener;
            this.mViewHolderItemHeight = height;
        }


        public void setmMovieList(List<Movie> mMovieList) {

            this.mMovieList = mMovieList;
            notifyDataSetChanged();
        }

        public List<Movie> getmMovieList() {
            return mMovieList;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            MovieGridItemBinding itemBinding = MovieGridItemBinding.inflate(layoutInflater,parent,false);
            return new MovieViewHolder( itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

            Movie movie = mMovieList.get(position);
            String posterPath = movie.getPosterPath();
            Uri uri = NetworkUtils.buildMoviePosterUrl(posterPath);
            holder.bindMoviePosterPath(uri);

        }

        @Override
        public int getItemCount() {
            if (mMovieList == null)
                return 0;
            else
                return mMovieList.size();
        }

        public interface MovieAdapterClickListener {

            void movieAdapterOnClick(int position);
        }

        public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private final MovieGridItemBinding mDataBinding;

            public MovieViewHolder(@NonNull MovieGridItemBinding binging) {

                super(binging.getRoot());
                mDataBinding = binging;
                mDataBinding.ivMoviePoster.getLayoutParams().height = mViewHolderItemHeight;
                binging.getRoot().setOnClickListener(this);

            }

            void bindMoviePosterPath(Uri posterPath) {
                if (posterPath != null) {
                    Picasso.get().load(posterPath).into(mDataBinding.ivMoviePoster);

                }
            }

            @Override
            public void onClick(View v) {

                int position = getAdapterPosition();
                mAdapterListener.movieAdapterOnClick(position);
            }

        }
    }

