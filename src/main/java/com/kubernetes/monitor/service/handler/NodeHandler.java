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
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    public void getCluster() throws CustomException{
        VmInfo master = vmDao.getMaster();
        if (master!=null){
            new KubeClient(master.getHostname());
        }
        else{
            throw new CustomException(ResultEnum.NOT_FIND_MASTER);
        }
    }

    public void exit(){
        vmDao.deleteAll();
        new KubeClient(null);
    }

    public List<Node> listNode() throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        V1NodeList list = apiInstance.listNode(null, null, null,
                null, null, null, null, null, null);
        LinkedList<Node> result = new LinkedList<>();
        for (V1Node item:list.getItems()){
            Node node = Node.toNode(item);
            List<V1Pod> pods = podHandler.listPodsByNode(node.getAddress());
            BigDecimal usedCpu = new BigDecimal("0");
            BigDecimal usedMemory = new BigDecimal("0");
            for (V1Pod pod:pods){
                for (V1Container container:pod.getSpec().getContainers()){
                    if (container.getResources().getLimits()==null||
                            container.getResources().getLimits().get("cpu")==null||
                            container.getResources().getLimits().get("memory")==null||
                            container.getResources().getLimits().get("cpu").getNumber()==null||
                            container.getResources().getLimits().get("memory").getNumber()==null)
                        continue;
                    usedCpu = usedCpu.add(container.getResources().getLimits().get("cpu").getNumber());
                    usedMemory = usedMemory.add(container.getResources().getLimits().get("memory").getNumber());
                }
            }
            node.setUsableCpu(node.getRestCpu().subtract(usedCpu));
            node.setUsableMemory(node.getRestMemory().subtract(usedMemory));
            result.add(node);
        }
        return result;
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

    public List<VmInfo> listVm() {
        return vmDao.findAll();
    }

    public VmInfo getMaster() {

        return vmDao.getMaster();
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
