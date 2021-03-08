package com.company.shenzhou.utils.db;

import com.company.shenzhou.base.App;
import com.company.shenzhou.bean.dbbean.VideoDBBean01;

import java.util.List;

/**
 * LoveLin
 * <p>
 * Describe 设备视频链接记录表--的CURD操作工具类
 */
public class VideoDB01Utils {
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
    public static void insertData(VideoDBBean01 bean) {
        App.getInstance().getDaoSession().insert(bean);
    }

    /**
     * 增  --数据存在则替换，数据不存在则插入
     *
     * @param bean
     */
    public static void insertOrReplaceData(VideoDBBean01 bean) {
        App.getInstance().getDaoSession().insertOrReplace(bean);
    }


    /**
     * 删--delete()和deleteAll()；分别表示删除单个和删除所有。
     */

    public static void deleteData(VideoDBBean01 bean) {
        App.getInstance().getDaoSession().delete(bean);

    }

    public static void deleteAllData(Class clazz) {
        App.getInstance().getDaoSession().deleteAll(clazz);

    }


    /**
     * 改--通过update来进行修改：
     * update之前必须先设置他本身之前的id不然crash
     */


    public static void updateData(VideoDBBean01 bean) {
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

    public static List queryRaw(Long id) {

        List<VideoDBBean01> beanLis = (List<VideoDBBean01>)
                App.getInstance().getDaoSession().queryRaw(VideoDBBean01.class, " where id = ?", id + "");
        return beanLis;
    }
    public static List queryRawTag(String tag) {

        List<VideoDBBean01> beanLis = (List<VideoDBBean01>)
                App.getInstance().getDaoSession().queryRaw(VideoDBBean01.class, " where tag = ?", tag + "");
        return beanLis;
    }


}
