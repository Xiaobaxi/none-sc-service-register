package io.github.xiaobaxi.serviceregister;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * pojo for the none spring cloud service register
 * @author fangzhibin
 */
@Data
@AllArgsConstructor
@Builder
public class NoneSpringCloudInfo {

  @NotNull
  private String name;

  @NotNull
  private String instanceId;

  @NotNull
  private String address;

  @NotNull
  private Integer port;

  @NotNull
  private String contextPath;

  private String checkUrl;

}
