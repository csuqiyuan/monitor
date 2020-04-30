package com.kubernetes.monitor.controller;

import com.kubernetes.monitor.entity.Patch;
import com.kubernetes.monitor.service.*;
import com.kubernetes.monitor.util.response.ResponseMessage;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class KubeController {

    private ServiceService serviceService;
    private PodService podService;
    private DepolyService depolyService;
    private NodeService nodeService;
    private NamespaceService namespaceService;

    @Autowired
    public KubeController(ServiceService serviceService,
                          PodService podService,
                          DepolyService depolyService,
                          NodeService nodeService,
                          NamespaceService namespaceService) {
        this.serviceService = serviceService;
        this.podService = podService;
        this.depolyService = depolyService;
        this.nodeService = nodeService;
        this.namespaceService = namespaceService;
    }

    /* pod start */
    @PostMapping("/{namespace}/pod")
    public ResponseMessage createPod(@RequestBody V1Pod pod,
                                     @PathVariable("namespace") String namespace) {

        return podService.createNamespacedPod(pod, namespace);
    }

    @DeleteMapping("/{namespace}/pod/{name}")
    public ResponseMessage deletePod(@PathVariable("name") String name,
                                     @PathVariable("namespace") String namespace) {
        return podService.deleteNamespacedPod(name, namespace);
    }

    @GetMapping("/{namespace}/pods")
    public ResponseMessage listPod(@PathVariable("namespace") String namespace) {
        return podService.listNamespacedPod(namespace);
    }

    @GetMapping("/{namespace}/pod/{name}")
    public ResponseMessage readPod(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return podService.readNamespacedPod(name, namespace);
    }
    /* pod end */

    /* service start */
    @PostMapping("/{namespace}/service")
    public ResponseMessage createService(@RequestBody V1Service body,
                                         @PathVariable("namespace") String namespace) {

        return serviceService.createNamespacedService(body, namespace);
    }

    @DeleteMapping("/{namespace}/service/{name}")
    public ResponseMessage deleteService(@PathVariable("name") String name,
                                         @PathVariable("namespace") String namespace) {
        return serviceService.deleteNamespacedService(name, namespace);
    }

    @GetMapping("/{namespace}/services")
    public ResponseMessage listService(@PathVariable("namespace") String namespace) {
        return serviceService.listNamespacedService(namespace);
    }

    @GetMapping("/{namespace}/service/{name}")
    public ResponseMessage readService(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return serviceService.readNamespacedService(name, namespace);
    }
    /* service end */

    /* deployment start */
    @PostMapping("/{namespace}/deployment")
    public ResponseMessage createDeployment(@RequestBody V1Deployment body,
                                            @PathVariable("namespace") String namespace) {
        return depolyService.createNamespacedDeploy(body, namespace);
    }

    @DeleteMapping("/{namespace}/deployment/{name}")
    public ResponseMessage deleteDeployment(@PathVariable("name") String name,
                                            @PathVariable("namespace") String namespace) {
        return depolyService.deleteNamespacedDeploy(name, namespace);
    }

    @GetMapping("/{namespace}/deployments")
    public ResponseMessage listDeployment(@PathVariable("namespace") String namespace) {
        return depolyService.listNamespacedDeploy(namespace);
    }

    @GetMapping("/{namespace}/deployment/{name}")
    public ResponseMessage readDeployment(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return depolyService.readNamespacedDeploy(name, namespace);
    }

    @PatchMapping("/{namespace}/deployment/{name}")
    public ResponseMessage patchDeployment(@PathVariable("namespace") String namespace,
                                           @PathVariable("name") String name,
                                           @RequestBody Patch patch) {
        return depolyService.patchNamespacedDeploy(name, namespace, patch);
    }
    /* deployment end */

    /* node start */
    @GetMapping("/nodes")
    public ResponseMessage listNode() {
        return nodeService.listNode();
    }
    /* node end */

    /* deployment start */
    @PostMapping("/namespace")
    public ResponseMessage createNamespace(@RequestBody V1Namespace body) {
        return namespaceService.createNamespace(body);
    }

    @DeleteMapping("/namespace/{name}")
    public ResponseMessage deleteNamespace(@PathVariable("name") String name) {
        return namespaceService.deleteNamespace(name);
    }

    @GetMapping("/namespaces")
    public ResponseMessage listNamespace() {
        return namespaceService.listNamespace();
    }

    @GetMapping("/namespace/{name}")
    public ResponseMessage readNamespace(@PathVariable("name") String name) {
        return namespaceService.readNamespace(name);
    }
    /* deployment end */
}
