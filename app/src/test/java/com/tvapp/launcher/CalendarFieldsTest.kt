package com.tvapp.launcher

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for CalendarFields data class
 * These tests verify date/time data integrity
 */
class CalendarFieldsTest {

    @Test
    fun `CalendarFields can be created with valid values`() {
        val calendar = CalendarFields(
            hour = 14,
            minute = 30,
            day = 15,
            month = 6,
            year = 2026
        )

        assertEquals(14, calendar.hour)
        assertEquals(30, calendar.minute)
        assertEquals(15, calendar.day)
        assertEquals(6, calendar.month)
        assertEquals(2026, calendar.year)
    }

    @Test
    fun `CalendarFields handles midnight correctly`() {
        val midnight = CalendarFields(
            hour = 0,
            minute = 0,
            day = 1,
            month = 1,
            year = 2026
        )

        assertEquals(0, midnight.hour)
        assertEquals(0, midnight.minute)
    }

    @Test
    fun `CalendarFields handles end of day correctly`() {
        val endOfDay = CalendarFields(
            hour = 23,
            minute = 59,
            day = 31,
            month = 12,
            year = 2026
        )

        assertEquals(23, endOfDay.hour)
        assertEquals(59, endOfDay.minute)
        assertEquals(31, endOfDay.day)
        assertEquals(12, endOfDay.month)
    }

    @Test
    fun `CalendarFields equality works correctly`() {
        val calendar1 = CalendarFields(14, 30, 15, 6, 2026)
        val calendar2 = CalendarFields(14, 30, 15, 6, 2026)
        val calendar3 = CalendarFields(15, 30, 15, 6, 2026)

        assertEquals(calendar1, calendar2)
        assertNotEquals(calendar1, calendar3)
    }

    @Test
    fun `CalendarFields copy works correctly`() {
        val original = CalendarFields(14, 30, 15, 6, 2026)
        val modified = original.copy(hour = 15)

        assertEquals(15, modified.hour)
        assertEquals(30, modified.minute)
        assertEquals(15, modified.day)
        assertEquals(6, modified.month)
        assertEquals(2026, modified.year)
    }
}
