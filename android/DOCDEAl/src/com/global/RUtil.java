package com.global;

import java.lang.reflect.Field;

import com.docdeal.R;

public class RUtil {
	public static int getIdByName(String name) {
		int id = 0;
		try {
			Field field = R.id.class.getField(name);
			id = field.getInt(new R.id());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Caught " + e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Caught " + e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Caught " + e);
		}
		return id;
	}

	public static String getNameById(int id) {
		String fieldName = "";
		Field[] fields = R.id.class.getFields();
		try {
			for (Field f : fields) {

				if (id == f.getInt(new R.id())) {
					fieldName = f.getName();
					break;

				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fieldName;
	}
}
