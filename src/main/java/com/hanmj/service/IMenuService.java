package com.hanmj.service;

import com.hanmj.bean.Menu;

import java.util.List;


public interface IMenuService {
    public List<Menu> getAllMenu();

    public List<Menu> getMenusByHrId() ;

    public List<Menu> menuTree() ;

    public List<Long> getMenusByRid(Long rid) ;
}
