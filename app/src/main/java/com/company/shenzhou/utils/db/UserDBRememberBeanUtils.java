package com.company.shenzhou.utils.db;

import android.util.Log;

import com.company.shenzhou.base.App;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.player.db.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * LoveLin
 * <p>
 * Describe 用户表的CURD操作工具类
 */
public class UserDBRememberBeanUtils {
//
//    insert： 会进行去重，保存第一次的数据，也就是不会进行更新。至于是 由于主键去重，还是有重复的元素就去我还会在看看
//
//    insertOrReplace：  会去重，保存最新的数据，也就是会进行更新
//
//    save：  不会去重，保存所有数据

    /**
     * 增  --插入数据
     *
     * @param bean
     */
    public static void insertData(UserDBRememberBean bean) {
        App.getInstance().getDaoSession().insert(bean);
    }

    /**
     * 增  --数据存在则替换，数据不存在则插入
     *
     * @param bean
     */
    public static void insertOrReplaceData(UserDBRememberBean bean) {
        App.getInstance().getDaoSession().insertOrReplace(bean);
    }


    /**
     * 删--delete()和deleteAll()；分别表示删除单个和删除所有。
     */

    public static void deleteData(UserDBRememberBean bean) {
        App.getInstance().getDaoSession().delete(bean);

    }

    public static void deleteAllData(Class clazz) {
        App.getInstance().getDaoSession().deleteAll(clazz);

    }


    /**
     * 改--通过update来进行修改：
     */


    public static void updateData(UserDBRememberBean bean) {
        App.getInstance().getDaoSession().update(bean);

    }


    /**
     * 查询
     * loadAll()：查询所有数据。
     * queryRaw()：根据条件查询
     * queryBuilder() : 方便查询的创建，后面详细讲解。
     */


    public static List queryAll(Class clazz) {

        List list = App.getInstance().getDaoSession().loadAll(clazz);
        return list;
    }

    public static List queryRaw(UserDBRememberBean bean, Long id) {

        List<UserDBRememberBean> beanLis = (List<UserDBRememberBean>)
                App.getInstance().getDaoSession().queryRaw(UserDBRememberBean.class, " where id = ?", id + "");
        return beanLis;
    }

    /**
     * 根据用户名条件查询,返回password
     *
     * @param name
     * @return
     */
    public static UserDBRememberBean queryListByMessageToGetPassword(String name) {
        boolean isExist = queryListIsExist(name);
        if (isExist) {
            List<UserDBRememberBean> UserDBRememberBeanList = queryListByMessage(name);
            Log.e("path=====:=====", UserDBRememberBeanList.size() + ""); //   /storage/emulated/0/1604026573438.mp4
            for (int i = 0; i < UserDBRememberBeanList.size(); i++) {

                return UserDBRememberBeanList.get(0);

            }
        } else {
            return new UserDBRememberBean();
        }
        return new UserDBRememberBean();

    }

    /**
     * 根据单个条件查询
     *
     * @param name
     * @return
     */
    public static List<UserDBRememberBean> queryListByMessage(String name) {
        DaoSession daoSession = App.getInstance().getDaoSession();
        QueryBuilder<UserDBRememberBean> qb = daoSession.queryBuilder(UserDBRememberBean.class);
        List<UserDBRememberBean> students = daoSession.queryRaw(UserDBRememberBean.class, " where username = ?", name);
        return students;
    }
    public static UserDBRememberBean queryListByName(String name) {
        DaoSession daoSession = App.getInstance().getDaoSession();
        QueryBuilder<UserDBRememberBean> qb = daoSession.queryBuilder(UserDBRememberBean.class);
        List<UserDBRememberBean> students = daoSession.queryRaw(UserDBRememberBean.class, " where username = ?", name);
        UserDBRememberBean userDBRememberBean = students.get(0);
        return userDBRememberBean;
    }

    /**
     * @param name
     * @return 查询是否存在
     */
    public static boolean queryListIsExist(String name) {
        DaoSession daoSession = App.getInstance().getDaoSession();
        QueryBuilder<UserDBRememberBean> qb = daoSession.queryBuilder(UserDBRememberBean.class);
        List<UserDBRememberBean> students = daoSession.queryRaw(UserDBRememberBean.class, " where username = ?", name);
//        List<Student> students = daoSession.loadAll(Student.class);
//        return students;
        Log.e("path=====Start:=====", students.size() + ""); //   /storage/emulated/0/1604026573438.mp4

        if (students.size() != 0) {  //存在
            return true;
        } else {
            return false;
        }
    }


}
