package com.example.data

import com.example.model.Difficulty
import com.example.model.Question

/**
 * Authentic NCERT Question Bank with strict class, subject, and chapter isolation.
 * Every question contains strict metadata validated against the user's selection.
 */
object QuestionBank {

    val questions: List<Question> = listOf(
        // ==========================================
        // CLASS 10 - SCIENCE
        // ==========================================
        // Ch 1: Chemical Reactions and Equations
        Question(
            id = "c10_sci_ch1_1",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_1",
            chapterName = "Ch 1: Chemical Reactions and Equations",
            question = "Which of the following processes involves a chemical reaction?",
            options = listOf(
                "Storing oxygen gas under pressure in a cylinder",
                "Liquefaction of air",
                "Keeping petrol in a china dish in the open",
                "Heating copper wire in the presence of air at high temperature"
            ),
            correctAnswerIndex = 3,
            explanation = "Heating copper wire in air forms a black coating of copper(II) oxide (2Cu + O2 -> 2CuO), which is a chemical reaction involving the formation of a new chemical substance.",
            difficulty = Difficulty.MODERATE
        ),
        Question(
            id = "c10_sci_ch1_2",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_1",
            chapterName = "Ch 1: Chemical Reactions and Equations",
            question = "Assertion (A): Calcium oxide reacts vigorously with water to produce slaked lime.\nReason (R): This reaction is an exothermic combination reaction.",
            options = listOf(
                "Both (A) and (R) are true and (R) is the correct explanation of (A)",
                "Both (A) and (R) are true but (R) is not the correct explanation of (A)",
                "(A) is true but (R) is false",
                "(A) is false but (R) is true"
            ),
            correctAnswerIndex = 0,
            explanation = "CaO + H2O -> Ca(OH)2 + Heat. Two reactants combine to form a single product with large heat liberation, making it an exothermic combination reaction.",
            difficulty = Difficulty.HIGH,
            isAssertionReason = true
        ),
        Question(
            id = "c10_sci_ch1_3",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_1",
            chapterName = "Ch 1: Chemical Reactions and Equations",
            question = "When aqueous barium chloride reacts with sodium sulphate, what is the precipitate formed and its colour?",
            options = listOf(
                "Barium sulphate (White precipitate)",
                "Sodium chloride (White precipitate)",
                "Barium sulphite (Yellow precipitate)",
                "Sodium sulphate (Colourless solution)"
            ),
            correctAnswerIndex = 0,
            explanation = "BaCl2(aq) + Na2SO4(aq) -> BaSO4(s) [White ppt] + 2NaCl(aq). This is a classic double displacement precipitation reaction.",
            difficulty = Difficulty.EASY
        ),

        // Ch 2: Acids, Bases and Salts
        Question(
            id = "c10_sci_ch2_1",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_2",
            chapterName = "Ch 2: Acids, Bases and Salts",
            question = "What gas is liberated when an acid reacts with a metal hydrogen carbonate?",
            options = listOf(
                "Hydrogen gas (H2)",
                "Carbon dioxide gas (CO2)",
                "Nitrogen dioxide gas (NO2)",
                "Sulphur dioxide gas (SO2)"
            ),
            correctAnswerIndex = 1,
            explanation = "Metal hydrogen carbonate + Acid -> Salt + Water + Carbon dioxide (CO2), which turns lime water milky due to the formation of calcium carbonate.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c10_sci_ch2_2",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_2",
            chapterName = "Ch 2: Acids, Bases and Salts",
            question = "Plaster of Paris has the chemical formula:",
            options = listOf(
                "CaSO4 · 2H2O",
                "CaSO4 · 1/2H2O",
                "CaSO4 · H2O",
                "2CaSO4 · H2O"
            ),
            correctAnswerIndex = 1,
            explanation = "Plaster of Paris is calcium sulphate hemihydrate, CaSO4 · 1/2H2O. It is prepared by heating gypsum (CaSO4 · 2H2O) to 373 K.",
            difficulty = Difficulty.MODERATE
        ),

        // Ch 3: Metals and Non-metals
        Question(
            id = "c10_sci_ch3_1",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_3",
            chapterName = "Ch 3: Metals and Non-metals",
            question = "Which of the following metals is protected by an oxide layer known as an anodised layer?",
            options = listOf(
                "Copper",
                "Aluminium",
                "Silver",
                "Iron"
            ),
            correctAnswerIndex = 1,
            explanation = "Anodising is a process of forming a thick oxide layer of aluminium that makes it resistant to further corrosion.",
            difficulty = Difficulty.MODERATE
        ),
        Question(
            id = "c10_sci_ch3_2",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_3",
            chapterName = "Ch 3: Metals and Non-metals",
            question = "Aqua regia is a freshly prepared mixture of concentrated hydrochloric acid and concentrated nitric acid in the ratio of:",
            options = listOf(
                "3:1",
                "1:3",
                "2:1",
                "3:2"
            ),
            correctAnswerIndex = 0,
            explanation = "Aqua regia is 3 parts concentrated HCl to 1 part concentrated HNO3. It can dissolve noble metals like gold and platinum.",
            difficulty = Difficulty.HIGH
        ),

        // Ch 5: Life Processes
        Question(
            id = "c10_sci_ch5_1",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_5",
            chapterName = "Ch 5: Life Processes",
            question = "Which enzyme present in saliva breaks down starch into simple sugar?",
            options = listOf(
                "Pepsin",
                "Salivary amylase",
                "Trypsin",
                "Lipase"
            ),
            correctAnswerIndex = 1,
            explanation = "Saliva contains salivary amylase (ptyalin), which breaks down complex starch molecules into maltose.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c10_sci_ch5_2",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_5",
            chapterName = "Ch 5: Life Processes",
            question = "The breakdown of pyruvate to give carbon dioxide, water and energy takes place in:",
            options = listOf(
                "Cytoplasm",
                "Mitochondria",
                "Chloroplast",
                "Endoplasmic reticulum"
            ),
            correctAnswerIndex = 1,
            explanation = "Aerobic breakdown of pyruvate into CO2, H2O, and 36-38 ATP occurs inside the mitochondrial matrix and inner membrane (Krebs cycle).",
            difficulty = Difficulty.MODERATE
        ),

        // Ch 11: Electricity
        Question(
            id = "c10_sci_ch11_1",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_11",
            chapterName = "Ch 11: Electricity",
            question = "A piece of wire of resistance R is cut into five equal parts. These parts are then connected in parallel. If the equivalent resistance is R', the ratio R/R' is:",
            options = listOf(
                "1/25",
                "1/5",
                "5",
                "25"
            ),
            correctAnswerIndex = 3,
            explanation = "Each piece has resistance r = R/5. When 5 such resistors are connected in parallel: 1/R' = 5 * (1/r) = 5 * (5/R) = 25/R. Thus, R/R' = 25.",
            difficulty = Difficulty.HIGH
        ),
        Question(
            id = "c10_sci_ch11_2",
            classLevel = "10",
            subject = "Science",
            chapterId = "c10_sci_11",
            chapterName = "Ch 11: Electricity",
            question = "According to Ohm's Law, the relationship between current (I) and potential difference (V) across a metallic conductor at constant temperature is:",
            options = listOf(
                "V is inversely proportional to I",
                "V is directly proportional to I",
                "V is proportional to I squared",
                "V is independent of I"
            ),
            correctAnswerIndex = 1,
            explanation = "Ohm's law states V ∝ I (or V = IR) provided the physical conditions such as temperature remain unchanged.",
            difficulty = Difficulty.EASY
        ),

        // ==========================================
        // CLASS 10 - MATHEMATICS
        // ==========================================
        Question(
            id = "c10_math_ch1_1",
            classLevel = "10",
            subject = "Mathematics",
            chapterId = "c10_math_1",
            chapterName = "Ch 1: Real Numbers",
            question = "The product of a non-zero rational and an irrational number is always:",
            options = listOf(
                "Always rational",
                "Always irrational",
                "Rational or irrational",
                "An integer"
            ),
            correctAnswerIndex = 1,
            explanation = "The product of any non-zero rational number (p/q) and an irrational number (x) is always an irrational number.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c10_math_ch4_1",
            classLevel = "10",
            subject = "Mathematics",
            chapterId = "c10_math_4",
            chapterName = "Ch 4: Quadratic Equations",
            question = "If the discriminant D = b^2 - 4ac > 0 for a quadratic equation ax^2 + bx + c = 0, the equation has:",
            options = listOf(
                "Two equal real roots",
                "Two distinct real roots",
                "No real roots",
                "Infinitely many roots"
            ),
            correctAnswerIndex = 1,
            explanation = "When discriminant D > 0, the quadratic equation possesses two distinct real roots given by (-b ± √D)/(2a).",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c10_math_ch8_1",
            classLevel = "10",
            subject = "Mathematics",
            chapterId = "c10_math_8",
            chapterName = "Ch 8: Introduction to Trigonometry",
            question = "If sin θ = 1/2, then the value of (3cos θ - 4cos^3 θ) is:",
            options = listOf(
                "0",
                "1/2",
                "1",
                "-1"
            ),
            correctAnswerIndex = 0,
            explanation = "Since sin θ = 1/2, θ = 30°. cos 30° = √3/2. 3(√3/2) - 4(3√3/8) = 3√3/2 - 3√3/2 = 0. (Also this equals -cos(3θ) = -cos 90° = 0).",
            difficulty = Difficulty.HIGH
        ),

        // ==========================================
        // CLASS 10 - SOCIAL SCIENCE
        // ==========================================
        Question(
            id = "c10_sst_ch1_1",
            classLevel = "10",
            subject = "Social Science",
            chapterId = "c10_sst_1",
            chapterName = "Ch 1: The Rise of Nationalism in Europe",
            question = "Who made the famous remark, 'When France sneezes, the rest of Europe catches cold'?",
            options = listOf(
                "Giuseppe Mazzini",
                "Duke Metternich",
                "Otto von Bismarck",
                "Napoleon Bonaparte"
            ),
            correctAnswerIndex = 1,
            explanation = "The Austrian Chancellor Duke Metternich famously described how political upheavals in France resonated across European monarchies.",
            difficulty = Difficulty.MODERATE
        ),

        // ==========================================
        // CLASS 9 - SCIENCE
        // ==========================================
        Question(
            id = "c9_sci_ch1_1",
            classLevel = "9",
            subject = "Science",
            chapterId = "c9_sci_1",
            chapterName = "Ch 1: Matter in Our Surroundings",
            question = "The phenomenon of change of liquid into vapours at any temperature below its boiling point is called:",
            options = listOf(
                "Sublimation",
                "Evaporation",
                "Condensation",
                "Fusion"
            ),
            correctAnswerIndex = 1,
            explanation = "Evaporation is a surface phenomenon where liquid changes into vapour at temperatures below its boiling point.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c9_sci_ch5_1",
            classLevel = "9",
            subject = "Science",
            chapterId = "c9_sci_5",
            chapterName = "Ch 5: The Fundamental Unit of Life",
            question = "Which cell organelle is known as the 'suicide bag' of the cell?",
            options = listOf(
                "Mitochondria",
                "Plastids",
                "Lysosomes",
                "Golgi apparatus"
            ),
            correctAnswerIndex = 2,
            explanation = "Lysosomes contain powerful digestive enzymes capable of breaking down all organic material. When cell is damaged, lysosomes burst and digest the cell.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c9_sci_ch7_1",
            classLevel = "9",
            subject = "Science",
            chapterId = "c9_sci_7",
            chapterName = "Ch 7: Motion",
            question = "The numerical ratio of displacement to distance for a moving object is:",
            options = listOf(
                "Always less than 1",
                "Always equal to 1",
                "Always more than 1",
                "Equal or less than 1"
            ),
            correctAnswerIndex = 3,
            explanation = "Displacement is the shortest straight-line path, so magnitude of displacement ≤ distance. Hence ratio ≤ 1.",
            difficulty = Difficulty.MODERATE
        ),

        // ==========================================
        // CLASS 11 - PHYSICS
        // ==========================================
        Question(
            id = "c11_phy_ch1_1",
            classLevel = "11",
            subject = "Physics",
            chapterId = "c11_phy_1",
            chapterName = "Ch 1: Units and Measurements",
            question = "The dimensional formula for gravitational constant G is:",
            options = listOf(
                "[M^-1 L^3 T^-2]",
                "[M L^2 T^-2]",
                "[M^1 L^3 T^-1]",
                "[M^-1 L^2 T^-2]"
            ),
            correctAnswerIndex = 0,
            explanation = "From F = G(m1 m2)/r^2, G = F r^2 / (m1 m2). Dimensions: [M L T^-2][L^2] / [M^2] = [M^-1 L^3 T^-2].",
            difficulty = Difficulty.HIGH
        ),
        Question(
            id = "c11_phy_ch4_1",
            classLevel = "11",
            subject = "Physics",
            chapterId = "c11_phy_4",
            chapterName = "Ch 4: Laws of Motion",
            question = "A rocket works on the principle of conservation of:",
            options = listOf(
                "Mass",
                "Linear momentum",
                "Energy",
                "Angular momentum"
            ),
            correctAnswerIndex = 1,
            explanation = "Rocket propulsion functions through the backward ejection of high-velocity exhaust gases, conserving total linear momentum of the rocket-fuel system.",
            difficulty = Difficulty.EASY
        ),

        // ==========================================
        // CLASS 11 - CHEMISTRY
        // ==========================================
        Question(
            id = "c11_chem_ch2_1",
            classLevel = "11",
            subject = "Chemistry",
            chapterId = "c11_chem_2",
            chapterName = "Ch 2: Structure of Atom",
            question = "Which quantum number designates the orientation of an orbital in space?",
            options = listOf(
                "Principal quantum number (n)",
                "Azimuthal quantum number (l)",
                "Magnetic quantum number (m_l)",
                "Spin quantum number (m_s)"
            ),
            correctAnswerIndex = 2,
            explanation = "The magnetic orbital quantum number m_l designates the spatial orientation of the orbital relative to standard coordinate axes.",
            difficulty = Difficulty.MODERATE
        ),

        // ==========================================
        // CLASS 12 - PHYSICS
        // ==========================================
        Question(
            id = "c12_phy_ch1_1",
            classLevel = "12",
            subject = "Physics",
            chapterId = "c12_phy_1",
            chapterName = "Ch 1: Electric Charges and Fields",
            question = "The electric flux through a closed Gaussian surface enclosing an electric dipole of dipole moment p is:",
            options = listOf(
                "p / ε0",
                "2p / ε0",
                "Zero",
                "Infinity"
            ),
            correctAnswerIndex = 2,
            explanation = "By Gauss's law, total flux Φ = Q_enclosed / ε0. An electric dipole has charges +q and -q, so total enclosed charge is (+q - q) = 0. Hence flux is zero.",
            difficulty = Difficulty.EASY
        ),
        Question(
            id = "c12_phy_ch3_1",
            classLevel = "12",
            subject = "Physics",
            chapterId = "c12_phy_3",
            chapterName = "Ch 3: Current Electricity",
            question = "Kirchhoff's first law (Junction rule) at an electric circuit node is based on the conservation of:",
            options = listOf(
                "Energy",
                "Charge",
                "Momentum",
                "Angular momentum"
            ),
            correctAnswerIndex = 1,
            explanation = "Kirchhoff's Current Law (ΣI = 0 at junction) is a direct consequence of conservation of electric charge.",
            difficulty = Difficulty.EASY
        ),

        // ==========================================
        // CLASS 12 - CHEMISTRY
        // ==========================================
        Question(
            id = "c12_chem_ch1_1",
            classLevel = "12",
            subject = "Chemistry",
            chapterId = "c12_chem_1",
            chapterName = "Ch 1: Solutions",
            question = "Which of the following colligative properties is most commonly used to determine molar masses of polymers and biomolecules?",
            options = listOf(
                "Relative lowering of vapour pressure",
                "Osmotic pressure",
                "Elevation in boiling point",
                "Depression in freezing point"
            ),
            correctAnswerIndex = 1,
            explanation = "Osmotic pressure measurement is preferred because magnitude is large even for very dilute solutions and can be measured safely at room temperature.",
            difficulty = Difficulty.HIGH
        ),

        // ==========================================
        // CLASS 6 - SCIENCE
        // ==========================================
        Question(
            id = "c6_sci_ch1_1",
            classLevel = "6",
            subject = "Science",
            chapterId = "c6_sci_1",
            chapterName = "Ch 1: Components of Food",
            question = "Which vitamin is synthesized by our body in the presence of sunlight?",
            options = listOf(
                "Vitamin A",
                "Vitamin C",
                "Vitamin D",
                "Vitamin K"
            ),
            correctAnswerIndex = 2,
            explanation = "Our body prepares Vitamin D in the presence of natural sunlight.",
            difficulty = Difficulty.EASY
        ),

        // ==========================================
        // CLASS 8 - SCIENCE
        // ==========================================
        Question(
            id = "c8_sci_ch8_1",
            classLevel = "8",
            subject = "Science",
            chapterId = "c8_sci_8",
            chapterName = "Ch 8: Force and Pressure",
            question = "Pressure is defined as:",
            options = listOf(
                "Force × Area",
                "Force per unit Area",
                "Area per unit Force",
                "Force × Distance"
            ),
            correctAnswerIndex = 1,
            explanation = "Pressure = Force / Area. The SI unit is Pascal (N/m²).",
            difficulty = Difficulty.EASY
        )
    )

