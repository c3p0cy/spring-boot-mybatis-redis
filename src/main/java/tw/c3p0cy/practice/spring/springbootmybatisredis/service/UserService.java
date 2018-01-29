package tw.c3p0cy.practice.spring.springbootmybatisredis.service;

import java.util.List;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;

public interface UserService {
  List<User> findAll();
  User findBySid(Integer sid);
  User findById(String id);
  Integer create(User user);
  Integer update(User user);
  Integer disable(Integer sid);
}
