package com.hezd.contact.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hezd.contact.R;
import com.hezd.contact.adapter.MyContactAdapter;
import com.hezd.contact.bean.MyContact;
import com.hezd.contact.utils.ChineseComparator;
import com.hezd.contact.utils.ContactUtil;
import com.hezd.contact.widget.DividerDecoration;
import com.hezd.contact.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 76;
    private List<MyContact> contacts;
    private ChineseComparator pinyinComparator;
    private MyContactAdapter adapter;
    private TextView contactDialog;
    private SideBar sideBar;
    private RecyclerView recyclerView;
    private EditText mSearchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        setViews();
        setListeners();
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            initData();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACT_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == READ_CONTACT_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initData();
        }
    }

    private void initData() {
//        List<Contact> contacts = Contacts.getQuery().find();
        contacts = ContactUtil.getInstance().getAllContact(this);
        Collections.sort(contacts, pinyinComparator);
        adapter = new MyContactAdapter(this, contacts);
        int orientation = LinearLayoutManager.VERTICAL;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
//        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
//        recyclerView.addItemDecoration(headersDecor);
        recyclerView.addItemDecoration(new DividerDecoration(this));
    }

    private void getViews() {
        contactDialog = (TextView) findViewById(R.id.contact_dialog);
        sideBar = (SideBar) findViewById(R.id.contact_sidebar);
        recyclerView = (RecyclerView) findViewById(R.id.contact_member);
        mSearchEt = (EditText) findViewById(R.id.et_search);
    }

    private void setViews() {

        sideBar.setTextView(contactDialog);

        pinyinComparator = new ChineseComparator();
    }

    private void setListeners() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                hideSoftKeybord(MainActivity.this);
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.getLayoutManager().scrollToPosition(position);
                }
            }
        });

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mSearchEt.getText().toString();
                if(!TextUtils.isEmpty(content)) {
                    if(contacts!=null&&contacts.size()>0) {
                        List<MyContact> contactLike = getLikeList(contacts,content);
                        adapter.setData(contactLike);
                    }
                }else {
                    if(contacts == null) {
                        contacts = ContactUtil.getInstance().getAllContact(MainActivity.this);
                    }
                    adapter.setData(contacts);
                }
            }
        });
    }

    private List<MyContact> getLikeList(List<MyContact> contacts, String content) {
        List<MyContact> tempList = new ArrayList<>();
        for(MyContact contact : contacts) {
            if(contact.getKey().trim().contains(content)) {
                tempList.add(contact);
            }
        }
        return tempList;
    }

    public void hideSoftKeybord(Activity activity) {

        if (null == activity) {
            return;
        }
        try {
            final View v = activity.getWindow().peekDecorView();
            if (v != null && v.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }
}
