package com.kubernetes.monitor.service.handler;

import com.kubernetes.monitor.config.resultcode.ResultEnum;
import com.kubernetes.monitor.dao.VmDao;
import com.kubernetes.monitor.entity.Node;
import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.util.KubeClient;
import com.kubernetes.monitor.util.exception.CustomException;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class NodeHandler {
    private String[] arg = {
            "kube-proxy",
            "coredns"
    };
    private VmDao vmDao;
    private PodHandler podHandler;

    @Autowired
    public NodeHandler(VmDao vmDao,
                       PodHandler podHandler) {
        this.vmDao = vmDao;
        this.podHandler = podHandler;
    }

    public void getCluster() throws CustomException {
        VmInfo master = vmDao.getMaster();
        if (master != null) {
            new KubeClient(master.getHostname());
        } else {
            throw new CustomException(ResultEnum.NOT_FIND_MASTER);
        }
    }

    public void exit() {
        deleteAll();
        new KubeClient(null);
    }

    public void deleteAll() {
        vmDao.deleteAll();
    }

    public List<Node> listNode() throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        V1NodeList list = apiInstance.listNode(null, null, null,
                null, null, null, null, null, null);
        LinkedList<Node> result = new LinkedList<>();
        for (V1Node item : list.getItems()) {
            Node node = toNode(item);
            result.add(node);
        }
        return result;
    }

    public Node readNode(String name) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        V1Node item = apiInstance.readNode(name, null, null, null);
        return toNode(item);
    }

    public Node toNode(V1Node item) throws ApiException {
        Node node = Node.toNode(item);
        VmInfo master = getMaster();
        if (item.getMetadata().getCreationTimestamp() != null) {
            Date date = item.getMetadata().getCreationTimestamp().toDate();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String createTime = format.format(date);
            node.setCreateTime(createTime);
        }
        V1PodList pods = podHandler.listPodsByNode(node.getHostname());
        BigDecimal usedCpu = new BigDecimal("0");
        BigDecimal usedMemory = new BigDecimal("0");
        for (V1Pod pod : pods.getItems()) {
            for (V1Container container : pod.getSpec().getContainers()) {
                if (container.getResources().getLimits() == null ||
                        container.getResources().getLimits().get("cpu") == null ||
                        container.getResources().getLimits().get("memory") == null ||
                        container.getResources().getLimits().get("cpu").getNumber() == null ||
                        container.getResources().getLimits().get("memory").getNumber() == null) {
                    try {
                        usedCpu = usedCpu.add(container.getResources().getLimits().get("cpu").getNumber());
                    } catch (NullPointerException e) {
                        try {
                            usedCpu = usedCpu.add(container.getResources().getRequests().get("cpu").getNumber());
                        }catch (NullPointerException e1){}
                    }
                    try {
                        usedMemory = usedMemory.add(container.getResources().getLimits().get("memory").getNumber());
                    } catch (NullPointerException e) {
                        try {
                            usedCpu = usedCpu.add(container.getResources().getRequests().get("memory").getNumber());
                        }catch (NullPointerException e1){}
                    }
                    continue;
                }
                usedCpu = usedCpu.add(container.getResources().getLimits().get("cpu").getNumber());
                usedMemory = usedMemory.add(container.getResources().getLimits().get("memory").getNumber());
            }
        }
        node.setUsableCpu(node.getRestCpu().subtract(usedCpu));
        node.setUsableMemory(node.getRestMemory().subtract(usedMemory));
        if (node.getAddress().equals(master.getHostname())){
            node.setIsMaster(1);
        }
        else{
            node.setIsMaster(0);
        }
        return node;
    }

    public V1Status removeNode(String name) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        return apiInstance.deleteNode(name, null, null, null, null,
                null, null);
    }

    public VmInfo addVm(VmInfo info) {
        return vmDao.save(info);
    }

    public void deleteVm(String hostname) throws CustomException {
        try {
            vmDao.deleteById(hostname);
        } catch (Exception e) {
            throw new CustomException(500, e.getMessage());
        }
    }

    public VmInfo getMaster() {

        return vmDao.getMaster();
    }

    public VmInfo getVm(String name) {
        return vmDao.findById(name).orElse(new VmInfo());
    }

    public VmInfo postTokenAndSha(TokenAndSha update) throws CustomException {
        try {
            VmInfo vmInfo = getMaster();
            vmInfo.setToken(update.getToken());
            vmInfo.setSha(update.getSha());
            return vmDao.save(vmInfo);
        } catch (NullPointerException e) {
            throw new CustomException(ResultEnum.NOT_FIND_MASTER);
        }
    }

}
