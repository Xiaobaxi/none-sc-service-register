package io.github.xiaobaxi.serviceregister;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * configuration for the none spring cloud service register
 * @author fangzhibin
 */
@Configuration
@EnableConfigurationProperties
@AutoConfigureAfter({ConsulAutoConfiguration.class, ServiceRegistryAutoConfiguration.class})
public class NoneSpringCloudConfiguration {

  @Bean
  public HeartbeatProperties heartbeatProperties() {
    return new HeartbeatProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  public ConsulDiscoveryProperties consulDiscoveryProperties(InetUtils inetUtils) {
    return new ConsulDiscoveryProperties(inetUtils);
  }

  @Bean
  @ConditionalOnMissingBean
  public TtlScheduler ttlScheduler(ConsulClient consulClient, HeartbeatProperties heartbeatProperties) {
    return new TtlScheduler(heartbeatProperties, consulClient);
  }

  @Bean
  @ConditionalOnMissingBean
  public ConsulServiceRegistry consulServiceRegistry(ConsulClient consulClient, ConsulDiscoveryProperties properties,
      HeartbeatProperties heartbeatProperties) {
    return new ConsulServiceRegistry(consulClient, properties, ttlScheduler(consulClient, heartbeatProperties), heartbeatProperties);
  }
}
