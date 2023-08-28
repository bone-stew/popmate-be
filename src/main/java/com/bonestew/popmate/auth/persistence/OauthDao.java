package com.bonestew.popmate.auth.persistence;

import com.bonestew.popmate.auth.domain.OauthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OauthDao {

    // 회원이 있는지 없는지 확인하는 곳
    OauthUser findCheck(@Param("email") String email);

}
