package com.chhotumaharajsupermarketbusiness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chhotumaharajsupermarketbusiness.Model.TopicWiseModel;
import com.chhotumaharajsupermarketbusiness.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TopicWiseAdapter extends RecyclerView.Adapter<TopicWiseAdapter.MyViewHolder> {

    private Context context;
    private List<TopicWiseModel> topicWiseModels;
    int pos;
    public static TopicWiseAdapter.ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public ImageView video, pdf;
        public LinearLayout viewHolder;

        public MyViewHolder(View view) {
            super(view);
            video = view.findViewById(R.id.topic_wise_video);
            name = view.findViewById(R.id.topic_wise_name);
            pdf = view.findViewById(R.id.topic_wise_pdf);
            number = view.findViewById(R.id.topic_wise_number);

        }
    }

    public TopicWiseAdapter() {

    }

    public TopicWiseAdapter(Context context, List<TopicWiseModel> topicWiseModels) {
        this.context = context;
        this.topicWiseModels = topicWiseModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // creates view holder and inflates
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_wise_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) { // binds/adds details to the views
        final TopicWiseModel item = topicWiseModels.get(position);
        holder.name.setText(item.getName());

        holder.number.setText(item.getNumber() + "");

        if (!item.getPpt().equalsIgnoreCase("null")) {
            holder.pdf.setImageResource(R.drawable.pdf);
        } else {
            holder.pdf.setImageResource(R.drawable.ic_nofile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position, "1");
                //birlagold.developer@gmail.com
                //
                //baw_x63E#HJv^r&S
            }
        });

        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position, "1");
            }
        });

        holder.pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position, "2");
            }
        });

    }

    @Override
    public int getItemCount() {
        return topicWiseModels.size();
    }

    public TopicWiseModel getWordAtPosition(int position) {
        return topicWiseModels.get(position);
    }

    public void setOnItemClickListener(TopicWiseAdapter.ClickListener clickListener) {
        TopicWiseAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position, String flag);
    }

}