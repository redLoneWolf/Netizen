package com.sudhar.netizen.RoomDB.Daos;

import android.database.Cursor;
import androidx.collection.ArrayMap;
import androidx.paging.DataSource;
import androidx.paging.PagingSource;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetDataSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sudhar.netizen.RoomDB.DateTimeConverter;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.Entities.ImageModelEntity;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.RoomDB.Entities.TemplateEntity;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MainDao_Impl implements MainDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CommentEntity> __insertionAdapterOfCommentEntity;

  private final DateTimeConverter __dateTimeConverter = new DateTimeConverter();

  private final EntityInsertionAdapter<ImageModelEntity> __insertionAdapterOfImageModelEntity;

  private final EntityInsertionAdapter<MemeEntity> __insertionAdapterOfMemeEntity;

  private final EntityInsertionAdapter<TemplateEntity> __insertionAdapterOfTemplateEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteImages;

  private final SharedSQLiteStatement __preparedStmtOfDeleteComments;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMemes;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLike;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookmark;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFollow;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTemplates;

  public MainDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommentEntity = new EntityInsertionAdapter<CommentEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `comment_table` (`id`,`username`,`parentId`,`contentType`,`objectId`,`userId`,`body`,`created`,`identifier`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CommentEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUsername());
        }
        if (value.getParentId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getParentId());
        }
        if (value.getContentType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getContentType());
        }
        if (value.getObjectId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getObjectId());
        }
        stmt.bindLong(6, value.getUserId());
        if (value.getBody() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getBody());
        }
        final String _tmp;
        _tmp = __dateTimeConverter.fromOffsetDateTime(value.getCreated());
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, _tmp);
        }
        if (value.getIdentifier() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getIdentifier());
        }
      }
    };
    this.__insertionAdapterOfImageModelEntity = new EntityInsertionAdapter<ImageModelEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `images_table` (`id`,`objectId`,`contentType`,`imageUrl`,`identifier`,`created`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImageModelEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getObjectId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getObjectId());
        }
        if (value.getContentType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getContentType());
        }
        if (value.getImageUrl() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageUrl());
        }
        if (value.getIdentifier() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getIdentifier());
        }
        final String _tmp;
        _tmp = __dateTimeConverter.fromOffsetDateTime(value.getCreated());
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp);
        }
      }
    };
    this.__insertionAdapterOfMemeEntity = new EntityInsertionAdapter<MemeEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `meme_table` (`id`,`description`,`username`,`created`,`contentType`,`is_following`,`is_liked`,`likes`,`is_bookmarked`,`userId`,`identifier`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MemeEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescription());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        final String _tmp;
        _tmp = __dateTimeConverter.fromOffsetDateTime(value.getCreated());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        if (value.getContentType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getContentType());
        }
        final int _tmp_1;
        _tmp_1 = value.is_following() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.is_liked() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        stmt.bindLong(8, value.getLikes());
        final int _tmp_3;
        _tmp_3 = value.is_bookmarked() ? 1 : 0;
        stmt.bindLong(9, _tmp_3);
        stmt.bindLong(10, value.getUserId());
        if (value.getIdentifier() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getIdentifier());
        }
      }
    };
    this.__insertionAdapterOfTemplateEntity = new EntityInsertionAdapter<TemplateEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `template_table` (`id`,`description`,`username`,`created`,`contentType`,`is_following`,`is_liked`,`likes`,`is_bookmarked`,`userId`,`identifier`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemplateEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescription());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        final String _tmp;
        _tmp = __dateTimeConverter.fromOffsetDateTime(value.getCreated());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        if (value.getContentType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getContentType());
        }
        final int _tmp_1;
        _tmp_1 = value.is_following() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.is_liked() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        stmt.bindLong(8, value.getLikes());
        final int _tmp_3;
        _tmp_3 = value.is_bookmarked() ? 1 : 0;
        stmt.bindLong(9, _tmp_3);
        stmt.bindLong(10, value.getUserId());
        if (value.getIdentifier() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getIdentifier());
        }
      }
    };
    this.__preparedStmtOfDeleteImages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM images_table";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteComments = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM comment_table";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMemes = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM meme_table";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLike = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE meme_table SET is_liked=?,likes =? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBookmark = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE meme_table SET is_bookmarked=? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFollow = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE meme_table SET is_following=? WHERE username=?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTemplates = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM template_table";
        return _query;
      }
    };
  }

  @Override
  public void insertComment(final CommentEntity memeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentEntity.insert(memeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertImage(final ImageModelEntity memeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfImageModelEntity.insert(memeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertImages(final List<ImageModelEntity> imageModelEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfImageModelEntity.insert(imageModelEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertComments(final List<CommentEntity> commentEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentEntity.insert(commentEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMeme(final MemeEntity memeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMemeEntity.insert(memeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMemes(final List<MemeEntity> memeEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMemeEntity.insert(memeEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertTemplate(final TemplateEntity templateEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTemplateEntity.insert(templateEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertTemplates(final List<TemplateEntity> templateEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTemplateEntity.insert(templateEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteImages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteImages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteImages.release(_stmt);
    }
  }

  @Override
  public void deleteComments() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteComments.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteComments.release(_stmt);
    }
  }

  @Override
  public void deleteMemes() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMemes.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMemes.release(_stmt);
    }
  }

  @Override
  public void updateLike(final boolean is_liked, final int likes, final String id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLike.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = is_liked ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, likes);
    _argIndex = 3;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateLike.release(_stmt);
    }
  }

  @Override
  public void updateBookmark(final boolean is_bookmarked, final String id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookmark.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = is_bookmarked ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBookmark.release(_stmt);
    }
  }

  @Override
  public void updateFollow(final boolean is_following, final String username) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFollow.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = is_following ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (username == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, username);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateFollow.release(_stmt);
    }
  }

  @Override
  public void deleteTemplates() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTemplates.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteTemplates.release(_stmt);
    }
  }

  @Override
  public List<CommentEntity> getComments(final String objectId, final String contentType) {
    final String _sql = "SELECT * FROM comment_table WHERE ObjectId = ? AND ContentType = ? AND parentId=null ORDER BY created desc LIMIT 3 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (objectId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, objectId);
    }
    _argIndex = 2;
    if (contentType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contentType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final List<CommentEntity> _result = new ArrayList<CommentEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CommentEntity _item;
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final String _tmpParentId;
        _tmpParentId = _cursor.getString(_cursorIndexOfParentId);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final String _tmpObjectId;
        _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpBody;
        _tmpBody = _cursor.getString(_cursorIndexOfBody);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _item = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ImageModelEntity> getImages(final String objectId, final String contentType) {
    final String _sql = "SELECT * FROM images_table WHERE ObjectId = ? AND ContentType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (objectId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, objectId);
    }
    _argIndex = 2;
    if (contentType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contentType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final List<ImageModelEntity> _result = new ArrayList<ImageModelEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ImageModelEntity _item;
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpObjectId;
        _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final String _tmpImageUrl;
        _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        _item = new ImageModelEntity(_tmpId,_tmpObjectId,_tmpContentType,_tmpImageUrl,_tmpCreated,_tmpIdentifier);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CommentWithReplies> getc() {
    final String _sql = "SELECT * FROM comment_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final ArrayMap<String, ArrayList<CommentEntity>> _collectionCommentWithReplies = new ArrayMap<String, ArrayList<CommentEntity>>();
      while (_cursor.moveToNext()) {
        if (!_cursor.isNull(_cursorIndexOfId)) {
          final String _tmpKey = _cursor.getString(_cursorIndexOfId);
          ArrayList<CommentEntity> _tmpCommentWithRepliesCollection = _collectionCommentWithReplies.get(_tmpKey);
          if (_tmpCommentWithRepliesCollection == null) {
            _tmpCommentWithRepliesCollection = new ArrayList<CommentEntity>();
            _collectionCommentWithReplies.put(_tmpKey, _tmpCommentWithRepliesCollection);
          }
        }
      }
      _cursor.moveToPosition(-1);
      __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity(_collectionCommentWithReplies);
      final List<CommentWithReplies> _result = new ArrayList<CommentWithReplies>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CommentWithReplies _item;
        final CommentEntity _tmpComment;
        if (! (_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfUsername) && _cursor.isNull(_cursorIndexOfParentId) && _cursor.isNull(_cursorIndexOfContentType) && _cursor.isNull(_cursorIndexOfObjectId) && _cursor.isNull(_cursorIndexOfUserId) && _cursor.isNull(_cursorIndexOfBody) && _cursor.isNull(_cursorIndexOfCreated) && _cursor.isNull(_cursorIndexOfIdentifier))) {
          final String _tmpId;
          _tmpId = _cursor.getString(_cursorIndexOfId);
          final String _tmpUsername;
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
          final String _tmpParentId;
          _tmpParentId = _cursor.getString(_cursorIndexOfParentId);
          final String _tmpContentType;
          _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
          final String _tmpObjectId;
          _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
          final int _tmpUserId;
          _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
          final String _tmpBody;
          _tmpBody = _cursor.getString(_cursorIndexOfBody);
          final OffsetDateTime _tmpCreated;
          final String _tmp;
          _tmp = _cursor.getString(_cursorIndexOfCreated);
          _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
          final String _tmpIdentifier;
          _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
          _tmpComment = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
        }  else  {
          _tmpComment = null;
        }
        ArrayList<CommentEntity> _tmpCommentWithRepliesCollection_1 = null;
        if (!_cursor.isNull(_cursorIndexOfId)) {
          final String _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
          _tmpCommentWithRepliesCollection_1 = _collectionCommentWithReplies.get(_tmpKey_1);
        }
        if (_tmpCommentWithRepliesCollection_1 == null) {
          _tmpCommentWithRepliesCollection_1 = new ArrayList<CommentEntity>();
        }
        _item = new CommentWithReplies(_tmpComment,_tmpCommentWithRepliesCollection_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CommentEntity> getReplies(final String id) {
    final String _sql = "SELECT * FROM comment_table where parentId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final List<CommentEntity> _result = new ArrayList<CommentEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CommentEntity _item;
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final String _tmpParentId;
        _tmpParentId = _cursor.getString(_cursorIndexOfParentId);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final String _tmpObjectId;
        _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpBody;
        _tmpBody = _cursor.getString(_cursorIndexOfBody);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _item = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PagingSource<Integer, CommentEntity> getCommentsSource(final String objectId,
      final String contentType) {
    final String _sql = "SELECT * FROM comment_table WHERE ObjectId = ? AND ContentType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (objectId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, objectId);
    }
    _argIndex = 2;
    if (contentType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contentType);
    }
    return new DataSource.Factory<Integer, CommentEntity>() {
      @Override
      public LimitOffsetDataSource<CommentEntity> create() {
        return new LimitOffsetDataSource<CommentEntity>(__db, _statement, false , "comment_table") {
          @Override
          protected List<CommentEntity> convertRows(Cursor cursor) {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(cursor, "username");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(cursor, "parentId");
            final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(cursor, "contentType");
            final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(cursor, "objectId");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(cursor, "userId");
            final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(cursor, "body");
            final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(cursor, "created");
            final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(cursor, "identifier");
            final List<CommentEntity> _res = new ArrayList<CommentEntity>(cursor.getCount());
            while(cursor.moveToNext()) {
              final CommentEntity _item;
              final String _tmpId;
              _tmpId = cursor.getString(_cursorIndexOfId);
              final String _tmpUsername;
              _tmpUsername = cursor.getString(_cursorIndexOfUsername);
              final String _tmpParentId;
              _tmpParentId = cursor.getString(_cursorIndexOfParentId);
              final String _tmpContentType;
              _tmpContentType = cursor.getString(_cursorIndexOfContentType);
              final String _tmpObjectId;
              _tmpObjectId = cursor.getString(_cursorIndexOfObjectId);
              final int _tmpUserId;
              _tmpUserId = cursor.getInt(_cursorIndexOfUserId);
              final String _tmpBody;
              _tmpBody = cursor.getString(_cursorIndexOfBody);
              final OffsetDateTime _tmpCreated;
              final String _tmp;
              _tmp = cursor.getString(_cursorIndexOfCreated);
              _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
              final String _tmpIdentifier;
              _tmpIdentifier = cursor.getString(_cursorIndexOfIdentifier);
              _item = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
              _res.add(_item);
            }
            return _res;
          }
        };
      }
    }.asPagingSourceFactory().invoke();
  }

  @Override
  public PagingSource<Integer, CommentWithReplies> getCommentsReplySource(final String objectId,
      final String contentType) {
    final String _sql = "SELECT * FROM comment_table WHERE ObjectId = ? AND ContentType = ? AND parentId is NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (objectId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, objectId);
    }
    _argIndex = 2;
    if (contentType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contentType);
    }
    return new DataSource.Factory<Integer, CommentWithReplies>() {
      @Override
      public LimitOffsetDataSource<CommentWithReplies> create() {
        return new LimitOffsetDataSource<CommentWithReplies>(__db, _statement, false , "comment_table") {
          @Override
          protected List<CommentWithReplies> convertRows(Cursor cursor) {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(cursor, "username");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(cursor, "parentId");
            final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(cursor, "contentType");
            final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(cursor, "objectId");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(cursor, "userId");
            final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(cursor, "body");
            final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(cursor, "created");
            final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(cursor, "identifier");
            final ArrayMap<String, ArrayList<CommentEntity>> _collectionCommentWithReplies = new ArrayMap<String, ArrayList<CommentEntity>>();
            while (cursor.moveToNext()) {
              if (!cursor.isNull(_cursorIndexOfId)) {
                final String _tmpKey = cursor.getString(_cursorIndexOfId);
                ArrayList<CommentEntity> _tmpCommentWithRepliesCollection = _collectionCommentWithReplies.get(_tmpKey);
                if (_tmpCommentWithRepliesCollection == null) {
                  _tmpCommentWithRepliesCollection = new ArrayList<CommentEntity>();
                  _collectionCommentWithReplies.put(_tmpKey, _tmpCommentWithRepliesCollection);
                }
              }
            }
            cursor.moveToPosition(-1);
            __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity(_collectionCommentWithReplies);
            final List<CommentWithReplies> _res = new ArrayList<CommentWithReplies>(cursor.getCount());
            while(cursor.moveToNext()) {
              final CommentWithReplies _item;
              final CommentEntity _tmpComment;
              if (! (cursor.isNull(_cursorIndexOfId) && cursor.isNull(_cursorIndexOfUsername) && cursor.isNull(_cursorIndexOfParentId) && cursor.isNull(_cursorIndexOfContentType) && cursor.isNull(_cursorIndexOfObjectId) && cursor.isNull(_cursorIndexOfUserId) && cursor.isNull(_cursorIndexOfBody) && cursor.isNull(_cursorIndexOfCreated) && cursor.isNull(_cursorIndexOfIdentifier))) {
                final String _tmpId;
                _tmpId = cursor.getString(_cursorIndexOfId);
                final String _tmpUsername;
                _tmpUsername = cursor.getString(_cursorIndexOfUsername);
                final String _tmpParentId;
                _tmpParentId = cursor.getString(_cursorIndexOfParentId);
                final String _tmpContentType;
                _tmpContentType = cursor.getString(_cursorIndexOfContentType);
                final String _tmpObjectId;
                _tmpObjectId = cursor.getString(_cursorIndexOfObjectId);
                final int _tmpUserId;
                _tmpUserId = cursor.getInt(_cursorIndexOfUserId);
                final String _tmpBody;
                _tmpBody = cursor.getString(_cursorIndexOfBody);
                final OffsetDateTime _tmpCreated;
                final String _tmp;
                _tmp = cursor.getString(_cursorIndexOfCreated);
                _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
                final String _tmpIdentifier;
                _tmpIdentifier = cursor.getString(_cursorIndexOfIdentifier);
                _tmpComment = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
              }  else  {
                _tmpComment = null;
              }
              ArrayList<CommentEntity> _tmpCommentWithRepliesCollection_1 = null;
              if (!cursor.isNull(_cursorIndexOfId)) {
                final String _tmpKey_1 = cursor.getString(_cursorIndexOfId);
                _tmpCommentWithRepliesCollection_1 = _collectionCommentWithReplies.get(_tmpKey_1);
              }
              if (_tmpCommentWithRepliesCollection_1 == null) {
                _tmpCommentWithRepliesCollection_1 = new ArrayList<CommentEntity>();
              }
              _item = new CommentWithReplies(_tmpComment,_tmpCommentWithRepliesCollection_1);
              _res.add(_item);
            }
            return _res;
          }
        };
      }
    }.asPagingSourceFactory().invoke();
  }

  @Override
  public MemeEntity getMemeById(final String id) {
    final String _sql = "SELECT * FROM meme_table where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "is_following");
      final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_liked");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_bookmarked");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final MemeEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final boolean _tmpIs_following;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFollowing);
        _tmpIs_following = _tmp_1 != 0;
        final boolean _tmpIs_liked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLiked);
        _tmpIs_liked = _tmp_2 != 0;
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        final boolean _tmpIs_bookmarked;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
        _tmpIs_bookmarked = _tmp_3 != 0;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _result = new MemeEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MemeEntity> getMemeList() {
    final String _sql = "SELECT * FROM meme_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "is_following");
      final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_liked");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_bookmarked");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final List<MemeEntity> _result = new ArrayList<MemeEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MemeEntity _item;
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final boolean _tmpIs_following;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFollowing);
        _tmpIs_following = _tmp_1 != 0;
        final boolean _tmpIs_liked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLiked);
        _tmpIs_liked = _tmp_2 != 0;
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        final boolean _tmpIs_bookmarked;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
        _tmpIs_bookmarked = _tmp_3 != 0;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _item = new MemeEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PagingSource<Integer, Meme> getMemeSource() {
    final String _sql = "SELECT * FROM meme_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new DataSource.Factory<Integer, Meme>() {
      @Override
      public LimitOffsetDataSource<Meme> create() {
        return new LimitOffsetDataSource<Meme>(__db, _statement, true , "images_table", "comment_table", "meme_table") {
          @Override
          protected List<Meme> convertRows(Cursor cursor) {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(cursor, "username");
            final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(cursor, "created");
            final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(cursor, "contentType");
            final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(cursor, "is_following");
            final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(cursor, "is_liked");
            final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(cursor, "likes");
            final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(cursor, "is_bookmarked");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(cursor, "userId");
            final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(cursor, "identifier");
            final ArrayMap<String, ArrayList<ImageModelEntity>> _collectionImages = new ArrayMap<String, ArrayList<ImageModelEntity>>();
            final ArrayMap<String, ArrayList<CommentEntity>> _collectionComments = new ArrayMap<String, ArrayList<CommentEntity>>();
            while (cursor.moveToNext()) {
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey = cursor.getString(_cursorIndexOfIdentifier);
                ArrayList<ImageModelEntity> _tmpImagesCollection = _collectionImages.get(_tmpKey);
                if (_tmpImagesCollection == null) {
                  _tmpImagesCollection = new ArrayList<ImageModelEntity>();
                  _collectionImages.put(_tmpKey, _tmpImagesCollection);
                }
              }
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_1 = cursor.getString(_cursorIndexOfIdentifier);
                ArrayList<CommentEntity> _tmpCommentsCollection = _collectionComments.get(_tmpKey_1);
                if (_tmpCommentsCollection == null) {
                  _tmpCommentsCollection = new ArrayList<CommentEntity>();
                  _collectionComments.put(_tmpKey_1, _tmpCommentsCollection);
                }
              }
            }
            cursor.moveToPosition(-1);
            __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(_collectionImages);
            __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(_collectionComments);
            final List<Meme> _res = new ArrayList<Meme>(cursor.getCount());
            while(cursor.moveToNext()) {
              final Meme _item;
              final MemeEntity _tmpMeme;
              if (! (cursor.isNull(_cursorIndexOfId) && cursor.isNull(_cursorIndexOfDescription) && cursor.isNull(_cursorIndexOfUsername) && cursor.isNull(_cursorIndexOfCreated) && cursor.isNull(_cursorIndexOfContentType) && cursor.isNull(_cursorIndexOfIsFollowing) && cursor.isNull(_cursorIndexOfIsLiked) && cursor.isNull(_cursorIndexOfLikes) && cursor.isNull(_cursorIndexOfIsBookmarked) && cursor.isNull(_cursorIndexOfUserId) && cursor.isNull(_cursorIndexOfIdentifier))) {
                final String _tmpId;
                _tmpId = cursor.getString(_cursorIndexOfId);
                final String _tmpDescription;
                _tmpDescription = cursor.getString(_cursorIndexOfDescription);
                final String _tmpUsername;
                _tmpUsername = cursor.getString(_cursorIndexOfUsername);
                final OffsetDateTime _tmpCreated;
                final String _tmp;
                _tmp = cursor.getString(_cursorIndexOfCreated);
                _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
                final String _tmpContentType;
                _tmpContentType = cursor.getString(_cursorIndexOfContentType);
                final boolean _tmpIs_following;
                final int _tmp_1;
                _tmp_1 = cursor.getInt(_cursorIndexOfIsFollowing);
                _tmpIs_following = _tmp_1 != 0;
                final boolean _tmpIs_liked;
                final int _tmp_2;
                _tmp_2 = cursor.getInt(_cursorIndexOfIsLiked);
                _tmpIs_liked = _tmp_2 != 0;
                final int _tmpLikes;
                _tmpLikes = cursor.getInt(_cursorIndexOfLikes);
                final boolean _tmpIs_bookmarked;
                final int _tmp_3;
                _tmp_3 = cursor.getInt(_cursorIndexOfIsBookmarked);
                _tmpIs_bookmarked = _tmp_3 != 0;
                final int _tmpUserId;
                _tmpUserId = cursor.getInt(_cursorIndexOfUserId);
                final String _tmpIdentifier;
                _tmpIdentifier = cursor.getString(_cursorIndexOfIdentifier);
                _tmpMeme = new MemeEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
              }  else  {
                _tmpMeme = null;
              }
              ArrayList<ImageModelEntity> _tmpImagesCollection_1 = null;
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_2 = cursor.getString(_cursorIndexOfIdentifier);
                _tmpImagesCollection_1 = _collectionImages.get(_tmpKey_2);
              }
              if (_tmpImagesCollection_1 == null) {
                _tmpImagesCollection_1 = new ArrayList<ImageModelEntity>();
              }
              ArrayList<CommentEntity> _tmpCommentsCollection_1 = null;
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_3 = cursor.getString(_cursorIndexOfIdentifier);
                _tmpCommentsCollection_1 = _collectionComments.get(_tmpKey_3);
              }
              if (_tmpCommentsCollection_1 == null) {
                _tmpCommentsCollection_1 = new ArrayList<CommentEntity>();
              }
              _item = new Meme();
              _item.meme = _tmpMeme;
              _item.images = _tmpImagesCollection_1;
              _item.comments = _tmpCommentsCollection_1;
              _res.add(_item);
            }
            return _res;
          }
        };
      }
    }.asPagingSourceFactory().invoke();
  }

  @Override
  public Meme getMemeAllById(final String id) {
    final String _sql = "SELECT * FROM meme_table where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
        final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
        final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
        final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
        final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "is_following");
        final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_liked");
        final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
        final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_bookmarked");
        final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
        final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
        final ArrayMap<String, ArrayList<ImageModelEntity>> _collectionImages = new ArrayMap<String, ArrayList<ImageModelEntity>>();
        final ArrayMap<String, ArrayList<CommentEntity>> _collectionComments = new ArrayMap<String, ArrayList<CommentEntity>>();
        while (_cursor.moveToNext()) {
          if (!_cursor.isNull(_cursorIndexOfIdentifier)) {
            final String _tmpKey = _cursor.getString(_cursorIndexOfIdentifier);
            ArrayList<ImageModelEntity> _tmpImagesCollection = _collectionImages.get(_tmpKey);
            if (_tmpImagesCollection == null) {
              _tmpImagesCollection = new ArrayList<ImageModelEntity>();
              _collectionImages.put(_tmpKey, _tmpImagesCollection);
            }
          }
          if (!_cursor.isNull(_cursorIndexOfIdentifier)) {
            final String _tmpKey_1 = _cursor.getString(_cursorIndexOfIdentifier);
            ArrayList<CommentEntity> _tmpCommentsCollection = _collectionComments.get(_tmpKey_1);
            if (_tmpCommentsCollection == null) {
              _tmpCommentsCollection = new ArrayList<CommentEntity>();
              _collectionComments.put(_tmpKey_1, _tmpCommentsCollection);
            }
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(_collectionImages);
        __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(_collectionComments);
        final Meme _result;
        if(_cursor.moveToFirst()) {
          final MemeEntity _tmpMeme;
          if (! (_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfDescription) && _cursor.isNull(_cursorIndexOfUsername) && _cursor.isNull(_cursorIndexOfCreated) && _cursor.isNull(_cursorIndexOfContentType) && _cursor.isNull(_cursorIndexOfIsFollowing) && _cursor.isNull(_cursorIndexOfIsLiked) && _cursor.isNull(_cursorIndexOfLikes) && _cursor.isNull(_cursorIndexOfIsBookmarked) && _cursor.isNull(_cursorIndexOfUserId) && _cursor.isNull(_cursorIndexOfIdentifier))) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final OffsetDateTime _tmpCreated;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCreated);
            _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
            final String _tmpContentType;
            _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
            final boolean _tmpIs_following;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFollowing);
            _tmpIs_following = _tmp_1 != 0;
            final boolean _tmpIs_liked;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIs_liked = _tmp_2 != 0;
            final int _tmpLikes;
            _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
            final boolean _tmpIs_bookmarked;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIs_bookmarked = _tmp_3 != 0;
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpIdentifier;
            _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
            _tmpMeme = new MemeEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
          }  else  {
            _tmpMeme = null;
          }
          ArrayList<ImageModelEntity> _tmpImagesCollection_1 = null;
          if (!_cursor.isNull(_cursorIndexOfIdentifier)) {
            final String _tmpKey_2 = _cursor.getString(_cursorIndexOfIdentifier);
            _tmpImagesCollection_1 = _collectionImages.get(_tmpKey_2);
          }
          if (_tmpImagesCollection_1 == null) {
            _tmpImagesCollection_1 = new ArrayList<ImageModelEntity>();
          }
          ArrayList<CommentEntity> _tmpCommentsCollection_1 = null;
          if (!_cursor.isNull(_cursorIndexOfIdentifier)) {
            final String _tmpKey_3 = _cursor.getString(_cursorIndexOfIdentifier);
            _tmpCommentsCollection_1 = _collectionComments.get(_tmpKey_3);
          }
          if (_tmpCommentsCollection_1 == null) {
            _tmpCommentsCollection_1 = new ArrayList<CommentEntity>();
          }
          _result = new Meme();
          _result.meme = _tmpMeme;
          _result.images = _tmpImagesCollection_1;
          _result.comments = _tmpCommentsCollection_1;
        } else {
          _result = null;
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public TemplateEntity getTemplateById(final String id) {
    final String _sql = "SELECT * FROM template_table where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "is_following");
      final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_liked");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_bookmarked");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final TemplateEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final boolean _tmpIs_following;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFollowing);
        _tmpIs_following = _tmp_1 != 0;
        final boolean _tmpIs_liked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLiked);
        _tmpIs_liked = _tmp_2 != 0;
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        final boolean _tmpIs_bookmarked;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
        _tmpIs_bookmarked = _tmp_3 != 0;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _result = new TemplateEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TemplateEntity> getTemplateList() {
    final String _sql = "SELECT * FROM template_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "is_following");
      final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_liked");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_bookmarked");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final List<TemplateEntity> _result = new ArrayList<TemplateEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemplateEntity _item;
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final OffsetDateTime _tmpCreated;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfCreated);
        _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
        final String _tmpContentType;
        _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
        final boolean _tmpIs_following;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFollowing);
        _tmpIs_following = _tmp_1 != 0;
        final boolean _tmpIs_liked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLiked);
        _tmpIs_liked = _tmp_2 != 0;
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        final boolean _tmpIs_bookmarked;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
        _tmpIs_bookmarked = _tmp_3 != 0;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpIdentifier;
        _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
        _item = new TemplateEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PagingSource<Integer, Template> getTemplateSource() {
    final String _sql = "SELECT * FROM template_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new DataSource.Factory<Integer, Template>() {
      @Override
      public LimitOffsetDataSource<Template> create() {
        return new LimitOffsetDataSource<Template>(__db, _statement, true , "images_table", "comment_table", "template_table") {
          @Override
          protected List<Template> convertRows(Cursor cursor) {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(cursor, "username");
            final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(cursor, "created");
            final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(cursor, "contentType");
            final int _cursorIndexOfIsFollowing = CursorUtil.getColumnIndexOrThrow(cursor, "is_following");
            final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(cursor, "is_liked");
            final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(cursor, "likes");
            final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(cursor, "is_bookmarked");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(cursor, "userId");
            final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(cursor, "identifier");
            final ArrayMap<String, ArrayList<ImageModelEntity>> _collectionImages = new ArrayMap<String, ArrayList<ImageModelEntity>>();
            final ArrayMap<String, ArrayList<CommentEntity>> _collectionComments = new ArrayMap<String, ArrayList<CommentEntity>>();
            while (cursor.moveToNext()) {
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey = cursor.getString(_cursorIndexOfIdentifier);
                ArrayList<ImageModelEntity> _tmpImagesCollection = _collectionImages.get(_tmpKey);
                if (_tmpImagesCollection == null) {
                  _tmpImagesCollection = new ArrayList<ImageModelEntity>();
                  _collectionImages.put(_tmpKey, _tmpImagesCollection);
                }
              }
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_1 = cursor.getString(_cursorIndexOfIdentifier);
                ArrayList<CommentEntity> _tmpCommentsCollection = _collectionComments.get(_tmpKey_1);
                if (_tmpCommentsCollection == null) {
                  _tmpCommentsCollection = new ArrayList<CommentEntity>();
                  _collectionComments.put(_tmpKey_1, _tmpCommentsCollection);
                }
              }
            }
            cursor.moveToPosition(-1);
            __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(_collectionImages);
            __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(_collectionComments);
            final List<Template> _res = new ArrayList<Template>(cursor.getCount());
            while(cursor.moveToNext()) {
              final Template _item;
              final TemplateEntity _tmpTemplate;
              if (! (cursor.isNull(_cursorIndexOfId) && cursor.isNull(_cursorIndexOfDescription) && cursor.isNull(_cursorIndexOfUsername) && cursor.isNull(_cursorIndexOfCreated) && cursor.isNull(_cursorIndexOfContentType) && cursor.isNull(_cursorIndexOfIsFollowing) && cursor.isNull(_cursorIndexOfIsLiked) && cursor.isNull(_cursorIndexOfLikes) && cursor.isNull(_cursorIndexOfIsBookmarked) && cursor.isNull(_cursorIndexOfUserId) && cursor.isNull(_cursorIndexOfIdentifier))) {
                final String _tmpId;
                _tmpId = cursor.getString(_cursorIndexOfId);
                final String _tmpDescription;
                _tmpDescription = cursor.getString(_cursorIndexOfDescription);
                final String _tmpUsername;
                _tmpUsername = cursor.getString(_cursorIndexOfUsername);
                final OffsetDateTime _tmpCreated;
                final String _tmp;
                _tmp = cursor.getString(_cursorIndexOfCreated);
                _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
                final String _tmpContentType;
                _tmpContentType = cursor.getString(_cursorIndexOfContentType);
                final boolean _tmpIs_following;
                final int _tmp_1;
                _tmp_1 = cursor.getInt(_cursorIndexOfIsFollowing);
                _tmpIs_following = _tmp_1 != 0;
                final boolean _tmpIs_liked;
                final int _tmp_2;
                _tmp_2 = cursor.getInt(_cursorIndexOfIsLiked);
                _tmpIs_liked = _tmp_2 != 0;
                final int _tmpLikes;
                _tmpLikes = cursor.getInt(_cursorIndexOfLikes);
                final boolean _tmpIs_bookmarked;
                final int _tmp_3;
                _tmp_3 = cursor.getInt(_cursorIndexOfIsBookmarked);
                _tmpIs_bookmarked = _tmp_3 != 0;
                final int _tmpUserId;
                _tmpUserId = cursor.getInt(_cursorIndexOfUserId);
                final String _tmpIdentifier;
                _tmpIdentifier = cursor.getString(_cursorIndexOfIdentifier);
                _tmpTemplate = new TemplateEntity(_tmpId,_tmpDescription,_tmpUsername,_tmpCreated,_tmpContentType,_tmpIs_following,_tmpIs_liked,_tmpLikes,_tmpIs_bookmarked,_tmpUserId,_tmpIdentifier);
              }  else  {
                _tmpTemplate = null;
              }
              ArrayList<ImageModelEntity> _tmpImagesCollection_1 = null;
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_2 = cursor.getString(_cursorIndexOfIdentifier);
                _tmpImagesCollection_1 = _collectionImages.get(_tmpKey_2);
              }
              if (_tmpImagesCollection_1 == null) {
                _tmpImagesCollection_1 = new ArrayList<ImageModelEntity>();
              }
              ArrayList<CommentEntity> _tmpCommentsCollection_1 = null;
              if (!cursor.isNull(_cursorIndexOfIdentifier)) {
                final String _tmpKey_3 = cursor.getString(_cursorIndexOfIdentifier);
                _tmpCommentsCollection_1 = _collectionComments.get(_tmpKey_3);
              }
              if (_tmpCommentsCollection_1 == null) {
                _tmpCommentsCollection_1 = new ArrayList<CommentEntity>();
              }
              _item = new Template();
              _item.template = _tmpTemplate;
              _item.images = _tmpImagesCollection_1;
              _item.comments = _tmpCommentsCollection_1;
              _res.add(_item);
            }
            return _res;
          }
        };
      }
    }.asPagingSourceFactory().invoke();
  }

  private void __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity(
      final ArrayMap<String, ArrayList<CommentEntity>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    // check if the size is too big, if so divide;
    if(_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      ArrayMap<String, ArrayList<CommentEntity>> _tmpInnerMap = new ArrayMap<String, ArrayList<CommentEntity>>(androidx.room.RoomDatabase.MAX_BIND_PARAMETER_CNT);
      int _tmpIndex = 0;
      int _mapIndex = 0;
      final int _limit = _map.size();
      while(_mapIndex < _limit) {
        _tmpInnerMap.put(_map.keyAt(_mapIndex), _map.valueAt(_mapIndex));
        _mapIndex++;
        _tmpIndex++;
        if(_tmpIndex == RoomDatabase.MAX_BIND_PARAMETER_CNT) {
          __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity(_tmpInnerMap);
          _tmpInnerMap = new ArrayMap<String, ArrayList<CommentEntity>>(RoomDatabase.MAX_BIND_PARAMETER_CNT);
          _tmpIndex = 0;
        }
      }
      if(_tmpIndex > 0) {
        __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity(_tmpInnerMap);
      }
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`username`,`parentId`,`contentType`,`objectId`,`userId`,`body`,`created`,`identifier` FROM `comment_table` WHERE `parentId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "parentId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      while(_cursor.moveToNext()) {
        if (!_cursor.isNull(_itemKeyIndex)) {
          final String _tmpKey = _cursor.getString(_itemKeyIndex);
          ArrayList<CommentEntity> _tmpRelation = _map.get(_tmpKey);
          if (_tmpRelation != null) {
            final CommentEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpParentId;
            _tmpParentId = _cursor.getString(_cursorIndexOfParentId);
            final String _tmpContentType;
            _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
            final String _tmpObjectId;
            _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final OffsetDateTime _tmpCreated;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCreated);
            _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
            final String _tmpIdentifier;
            _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
            _item_1 = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
            _tmpRelation.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(
      final ArrayMap<String, ArrayList<ImageModelEntity>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    // check if the size is too big, if so divide;
    if(_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      ArrayMap<String, ArrayList<ImageModelEntity>> _tmpInnerMap = new ArrayMap<String, ArrayList<ImageModelEntity>>(androidx.room.RoomDatabase.MAX_BIND_PARAMETER_CNT);
      int _tmpIndex = 0;
      int _mapIndex = 0;
      final int _limit = _map.size();
      while(_mapIndex < _limit) {
        _tmpInnerMap.put(_map.keyAt(_mapIndex), _map.valueAt(_mapIndex));
        _mapIndex++;
        _tmpIndex++;
        if(_tmpIndex == RoomDatabase.MAX_BIND_PARAMETER_CNT) {
          __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(_tmpInnerMap);
          _tmpInnerMap = new ArrayMap<String, ArrayList<ImageModelEntity>>(RoomDatabase.MAX_BIND_PARAMETER_CNT);
          _tmpIndex = 0;
        }
      }
      if(_tmpIndex > 0) {
        __fetchRelationshipimagesTableAscomSudharNetizenRoomDBEntitiesImageModelEntity(_tmpInnerMap);
      }
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`objectId`,`contentType`,`imageUrl`,`identifier`,`created` FROM `images_table` WHERE `identifier` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "identifier");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      while(_cursor.moveToNext()) {
        if (!_cursor.isNull(_itemKeyIndex)) {
          final String _tmpKey = _cursor.getString(_itemKeyIndex);
          ArrayList<ImageModelEntity> _tmpRelation = _map.get(_tmpKey);
          if (_tmpRelation != null) {
            final ImageModelEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpObjectId;
            _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            final String _tmpContentType;
            _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpIdentifier;
            _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
            final OffsetDateTime _tmpCreated;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCreated);
            _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
            _item_1 = new ImageModelEntity(_tmpId,_tmpObjectId,_tmpContentType,_tmpImageUrl,_tmpCreated,_tmpIdentifier);
            _tmpRelation.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(
      final ArrayMap<String, ArrayList<CommentEntity>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    // check if the size is too big, if so divide;
    if(_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      ArrayMap<String, ArrayList<CommentEntity>> _tmpInnerMap = new ArrayMap<String, ArrayList<CommentEntity>>(androidx.room.RoomDatabase.MAX_BIND_PARAMETER_CNT);
      int _tmpIndex = 0;
      int _mapIndex = 0;
      final int _limit = _map.size();
      while(_mapIndex < _limit) {
        _tmpInnerMap.put(_map.keyAt(_mapIndex), _map.valueAt(_mapIndex));
        _mapIndex++;
        _tmpIndex++;
        if(_tmpIndex == RoomDatabase.MAX_BIND_PARAMETER_CNT) {
          __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(_tmpInnerMap);
          _tmpInnerMap = new ArrayMap<String, ArrayList<CommentEntity>>(RoomDatabase.MAX_BIND_PARAMETER_CNT);
          _tmpIndex = 0;
        }
      }
      if(_tmpIndex > 0) {
        __fetchRelationshipcommentTableAscomSudharNetizenRoomDBEntitiesCommentEntity_1(_tmpInnerMap);
      }
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`username`,`parentId`,`contentType`,`objectId`,`userId`,`body`,`created`,`identifier` FROM `comment_table` WHERE `identifier` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "identifier");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfContentType = CursorUtil.getColumnIndexOrThrow(_cursor, "contentType");
      final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfIdentifier = CursorUtil.getColumnIndexOrThrow(_cursor, "identifier");
      while(_cursor.moveToNext()) {
        if (!_cursor.isNull(_itemKeyIndex)) {
          final String _tmpKey = _cursor.getString(_itemKeyIndex);
          ArrayList<CommentEntity> _tmpRelation = _map.get(_tmpKey);
          if (_tmpRelation != null) {
            final CommentEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpParentId;
            _tmpParentId = _cursor.getString(_cursorIndexOfParentId);
            final String _tmpContentType;
            _tmpContentType = _cursor.getString(_cursorIndexOfContentType);
            final String _tmpObjectId;
            _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final OffsetDateTime _tmpCreated;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCreated);
            _tmpCreated = __dateTimeConverter.toOffsetDateTime(_tmp);
            final String _tmpIdentifier;
            _tmpIdentifier = _cursor.getString(_cursorIndexOfIdentifier);
            _item_1 = new CommentEntity(_tmpId,_tmpUsername,_tmpParentId,_tmpContentType,_tmpObjectId,_tmpUserId,_tmpBody,_tmpCreated,_tmpIdentifier);
            _tmpRelation.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
