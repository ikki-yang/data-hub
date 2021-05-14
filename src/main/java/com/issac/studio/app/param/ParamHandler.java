package com.issac.studio.app.param;

import com.issac.studio.app.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamHandler {
    private final static Logger log = LoggerFactory.getLogger(ParamHandler.class);

    public static String handleSQL(String source, String paramDt) throws Exception {
        log.info("开始SQL替换参数！source={}", source);

        String replaced = source;
        replaced = dtCommon(replaced, paramDt, true);
        replaced = lastDayOfLastMonth(replaced, paramDt, true);
        replaced = lastMonth(replaced, paramDt, true);

        log.info("成功替换SQL参数！replaced={}", replaced);
        return replaced;
    }

    public static String handlePath(String source, String paramDt) throws Exception {
        log.info("开始SQL替换参数！source={}", source);

        String replaced = source;
        replaced = dtCommon(replaced, paramDt, false);
        replaced = lastDayOfLastMonth(replaced, paramDt, false);
        replaced = lastMonth(replaced, paramDt, false);

        log.info("成功替换SQL参数！replaced={}", replaced);
        return replaced;
    }

    private static String lastDayOfLastMonth(String source, String todayStr, boolean hasRel) throws Exception {
        if (StringUtils.isBlank(todayStr)) {
            todayStr = DateUtil.today("yyyyMMdd");
            log.info("程序传入的日期参数为空，自动使用当日={}日期参数", todayStr);
        }
        String replaced = source;
        String regex = "\\#\\{lastMonth[,]?[yYmMdD-]*\\}";
        ArrayList<String> matched = matchedList(source, regex);

        for(String item : matched) {
            String[] splited = item.split(",");
            if(splited.length == 1){
                // 表示默认格式(yyyyMMdd), 例：#{lastMonth}
                String dateStr = DateUtil.lastMonth(todayStr, "yyyyMMdd", "yyyyMMdd");
                if(hasRel){
                    replaced = replaced.replaceAll("\\#\\{lastDayOfLastMonth\\}", "'" + dateStr + "'");
                }else {
                    replaced = replaced.replaceAll("\\#\\{lastDayOfLastMonth\\}",  dateStr);
                }
            }
            if(splited.length == 2){
                // 表示指定格式, 例：#{lastMonth,yyyy-MM-dd}
                String format = splited[1];
                String dateStr = DateUtil.lastMonth(todayStr, "yyyyMMdd", format);
                if(hasRel){
                    replaced = replaced.replaceAll("\\#\\{lastDayOfLastMonth" + format + "\\}", "'" + dateStr + "'");
                }else {
                    replaced = replaced.replaceAll("\\#\\{lastDayOfLastMonth" + format + "\\}",  dateStr);
                }
            }
        }

        return replaced;
    }

    private static String lastMonth(String source, String todayStr, boolean hasRel) throws Exception {
        if (StringUtils.isBlank(todayStr)) {
            todayStr = DateUtil.today("yyyyMMdd");
            log.info("程序传入的日期参数为空，自动使用当日={}日期参数", todayStr);
        }
        String replaced = source;
        String regex = "\\#\\{lastMonth[,]?[yYmMdD-]*\\}";
        ArrayList<String> matched = matchedList(source, regex);

        for(String item : matched) {
            String[] splited = item.split(",");
            if(splited.length == 1){
                // 表示默认格式(yyyyMM), 例：#{lastMonth}
                String dateStr = DateUtil.lastMonth(todayStr, "yyyyMMdd", "yyyyMM");
                if(hasRel){
                    replaced = replaced.replaceAll("\\#\\{lastMonth\\}", "'" + dateStr + "'");
                }else {
                    replaced = replaced.replaceAll("\\#\\{lastMonth\\}",  dateStr);
                }
            }
            if(splited.length == 2){
                // 表示指定格式, 例：#{lastMonth,yyyy-MM}
                String format = splited[1];
                String dateStr = DateUtil.lastMonth(todayStr, "yyyyMMdd", format);
                if(hasRel){
                    replaced = replaced.replaceAll("\\#\\{lastMonth" + format + "\\}", "'" + dateStr + "'");
                }else {
                    replaced = replaced.replaceAll("\\#\\{lastMonth" + format + "\\}",  dateStr);
                }
            }
        }

        return replaced;
    }

    private static String dtCommon(String source, String todayStr, boolean hasRel) throws Exception {
        if (StringUtils.isBlank(todayStr)) {
            todayStr = DateUtil.today("yyyyMMdd");
            log.info("程序传入的日期参数为空，自动使用当日={}日期参数", todayStr);
        }
        String replaced = source;
        String regex = "\\#\\{dt[-+]?(\\d*)[,]?[yYmMdD-]*\\}";
        ArrayList<String> matched = matchedList(source, regex);

        for(String item : matched){
            String[] splited = item.split(",");
            if (splited.length == 1){
                // 表示默认格式(yyyyMMdd), 例：#{dt+2}
                String dtStr = splited[0];
                if("dt".equals(dtStr)){
                    // 表示无加减日期，例：#{dt}
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{dt\\}", "'" + todayStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{dt\\}",  todayStr);
                    }
                }
                if (dtStr.split("-").length == 2){
                    // 表示减法
                    String num = dtStr.split("-")[1];
                    String dateStr = DateUtil.dateAdd(todayStr, "yyyyMMdd", -Integer.valueOf(num), "yyyyMMdd");
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{" + dtStr + "\\}", "'" + dateStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{" + dtStr + "\\}",  dateStr);
                    }
                }
                if (dtStr.split("\\+").length == 2){
                    // 表示加法
                    String num = dtStr.split("\\+")[1];
                    String dateStr = DateUtil.dateAdd(todayStr, "yyyyMMdd", Integer.valueOf(num), "yyyyMMdd");
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{dt\\+" + num + "\\}", "'" + dateStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{dt\\+" + num + "\\}",  dateStr);
                    }
                }
            }
            if(splited.length == 2){
                // 表示指定格式，例：#{dt+2, yyyy-MM-dd}
                String dtStr = splited[0];
                String format = splited[1];
                if("dt".equals(dtStr)){
                    // 表示无加减日期，例：#{dt}
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{dt" + format + "\\}", "'" + todayStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{dt" + format + "\\}",  todayStr);
                    }
                }
                if (dtStr.split("-").length == 2){
                    // 表示减法
                    String num = dtStr.split("-")[1];
                    String dateStr = DateUtil.dateAdd(todayStr, "yyyyMMdd", -Integer.valueOf(num), format);
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{" + dtStr + "," + format + "\\}", "'" + dateStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{" + dtStr + "," + format + "\\}",  dateStr);
                    }
                }
                if (dtStr.split("\\+").length == 2){
                    // 表示加法
                    String num = dtStr.split("\\+")[1];
                    String dateStr = DateUtil.dateAdd(todayStr, "yyyyMMdd", Integer.valueOf(num), format);
                    if(hasRel){
                        replaced = replaced.replaceAll("\\#\\{dt\\+" + num + "," + format + "\\}", "'" + dateStr + "'");
                    }else {
                        replaced = replaced.replaceAll("\\#\\{dt\\+" + num + "," + format + "\\}",  dateStr);
                    }
                }
            }
        }

        return replaced;
    }

    private static ArrayList<String> matchedList(String source, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        ArrayList<String> matched = new ArrayList<>();
        while (matcher.find()){
            String group = matcher.group();
            matched.add(group.substring(2, group.length() - 1));
        }
        return matched;
    }

    public static void main(String[] args) throws Exception {
        String handled = ParamHandler.handleSQL("select * from xxx where dt = #{dt} and dy = #{dt+10}  and dd = #{dt-12} and rr = #{dt+100}", "");
        System.out.println(handled);
    }
}
