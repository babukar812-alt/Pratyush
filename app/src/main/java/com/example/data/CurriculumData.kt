package com.example.data

import com.example.model.Chapter
import com.example.model.ClassCurriculum
import com.example.model.Question
import com.example.model.SubjectCurriculum

/**
 * Centralized, structured NCERT curriculum database for Classes 6 through 12.
 * Adheres strictly to the official NCERT syllabus structure.
 */
object CurriculumData {

    private val class6Curriculum = ClassCurriculum(
        classLevel = "6",
        displayName = "Class 6",
        subjects = listOf(
            SubjectCurriculum(
                id = "c6_sci",
                name = "Science",
                iconKey = "science",
                chapters = listOf(
                    Chapter("c6_sci_1", 1, "Components of Food", "6", "Science"),
                    Chapter("c6_sci_2", 2, "Sorting Materials into Groups", "6", "Science"),
                    Chapter("c6_sci_3", 3, "Separation of Substances", "6", "Science"),
                    Chapter("c6_sci_4", 4, "Getting to Know Plants", "6", "Science"),
                    Chapter("c6_sci_5", 5, "Body Movements", "6", "Science"),
                    Chapter("c6_sci_6", 6, "The Living Organisms — Characteristics & Habitats", "6", "Science"),
                    Chapter("c6_sci_7", 7, "Motion and Measurement of Distances", "6", "Science"),
                    Chapter("c6_sci_8", 8, "Light, Shadows and Reflections", "6", "Science"),
                    Chapter("c6_sci_9", 9, "Electricity and Circuits", "6", "Science"),
                    Chapter("c6_sci_10", 10, "Fun with Magnets", "6", "Science"),
                    Chapter("c6_sci_11", 11, "Air Around Us", "6", "Science")
                )
            ),
            SubjectCurriculum(
                id = "c6_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c6_math_1", 1, "Knowing Our Numbers", "6", "Mathematics"),
                    Chapter("c6_math_2", 2, "Whole Numbers", "6", "Mathematics"),
                    Chapter("c6_math_3", 3, "Playing with Numbers", "6", "Mathematics"),
                    Chapter("c6_math_4", 4, "Basic Geometrical Ideas", "6", "Mathematics"),
                    Chapter("c6_math_5", 5, "Understanding Elementary Shapes", "6", "Mathematics"),
                    Chapter("c6_math_6", 6, "Integers", "6", "Mathematics"),
                    Chapter("c6_math_7", 7, "Fractions", "6", "Mathematics"),
                    Chapter("c6_math_8", 8, "Decimals", "6", "Mathematics"),
                    Chapter("c6_math_9", 9, "Data Handling", "6", "Mathematics"),
                    Chapter("c6_math_10", 10, "Mensuration", "6", "Mathematics"),
                    Chapter("c6_math_11", 11, "Algebra", "6", "Mathematics"),
                    Chapter("c6_math_12", 12, "Ratio and Proportion", "6", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c6_sst",
                name = "Social Science",
                iconKey = "social_science",
                chapters = listOf(
                    Chapter("c6_sst_1", 1, "What, Where, How and When?", "6", "Social Science"),
                    Chapter("c6_sst_2", 2, "From Hunting-Gathering to Growing Food", "6", "Social Science"),
                    Chapter("c6_sst_3", 3, "In the Earliest Cities", "6", "Social Science"),
                    Chapter("c6_sst_4", 4, "What Books and Burials Tell Us", "6", "Social Science"),
                    Chapter("c6_sst_5", 5, "Kingdoms, Kings and an Early Republic", "6", "Social Science"),
                    Chapter("c6_sst_6", 6, "The Earth in the Solar System", "6", "Social Science"),
                    Chapter("c6_sst_7", 7, "Globe: Latitudes and Longitudes", "6", "Social Science"),
                    Chapter("c6_sst_8", 8, "Motions of the Earth", "6", "Social Science"),
                    Chapter("c6_sst_9", 9, "Maps", "6", "Social Science"),
                    Chapter("c6_sst_10", 10, "Understanding Diversity", "6", "Social Science"),
                    Chapter("c6_sst_11", 11, "Diversity and Discrimination", "6", "Social Science"),
                    Chapter("c6_sst_12", 12, "What is Government?", "6", "Social Science")
                )
            ),
            SubjectCurriculum(
                id = "c6_eng",
                name = "English",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c6_eng_1", 1, "Who Did Patrick's Homework?", "6", "English"),
                    Chapter("c6_eng_2", 2, "How the Dog Found Himself a New Master!", "6", "English"),
                    Chapter("c6_eng_3", 3, "Taro's Reward", "6", "English"),
                    Chapter("c6_eng_4", 4, "An Indian-American Woman in Space", "6", "English"),
                    Chapter("c6_eng_5", 5, "A Different Kind of School", "6", "English"),
                    Chapter("c6_eng_6", 6, "Who I Am", "6", "English"),
                    Chapter("c6_eng_7", 7, "Fair Play", "6", "English"),
                    Chapter("c6_eng_8", 8, "The Banyan Tree", "6", "English")
                )
            ),
            SubjectCurriculum(
                id = "c6_hin",
                name = "Hindi",
                iconKey = "hindi",
                chapters = listOf(
                    Chapter("c6_hin_1", 1, "Vah Chidiya Jo", "6", "Hindi"),
                    Chapter("c6_hin_2", 2, "Bachpan", "6", "Hindi"),
                    Chapter("c6_hin_3", 3, "Nadan Dost", "6", "Hindi"),
                    Chapter("c6_hin_4", 4, "Chand Se Thodi Si Gappe", "6", "Hindi"),
                    Chapter("c6_hin_5", 5, "Aksharon Ka Mahatva", "6", "Hindi"),
                    Chapter("c6_hin_6", 6, "Paar Nazar Ke", "6", "Hindi"),
                    Chapter("c6_hin_7", 7, "Saathi Haath Badhana", "6", "Hindi")
                )
            )
        )
    )

    private val class7Curriculum = ClassCurriculum(
        classLevel = "7",
        displayName = "Class 7",
        subjects = listOf(
            SubjectCurriculum(
                id = "c7_sci",
                name = "Science",
                iconKey = "science",
                chapters = listOf(
                    Chapter("c7_sci_1", 1, "Nutrition in Plants", "7", "Science"),
                    Chapter("c7_sci_2", 2, "Nutrition in Animals", "7", "Science"),
                    Chapter("c7_sci_3", 3, "Heat", "7", "Science"),
                    Chapter("c7_sci_4", 4, "Acids, Bases and Salts", "7", "Science"),
                    Chapter("c7_sci_5", 5, "Physical and Chemical Changes", "7", "Science"),
                    Chapter("c7_sci_6", 6, "Respiration in Organisms", "7", "Science"),
                    Chapter("c7_sci_7", 7, "Transportation in Animals and Plants", "7", "Science"),
                    Chapter("c7_sci_8", 8, "Reproduction in Plants", "7", "Science"),
                    Chapter("c7_sci_9", 9, "Motion and Time", "7", "Science"),
                    Chapter("c7_sci_10", 10, "Electric Current and Its Effects", "7", "Science"),
                    Chapter("c7_sci_11", 11, "Light", "7", "Science"),
                    Chapter("c7_sci_12", 12, "Forests: Our Lifeline", "7", "Science"),
                    Chapter("c7_sci_13", 13, "Wastewater Story", "7", "Science")
                )
            ),
            SubjectCurriculum(
                id = "c7_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c7_math_1", 1, "Integers", "7", "Mathematics"),
                    Chapter("c7_math_2", 2, "Fractions and Decimals", "7", "Mathematics"),
                    Chapter("c7_math_3", 3, "Data Handling", "7", "Mathematics"),
                    Chapter("c7_math_4", 4, "Simple Equations", "7", "Mathematics"),
                    Chapter("c7_math_5", 5, "Lines and Angles", "7", "Mathematics"),
                    Chapter("c7_math_6", 6, "The Triangle and its Properties", "7", "Mathematics"),
                    Chapter("c7_math_7", 7, "Comparing Quantities", "7", "Mathematics"),
                    Chapter("c7_math_8", 8, "Rational Numbers", "7", "Mathematics"),
                    Chapter("c7_math_9", 9, "Perimeter and Area", "7", "Mathematics"),
                    Chapter("c7_math_10", 10, "Algebraic Expressions", "7", "Mathematics"),
                    Chapter("c7_math_11", 11, "Exponents and Powers", "7", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c7_sst",
                name = "Social Science",
                iconKey = "social_science",
                chapters = listOf(
                    Chapter("c7_sst_1", 1, "Tracing Changes Through A Thousand Years", "7", "Social Science"),
                    Chapter("c7_sst_2", 2, "Kings and Kingdoms", "7", "Social Science"),
                    Chapter("c7_sst_3", 3, "Delhi: 12th to 15th Century", "7", "Social Science"),
                    Chapter("c7_sst_4", 4, "The Mughals (16th to 17th Century)", "7", "Social Science"),
                    Chapter("c7_sst_5", 5, "Environment", "7", "Social Science"),
                    Chapter("c7_sst_6", 6, "Inside Our Earth", "7", "Social Science"),
                    Chapter("c7_sst_7", 7, "Our Changing Earth", "7", "Social Science"),
                    Chapter("c7_sst_8", 8, "Air", "7", "Social Science"),
                    Chapter("c7_sst_9", 9, "Water", "7", "Social Science"),
                    Chapter("c7_sst_10", 10, "On Equality", "7", "Social Science"),
                    Chapter("c7_sst_11", 11, "Role of the Government in Health", "7", "Social Science"),
                    Chapter("c7_sst_12", 12, "How the State Government Works", "7", "Social Science")
                )
            ),
            SubjectCurriculum(
                id = "c7_eng",
                name = "English",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c7_eng_1", 1, "Three Questions", "7", "English"),
                    Chapter("c7_eng_2", 2, "A Gift of Chappals", "7", "English"),
                    Chapter("c7_eng_3", 3, "Gopal and the Hilsa Fish", "7", "English"),
                    Chapter("c7_eng_4", 4, "The Ashes That Made Trees Bloom", "7", "English"),
                    Chapter("c7_eng_5", 5, "Quality", "7", "English"),
                    Chapter("c7_eng_6", 6, "Expert Detectives", "7", "English"),
                    Chapter("c7_eng_7", 7, "The Invention of Vita-Wonk", "7", "English")
                )
            ),
            SubjectCurriculum(
                id = "c7_hin",
                name = "Hindi",
                iconKey = "hindi",
                chapters = listOf(
                    Chapter("c7_hin_1", 1, "Hum Panchhi Unmukt Gagan Ke", "7", "Hindi"),
                    Chapter("c7_hin_2", 2, "Dadi Maa", "7", "Hindi"),
                    Chapter("c7_hin_3", 3, "Himalaya Ki Betiyan", "7", "Hindi"),
                    Chapter("c7_hin_4", 4, "Kathputli", "7", "Hindi"),
                    Chapter("c7_hin_5", 5, "Mithaiwala", "7", "Hindi"),
                    Chapter("c7_hin_6", 6, "Rakt Aur Hamara Sharir", "7", "Hindi")
                )
            )
        )
    )

    private val class8Curriculum = ClassCurriculum(
        classLevel = "8",
        displayName = "Class 8",
        subjects = listOf(
            SubjectCurriculum(
                id = "c8_sci",
                name = "Science",
                iconKey = "science",
                chapters = listOf(
                    Chapter("c8_sci_1", 1, "Crop Production and Management", "8", "Science"),
                    Chapter("c8_sci_2", 2, "Microorganisms: Friend and Foe", "8", "Science"),
                    Chapter("c8_sci_3", 3, "Coal and Petroleum", "8", "Science"),
                    Chapter("c8_sci_4", 4, "Combustion and Flame", "8", "Science"),
                    Chapter("c8_sci_5", 5, "Conservation of Plants and Animals", "8", "Science"),
                    Chapter("c8_sci_6", 6, "Reproduction in Animals", "8", "Science"),
                    Chapter("c8_sci_7", 7, "Reaching the Age of Adolescence", "8", "Science"),
                    Chapter("c8_sci_8", 8, "Force and Pressure", "8", "Science"),
                    Chapter("c8_sci_9", 9, "Friction", "8", "Science"),
                    Chapter("c8_sci_10", 10, "Sound", "8", "Science"),
                    Chapter("c8_sci_11", 11, "Chemical Effects of Electric Current", "8", "Science"),
                    Chapter("c8_sci_12", 12, "Some Natural Phenomena", "8", "Science"),
                    Chapter("c8_sci_13", 13, "Light", "8", "Science")
                )
            ),
            SubjectCurriculum(
                id = "c8_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c8_math_1", 1, "Rational Numbers", "8", "Mathematics"),
                    Chapter("c8_math_2", 2, "Linear Equations in One Variable", "8", "Mathematics"),
                    Chapter("c8_math_3", 3, "Understanding Quadrilaterals", "8", "Mathematics"),
                    Chapter("c8_math_4", 4, "Data Handling", "8", "Mathematics"),
                    Chapter("c8_math_5", 5, "Square and Square Roots", "8", "Mathematics"),
                    Chapter("c8_math_6", 6, "Cube and Cube Roots", "8", "Mathematics"),
                    Chapter("c8_math_7", 7, "Comparing Quantities", "8", "Mathematics"),
                    Chapter("c8_math_8", 8, "Algebraic Expressions and Identities", "8", "Mathematics"),
                    Chapter("c8_math_9", 9, "Mensuration", "8", "Mathematics"),
                    Chapter("c8_math_10", 10, "Exponents and Powers", "8", "Mathematics"),
                    Chapter("c8_math_11", 11, "Direct and Inverse Proportions", "8", "Mathematics"),
                    Chapter("c8_math_12", 12, "Factorisation", "8", "Mathematics"),
                    Chapter("c8_math_13", 13, "Introduction to Graphs", "8", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c8_sst",
                name = "Social Science",
                iconKey = "social_science",
                chapters = listOf(
                    Chapter("c8_sst_1", 1, "How, When and Where", "8", "Social Science"),
                    Chapter("c8_sst_2", 2, "From Trade to Territory", "8", "Social Science"),
                    Chapter("c8_sst_3", 3, "Ruling the Countryside", "8", "Social Science"),
                    Chapter("c8_sst_4", 4, "Tribals, Dikus and the Golden Age", "8", "Social Science"),
                    Chapter("c8_sst_5", 5, "When People Rebel (1857 and After)", "8", "Social Science"),
                    Chapter("c8_sst_6", 6, "Resources", "8", "Social Science"),
                    Chapter("c8_sst_7", 7, "Land, Soil, Water and Wildlife", "8", "Social Science"),
                    Chapter("c8_sst_8", 8, "Agriculture", "8", "Social Science"),
                    Chapter("c8_sst_9", 9, "The Indian Constitution", "8", "Social Science"),
                    Chapter("c8_sst_10", 10, "Understanding Secularism", "8", "Social Science"),
                    Chapter("c8_sst_11", 11, "Parliament and Making of Laws", "8", "Social Science"),
                    Chapter("c8_sst_12", 12, "Judiciary", "8", "Social Science")
                )
            ),
            SubjectCurriculum(
                id = "c8_eng",
                name = "English",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c8_eng_1", 1, "The Best Christmas Present in the World", "8", "English"),
                    Chapter("c8_eng_2", 2, "The Tsunami", "8", "English"),
                    Chapter("c8_eng_3", 3, "Glimpses of the Past", "8", "English"),
                    Chapter("c8_eng_4", 4, "Bepin Choudhury's Lapse of Memory", "8", "English"),
                    Chapter("c8_eng_5", 5, "The Summit Within", "8", "English"),
                    Chapter("c8_eng_6", 6, "This is Jody's Fawn", "8", "English"),
                    Chapter("c8_eng_7", 7, "A Visit to Cambridge", "8", "English")
                )
            ),
            SubjectCurriculum(
                id = "c8_hin",
                name = "Hindi",
                iconKey = "hindi",
                chapters = listOf(
                    Chapter("c8_hin_1", 1, "Dhwani", "8", "Hindi"),
                    Chapter("c8_hin_2", 2, "Laakh Ki Chudiyan", "8", "Hindi"),
                    Chapter("c8_hin_3", 3, "Bus Ki Yatra", "8", "Hindi"),
                    Chapter("c8_hin_4", 4, "Deewanon Ki Hasti", "8", "Hindi"),
                    Chapter("c8_hin_5", 5, "Chitthiyon Ki Anoothi Duniya", "8", "Hindi"),
                    Chapter("c8_hin_6", 6, "Bhagwan Ke Daakiye", "8", "Hindi")
                )
            )
        )
    )

    private val class9Curriculum = ClassCurriculum(
        classLevel = "9",
        displayName = "Class 9",
        subjects = listOf(
            SubjectCurriculum(
                id = "c9_sci",
                name = "Science",
                iconKey = "science",
                chapters = listOf(
                    Chapter("c9_sci_1", 1, "Matter in Our Surroundings", "9", "Science"),
                    Chapter("c9_sci_2", 2, "Is Matter Around Us Pure?", "9", "Science"),
                    Chapter("c9_sci_3", 3, "Atoms and Molecules", "9", "Science"),
                    Chapter("c9_sci_4", 4, "Structure of the Atom", "9", "Science"),
                    Chapter("c9_sci_5", 5, "The Fundamental Unit of Life", "9", "Science"),
                    Chapter("c9_sci_6", 6, "Tissues", "9", "Science"),
                    Chapter("c9_sci_7", 7, "Motion", "9", "Science"),
                    Chapter("c9_sci_8", 8, "Force and Laws of Motion", "9", "Science"),
                    Chapter("c9_sci_9", 9, "Gravitation", "9", "Science"),
                    Chapter("c9_sci_10", 10, "Work and Energy", "9", "Science"),
                    Chapter("c9_sci_11", 11, "Sound", "9", "Science"),
                    Chapter("c9_sci_12", 12, "Improvement in Food Resources", "9", "Science")
                )
            ),
            SubjectCurriculum(
                id = "c9_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c9_math_1", 1, "Number Systems", "9", "Mathematics"),
                    Chapter("c9_math_2", 2, "Polynomials", "9", "Mathematics"),
                    Chapter("c9_math_3", 3, "Coordinate Geometry", "9", "Mathematics"),
                    Chapter("c9_math_4", 4, "Linear Equations in Two Variables", "9", "Mathematics"),
                    Chapter("c9_math_5", 5, "Introduction to Euclid's Geometry", "9", "Mathematics"),
                    Chapter("c9_math_6", 6, "Lines and Angles", "9", "Mathematics"),
                    Chapter("c9_math_7", 7, "Triangles", "9", "Mathematics"),
                    Chapter("c9_math_8", 8, "Quadrilaterals", "9", "Mathematics"),
                    Chapter("c9_math_9", 9, "Circles", "9", "Mathematics"),
                    Chapter("c9_math_10", 10, "Heron's Formula", "9", "Mathematics"),
                    Chapter("c9_math_11", 11, "Surface Areas and Volumes", "9", "Mathematics"),
                    Chapter("c9_math_12", 12, "Statistics", "9", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c9_sst",
                name = "Social Science",
                iconKey = "social_science",
                chapters = listOf(
                    Chapter("c9_sst_1", 1, "The French Revolution", "9", "Social Science"),
                    Chapter("c9_sst_2", 2, "Socialism in Europe & Russian Revolution", "9", "Social Science"),
                    Chapter("c9_sst_3", 3, "Nazism and the Rise of Hitler", "9", "Social Science"),
                    Chapter("c9_sst_4", 4, "India — Size and Location", "9", "Social Science"),
                    Chapter("c9_sst_5", 5, "Physical Features of India", "9", "Social Science"),
                    Chapter("c9_sst_6", 6, "Drainage", "9", "Social Science"),
                    Chapter("c9_sst_7", 7, "Climate", "9", "Social Science"),
                    Chapter("c9_sst_8", 8, "Natural Vegetation and Wildlife", "9", "Social Science"),
                    Chapter("c9_sst_9", 9, "What is Democracy? Why Democracy?", "9", "Social Science"),
                    Chapter("c9_sst_10", 10, "Constitutional Design", "9", "Social Science"),
                    Chapter("c9_sst_11", 11, "Electoral Politics", "9", "Social Science"),
                    Chapter("c9_sst_12", 12, "Working of Institutions", "9", "Social Science"),
                    Chapter("c9_sst_13", 13, "Democratic Rights", "9", "Social Science"),
                    Chapter("c9_sst_14", 14, "The Story of Village Palampur", "9", "Social Science"),
                    Chapter("c9_sst_15", 15, "People as Resource", "9", "Social Science"),
                    Chapter("c9_sst_16", 16, "Poverty as a Challenge", "9", "Social Science")
                )
            ),
            SubjectCurriculum(
                id = "c9_eng",
                name = "English",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c9_eng_1", 1, "The Fun They Had", "9", "English"),
                    Chapter("c9_eng_2", 2, "The Sound of Music", "9", "English"),
                    Chapter("c9_eng_3", 3, "The Little Girl", "9", "English"),
                    Chapter("c9_eng_4", 4, "A Truly Beautiful Mind", "9", "English"),
                    Chapter("c9_eng_5", 5, "The Snake and the Mirror", "9", "English"),
                    Chapter("c9_eng_6", 6, "My Childhood", "9", "English"),
                    Chapter("c9_eng_7", 7, "Reach for the Top", "9", "English"),
                    Chapter("c9_eng_8", 8, "Kathmandu", "9", "English"),
                    Chapter("c9_eng_9", 9, "If I Were You", "9", "English")
                )
            ),
            SubjectCurriculum(
                id = "c9_hin",
                name = "Hindi",
                iconKey = "hindi",
                chapters = listOf(
                    Chapter("c9_hin_1", 1, "Do Bailon Ki Katha", "9", "Hindi"),
                    Chapter("c9_hin_2", 2, "Lhasa Ki Aur", "9", "Hindi"),
                    Chapter("c9_hin_3", 3, "Upbhoktavadi Ki Sanskriti", "9", "Hindi"),
                    Chapter("c9_hin_4", 4, "Sanwale Sapno Ki Yaad", "9", "Hindi"),
                    Chapter("c9_hin_5", 5, "Premchand Ke Phate Joote", "9", "Hindi"),
                    Chapter("c9_hin_6", 6, "Mere Bachpan Ke Din", "9", "Hindi")
                )
            )
        )
    )

    private val class10Curriculum = ClassCurriculum(
        classLevel = "10",
        displayName = "Class 10",
        subjects = listOf(
            SubjectCurriculum(
                id = "c10_sci",
                name = "Science",
                iconKey = "science",
                chapters = listOf(
                    Chapter("c10_sci_1", 1, "Chemical Reactions and Equations", "10", "Science"),
                    Chapter("c10_sci_2", 2, "Acids, Bases and Salts", "10", "Science"),
                    Chapter("c10_sci_3", 3, "Metals and Non-metals", "10", "Science"),
                    Chapter("c10_sci_4", 4, "Carbon and its Compounds", "10", "Science"),
                    Chapter("c10_sci_5", 5, "Life Processes", "10", "Science"),
                    Chapter("c10_sci_6", 6, "Control and Coordination", "10", "Science"),
                    Chapter("c10_sci_7", 7, "How do Organisms Reproduce?", "10", "Science"),
                    Chapter("c10_sci_8", 8, "Heredity", "10", "Science"),
                    Chapter("c10_sci_9", 9, "Light – Reflection and Refraction", "10", "Science"),
                    Chapter("c10_sci_10", 10, "The Human Eye and Colourful World", "10", "Science"),
                    Chapter("c10_sci_11", 11, "Electricity", "10", "Science"),
                    Chapter("c10_sci_12", 12, "Magnetic Effects of Electric Current", "10", "Science"),
                    Chapter("c10_sci_13", 13, "Our Environment", "10", "Science")
                )
            ),
            SubjectCurriculum(
                id = "c10_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c10_math_1", 1, "Real Numbers", "10", "Mathematics"),
                    Chapter("c10_math_2", 2, "Polynomials", "10", "Mathematics"),
                    Chapter("c10_math_3", 3, "Pair of Linear Equations in Two Variables", "10", "Mathematics"),
                    Chapter("c10_math_4", 4, "Quadratic Equations", "10", "Mathematics"),
                    Chapter("c10_math_5", 5, "Arithmetic Progressions", "10", "Mathematics"),
                    Chapter("c10_math_6", 6, "Triangles", "10", "Mathematics"),
                    Chapter("c10_math_7", 7, "Coordinate Geometry", "10", "Mathematics"),
                    Chapter("c10_math_8", 8, "Introduction to Trigonometry", "10", "Mathematics"),
                    Chapter("c10_math_9", 9, "Some Applications of Trigonometry", "10", "Mathematics"),
                    Chapter("c10_math_10", 10, "Circles", "10", "Mathematics"),
                    Chapter("c10_math_11", 11, "Areas Related to Circles", "10", "Mathematics"),
                    Chapter("c10_math_12", 12, "Surface Areas and Volumes", "10", "Mathematics"),
                    Chapter("c10_math_13", 13, "Statistics", "10", "Mathematics"),
                    Chapter("c10_math_14", 14, "Probability", "10", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c10_sst",
                name = "Social Science",
                iconKey = "social_science",
                chapters = listOf(
                    Chapter("c10_sst_1", 1, "The Rise of Nationalism in Europe", "10", "Social Science"),
                    Chapter("c10_sst_2", 2, "Nationalism in India", "10", "Social Science"),
                    Chapter("c10_sst_3", 3, "The Making of a Global World", "10", "Social Science"),
                    Chapter("c10_sst_4", 4, "The Age of Industrialisation", "10", "Social Science"),
                    Chapter("c10_sst_5", 5, "Print Culture and the Modern World", "10", "Social Science"),
                    Chapter("c10_sst_6", 6, "Resources and Development", "10", "Social Science"),
                    Chapter("c10_sst_7", 7, "Forest and Wildlife Resources", "10", "Social Science"),
                    Chapter("c10_sst_8", 8, "Water Resources", "10", "Social Science"),
                    Chapter("c10_sst_9", 9, "Agriculture", "10", "Social Science"),
                    Chapter("c10_sst_10", 10, "Minerals and Energy Resources", "10", "Social Science"),
                    Chapter("c10_sst_11", 11, "Power Sharing", "10", "Social Science"),
                    Chapter("c10_sst_12", 12, "Federalism", "10", "Social Science"),
                    Chapter("c10_sst_13", 13, "Gender, Religion and Caste", "10", "Social Science"),
                    Chapter("c10_sst_14", 14, "Political Parties", "10", "Social Science"),
                    Chapter("c10_sst_15", 15, "Outcomes of Democracy", "10", "Social Science"),
                    Chapter("c10_sst_16", 16, "Development", "10", "Social Science"),
                    Chapter("c10_sst_17", 17, "Sectors of the Indian Economy", "10", "Social Science"),
                    Chapter("c10_sst_18", 18, "Money and Credit", "10", "Social Science"),
                    Chapter("c10_sst_19", 19, "Globalisation and the Indian Economy", "10", "Social Science")
                )
            ),
            SubjectCurriculum(
                id = "c10_eng",
                name = "English",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c10_eng_1", 1, "A Letter to God", "10", "English"),
                    Chapter("c10_eng_2", 2, "Nelson Mandela: Long Walk to Freedom", "10", "English"),
                    Chapter("c10_eng_3", 3, "Two Stories about Flying", "10", "English"),
                    Chapter("c10_eng_4", 4, "From the Diary of Anne Frank", "10", "English"),
                    Chapter("c10_eng_5", 5, "Glimpses of India", "10", "English"),
                    Chapter("c10_eng_6", 6, "Mijbil the Otter", "10", "English"),
                    Chapter("c10_eng_7", 7, "Madam Rides the Bus", "10", "English"),
                    Chapter("c10_eng_8", 8, "The Sermon at Benares", "10", "English"),
                    Chapter("c10_eng_9", 9, "The Proposal", "10", "English")
                )
            ),
            SubjectCurriculum(
                id = "c10_hin",
                name = "Hindi",
                iconKey = "hindi",
                chapters = listOf(
                    Chapter("c10_hin_1", 1, "Surdas Ke Pad", "10", "Hindi"),
                    Chapter("c10_hin_2", 2, "Ram-Lakshman-Parashuram Samvad", "10", "Hindi"),
                    Chapter("c10_hin_3", 3, "Utsah aur Atman-Katha", "10", "Hindi"),
                    Chapter("c10_hin_4", 4, "Netaji Ka Chashma", "10", "Hindi"),
                    Chapter("c10_hin_5", 5, "Balgobin Bhagat", "10", "Hindi"),
                    Chapter("c10_hin_6", 6, "Lakhnavi Andaz", "10", "Hindi"),
                    Chapter("c10_hin_7", 7, "Yeh Danturit Muskan", "10", "Hindi")
                )
            )
        )
    )

    private val class11Curriculum = ClassCurriculum(
        classLevel = "11",
        displayName = "Class 11",
        subjects = listOf(
            SubjectCurriculum(
                id = "c11_phy",
                name = "Physics",
                iconKey = "physics",
                chapters = listOf(
                    Chapter("c11_phy_1", 1, "Units and Measurements", "11", "Physics"),
                    Chapter("c11_phy_2", 2, "Motion in a Straight Line", "11", "Physics"),
                    Chapter("c11_phy_3", 3, "Motion in a Plane", "11", "Physics"),
                    Chapter("c11_phy_4", 4, "Laws of Motion", "11", "Physics"),
                    Chapter("c11_phy_5", 5, "Work, Energy and Power", "11", "Physics"),
                    Chapter("c11_phy_6", 6, "System of Particles & Rotational Motion", "11", "Physics"),
                    Chapter("c11_phy_7", 7, "Gravitation", "11", "Physics"),
                    Chapter("c11_phy_8", 8, "Mechanical Properties of Solids", "11", "Physics"),
                    Chapter("c11_phy_9", 9, "Mechanical Properties of Fluids", "11", "Physics"),
                    Chapter("c11_phy_10", 10, "Thermal Properties of Matter", "11", "Physics"),
                    Chapter("c11_phy_11", 11, "Thermodynamics", "11", "Physics"),
                    Chapter("c11_phy_12", 12, "Kinetic Theory", "11", "Physics"),
                    Chapter("c11_phy_13", 13, "Oscillations", "11", "Physics"),
                    Chapter("c11_phy_14", 14, "Waves", "11", "Physics")
                )
            ),
            SubjectCurriculum(
                id = "c11_chem",
                name = "Chemistry",
                iconKey = "chemistry",
                chapters = listOf(
                    Chapter("c11_chem_1", 1, "Some Basic Concepts of Chemistry", "11", "Chemistry"),
                    Chapter("c11_chem_2", 2, "Structure of Atom", "11", "Chemistry"),
                    Chapter("c11_chem_3", 3, "Classification of Elements & Periodicity", "11", "Chemistry"),
                    Chapter("c11_chem_4", 4, "Chemical Bonding & Molecular Structure", "11", "Chemistry"),
                    Chapter("c11_chem_5", 5, "Chemical Thermodynamics", "11", "Chemistry"),
                    Chapter("c11_chem_6", 6, "Equilibrium", "11", "Chemistry"),
                    Chapter("c11_chem_7", 7, "Redox Reactions", "11", "Chemistry"),
                    Chapter("c11_chem_8", 8, "Organic Chemistry — Principles & Techniques", "11", "Chemistry"),
                    Chapter("c11_chem_9", 9, "Hydrocarbons", "11", "Chemistry")
                )
            ),
            SubjectCurriculum(
                id = "c11_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c11_math_1", 1, "Sets", "11", "Mathematics"),
                    Chapter("c11_math_2", 2, "Relations and Functions", "11", "Mathematics"),
                    Chapter("c11_math_3", 3, "Trigonometric Functions", "11", "Mathematics"),
                    Chapter("c11_math_4", 4, "Complex Numbers & Quadratic Equations", "11", "Mathematics"),
                    Chapter("c11_math_5", 5, "Linear Inequalities", "11", "Mathematics"),
                    Chapter("c11_math_6", 6, "Permutations and Combinations", "11", "Mathematics"),
                    Chapter("c11_math_7", 7, "Binomial Theorem", "11", "Mathematics"),
                    Chapter("c11_math_8", 8, "Sequences and Series", "11", "Mathematics"),
                    Chapter("c11_math_9", 9, "Straight Lines", "11", "Mathematics"),
                    Chapter("c11_math_10", 10, "Conic Sections", "11", "Mathematics"),
                    Chapter("c11_math_11", 11, "Introduction to Three Dimensional Geometry", "11", "Mathematics"),
                    Chapter("c11_math_12", 12, "Limits and Derivatives", "11", "Mathematics"),
                    Chapter("c11_math_13", 13, "Statistics", "11", "Mathematics"),
                    Chapter("c11_math_14", 14, "Probability", "11", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c11_bio",
                name = "Biology",
                iconKey = "biology",
                chapters = listOf(
                    Chapter("c11_bio_1", 1, "The Living World", "11", "Biology"),
                    Chapter("c11_bio_2", 2, "Biological Classification", "11", "Biology"),
                    Chapter("c11_bio_3", 3, "Plant Kingdom", "11", "Biology"),
                    Chapter("c11_bio_4", 4, "Animal Kingdom", "11", "Biology"),
                    Chapter("c11_bio_5", 5, "Morphology of Flowering Plants", "11", "Biology"),
                    Chapter("c11_bio_6", 6, "Anatomy of Flowering Plants", "11", "Biology"),
                    Chapter("c11_bio_7", 7, "Structural Organisation in Animals", "11", "Biology"),
                    Chapter("c11_bio_8", 8, "Cell: The Unit of Life", "11", "Biology"),
                    Chapter("c11_bio_9", 9, "Biomolecules", "11", "Biology"),
                    Chapter("c11_bio_10", 10, "Cell Cycle and Cell Division", "11", "Biology"),
                    Chapter("c11_bio_11", 11, "Photosynthesis in Higher Plants", "11", "Biology"),
                    Chapter("c11_bio_12", 12, "Respiration in Plants", "11", "Biology"),
                    Chapter("c11_bio_13", 13, "Plant Growth and Development", "11", "Biology"),
                    Chapter("c11_bio_14", 14, "Breathing and Exchange of Gases", "11", "Biology"),
                    Chapter("c11_bio_15", 15, "Body Fluids and Circulation", "11", "Biology"),
                    Chapter("c11_bio_16", 16, "Excretory Products and Elimination", "11", "Biology"),
                    Chapter("c11_bio_17", 17, "Locomotion and Movement", "11", "Biology"),
                    Chapter("c11_bio_18", 18, "Neural Control and Coordination", "11", "Biology"),
                    Chapter("c11_bio_19", 19, "Chemical Coordination & Integration", "11", "Biology")
                )
            ),
            SubjectCurriculum(
                id = "c11_eng",
                name = "English Core",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c11_eng_1", 1, "The Portrait of a Lady", "11", "English Core"),
                    Chapter("c11_eng_2", 2, "We're Not Afraid to Die", "11", "English Core"),
                    Chapter("c11_eng_3", 3, "Discovering Tut: The Saga Continues", "11", "English Core"),
                    Chapter("c11_eng_4", 4, "The Voice of the Rain", "11", "English Core"),
                    Chapter("c11_eng_5", 5, "Childhood", "11", "English Core"),
                    Chapter("c11_eng_6", 6, "The Adventure", "11", "English Core"),
                    Chapter("c11_eng_7", 7, "Silk Road", "11", "English Core")
                )
            ),
            SubjectCurriculum(
                id = "c11_eco",
                name = "Economics",
                iconKey = "economics",
                chapters = listOf(
                    Chapter("c11_eco_1", 1, "Indian Economy on the Eve of Independence", "11", "Economics"),
                    Chapter("c11_eco_2", 2, "Indian Economy 1950-1990", "11", "Economics"),
                    Chapter("c11_eco_3", 3, "Liberalisation, Privatisation & Globalisation", "11", "Economics"),
                    Chapter("c11_eco_4", 4, "Human Capital Formation in India", "11", "Economics"),
                    Chapter("c11_eco_5", 5, "Rural Development", "11", "Economics"),
                    Chapter("c11_eco_6", 6, "Employment: Growth & Informalisation", "11", "Economics"),
                    Chapter("c11_eco_7", 7, "Environment & Sustainable Development", "11", "Economics")
                )
            ),
            SubjectCurriculum(
                id = "c11_cs",
                name = "Computer Science",
                iconKey = "computer",
                chapters = listOf(
                    Chapter("c11_cs_1", 1, "Computer System Overview", "11", "Computer Science"),
                    Chapter("c11_cs_2", 2, "Data Representation & Boolean Logic", "11", "Computer Science"),
                    Chapter("c11_cs_3", 3, "Computational Thinking & Python Basics", "11", "Computer Science"),
                    Chapter("c11_cs_4", 4, "Strings, Lists and Tuples in Python", "11", "Computer Science"),
                    Chapter("c11_cs_5", 5, "Dictionaries and Python Functions", "11", "Computer Science"),
                    Chapter("c11_cs_6", 6, "Society, Law and Ethics", "11", "Computer Science")
                )
            )
        )
    )

    private val class12Curriculum = ClassCurriculum(
        classLevel = "12",
        displayName = "Class 12",
        subjects = listOf(
            SubjectCurriculum(
                id = "c12_phy",
                name = "Physics",
                iconKey = "physics",
                chapters = listOf(
                    Chapter("c12_phy_1", 1, "Electric Charges and Fields", "12", "Physics"),
                    Chapter("c12_phy_2", 2, "Electrostatic Potential & Capacitance", "12", "Physics"),
                    Chapter("c12_phy_3", 3, "Current Electricity", "12", "Physics"),
                    Chapter("c12_phy_4", 4, "Moving Charges and Magnetism", "12", "Physics"),
                    Chapter("c12_phy_5", 5, "Magnetism and Matter", "12", "Physics"),
                    Chapter("c12_phy_6", 6, "Electromagnetic Induction", "12", "Physics"),
                    Chapter("c12_phy_7", 7, "Alternating Current", "12", "Physics"),
                    Chapter("c12_phy_8", 8, "Electromagnetic Waves", "12", "Physics"),
                    Chapter("c12_phy_9", 9, "Ray Optics and Optical Instruments", "12", "Physics"),
                    Chapter("c12_phy_10", 10, "Wave Optics", "12", "Physics"),
                    Chapter("c12_phy_11", 11, "Dual Nature of Radiation and Matter", "12", "Physics"),
                    Chapter("c12_phy_12", 12, "Atoms", "12", "Physics"),
                    Chapter("c12_phy_13", 13, "Nuclei", "12", "Physics"),
                    Chapter("c12_phy_14", 14, "Semiconductor Electronics: Materials & Circuits", "12", "Physics")
                )
            ),
            SubjectCurriculum(
                id = "c12_chem",
                name = "Chemistry",
                iconKey = "chemistry",
                chapters = listOf(
                    Chapter("c12_chem_1", 1, "Solutions", "12", "Chemistry"),
                    Chapter("c12_chem_2", 2, "Electrochemistry", "12", "Chemistry"),
                    Chapter("c12_chem_3", 3, "Chemical Kinetics", "12", "Chemistry"),
                    Chapter("c12_chem_4", 4, "The d- and f-Block Elements", "12", "Chemistry"),
                    Chapter("c12_chem_5", 5, "Coordination Compounds", "12", "Chemistry"),
                    Chapter("c12_chem_6", 6, "Haloalkanes and Haloarenes", "12", "Chemistry"),
                    Chapter("c12_chem_7", 7, "Alcohols, Phenols and Ethers", "12", "Chemistry"),
                    Chapter("c12_chem_8", 8, "Aldehydes, Ketones & Carboxylic Acids", "12", "Chemistry"),
                    Chapter("c12_chem_9", 9, "Amines", "12", "Chemistry"),
                    Chapter("c12_chem_10", 10, "Biomolecules", "12", "Chemistry")
                )
            ),
            SubjectCurriculum(
                id = "c12_math",
                name = "Mathematics",
                iconKey = "math",
                chapters = listOf(
                    Chapter("c12_math_1", 1, "Relations and Functions", "12", "Mathematics"),
                    Chapter("c12_math_2", 2, "Inverse Trigonometric Functions", "12", "Mathematics"),
                    Chapter("c12_math_3", 3, "Matrices", "12", "Mathematics"),
                    Chapter("c12_math_4", 4, "Determinants", "12", "Mathematics"),
                    Chapter("c12_math_5", 5, "Continuity and Differentiability", "12", "Mathematics"),
                    Chapter("c12_math_6", 6, "Application of Derivatives", "12", "Mathematics"),
                    Chapter("c12_math_7", 7, "Integrals", "12", "Mathematics"),
                    Chapter("c12_math_8", 8, "Application of Integrals", "12", "Mathematics"),
                    Chapter("c12_math_9", 9, "Differential Equations", "12", "Mathematics"),
                    Chapter("c12_math_10", 10, "Vector Algebra", "12", "Mathematics"),
                    Chapter("c12_math_11", 11, "Three Dimensional Geometry", "12", "Mathematics"),
                    Chapter("c12_math_12", 12, "Linear Programming", "12", "Mathematics"),
                    Chapter("c12_math_13", 13, "Probability", "12", "Mathematics")
                )
            ),
            SubjectCurriculum(
                id = "c12_bio",
                name = "Biology",
                iconKey = "biology",
                chapters = listOf(
                    Chapter("c12_bio_1", 1, "Sexual Reproduction in Flowering Plants", "12", "Biology"),
                    Chapter("c12_bio_2", 2, "Human Reproduction", "12", "Biology"),
                    Chapter("c12_bio_3", 3, "Reproductive Health", "12", "Biology"),
                    Chapter("c12_bio_4", 4, "Principles of Inheritance and Variation", "12", "Biology"),
                    Chapter("c12_bio_5", 5, "Molecular Basis of Inheritance", "12", "Biology"),
                    Chapter("c12_bio_6", 6, "Evolution", "12", "Biology"),
                    Chapter("c12_bio_7", 7, "Human Health and Disease", "12", "Biology"),
                    Chapter("c12_bio_8", 8, "Microbes in Human Welfare", "12", "Biology"),
                    Chapter("c12_bio_9", 9, "Biotechnology: Principles and Processes", "12", "Biology"),
                    Chapter("c12_bio_10", 10, "Biotechnology and its Applications", "12", "Biology"),
                    Chapter("c12_bio_11", 11, "Organisms and Populations", "12", "Biology"),
                    Chapter("c12_bio_12", 12, "Ecosystem", "12", "Biology"),
                    Chapter("c12_bio_13", 13, "Biodiversity and Conservation", "12", "Biology")
                )
            ),
            SubjectCurriculum(
                id = "c12_eng",
                name = "English Core",
                iconKey = "english",
                chapters = listOf(
                    Chapter("c12_eng_1", 1, "The Last Lesson", "12", "English Core"),
                    Chapter("c12_eng_2", 2, "Lost Spring", "12", "English Core"),
                    Chapter("c12_eng_3", 3, "Deep Water", "12", "English Core"),
                    Chapter("c12_eng_4", 4, "The Rattrap", "12", "English Core"),
                    Chapter("c12_eng_5", 5, "Indigo", "12", "English Core"),
                    Chapter("c12_eng_6", 6, "Poets and Pancakes", "12", "English Core"),
                    Chapter("c12_eng_7", 7, "The Interview", "12", "English Core"),
                    Chapter("c12_eng_8", 8, "Going Places", "12", "English Core")
                )
            ),
            SubjectCurriculum(
                id = "c12_eco",
                name = "Economics",
                iconKey = "economics",
                chapters = listOf(
                    Chapter("c12_eco_1", 1, "Introduction to Macroeconomics", "12", "Economics"),
                    Chapter("c12_eco_2", 2, "National Income Accounting", "12", "Economics"),
                    Chapter("c12_eco_3", 3, "Money and Banking", "12", "Economics"),
                    Chapter("c12_eco_4", 4, "Determination of Income and Employment", "12", "Economics"),
                    Chapter("c12_eco_5", 5, "Government Budget and the Economy", "12", "Economics"),
                    Chapter("c12_eco_6", 6, "Open Economy Macroeconomics", "12", "Economics")
                )
            ),
            SubjectCurriculum(
                id = "c12_cs",
                name = "Computer Science",
                iconKey = "computer",
                chapters = listOf(
                    Chapter("c12_cs_1", 1, "Computational Thinking and Programming — 2", "12", "Computer Science"),
                    Chapter("c12_cs_2", 2, "Functions and Modules in Python", "12", "Computer Science"),
                    Chapter("c12_cs_3", 3, "File Handling (Text, Binary, CSV)", "12", "Computer Science"),
                    Chapter("c12_cs_4", 4, "Data Structures: Linear List and Stacks", "12", "Computer Science"),
                    Chapter("c12_cs_5", 5, "Computer Networks & Protocols", "12", "Computer Science"),
                    Chapter("c12_cs_6", 6, "Database Management & SQL Queries", "12", "Computer Science")
                )
            )
        )
    )

    private val allCurricula = listOf(
        class6Curriculum,
        class7Curriculum,
        class8Curriculum,
        class9Curriculum,
        class10Curriculum,
        class11Curriculum,
        class12Curriculum
    )

    private val curriculumMap: Map<String, ClassCurriculum> = allCurricula.associateBy { it.classLevel }

    fun getClasses(): List<String> = listOf("6", "7", "8", "9", "10", "11", "12")

    fun getClassCurriculum(classLevel: String): ClassCurriculum? {
        return curriculumMap[classLevel] ?: curriculumMap["10"]
    }

    fun getSubjects(classLevel: String): List<String> {
        return getClassCurriculum(classLevel)?.subjects?.map { it.name }
            ?: listOf("Science", "Mathematics", "Social Science", "English", "Hindi")
    }

    fun getSubjectCurriculum(classLevel: String, subjectName: String): SubjectCurriculum? {
        val classCurr = getClassCurriculum(classLevel) ?: return null
        return classCurr.subjects.firstOrNull {
            it.name.equals(subjectName, ignoreCase = true)
        }
    }

    fun getChapters(classLevel: String, subjectName: String): List<Chapter> {
        val sub = getSubjectCurriculum(classLevel, subjectName)
        return sub?.chapters ?: emptyList()
    }

    fun getChapterNames(classLevel: String, subjectName: String, includeAll: Boolean = true): List<String> {
        val chapters = getChapters(classLevel, subjectName)
        val names = chapters.map { "Ch ${it.chapterNumber}: ${it.name}" }
        return if (includeAll) {
            listOf("All Chapters (Full Syllabus)") + names
        } else {
            names
        }
    }

    fun getChapterCount(classLevel: String, subjectName: String): Int {
        return getChapters(classLevel, subjectName).size
    }

    fun isSubjectValidForClass(classLevel: String, subjectName: String): Boolean {
        return getSubjects(classLevel).any { it.equals(subjectName, ignoreCase = true) }
    }

    fun getDefaultSubject(classLevel: String): String {
        return when (classLevel) {
            "11", "12" -> "Physics"
            else -> "Science"
        }
    }

    fun validateQuestion(question: Question, classLevel: String, subject: String, chapterName: String): Boolean {
        if (question.classLevel != classLevel) return false
        if (!question.subject.equals(subject, ignoreCase = true)) return false
        if (chapterName != "All Chapters (Full Syllabus)" && chapterName.isNotBlank()) {
            val normalizedRequested = chapterName.lowercase().replace("ch [0-9]+:".toRegex(), "").trim()
            val normalizedQuestionCh = question.chapterName.lowercase().replace("ch [0-9]+:".toRegex(), "").trim()
            if (!normalizedQuestionCh.contains(normalizedRequested) && !normalizedRequested.contains(normalizedQuestionCh)) {
                return false
            }
        }
        return true
    }
}
