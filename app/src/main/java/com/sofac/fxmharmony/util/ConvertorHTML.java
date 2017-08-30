package com.sofac.fxmharmony.util;

/**
 * Created by Maxim on 15.08.2017.
 */

public class ConvertorHTML {
    public static String fromHTML(String html) {
        return ((html.replaceAll("<(.*?)br(.*?)>", "\n")).replaceAll("</(.*?)p(.*?)>", "\n")).replaceAll("<(.*?)>", " ");
    }

    public static String toHTML(String html) {
        return html.replaceAll("\n", "<br>");
    }

}
