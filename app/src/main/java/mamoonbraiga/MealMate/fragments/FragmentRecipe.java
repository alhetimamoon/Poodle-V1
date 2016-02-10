package mamoonbraiga.MealMate.fragments;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.List;

import mamoonbraiga.MealMate.activities.MainActivity;
import mamoonbraiga.MealMate.extras.Recipe;
import mamoonbraiga.MealMate.network.VolleySingleton;
import mamoonbraiga.poodle_v3.R;


public class FragmentRecipe extends Fragment{

    private ImageView header;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private ImageLoader imageLoader=volleySingleton.getImageLoader();
    private Bundle bundle;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        final View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        //header = (ImageView) view.findViewById(R.id.recipe_header);  //setting up the header
        bundle = mainActivity.getSavedData();
        final Recipe recipe = bundle.getParcelable("recipe");

        //load the header
        //loadHeader(recipe);

        /** view pager and tab setup **/
        final MaterialViewPager vPager = (MaterialViewPager) view.findViewById(R.id.recipeViewPager);
        vPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.ColorPrimaryDark,
                                recipe.getImageUrl());
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        /** view pager and tab setup **/
        ViewPager viewPager = vPager.getViewPager();
        setUpViewPager(viewPager);
        vPager.getPagerTitleStrip().setViewPager(vPager.getViewPager());

        return view;
    }

    private void loadHeader(Recipe recipe) {
        imageLoader.get(recipe.getImageUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Drawable d = new BitmapDrawable(getResources(), response.getBitmap());
                header.setBackground(d);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new FragmentDescription(), "Description");
        adapter.addFrag(new FragmentIngredients(), "Ingredients");
        adapter.addFrag(new FragmentNutrition(), "Nutrition");
        viewPager.setAdapter(adapter);

    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter{


        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }

    }
    @Override
    public void onPause(){
        super.onPause();
    }

}