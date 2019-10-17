package com.hanmj.dao;

import com.hanmj.bean.Hr;
import com.hanmj.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Mapper
public interface HrDao {
    Hr loadUserByUsername(String username);

    List<Role> getRolesByHrId(Long id);

    int hrReg(@Param("username") String username, @Param("password") String password);

    List<Hr> getHrsByKeywords(@Param("keywords") String keywords);

    int updateHr(Hr hr);

    int deleteRoleByHrId(Long hrId);

    int addRolesForHr(@Param("hrId") Long hrId, @Param("rids") Long[] rids);

    Hr getHrById(Long hrId);

    int deleteHr(Long hrId);

    List<Hr> getAllHr(@Param("currentId") Long currentId);
}
