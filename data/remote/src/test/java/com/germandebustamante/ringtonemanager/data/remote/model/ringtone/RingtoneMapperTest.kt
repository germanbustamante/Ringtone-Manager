package com.germandebustamante.ringtonemanager.data.remote.model.ringtone

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RingtoneMapperTest {

    @Nested
    inner class ToDomain {

        @Test
        fun `GIVEN complete RingtoneDTO WHEN mapping to domain THEN all fields are correctly mapped`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id-123",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals("test-id-123", result.id)
            assertEquals("Test Ringtone", result.name)
            assertEquals("Test Artist", result.artist)
            assertEquals("Test Source", result.source)
            assertEquals("https://example.com/ringtone.mp3", result.fileUrl)
            assertEquals(100, result.popularity)
        }

        @Test
        fun `GIVEN RingtoneDTO with null id WHEN mapping to domain THEN id is converted to string null`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = null,
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals("null", result.id)
        }

        @Test
        fun `GIVEN RingtoneDTO with null name WHEN mapping to domain THEN name is empty string`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = null,
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals("", result.name)
        }

        @Test
        fun `GIVEN RingtoneDTO with null artist WHEN mapping to domain THEN artist is null`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = null,
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals(null, result.artist)
        }

        @Test
        fun `GIVEN RingtoneDTO with null source WHEN mapping to domain THEN source is null`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = null,
                file_url = "https://example.com/ringtone.mp3",
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals(null, result.source)
        }

        @Test
        fun `GIVEN RingtoneDTO with null file_url WHEN mapping to domain THEN fileUrl is empty string`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = null,
                popularity = 100
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals("", result.fileUrl)
        }

        @Test
        fun `GIVEN RingtoneDTO with null popularity WHEN mapping to domain THEN popularity is 0`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = null
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals(0, result.popularity)
        }

        @Test
        fun `GIVEN RingtoneDTO with all null values WHEN mapping to domain THEN returns valid domain object with defaults`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = null,
                name = null,
                artist = null,
                source = null,
                file_url = null,
                popularity = null
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals("null", result.id)
            assertEquals("", result.name)
            assertEquals(null, result.artist)
            assertEquals(null, result.source)
            assertEquals("", result.fileUrl)
            assertEquals(0, result.popularity)
        }

        @Test
        fun `GIVEN RingtoneDTO with zero popularity WHEN mapping to domain THEN popularity is preserved as 0`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = 0
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals(0, result.popularity)
        }

        @Test
        fun `GIVEN RingtoneDTO with negative popularity WHEN mapping to domain THEN negative popularity is preserved`() {
            // Given
            val ringtoneDTO = RingtoneDTO(
                id = "test-id",
                name = "Test Ringtone",
                artist = "Test Artist",
                source = "Test Source",
                file_url = "https://example.com/ringtone.mp3",
                popularity = -5
            )

            // When
            val result = ringtoneDTO.toDomain()

            // Then
            assertEquals(-5, result.popularity)
        }
    }
}
