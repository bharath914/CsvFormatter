package passPercentage

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import results.Result


// This will find the pass percentage by total number of passed Subjects by total Number of subjects written
// eg : 5 students with each having 10 subjects , each of them had passed 6 subjects then it will be 30/50

suspend fun main() {
    val percentageFinder = PassPercentageFinder()
    percentageFinder.formatter()
}

//
class PassPercentageFinder {
    private val resultList = mutableListOf<Result>()
    private val r20TwoOneLabs = hashSetOf(
        "R2021123",
        "R2021423",
        "R2021424",
        "R2021425",
        "R2021426"
    )

    suspend fun formatter() {
        coroutineScope {
            /**
             * Paths should be changed everytime for specific result percentage.
             */
            launch {
                readCsv("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\passPercentage\\Kiek2ndYr2-1.csv")
                readCsv("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\passPercentage\\KIET2ndYr2-1.csv")
            }.invokeOnCompletion {
                findTotalPercentage()
                breakLine()
                findOnlyWrittenSubsPercent()
                breakLine()
                findPercentagePerSubject(subjectCode = "R2021011", subjectName = "Mathematics 3")
                breakLine()
                findPercentagePerSubject(subjectCode = "R2021054", subjectName = "MFCS")
                breakLine()
                findPercentagePerSubject(subjectCode = "R2021121", subjectName = "DBMS")
                breakLine()
                findPercentagePerSubject(subjectCode = "R2021422", subjectName = "OOP Java")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021431", subjectName ="CAI AI ")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021052", subjectName ="Operating systems")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021061", subjectName ="Data Structure")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021062", subjectName ="Java Programming")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021421", subjectName ="CSM AI")
                breakLine()
                findPercentagePerSubject(subjectCode = "R2021441", subjectName = "Fundamentals of Data Science")
                breakLine()
                findPercentagePerSubject(subjectCode ="R2021451", subjectName ="AID AI")
            }
        }

    }

    private fun findPercentagePerSubject(subjectCode: String, subjectName: String) {
        var totalPassCount = 0
        val perSubjectList = resultList.filter {
            it.subjectCode == subjectCode
        }
        perSubjectList.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassCount++;
            }
        }
        val percent = (totalPassCount.toFloat() / perSubjectList.size.toFloat()) * 100f
        println("Total Pass Percentage of $subjectName is : $percent")
    }

    private fun findTotalPercentage() {
        var totalPassedSubjects = 0

        resultList.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassedSubjects++
            }
        }
        val percent = (totalPassedSubjects.toFloat() / resultList.size.toFloat()) * 100f
        println("Total Pass Percentage : $percent")
    }

    private fun findOnlyWrittenSubsPercent() {
        var totalPassedSubjects = 0
        val onlySubjects = resultList
            .filter {
                (it.credits == "3") || (it.grade == "F") || (it.grade == "ABSENT")
                        && (r20TwoOneLabs.contains(it.subjectCode).not())
            }
        onlySubjects.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassedSubjects++
            }
        }
        val percent = (totalPassedSubjects.toFloat() / onlySubjects.size.toFloat()) * 100f
        println("Total Pass Percentage of written subjects is: $percent")
    }

    private fun readCsv(path: String) {
        csvReader().open(path) {
            readAllAsSequence().forEachIndexed { ind: Int, row: List<String> ->
                if (ind != 0) {
                    resultList.add(
                        Result(
                            sem = "",
                            date = "",
                            rollNo = row[0],
                            subjectCode = row[1],
                            subject = "",
                            grade = row[4],
                            credits = row[5],
                            internalMarks = ""

                        )
                    )
                }
            }
        }
    }

    private fun breakLine() {
        println("--------------------------------------------------------------------")
    }
}