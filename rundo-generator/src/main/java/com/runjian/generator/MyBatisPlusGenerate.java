package com.runjian.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName Application
 * @Description 代码生成器
 * @date 2022-12-30 周五 9:46
 */
public class MyBatisPlusGenerate {

    /**
     * 作者
     */
    private static final String AUTHOR = "Jiang4Yu@126.com";

    /**
     * 工作目录
     */
    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * JDBC 驱动程序
     */
    private static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    /**
     * JDBC 连接地址
     */
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/rundo_rbac?serverTimezone=Asia/Shanghai" +
            "&useLegacyDatetimeCode=false" +
            "&nullNamePatternMatchesAll=true" +
            "&allowPublicKeyRetrieval=true" +
            "&zeroDateTimeBehavior=CONVERT_TO_NULL&tinyInt1isBit=false" +
            "&autoReconnect=true&useSSL=false" +
            "&pinGlobalTxToPhysicalConnection=true";

    /**
     * 数据库账号
     */
    private static final String JDBC_USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String JDBC_PASSWORD = "root";

    /**
     * 包配置 - 父级目录
     */
    private static final String PACKAGE_PARENT = "com.runjian.auth.server";

    /**
     * 包配置 - 模块目录 <br>
     * 注意：如果表前缀与模块命相同，生成时会删除前缀，比如：core_admin 最终构建为 Admin, AdminController ...
     */
    private static final String PACKAGE_MODULE_NAME = "";

    /**
     * 包配置 - 实体类目录
     */
    private static final String PACKAGE_ENTITY = "entity";

    /**
     * 包配置 - 数据访问接口目录
     */
    private static final String PACKAGE_MAPPER = "mapper";

    /**
     * 包配置 - 业务处理接口目录
     */
    private static final String PACKAGE_SERVICE = "service";

    /**
     * 包配置 - 业务处理实现目录
     */
    private static final String PACKAGE_SERVICE_IMPL = "service.impl";

    /**
     * 包配置 - 控制器目录
     */
    private static final String PACKAGE_CONTROLLER = "controller";

    /**
     * 父类配置 - entity
     */
    private static final String SUPER_ENTITY = "com.runjian.auth.server.base.BaseEntity";

    /**
     * 父类配置 - service
     */
    private static final String SUPER_SERVICE = "com.runjian.common.base.IBaseService";

    /**
     * 父类配置 - serviceImpl
     */
    private static final String SUPER_SERVICEIMPL = "com.runjian.common.base.BaseServiceImpl";

    /**
     * 父类配置 - controller
     */
    private static final String SUPER_CONTROLLER = "com.runjian.common.base.BaseController";

    /**
     * 要生成的表，用 `,` 分割，all 代表生成所有表
     */
    private static final String TABLES = "api_info," +
            "app_info," +
            "config_info," +
            "dict_info," +
            "menu_info," +
            "user_info," +
            "role_info," +
            "video_area," +
            "org_info";

