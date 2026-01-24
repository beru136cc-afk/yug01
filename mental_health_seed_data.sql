-- Mental Health Content Table Schema
-- This SQL script demonstrates the table structure and seed data
-- The actual implementation is in DatabaseHelper.kt using raw SQL

-- Create mental_health_content table
CREATE TABLE mental_health_content (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    type TEXT NOT NULL,          -- 'Video' or 'Tip'
    category TEXT NOT NULL,      -- 'Meditation', 'Stress', or 'Sleep'
    content_data TEXT NOT NULL   -- URL link or description text
);

-- Insert seed data (3 sample records)

-- 1. 5-Minute Breathing Exercise (Meditation Video)
INSERT INTO mental_health_content 
(title, type, category, content_data) 
VALUES 
('5-Minute Breathing Exercise', 'Video', 'Meditation', 'https://www.youtube.com/watch?v=tybOi4hjZFQ');

-- 2. Better Sleep Tips (Sleep Tip)
INSERT INTO mental_health_content 
(title, type, category, content_data) 
VALUES 
('Better Sleep Tips', 'Tip', 'Sleep', 'Maintain a consistent sleep schedule by going to bed and waking up at the same time every day. Avoid screens 1 hour before bedtime and create a relaxing bedtime routine.');

-- 3. Stress Relief Guide (Stress Tip)
INSERT INTO mental_health_content 
(title, type, category, content_data) 
VALUES 
('Stress Relief Guide', 'Tip', 'Stress', 'Practice deep breathing: Inhale for 4 counts, hold for 4, exhale for 4. Take short walks in nature. Journal your thoughts and feelings. Limit caffeine and prioritize self-care activities.');

-- Query examples:

-- Get all mental health content
SELECT * FROM mental_health_content;

-- Get content by category (Meditation)
SELECT * FROM mental_health_content WHERE category = 'Meditation';

-- Get content by category (Stress)
SELECT * FROM mental_health_content WHERE category = 'Stress';

-- Get content by category (Sleep)
SELECT * FROM mental_health_content WHERE category = 'Sleep';

-- Get only videos
SELECT * FROM mental_health_content WHERE type = 'Video';

-- Get only tips
SELECT * FROM mental_health_content WHERE type = 'Tip';
