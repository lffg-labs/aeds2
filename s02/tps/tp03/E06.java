import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

// `Game` changelog
//
// - tp03/03 (2022-10-15)
//   - Used `setGameWebsite` to remove query string parameters.

class E06 {
    public static void main(String[] args) throws IOException {
        String datasetPath = args.length > 0 ? args[0] : "/tmp/games.csv";

        File dataset = new File(datasetPath);
        HashMap<Integer, Game> gamesDataset = new HashMap<Integer, Game>();

        try (Scanner s = new Scanner(dataset)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                Game game = Game.fromCSVLine(line);
                gamesDataset.put(game.getAppId(), game);
            }
        }

        try (Scanner s = new Scanner(System.in)) {
            ArrayList<Game> selected = new ArrayList<Game>();
            // Fill the array.
            consumeGroup(s, (rawId) -> {
                selected.add(gamesDataset.get(Integer.parseInt(rawId)));
            });

            quickSort(selected, (a, b) -> {
                int res = a.getReleaseDate().compareTo(b.getReleaseDate());
                if (res != 0) {
                    return res;
                }
                return a.getName().compareTo(b.getName());
            });

            for (Game game : selected) {
                System.out.println(game.toString());
            }
        }
    }

    private static <T> void quickSort(List<T> list, Comparator<? super T> c) {
        quickSort(list, c, 0, list.size() - 1);
    }

    private static <T> void quickSort(List<T> list, Comparator<? super T> c, int left, int right) {
        int l = left;
        int r = right;
        T pivot = list.get(left + ((right - left) / 2));
        while (l <= r) {
            while (c.compare(list.get(l), pivot) < 0) {
                l++;
            }
            while (c.compare(list.get(r), pivot) > 0) {
                r--;
            }
            if (l <= r) {
                swap(list, l++, r--);
            }
        }
        if (left < r) {
            quickSort(list, c, left, r);
        }
        if (l < right) {
            quickSort(list, c, l, right);
        }
    }

    private static <T> void swap(List<T> list, int a, int b) {
        T temp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, temp);
    }

    private static void consumeGroup(Scanner s, Consumer<String> cb) {
        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (line.equals("FIM")) {
                break;
            }
            cb.accept(line);
        }
    }
}

