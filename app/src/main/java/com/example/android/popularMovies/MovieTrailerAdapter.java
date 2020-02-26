package com.example.android.popularMovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.popularMovies.databinding.MovieTrailerItemBinding;
import com.example.android.popularMovies.model.Trailer;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerHolder>{


    private static final String TAG = MovieTrailerAdapter.class.getSimpleName();

    private List<Trailer> mTrailerList;
    private PlayTrailerListener mListener;


    public void setmTrailerList(List<Trailer> mTrailerList) {
        this.mTrailerList = mTrailerList;
        notifyDataSetChanged();
    }

    public List<Trailer> getmTrailerList() {
        return mTrailerList;
    }

    public MovieTrailerAdapter(PlayTrailerListener listener) {
        super();
        mListener = listener;

    }

    public interface PlayTrailerListener{
        void playTapped(int position);
        void movieTrailerAdapterItemClicked(int position);
    }

    @NonNull
    @Override
    public MovieTrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieTrailerItemBinding binding = MovieTrailerItemBinding.inflate(inflater,parent,false);
        return new MovieTrailerHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerHolder holder, int position) {
       String text = holder.itemView.getContext().getString(R.string.trailer_no_text_view);
       String trailerNo = String.format(text, Integer.toString(position+1) );
       holder.setTrailerLabel(trailerNo);

    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null)
        {
            return 0;
        }else{
            return  mTrailerList.size();
        }
    }

    public class MovieTrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final MovieTrailerItemBinding mDataBinding;


        MovieTrailerHolder(@NonNull  MovieTrailerItemBinding binding) {
            super( binding.getRoot());

            mDataBinding = binding;
            mDataBinding.buttonTrailerPlay.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);

        }

        void setTrailerLabel(String ch){
            mDataBinding.tvTrailerNoLabel.setText(ch);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.playTapped(position);
            mListener.movieTrailerAdapterItemClicked(position);
        }
    }
}
