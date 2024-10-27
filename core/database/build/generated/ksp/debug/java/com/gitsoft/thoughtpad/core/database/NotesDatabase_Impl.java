package com.gitsoft.thoughtpad.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotesDatabase_Impl extends NotesDatabase {
  private volatile NotesDatabaseDao _notesDatabaseDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `notes_table` (`noteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `noteTitle` TEXT, `noteText` TEXT, `createdAt` INTEGER, `updatedAt` INTEGER, `isPinned` INTEGER NOT NULL, `isArchived` INTEGER NOT NULL, `color` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `isCheckList` INTEGER NOT NULL, `reminderTime` INTEGER, `attachments` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `noteTags` (`tagId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `color` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `checklist` (`checkListItemId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `noteId` INTEGER, `text` TEXT, `isChecked` INTEGER NOT NULL, FOREIGN KEY(`noteId`) REFERENCES `notes_table`(`noteId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_checklist_noteId` ON `checklist` (`noteId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `NoteTagCrossRef` (`noteId` INTEGER NOT NULL, `tagId` INTEGER NOT NULL, PRIMARY KEY(`noteId`, `tagId`), FOREIGN KEY(`noteId`) REFERENCES `notes_table`(`noteId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`tagId`) REFERENCES `noteTags`(`tagId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_NoteTagCrossRef_tagId` ON `NoteTagCrossRef` (`tagId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a2f2bd43cddd91cce007e5296d9d56b8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `notes_table`");
        db.execSQL("DROP TABLE IF EXISTS `noteTags`");
        db.execSQL("DROP TABLE IF EXISTS `checklist`");
        db.execSQL("DROP TABLE IF EXISTS `NoteTagCrossRef`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsNotesTable = new HashMap<String, TableInfo.Column>(12);
        _columnsNotesTable.put("noteId", new TableInfo.Column("noteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("noteTitle", new TableInfo.Column("noteTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("noteText", new TableInfo.Column("noteText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("isPinned", new TableInfo.Column("isPinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("isCheckList", new TableInfo.Column("isCheckList", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("reminderTime", new TableInfo.Column("reminderTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotesTable.put("attachments", new TableInfo.Column("attachments", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotesTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotesTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotesTable = new TableInfo("notes_table", _columnsNotesTable, _foreignKeysNotesTable, _indicesNotesTable);
        final TableInfo _existingNotesTable = TableInfo.read(db, "notes_table");
        if (!_infoNotesTable.equals(_existingNotesTable)) {
          return new RoomOpenHelper.ValidationResult(false, "notes_table(com.gitsoft.thoughtpad.core.model.Note).\n"
                  + " Expected:\n" + _infoNotesTable + "\n"
                  + " Found:\n" + _existingNotesTable);
        }
        final HashMap<String, TableInfo.Column> _columnsNoteTags = new HashMap<String, TableInfo.Column>(3);
        _columnsNoteTags.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNoteTags.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNoteTags.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNoteTags = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNoteTags = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNoteTags = new TableInfo("noteTags", _columnsNoteTags, _foreignKeysNoteTags, _indicesNoteTags);
        final TableInfo _existingNoteTags = TableInfo.read(db, "noteTags");
        if (!_infoNoteTags.equals(_existingNoteTags)) {
          return new RoomOpenHelper.ValidationResult(false, "noteTags(com.gitsoft.thoughtpad.core.model.Tag).\n"
                  + " Expected:\n" + _infoNoteTags + "\n"
                  + " Found:\n" + _existingNoteTags);
        }
        final HashMap<String, TableInfo.Column> _columnsChecklist = new HashMap<String, TableInfo.Column>(4);
        _columnsChecklist.put("checkListItemId", new TableInfo.Column("checkListItemId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklist.put("noteId", new TableInfo.Column("noteId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklist.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklist.put("isChecked", new TableInfo.Column("isChecked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChecklist = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChecklist.add(new TableInfo.ForeignKey("notes_table", "CASCADE", "NO ACTION", Arrays.asList("noteId"), Arrays.asList("noteId")));
        final HashSet<TableInfo.Index> _indicesChecklist = new HashSet<TableInfo.Index>(1);
        _indicesChecklist.add(new TableInfo.Index("index_checklist_noteId", false, Arrays.asList("noteId"), Arrays.asList("ASC")));
        final TableInfo _infoChecklist = new TableInfo("checklist", _columnsChecklist, _foreignKeysChecklist, _indicesChecklist);
        final TableInfo _existingChecklist = TableInfo.read(db, "checklist");
        if (!_infoChecklist.equals(_existingChecklist)) {
          return new RoomOpenHelper.ValidationResult(false, "checklist(com.gitsoft.thoughtpad.core.model.CheckListItem).\n"
                  + " Expected:\n" + _infoChecklist + "\n"
                  + " Found:\n" + _existingChecklist);
        }
        final HashMap<String, TableInfo.Column> _columnsNoteTagCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsNoteTagCrossRef.put("noteId", new TableInfo.Column("noteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNoteTagCrossRef.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNoteTagCrossRef = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysNoteTagCrossRef.add(new TableInfo.ForeignKey("notes_table", "CASCADE", "NO ACTION", Arrays.asList("noteId"), Arrays.asList("noteId")));
        _foreignKeysNoteTagCrossRef.add(new TableInfo.ForeignKey("noteTags", "CASCADE", "NO ACTION", Arrays.asList("tagId"), Arrays.asList("tagId")));
        final HashSet<TableInfo.Index> _indicesNoteTagCrossRef = new HashSet<TableInfo.Index>(1);
        _indicesNoteTagCrossRef.add(new TableInfo.Index("index_NoteTagCrossRef_tagId", false, Arrays.asList("tagId"), Arrays.asList("ASC")));
        final TableInfo _infoNoteTagCrossRef = new TableInfo("NoteTagCrossRef", _columnsNoteTagCrossRef, _foreignKeysNoteTagCrossRef, _indicesNoteTagCrossRef);
        final TableInfo _existingNoteTagCrossRef = TableInfo.read(db, "NoteTagCrossRef");
        if (!_infoNoteTagCrossRef.equals(_existingNoteTagCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "NoteTagCrossRef(com.gitsoft.thoughtpad.core.model.NoteTagCrossRef).\n"
                  + " Expected:\n" + _infoNoteTagCrossRef + "\n"
                  + " Found:\n" + _existingNoteTagCrossRef);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a2f2bd43cddd91cce007e5296d9d56b8", "4ceb70173c1573f219e4be5c3fd7e959");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "notes_table","noteTags","checklist","NoteTagCrossRef");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `notes_table`");
      _db.execSQL("DELETE FROM `noteTags`");
      _db.execSQL("DELETE FROM `checklist`");
      _db.execSQL("DELETE FROM `NoteTagCrossRef`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NotesDatabaseDao.class, NotesDatabaseDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public NotesDatabaseDao dao() {
    if (_notesDatabaseDao != null) {
      return _notesDatabaseDao;
    } else {
      synchronized(this) {
        if(_notesDatabaseDao == null) {
          _notesDatabaseDao = new NotesDatabaseDao_Impl(this);
        }
        return _notesDatabaseDao;
      }
    }
  }
}
