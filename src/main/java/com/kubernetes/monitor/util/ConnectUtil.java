package com.kubernetes.monitor.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.config.resultcode.ResultEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectUtil {
    private final static int port = 22;//22 usually the default port

    public static void exec(VmInfo vmInfo, String cmd, boolean isRoot) throws CustomException {
        Connection conn = new Connection(vmInfo.getHostname(), port);
        Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn;
            if (isRoot) {
                isconn = conn.authenticateWithPassword(vmInfo.getRootName(), vmInfo.getRootPassword());
            } else {
                isconn = conn.authenticateWithPassword(vmInfo.getUsername(), vmInfo.getPassword());
            }

            if (!isconn) {
                System.out.println("用户名称或者是密码不正确");
                throw new CustomException(ResultEnum.UN_OR_PW_ERROR);
            } else {
                System.out.println("已经连接OK");

                //执行命令
                ssh = conn.openSession();
                ssh.execCommand(cmd);

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
