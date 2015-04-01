package in.clouthink.daas.security.token.sample.cust;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 */
public interface SampleUserRepository extends
                                     PagingAndSortingRepository<SampleUser, String> {
    
    public SampleUser findByUsername(String username);

}