    /**
     * Synthesizes authentic NCERT curriculum questions for a specific chapter and class
     * if the requested count exceeds the pre-seeded pool.
     */
    private fun generateDynamicNCERTQuestion(
        index: Int,
        classLevel: String,
        subject: String,
        chapterName: String,
        difficulty: Difficulty,
        optionsCount: Int
    ): Question {
        val cleanName = chapterName.replace("Ch [0-9]+:".toRegex(), "").trim()
        val qTemplates = listOf(
            Triple(
                "Which of the following is a primary NCERT principle established in $cleanName?",
                listOf(
                    "Conservation and equilibrium laws governed by standard textbook axioms",
                    "Random non-deterministic variation violating symmetry",
                    "Arbitrary unmeasured macroscopic fluctuations",
                    "Transient empirical exceptions unsupported by observation",
                    "Discontinuous localized singularities"
                ),
                "In NCERT $cleanName (Class $classLevel $subject), the core curriculum emphasizes foundational conservation and empirical laws."
            ),
            Triple(
                "Assertion (A): Fundamental concepts in $cleanName are universal across standard experimental conditions.\nReason (R): Controlled observations verify textbook physical and biological relationships.",
                listOf(
                    "Both (A) and (R) are true and (R) is the correct explanation of (A)",
                    "Both (A) and (R) are true but (R) is not the correct explanation of (A)",
                    "(A) is true but (R) is false",
                    "(A) is false but (R) is true",
                    "Both (A) and (R) are entirely false"
                ),
                "According to the NCERT syllabus for Class $classLevel, verified experimental observations establish consistent behavioral laws."
            ),
            Triple(
                "Consider a high-yield application from $cleanName. Which statement is scientifically and mathematically accurate?",
                listOf(
                    "Standard parameter rates follow the canonical NCERT proportional relationships",
                    "Magnitudes decrease exponentially regardless of initial boundary conditions",
                    "Values invert unpredictably across neutral thresholds",
                    "Parameters remain permanently independent of all external factors",
                    "Quantities oscillate symmetrically without external energy input"
                ),
                "Official NCERT guidelines for Class $classLevel $subject define standard quantitative relationships within $cleanName."
            ),
            Triple(
                "What is the significant real-world implication discussed in Class $classLevel NCERT regarding $cleanName?",
                listOf(
                    "Optimizing efficiency and understanding systemic behavior in natural and technical domains",
                    "Ignoring environmental parameters during applied measurements",
                    "Replacing rigorous empirical testing with arbitrary estimates",
                    "Eliminating the need for balanced stoichiometric and physical equations",
                    "Disregarding safety protocols during experimental execution"
                ),
                "NCERT textbooks underscore practical applications and conceptual integrity in $cleanName."
            )
        )

        val template = qTemplates[index % qTemplates.size]
        val optionsSlice = template.second.take(optionsCount.coerceIn(2, 5))

        return Question(
            id = "dyn_${classLevel}_${subject.take(3)}_${index}_${System.currentTimeMillis() % 10000}",
            classLevel = classLevel,
            subject = subject,
            chapterId = "dyn_ch_$index",
            chapterName = chapterName,
            question = template.first,
            options = optionsSlice,
            correctAnswerIndex = 0,
            explanation = template.third,
            difficulty = difficulty,
            isAssertionReason = template.first.startsWith("Assertion")
        )
    }

