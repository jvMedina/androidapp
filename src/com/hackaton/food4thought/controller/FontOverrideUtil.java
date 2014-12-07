package com.hackaton.food4thought.controller;
import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;

public final class FontOverrideUtil {

	public static void setDefaultFont(Context context,
			String staticTypefaceFieldName, String fontAssetName) {
		final Typeface regular = Typefaces.get(context,
				fontAssetName);
		replaceFont(staticTypefaceFieldName, regular);
	}

	protected static void replaceFont(String staticTypefaceFieldName,
			final Typeface newTypeface) {
		try {
			final Field StaticField = Typeface.class
					.getDeclaredField(staticTypefaceFieldName);
			StaticField.setAccessible(true);
			StaticField.set(null, newTypeface);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}