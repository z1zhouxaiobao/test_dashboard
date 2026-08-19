package com.liquicool.config;

import com.liquicool.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 空库（无产品数据）启动时自动导入全部演示数据，解决公网部署后库空无法登录/无内容。
 */
@Component
public class DemoDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/seed-demo.sql"));
            log.info("空库已自动导入全部演示数据，账号: admin/123456 、 user1/123456");
        } catch (Exception e) {
            log.error("自动导入演示数据失败", e);
        }
    }
}
