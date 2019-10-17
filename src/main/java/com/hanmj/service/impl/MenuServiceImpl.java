package com.hanmj.service.impl;

import com.hanmj.bean.Menu;
import com.hanmj.common.UserUtils;
import com.hanmj.dao.MenuDao;
import com.hanmj.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "menus_cache")
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private MenuDao menuDao;


    @Cacheable(key = "#root.methodName")
    @Override
    public List<Menu> getAllMenu() {
        return menuDao.getAllMenu();
    }

    @Override
    public List<Menu> getMenusByHrId() {
        return this.menuDao.getMenusByHrId(UserUtils.getcurrentUser().getId());
    }

    @Override
    public List<Menu> menuTree() {
        return menuDao.menuTree();
    }

    @Override
    public List<Long> getMenusByRid(Long rid) {
        return this.menuDao.getMenusByRid(rid);
    }
}
