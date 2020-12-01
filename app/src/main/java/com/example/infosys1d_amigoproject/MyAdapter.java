package com.example.infosys1d_amigoproject;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.projectmanagement.Project;
import com.example.infosys1d_amigoproject.projectmanagement.ProjectDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <com.example.infosys1d_amigoproject.MyAdapter.myholder> implements Filterable {
    private List<Project> projectsList;
    private List<Project> projectListAll;

    public MyAdapter(List<Project> projectsList) {
        this.projectsList = projectsList;
        projectListAll = new ArrayList<>(projectsList);
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
        holder.mytext1.setText(projectsList.get(position).getProjectitle());
        holder.mytext2.setText(projectsList.get(position).getProjectdescription());
        // set up Picasso
        // holder.thumbnail.setImageResource(projectsList.get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        int length = projectsList.size();
        return length;
    }

    @Override
    public Filter getFilter() {
        return projectsFilter;
    }

    private Filter projectsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Project> filteredProjects = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredProjects.addAll(projectListAll);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Project project : projectListAll) {
                    if (project.getProjectitle().toLowerCase().contains(filterPattern)
                            || project.getProjectdescription().toLowerCase().contains(filterPattern)
                            || project.getCreatedby().toLowerCase().contains(filterPattern))
                    {
                        filteredProjects.add(project);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredProjects;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            projectsList.clear();
            projectsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class myholder extends RecyclerView.ViewHolder{

        TextView mytext1,mytext2;
        ImageView thumbnail;

        public myholder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.project_picture);
            mytext1 = itemView.findViewById(R.id.info_text);
            mytext2 = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProjectDetails.class);
                    view.getContext().startActivity(intent);
                    intent.putExtra("hello","hai");
                }
            });

        }
    }
}