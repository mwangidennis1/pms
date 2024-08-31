package org.mwangi.maya.repositories;

import org.mwangi.maya.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    @Query(
            """
SELECT e FROM Employee e WHERE lower(e.username)=lower(:name) 
"""
    )
    public Employee getEmployeeIgoreCase(@Param("name") String name);
    @Query("""
 SELECT  e FROM  Employee e WHERE  lower(e.username)=lower( ?1)
""")
    Employee findEmployeeByEmail(String name);
    @Query("""
   SELECT e FROM Employee e WHERE e.email=?1
""")
    Employee findByEmail(String email);
    @Query("""
 SELECT  e FROM  Employee  e WHERE  e.resetPasswordToken=?1
""")
    Employee findByResetPasswordToken(String token);
}
