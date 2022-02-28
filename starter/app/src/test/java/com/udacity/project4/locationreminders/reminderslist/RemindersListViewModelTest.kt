package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var datasource: FakeDataSource

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadReminders_saveReminder() = runBlockingTest {
       val reminder1 = ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34)

        val reminder2 =ReminderDTO("Ayse", "Aysegul","Gaziantep",37.05,37.34)

        datasource.saveReminder(reminder1)
        datasource.saveReminder(reminder2)

        remindersListViewModel.loadReminders()

        val remindersList = remindersListViewModel.remindersList.getOrAwaitValueTest()
        assertThat(remindersList.size, `is`(2))
        assertThat(remindersListViewModel.remindersList.getOrAwaitValueTest(), `is`(remindersList))
        assertThat(remindersList[0].id, `is`(reminder1.id))
        assertThat(remindersList[0].title, `is`(reminder1.title))
        assertThat(remindersList[0].description, `is`(reminder1.description))
        assertThat(remindersList[1].id, `is`(reminder2.id))
        assertThat(remindersList[1].title, `is`(reminder2.title))
        assertThat(remindersList[1].description, `is`(reminder2.description))

        assertThat(remindersListViewModel.remindersList.getOrAwaitValueTest().isEmpty(), `is`(false))
        assertThat(remindersListViewModel.showLoading.getOrAwaitValueTest(), `is`(false))
        assertThat(remindersListViewModel.showNoData.getOrAwaitValueTest(), `is`(false))
    }



}