package org.taxiservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.taxiservice.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);

    List<Authority> findAll();

}
