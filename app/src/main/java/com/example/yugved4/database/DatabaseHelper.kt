package com.example.yugved4.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.yugved4.models.Doctor
import com.example.yugved4.models.UserProfile

/**
 * SQLiteOpenHelper for Doctor Database
 * Uses raw SQL statements for database operations
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "yugved.db"
        private const val DATABASE_VERSION = 5  // Incremented for helplines table
        
        // Doctors table and columns
        private const val TABLE_DOCTORS = "doctors"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SPECIALTY = "specialty"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        
        // User profile table and columns
        private const val TABLE_USER_PROFILE = "user_profile"
        private const val COLUMN_PROFILE_ID = "id"
        private const val COLUMN_PROFILE_NAME = "name"
        private const val COLUMN_TARGET_CALORIES = "target_calories"
        private const val COLUMN_CURRENT_WEIGHT = "current_weight"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_ACTIVITY_LEVEL = "activity_level"
        private const val COLUMN_DIET_PREFERENCE = "diet_preference"
        private const val COLUMN_HEIGHT = "height"
        
        // Mental health content table and columns
        private const val TABLE_MENTAL_HEALTH = "mental_health_content"
        private const val COLUMN_MH_ID = "id"
        private const val COLUMN_MH_TITLE = "title"
        private const val COLUMN_MH_TYPE = "type"  // Video/Tip
        private const val COLUMN_MH_CATEGORY = "category"  // Meditation/Stress/Sleep
        private const val COLUMN_MH_CONTENT_DATA = "content_data"  // Link or Description
        
        // Helplines table and columns
        private const val TABLE_HELPLINES = "helplines"
        private const val COLUMN_HELPLINE_ID = "id"
        private const val COLUMN_HELPLINE_NAME = "name"
        private const val COLUMN_HELPLINE_NUMBER = "number"
        private const val COLUMN_HELPLINE_DESCRIPTION = "description"
    }

    /**
     * Create database table
     */
    override fun onCreate(db: SQLiteDatabase) {
        // Create doctors table
        val createDoctorsTable = """
            CREATE TABLE $TABLE_DOCTORS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_SPECIALTY TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()
        
        // Create user profile table
        val createProfileTable = """
            CREATE TABLE $TABLE_USER_PROFILE (
                $COLUMN_PROFILE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PROFILE_NAME TEXT,
                $COLUMN_TARGET_CALORIES INTEGER NOT NULL,
                $COLUMN_CURRENT_WEIGHT REAL NOT NULL,
                $COLUMN_AGE INTEGER,
                $COLUMN_GENDER TEXT,
                $COLUMN_ACTIVITY_LEVEL TEXT,
                $COLUMN_DIET_PREFERENCE TEXT,
                $COLUMN_HEIGHT REAL
            )
        """.trimIndent()
        
        // Create mental health content table
        val createMentalHealthTable = """
            CREATE TABLE $TABLE_MENTAL_HEALTH (
                $COLUMN_MH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MH_TITLE TEXT NOT NULL,
                $COLUMN_MH_TYPE TEXT NOT NULL,
                $COLUMN_MH_CATEGORY TEXT NOT NULL,
                $COLUMN_MH_CONTENT_DATA TEXT NOT NULL
            )
        """.trimIndent()
        
        // Create helplines table
        val createHelplinesTable = """
            CREATE TABLE $TABLE_HELPLINES (
                $COLUMN_HELPLINE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_HELPLINE_NAME TEXT NOT NULL,
                $COLUMN_HELPLINE_NUMBER TEXT NOT NULL,
                $COLUMN_HELPLINE_DESCRIPTION TEXT NOT NULL
            )
        """.trimIndent()
        
        db.execSQL(createDoctorsTable)
        db.execSQL(createProfileTable)
        db.execSQL(createMentalHealthTable)
        db.execSQL(createHelplinesTable)
        
        // Insert seed data for mental health content
        insertMentalHealthSeedData(db)
        
        // Insert seed data for helplines
        insertHelplinesSeedData(db)
    }

    /**
     * Upgrade database (drop and recreate)
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DOCTORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_PROFILE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MENTAL_HEALTH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HELPLINES")
        onCreate(db)
    }

    /**
     * Add a new doctor to database
     * @return row ID of newly inserted doctor, or -1 if error
     */
    fun addDoctor(name: String, specialty: String, phone: String, email: String? = null): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SPECIALTY, specialty)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
        }
        
        val id = db.insert(TABLE_DOCTORS, null, values)
        db.close()
        return id
    }

    /**
     * Get all doctors from database using raw SQL query
     * @return List of Doctor objects
     */
    fun getAllDoctors(): List<Doctor> {
        val doctorList = mutableListOf<Doctor>()
        val db = readableDatabase
        
        db.rawQuery("SELECT * FROM $TABLE_DOCTORS", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                    val specialty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALTY))
                    val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                    
                    val doctor = Doctor(
                        id = id.toString(),
                        name = name,
                        specialty = specialty,
                        phoneNumber = phone,
                        email = email
                    )
                    doctorList.add(doctor)
                } while (cursor.moveToNext())
            }
        }
        
        db.close()
        return doctorList
    }

    /**
     * Update an existing doctor
     * @return number of rows affected
     */
    fun updateDoctor(id: String, name: String, specialty: String, phone: String, email: String? = null): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SPECIALTY, specialty)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
        }
        
        val rowsAffected = db.update(TABLE_DOCTORS, values, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return rowsAffected
    }

    /**
     * Delete a doctor from database
     * @return number of rows deleted
     */
    fun deleteDoctor(id: String): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_DOCTORS, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return rowsDeleted
    }

    /**
     * Get doctor count
     */
    fun getDoctorCount(): Int {
        val db = readableDatabase
        var count = 0
        
        db.rawQuery("SELECT COUNT(*) FROM $TABLE_DOCTORS", null).use { cursor ->
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        }
        
        db.close()
        return count
    }

    /**
     * Delete all doctors
     */
    fun deleteAllDoctors(): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_DOCTORS, null, null)
        db.close()
        return rowsDeleted
    }
    
    // =============== USER PROFILE METHODS ===============
    
    /**
     * Save user profile (diet data)
     * @return row ID or -1 if error
     */
    fun saveUserProfile(
        targetCalories: Int,
        currentWeight: Double,
        age: Int? = null,
        gender: String? = null,
        activityLevel: String? = null,
        dietPreference: String? = null,
        height: Double? = null,
        name: String? = null
    ): Long {
        val db = writableDatabase
        
        // Delete existing profile (single user app)
        db.delete(TABLE_USER_PROFILE, null, null)
        
        val values = ContentValues().apply {
            put(COLUMN_PROFILE_NAME, name)
            put(COLUMN_TARGET_CALORIES, targetCalories)
            put(COLUMN_CURRENT_WEIGHT, currentWeight)
            put(COLUMN_AGE, age)
            put(COLUMN_GENDER, gender)
            put(COLUMN_ACTIVITY_LEVEL, activityLevel)
            put(COLUMN_DIET_PREFERENCE, dietPreference)
            put(COLUMN_HEIGHT, height)
        }
        
        val id = db.insert(TABLE_USER_PROFILE, null, values)
        db.close()
        return id
    }
    
    /**
     * Get user profile from database
     * @return UserProfile object or null if not found
     */
    fun getUserProfile(): UserProfile? {
        val db = readableDatabase
        var profile: UserProfile? = null
        
        db.rawQuery("SELECT * FROM $TABLE_USER_PROFILE LIMIT 1", null).use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_NAME))
                val targetCalories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TARGET_CALORIES))
                val currentWeight = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_WEIGHT))
                val age = if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_AGE))) {
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                } else null
                val gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER))
                val activityLevel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_LEVEL))
                val dietPreference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIET_PREFERENCE))
                val height = if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT))) {
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT))
                } else null
                
                profile = UserProfile(
                    id = id,
                    name = name,
                    targetCalories = targetCalories,
                    currentWeight = currentWeight,
                    age = age,
                    gender = gender,
                    activityLevel = activityLevel,
                    dietPreference = dietPreference,
                    height = height
                )
            }
        }
        
        db.close()
        return profile
    }
    
    /**
     * Check if user profile exists
     * @return true if profile exists, false otherwise
     */
    fun hasUserProfile(): Boolean {
        val db = readableDatabase
        var hasProfile = false
        
        db.rawQuery("SELECT COUNT(*) FROM $TABLE_USER_PROFILE", null).use { cursor ->
            if (cursor.moveToFirst()) {
                hasProfile = cursor.getInt(0) > 0
            }
        }
        
        db.close()
        return hasProfile
    }
    
    /**
     * Update user weight
     */
    fun updateWeight(newWeight: Double): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CURRENT_WEIGHT, newWeight)
        }
        
        val rowsAffected = db.update(TABLE_USER_PROFILE, values, null, null)
        db.close()
        return rowsAffected
    }
    
    /**
     * Update user profile with all editable fields
     * @return number of rows affected
     */
    fun updateUserProfile(
        name: String?,
        age: Int,
        height: Double,
        weight: Double
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROFILE_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_HEIGHT, height)
            put(COLUMN_CURRENT_WEIGHT, weight)
        }
        
        val rowsAffected = db.update(TABLE_USER_PROFILE, values, null, null)
        db.close()
        return rowsAffected
    }
    
    /**
     * Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor formula
     * @param age User's age in years
     * @param gender User's gender ("Male" or "Female")
     * @param height User's height in cm
     * @param weight User's weight in kg
     * @return BMR in calories
     */
    private fun calculateBMR(age: Int, gender: String?, height: Double, weight: Double): Double {
        return if (gender == "Male") {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }
    }
    
    /**
     * Update target calories based on current profile data
     * Recalculates BMR and applies activity level multiplier
     * @return number of rows affected
     */
    fun updateTargetCalories(): Int {
        val profile = getUserProfile() ?: return 0
        
        // Ensure we have necessary data
        val age = profile.age ?: return 0
        val height = profile.height ?: return 0
        val weight = profile.currentWeight
        val gender = profile.gender
        val activityLevel = profile.activityLevel ?: "Sedentary"
        
        // Calculate BMR
        val bmr = calculateBMR(age, gender, height, weight)
        
        // Activity level multipliers
        val multiplier = when {
            activityLevel.contains("Sedentary", ignoreCase = true) -> 1.2
            activityLevel.contains("Lightly", ignoreCase = true) -> 1.375
            activityLevel.contains("Moderately", ignoreCase = true) -> 1.55
            activityLevel.contains("Very", ignoreCase = true) -> 1.725
            activityLevel.contains("Extra", ignoreCase = true) -> 1.9
            else -> 1.2
        }
        
        val targetCalories = (bmr * multiplier).toInt()
        
        // Update target calories in database
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TARGET_CALORIES, targetCalories)
        }
        
        val rowsAffected = db.update(TABLE_USER_PROFILE, values, null, null)
        db.close()
        return rowsAffected
    }

    // ===========================
    // Workout Statistics
    // ===========================
    
    /**
     * Get workout statistics (counts of gym exercises and yoga asanas)
     * 
     * NOTE: Currently, gym exercises and yoga asanas are stored in DataProviders
     * (static code), not in SQL tables. This method returns counts from those sources.
     * If you want to store exercises in SQL, you'll need to create tables and migrate the data.
     * 
     * @return WorkoutStats object with gym and yoga counts
     */
    fun getWorkoutStats(): com.example.yugved4.models.WorkoutStats {
        // Gym exercises count (from GymFragment hardcoded categories)
        // Categories: Chest(12), Back(10), Legs(15), Shoulders(8), Arms(14), Core(11)
        val gymExerciseCount = 12 + 10 + 15 + 8 + 14 + 11 // Total: 70 exercises
        
        // Yoga asanas count (from YogaDataProvider)
        val yogaAsanaCount = com.example.yugved4.utils.YogaDataProvider.getAllAsanas().size
        
        return com.example.yugved4.models.WorkoutStats(
            gymExerciseCount = gymExerciseCount,
            yogaAsanaCount = yogaAsanaCount
        )
    }
    
    // =============== MENTAL HEALTH CONTENT METHODS ===============
    
    /**
     * Insert seed data for mental health content
     * This method is called during database creation
     */
    private fun insertMentalHealthSeedData(db: SQLiteDatabase) {
        // Sample record 1: 5-Minute Breathing meditation video
        db.execSQL("""
            INSERT INTO $TABLE_MENTAL_HEALTH 
            ($COLUMN_MH_TITLE, $COLUMN_MH_TYPE, $COLUMN_MH_CATEGORY, $COLUMN_MH_CONTENT_DATA) 
            VALUES 
            ('5-Minute Breathing Exercise', 'Video', 'Meditation', 'https://www.youtube.com/watch?v=tybOi4hjZFQ')
        """)
        
        // Sample record 2: Better Sleep tip
        db.execSQL("""
            INSERT INTO $TABLE_MENTAL_HEALTH 
            ($COLUMN_MH_TITLE, $COLUMN_MH_TYPE, $COLUMN_MH_CATEGORY, $COLUMN_MH_CONTENT_DATA) 
            VALUES 
            ('Better Sleep Tips', 'Tip', 'Sleep', 'Maintain a consistent sleep schedule by going to bed and waking up at the same time every day. Avoid screens 1 hour before bedtime and create a relaxing bedtime routine.')
        """)
        
        // Sample record 3: Stress Relief guide
        db.execSQL("""
            INSERT INTO $TABLE_MENTAL_HEALTH 
            ($COLUMN_MH_TITLE, $COLUMN_MH_TYPE, $COLUMN_MH_CATEGORY, $COLUMN_MH_CONTENT_DATA) 
            VALUES 
            ('Stress Relief Guide', 'Tip', 'Stress', 'Practice deep breathing: Inhale for 4 counts, hold for 4, exhale for 4. Take short walks in nature. Journal your thoughts and feelings. Limit caffeine and prioritize self-care activities.')
        """)
    }
    
    /**
     * Get mental health content by category using Cursor
     * @param category The category to filter by (Meditation/Stress/Sleep)
     * @return List of MentalHealthContent objects
     */
    fun getMentalHealthContent(category: String): List<MentalHealthContent> {
        val contentList = mutableListOf<MentalHealthContent>()
        val db = readableDatabase
        
        // Raw SQL query with WHERE clause
        val query = "SELECT * FROM $TABLE_MENTAL_HEALTH WHERE $COLUMN_MH_CATEGORY = ?"
        
        db.rawQuery(query, arrayOf(category)).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MH_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_TITLE))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_TYPE))
                    val contentCategory = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_CATEGORY))
                    val contentData = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_CONTENT_DATA))
                    
                    val content = MentalHealthContent(
                        id = id,
                        title = title,
                        type = type,
                        category = contentCategory,
                        contentData = contentData
                    )
                    contentList.add(content)
                } while (cursor.moveToNext())
            }
        }
        
        db.close()
        return contentList
    }
    
    /**
     * Get all mental health content (no filter)
     * @return List of all MentalHealthContent objects
     */
    fun getAllMentalHealthContent(): List<MentalHealthContent> {
        val contentList = mutableListOf<MentalHealthContent>()
        val db = readableDatabase
        
        db.rawQuery("SELECT * FROM $TABLE_MENTAL_HEALTH", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MH_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_TITLE))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_TYPE))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_CATEGORY))
                    val contentData = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MH_CONTENT_DATA))
                    
                    val content = MentalHealthContent(
                        id = id,
                        title = title,
                        type = type,
                        category = category,
                        contentData = contentData
                    )
                    contentList.add(content)
                } while (cursor.moveToNext())
            }
        }
        
        db.close()
        return contentList
    }
    
    /**
     * Data class for Mental Health Content
     */
    data class MentalHealthContent(
        val id: Int,
        val title: String,
        val type: String,  // Video or Tip
        val category: String,  // Meditation, Stress, Sleep
        val contentData: String  // Link or Description
    )
    
    // =============== HELPLINES METHODS ===============
    
    /**
     * Insert seed data for helplines
     * This method is called during database creation
     */
    private fun insertHelplinesSeedData(db: SQLiteDatabase) {
        // AASRA Helpline
        db.execSQL("""
            INSERT INTO $TABLE_HELPLINES 
            ($COLUMN_HELPLINE_NAME, $COLUMN_HELPLINE_NUMBER, $COLUMN_HELPLINE_DESCRIPTION) 
            VALUES 
            ('AASRA', '9820466726', '24x7 helpline for emotional support and suicide prevention')
        """)
        
        // iCall Helpline
        db.execSQL("""
            INSERT INTO $TABLE_HELPLINES 
            ($COLUMN_HELPLINE_NAME, $COLUMN_HELPLINE_NUMBER, $COLUMN_HELPLINE_DESCRIPTION) 
            VALUES 
            ('iCall', '9152987821', 'Psychosocial helpline by TISS for mental health support')
        """)
        
        // Vandrevala Foundation
        db.execSQL("""
            INSERT INTO $TABLE_HELPLINES 
            ($COLUMN_HELPLINE_NAME, $COLUMN_HELPLINE_NUMBER, $COLUMN_HELPLINE_DESCRIPTION) 
            VALUES 
            ('Vandrevala Foundation', '9999776555', 'Mental health support and counseling services')
        """)
    }
    
    /**
     * Get all helplines from database using raw SQL query
     * @return List of Helpline objects
     */
    fun getAllHelplines(): List<HelplineData> {
        val helplineList = mutableListOf<HelplineData>()
        val db = readableDatabase
        
        db.rawQuery("SELECT * FROM $TABLE_HELPLINES", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HELPLINE_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HELPLINE_NAME))
                    val number = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HELPLINE_NUMBER))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HELPLINE_DESCRIPTION))
                    
                    val helpline = HelplineData(
                        id = id,
                        name = name,
                        number = number,
                        description = description
                    )
                    helplineList.add(helpline)
                } while (cursor.moveToNext())
            }
        }
        
        db.close()
        return helplineList
    }
    
    /**
     * Data class for Helpline
     */
    data class HelplineData(
        val id: Int,
        val name: String,
        val number: String,
        val description: String
    )
}