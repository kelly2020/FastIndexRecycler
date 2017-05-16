package clonetrain.wondersgroup.com.fastindexrecycler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import clonetrain.wondersgroup.com.fastindexrecycler.view.IndexFastScrollRecyclerView;

/**
 * Created by zhangwentao on 16/10/31.
 * Description :
 * Version :
 */
public class RecyclerViewIndexActivity extends Activity {
    @Bind(R.id.fast_scroller_recycler)
    IndexFastScrollRecyclerView recyclerView;

    private List<ContactModel> mDatas;

    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_index);
        ButterKnife.bind(this);

        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setIndexTextSize(12);
        recyclerView.setIndexBarTextColor("#FFFFFF");

        addDatas();


        RecyclerViewIndexAdapter adapter = new RecyclerViewIndexAdapter(mDatas);
        recyclerView.setAdapter(adapter);

    }

    private void addDatas() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        String[] contacts = getResources().getStringArray(R.array.contact);

        mDatas = getContactData(contacts);
        Collections.sort(mDatas, pinyinComparator);

    }

    /**
     * 填充listview 数据
     *
     * @param datas
     * @return
     */
    public List<ContactModel> getContactData(String[] datas) {
        List<ContactModel> contactModels = new ArrayList<ContactModel>();
        for (int i = 0; i < datas.length; i++) {
            ContactModel contactModel = new ContactModel();
            contactModel.setName(datas[i]);

            String pingying = characterParser.getSelling(datas[i]);
            String sortString = pingying.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                contactModel.setSortLetters(sortString.toUpperCase());
            } else {
                contactModel.setSortLetters("#");
            }
            contactModels.add(contactModel);
        }

        return contactModels;
    }


    public class PinyinComparator implements Comparator<ContactModel> {

        public int compare(ContactModel o1, ContactModel o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (o1.getSortLetters().equals("#")) {
                return -1;
            } else if (o2.getSortLetters().equals("#")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }

    }

}
