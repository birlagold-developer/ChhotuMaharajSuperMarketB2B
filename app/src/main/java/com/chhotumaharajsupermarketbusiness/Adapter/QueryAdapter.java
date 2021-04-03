package com.chhotumaharajsupermarketbusiness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.chhotumaharajsupermarketbusiness.Model.QueryModel;
import com.chhotumaharajsupermarketbusiness.Model.SubQueryModel;
import com.chhotumaharajsupermarketbusiness.R;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.MyViewHolder> {

    private Context context;
    private QueryModel queryModel;
    public static QueryAdapter.ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;


        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.query_checkbox);

        }
    }

    public QueryAdapter(Context context, QueryModel queryModel) {
        this.context = context;
        this.queryModel = queryModel;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // creates view holder and inflates
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) { // binds/adds details to the views
        final SubQueryModel item = queryModel.getSubQueryModels().get(position);

        holder.checkBox.setText(item.getVideo_query());

        holder.checkBox.setChecked(item.isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                item.setSelected(isChecked);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return queryModel.getSubQueryModels().size();
    }

    public SubQueryModel getWordAtPosition(int position) {
        return queryModel.getSubQueryModels().get(position);
    }

    public void setOnItemClickListener(QueryAdapter.ClickListener clickListener) {
        QueryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position, String flag);
    }

}