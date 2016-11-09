package com.sf.SFSample.ui;

import android.view.View;
import android.widget.AdapterView;

import com.basesmartframe.baseadapter.BaseAdapterHelper;
import com.basesmartframe.baseui.BasePullListFragment;
import com.sf.SFSample.R;
import com.sflib.reflection.core.ThreadHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NetEase on 2016/8/10 0010.
 */
public class MessagePullListFragment extends BasePullListFragment<Object> {
    private int mCount = 1;

    @Override
    protected boolean onRefresh() {
       final List<Object> students = new ArrayList<>();
        mCount=1;
        for (; mCount < 10; mCount++) {
            students.add(new Student("students" + mCount, 100 * mCount));
        }
       ThreadHelp.runInMain(new Runnable() {
           @Override
           public void run() {
               finishRefreshOrLoading(students, true);
           }
       },500);
        return true;
    }

    @Override
    protected boolean onLoadMore() {
        final List<Object> students = new ArrayList<>();
        students.add(new Student("students" + mCount, 100 * mCount));
        mCount++;
        ThreadHelp.runInMain(new Runnable() {
            @Override
            public void run() {
                finishRefreshOrLoading(students, true);
            }
        },500);
        return true;
    }

    @Override
    protected int[] getLayoutIds() {
        return new int[]{
                R.layout.sf_chat_item
        };
    }

    @Override
    protected int getViewType(int position) {
        return super.getViewType(position);
    }

    @Override
    protected void bindView(BaseAdapterHelper help, int position, Object bean) {

        if(bean instanceof Student){
            Student student= (Student) bean;
            help.setText(R.id.chat_tv,student.name);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
