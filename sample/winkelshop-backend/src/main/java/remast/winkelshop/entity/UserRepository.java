package remast.winkelshop.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends Repository<UserRecord, String> {

    @Query("SELECT * FROM users WHERE lower(email) = lower(:email)")
    Optional<UserRecord> findByEmail(@Param("email") String email);

    Optional<UserRecord> findById(String id);
}
