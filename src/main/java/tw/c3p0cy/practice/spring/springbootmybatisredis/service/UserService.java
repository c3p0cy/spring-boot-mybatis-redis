package tw.c3p0cy.practice.spring.springbootmybatisredis.service;

import java.util.List;
import java.util.Optional;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;

public interface UserService {
  List<User> findAll();
  Optional<User> findBySid(Integer sid);
  Optional<User> findById(String id);
  Integer create(User user);
  Integer update(User user);
  Integer disable(Integer sid);
}
