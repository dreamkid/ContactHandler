package com.hezd.contact.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.hezd.contact.bean.MyContact;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hezd on 2017/12/20.
 */

public class ContactUtil {
    private static ContactUtil instance = null;
    private ContactUtil(){}

    public static ContactUtil getInstance() {
        if(instance == null) {
            synchronized (ContactUtil.class) {
                if(instance == null) {
                    instance = new ContactUtil();
                }
            }
        }

        return instance;
    }

    public List<MyContact> getAllContact(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        List<MyContact> contactList = new ArrayList<>();
        while(cursor.moveToNext()) {
            MyContact contact = new MyContact();

            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int contactIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(contactIndex);

            String name = cursor.getString(nameIndex);
            if(!TextUtils.isEmpty(name)) {
                name = name.trim().replaceAll(" ","");
            }
            contact.setDisplayName(name);

            String workAddress = "" ;


            Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                    null, null);

            if(address.moveToNext()) {
                workAddress = address.getString(address.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.DATA));
                if(!TextUtils.isEmpty(workAddress)) {
                    List<String> addressList = new ArrayList<>();
                    addressList.add(workAddress);
                    contact.setAddresses(addressList);
                }
            }
            address.close();

            Cursor phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + "=" + contactId, null, null);

            List<String> numberList = new ArrayList<>();
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                if(numberList.size()<2) {
                    numberList.add(phoneNumber);
                }
            }

            phoneCursor.close();
            contact.setPhoneNumbers(numberList);

            StringBuilder sb = new StringBuilder();

            if(!TextUtils.isEmpty(name)) {
                name = name.trim().replaceAll(" ","");
                if(!isNumeric(name)) {
                    for(int i = 0;i<name.length();i++) {
                        String str= removeDigital(String.valueOf(PinyinHelper.toHanyuPinyinStringArray(name.charAt(i))[0]));
                        sb.append(str);
                    }
                }else {
                    sb.append(name);
                }
            }
            contact.setKey(name+"#"+sb.toString().toLowerCase()+"#"+workAddress+(numberList.size()==0?"":numberList.get(0)));


            contactList.add(contact);
        }
        cursor.close();

        return contactList;
    }

//    /**
//     *
//     * @param context
//     * @return
//     */
//    public List<MyContact> getContactLike(Context context,String like) {
//
//        // 如果是纯数字搜索就是搜索手机号，否则搜索姓名
//        boolean numeric = isNumeric(like);
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = null;
//        List<MyContact> contactList = new ArrayList<>();
//        if(numeric) {
//            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER +" LIKE '%"+like+"%'",
//                    null, null);
//            while(cursor.moveToNext()) {
//                MyContact contact = new MyContact();
//
//                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                int contactIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
//                String contactId = cursor.getString(contactIndex);
//
//                String name = cursor.getString(nameIndex);
//                contact.setDisplayName(name);
//
//                String workAddress ;
//
//
//                Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
//                        null, null);
//                if(address.moveToNext()) {
//                    workAddress = address.getString(address.getColumnIndex(
//                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));
//                    if(!TextUtils.isEmpty(workAddress)) {
//                        List<String> addressList = new ArrayList<>();
//                        addressList.add(workAddress);
//                        contact.setAddresses(addressList);
//                    }
//                }
//                address.close();
//
//                Cursor phoneCursor = contentResolver.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                                + "=" + contactId, null, null);
//                List<String> numberList = new ArrayList<>();
//                while (phoneCursor.moveToNext()) {
//                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
//                    if(numberList.size()<2) {
//                        numberList.add(phoneNumber);
//                    }
//                }
//                contact.setPhoneNumbers(numberList);
//                phoneCursor.close();
//
//                contactList.add(contact);
//            }
//        }else {
//
//            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null,
//                    null, null);
//            while(cursor.moveToNext()) {
//
//                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                int contactId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//
//                String name = cursor.getString(nameIndex);
//
//                // 如果仅包含字母比较麻烦 多重判断
//                // 否则直接contains匹配 比较简单
//                if(!isLetter(like)) {
//                    if(name.contains(like)) {
//                        addContact(contentResolver, contactList, contactId, name);
//                    }
//                }else {
//                    StringBuilder sb = new StringBuilder();
//                    for(int i = 0;i<name.length();i++) {
//                        String  str= removeDigital(String.valueOf(PinyinHelper.toHanyuPinyinStringArray(name.charAt(i))[0]));
//                        sb.append(str);
//                    }
//
//                    if(sb.toString().contains(like)) {
//                        addContact(contentResolver, contactList, contactId, name);
//                    }
//                }
//
//
//            }
//        }
//
//
//        cursor.close();
//
//        return contactList;
//    }

    private void addContact(ContentResolver contentResolver, List<MyContact> contactList, int contactId, String name) {
        MyContact contact = new MyContact();
        contact.setDisplayName(name);

        String workAddress ;


        Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                null, null);
        if(address.moveToNext()) {
            workAddress = address.getString(address.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.DATA));
            if(!TextUtils.isEmpty(workAddress)) {
                List<String> addressList = new ArrayList<>();
                addressList.add(workAddress);
                contact.setAddresses(addressList);
            }
        }
        address.close();

        Cursor phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + "=" + contactId, null, null);
        List<String> numberList = new ArrayList<>();
        while (phoneCursor.moveToNext()) {
            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(numberList.size()<2) {
                numberList.add(phoneNumber);
            }
        }
        contact.setPhoneNumbers(numberList);
        phoneCursor.close();

        contactList.add(contact);
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 剔除数字
     * @param value
     */
    public String removeDigital(String value){

        Pattern p = Pattern.compile("[\\d]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }

    /*判断字符串中是否仅包含字母数字和汉字
      *各种字符的unicode编码的范围：
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     *小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
*/
    public static boolean isLetter(String str) {
        String regex = "^[a-zA-Z]+$";
        return str.matches(regex);
    }
}
