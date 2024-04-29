package duplicateFinder

import StudentsListMaker.Student
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter


class DuplicateFinder {
    private val firstList = arrayListOf<Student>()
    private val secondList = arrayListOf<Student>()

     fun format() {
        csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\21_25\\Students_rows.csv") {
            val first = readAllAsSequence().map { rawList ->
                Student(
                    roll_no = rawList[0],
                    name = rawList[1],
                    password = rawList[2],
                    branch = rawList[3],
                    campus_code = rawList[4],
                    profile_image = rawList[5],
                    batch = rawList[6],
                    phone_num = rawList[7],
                    email = rawList[8],
                    regulation = rawList[9]

                )
            }
            firstList.addAll(first)


        }
        csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\secondyrsgccstudents.csv") {
            val second = readAllAsSequence().map { rawList ->
                Student(
                    roll_no = rawList[0],
                    name = rawList[1],
                    password = rawList[2],
                    branch = rawList[3],
                    campus_code = rawList[4],
                    profile_image = rawList[5],
                    batch = rawList[6],
                    phone_num = rawList[7],
                    email = rawList[8],
                    regulation = rawList[9]

                )
            }
            secondList.addAll(second)
        }

        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\filteredSecondYr.csv") {
            val set = firstList.map { it.roll_no }.toHashSet();
            val visited  = hashSetOf<String>()
            writeRow(  "roll_no",
                "name",
                "password",
                "branch",
                "campus_code",
                "profile_image",
                "batch",
                "phone_num",
                "email",
                "regulation")
            secondList.forEach {

                if (!set.contains(it.roll_no) && !visited.contains(it.roll_no)){
                    visited.add(it.roll_no)
                    it.apply {
                        writeRow(
                            roll_no,
                            name,
                            password,
                            branch,
                            campus_code,
                            profile_image,
                            batch,
                            phone_num,
                            email,
                            regulation
                        )

                    }
                }
            }
        }
    }
}

suspend fun main() {
    val duplicateFinder = DuplicateFinder()
    duplicateFinder.format()
}