class Game {
    private int appId;
    private String name;
    private LocalDate releaseDate;
    private String owners;
    private int age;
    private float price;
    private int dlcCount;
    private List<String> languages;
    private String website;
    private boolean hasWindowsSupport;
    private boolean hasMacOSSupport;
    private boolean hasLinuxSupport;
    private float upvotes1;
    private float upvotes2;
    private int averagePlayTime;
    private String developers;
    private List<String> genres;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int val) {
        appId = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate val) {
        releaseDate = val;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String val) {
        owners = val;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int val) {
        age = val;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float val) {
        price = val;
    }

    public int getDlcCount() {
        return dlcCount;
    }

    public void setDlcCount(int val) {
        dlcCount = val;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> val) {
        languages = val;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String val) {
        website = val.split("\\?")[0];
    }

    public boolean getHasWindowsSupport() {
        return hasWindowsSupport;
    }

    public void setHasWindowsSupport(boolean val) {
        hasWindowsSupport = val;
    }

    public boolean getHasMacOSSupport() {
        return hasMacOSSupport;
    }

    public void setHasMacOSSupport(boolean val) {
        hasMacOSSupport = val;
    }

    public boolean getHasLinuxSupport() {
        return hasLinuxSupport;
    }

    public void setHasLinuxSupport(boolean val) {
        hasLinuxSupport = val;
    }

    public float getUpvotes1() {
        return upvotes1;
    }

    public void setUpvotes1(float val) {
        upvotes1 = val;
    }

    public float getUpvotes2() {
        return upvotes2;
    }

    public void setUpvotes2(float val) {
        upvotes2 = val;
    }

    public int getAveragePlayTime() {
        return averagePlayTime;
    }

    public void setAveragePlayTime(int val) {
        averagePlayTime = val;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String val) {
        developers = val;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> val) {
        genres = val;
    }

    Game(
            int appId,
            String name,
            LocalDate releaseDate,
            String owners,
            int age,
            float price,
            int dlcCount,
            List<String> languages,
            String website,
            boolean hasWindowsSupport,
            boolean hasMacOSSupport,
            boolean hasLinuxSupport,
            float upvotes1,
            float upvotes2,
            int averagePlayTime,
            String developers,
            List<String> genres) {
        this.appId = appId;
        this.name = name;
        this.releaseDate = releaseDate;
        this.owners = owners;
        this.age = age;
        this.price = price;
        this.dlcCount = dlcCount;
        this.languages = languages;
        setWebsite(website);
        this.hasWindowsSupport = hasWindowsSupport;
        this.hasMacOSSupport = hasMacOSSupport;
        this.hasLinuxSupport = hasLinuxSupport;
        this.upvotes1 = upvotes1;
        this.upvotes2 = upvotes2;
        this.averagePlayTime = averagePlayTime;
        this.developers = developers;
        this.genres = genres;
    }

    Game() {
        appId = 0;
        name = "";
        releaseDate = LocalDate.now();
        owners = "";
        age = 0;
        price = 0;
        dlcCount = 0;
        languages = new ArrayList<String>();
        website = "";
        hasWindowsSupport = false;
        hasMacOSSupport = false;
        hasLinuxSupport = false;
        upvotes1 = 0;
        upvotes2 = 0;
        averagePlayTime = 0;
        developers = "";
        genres = new ArrayList<String>();
    }

    public static Game fromCSVLine(String line) {
        CSV.Parser p = new CSV.Parser(line);
        List<Game.CSV.Data> d = p.parseLine(); // `d` as of `data`.

        Game g = new Game();
        g.appId = d.get(0).strAsInt();
        g.name = d.get(1).str();
        g.releaseDate = Util.parseDate(d.get(2).str());
        g.owners = d.get(3).str();
        g.age = d.get(4).strAsInt();
        g.price = d.get(5).strAsFloat();
        g.dlcCount = d.get(6).strAsInt();
        g.languages = d.get(7).vec();
        String possibleWebsite = d.get(8).str();
        g.setWebsite(possibleWebsite.isEmpty() ? "null" : possibleWebsite);
        g.hasWindowsSupport = d.get(9).str().toLowerCase().startsWith("t");
        g.hasMacOSSupport = d.get(10).str().toLowerCase().startsWith("t");
        g.hasLinuxSupport = d.get(11).str().toLowerCase().startsWith("t");
        g.upvotes1 = d.get(12).strAsFloat();
        g.upvotes2 = d.get(13).strAsFloat();
        g.averagePlayTime = d.get(14).strAsInt();
        g.developers = d.get(15).str();
        if (d.size() > 16) {
            g.genres = d.get(16).strAsVec(",");
        }

        return g;
    }

    @Override
    public Game clone() {
        return new Game(
                appId, name, releaseDate,
                owners, age, price,
                dlcCount, new ArrayList<String>(languages), website,
                hasWindowsSupport, hasMacOSSupport, hasLinuxSupport,
                upvotes1, upvotes2, averagePlayTime,
                developers, new ArrayList<String>(genres));
    }

    @Override
    public String toString() {
        String releaseDateFmt = String.format("%s/%d",
                Util.monthNumberToString(releaseDate.getMonthValue()),
                releaseDate.getYear());
        String priceFmt = Util.dotFloat(price);
        String languagesFmt = Util.formatList(languages);
        int averageUpvotes = Math.round(upvotes1 / (upvotes1 + upvotes2) * 100);
        String averageUpvotesFmt = Integer.toString(averageUpvotes) + "%";
        String averagePlayTimeFmt = averagePlayTime == 0
                ? "null"
                : Util.formatMinuteInterval(averagePlayTime);
        String genresFmt = Util.formatList(genres);

        return String.format(
                // ..... 11111111 22222222 33333333 44444444 55555555 66
                /* _ */ "%d %s %s %s %d %s %d %s %s %b %b %b %s %s %s %s",
                /* 1 */ appId, name, releaseDateFmt,
                /* 2 */ owners, age, priceFmt,
                /* 3 */ dlcCount, languagesFmt, website,
                /* 4 */ hasWindowsSupport, hasMacOSSupport, hasLinuxSupport,
                /* 5 */ averageUpvotesFmt, averagePlayTimeFmt, developers,
                /* 6 */ genresFmt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // GAME CSV PARSING INFRASTRUCTURE
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class CSV {
        private static class Parser {
            private static final char[] TOP_LEVEL_STRING_DELIMITERS = new char[] { '"' };
            private static final char SEPARATOR = ',';

            private final String source;
            private int cursor;

            // Horrific stuff, but eases the implementation.
            private HashSet<Character> terminators;

            public Parser(String source) {
                terminators = new HashSet<Character>();
                terminators.add('\0');
                terminators.add(',');

                this.source = source.trim();
                cursor = 0;
            }

            //
            // Parsing routines.
            //

            public List<Data> parseLine() {
                ArrayList<Data> line = new ArrayList<Data>();
                while (!isAtEnd()) {
                    line.add(parseCell());
                    // Comma is optional just before the end.
                    if (!isAtEnd()) {
                        consume(',');
                    }
                }
                return line;
            }

            private Data parseCell() {
                for (char c : TOP_LEVEL_STRING_DELIMITERS) {
                    if (c == peek()) {
                        return parseQuotedCell();

                    }
                }
                return parseMaybeMultiCell();
            }

            private Data parseQuotedCell() {
                return withoutTerminator(SEPARATOR, () -> {
                    char quote = consume(TOP_LEVEL_STRING_DELIMITERS);
                    Data data = withTerminator(quote, () -> {
                        return parseMaybeMultiCell();
                    });
                    consume(quote);
                    return data;
                });
            }

            private Data parseMaybeMultiCell() {
                char next = peek(1);
                // Maybe improve this in the future?
                //
                // Sadly it would be quite unwieldy since `['2.0']` should be
                // parsed as a vector and [2.0] as a string, "[2.0]".
                if (peek() == '[' && (next == '"' || next == '\'' || next == ']')) {
                    return parseMultiCell();
                }
                return parseSimpleCell();
            }

            private Data parseMultiCell() {
                Data.Vec.Builder vb = new Data.Vec.Builder();
                consume('[');
                while (peek() != ']') {
                    bumpSpaces();
                    vb.append(parseBareString());
                    bumpSpaces();
                    if (peek() != ']') {
                        consume(',');
                        bumpSpaces();
                    }
                }
                consume(']');
                return vb.toVec();
            }

            private String parseBareString() {
                return withoutTerminator(SEPARATOR, () -> {
                    char quote = consume(new char[] { '"', '\'' });
                    StringBuilder sb = new StringBuilder();
                    while (peek() != quote) {
                        checkForUnterminated("string", quote);
                        sb.append(bump());
                    }
                    consume(quote);
                    return sb.toString();
                });
            }

            private Data parseSimpleCell() {
                StringBuilder sb = new StringBuilder();
                while (!terminators.contains(peek())) {
                    // Sanity check. If this throws an exception, it would certainly be a bug.
                    checkForUnterminated("???", '?');
                    sb.append(bump());
                }
                return new Data.Str(sb.toString());
            }

            //
            // Utils.
            //

            private char peek(int offset) {
                if (isAtEnd()) {
                    return '\0';
                }
                return source.charAt(cursor + offset);
            }

            private boolean isAtEnd(int offset) {
                return cursor + offset >= source.length();
            }

            private boolean isAtEnd() {
                return isAtEnd(0);
            }

            private char peek() {
                return peek(0);
            }

            private char bump() {
                return source.charAt(cursor++);
            }

            private void bumpSpaces() {
                while (peek() == ' ') {
                    bump();
                }
            }

            private char consume(char expected) {
                if (peek() != expected) {
                    throw new RuntimeException(String.format(
                            "Expected `%s`, instead got `%s` (@ %d)", expected, peek(), cursor));
                }
                return bump();
            }

            private char consume(char[] expected) {
                for (char c : expected) {
                    if (peek() == c) {
                        return bump();
                    }
                }
                throw new RuntimeException(String.format(
                        "Unexpected character `%s` (@ %d)", peek(), cursor));
            }

            private <T> T withTerminator(char terminator, Supplier<T> fn) {
                boolean original = terminators.contains(terminator);
                if (!original) {
                    terminators.add(terminator);
                }
                T result = fn.get();
                if (!original) {
                    terminators.remove(terminator);
                }
                return result;
            }

            private <T> T withoutTerminator(char terminator, Supplier<T> fn) {
                boolean original = terminators.contains(terminator);
                if (original) {
                    terminators.remove(terminator);
                }
                T result = fn.get();
                if (original) {
                    terminators.add(terminator);
                }
                return result;
            }

            private void checkForUnterminated(String currentKindName, char closingDelimiter) {
                if (terminators.contains(peek())) {
                    throw new RuntimeException(String.format(
                            "Unterminated %s (missing closing `%c`) (@ %d)",
                            currentKindName, closingDelimiter, cursor));
                }
            }
        }

        public abstract static class Data {
            public static class Str extends Data {
                public final String data;

                public Str(String data) {
                    this.data = data.trim();
                }

                @Override
                public String toString() {
                    return "Str(" + data + ")";
                }
            }

            public static class Vec extends Data {
                public final List<String> data;

                public Vec(List<String> data) {
                    this.data = data;
                }

                @Override
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Vec(");
                    for (int i = 0; i < data.size(); i++) {
                        if (i != 0) {
                            sb.append(", ");
                        }
                        sb.append(data.get(i));
                    }
                    sb.append(")");
                    return sb.toString();
                }

                public static class Builder {
                    private ArrayList<String> data = new ArrayList<String>();

                    public Builder() {
                    }

                    public void append(String str) {
                        data.add(str);
                    }

                    public Vec toVec() {
                        return new Vec(data);
                    }
                }
            }

            public String str() {
                return ((Str) this).data;
            }

            public int strAsInt() {
                return Integer.parseInt(str());
            }

            public float strAsFloat() {
                return Float.parseFloat(str());
            }

            public List<String> strAsVec(String separatorRegex) {
                ArrayList<String> vec = new ArrayList<String>();
                for (String item : str().split(separatorRegex)) {
                    vec.add(item.trim());
                }
                return vec;
            }

            public List<String> vec() {
                return ((Vec) this).data;
            }
        }
    }

    private static class Util {
        public static <T extends Object> String formatList(List<T> list) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(list.get(i).toString());
            }
            sb.append("]");
            return sb.toString();
        }

        public static String formatMinuteInterval(int interval) {
            // `interval` is in minutes.
            int hours = interval / 60;
            int minutes = interval % 60;
            StringBuilder sb = new StringBuilder();
            if (hours != 0) {
                sb.append(hours + "h");
            }
            if (minutes != 0) {
                if (sb.length() != 0) {
                    sb.append(" ");
                }
                sb.append(minutes + "m");
            }
            return sb.toString();
        }

        public static LocalDate parseDate(String raw) {
            String[] parts = raw.split(",? ");
            int day = 1;
            int month = 1;
            int year = 2000;
            if (parts.length == 3) {
                month = monthStringToNumber(parts[0]);
                day = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);
            } else if (parts.length == 2) {
                month = monthStringToNumber(parts[0]);
                year = Integer.parseInt(parts[1]);
            } else {
                throw new RuntimeException(String.format(
                        "Invalid date format `%s`", raw));
            }
            return LocalDate.of(year, month, day);
        }

        public static int monthStringToNumber(String month) {
            switch (month.toLowerCase()) {
                case "jan":
                    return 1;
                case "feb":
                    return 2;
                case "mar":
                    return 3;
                case "apr":
                    return 4;
                case "may":
                    return 5;
                case "jun":
                    return 6;
                case "jul":
                    return 7;
                case "aug":
                    return 8;
                case "sep":
                    return 9;
                case "oct":
                    return 10;
                case "nov":
                    return 11;
                case "dec":
                    return 12;
                default:
                    throw new RuntimeException(String.format(
                            "Invalid month string `%s`", month));
            }
        }

        public static String monthNumberToString(int month) {
            switch (month) {
                case 1:
                    return "Jan";
                case 2:
                    return "Feb";
                case 3:
                    return "Mar";
                case 4:
                    return "Apr";
                case 5:
                    return "May";
                case 6:
                    return "Jun";
                case 7:
                    return "Jul";
                case 8:
                    return "Aug";
                case 9:
                    return "Sep";
                case 10:
                    return "Oct";
                case 11:
                    return "Nov";
                case 12:
                    return "Dec";
                default:
                    throw new RuntimeException(String.format(
                            "Invalid month `%s`", month));
            }
        }

        public static String dotFloat(float raw) {
            return String.format("%.2f", raw).replace(",", ".");
        }
    }
}
