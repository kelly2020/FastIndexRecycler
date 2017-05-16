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
public class RecyclerViewIndexAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {

    private List<ContactModel> mDataArray;
    private ArrayList<Integer> mSectionPositions;

    RecyclerViewIndexAdapter(List<ContactModel> dataArray){
        mDataArray = dataArray;

    }
    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).getSortLetters().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mTextView.setText(mDataArray.get(position).getName());

    }


    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView sectionView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view_index);
            sectionView = (TextView) itemView.findViewById(R.id.text_view_section);
        }
    }
}