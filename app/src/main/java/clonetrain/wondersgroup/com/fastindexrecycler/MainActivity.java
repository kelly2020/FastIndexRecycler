package clonetrain.wondersgroup.com.fastindexrecycler;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    @Bind(R.id.fast_scroller_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.notify_indexlist)
    IndexListView indexListView;
    @Bind(R.id.button_recyclerview_index)
    Button indexBtn;

    List<ContactModel> datas = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        recyclerView = (RecyclerView) findViewById(R.id.fast_scroller_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] contacts = getResources().getStringArray(R.array.contact);

        datas = getContactData(contacts);
        Collections.sort(datas, pinyinComparator);

        adapter = new RecyclerViewAdapter(datas);
        recyclerView.setAdapter(adapter);

        //通过触摸indexlistview 来对应显示recyclerview 对应的位置
        indexListView.setOnTouchingLetterChangedListener(new IndexListView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.scrollToPosition(position);
                }
            }
        });

        indexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RecyclerViewIndexActivity.class);
                startActivity(intent);
            }
        });
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

    /**
     * 按关键字搜索
     *
     * @param searchValue
     */
    private void filterData(String searchValue) {
        List<ContactModel> filterList = new ArrayList<ContactModel>();
        if (searchValue.isEmpty()) {
            filterList = datas;
        } else {
            filterList.clear();
            for (ContactModel contactModel : datas) {
                String contactName = contactModel.getName();
                if (contactName.toUpperCase().indexOf(searchValue.toUpperCase()) != -1
                        || characterParser.getSelling(contactName).toUpperCase().
                        startsWith(searchValue.toString().toUpperCase())) {
                    filterList.add(contactModel);
                }
            }
        }
        Collections.sort(filterList, pinyinComparator);
        adapter.uploadListView(filterList);
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
