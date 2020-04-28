package com.kubernetes.monitor.dao;

import com.kubernetes.monitor.entity.VmInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VmDao extends JpaRepository<VmInfo,String> {
}
