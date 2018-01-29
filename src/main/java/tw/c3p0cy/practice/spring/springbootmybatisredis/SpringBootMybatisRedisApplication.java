package tw.c3p0cy.practice.spring.springbootmybatisredis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("tw.c3p0cy.practice.spring.springbootmybatisredis.repo")
public class SpringBootMybatisRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMybatisRedisApplication.class, args);
	}
}
