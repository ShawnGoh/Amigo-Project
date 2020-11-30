package com.example.infosys1d_amigoproject.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;

import java.util.ArrayList;

public class OnboardingViewPagerAdapter extends RecyclerView.Adapter<OnboardingViewPagerAdapter.ViewHolder> {


    Context context;
    ArrayList<OnboardingItem> OnboardingList;



    public OnboardingViewPagerAdapter(Context context, ArrayList<OnboardingItem> onboardingList) {
        this.context = context;
        OnboardingList = onboardingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.onboarding_card_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(OnboardingList.get(position).getTitle());
        holder.desc.setText(OnboardingList.get(position).getDescription());
        holder.img.setImageResource(OnboardingList.get(position).getImage());
    }


//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.title.setText(OnboardingList.get(position).getTitle());
//        holder.desc.setText(OnboardingList.get(position).getDescription());
//        holder.img.setImageResource(OnboardingList.get(position).getImage());
//    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.OnboardingImage);
            title = itemView.findViewById(R.id.OnboardingTitle);
            desc = itemView.findViewById(R.id.OnboardingDesc);

        }
    }


    @Override
    public int getItemCount() {
        return OnboardingList.size();
    }
}
