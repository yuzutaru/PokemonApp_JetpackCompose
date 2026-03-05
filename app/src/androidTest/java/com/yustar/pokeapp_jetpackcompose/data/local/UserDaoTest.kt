package com.yustar.pokeapp_jetpackcompose.data.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: UserDB
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Use an in-memory database for testing
        db = Room.inMemoryDatabaseBuilder(context, UserDB::class.java)
            .allowMainThreadQueries() // Only for tests
            .build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun insertAndGetUser_returnsCorrectData() = runBlocking {
        val user = User(username = "ash_ketchum", password = "password123")
        userDao.insertUser(user)

        val retrievedUser = userDao.getUserByUsername("ash_ketchum")

        Assert.assertEquals("Username should match", user.username, retrievedUser?.username)
        Assert.assertEquals("Password should match", user.password, retrievedUser?.password)
    }

    @Test
    fun getUserByUsername_returnsNullIfUserDoesNotExist() = runBlocking {
        val retrievedUser = userDao.getUserByUsername("non_existent")
        Assert.assertNull(retrievedUser)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertDuplicateUsername_throwsException() = runBlocking {
        val user1 = User(username = "ash_ketchum", password = "password123")
        val user2 = User(username = "ash_ketchum", password = "different_password")

        userDao.insertUser(user1)

        // This should throw SQLiteConstraintException because 'username' is the PrimaryKey
        // and the default Room @Insert conflict strategy is ABORT (unless specified otherwise)
        userDao.insertUser(user2)
    }
}