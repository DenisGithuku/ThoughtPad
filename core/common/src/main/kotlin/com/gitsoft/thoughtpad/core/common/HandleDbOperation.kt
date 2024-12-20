
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.gitsoft.thoughtpad.core.common

import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteAccessPermException
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import android.database.sqlite.SQLiteReadOnlyDatabaseException
import android.os.TransactionTooLargeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend inline fun <T> safeDbCall(crossinline call: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        try {
            val result = call()
            result
        } catch (e: SQLiteConstraintException) {
            // Handle constraint violations (unique, foreign key, etc.)
            Timber.tag("RoomError").e("Constraint violation: ${e.message}")
            // Example: Show a message to the user
            throw e
        } catch (e: SQLiteFullException) {
            // Handle case when the disk is full
            Timber.tag("RoomError").e("Disk full: ${e.message}")
            // Example: Notify the user or free up space
            throw e
        } catch (e: SQLiteReadOnlyDatabaseException) {
            // Handle operations attempted on a read-only database
            Timber.tag("RoomError").e("Database is read-only: ${e.message}")
            // Example: Notify the user that they cannot modify the data
            throw e
        } catch (e: SQLiteCantOpenDatabaseException) {
            // Handle case where the database can't be opened
            Timber.tag("RoomError").e("Unable to open database: ${e.message}")
            // Example: Check if the database file is accessible
            throw e
        } catch (e: SQLiteDiskIOException) {
            // Handle general disk I/O errors
            Timber.tag("RoomError").e("Disk I/O error: ${e.message}")
            // Example: Notify the user or log for future investigation
            throw e
        } catch (e: SQLiteDatabaseCorruptException) {
            // Handle database corruption issues
            Timber.tag("RoomError").e("Database corruption: ${e.message}")
            // Example: Clear the database or restore from a backup
            throw e
        } catch (e: SQLiteAccessPermException) {
            // Handle permission-related errors when accessing the database
            Timber.tag("RoomError").e("Permission error: ${e.message}")
            // Example: Check app permissions or inform the user
            throw e
        } catch (e: TransactionTooLargeException) {
            // Handle cases where the transaction is too large
            Timber.tag("RoomError").e("Transaction too large: ${e.message}")
            // Example: Break the transaction into smaller parts
            throw e
        } catch (e: CursorIndexOutOfBoundsException) {
            // Handle query results that are out of bounds
            Timber.tag("RoomError").e("Cursor out of bounds: ${e.message}")
            // Example: Handle empty results or cursor issues
            throw e
        } catch (e: OutOfMemoryError) {
            // Handle out-of-memory issues during large data operations
            Timber.tag("RoomError").e("Out of memory: ${e.message}")
            // Example: Optimize memory usage or reduce data size
            throw e
        } catch (e: IllegalStateException) {
            // Handle illegal states in transactions or cursor access
            Timber.tag("RoomError").e("Illegal state: ${e.message}")
            // Example: Ensure the transaction is still open
            throw e
        } catch (e: Exception) {
            // Catch-all for any other exceptions
            Timber.tag("RoomError").e("Unexpected error: ${e.message}")
            // Example: Log for further debugging or provide fallback actions
            throw e
        }
    }
}

inline fun <T> Flow<T>.safeDbReactiveDataRead(crossinline fallback: () -> T): Flow<T> {
    return this.catch { e ->
        // Log the specific error based on the exception type
        when (e) {
            is SQLiteConstraintException ->
                Timber.tag("FlowError").e("Constraint violation: ${e.message}")
            is SQLiteFullException -> Timber.tag("FlowError").e("Disk full: ${e.message}")
            // Handle additional specific exceptions as needed
            else -> Timber.tag("FlowError").e("Unknown error: ${e.message}")
        }
        // Emit a fallback value (could be empty list or other logic)
        emit(fallback())
    }
}
