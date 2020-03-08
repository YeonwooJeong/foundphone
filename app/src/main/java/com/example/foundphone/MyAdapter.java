package com.example.foundphone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<ItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_assetNumber, tv_itemNumber, tv_phoneNumber, tv_check;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_assetNumber = (TextView) itemView.findViewById(R.id.tv_assetNumber);
            tv_itemNumber = (TextView) itemView.findViewById(R.id.tv_itemNumber);
            tv_phoneNumber = (TextView) itemView.findViewById(R.id.tv_phoneNumber);
            tv_check = (TextView) itemView.findViewById(R.id.tv_check);
        }
    }

    //생성자
    public MyAdapter(ArrayList<ItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_parsing_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.tv_assetNumber.setText(String.valueOf(mList.get(position).getAssetNumber()));
        holder.tv_itemNumber.setText(String.valueOf(mList.get(position).getItemNumber()));
        holder.tv_phoneNumber.setText(String.valueOf(mList.get(position).getPhoneName()));
        holder.tv_check.setText(String.valueOf(mList.get(position).getCheck()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
