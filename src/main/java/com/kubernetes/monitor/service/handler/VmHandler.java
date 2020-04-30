package com.kubernetes.monitor.service.handler;

import com.kubernetes.monitor.dao.VmDao;
import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VmHandler {
    private VmDao vmDao;

    @Autowired
    public VmHandler(VmDao vmDao) {
        this.vmDao = vmDao;
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
