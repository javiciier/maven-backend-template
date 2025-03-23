package ${package}.users.domain.repositories;

import ${package}.users.domain.entities.ContactInfo;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;

@Lazy
public interface ContactInfoRepository extends CrudRepository<ContactInfo, Long> {

}