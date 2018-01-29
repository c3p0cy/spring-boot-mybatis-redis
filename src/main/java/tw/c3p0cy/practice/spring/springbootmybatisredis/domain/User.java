package tw.c3p0cy.practice.spring.springbootmybatisredis.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User implements Serializable {

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private static final long serialVersionUID = -1L;

  private Integer sid;
  private Boolean enabled;
  private Date created;
  private Integer createdBy;
  private Date lastModified;
  private Integer lastModifiedBy;
  private String id;
  private String name;
  private Integer primaryOrg;
  private String customSettings;
  private String email;

}
