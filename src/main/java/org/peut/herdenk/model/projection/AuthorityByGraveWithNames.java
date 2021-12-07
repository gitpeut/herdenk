package org.peut.herdenk.model.projection;

public interface AuthorityByGraveWithNames {
      Long   getGraveId();
      String getOccupantFullName();
      Long   getUserId();
      String getUserFullName();
      String getAccess();
}
