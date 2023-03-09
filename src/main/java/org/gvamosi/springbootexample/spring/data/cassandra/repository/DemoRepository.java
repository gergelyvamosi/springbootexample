package org.gvamosi.springbootexample.spring.data.cassandra.repository;

import java.util.List;
import java.util.UUID;

import org.gvamosi.springbootexample.spring.data.cassandra.model.Demo;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DemoRepository extends CassandraRepository<Demo, UUID> {

    @AllowFiltering
    List<Demo> findByPublished(boolean published);

    List<Demo> findByTitleContaining(String title);
}