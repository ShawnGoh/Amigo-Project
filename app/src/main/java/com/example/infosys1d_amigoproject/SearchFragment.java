package com.example.infosys1d_amigoproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;

import com.example.infosys1d_amigoproject.projectmanagement_tab.ExploreProjectListings;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

//Search fragment that is opened when the search bar is clicked on.
public class SearchFragment extends Fragment {

    private SearchView searchView;
    private Button cancelButton;
    private Context mcontext;
    private ChipGroup mfilters;
    private ArrayList<String> selectedChipData;
    private String filterString;
    private Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchBar);
        searchView.onActionViewExpanded();
        cancelButton = view.findViewById(R.id.cancelSearchButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverFragment discoverFragment = new DiscoverFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, discoverFragment);
                transaction.commit();
                MainActivity.menu_bottom.setVisibility(View.VISIBLE);
            }
        });

        mcontext = getActivity();
        String[] filterList = mcontext.getResources().getStringArray(R.array.skills_list);

        mfilters = view.findViewById(R.id.filterChipGroup);

        LayoutInflater inflater_0 = LayoutInflater.from(mcontext);
        for(String text: filterList){
            Chip newChip = (Chip) inflater_0.inflate(R.layout.chip_filter,null,false);
            newChip.setText(text);
            mfilters.addView(newChip);}

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterString = newText;
                return false;
            }
        });
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedChipData = new ArrayList<String>();
                selectedChipData = submitFilters();
                Intent resultIntent = new Intent(getActivity(), ExploreProjectListings.class);
                resultIntent.setAction(Intent.ACTION_SEARCH);
                resultIntent.putExtra("skills_filter", selectedChipData);
                resultIntent.putExtra("text_filter", filterString);
                startActivity(resultIntent);
            }
        });
        return view;
    }

    private ArrayList<String> submitFilters() {
        selectedChipData = new ArrayList<String>();

        for(int i = 0; i<mfilters.getChildCount(); i++){
            Chip chip = (Chip)mfilters.getChildAt(i);
            if(chip.isChecked()){
                    selectedChipData.add(chip.getText().toString());
                }
        }

        return selectedChipData;

    }
}

