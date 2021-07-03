package org.zhiqiang.lu.easycode.core.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class HTML {


    // 返回html代码
    private static String getHtmlCode(String httpUrl, String code) throws Exception {
        long star, end, time;
        Date before = new Date();
        star = before.getTime();
        StringBuilder htmlCode = new StringBuilder();
        try {
            InputStream in;
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.connect();
            in = connection.getInputStream();
            BufferedReader breader = new BufferedReader(new InputStreamReader(in, code));
            String currentLine;
            while ((currentLine = breader.readLine()) != null) {
                htmlCode.append(currentLine);
            }
        } finally {
            Date after = new Date();
            end = after.getTime();
            time = end - star;
            System.out.println("执行时间:" + time + "毫秒");
        }
        return htmlCode.toString();
    }

    // 存储文件
    private static synchronized void writeHtml(String filePath, String info) throws Exception {
        System.out.println(info);
        PrintWriter pw = null;
        try {
            File writeFile = new File(filePath);
            boolean isExit = writeFile.exists();
            if (!isExit) {
                writeFile.createNewFile();
            } else {
                writeFile.delete();
                writeFile.createNewFile();
            }
            pw = new PrintWriter(new FileOutputStream(filePath, true));
            pw.println(info);
            pw.close();
        } finally {
            assert pw != null;
            pw.close();
        }
    }

    private static void transferFile(String filePath, String code) throws IOException {
        String line_separator = System.getProperty("line.separator");
        FileInputStream fis = new FileInputStream(filePath);
        StringBuilder content = new StringBuilder();
        DataInputStream in = new DataInputStream(fis);
        BufferedReader d = new BufferedReader(new InputStreamReader(in, "GBK"));// ,
        String line;
        while ((line = d.readLine()) != null) {
            content.append(line).append(line_separator);
        }
        d.close();
        in.close();
        fis.close();

        Writer ow = new OutputStreamWriter(new FileOutputStream(filePath), code);
        ow.write(content.toString());
        ow.close();
    }

    /**
     * 方法用于使用页面链接生产html页面文件
     *
     * @param url       访问的地址
     * @param path      生产html文件位置
     * @param inputCode 访问地址页面编码
     * @param outCode   生产html页面编码
     */
    public static void FILE(String url, String path, String inputCode, String outCode) throws Exception {
        writeHtml(path, getHtmlCode(url, inputCode));
        transferFile(path, outCode);
    }

    /**
     * 方法用于使用页面链接生产html页面文件
     *
     * @param url  访问的地址
     * @param path 生产html文件位置
     */
    public static void FILE(String url, String path) throws Exception {
        writeHtml(path, getHtmlCode(url, "UTF-8"));
        transferFile(path, "UTF-8");
    }
}
