package com.hanmj.service.impl;

import com.hanmj.bean.Hr;
import com.hanmj.dao.HrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HrServiceImpl implements UserDetailsService {

    @Autowired
    HrDao hrDao;

    /**
     * 登录时获取用户名称
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //System.out.println("4、查询账户信息");
        Hr hr = hrDao.loadUserByUsername(s);
        if(hr==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        return hr;
    }
}
