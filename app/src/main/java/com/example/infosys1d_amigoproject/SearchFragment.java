package com.example.infosys1d_amigoproject;

import android.app.SearchManager;
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
import android.widget.CompoundButton;
import android.widget.SearchView;

import com.example.infosys1d_amigoproject.projectmanagement.ExploreProjectListings;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    private Button cancelButton;
    private Context mcontext;
    private ChipGroup mfilters;
    private ArrayList<String> selectedChipData;
    private String filterString;
    private Button searchButton;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
//        String filterChipsList = getString(R.string.skill_chips_list);
//        String[] filterSplit = filterChipsList.split(" ");
        mcontext = getActivity();
        String[] filterList = mcontext.getResources().getStringArray(R.array.skills_list);

        mfilters = view.findViewById(R.id.filterChipGroup);

        LayoutInflater inflater_0 = LayoutInflater.from(mcontext);
        for(String text: filterList){
            Chip newChip = (Chip) inflater_0.inflate(R.layout.chip_filter,null,false);
            newChip.setText(text);
            mfilters.addView(newChip);}

//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) mcontext.getSystemService(Context.SEARCH_SERVICE);
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
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
//        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    selectedChipData.add(buttonView.getText().toString());
//                }
//                else{
//                    selectedChipData.remove(buttonView.getText().toString());
//                }
//            }
//        };

        for(int i = 0; i<mfilters.getChildCount(); i++){
            Chip chip = (Chip)mfilters.getChildAt(i);
            if(chip.isChecked()){
                    selectedChipData.add(chip.getText().toString());
                }
        }

//        List<Integer> ids = mfilters.getCheckedChipIds();
//        for (Integer id:ids) {
//            Chip chip = mfilters.findViewById(id);
//            selectedChipData.add(chip.getText().toString());
//        }
        return selectedChipData;

    }
}