    /**
     * 数据源配置
     *
     * @return {@link DataSourceConfig}
     */
    private static DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig.Builder(
                JDBC_URL,
                JDBC_USERNAME,
                JDBC_PASSWORD
        ).build();
    }

    /**
     * 全局配置
     *
     * @return {@link GlobalConfig}
     */
    private static GlobalConfig globalConfig() {

        return new GlobalConfig.Builder()
                // 设置作者名
                .author(AUTHOR)
                // 设置输出路径：项目的 java 目录下
                .outputDir(USER_DIR + "/src/main/java")
                // 注释日期时间格式
                .commentDate("yyyy-MM-dd HH:mm:ss")
                // 定义生成的实体类中日期的类型
                // .dateType(DateType.ONLY_DATE)
                // 覆盖之前的文件
                .fileOverride()
                // 开启 swagger 模式
                .enableSwagger()
                // 禁止打开输出目录，默认打开
                .disableOpenDir()
                .build();
    }


    /**
     * 包配置
     *
     * @return {@link PackageConfig}
     */
    private static PackageConfig packageConfig() {
        return new PackageConfig.Builder()
                // 设置父包名
                .parent(PACKAGE_PARENT)
                // 设置模块包名
                .moduleName(PACKAGE_MODULE_NAME)
                // 实体类包名
                .entity(PACKAGE_ENTITY)
                // Mapper 包名
                .mapper(PACKAGE_MAPPER)
                // Service 包名
                .service(PACKAGE_SERVICE)
                // ServiceImpl 包名
                .serviceImpl(PACKAGE_SERVICE_IMPL)
                // Controller 包名
                .controller(PACKAGE_CONTROLLER)
                // Mapper XML 包名
                .xml(null)
                // 自定义文件包名
                .other(null)
                // 配置 mapper.xml 路径信息：项目的 resources 目录下
                .pathInfo(
                        Collections.singletonMap(
                                OutputFile.xml,
                                USER_DIR + "/src/main/resources/mapper"
                        )
                )
                .build();
    }

    /**
     * 代码生成策略配置
     *
     * @return {@link StrategyConfig}
     */
    private static StrategyConfig strategyConfig() {
        // 策略配置,数据库表配置
        return new StrategyConfig.Builder()
                // 设置需要生成的数据表名
                .addInclude(getTables(TABLES))
                // 设置过滤表前缀
                .addTablePrefix(packageConfig().getModuleName() + "_", "tb_")
                // 实体类策略配置
                .entityBuilder()
                // 开启 Lombok
                .enableLombok()
                // 不实现 Serializable 接口，不生产 SerialVersionUID
                // .disableSerialVersionUID()
                // 逻辑删除字段名
                .logicDeleteColumnName("delete_flag")
                // 数据库表映射到实体的命名策略：下划线转驼峰命
                .naming(NamingStrategy.underline_to_camel)
                // 数据库表字段映射到实体的命名策略：下划线转驼峰命
                .columnNaming(NamingStrategy.underline_to_camel)
                // 定义在基础BaseDomain中的通用字段
                // .addSuperEntityColumns("id", "created_time", "updated_time")
                //添加表字段填充，"create_time"字段自动填充为插入时间，"update_time"字段自动填充为插入修改时间
                .addTableFills(
                        new Column("delete_flag", FieldFill.INSERT),
                        new Column("created_time", FieldFill.INSERT),
                        new Column("created_by", FieldFill.INSERT),
                        new Column("updated_time", FieldFill.INSERT_UPDATE),
                        new Column("updated_by", FieldFill.INSERT_UPDATE)
                )
                // 开启生成实体时生成字段注解
                .enableTableFieldAnnotation()

                // .superClass(SUPER_ENTITY)

                // Mapper策略配置
                .mapperBuilder()
                // 设置父类
                .superClass(BaseMapper.class)
                // 格式化 mapper 文件名称
                .formatMapperFileName("%sMapper")
                // 开启 @Mapper 注解
                .enableMapperAnnotation()
                // 格式化 Xml 文件名称
                .formatXmlFileName("%sMapper")

                // Service 策略配置
                .serviceBuilder()
                // 父类配置
                // .superServiceClass(SUPER_SERVICE)
                // 格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                .formatServiceFileName("%sService")
                // 父类配置
                // .superServiceImplClass(SUPER_SERVICEIMPL)
                // 格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                .formatServiceImplFileName("%sServiceImpl")

                // Controller 策略配置
                .controllerBuilder()
                // 父类配置
                // .superClass(SUPER_CONTROLLER)
                // 格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                .formatFileName("%sController")
                // 开启生成 @RestController 控制器
                .enableRestStyle()
                .build();
    }

    /**
     * 代码生成模板配置 - Freemarker
     *
     * @return {@link TemplateConfig}
     */
    private static TemplateConfig templateConfig() {
        return new TemplateConfig.Builder()
                .entity("templates/entity.java")
                .mapper("templates/mapper.java")
                .service("templates/service.java")
                .serviceImpl("templates/serviceImpl.java")
                .controller("templates/controller.java")
                .xml("templates/mapper.xml")
                .build();
    }


    /**
     * 处理获取所有表的情况
     *
     * @param tables
     * @return
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    /**
     * 自定义配置
     */
    private static InjectionConfig injectionConfig() {

        return new InjectionConfig.Builder()
                .beforeOutputFile(((tableInfo, stringObjectMap) -> {

                }))

                .build();
    }

    public static void main(String[] args) {

        DataSourceConfig dataSourceConfig = dataSourceConfig();
        GlobalConfig globalConfig = globalConfig();
        PackageConfig packageConfig = packageConfig();
        StrategyConfig strategyConfig = strategyConfig();
        TemplateConfig templateConfig = templateConfig();
        InjectionConfig injectionConfig = injectionConfig();

        AutoGenerator generator = new AutoGenerator(dataSourceConfig);
        generator.global(globalConfig)
                .packageInfo(packageConfig)
                .strategy(strategyConfig)
                .template(templateConfig)
                .injection(injectionConfig)
                .execute(new FreemarkerTemplateEngine());
    }
}
