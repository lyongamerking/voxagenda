package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AgendaEvent
import com.example.data.DoodleDrawing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("VoxAgenda", appName)
    }

    @Test
    fun `doodle json serialization and deserialization`() {
        val originalDoodle = DoodleDrawing.createSampleCake()
        val json = originalDoodle.toJson()
        val restored = DoodleDrawing.fromJson(json)

        assertEquals(originalDoodle.strokes.size, restored.strokes.size)
        assertEquals(originalDoodle.stamps.size, restored.stamps.size)
        assertEquals(originalDoodle.bgColorHex, restored.bgColorHex)
    }

    @Test
    fun `agenda event voice message generation`() {
        val event = AgendaEvent(
            title = "Cumpleaños de Mamá",
            dateTimeEpochMs = 1700000000000L,
            voicePersona = "Cálida"
        )
        val voiceMsg = event.getEffectiveVoiceMessage()
        assertTrue(voiceMsg.contains("Cumpleaños de Mamá"))
        assertTrue(voiceMsg.contains("Aura"))
    }

    @Test
    fun `agenda event supports multiple visual types`() {
        val photoEvent = AgendaEvent(
            title = "Viaje a Italia",
            dateTimeEpochMs = 1700000000000L,
            visualType = "PHOTO",
            photoUri = "content://media/external/images/media/42"
        )
        assertEquals("PHOTO", photoEvent.visualType)
        assertEquals("content://media/external/images/media/42", photoEvent.photoUri)

        val aiArtEvent = AgendaEvent(
            title = "Graduación",
            dateTimeEpochMs = 1700000000000L,
            visualType = "AI_ART",
            aiArtStyle = "Celebración & Acuarela"
        )
        assertEquals("AI_ART", aiArtEvent.visualType)
    }
}
