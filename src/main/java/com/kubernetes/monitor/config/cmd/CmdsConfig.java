package com.kubernetes.monitor.config.cmd;

public class CmdsConfig {

    public static String uninstall(String username){
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -u", username);
    }

    public static String netWork(String username) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -p", username);
    }

    public static String getTool(String username) {
        return String.format("yum install git -y;rm -rf /home/%s/kube_tool;git clone https://github.com/csuqiyuan/kube_tool.git /home/%s/kube_tool;cd /home/%s;chmod -R 777 kube_tool/", username, username, username);
    }

    public static String cpConfig(String username,String password) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -c %s", username, password);
    }

    public static String installMaster(String username, String privateDockerRepo) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -m %s", username, privateDockerRepo);
    }

    public static String postToken(String username, String serverIp) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -t %s", username, serverIp);
    }

    public static String createPostToken(String username,String serverIp) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -r %s",username, serverIp);
    }

    public static String installNode(String username,String args) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -n %s",username, args);
    }

    public static String scpConfig(String username,String args) {
        return String.format("cd /home/%s/kube_tool;./kube_tool.sh -s %s",username, args);
    }
}
