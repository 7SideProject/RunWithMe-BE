package com.runwithme.runwithme.global.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwithme.runwithme.global.entity.Image;

public class ImageCache {
	public static final String DEFAULT_PROFILE = "defaultImage";
	public static final String DEFAULT_CHALLENGE = "defaultChallengeImage";
	private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();

	public static Image get(String key) {
		return imageCache.get(key);
	}

	public static void put(String key, Image image) {
		imageCache.put(key, image);
	}
}
