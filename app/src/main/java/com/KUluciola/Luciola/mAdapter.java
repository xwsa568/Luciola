package com.KUluciola.Luciola;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class mAdapter extends RecyclerView.Adapter<mAdapter.MyViewHolder> {

    String data1[], data2[];
    Context context;

    public mAdapter(Context ct, String s1[], String s2[]) {
        context = ct;
        data1 = s1;
        data2 = s2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mText1.setText(data1[position]);
        holder.mText2.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mText1, mText2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.textView1);
            mText2 = itemView.findViewById(R.id.textView2);
        }
    }
}
