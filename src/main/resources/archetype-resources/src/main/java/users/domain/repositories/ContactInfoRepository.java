package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.ContactInfo;
import ${package}.users.domain.repositories.ContactInfoRepository;

import org.springframework.data.repository.CrudRepository;


public interface ContactInfoRepository extends CrudRepository<ContactInfo, Long> {
}