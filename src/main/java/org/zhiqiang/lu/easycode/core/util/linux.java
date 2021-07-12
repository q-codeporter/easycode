package org.zhiqiang.lu.easycode.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Created by riot94 on 1/6/2017.
 */

public class linux {

  public static void main(String[] args) {
  }

  public Session session(String host, String user, String password) throws Exception {
    InetAddress.getByName(host).isReachable(500);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    JSch jsch = new JSch();
    Session session = jsch.getSession(user, host, 22);
    session.setPassword(password);
    session.setConfig(config);
    session.setTimeout(1000);
    session.connect();
    return session;
  }

  public StringBuffer exec(Session session, String... commands) throws Exception {
    StringBuffer sb = new StringBuffer();
    String command = "";
    for (String c : commands) {
      command += " && " + c;
    }
    command = command.substring(4);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
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
      }
      while (error.available() > 0) {
        int i = error.read(tmp_error, 0, 1024);
        if (i < 0) {
          break;
        }
        String str = new String(tmp, 0, i);
        sb.append(str);
      }
      if (channel.isEOF()) {
        break;
      }
    }
    if (channel != null) {
      channel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
    return sb;
  }

  // public static StringBuffer execCommandByShell(javax.websocket.Session
  // sock_session, Session session,
  // String... commands) throws Exception {
  // StringBuffer result = new StringBuffer();
  // ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
  // InputStream inputStream = channelShell.getInputStream();
  // // channelShell.setPty(true);
  // channelShell.connect();
  // OutputStream outputStream = channelShell.getOutputStream();// 写入该流的数据
  // 都将发送到远程端

  // // 使用PrintWriter 就是为了使用println 这个方法
  // // 好处就是不需要每次手动给字符加\n
  // PrintWriter printWriter = new PrintWriter(outputStream);
  // for (String c : commands) {
  // printWriter.println(c);
  // }
  // printWriter.flush();// 把缓冲区的数据强行输出

  // /**
  // * shell管道本身就是交互模式的。要想停止，有两种方式： 一、人为的发送一个exit命令，告诉程序本次交互结束
  // * 二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。 为了避免阻塞
  // */
  // byte[] tmp = new byte[1024];
  // while (true) {
  // while (inputStream.available() > 0) {
  // int i = inputStream.read(tmp, 0, 1024);
  // if (i < 0) {
  // break;
  // }
  // String str = new String(tmp, 0, i);
  // str = str.replaceAll("\n\r", "<br/>");
  // str = str.replaceAll("\b", "");
  // str = str.replaceAll("\t", "");
  // str = str.replaceAll("\n", "");
  // str = str.replaceAll("\f", "");
  // str = str.replaceAll("\r", "");
  // System.out.println(str);
  // sock_session.getBasicRemote().sendText(str);
  // }
  // if (channelShell.isEOF()) {
  // System.out.println("breakbreakbreakbreakbreakbreakbreakbreak");
  // break;
  // }
  // try {
  // Thread.sleep(100);
  // } catch (Exception e) {
  // }
  // }
  // outputStream.close();
  // inputStream.close();
  // channelShell.disconnect();
  // session.disconnect();
  // return result;
  // }
}