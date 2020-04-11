package com.example.android.popularMovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularMovies.databinding.MovieReviewItemBinding;
import com.example.android.popularMovies.model.Review;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewHolder>{


    private List<Review> mReviewList;

    public void setmReviewList(List<Review> mReviewList) {
        this.mReviewList = mReviewList;
        notifyDataSetChanged();
    }

    public MovieReviewAdapter() {
      super();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieReviewItemBinding binding = MovieReviewItemBinding.inflate(layoutInflater,parent,false);
        return new ReviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        holder.bindData();
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null)
            return 0;
        else
            return mReviewList.size();

    }


    public class ReviewHolder extends RecyclerView.ViewHolder{

        private final MovieReviewItemBinding mDataBinding;

        public ReviewHolder(@NonNull MovieReviewItemBinding binding) {
            super(binding.getRoot());
            mDataBinding = binding;
        }

        void bindData(){
            int position = getAdapterPosition();
            Review review = mReviewList.get(position);
            mDataBinding.tvReviewContent.setText(review.getContent());
            mDataBinding.tvReviewAuthor.setText(review.getAuthor());

        }


    }
}
