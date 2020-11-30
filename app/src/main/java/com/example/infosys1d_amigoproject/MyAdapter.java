package com.example.infosys1d_amigoproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.projectmanagement.ProjectDetails;

public class MyAdapter extends RecyclerView.Adapter <com.example.infosys1d_amigoproject.MyAdapter.myholder> {
    String data1[], data2[];

    public MyAdapter(String s1[], String s2[]) {
        data1 = s1;
        data2 = s2;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row,parent,false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.mytext1.setText(data1[position]);
        holder.mytext2.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        int length = data1.length;
        return length;
    }


    public class myholder extends RecyclerView.ViewHolder{

        TextView mytext1,mytext2;

        public myholder(@NonNull View itemView) {
            super(itemView);
            mytext1 = itemView.findViewById(R.id.info_text);
            mytext2 = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProjectDetails.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
