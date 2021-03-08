package com.company.shenzhou.player.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.company.shenzhou.bean.dbbean.VideoDBBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "VIDEO_DBBEAN".
*/
public class VideoDBBeanDao extends AbstractDao<VideoDBBean, Long> {

    public static final String TABLENAME = "VIDEO_DBBEAN";

    /**
     * Properties of entity VideoDBBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Account = new Property(1, String.class, "account", false, "ACCOUNT");
        public final static Property Password = new Property(2, String.class, "password", false, "PASSWORD");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Ip = new Property(4, String.class, "ip", false, "IP");
        public final static Property MakeMessage = new Property(5, String.class, "makeMessage", false, "MAKE_MESSAGE");
        public final static Property Port = new Property(6, String.class, "port", false, "PORT");
        public final static Property Type = new Property(7, String.class, "type", false, "TYPE");
        public final static Property Tag = new Property(8, String.class, "tag", false, "TAG");
    }


    public VideoDBBeanDao(DaoConfig config) {
        super(config);
    }
    
    public VideoDBBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"VIDEO_DBBEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACCOUNT\" TEXT," + // 1: account
                "\"PASSWORD\" TEXT," + // 2: password
                "\"TITLE\" TEXT," + // 3: title
                "\"IP\" TEXT," + // 4: ip
                "\"MAKE_MESSAGE\" TEXT," + // 5: makeMessage
                "\"PORT\" TEXT," + // 6: port
                "\"TYPE\" TEXT," + // 7: type
                "\"TAG\" TEXT);"); // 8: tag
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"VIDEO_DBBEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, VideoDBBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(5, ip);
        }
 
        String makeMessage = entity.getMakeMessage();
        if (makeMessage != null) {
            stmt.bindString(6, makeMessage);
        }
 
        String port = entity.getPort();
        if (port != null) {
            stmt.bindString(7, port);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(9, tag);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, VideoDBBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(5, ip);
        }
 
        String makeMessage = entity.getMakeMessage();
        if (makeMessage != null) {
            stmt.bindString(6, makeMessage);
        }
 
        String port = entity.getPort();
        if (port != null) {
            stmt.bindString(7, port);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(9, tag);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public VideoDBBean readEntity(Cursor cursor, int offset) {
        VideoDBBean entity = new VideoDBBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // account
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // password
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ip
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // makeMessage
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // port
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // type
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // tag
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, VideoDBBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccount(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPassword(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIp(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMakeMessage(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPort(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTag(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(VideoDBBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(VideoDBBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(VideoDBBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
