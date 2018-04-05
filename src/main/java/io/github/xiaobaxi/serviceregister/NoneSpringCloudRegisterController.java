package io.github.xiaobaxi.serviceregister;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller for the none spring cloud service register
 * @author fangzhibin
 */
@RestController
public class NoneSpringCloudRegisterController {
  @Autowired
  private TtlScheduler ttlScheduler;

  @Autowired
  private ConsulDiscoveryProperties consulDiscoveryProperties;

  @Autowired
  private ConsulServiceRegistry consulServiceRegistry;

  @Autowired
  private HeartbeatProperties heartbeatProperties;

  @PostMapping("/register")
  public String register(@RequestBody @Valid NoneSpringCloudInfo noneSpringCloudInfo) {
    NoneSpringCloudRegister noneSpringCloudRegister = new NoneSpringCloudRegister(consulServiceRegistry, consulDiscoveryProperties, heartbeatProperties, ttlScheduler);
    noneSpringCloudRegister.register(noneSpringCloudInfo);
    return "SUCCESS";
  }

  @RequestMapping("/unRegister/{instanceId}")
  public String unRegister(@PathVariable @NotNull String instanceId) {
    NoneSpringCloudRegister noneSpringCloudRegister = new NoneSpringCloudRegister(consulServiceRegistry, consulDiscoveryProperties, heartbeatProperties, ttlScheduler);
    noneSpringCloudRegister.unRegister(instanceId);
    return "SUCCESS";
  }
}
