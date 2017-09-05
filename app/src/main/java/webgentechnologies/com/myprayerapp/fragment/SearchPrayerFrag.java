package webgentechnologies.com.myprayerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import webgentechnologies.com.myprayerapp.R;
//import com.ramotion.foldingcell.FoldingCell;

/**
 * Created by Kaiser on 28-07-2017.
 */

public class SearchPrayerFrag extends Fragment {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_search_prayer, container, false);
        setCustomDesign();
        setCustomClickListeners();
        return rootView;
    }

    private void setCustomDesign() {
    }

    private void setCustomClickListeners() {

        final TextView txt_overflow = (TextView) rootView.findViewById(R.id.txt_overflow);
        txt_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortPopUp();
            }
        });
        final ImageView img_overflow = (ImageView) rootView.findViewById(R.id.img_overflow);
        img_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortPopUp();
            }
        });

//        // get our folding cell
//        final FoldingCell fc = (FoldingCell) rootView.findViewById(R.id.folding_cell);
//        // attach click listener to folding cell
//        fc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fc.toggle(false);
//            }
//        });

    }

    private void showSortPopUp() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.sort_by_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(rootView.getContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }
}
