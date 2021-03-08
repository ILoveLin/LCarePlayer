package com.company.shenzhou.utils.db;

import android.util.Log;
import android.widget.Toast;

import com.company.shenzhou.base.App;
import com.company.shenzhou.bean.dbbean.UserDBBean;
import com.company.shenzhou.player.db.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * LoveLin
 * <p>
 * Describe 用户表的CURD操作工具类
 */
public class UserDBUtils {
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
    public static void insertData(UserDBBean bean) {
        App.getInstance().getDaoSession().insert(bean);
    }

    /**
     * 增  --数据存在则替换，数据不存在则插入
     *
     * @param bean
     */
    public static void insertOrReplaceData(UserDBBean bean) {
        App.getInstance().getDaoSession().insertOrReplace(bean);
    }


    /**
     * 删--delete()和deleteAll()；分别表示删除单个和删除所有。
     */

    public static void deleteData(UserDBBean bean) {
        App.getInstance().getDaoSession().delete(bean);

    }

    public static void deleteAllData(Class clazz) {
        App.getInstance().getDaoSession().deleteAll(clazz);

    }


    /**
     * 改--通过update来进行修改：
     */


    public static void updateData(UserDBBean bean) {
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

    public static List queryRaw(UserDBBean bean, Long id) {

        List<UserDBBean> beanLis = (List<UserDBBean>)
                App.getInstance().getDaoSession().queryRaw(UserDBBean.class, " where id = ?", id + "");
        return beanLis;
    }

    /**
     * 根据用户名条件查询,返回password
     *
     * @param name
     * @return
     */
    public static UserDBBean queryListByMessageToGetPassword(String name) {
        boolean isExist = queryListIsExist(name);
        if (isExist) {
            List<UserDBBean> userDBBeanList = queryListByMessage(name);
            Log.e("path=====:=====", userDBBeanList.size() + ""); //   /storage/emulated/0/1604026573438.mp4
            for (int i = 0; i < userDBBeanList.size(); i++) {

                return userDBBeanList.get(0);

            }
        } else {
            return new UserDBBean();
        }
        return new UserDBBean();

    }

    /**
     * 根据单个条件查询
     *
     * @param name
     * @return
     */
    public static List<UserDBBean> queryListByMessage(String name) {
        DaoSession daoSession = App.getInstance().getDaoSession();
        QueryBuilder<UserDBBean> qb = daoSession.queryBuilder(UserDBBean.class);
        List<UserDBBean> students = daoSession.queryRaw(UserDBBean.class, " where username = ?", name);
        return students;
    }

    /**
     * @param name
     * @return 查询是否存在
     */
    public static boolean queryListIsExist(String name) {
        DaoSession daoSession = App.getInstance().getDaoSession();
        QueryBuilder<UserDBBean> qb = daoSession.queryBuilder(UserDBBean.class);
        List<UserDBBean> students = daoSession.queryRaw(UserDBBean.class, " where username = ?", name);
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
