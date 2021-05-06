package com.zee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.zee.bean.MultiItemAdapter;
import com.zee.bean.RecyclerViewHolder;
import com.zee.utils.UIUtils;
import com.zee.utils.ZListUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings(value = {"unchecked", "deprecation"})
public abstract class BaseMultiZAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<? extends MultiItemType> mList = new ArrayList();

    private int curIndex;

    public BaseMultiZAdapter(List<? extends MultiItemType> list) {
        mList = list;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getBean() {
        Object object = mList.get(curIndex);
        return (T) object;
    }

    public <T extends Object> T getBean(int index) {
        Object object = mList.get(index);
        return (T) object;
    }

    public List getList() {
        return mList;
    }

    public void setList(List list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();

    }

    protected boolean isFirstItem() {
        return curIndex == 0;
    }

    public boolean isLastItem() {
        return (curIndex == mList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        curIndex = position;
        viewHolder.getMultiAdapterBean().bindObject(this, viewHolder, position, mList.get(position));
    }

    public int getCurIndex() {
        return curIndex;
    }

    /**
     * * 每一个位置的item都作为单独一项来设置 * viewType 设置为position * @param position * @return
     */
    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (ZListUtils.isNoEmpty(mList)) {
            return mList.get(position).getItemViewType();
        }
        return position;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiItemAdapter multiItemAdapter = getMultiItemAdapter(viewType);
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(multiItemAdapter.getLayoutResID(), parent, false), multiItemAdapter);
    }

    public abstract MultiItemAdapter getMultiItemAdapter(int viewType);

}
