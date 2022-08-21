import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class E08 {
    private static boolean isConsonant(char c) {
        boolean isVowel = c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
        return 'a' <= c && c <= 'z' && !isVowel;
    }

    private static void p(Object label, Object value) {
        System.out.print(label.toString() + "(" + value.toString() + ") ");
    }

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String name = s.nextLine().trim();
                if (name.equals("FIM")) {
                    break;
                }
                String rawUrl = s.nextLine().trim();
                URL url = new URL(rawUrl);

                VowelMap vowelMap = new VowelMap();
                int consonantCount = 0;
                int htmlBrCount = 0;
                int htmlTableCount = 0;

                //
                // Ingest
                //

                try (
                        InputStream stream = url.openStream();
                        Scanner html = new Scanner(stream)) {
                    while (html.hasNextLine()) {
                        // This is an ideal scenario, but I'll assume that we won't have tags spanning
                        // multiple lines. Otherwise for reliability's sake, I would have to implement a
                        // mini HTML parser.
                        String line = html.nextLine();

                        for (char c : line.toCharArray()) {
                            if (isConsonant(c)) {
                                consonantCount++;
                            } else {
                                vowelMap.putOrIgnore(c);
                            }
                        }

                        for (String tag : TagName.extract(line)) {
                            switch (tag) {
                                case "br":
                                    htmlBrCount++;
                                    break;
                                case "table":
                                    htmlTableCount++;
                                    break;
                            }
                        }
                    }
                }

                //
                // Report
                //

                vowelMap.forEach((c, n) -> p(c, n));
                p("consoante", consonantCount);
                p("<br>", htmlBrCount);
                p("<table>", htmlTableCount);
                System.out.println(name);
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}

class VowelMap {
    private static int[] ALLOWED_CHAR_CODE_POINTS = {
            97, // 'a'
            101, // 'e'
            105, // 'i'
            111, // 'o'
            117, // 'u'
            225, // 'á'
            233, // 'é'
            237, // 'í'
            243, // 'ó'
            250, // 'ú'
            224, // 'à'
            232, // 'è'
            236, // 'ì'
            242, // 'ò'
            249, // 'ù'
            227, // 'ã'
            245, // 'õ'
            226, // 'â'
            234, // 'ê'
            238, // 'î'
            244, // 'ô'
            251, // 'û'
    };

    // One needs to use `LinkedHashMap` to preserve insertion order.
    private LinkedHashMap<Character, Integer> map;

    public VowelMap() {
        map = new LinkedHashMap<>();
        for (int c : ALLOWED_CHAR_CODE_POINTS) {
            map.put((char) c, 0);
        }
    }

    public void putOrIgnore(char c) {
        // char normalized = Character.toLowerCase(c);
        char normalized = c;
        if (map.containsKey(normalized)) {
            map.put(normalized, map.get(normalized) + 1);
        }
    }

    public void forEach(BiConsumer<Character, Integer> fn) {
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            fn.accept(e.getKey(), e.getValue());
        }
    }
}

class TagName {
    // One definitely shouldn't use regex for HTML parsing, but who cares?
    // private static Pattern RE = Pattern.compile("<(?<tag>\\w+).*?>");
    private static Pattern RE = Pattern.compile("<(?<tag>\\w+)>");

    public static List<String> extract(String html) {
        Matcher m = RE.matcher(html);
        List<String> tags = new ArrayList<String>();
        while (m.find()) {
            tags.add(m.group("tag"));
        }
        return tags;
    }
}
