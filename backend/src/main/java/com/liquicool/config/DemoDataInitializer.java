package com.liquicool.config;

import com.liquicool.repository.NavMenuRepository;
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
 * 空库导入演示数据；已有业务数据但缺导航时，补种导航菜单。
 */
@Component
public class DemoDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NavMenuRepository navMenuRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() == 0) {
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/seed-demo.sql"));
                log.info("空库已自动导入全部演示数据，账号: admin/123456 、 user1/123456");
            } catch (Exception e) {
                log.error("自动导入演示数据失败", e);
            }
            return;
        }
        if (navMenuRepository.count() == 0) {
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/seed-nav-menu.sql"));
                log.info("已补种门户导航菜单（一/二/三级）");
            } catch (Exception e) {
                log.error("补种导航菜单失败", e);
            }
        }
    }
}
