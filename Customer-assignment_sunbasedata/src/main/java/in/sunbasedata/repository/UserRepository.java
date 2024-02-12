package in.sunbasedata.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sunbasedata.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUsername(String username);
}

