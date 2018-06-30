package in.clouthink.daas.security.token.sample.spi.impl;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SampleUserRepository extends PagingAndSortingRepository<SampleUser, String> {

    SampleUser findByUsername(String username);

}
