package ru.bstu.it41.service.tenders.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderPagerFragment extends ReampFragment<TenderPagerFragmentPresenter, TenderPagerFragmentState> {
    //Элементы управления
    //private int currentTitle = R.string.title_tenders;

    private ViewPager mViewPager;

    TextView text_message;
    //

    @Override
    public void onStateChanged(TenderPagerFragmentState stateModel) {
    }

    @Override
    public ReampPresenter<TenderPagerFragmentState> onCreatePresenter() {
        return new TenderPagerFragmentPresenter();
    }

    @Override
    public TenderPagerFragmentState onCreateStateModel() {
        return new TenderPagerFragmentState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tender_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.tender_pager);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if(position == 0){
                    return FragmentRetainer.getFragment(FragmentRetainer.FRAGMENT_TENDERS,null);
                }
                else if(position == 1){
                    return FragmentRetainer.getFragment(FragmentRetainer.FRAGMENT_TENDERS_MAP,null);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }
}