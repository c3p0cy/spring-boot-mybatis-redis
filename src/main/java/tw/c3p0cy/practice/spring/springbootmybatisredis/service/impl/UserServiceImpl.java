package tw.c3p0cy.practice.spring.springbootmybatisredis.service.impl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import tw.c3p0cy.common.util.RuntimeUtils;
import tw.c3p0cy.practice.spring.springbootmybatisredis.domain.User;
import tw.c3p0cy.practice.spring.springbootmybatisredis.repo.UserRepo;
import tw.c3p0cy.practice.spring.springbootmybatisredis.service.UserService;

/**
 * Cache Strategy: Cache Aside Pattern
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private static final String KEY_PREFIX = User.class.getName() + "_";
  private enum KEYS { SID, ID, ALL};

  @Autowired
  private UserRepo repo;

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public List<User> findAll() {
    String key = KEY_PREFIX + KEYS.ALL;
    ValueOperations<String, List<User>> operations;

    if (redisTemplate.hasKey(key)) {
      operations = redisTemplate.opsForValue();
      List<User> found = operations.get(key);
      log.debug(String.format("%s: Hits cache by key=%s >> %s", RuntimeUtils.getMethodName(), key,
          Arrays.toString(found.toArray())));
      return found;
    }

    List<User> found = repo.findAll();
    if (found != null) {
      operations = redisTemplate.opsForValue();
      operations.set(key, found, 30, TimeUnit.SECONDS);
      log.debug(String.format("%s: Query from db >> %s >> add to cache %s", RuntimeUtils.getMethodName(), found, key));
    }
    return found;
  }

  @Override
  public Optional<User> findBySid(Integer sid) {
    // Get user from cache
    String key = KEY_PREFIX + KEYS.SID + "_" + sid;
    ValueOperations<String, User> operations;

    // Hits cache
    if (redisTemplate.hasKey(key)) {
      operations = redisTemplate.opsForValue();
      User found = operations.get(key);
      log.debug(String.format("%s: Hits cache by key=%s >> %s", RuntimeUtils.getMethodName(), key, found));
      return Optional.of(found);
    }

    // Query from db
    User found = repo.findBySid(sid);
    if (found == null) {
      return Optional.empty();
    }

    // Add to cache
    operations = redisTemplate.opsForValue();
    operations.set(key, found, 30, TimeUnit.SECONDS);
    log.debug(String.format("%s: Query from db by sid=%s >> %s >> add to cache %s", RuntimeUtils.getMethodName(), sid, found, key));
    return Optional.of(found);
  }

  @Override
  public Optional<User> findById(String id) {
    // Get user from cache
    String key = KEY_PREFIX + KEYS.ID + "_" + id;
    ValueOperations<String, User> operations;

    // Hits cache
    if (redisTemplate.hasKey(key)) {
      operations = redisTemplate.opsForValue();
      User found = operations.get(key);
      log.debug(String.format("%s: Hits cache by key=%s >> %s", RuntimeUtils.getMethodName(), key, found));
      return Optional.of(found);
    }

    // Query from db
    User found = repo.findById(id);
    if (found == null) {
      return Optional.empty();
    }

    // Add to cache
    operations = redisTemplate.opsForValue();
    operations.set(key, found, 30, TimeUnit.SECONDS);
    log.debug(String.format("%s: Query from db by id=%s >> %s >> add to cache %s", RuntimeUtils.getMethodName(), id, found, key));
    return Optional.of(found);
  }

  @Override
  public Integer create(User user) {
    return repo.create(user);
  }

  @Override
  public Integer update(User user) {
    Integer result =  repo.update(user);
    if (result.equals(1)) {
      deleteCached(user);
    }
    return result;
  }

  @Override
  public Integer disable(Integer sid) {
    Integer result =  repo.disable(sid);
    if (result.equals(1)) {
      deleteCached(repo.findBySid(sid));
    }
    return result;
  }

  private void deleteCached(User user) {
    List<String> keys = cachedKeys(user);
    redisTemplate.delete(keys);
    log.debug(String.format("%s: Delete cache by key = %s", RuntimeUtils.getMethodName(3), Arrays.toString(keys.toArray())));
  }

  private List<String> cachedKeys(User user) {
    return Stream.of(KEYS.values())
        .map(e -> concatKey(e, user))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }

  private String concatKey(KEYS key, User user) {
    switch (key) {
      case ID:
        return KEY_PREFIX + key + "_" + user.getId();
      case SID:
        return KEY_PREFIX + key + "_" + user.getSid();
      case ALL:
        return KEY_PREFIX + key;
      default:
        throw new UnsupportedOperationException("Undefined key for KEYS." + key);
    }
  }
}
