package tw.c3p0cy.practice.spring.springbootmybatisredis.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;
import tw.c3p0cy.practice.spring.springbootmybatisredis.service.UserService;

@RestController
@RequestMapping("/user")
public class Controller {

  @Autowired
  private UserService service;

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public List<User> findAll() {
    return service.findAll();
  }

  @RequestMapping(value = "/findBySid/{sid}", method = RequestMethod.GET)
  public User findBySid(@PathVariable("sid") Integer sid) {
    return service.findBySid(sid).orElse(null);
  }

  @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
  public User findById(@PathVariable("id") String id) {
    return service.findById(id).orElse(null);
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public Integer create(@RequestBody User user) {
    return service.create(user);
  }

  @RequestMapping(value = "/update", method = RequestMethod.PUT)
  public Integer update(@RequestBody User user) {
    return service.update(user);
  }

  @RequestMapping(value = "/delete/{sid}", method = RequestMethod.DELETE)
  public Integer disable(@PathVariable("sid") Integer sid) {
    return service.disable(sid);
  }
}
