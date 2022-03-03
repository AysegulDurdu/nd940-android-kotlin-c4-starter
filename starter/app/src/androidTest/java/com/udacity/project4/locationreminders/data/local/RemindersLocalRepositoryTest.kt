package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var db: RemindersDatabase
    private lateinit var repo: RemindersLocalRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private  val reminder = ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34,"1")

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RemindersDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repo = RemindersLocalRepository(db.reminderDao(), Dispatchers.Main)
    }


    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun saveRemindercheckEqualRepo() = runBlocking {
        //GIVE
        repo.saveReminder(reminder)

        //WHEN
        val result = (repo.getReminders() as Result.Success).data

        // THEN
        assertThat(result[0].id, `is`(reminder.id))
        assertThat(result[0].title, `is`(reminder.title))
        assertThat(result[0].description, `is`(reminder.description))
        assertThat(result[0].latitude, `is`(reminder.latitude))
        assertThat(result[0].longitude, `is`(reminder.longitude))
        assertThat(result[0].location, `is`(reminder.location))
    }

    @Test
    fun deleteAllReminders() = runBlocking {
        //GIVE
        repo.saveReminder(reminder)

        var result = (repo.getReminders() as Result.Success).data

        repo.deleteAllReminders()

        //WHEN
         result = (repo.getReminders() as Result.Success).data

        //THEN
        assertThat(result, `is`(emptyList()))
    }



}