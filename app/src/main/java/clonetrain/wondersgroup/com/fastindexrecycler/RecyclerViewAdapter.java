package clonetrain.wondersgroup.com.fastindexrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 16/10/31.
 * Description :
 * Version :
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {
    private List<ContactModel> mDataArray;

    public RecyclerViewAdapter(List<ContactModel> dataset) {
        mDataArray = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mTextView.setText(mDataArray.get(position).getName());

        //通过position 得到section 显示当前位置在那个区域（a~z）里面
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.sectionView.setVisibility(View.VISIBLE);
            viewHolder.sectionView.setText(mDataArray.get(position).getSortLetters());
        } else {
            viewHolder.sectionView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    public void uploadListView(List<ContactModel> list) {
        this.mDataArray = list;
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortLetters = mDataArray.get(i).getSortLetters();
            char firstChar = sortLetters.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mDataArray.get(position).getSortLetters().charAt(0);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView sectionView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view_index);
            sectionView = (TextView) itemView.findViewById(R.id.text_view_section);
        }
    }
}
