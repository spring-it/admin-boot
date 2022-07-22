package cn.mesmile.admin.common.filter.xss;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zb
 * @Description
 */
public final class XssHtmlFilter {

    private static final int REGEX_FLAGS_SI = 34;
    private static final Pattern P_COMMENTS = Pattern.compile("<!--(.*?)-->", 32);
    private static final Pattern P_COMMENT = Pattern.compile("^!--(.*)--$", 34);
    private static final Pattern P_TAGS = Pattern.compile("<(.*?)>", 32);
    private static final Pattern P_END_TAG = Pattern.compile("^/([a-z0-9]+)", 34);
    private static final Pattern P_START_TAG = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", 34);
    private static final Pattern P_QUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2", 34);
    private static final Pattern P_UNQUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)", 34);
    private static final Pattern P_PROTOCOL = Pattern.compile("^([^:]+):", 34);
    private static final Pattern P_ENTITY = Pattern.compile("&#(\\d+);?");
    private static final Pattern P_ENTITY_UNICODE = Pattern.compile("&#x([0-9a-f]+);?");
    private static final Pattern P_ENCODE = Pattern.compile("%([0-9a-f]{2});?");
    private static final Pattern P_VALID_ENTITIES = Pattern.compile("&([^&;]*)(?=(;|&|$))");
    private static final Pattern P_VALID_QUOTES = Pattern.compile("(>|^)([^<]+?)(<|$)", 32);
    private static final Pattern P_END_ARROW = Pattern.compile("^>");
    private static final Pattern P_BODY_TO_END = Pattern.compile("<([^>]*?)(?=<|$)");
    private static final Pattern P_XML_CONTENT = Pattern.compile("(^|>)([^<]*?)(?=>)");
    private static final Pattern P_STRAY_LEFT_ARROW = Pattern.compile("<([^>]*?)(?=<|$)");
    private static final Pattern P_STRAY_RIGHT_ARROW = Pattern.compile("(^|>)([^<]*?)(?=>)");
    private static final Pattern P_AMP = Pattern.compile("&");
    private static final Pattern P_QUOTE = Pattern.compile("<");
    private static final Pattern P_LEFT_ARROW = Pattern.compile("<");
    private static final Pattern P_RIGHT_ARROW = Pattern.compile(">");
    private static final Pattern P_BOTH_ARROWS = Pattern.compile("<>");
    private static final ConcurrentMap<String, Pattern> P_REMOVE_PAIR_BLANKS = new ConcurrentHashMap();
    private static final ConcurrentMap<String, Pattern> P_REMOVE_SELF_BLANKS = new ConcurrentHashMap();
    private final Map<String, List<String>> vAllowed;
    private final Map<String, Integer> vTagCounts;
    private final String[] vSelfClosingTags;
    private final String[] vNeedClosingTags;
    private final String[] vDisallowed;
    private final String[] vProtocolAtts;
    private final String[] vAllowedProtocols;
    private final String[] vRemoveBlanks;
    private final String[] vAllowedEntities;
    private final boolean stripComment;
    private final boolean encodeQuotes;
    private boolean vDebug;
    private final boolean alwaysMakeTags;

    public XssHtmlFilter() {
        this.vTagCounts = new HashMap();
        this.vDebug = false;
        this.vAllowed = new HashMap();
        ArrayList<String> aAtts = new ArrayList();
        aAtts.add("href");
        aAtts.add("target");
        this.vAllowed.put("a", aAtts);
        ArrayList<String> imgAtts = new ArrayList();
        imgAtts.add("src");
        imgAtts.add("width");
        imgAtts.add("height");
        imgAtts.add("alt");
        this.vAllowed.put("img", imgAtts);
        ArrayList<String> noAtts = new ArrayList();
        this.vAllowed.put("b", noAtts);
        this.vAllowed.put("strong", noAtts);
        this.vAllowed.put("i", noAtts);
        this.vAllowed.put("em", noAtts);
        this.vSelfClosingTags = new String[]{"img"};
        this.vNeedClosingTags = new String[]{"a", "b", "strong", "i", "em"};
        this.vDisallowed = new String[0];
        this.vAllowedProtocols = new String[]{"http", "mailto", "https"};
        this.vProtocolAtts = new String[]{"src", "href"};
        this.vRemoveBlanks = new String[]{"a", "b", "strong", "i", "em"};
        this.vAllowedEntities = new String[]{"amp", "gt", "lt", "quot"};
        this.stripComment = true;
        this.encodeQuotes = true;
        this.alwaysMakeTags = false;
    }

    public XssHtmlFilter(final boolean debug) {
        this();
        this.vDebug = debug;
    }

    public XssHtmlFilter(final Map<String, Object> conf) {
        this.vTagCounts = new HashMap();
        this.vDebug = false;

        assert conf.containsKey("vAllowed") : "configuration requires vAllowed";

        assert conf.containsKey("vSelfClosingTags") : "configuration requires vSelfClosingTags";

        assert conf.containsKey("vNeedClosingTags") : "configuration requires vNeedClosingTags";

        assert conf.containsKey("vDisallowed") : "configuration requires vDisallowed";

        assert conf.containsKey("vAllowedProtocols") : "configuration requires vAllowedProtocols";

        assert conf.containsKey("vProtocolAtts") : "configuration requires vProtocolAtts";

        assert conf.containsKey("vRemoveBlanks") : "configuration requires vRemoveBlanks";

        assert conf.containsKey("vAllowedEntities") : "configuration requires vAllowedEntities";

        this.vAllowed = Collections.unmodifiableMap((HashMap)conf.get("vAllowed"));
        this.vSelfClosingTags = (String[])((String[])conf.get("vSelfClosingTags"));
        this.vNeedClosingTags = (String[])((String[])conf.get("vNeedClosingTags"));
        this.vDisallowed = (String[])((String[])conf.get("vDisallowed"));
        this.vAllowedProtocols = (String[])((String[])conf.get("vAllowedProtocols"));
        this.vProtocolAtts = (String[])((String[])conf.get("vProtocolAtts"));
        this.vRemoveBlanks = (String[])((String[])conf.get("vRemoveBlanks"));
        this.vAllowedEntities = (String[])((String[])conf.get("vAllowedEntities"));
        this.stripComment = conf.containsKey("stripComment") ? (Boolean)conf.get("stripComment") : true;
        this.encodeQuotes = conf.containsKey("encodeQuotes") ? (Boolean)conf.get("encodeQuotes") : true;
        this.alwaysMakeTags = conf.containsKey("alwaysMakeTags") ? (Boolean)conf.get("alwaysMakeTags") : true;
    }

    private void reset() {
        this.vTagCounts.clear();
    }

    private void debug(final String msg) {
        if (this.vDebug) {
            Logger.getAnonymousLogger().info(msg);
        }

    }

    public static String chr(final int decimal) {
        return String.valueOf((char)decimal);
    }

    public static String htmlSpecialChars(final String s) {
        String result = regexReplace(P_AMP, "&amp;", s);
        result = regexReplace(P_QUOTE, "&quot;", result);
        result = regexReplace(P_LEFT_ARROW, "&lt;", result);
        result = regexReplace(P_RIGHT_ARROW, "&gt;", result);
        return result;
    }

    public String filter(final String input) {
        this.reset();
        this.debug("************************************************");
        this.debug("              INPUT: " + input);
        String s = this.escapeComments(input);
        this.debug("     escapeComments: " + s);
        s = this.balanceHtml(s);
        this.debug("        balanceHtml: " + s);
        s = this.checkTags(s);
        this.debug("          checkTags: " + s);
        s = this.processRemoveBlanks(s);
        this.debug("processRemoveBlanks: " + s);
        s = this.validateEntities(s);
        this.debug("    validateEntites: " + s);
        this.debug("************************************************\n\n");
        // 去除两边空格
//        s = StringUtils.trimWhitespace(s);
//        s = StrUtil.trim(s);
        return s;
    }

    public boolean isAlwaysMakeTags() {
        return this.alwaysMakeTags;
    }

    public boolean isStripComments() {
        return this.stripComment;
    }

    private String escapeComments(final String s) {
        Matcher m = P_COMMENTS.matcher(s);
        StringBuffer buf = new StringBuffer();
        if (m.find()) {
            String match = m.group(1);
            m.appendReplacement(buf, Matcher.quoteReplacement("<!--" + htmlSpecialChars(match) + "-->"));
        }

        m.appendTail(buf);
        return buf.toString();
    }

    private String balanceHtml(String s) {
        if (this.alwaysMakeTags) {
            s = regexReplace(P_END_ARROW, "", s);
            s = regexReplace(P_BODY_TO_END, "<$1>", s);
            s = regexReplace(P_XML_CONTENT, "$1<$2", s);
        } else {
            s = regexReplace(P_STRAY_LEFT_ARROW, "&lt;$1", s);
            s = regexReplace(P_STRAY_RIGHT_ARROW, "$1$2&gt;<", s);
            s = regexReplace(P_BOTH_ARROWS, "", s);
        }

        return s;
    }

    private String checkTags(String s) {
        Matcher m = P_TAGS.matcher(s);
        StringBuffer buf = new StringBuffer();

        while(m.find()) {
            String replaceStr = m.group(1);
            replaceStr = this.processTag(replaceStr);
            m.appendReplacement(buf, Matcher.quoteReplacement(replaceStr));
        }

        m.appendTail(buf);
        s = buf.toString();
        Iterator iterator = this.vTagCounts.keySet().iterator();

        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            for(int ii = 0; ii < (Integer)this.vTagCounts.get(key); ++ii) {
                s = s + "</" + key + ">";
            }
        }
        return s;
    }

    private String processRemoveBlanks(final String s) {
        String result = s;
        String[] blanks = this.vRemoveBlanks;
        int length = blanks.length;
        for(int i = 0; i < length; ++i) {
            String tag = blanks[i];
            if (!P_REMOVE_PAIR_BLANKS.containsKey(tag)) {
                P_REMOVE_PAIR_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?></" + tag + ">"));
            }

            result = regexReplace((Pattern)P_REMOVE_PAIR_BLANKS.get(tag), "", result);
            if (!P_REMOVE_SELF_BLANKS.containsKey(tag)) {
                P_REMOVE_SELF_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?/>"));
            }

            result = regexReplace((Pattern)P_REMOVE_SELF_BLANKS.get(tag), "", result);
        }

        return result;
    }

    private static String regexReplace(final Pattern regexPattern, final String replacement, final String s) {
        Matcher m = regexPattern.matcher(s);
        return m.replaceAll(replacement);
    }

    private String processTag(final String s) {
        Matcher m = P_END_TAG.matcher(s);
        String name;
        if (m.find()) {
            name = m.group(1).toLowerCase();
            if (this.allowed(name) && !inArray(name, this.vSelfClosingTags) && this.vTagCounts.containsKey(name)) {
                this.vTagCounts.put(name, (Integer)this.vTagCounts.get(name) - 1);
                return "</" + name + ">";
            }
        }

        m = P_START_TAG.matcher(s);
        if (!m.find()) {
            m = P_COMMENT.matcher(s);
            return !this.stripComment && m.find() ? "<" + m.group() + ">" : "";
        } else {
            name = m.group(1).toLowerCase();
            String body = m.group(2);
            String ending = m.group(3);
            if (!this.allowed(name)) {
                return "";
            } else {
                String params = "";
                Matcher m2 = P_QUOTED_ATTRIBUTES.matcher(body);
                Matcher m3 = P_UNQUOTED_ATTRIBUTES.matcher(body);
                List<String> paramNames = new ArrayList();
                ArrayList paramValues = new ArrayList();

                while(m2.find()) {
                    paramNames.add(m2.group(1));
                    paramValues.add(m2.group(3));
                }

                while(m3.find()) {
                    paramNames.add(m3.group(1));
                    paramValues.add(m3.group(3));
                }

                for(int ii = 0; ii < paramNames.size(); ++ii) {
                    String paramName = ((String)paramNames.get(ii)).toLowerCase();
                    String paramValue = (String)paramValues.get(ii);
                    if (this.allowedAttribute(name, paramName)) {
                        if (inArray(paramName, this.vProtocolAtts)) {
                            paramValue = this.processParamProtocol(paramValue);
                        }

                        params = params + " " + paramName + "=\"" + paramValue + "\"";
                    }
                }

                if (inArray(name, this.vSelfClosingTags)) {
                    ending = " /";
                }

                if (inArray(name, this.vNeedClosingTags)) {
                    ending = "";
                }

                if (ending != null && ending.length() >= 1) {
                    ending = " /";
                } else if (this.vTagCounts.containsKey(name)) {
                    this.vTagCounts.put(name, (Integer)this.vTagCounts.get(name) + 1);
                } else {
                    this.vTagCounts.put(name, 1);
                }

                return "<" + name + params + ending + ">";
            }
        }
    }

    private String processParamProtocol(String s) {
        s = this.decodeEntities(s);
        Matcher m = P_PROTOCOL.matcher(s);
        if (m.find()) {
            String protocol = m.group(1);
            if (!inArray(protocol, this.vAllowedProtocols)) {
                s = "#" + s.substring(protocol.length() + 1);
                if (s.startsWith("#//")) {
                    s = "#" + s.substring(3);
                }
            }
        }

        return s;
    }

    private String decodeEntities(String s) {
        StringBuffer buf = new StringBuffer();
        Matcher m = P_ENTITY.matcher(s);

        String match;
        int decimal;
        while(m.find()) {
            match = m.group(1);
            decimal = Integer.decode(match);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }

        m.appendTail(buf);
        s = buf.toString();
        buf = new StringBuffer();
        m = P_ENTITY_UNICODE.matcher(s);

        while(m.find()) {
            match = m.group(1);
            decimal = Integer.valueOf(match, 16);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }

        m.appendTail(buf);
        s = buf.toString();
        buf = new StringBuffer();
        m = P_ENCODE.matcher(s);

        while(m.find()) {
            match = m.group(1);
            decimal = Integer.valueOf(match, 16);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }

        m.appendTail(buf);
        s = buf.toString();
        s = this.validateEntities(s);
        return s;
    }

    private String validateEntities(final String s) {
        StringBuffer buf = new StringBuffer();
        Matcher m = P_VALID_ENTITIES.matcher(s);

        while(m.find()) {
            String one = m.group(1);
            String two = m.group(2);
            m.appendReplacement(buf, Matcher.quoteReplacement(this.checkEntity(one, two)));
        }

        m.appendTail(buf);
        return this.encodeQuotes(buf.toString());
    }

    private String encodeQuotes(final String s) {
        if (!this.encodeQuotes) {
            return s;
        } else {
            StringBuffer buf = new StringBuffer();
            Matcher m = P_VALID_QUOTES.matcher(s);

            while(m.find()) {
                String one = m.group(1);
                String two = m.group(2);
                String three = m.group(3);
                m.appendReplacement(buf, Matcher.quoteReplacement(one + regexReplace(P_QUOTE, "&quot;", two) + three));
            }

            m.appendTail(buf);
            return buf.toString();
        }
    }

    private String checkEntity(final String preamble, final String term) {
        return ";".equals(term) && this.isValidEntity(preamble) ? '&' + preamble : "&amp;" + preamble;
    }

    private boolean isValidEntity(final String entity) {
        return inArray(entity, this.vAllowedEntities);
    }

    private static boolean inArray(final String s, final String[] array) {
        String[] newArray = array;
        int length = array.length;
        for(int i = 0; i < length; ++i) {
            String item = newArray[i];
            if (item != null && item.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean allowed(final String name) {
        return (this.vAllowed.isEmpty() || this.vAllowed.containsKey(name)) && !inArray(name, this.vDisallowed);
    }

    private boolean allowedAttribute(final String name, final String paramName) {
        return this.allowed(name) && (this.vAllowed.isEmpty() || ((List)this.vAllowed.get(name)).contains(paramName));
    }
}

