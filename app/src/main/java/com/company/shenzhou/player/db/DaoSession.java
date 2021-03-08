package com.company.shenzhou.player.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.company.shenzhou.bean.dbbean.UserDBBean;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean01;

import com.company.shenzhou.player.db.UserDBBeanDao;
import com.company.shenzhou.player.db.UserDBRememberBeanDao;
import com.company.shenzhou.player.db.VideoDBBeanDao;
import com.company.shenzhou.player.db.VideoDBBean01Dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDBBeanDaoConfig;
    private final DaoConfig userDBRememberBeanDaoConfig;
    private final DaoConfig videoDBBeanDaoConfig;
    private final DaoConfig videoDBBean01DaoConfig;

    private final UserDBBeanDao userDBBeanDao;
    private final UserDBRememberBeanDao userDBRememberBeanDao;
    private final VideoDBBeanDao videoDBBeanDao;
    private final VideoDBBean01Dao videoDBBean01Dao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDBBeanDaoConfig = daoConfigMap.get(UserDBBeanDao.class).clone();
        userDBBeanDaoConfig.initIdentityScope(type);

        userDBRememberBeanDaoConfig = daoConfigMap.get(UserDBRememberBeanDao.class).clone();
        userDBRememberBeanDaoConfig.initIdentityScope(type);

        videoDBBeanDaoConfig = daoConfigMap.get(VideoDBBeanDao.class).clone();
        videoDBBeanDaoConfig.initIdentityScope(type);

        videoDBBean01DaoConfig = daoConfigMap.get(VideoDBBean01Dao.class).clone();
        videoDBBean01DaoConfig.initIdentityScope(type);

        userDBBeanDao = new UserDBBeanDao(userDBBeanDaoConfig, this);
        userDBRememberBeanDao = new UserDBRememberBeanDao(userDBRememberBeanDaoConfig, this);
        videoDBBeanDao = new VideoDBBeanDao(videoDBBeanDaoConfig, this);
        videoDBBean01Dao = new VideoDBBean01Dao(videoDBBean01DaoConfig, this);

        registerDao(UserDBBean.class, userDBBeanDao);
        registerDao(UserDBRememberBean.class, userDBRememberBeanDao);
        registerDao(VideoDBBean.class, videoDBBeanDao);
        registerDao(VideoDBBean01.class, videoDBBean01Dao);
    }
    
    public void clear() {
        userDBBeanDaoConfig.clearIdentityScope();
        userDBRememberBeanDaoConfig.clearIdentityScope();
        videoDBBeanDaoConfig.clearIdentityScope();
        videoDBBean01DaoConfig.clearIdentityScope();
    }

    public UserDBBeanDao getUserDBBeanDao() {
        return userDBBeanDao;
    }

    public UserDBRememberBeanDao getUserDBRememberBeanDao() {
        return userDBRememberBeanDao;
    }

    public VideoDBBeanDao getVideoDBBeanDao() {
        return videoDBBeanDao;
    }

    public VideoDBBean01Dao getVideoDBBean01Dao() {
        return videoDBBean01Dao;
    }

}