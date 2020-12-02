package com.example.infosys1d_amigoproject.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.example.infosys1d_amigoproject.projectmanagement_tab.ProjectDetails;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myholder> implements Filterable {
        private List<Project> projectsList;
        private List<Project> projectListAll;
        private StorageReference storageReference,projectref;

        public boolean isEmpty;

        public MyAdapter(List<Project> projectsList) {
            this.projectsList = projectsList;
            projectListAll = new ArrayList<>(projectsList);
        }
        public void setProjectsList(List<Project> projectsList) {
            this.projectsList = projectsList;
            notifyDataSetChanged();
        }

        public void setProjectListAll(List<Project> projectListAll) {
            this.projectListAll = projectListAll;
        }
        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.row, parent, false);
            Log.d(TAG, "Creating myholder");
            return new myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, int position) {
            holder.mytext1.setText(projectsList.get(position).getProjectitle());
            holder.mytext2.setText(projectsList.get(position).getProjectdescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProjectDetails.class);
                    intent.putExtra("ProjectID", projectsList.get(position).getProjectID());
                    v.getContext().startActivity(intent);
                }
            });
            Picasso.get().load(projectsList.get(position).getThumbnail()).into(holder.thumbnail);
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
        public boolean checkIsEmpty() {
            if (isEmpty == true) {
                System.out.println("HELLO I AM EMPTY LA");
            }
            return isEmpty;}
        private Filter projectsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Project> filteredProjects = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredProjects.addAll(projectListAll);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                System.out.println("HELLOOOOOO!");

                System.out.println(projectsList);
                System.out.println(projectListAll);
                System.out.println(filterPattern);
                System.out.println(filteredProjects);

                for (Project project : projectListAll) {
                    System.out.println("omG!");
                    if (project.getProjectitle().toLowerCase().contains(filterPattern)
                            || project.getProjectdescription().toLowerCase().contains(filterPattern)
                            || project.getCreatedby().toLowerCase().contains(filterPattern))
                    {
                        filteredProjects.add(project);
                        System.out.println("This works!");
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredProjects;
            if (filteredProjects.isEmpty()) {
                isEmpty = true;
                System.out.println("I'm Empty!");

            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            projectsList.clear();
            projectsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


        public class myholder extends RecyclerView.ViewHolder {

            TextView mytext1, mytext2;
            ImageView thumbnail;

            public myholder(@NonNull View itemView) {
                super(itemView);
                thumbnail = itemView.findViewById(R.id.project_picture);
                mytext1 = itemView.findViewById(R.id.info_text);
                mytext2 = itemView.findViewById(R.id.description);
            }
        }
    }


