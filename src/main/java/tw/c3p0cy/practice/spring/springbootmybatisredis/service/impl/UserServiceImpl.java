package tw.c3p0cy.practice.spring.springbootmybatisredis.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;
import tw.c3p0cy.practice.spring.springbootmybatisredis.repo.UserRepo;
import tw.c3p0cy.practice.spring.springbootmybatisredis.service.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepo repo;

  @Override
  public List<User> findAll() {
    return repo.findAll();
  }

  @Override
  public User findBySid(Integer sid) {
    return repo.findBySid(sid);
  }

  @Override
  public User findById(String id) {
    return repo.findById(id);
  }

  @Override
  public Integer create(User user) {
    return repo.create(user);
  }

  @Override
  public Integer update(User user) {
    return repo.update(user);
  }

  @Override
  public Integer disable(Integer sid) {
    return repo.disable(sid);
  }
}
