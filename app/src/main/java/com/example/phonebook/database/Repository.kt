package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NoteModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(
    private val contactDao: ContactDao,
    private val tagDao: TagDao,
    private val dbMapper: DbMapper
) {

    // Working Notes
    private val notesNotInTrashLiveData: MutableLiveData<List<NoteModel>> by lazy {
        MutableLiveData<List<NoteModel>>()
    }

    fun getAllNotesNotInTrash(): LiveData<List<NoteModel>> = notesNotInTrashLiveData

    // Deleted Notes
    private val notesInTrashLiveData: MutableLiveData<List<NoteModel>> by lazy {
        MutableLiveData<List<NoteModel>>()
    }

    fun getAllNotesInTrash(): LiveData<List<NoteModel>> = notesInTrashLiveData

    init {
        initDatabase(this::updateNotesLiveData)
    }

    /**
     * Populates database with colors if it is empty.
     */
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            // Prepopulate colors
            val colors = TagDbModel.DEFAULT_COLORS.toTypedArray()
            val dbColors = tagDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                tagDao.insertAll(*colors)
            }

            // Prepopulate notes
            val notes = ContactDbModel.DEFAULT_NOTES.toTypedArray()
            val dbNotes = contactDao.getAllSync()
            if (dbNotes.isNullOrEmpty()) {
                contactDao.insertAll(*notes)
            }

            postInitAction.invoke()
        }
    }

    // get list of working notes or deleted notes
    private fun getAllNotesDependingOnTrashStateSync(inTrash: Boolean): List<NoteModel> {
        val tagDbModels: Map<Long, TagDbModel> = tagDao.getAllSync().map { it.id to it }.toMap()
        val dbNotes: List<ContactDbModel> =
            contactDao.getAllSync().filter { it.isInTrash == inTrash }
        return dbMapper.mapNotes(dbNotes, tagDbModels)
    }

    fun insertNote(note: NoteModel) {
        contactDao.insert(dbMapper.mapDbNote(note))
        updateNotesLiveData()
    }

    fun deleteNotes(noteIds: List<Long>) {
        contactDao.delete(noteIds)
        updateNotesLiveData()
    }

    fun moveNoteToTrash(noteId: Long) {
        val dbNote = contactDao.findByIdSync(noteId)
        val newDbNote = dbNote.copy(isInTrash = true)
        contactDao.insert(newDbNote)
        updateNotesLiveData()
    }

    fun restoreNotesFromTrash(noteIds: List<Long>) {
        val dbNotesInTrash = contactDao.getNotesByIdsSync(noteIds)
        dbNotesInTrash.forEach {
            val newDbNote = it.copy(isInTrash = false)
            contactDao.insert(newDbNote)
        }
        updateNotesLiveData()
    }

    fun getAllColors(): LiveData<List<ColorModel>> =
        Transformations.map(tagDao.getAll()) { dbMapper.mapColors(it) }

    private fun updateNotesLiveData() {
        notesNotInTrashLiveData.postValue(getAllNotesDependingOnTrashStateSync(false))
        notesInTrashLiveData.postValue(getAllNotesDependingOnTrashStateSync(true))
    }
}