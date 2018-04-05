package io.github.xiaobaxi.serviceregister;

import com.ecwid.consul.v1.agent.model.NewService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.util.StringUtils;

/**
 * handler for the none spring cloud service register
 * @author fangzhibin
 */
public class NoneSpringCloudRegister {

  private final TtlScheduler ttlScheduler;

  private ConsulServiceRegistry consulServiceRegistry;

  private ConsulDiscoveryProperties consulDiscoveryProperties;

  private HeartbeatProperties heartbeatProperties;


  public NoneSpringCloudRegister(ConsulServiceRegistry consulServiceRegistry, ConsulDiscoveryProperties consulDiscoveryProperties, HeartbeatProperties heartbeatProperties, TtlScheduler ttlScheduler) {
    this.consulServiceRegistry = consulServiceRegistry;
    this.consulDiscoveryProperties = consulDiscoveryProperties;
    this.heartbeatProperties = heartbeatProperties;
    this.ttlScheduler = ttlScheduler;
  }

  public void register(NoneSpringCloudInfo noneSpringCloudInfo) {
    NewService service = new NewService();
    service.setId(noneSpringCloudInfo.getInstanceId());
    service.setAddress(noneSpringCloudInfo.getAddress());
    service.setPort(noneSpringCloudInfo.getPort());
    service.setName(noneSpringCloudInfo.getName());
    List<String> tags = new ArrayList<>();
    tags.add("contextPath=" + noneSpringCloudInfo.getContextPath());
    service.setTags(tags);
    service.setCheck(createCheck(noneSpringCloudInfo, heartbeatProperties, consulDiscoveryProperties));
    ConsulRegistration registration = new ConsulRegistration(service, this.consulDiscoveryProperties);
    try {
      consulServiceRegistry.register(registration);
    } catch (RuntimeException e) {
    } finally {}
  }

  public void unRegister(String instanceId) {
    if (ttlScheduler != null) {
      ttlScheduler.remove(instanceId);
    }
    NewService service = new NewService();
    service.setId(instanceId);
    ConsulRegistration registration = new ConsulRegistration(service, this.consulDiscoveryProperties);
    consulServiceRegistry.deregister(registration);
  }

  private NewService.Check createCheck(NoneSpringCloudInfo noneSpringCloudInfo, HeartbeatProperties heartbeatProperties, ConsulDiscoveryProperties consulDiscoveryProperties) {
    NewService.Check check = new NewService.Check();
    if (heartbeatProperties.isEnabled()) {
      check.setTtl(heartbeatProperties.getTtl());
      return check;
    }
    String checkUrl = noneSpringCloudInfo.getCheckUrl();
    if (null != checkUrl) {
      check.setHttp(checkUrl);
    } else {
      check.setHttp(String.format("%s://%s:%s%s", consulDiscoveryProperties.getScheme(),
          noneSpringCloudInfo.getAddress(), noneSpringCloudInfo.getPort(),
          consulDiscoveryProperties.getHealthCheckPath()));
    }
    check.setInterval(consulDiscoveryProperties.getHealthCheckInterval());
    check.setTimeout(consulDiscoveryProperties.getHealthCheckTimeout());
    if (StringUtils.hasText(consulDiscoveryProperties.getHealthCheckCriticalTimeout())) {
      check.setDeregisterCriticalServiceAfter(consulDiscoveryProperties.getHealthCheckCriticalTimeout());
    }
    check.setTlsSkipVerify(consulDiscoveryProperties.getHealthCheckTlsSkipVerify());
    return check;
  }

}
