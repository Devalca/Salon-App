package com.toxic.salonapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.MyViewHolder> {

    Context mContext;
    List<SalonPost> mData ;


    public SalonAdapter(Context mContext, List<SalonPost> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.blog_list_items,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.salonTitle.setText(mData.get(position).getUser_id());
        holder.salonDesc.setText(mData.get(position).getDesc());
        holder.blogDate.setText(mData.get(position).getLokasi_direct());
        Glide.with(mContext).load(mData.get(position).getImage_url()).into(holder.blogImage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView salonTitle;
        ImageView blogImage;
        TextView salonDesc;
        TextView blogDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            salonTitle = itemView.findViewById(R.id.blog_user_name);
            blogImage = itemView.findViewById(R.id.blog_image);
            salonDesc = itemView.findViewById(R.id.blog_desc);
            blogDate = itemView.findViewById(R.id.blog_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext,PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("title",mData.get(position).getUser_id());
                    postDetailActivity.putExtra("image_url",mData.get(position).getImage_url());
                    postDetailActivity.putExtra("desc",mData.get(position).getDesc());
                    postDetailActivity.putExtra("loklok", mData.get(position).getLokasi_direct());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("timestamp",timestamp) ;
                    mContext.startActivity(postDetailActivity);

                }
            });

        }


    }
}

