package com.gitsoft.thoughtpad.core.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gitsoft.thoughtpad.core.model.CheckListItem;
import com.gitsoft.thoughtpad.core.model.Converters;
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags;
import com.gitsoft.thoughtpad.core.model.Note;
import com.gitsoft.thoughtpad.core.model.NoteTagCrossRef;
import com.gitsoft.thoughtpad.core.model.Tag;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotesDatabaseDao_Impl implements NotesDatabaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Note> __insertionAdapterOfNote;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<CheckListItem> __insertionAdapterOfCheckListItem;

  private final EntityInsertionAdapter<Tag> __insertionAdapterOfTag;

  private final EntityInsertionAdapter<NoteTagCrossRef> __insertionAdapterOfNoteTagCrossRef;

  private final EntityInsertionAdapter<NoteTagCrossRef> __insertionAdapterOfNoteTagCrossRef_1;

  private final EntityDeletionOrUpdateAdapter<Note> __deletionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter<CheckListItem> __deletionAdapterOfCheckListItem;

  private final EntityDeletionOrUpdateAdapter<Tag> __deletionAdapterOfTag;

  private final EntityDeletionOrUpdateAdapter<Note> __updateAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter<CheckListItem> __updateAdapterOfCheckListItem;

  private final EntityDeletionOrUpdateAdapter<Tag> __updateAdapterOfTag;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNoteTagCrossRefsForNoteId;

  public NotesDatabaseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNote = new EntityInsertionAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notes_table` (`noteId`,`noteTitle`,`noteText`,`createdAt`,`updatedAt`,`isPinned`,`isArchived`,`color`,`isFavorite`,`isDeleted`,`isCheckList`,`reminderTime`,`attachments`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getNoteId());
        if (entity.getNoteTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNoteTitle());
        }
        if (entity.getNoteText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNoteText());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getUpdatedAt());
        }
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getColor());
        final int _tmp_2 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        final int _tmp_3 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(10, _tmp_3);
        final int _tmp_4 = entity.isCheckList() ? 1 : 0;
        statement.bindLong(11, _tmp_4);
        if (entity.getReminderTime() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getReminderTime());
        }
        final String _tmp_5 = __converters.fromAttachmentsList(entity.getAttachments());
        statement.bindString(13, _tmp_5);
      }
    };
    this.__insertionAdapterOfCheckListItem = new EntityInsertionAdapter<CheckListItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `checklist` (`checkListItemId`,`noteId`,`text`,`isChecked`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CheckListItem entity) {
        statement.bindLong(1, entity.getCheckListItemId());
        if (entity.getNoteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getNoteId());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        final int _tmp = entity.isChecked() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__insertionAdapterOfTag = new EntityInsertionAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `noteTags` (`tagId`,`name`,`color`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getColor() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getColor());
        }
      }
    };
    this.__insertionAdapterOfNoteTagCrossRef = new EntityInsertionAdapter<NoteTagCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `NoteTagCrossRef` (`noteId`,`tagId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteTagCrossRef entity) {
        statement.bindLong(1, entity.getNoteId());
        statement.bindLong(2, entity.getTagId());
      }
    };
    this.__insertionAdapterOfNoteTagCrossRef_1 = new EntityInsertionAdapter<NoteTagCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `NoteTagCrossRef` (`noteId`,`tagId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteTagCrossRef entity) {
        statement.bindLong(1, entity.getNoteId());
        statement.bindLong(2, entity.getTagId());
      }
    };
    this.__deletionAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `notes_table` WHERE `noteId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getNoteId());
      }
    };
    this.__deletionAdapterOfCheckListItem = new EntityDeletionOrUpdateAdapter<CheckListItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `checklist` WHERE `checkListItemId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CheckListItem entity) {
        statement.bindLong(1, entity.getCheckListItemId());
      }
    };
    this.__deletionAdapterOfTag = new EntityDeletionOrUpdateAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `noteTags` WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
      }
    };
    this.__updateAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notes_table` SET `noteId` = ?,`noteTitle` = ?,`noteText` = ?,`createdAt` = ?,`updatedAt` = ?,`isPinned` = ?,`isArchived` = ?,`color` = ?,`isFavorite` = ?,`isDeleted` = ?,`isCheckList` = ?,`reminderTime` = ?,`attachments` = ? WHERE `noteId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindLong(1, entity.getNoteId());
        if (entity.getNoteTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNoteTitle());
        }
        if (entity.getNoteText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNoteText());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getUpdatedAt());
        }
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getColor());
        final int _tmp_2 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        final int _tmp_3 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(10, _tmp_3);
        final int _tmp_4 = entity.isCheckList() ? 1 : 0;
        statement.bindLong(11, _tmp_4);
        if (entity.getReminderTime() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getReminderTime());
        }
        final String _tmp_5 = __converters.fromAttachmentsList(entity.getAttachments());
        statement.bindString(13, _tmp_5);
        statement.bindLong(14, entity.getNoteId());
      }
    };
    this.__updateAdapterOfCheckListItem = new EntityDeletionOrUpdateAdapter<CheckListItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `checklist` SET `checkListItemId` = ?,`noteId` = ?,`text` = ?,`isChecked` = ? WHERE `checkListItemId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CheckListItem entity) {
        statement.bindLong(1, entity.getCheckListItemId());
        if (entity.getNoteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getNoteId());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        final int _tmp = entity.isChecked() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCheckListItemId());
      }
    };
    this.__updateAdapterOfTag = new EntityDeletionOrUpdateAdapter<Tag>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `noteTags` SET `tagId` = ?,`name` = ?,`color` = ? WHERE `tagId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tag entity) {
        statement.bindLong(1, entity.getTagId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getColor() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getColor());
        }
        statement.bindLong(4, entity.getTagId());
      }
    };
    this.__preparedStmtOfDeleteNoteTagCrossRefsForNoteId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM NoteTagCrossRef WHERE noteId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertNote(final Note note, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNote.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertChecklistItems(final List<CheckListItem> checklistItems,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCheckListItem.insert(checklistItems);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTags(final List<Tag> tags, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTag.insert(tags);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTag(final Tag tag, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTag.insertAndReturnId(tag);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertNoteTagCrossRefs(final List<NoteTagCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNoteTagCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertNoteTagCrossRef(final NoteTagCrossRef noteTagCrossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNoteTagCrossRef_1.insert(noteTagCrossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Note note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfNote.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChecklistItems(final List<CheckListItem> checklistItems,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCheckListItem.handleMultiple(checklistItems);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTags(final List<Tag> tags, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTag.handleMultiple(tags);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNote(final Note note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNote.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChecklistItem(final CheckListItem checklistItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCheckListItem.handle(checklistItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTags(final List<Tag> tags, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTag.handleMultiple(tags);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTag(final Tag tag, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTag.handle(tag);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertNoteWithDetails(final Note note, final List<CheckListItem> checklistItems,
      final List<Tag> tags, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> NotesDatabaseDao.DefaultImpls.insertNoteWithDetails(NotesDatabaseDao_Impl.this, note, checklistItems, tags, __cont), $completion);
  }

  @Override
  public Object updateNoteWithDetails(final Note note, final List<CheckListItem> checklistItems,
      final List<Tag> tags, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> NotesDatabaseDao.DefaultImpls.updateNoteWithDetails(NotesDatabaseDao_Impl.this, note, checklistItems, tags, __cont), $completion);
  }

  @Override
  public Object deleteNoteTagCrossRefsForNoteId(final long noteId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNoteTagCrossRefsForNoteId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, noteId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteNoteTagCrossRefsForNoteId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object loadAllNotes(
      final Continuation<? super List<DataWithNotesCheckListItemsAndTags>> $completion) {
    final String _sql = "SELECT * FROM notes_table order by noteId desc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<DataWithNotesCheckListItemsAndTags>>() {
      @Override
      @NonNull
      public List<DataWithNotesCheckListItemsAndTags> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfNoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "noteId");
            final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTitle");
            final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
            final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfIsCheckList = CursorUtil.getColumnIndexOrThrow(_cursor, "isCheckList");
            final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderTime");
            final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
            final LongSparseArray<ArrayList<CheckListItem>> _collectionCheckListItems = new LongSparseArray<ArrayList<CheckListItem>>();
            final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfNoteId);
              if (!_collectionCheckListItems.containsKey(_tmpKey)) {
                _collectionCheckListItems.put(_tmpKey, new ArrayList<CheckListItem>());
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfNoteId);
              if (!_collectionTags.containsKey(_tmpKey_1)) {
                _collectionTags.put(_tmpKey_1, new ArrayList<Tag>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistAscomGitsoftThoughtpadCoreModelCheckListItem(_collectionCheckListItems);
            __fetchRelationshipnoteTagsAscomGitsoftThoughtpadCoreModelTag(_collectionTags);
            final List<DataWithNotesCheckListItemsAndTags> _result = new ArrayList<DataWithNotesCheckListItemsAndTags>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final DataWithNotesCheckListItemsAndTags _item;
              final Note _tmpNote;
              final long _tmpNoteId;
              _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
              final String _tmpNoteTitle;
              if (_cursor.isNull(_cursorIndexOfNoteTitle)) {
                _tmpNoteTitle = null;
              } else {
                _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
              }
              final String _tmpNoteText;
              if (_cursor.isNull(_cursorIndexOfNoteText)) {
                _tmpNoteText = null;
              } else {
                _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
              }
              final Long _tmpCreatedAt;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmpCreatedAt = null;
              } else {
                _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              }
              final Long _tmpUpdatedAt;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmpUpdatedAt = null;
              } else {
                _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              }
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final long _tmpColor;
              _tmpColor = _cursor.getLong(_cursorIndexOfColor);
              final boolean _tmpIsFavorite;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsFavorite);
              _tmpIsFavorite = _tmp_2 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_3 != 0;
              final boolean _tmpIsCheckList;
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsCheckList);
              _tmpIsCheckList = _tmp_4 != 0;
              final Long _tmpReminderTime;
              if (_cursor.isNull(_cursorIndexOfReminderTime)) {
                _tmpReminderTime = null;
              } else {
                _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
              }
              final List<String> _tmpAttachments;
              final String _tmp_5;
              _tmp_5 = _cursor.getString(_cursorIndexOfAttachments);
              _tmpAttachments = __converters.toAttachmentsList(_tmp_5);
              _tmpNote = new Note(_tmpNoteId,_tmpNoteTitle,_tmpNoteText,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPinned,_tmpIsArchived,_tmpColor,_tmpIsFavorite,_tmpIsDeleted,_tmpIsCheckList,_tmpReminderTime,_tmpAttachments);
              final ArrayList<CheckListItem> _tmpCheckListItemsCollection;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfNoteId);
              _tmpCheckListItemsCollection = _collectionCheckListItems.get(_tmpKey_2);
              final ArrayList<Tag> _tmpTagsCollection;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfNoteId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_3);
              _item = new DataWithNotesCheckListItemsAndTags(_tmpNote,_tmpCheckListItemsCollection,_tmpTagsCollection);
              _result.add(_item);
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
    }, $completion);
  }

  @Override
  public Object noteById(final int id,
      final Continuation<? super DataWithNotesCheckListItemsAndTags> $completion) {
    final String _sql = "SELECT * FROM notes_table where noteId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<DataWithNotesCheckListItemsAndTags>() {
      @Override
      @NonNull
      public DataWithNotesCheckListItemsAndTags call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfNoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "noteId");
            final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTitle");
            final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
            final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfIsCheckList = CursorUtil.getColumnIndexOrThrow(_cursor, "isCheckList");
            final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderTime");
            final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
            final LongSparseArray<ArrayList<CheckListItem>> _collectionCheckListItems = new LongSparseArray<ArrayList<CheckListItem>>();
            final LongSparseArray<ArrayList<Tag>> _collectionTags = new LongSparseArray<ArrayList<Tag>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfNoteId);
              if (!_collectionCheckListItems.containsKey(_tmpKey)) {
                _collectionCheckListItems.put(_tmpKey, new ArrayList<CheckListItem>());
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfNoteId);
              if (!_collectionTags.containsKey(_tmpKey_1)) {
                _collectionTags.put(_tmpKey_1, new ArrayList<Tag>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistAscomGitsoftThoughtpadCoreModelCheckListItem(_collectionCheckListItems);
            __fetchRelationshipnoteTagsAscomGitsoftThoughtpadCoreModelTag(_collectionTags);
            final DataWithNotesCheckListItemsAndTags _result;
            if (_cursor.moveToFirst()) {
              final Note _tmpNote;
              final long _tmpNoteId;
              _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
              final String _tmpNoteTitle;
              if (_cursor.isNull(_cursorIndexOfNoteTitle)) {
                _tmpNoteTitle = null;
              } else {
                _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
              }
              final String _tmpNoteText;
              if (_cursor.isNull(_cursorIndexOfNoteText)) {
                _tmpNoteText = null;
              } else {
                _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
              }
              final Long _tmpCreatedAt;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmpCreatedAt = null;
              } else {
                _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              }
              final Long _tmpUpdatedAt;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmpUpdatedAt = null;
              } else {
                _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              }
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final long _tmpColor;
              _tmpColor = _cursor.getLong(_cursorIndexOfColor);
              final boolean _tmpIsFavorite;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsFavorite);
              _tmpIsFavorite = _tmp_2 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_3 != 0;
              final boolean _tmpIsCheckList;
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsCheckList);
              _tmpIsCheckList = _tmp_4 != 0;
              final Long _tmpReminderTime;
              if (_cursor.isNull(_cursorIndexOfReminderTime)) {
                _tmpReminderTime = null;
              } else {
                _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
              }
              final List<String> _tmpAttachments;
              final String _tmp_5;
              _tmp_5 = _cursor.getString(_cursorIndexOfAttachments);
              _tmpAttachments = __converters.toAttachmentsList(_tmp_5);
              _tmpNote = new Note(_tmpNoteId,_tmpNoteTitle,_tmpNoteText,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPinned,_tmpIsArchived,_tmpColor,_tmpIsFavorite,_tmpIsDeleted,_tmpIsCheckList,_tmpReminderTime,_tmpAttachments);
              final ArrayList<CheckListItem> _tmpCheckListItemsCollection;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfNoteId);
              _tmpCheckListItemsCollection = _collectionCheckListItems.get(_tmpKey_2);
              final ArrayList<Tag> _tmpTagsCollection;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfNoteId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_3);
              _result = new DataWithNotesCheckListItemsAndTags(_tmpNote,_tmpCheckListItemsCollection,_tmpTagsCollection);
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
    }, $completion);
  }

  @Override
  public Object getTags(final Continuation<? super List<Tag>> $completion) {
    final String _sql = "SELECT * FROM noteTags";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Tag>>() {
      @Override
      @NonNull
      public List<Tag> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTagId = CursorUtil.getColumnIndexOrThrow(_cursor, "tagId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final List<Tag> _result = new ArrayList<Tag>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Tag _item;
            final long _tmpTagId;
            _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final Long _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            }
            _item = new Tag(_tmpTagId,_tmpName,_tmpColor);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTag(final long id, final Continuation<? super Tag> $completion) {
    final String _sql = "SELECT * FROM noteTags WHERE tagId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Tag>() {
      @Override
      @NonNull
      public Tag call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTagId = CursorUtil.getColumnIndexOrThrow(_cursor, "tagId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final Tag _result;
          if (_cursor.moveToFirst()) {
            final long _tmpTagId;
            _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final Long _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            }
            _result = new Tag(_tmpTagId,_tmpName,_tmpColor);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getChecklistItemsForNoteId(final long noteId,
      final Continuation<? super List<CheckListItem>> $completion) {
    final String _sql = "SELECT * FROM checklist WHERE noteId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, noteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CheckListItem>>() {
      @Override
      @NonNull
      public List<CheckListItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCheckListItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "checkListItemId");
          final int _cursorIndexOfNoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "noteId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfIsChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecked");
          final List<CheckListItem> _result = new ArrayList<CheckListItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CheckListItem _item;
            final long _tmpCheckListItemId;
            _tmpCheckListItemId = _cursor.getLong(_cursorIndexOfCheckListItemId);
            final Long _tmpNoteId;
            if (_cursor.isNull(_cursorIndexOfNoteId)) {
              _tmpNoteId = null;
            } else {
              _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
            }
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final boolean _tmpIsChecked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
            _tmpIsChecked = _tmp != 0;
            _item = new CheckListItem(_tmpCheckListItemId,_tmpNoteId,_tmpText,_tmpIsChecked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipchecklistAscomGitsoftThoughtpadCoreModelCheckListItem(
      @NonNull final LongSparseArray<ArrayList<CheckListItem>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipchecklistAscomGitsoftThoughtpadCoreModelCheckListItem(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `checkListItemId`,`noteId`,`text`,`isChecked` FROM `checklist` WHERE `noteId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "noteId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCheckListItemId = 0;
      final int _cursorIndexOfNoteId = 1;
      final int _cursorIndexOfText = 2;
      final int _cursorIndexOfIsChecked = 3;
      while (_cursor.moveToNext()) {
        final Long _tmpKey;
        if (_cursor.isNull(_itemKeyIndex)) {
          _tmpKey = null;
        } else {
          _tmpKey = _cursor.getLong(_itemKeyIndex);
        }
        if (_tmpKey != null) {
          final ArrayList<CheckListItem> _tmpRelation = _map.get(_tmpKey);
          if (_tmpRelation != null) {
            final CheckListItem _item_1;
            final long _tmpCheckListItemId;
            _tmpCheckListItemId = _cursor.getLong(_cursorIndexOfCheckListItemId);
            final Long _tmpNoteId;
            if (_cursor.isNull(_cursorIndexOfNoteId)) {
              _tmpNoteId = null;
            } else {
              _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
            }
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final boolean _tmpIsChecked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
            _tmpIsChecked = _tmp != 0;
            _item_1 = new CheckListItem(_tmpCheckListItemId,_tmpNoteId,_tmpText,_tmpIsChecked);
            _tmpRelation.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipnoteTagsAscomGitsoftThoughtpadCoreModelTag(
      @NonNull final LongSparseArray<ArrayList<Tag>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipnoteTagsAscomGitsoftThoughtpadCoreModelTag(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `noteTags`.`tagId` AS `tagId`,`noteTags`.`name` AS `name`,`noteTags`.`color` AS `color`,_junction.`noteId` FROM `NoteTagCrossRef` AS _junction INNER JOIN `noteTags` ON (_junction.`tagId` = `noteTags`.`tagId`) WHERE _junction.`noteId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.noteId;
      final int _itemKeyIndex = 3;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfTagId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColor = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Tag> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Tag _item_1;
          final long _tmpTagId;
          _tmpTagId = _cursor.getLong(_cursorIndexOfTagId);
          final String _tmpName;
          if (_cursor.isNull(_cursorIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _cursor.getString(_cursorIndexOfName);
          }
          final Long _tmpColor;
          if (_cursor.isNull(_cursorIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _cursor.getLong(_cursorIndexOfColor);
          }
          _item_1 = new Tag(_tmpTagId,_tmpName,_tmpColor);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
