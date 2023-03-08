package com.runjian.auth.gateway.config;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.runjian.auth.gateway.properties.SwaggerModelNameProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SwaggerResources
 * @Description Swagger配置类
 * @date 2023-02-28 周二 18:46
 */
@Slf4j
@Component
public class SwaggerResources implements SwaggerResourcesProvider {

    /**
     * 接口地址
     */
    public static final String API_URI = "v3/api-docs";

    /**
     * 路由加载器
     */
    @Resource
    private RouteLocator routeLocator;

    @Resource
    private GatewayProperties gatewayProperties;

    @Resource
    private SwaggerModelNameProperties properties;

    @Override
    public List<SwaggerResource> get() {
        // 接口资源列表
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        List<Map<String, String>> modelNameList = properties.getModelName();
        // 获取所有路由的ID
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        // 过滤出配置文件中定义的路由->过滤出Path Route Predicate->根据路径拼接成api-docs路径->生成SwaggerResource
        gatewayProperties.getRoutes().stream().filter(
                routeDefinition -> routes.contains(routeDefinition.getId())).forEach(
                route -> route.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            Map<String, String> args = predicateDefinition.getArgs();
                            String location = args.get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("**", API_URI);
                            String modelName = route.getId();
                            for (Map<String, String> map : modelNameList) {
                                String name = map.get(modelName);
                                if (StrUtil.isNotBlank(name)) {
                                    modelName = name;
                                    break;
                                }
                            }
                            SwaggerResource swaggerResource = swaggerResource(modelName, location);
                            resources.add(swaggerResource);
                        }));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
