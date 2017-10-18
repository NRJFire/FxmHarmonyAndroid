package com.sofac.fxmharmony.util;

/**
 * Created by Maxim on 15.08.2017.
 */

public class ConvertorHTML {
    public static String fromHTML(String html) {
        return (html
                .replaceAll("<(.*?)br(.*?)>", "\n")
//                .replaceAll("</(.*?)p(.*?)>", "\n")
                .replaceAll("<(.*?)>", "")
                .replaceAll("&nbsp;"," ")
                .replaceAll("&lt;","<")
                .replaceAll("&gt;",">")
                .replaceAll("&quot;","\"")
                .replaceAll("&apos;","'")
                .replaceAll("&amp;","&"));
    }

    public static String toHTML(String html) {
        return html.replaceAll("\n", "<br>");
    }

}
