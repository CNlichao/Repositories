package com.docdeal.util;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.util.Log;
import com.docdeal.util.HanziToPinyin.Token;

public class PinYin {
	@SuppressLint("DefaultLocale")
	public static String getPinYin(String input) {
		Log.d("sss", String.valueOf(input.charAt(0)));
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(
				String.valueOf(input.charAt(0)));
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		Log.d("sssssssss", sb.toString());
		return String.valueOf(sb.toString().charAt(0)).toUpperCase();
	}
}
