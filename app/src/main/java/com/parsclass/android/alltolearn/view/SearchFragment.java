package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.ListCategoryAdapter;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.viewmodel.CategoryViewModel;
import com.parsclass.android.alltolearn.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TAG_DEFAULT_SEARCH_FRAGMENT;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TAG_RESULT_FRAGMENT;


public class SearchFragment extends BaseFragment {


    private OnFragmentInteractionListener mListener;

    public static final String TAG="SearchFragment";
    private View rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //@BindView(R.id.CategoryRcy)
    RecyclerView categoryRcy;
    private ListCategoryAdapter adapter;
    private ArrayList<CategoryItem> categoryItems=new ArrayList<>();
    private CategoryViewModel categoryViewModel;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    private SearchView searchView;
    private MenuItem mSearchItem;
    private static  String[] SUGGESTIONS = {};
    public static final String COLUMN_NAME_SUGGESTION="title_course";
    private CursorAdapter cursorAdapter;


    private SearchViewModel searchViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  initRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_search, container, false);
            setHasOptionsMenu(true);
        }
        ButterKnife.bind(this,rootView);

        initView();
       // initRecyclerView();
        return rootView;
    }

    private void initView(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);
        //ViewCompat.setNestedScrollingEnabled(categoryRcy, false);

        //categoryViewModel=ViewModelProviders.of(this).get(CategoryViewModel.class);


        searchViewModel=ViewModelProviders.of(this).get(SearchViewModel.class);

        loadFragmentWithoutBackStack(R.id.container,new DefaultSearchFragment(),TAG_DEFAULT_SEARCH_FRAGMENT);

        final String[] from = new String[] {COLUMN_NAME_SUGGESTION};
        final int[] to = new int[] {android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    private void initRecyclerView(){

//        String[] listTitle=getResources().getStringArray(R.array.listTitleCategory);
//        for (int i=0;i<listTitle.length;i++){
//            categoryItems.add(new CategoryItem(i,listTitle[i],R.drawable.ic_course));
//        }
        categoryRcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CategoryItem> myList=new ArrayList<>();
        categoryViewModel.getFakeCategory().observe(this, new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(@Nullable List<CategoryItem> categoryItems) {
                myList.clear();
                myList.addAll(categoryItems);
                adapter=new ListCategoryAdapter(getActivity(),myList);
                categoryRcy.setAdapter(adapter);
                setCategoryOnClick();
            }
        });


    }

    private void setCategoryOnClick(){
        if(adapter!=null){
            adapter.setOnItemClickListener(new ListCategoryAdapter.OnItemClickListener() {
                @Override
                public void onItemCategoryClick(CategoryItem categoryItem) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY, categoryItem);
                    SubCategoryFragment fragment = new SubCategoryFragment();
                    fragment.setArguments(bundle);
                    loadFragment(R.id.flContent, fragment,ConstantUtil.TAG_MAIN_ACTIVITY);
                }
            });
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setIconifiedByDefault(false);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUGGESTION));
                searchView.setQuery(txt, true);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.search_action);
        searchView=(SearchView)mSearchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setIconifiedByDefault(false);
        ImageView searchIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(null);

//        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
//        ImageView magImage = (ImageView) searchView.findViewById(magId);
//        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        Log.e(TAG,"isIconfiedByDefault: "+ searchView.isIconfiedByDefault());

//        mSearchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                MyApplication.animateSearchToolbar(1, true, true,toolbar,getActivity());
//               // mSearchItem.setVisible(false);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                if (mSearchItem.isActionViewExpanded()) {
//                    MyApplication.animateSearchToolbar(1, false, false,toolbar,getActivity());
//                   // mSearchItem.setVisible(true);
//                }
//                return true;
//            }
//        });
        setupSearch();
        //super.onCreateOptionsMenu(menu,inflater);
    }

    private void setupSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG,"query: "+query);
                // processQuery(query);
                sendQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return true;
            }
        });
    }

    private void sendQuery(String query){
        Bundle bundle = new Bundle();
        bundle.putString("my_key",query);
        Fragment oldFragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_RESULT_FRAGMENT);
        if(oldFragment==null){
            oldFragment= new ShowResultSearchFragment();
            oldFragment.setArguments(bundle);
            loadFragment(R.id.container,oldFragment,TAG_RESULT_FRAGMENT);
        }else {
            oldFragment.setArguments(bundle);
            refreshFragment(oldFragment,TAG_RESULT_FRAGMENT);
        }
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DEFAULT_SEARCH_FRAGMENT);
//        if(fragment != null)
//            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void refreshFragment(Fragment fragment, String tag){
        FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    private void populateAdapter(String query) {

        ArrayList<String> suggestionList=new ArrayList<>();
        suggestionList.addAll(searchViewModel.getSuggestion(query));
        SUGGESTIONS=suggestionList.toArray(new String[suggestionList.size()]);
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, COLUMN_NAME_SUGGESTION });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        cursorAdapter.changeCursor(c);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
