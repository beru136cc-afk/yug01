package com.example.yugved4.utils

import com.example.yugved4.models.Helpline

/**
 * Data provider for mental health helplines
 */
object HelplineDataProvider {
    
    fun getHelplines(): List<Helpline> {
        return listOf(
            Helpline(
                name = "AASRA",
                description = "24x7 helpline for emotional support and suicide prevention",
                phoneNumber = "9820466726",
                isEmergency = true
            ),
            Helpline(
                name = "iCall",
                description = "Psychosocial helpline by TISS",
                phoneNumber = "9152987821",
                isEmergency = true
            ),
            Helpline(
                name = "Vandrevala Foundation",
                description = "Mental health support and counseling services",
                phoneNumber = "9999776555",
                isEmergency = true
            )
        )
    }
}
