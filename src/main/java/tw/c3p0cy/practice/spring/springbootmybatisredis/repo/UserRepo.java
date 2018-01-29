package tw.c3p0cy.practice.spring.springbootmybatisredis.repo;


import java.util.List;
import org.springframework.stereotype.Repository;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;

@Repository
public interface UserRepo {
  List<User> findAll();
  User findBySid(Integer sid);
  User findById(String id);
  Integer create(User user);
  Integer update(User user);
  Integer disable(Integer sid);
}
