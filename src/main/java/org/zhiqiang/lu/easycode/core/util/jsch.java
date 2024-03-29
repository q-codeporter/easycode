package org.zhiqiang.lu.easycode.core.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by riot94 on 1/6/2017.
 */

public class jsch {

  private Session session = null;
  private Channel channel = null;

  public static void main(String[] args) {
    try {
      System.out.println(jsch.exec_table("121.36.61.86", 22, "root", "hitsoft123@", "kubectl get pv --all-namespaces"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 构造器
   *
   * @param ip       ip地址
   * @param port     ssh 端口
   * @param username 远程访问用户名
   * @param password 远程访问密码
   * @param type     shell or sftp
   */
  public jsch(String ip, int port, String username, String password, String type) throws Exception {
    InetAddress.getByName(ip).isReachable(500);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    JSch jsch = new JSch();
    session = jsch.getSession(username, ip, port);
    session.setPassword(password);
    session.setConfig(config);
    // session.setTimeout(10000);
    session.connect();
    channel = session.openChannel(type);
    channel.connect();
  }

  /**
   * exec执行多条命令，多线程返回table json，要求命令结果为表格
   *
   * @param ip       ip地址
   * @param port     ssh 端口
   * @param username 远程访问用户名
   * @param password 远程访问密码
   * @param commands 多条命令
   */
  public static Map<String, List<Map<String, String>>> exec_table(String ip, int port, String username, String password,
      String... commands) throws Exception {
    Map<String, List<Map<String, String>>> map = new HashMap<>();
    final CountDownLatch latch = new CountDownLatch(commands.length);
    for (String c : commands) {
      new Thread(new Runnable() {
        public void run() {
          try {
            map.put(c, exec_table(ip, port, username, password, c));
          } catch (Exception e) {
            map.put(c, new ArrayList<Map<String, String>>());
          }
          latch.countDown();
        }
      }).start();
    }
    latch.await();
    return map;
  }

  /**
   * exec执行单条命令，返回table json，要求命令结果为表格
   *
   * @param ip       ip地址
   * @param port     ssh 端口
   * @param username 远程访问用户名
   * @param password 远程访问密码
   * @param command  单条命令
   */
  public static List<Map<String, String>> exec_table(String ip, int port, String username, String password,
      String command) throws Exception {
    List<Map<String, String>> list = new ArrayList<>();
    List<String> results = new ArrayList<>();
    for (String s : exec(ip, port, username, password, command).toString().split("\n")) {
      results.add(s);
    }
    String[] titles = results.get(0).toLowerCase().split("\\s{2,}");
    for (int i = 1; i < results.size(); i++) {
      String[] ns = results.get(i).toLowerCase().split("\\s{2,}");
      Map<String, String> map = new HashMap<>();
      for (int t = 0; t < titles.length; t++) {
        if (ns.length > t) {
          map.put(titles[t], data.to_string(ns[t]));
        } else {
          map.put(titles[t], "");
        }
      }
      list.add(map);
    }
    return list;
  }

  /**
   * exec执行多条命令，返回结果
   *
   * @param ip       ip地址
   * @param port     ssh 端口
   * @param username 远程访问用户名
   * @param password 远程访问密码
   * @param commands 多条同步可执行命令
   */
  public static StringBuffer exec(String ip, int port, String username, String password, String... commands)
      throws Exception {
    return exec(null, ip, port, username, password, commands);
  }

  /**
   * exec执行多条命令，返回结果
   *
   * @param sock_session sock_session
   * @param ip           ip地址
   * @param port         ssh 端口
   * @param username     远程访问用户名
   * @param password     远程访问密码
   * @param commands     多条同步可执行命令
   */
  public static StringBuffer exec(javax.websocket.Session sock_session, String ip, int port, String username,
      String password, String... commands) throws Exception {
    InetAddress.getByName(ip).isReachable(500);

    StringBuffer sb = new StringBuffer();
    String command = "";
    for (String c : commands) {
      command += " && " + c;
    }
    command = command.substring(4);

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    JSch jsch = new JSch();
    Session session = jsch.getSession(username, ip, port);
    session.setPassword(password);
    session.setConfig(config);
    session.connect();

    Channel channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    channel.setInputStream(null);
    channel.setOutputStream(System.out);
    InputStream out = channel.getInputStream();
    InputStream error = channel.getExtInputStream();
    channel.connect();
    byte[] tmp = new byte[1024];
    byte[] tmp_error = new byte[1024];
    while (true) {
      while (out.available() > 0) {
        int i = out.read(tmp, 0, 1024);
        if (i < 0) {
          break;
        }
        String str = new String(tmp, 0, i);
        sb.append(str);
        if (sock_session != null) {
          sock_session.getBasicRemote().sendText(str);
        }
      }
      while (error.available() > 0) {
        int i = error.read(tmp_error, 0, 1024);
        if (i < 0) {
          break;
        }
        String str = new String(tmp_error, 0, i);
        sb.append(str);
        if (sock_session != null) {
          sock_session.getBasicRemote().sendText(str);
        }
      }
      if (channel.isEOF()) {
        break;
      }
      // try {
      // Thread.sleep(100);
      // } catch (Exception e) {
      // }
    }
    if (channel != null) {
      channel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
    return sb;
  }

  /**
   * shell方式执行多条命令，返回结果
   *
   * @param commands 多条同步可执行命令
   */
  public StringBuffer shell(String... commands) throws Exception {
    return this.shell(null, commands);
  }

  /**
   * shell方式执行多条命令，返回结果
   *
   * @param sock_session sock_session
   * @param commands     多条同步可执行命令
   */
  public StringBuffer shell(javax.websocket.Session sock_session, String... commands) throws Exception {
    StringBuffer sb = new StringBuffer();
    OutputStream outputStream = ((ChannelShell) channel).getOutputStream(); // 写入该流的数据
    // 都将发送到远程端
    // 使用PrintWriter 就是为了使用println 这个方法
    // 好处就是不需要每次手动给字符加\n
    PrintWriter printWriter = new PrintWriter(outputStream);
    for (String c : commands) {
      printWriter.println(c);
    }
    printWriter.println("exit");
    printWriter.flush(); // 把缓冲区的数据强行输出
    /**
     * shell管道本身就是交互模式的。要想停止，有两种方式： 一、人为的发送一个exit命令，告诉程序本次交互结束
     * 二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。 为了避免阻塞
     */
    InputStream inputStream = ((ChannelShell) channel).getInputStream();
    byte[] tmp = new byte[1024];
    while (true) {
      while (inputStream.available() > 0) {
        int i = inputStream.read(tmp, 0, 1024);
        if (i < 0) {
          break;
        }
        String str = new String(tmp, 0, i);
        sb.append(str);
        if (sock_session != null) {
          sock_session.getBasicRemote().sendText(str);
        }
      }
      if (channel.isEOF()) {
        break;
      }
      // try {
      // Thread.sleep(100);
      // } catch (Exception e) {
      // }
    }
    outputStream.close();
    inputStream.close();
    return sb;
  }

  /**
   * 关闭
   */
  public void close() {
    if (channel != null) {
      channel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
  }

  /**
   * 远程上传
   *
   * @param path_from 上传文件地址，本地地址，包含文件名
   * @param path_to   上传到服务器的地址，包含文件名
   */
  public void uploads(String path_from, String path_to) throws Exception {
    ((ChannelSftp) channel).put(path_from, path_to, ChannelSftp.OVERWRITE);
  }

  /**
   * 远程上传
   *
   * @param path_from 上传文件地址，本地地址，包含文件名
   * @param path_to   上传到服务器的地址，包含文件名
   */
  public static void uploads(String ip, int port, String username, String password, String path_from, String path_to)
      throws Exception {
    jsch j = new jsch(ip, port, username, password, "sftp");
    ((ChannelSftp) j.channel).put(path_from, path_to, ChannelSftp.OVERWRITE);
    j.close();
  }

  /**
   * 远程下载
   *
   * @param path_from 下载文件地址，服务器地址，包含文件名
   * @param path_to   下载到本地的地址，包含文件名
   */
  public String downloads(String path_from, String path_to) throws Exception {
    File file = new File(path_to);
    FileOutputStream fieloutput = new FileOutputStream(file);
    ((ChannelSftp) channel).get(path_from, fieloutput);
    return file.getName();
  }

  /**
   * 远程下载
   *
   * @param path_from 下载文件地址，服务器地址，包含文件名
   * @param path_to   下载到本地的地址，包含文件名
   */
  public static String downloads(String ip, int port, String username, String password, String path_from,
      String path_to) throws Exception {
    jsch j = new jsch(ip, port, username, password, "sftp");
    File file = new File(path_to);
    FileOutputStream fieloutput = new FileOutputStream(file);
    ((ChannelSftp) j.channel).get(path_from, fieloutput);
    j.close();
    return file.getName();
  }
}
