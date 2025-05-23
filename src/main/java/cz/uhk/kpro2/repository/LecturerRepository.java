package cz.uhk.kpro2.repository;

import cz.uhk.kpro2.model.Lecturer;
import cz.uhk.kpro2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

}
