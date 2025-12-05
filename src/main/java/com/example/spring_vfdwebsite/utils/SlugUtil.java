package com.example.spring_vfdwebsite.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {

    private static final Pattern DIACRITICS_PATTERN =
            Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) return "";

        String replaced = input.replaceAll("Đ", "D").replaceAll("đ", "d");

        String normalized = Normalizer.normalize(replaced, Normalizer.Form.NFD);
        String noDiacritics = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");

        return noDiacritics
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}


