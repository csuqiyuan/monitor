package com.kubernetes.monitor.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.util.resultcode.ResultEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectUtil {
    private final static int port = 22;//22 usually the default port

    public void exec(String hostname, String username, String password) throws CustomException{
        Connection conn = new Connection(hostname, port);
        Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if (!isconn) {
                System.out.println("用户名称或者是密码不正确");
                throw new CustomException(ResultEnum.UN_OR_PW_ERROR);
            } else {
                System.out.println("已经连接OK");

                //将本地conf/server_start.sh传输到远程主机的/opt/pg944/目录下
                //可以用于两个机器间传文件
//                SCPClient clt = conn.createSCPClient();
//                clt.put("conf/server_start.sh", "/opt/pg944/");

                //执行命令
                ssh = conn.openSession();
                ssh.execCommand("echo \"testtest\"");
                //只允许使用一行命令，即ssh对象只能使用一次execCommand这个方法，多次使用则会出现异常.
                //使用多个命令用分号隔开
                //ssh.execCommand("cd /root; sh hello.sh");

                //将Terminal屏幕上的文字全部打印出来
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    String line = brs.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
