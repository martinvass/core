package com.github.martinyes.cape.preview.util;

public class PreviewUtils {

    /**
     * Removes all spaces if given String doesn't contain '&' char.
     * Citizens seem to auto color all name tags so removing spacing from name with '&' would colour it.
     *
     * @param source - Username to remove spacing
     * @return - Username without spacing or given String with additional space at the end
     */
    public static String removeSpacing(String source) {
        if (source.contains("&")) {
            //Red & BlueCape -> Red & Blue Cape
            return source += " ";
        }

        return source.replace(" ", "");
    }

    /**
     * Cuts the first 16 characters if longer than 16.
     * Minecraft usernames must be between 3 and 16 characters.
     *
     * @param source - Username to cut
     * @return - Username with max 16 characters
     */
    public static String getNormalName(String source) {
        if (source.length() > 16) {
            return source.substring(0, 16);
        }

        return source;
    }
}