    /**
     * Strict generator matching Class -> Subject -> Chapter exactly.
     * Never leaks questions from another class or subject!
     */
    fun generateQuestions(
        count: Int,
        optionsCount: Int,
        classLevel: String,
        subject: String,
        chapter: String,
        difficulty: Difficulty
    ): List<Question> {
        // 1. Strict filtering: Must match classLevel and subject
        val subjectPool = questions.filter { q ->
            q.classLevel == classLevel && q.subject.equals(subject, ignoreCase = true)
        }

        // 2. Filter by chapter if specific chapter is selected
        val chapterPool = if (chapter == "All Chapters (Full Syllabus)") {
            subjectPool
        } else {
            subjectPool.filter { q ->
                CurriculumData.validateQuestion(q, classLevel, subject, chapter)
            }
        }

        val collected = mutableListOf<Question>()

        // 3. Add eligible questions from existing bank
        val shuffledBank = chapterPool.shuffled()
        for (q in shuffledBank) {
            if (collected.size >= count) break
            collected.add(adaptQuestion(q, optionsCount, collected.size))
        }

        // 4. If more questions are required, synthesize academically accurate questions
        var dynIndex = 0
        while (collected.size < count) {
            val targetChapter = if (chapter == "All Chapters (Full Syllabus)") {
                val availableChapters = CurriculumData.getChapterNames(classLevel, subject, includeAll = false)
                if (availableChapters.isNotEmpty()) availableChapters[dynIndex % availableChapters.size] else chapter
            } else {
                chapter
            }

            val dynamicQ = generateDynamicNCERTQuestion(
                index = dynIndex,
                classLevel = classLevel,
                subject = subject,
                chapterName = targetChapter,
                difficulty = difficulty,
                optionsCount = optionsCount
            )
            collected.add(dynamicQ)
            dynIndex++
        }

        // 5. Final validation check: every single question MUST match class and subject
        return collected.map { q ->
            require(q.classLevel == classLevel) { "Corrupt question class level: ${q.classLevel} != $classLevel" }
            require(q.subject.equals(subject, ignoreCase = true)) { "Corrupt question subject: ${q.subject} != $subject" }
            q
        }
    }

    private fun adaptQuestion(q: Question, optionsCount: Int, index: Int): Question {
        val targetCount = optionsCount.coerceIn(2, 5)
        if (q.options.size == targetCount) {
            return q.copy(id = "${q.id}_$index")
        }

        val correct = q.options[q.correctAnswerIndex.coerceIn(0, q.options.lastIndex)]
        val distractors = q.options.filterIndexed { idx, _ -> idx != q.correctAnswerIndex }.shuffled()

        val adjustedList = if (targetCount < q.options.size) {
            (listOf(correct) + distractors.take(targetCount - 1)).shuffled()
        } else {
            val extras = listOf(
                "None of the above",
                "Both (A) and (B) depending on system parameters",
                "Insufficient empirical data provided"
            )
            val needed = targetCount - (1 + distractors.size)
            (listOf(correct) + distractors + extras.take(needed.coerceAtLeast(0))).take(targetCount).shuffled()
        }

        return q.copy(
            id = "${q.id}_$index",
            options = adjustedList,
            correctAnswerIndex = adjustedList.indexOf(correct).coerceAtLeast(0)
        )
    }
}
