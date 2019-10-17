package com.hanmj.dao;

import com.hanmj.bean.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Repository需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中。
 *
 * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中。
 */
@Mapper
public interface MenuDao {
    List<Menu> getAllMenu();

    List<Menu> getMenusByHrId(Long hrId);

    List<Menu> menuTree();

    List<Long> getMenusByRid(Long rid);
}
