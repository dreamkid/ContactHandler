package com.hezd.contact.utils;



import com.hezd.contact.bean.MyContact;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author xiaanming
 *
 */
public class ChineseComparator implements Comparator<MyContact> {

	public int compare(MyContact o1, MyContact o2) {
		String displayName = String.valueOf(o1.getDisplayName().charAt(0));
		String displayName1 = String.valueOf(o2.getDisplayName().charAt(0));
		if(isNumeric(displayName)) {
			return 1;
		}

		if(isNumeric(displayName1)) {
			return -1;
		}
		String str1 = String.valueOf(PinyinHelper.toHanyuPinyinStringArray(o1.getDisplayName().charAt(0))[0].toLowerCase().charAt(0));
		String str2 = String.valueOf(PinyinHelper.toHanyuPinyinStringArray(o2.getDisplayName().charAt(0))[0].toLowerCase().charAt(0));

		if (str1.equals("@")
				|| str2.equals("#")) {
			return -1;
		} else if (str1.equals("#")
				|| str2.equals("@")) {
			return 1;
		} else {
			return str1.compareTo(str2);
		}
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

	public boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

}
