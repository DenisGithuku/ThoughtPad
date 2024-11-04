
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
package com.gitsoft.thoughtpad.feature.tags

import androidx.test.filters.MediumTest
import com.gitsoft.thoughtpad.core.common.MainCoroutineRule
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.google.common.truth.Truth.assertThat
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeNotesRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeUserPrefsRepository
import junit.framework.TestCase.assertTrue
import kotlin.test.assertContains
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class TagViewModelTest {

    @get:Rule val mainCoroutineRule by lazy { MainCoroutineRule() }

    private lateinit var notesRepository: NotesRepository
    private lateinit var userPrefsRepository: UserPrefsRepository
    private lateinit var viewModel: TagViewModel

    @Before
    fun setUp() {
        notesRepository = FakeNotesRepository()
        userPrefsRepository = FakeUserPrefsRepository()
        viewModel = TagViewModel(notesRepository, userPrefsRepository)
    }

    @Test
    fun `test change tag color`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onChangeTagColor(TagColor.Red)
        assertThat(viewModel.state.value.selectedTagColor).isEqualTo(TagColor.Red)
    }

    @Test
    fun `test change tag name`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onChangeTagName("New name")
        assertThat(viewModel.state.value.tagName).isEqualTo("New name")
    }

    @Test
    fun `test save tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onSaveTag(Tag(name = "New tag", color = TagColor.Brown))
        assertContains(viewModel.state.value.tags.map { it.name }, "New tag")
    }

    @Test
    fun `test re-insert tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = viewModel.state.value.tags.first()
        viewModel.onDeleteTag(tag)
        viewModel.onReInsertTag()
        assertContains(viewModel.state.value.tags, tag)
    }

    @Test
    fun `test delete tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = viewModel.state.value.tags.first()
        viewModel.onDeleteTag(tag)
        assertThat(viewModel.state.value.tags).doesNotContain(tag)
    }

    @Test
    fun `test reset deleted tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = viewModel.state.value.tags.first()
        viewModel.onDeleteTag(tag)
        viewModel.onReInsertTag()
        assertNull(viewModel.state.value.deletedTag)
    }

    @Test
    fun `test update tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleEditTag(viewModel.state.value.tags.first())
        viewModel.onEditTagName("Edited name")
        viewModel.onUpdateTag()
        assertTrue(viewModel.state.value.tags.any { it.name == "Edited name" })
    }

    @Test
    fun `test toggle add tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleAddTag(true)
        assertTrue(viewModel.state.value.isAddTag)
    }

    @Test
    fun `test toggle edit tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleEditTag(viewModel.state.value.tags.first())
        assertNotNull(viewModel.state.value.selectedTag)
    }

    @Test
    fun `test edit tag color`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleEditTag(viewModel.state.value.tags.first())
        viewModel.onEditTagColor(TagColor.Blue)
        assertThat(viewModel.state.value.selectedTag!!.color).isEqualTo(TagColor.Blue)
    }

    @Test
    fun `test edit tag name`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleEditTag(viewModel.state.value.tags.first())
        viewModel.onEditTagName("New name")
        assertThat(viewModel.state.value.selectedTag!!.name).isEqualTo("New name")
    }
}
