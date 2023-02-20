package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByAddress_Profile_Username(String username);
}
