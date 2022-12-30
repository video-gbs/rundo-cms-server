package com.rundo.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
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
    private static final String AUTHOR = "Jiang4Yu";

    /**
     * 工作目录
     */
    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * JDBC 连接地址
     */
    private static final String JDBC_URL = "jdbc:mysql://192.168.91.100:3306/rundo?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&nullNamePatternMatchesAll=true&zeroDateTimeBehavior=CONVERT_TO_NULL&tinyInt1isBit=false&autoReconnect=true&useSSL=false&pinGlobalTxToPhysicalConnection=true";

    /**
     * JDBC 驱动程序
     */
    private static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库账号
     */
    private static final String JDBC_USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String JDBC_PASSWORD = "JiangYu)1234";

    /**
     * 包配置 - 父级目录
     */
    private static final String PACKAGE_PARENT = "com.runjian.rundo";

    /**
     * 包配置 - 模块目录 <br>
     * 注意：如果表前缀与模块命相同，生成时会删除前缀，比如：core_admin 最终构建为 Admin, AdminController ...
     */
    private static final String PACKAGE_MODULE_NAME = "";

    /**
     * 包配置 - 实体类目录
     */
    private static final String PACKAGE_ENTITY = "domain";

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
     * 要生成的表，用 `,` 分割
     */
    private static final String TABLES = "core_post";

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
                .dateType(DateType.ONLY_DATE)
                // 覆盖之前的文件
                .fileOverride()
                // 开启 swagger 模式
                // .enableSwagger()
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
                .addTablePrefix(packageConfig().getModuleName() + "_")
                // 实体类策略配置
                .entityBuilder()
                // 开启 Lombok
                .enableLombok()
                // 不实现 Serializable 接口，不生产 SerialVersionUID
                // .disableSerialVersionUID()
                // 逻辑删除字段名
                // .logicDeleteColumnName("deleted")
                // 数据库表映射到实体的命名策略：下划线转驼峰命
                .naming(NamingStrategy.underline_to_camel)
                // 数据库表字段映射到实体的命名策略：下划线转驼峰命
                .columnNaming(NamingStrategy.underline_to_camel)
                // 定义在基础BaseDomain中的通用字段
                .addSuperEntityColumns("id", "create_time", "update_time")
                //添加表字段填充，"create_time"字段自动填充为插入时间，"update_time"字段自动填充为插入修改时间
                .addTableFills(
                        new Column("is_deleted", FieldFill.INSERT),
                        new Column("create_time", FieldFill.INSERT),
                        new Column("update_time", FieldFill.INSERT_UPDATE)
                )
                // 开启生成实体时生成字段注解
                .enableTableFieldAnnotation()

                .superClass("com.runjian.rundo.commons.base.BaseDomain")

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
                // 格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                .formatServiceFileName("%sService")
                // 格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                .formatServiceImplFileName("%sServiceImpl")
                .superServiceClass("com.runjian.rundo.commons.base.IBaseService")
                .superServiceImplClass("com.runjian.rundo.commons.base.BaseServiceImpl")

                // Controller 策略配置
                .controllerBuilder()
                // 格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                .formatFileName("%sController")
                .superClass("com.runjian.rundo.commons.base.BaseController")
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


        // InjectionConfig config = new InjectionConfig() {
        //     @Override
        //     public void initMap() {
        //         // to do nothing
        //     }
        // };
        //
        // // 自定义输出 mapper.xml 到 resources 目录下
        // String mapperPath = "/templates/mapper.xml.ftl";
        // List<FileOutConfig> focList = new ArrayList<>();
        //
        // // 自定义配置会被优先输出
        // focList.add(new FileOutConfig(mapperPath) {
        //     @Override
        //     public String outputFile(TableInfo tableInfo) {
        //         // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        //         return USER_DIR + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
        //                 + StringPool.DOT_XML;
        //     }
        // });
        //
        // config.setFileOutConfigList(focList);
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
