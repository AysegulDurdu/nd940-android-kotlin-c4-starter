package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun setup() {
        stopKoin()
        val remindersList = mutableListOf<ReminderDTO>(
            ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34,),
            ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34,),
            ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34,)
        )

        fakeDataSource = FakeDataSource(remindersList)
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)

    }

    @Test
    fun saveReminder_dataSource() = mainCoroutineRule.runBlockingTest {

        fakeDataSource.deleteAllReminders()

        var reminderData = ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34)

        viewModel.saveReminder(reminderData)

        val reminderFromDataSource = (fakeDataSource.getReminders() as Result.Success).data

        assertThat(reminderData.title, equalTo(reminderFromDataSource[0].title))
    }
}