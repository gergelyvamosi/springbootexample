package org.gvamosi.springbootexample.spring.data.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableCassandraRepositories(basePackages = { "org.gvamosi.springbootexample.spring.data.cassandra.repository" })
public class CassandraConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public CassandraMappingContext mappingContext() {
        return new CassandraMappingContext();
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    @Bean
    public CqlSessionFactoryBean session() throws Exception {

        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(env.getProperty("spring.cassandra.contact-points"));
        session.setPort(Integer.parseInt(env.getProperty("spring.cassandra.port")));
        session.setKeyspaceName(env.getProperty("spring.cassandra.keyspace-name"));
        session.setLocalDatacenter(env.getProperty("spring.cassandra.local-datacenter"));

        return session;
    }

    @Bean(name = "cassandraTemplate")
    public CassandraOperations cassandraTemplate() throws Exception {
        LOG.info("Returning cassandraTemplate");
        return new CassandraTemplate(session().getObject());
    }
}
