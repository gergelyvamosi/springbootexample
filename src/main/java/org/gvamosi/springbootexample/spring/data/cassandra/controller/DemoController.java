package org.gvamosi.springbootexample.spring.data.cassandra.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gvamosi.springbootexample.spring.data.cassandra.model.Demo;
import org.gvamosi.springbootexample.spring.data.cassandra.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;

//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:4200", "http://localhost:8081"})
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class DemoController {

  @Autowired
  CassandraOperations cassandraOperations;

  @Autowired
  DemoRepository demoRepository;

  @GetMapping("/demos")
  public ResponseEntity<List<Demo>> getAlldemos(@RequestParam(required = false) String title) {
    try {
      List<Demo> demos = new ArrayList<Demo>();

      if (title == null)
        demoRepository.findAll().forEach(demos::add);
      else
        demoRepository.findByTitleContaining(title).forEach(demos::add);

      if (demos.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(demos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/demos/{id}")
  public ResponseEntity<Demo> getTutorialById(@PathVariable("id") UUID id) {
    Optional<Demo> tutorialData = demoRepository.findById(id);

    if (tutorialData.isPresent()) {
      return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/demos")
  public ResponseEntity<Demo> createTutorial(@RequestBody Demo tutorial) {
    try {
      Demo _tutorial = demoRepository
          .save(new Demo(UUIDs.timeBased(), tutorial.getTitle(), tutorial.getDescription(), false));
      return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/demos/{id}")
  public ResponseEntity<Demo> updateTutorial(@PathVariable("id") UUID id, @RequestBody Demo tutorial) {
    Optional<Demo> tutorialData = demoRepository.findById(id);

    if (tutorialData.isPresent()) {
      Demo _tutorial = tutorialData.get();
      _tutorial.setTitle(tutorial.getTitle());
      _tutorial.setDescription(tutorial.getDescription());
      _tutorial.setPublished(tutorial.isPublished());
      return new ResponseEntity<>(demoRepository.save(_tutorial), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/demos/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") UUID id) {
    try {
      demoRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/demos")
  public ResponseEntity<HttpStatus> deleteAlldemos() {
    try {
      demoRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/demos/published")
  public ResponseEntity<List<Demo>> findByPublished() {
    try {
      List<Demo> demos = demoRepository.findByPublished(true);

      if (demos.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(demos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}