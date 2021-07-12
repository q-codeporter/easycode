package org.zhiqiang.lu.easycode.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class data {

  public static String inputstream_string(InputStream is) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int i = -1;
    while ((i = is.read()) != -1) {
      baos.write(i);
    }
    return baos.toString();
  }

  /**
   * 方法用于字符串补位，前补位。
   *
   * @param bwStr 要判斷的补位字符串
   * @param ws    补位位数
   * @return 返回String数值类型，部位后的字符串
   */
  public static String zero_befor(String bwStr, int ws) {
    int max = ws - bwStr.length();
    if (bwStr.length() < ws) {
      StringBuilder bwStrBuilder = new StringBuilder(bwStr);
      for (int i = 0; i < max; i++) {
        bwStrBuilder.insert(0, "0");
      }
      bwStr = bwStrBuilder.toString();
    }
    return bwStr;
  }

  /**
   * 方法用于字符串补位，后补位。
   *
   * @param bwStr 要判斷的补位字符串
   * @param ws    补位位数
   * @return 返回String数值类型，部位后的字符串
   */
  public static String zero_after(String bwStr, int ws) {
    int max = ws - bwStr.length();
    if (bwStr.length() < ws) {
      StringBuilder bwStrBuilder = new StringBuilder(bwStr);
      for (int i = 0; i < max; i++) {
        bwStrBuilder.append("0");
      }
      bwStr = bwStrBuilder.toString();
    }
    return bwStr;
  }

  /**
   * 方法用于判断str1中包含str2的个数
   *
   * @param str1 要判斷的字符串
   * @param str2 包含字符串
   * @return 返回int数值类型，包含str2的个数
   */
  public static int counts(String str1, String str2) {
    if (!str1.contains(str2)) {
      return 0;
    } else if (str1.contains(str2)) {
      return counts(str1.substring(str1.indexOf(str2) + str2.length()), str2) + 1;
    }
    return 0;
  }

  /**
   * 该方法用于判断object是否为null或者对象String为“”或者为“null”，true为空或者“”或者“null” 反之false。
   *
   * @param arg 要判断的object类型的参数。
   * @return 返回boolean类型，false为null或者“”或者“null” 反之true。
   */
  public static boolean is_null(Object arg) {
    return !no_null(arg);
  }

  /**
   * 该方法用于判断object是否为null或者对象String为“”或者为“null”，false为空或者“”或者“null” 反之true。
   *
   * @param arg 要判断的object类型的参数。
   * @return 返回boolean类型，false为null或者“”或者“null” 反之true。
   */
  public static boolean no_null(Object arg) {
    if (null == arg) {
      return false;
    }
    if (arg instanceof String && (StringUtils.isBlank(arg.toString()) || arg.toString().contains("null"))) {
      return false;
    }
    if (arg instanceof List && ((List<?>) arg).isEmpty()) {
      return false;
    }
    return true;
  }

  /**
   * 该方法用于将Object的数据转换为String数据类型 并使用了trim()方法简单去空格。 自动换换null或者“空格”的
   * 为“”，转换出现异常也为“”
   *
   * @param arg 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @return 返回String类型，转化失败不会抛出异常而转换为“”
   */
  public static String to_string(Object arg) {
    try {
      if (no_null(arg)) {
        return arg.toString().trim();
      }
      return "";
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * 该方法用于将Object的数据转换为int数据null或者“”的话默认为0，转换失败也为0
   *
   * @param arg 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @return 返回int类型，转化失败不会抛出异常而转换为0
   */
  public static int to_int(Object arg) {
    try {
      return Integer.parseInt(to_string(arg));
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * 该方法用于将Object的数据转换为Short数据null或者“”的话默认为0，转换失败也为0
   *
   * @param arg 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @return 返回Short类型，转化失败不会抛出异常而转换为0
   */
  public static long to_long(Object arg) {
    try {
      return Long.parseLong(to_string(arg));
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * 该方法用于将Object的数据转换为double类型的数据null或者“”的话默认为0，转换失败也为0
   *
   * @param arg1 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @return 返回 double 类型，转化失败不会抛出异常而转换为0
   */
  public static double to_double(Object arg1) {
    try {
      BigDecimal bd = new BigDecimal(to_string(arg1));
      return bd.doubleValue();
    } catch (Exception e) {
      return 0.00;
    }
  }

  /**
   * 该方法用于将Object的数据转换为double类型的数据null或者“”的话默认为0，转换失败也为0
   *
   * @param num 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @param ws  int类型 要转换的参数保留几位有效数字
   * @param lb  int类型 要保留有效数字的类别，默认int值3个参数，0四舍五入，1向下取值，2向上取值
   * @return 返回 double 类型，转化失败不会抛出异常而转换为0
   */
  public static double to_double(Object num, int ws, int lb) {
    try {
      BigDecimal bd = new BigDecimal(to_string(num));
      BigDecimal bd1;
      if (lb == 0) {
        bd1 = bd.setScale(ws, BigDecimal.ROUND_HALF_UP);
      } else if (lb == 1) {
        bd1 = bd.setScale(ws, BigDecimal.ROUND_DOWN);
      } else {
        bd1 = bd.setScale(ws, BigDecimal.ROUND_UP);
      }
      double p = bd1.doubleValue();
      return p;
    } catch (Exception e) {
      return 0.00;
    }
  }

  /**
   * 该方法用于将Object的数据转换为double类型的String字符串，数据null或者“”的话默认为0，转换失败也为0
   *
   * @param num 要转换的object类型的参数，建议使用常用类型，如String，等。
   * @param ws  int类型 要转换的参数保留几位有效数字
   * @param lb  int类型 要保留有效数字的类别，默认int值3个参数，0四舍五入，1向下取值，2向上取值
   * @return 返回 String 类型，转化失败不会抛出异常而转换为0
   */
  public static String double_string(Object num, int ws, int lb) {
    try {
      double p = to_double(num, ws, lb);
      String xsStr = to_string(p + "");
      String[] xssz = xsStr.split("[.]");
      String sxdq = xssz[0];
      StringBuilder sxdh = new StringBuilder(xssz[1]);
      if (ws == 0) {
        return sxdq;
      }
      for (int i = 0; i < ws - xssz[1].length(); i++) {
        sxdh.append("0");
      }
      return sxdq + "." + sxdh;
    } catch (Exception e) {
      StringBuilder doubleStr = new StringBuilder("0.");
      for (int i = 0; i < ws; i++) {
        doubleStr.append("0");
      }
      return doubleStr.toString();
    }
  }

  public static String double_string(double num, int ws, int lb) {
    return double_string(String.valueOf(num), ws, lb);
  }

  /**
   * 该方法用于将字符串的数据转换为Date类型
   *
   * @param time       字符串类型 要转换的参数
   * @param timeFormat 要转换的时间格式 yyyy-MM-dd HH:mm:ss
   * @return 返回Date类型，转化失败不会抛出异常而转换为当前的时间
   */
  public static Date to_date(String time, String timeFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat.trim());
    Date date;
    try {
      date = sdf.parse(time.trim());
    } catch (Exception e) {
      System.out.println("String转化Date异常，请查看String参数是否匹配时间的格式" + e);
      date = new Date();
    }
    return date;

  }

  /**
   * 该方法用于将Date类型的数据转换为字符串
   *
   * @param time       Date类型 要转换的参数
   * @param timeFormat 要转换的时间格式 yyyy.MM.dd HH:mm:ss
   * @return 返回String类型，转化失败不会抛出异常而转换为当前的时间的字符串
   */
  public static String date_string(Date time, String timeFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat.trim());
    String dateStr;
    try {
      dateStr = sdf.format(time);
    } catch (Exception e) {
      System.out.println("Date转化String异常，请查看String参数是否匹配时间的格式" + e);
      Date d = new Date();
      dateStr = sdf.format(d);
    }
    return dateStr;

  }

  /**
   * 该方法用于将字符串的数据转换为Date类型再将Date类型的数据转换为字符串
   *
   * @param time        Date类型 要转换的参数
   * @param timeFormatQ 要转换的时间格式 yyyy.MM.dd HH:mm:ss
   * @param timeFormatH 转换后的时间格式 yyyy.MM.dd HH:mm:ss
   * @return 返回String类型，转化失败不会抛出异常而转换为当前的时间的字符串
   */
  public static String date_string(String time, String timeFormatQ, String timeFormatH) {
    return date_string(to_date(time, timeFormatQ), timeFormatH);
  }

  /**
   * 该方法用于获取字符串[]中的内容
   *
   * @param msg 字符串参数
   * @return List<String>
   */
  public static List<String> BRACKETS_MIDDLE(String msg) {
    List<String> l = new ArrayList<>();
    String regex = "\\[(.*?)]";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(msg);
    while (matcher.find()) {
      l.add(matcher.group(1));
    }
    return l;
  }
}
