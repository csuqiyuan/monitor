package com.kubernetes.monitor.service;

import com.kubernetes.monitor.dao.VmDao;
import com.kubernetes.monitor.entity.VmInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VmService {
    @Autowired
    private VmDao vmDao;
    public VmInfo addVm(VmInfo info){
        return vmDao.save(info);
    }
    public void deleteVm(String hostname){
        vmDao.deleteById(hostname);
    }
}
