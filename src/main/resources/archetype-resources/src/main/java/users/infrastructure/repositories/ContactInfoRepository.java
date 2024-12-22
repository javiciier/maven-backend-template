package $package.users.infrastructure.repositories;

import $package.users.domain.ContactInfo;
import org.springframework.data.repository.CrudRepository;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Long> {
}
