package com.example.myworkout.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.fragments.ImagesFragment;
import com.example.myworkout.helpers.GlideApp;
import com.example.myworkout.helpers.MyAppGlideModule;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private OnItemClickListener mListener;
    private ArrayList<String> urlList;
    final private String URL_PREFIX = "https://tusk.systems/trainingapp/icons/";
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconView;

        public MyViewHolder(View itemView, final GalleryAdapter.OnItemClickListener listener) {
            super(itemView);
            iconView = itemView.findViewById(R.id.gridIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public GalleryAdapter(ArrayList<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_gallery_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String currentIcon = URL_PREFIX + urlList.get(position);
        GlideApp.with(holder.itemView.getContext()).load(currentIcon).into(holder.iconView);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }
}
