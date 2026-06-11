package com.germandebustamante.ringtonemanager.data.local.db.mapper

import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.data.local.db.entity.RingtoneEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RingtoneEntityMapperTest {

    @Test
    fun `GIVEN RingtoneBO WHEN mapped to entity THEN all fields preserved`() {
        val bo = RingtoneBOMother.default()
        val entity = bo.toEntity()

        assertEquals(bo.id, entity.id)
        assertEquals(bo.name, entity.name)
        assertEquals(bo.artist, entity.artist)
        assertEquals(bo.source, entity.source)
        assertEquals(bo.fileUrl, entity.fileUrl)
        assertEquals(bo.popularity, entity.popularity)
    }

    @Test
    fun `GIVEN RingtoneEntity WHEN mapped to domain THEN all fields preserved`() {
        val entity = RingtoneEntity(
            id = "1",
            name = "Test",
            artist = "Artist",
            source = "Source",
            fileUrl = "https://example.com/file.mp3",
            popularity = 42,
        )
        val bo = entity.toDomain()

        assertEquals(entity.id, bo.id)
        assertEquals(entity.name, bo.name)
        assertEquals(entity.artist, bo.artist)
        assertEquals(entity.source, bo.source)
        assertEquals(entity.fileUrl, bo.fileUrl)
        assertEquals(entity.popularity, bo.popularity)
    }

    @Test
    fun `GIVEN BO mapped to entity and back THEN round-trip is lossless`() {
        val original = RingtoneBOMother.random("rt-1")
        val roundTripped = original.toEntity().toDomain()

        assertEquals(original, roundTripped)
    }
}
