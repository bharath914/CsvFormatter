package image_usersListMaker

import StudentsListMaker.Student
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import emptyFormatter.StudentDetails
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ImageAndUsersListMaker {
    /*
id,rollno,college,branch,phoneNumber,name,surname,batch,type,email,aadhar,DOB,category,Address,jnanabhumi_id,Father_Name,Mother_Name,Parent_PhoneNumber,cast,District,RefPerson

        //given :
    0. id,
    1. rollno,
    2. college,
    3. branch,
    4. phoneNumber,
    5. name,
    6. surname,
    7. batch,
    8. section,
    9. type,
    10. email,
    11. aadhar,
    12. DOB,
    13. category,
    14. Address,
    15. jnanabhumi_id,
    16. Father_Name,
    17. Mother_Name,
    18. Parent_PhoneNumber,
    19. cast,
    20. District,
    21. RefPerson
        //output :
     */

    private val resultListUsrs = arrayListOf<Student>()
    private val studentDetailsList = arrayListOf<StudentDetails>()
    val outputFileName= "secondyrsgccstudents"
    val outputFileNameDetails = "secondyrdetialsgcc"
    suspend fun format() {

        val path = "C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\2ndyrgcc.csv"

        coroutineScope {
            launch {

                csvReader().open(path) {
                    val list = readAllAsSequence().map { rawList ->
                        Student(
                            roll_no = rawList[1],
                            name = rawList[5] + " " + rawList[6],
                            password = rawList[1],
                            branch = rawList[3],
                            campus_code = rawList[2],
                            profile_image = "http://61.1.171.141/kiet/05photos/${rawList[1]}.jpg",
                            batch = rawList[7],
                            phone_num = rawList[4],
                            email = rawList[10],
                            regulation = "R20"
                        )


                    }
                    resultListUsrs.addAll(list)


                }
                csvReader().open(path) {
                    val detailsList = readAllAsSequence().map { rawList ->
                        StudentDetails(
                            id = rawList[0],
                            rollno = rawList[1],
                            college = rawList[2],
                            branch = rawList[3],
                            phoneNumber = rawList[4],
                            name = rawList[5],
                            surname = rawList[6],
                            imageUrl = "http://61.1.171.141/kiet/05photos/${rawList[1]}.jpg",
                            section = rawList[8],
                            batch = rawList[7],
                            type = rawList[9],
                            email = rawList[10],
                            aadhar = rawList[11],
                            DOB = rawList[12],
                            category = rawList[13],
                            Address = rawList[14],
                            jnanabhumi_id = rawList[15],
                            Father_Name = rawList[16],
                            Mother_Name = rawList[17],
                            Parent_PhoneNumber = rawList[18],
                            cast = rawList[19],
                            District = rawList[20],
                            RefPerson = rawList[21]

                        )
                    }
                    studentDetailsList.addAll(detailsList)
                }

            }
        }.invokeOnCompletion {


            write()

        }
    }

    private fun write() {
        println("Users List size ${resultListUsrs.size}")
        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\$outputFileName.csv") {
            writeRow(
                "roll_no",
                "name",
                "password",
                "branch",
                "campus_code",
                "profile_image",
                "batch",
                "phone_num",
                "email",
                "regulation"
            )
            resultListUsrs.forEach {
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

        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\$outputFileNameDetails.csv") {

            writeRow(
                "id",
                "rollno",
                "college",
                "branch",
                "phoneNumber",
                "name",
                "surname",
                "imageUrl",
                "section",
                "batch",
                "type",
                "email",
                "aadhar",
                "DOB",
                "category",
                "Address",
                "jnanabhumi_id",
                "Father_Name",
                "Mother_Name",
                "Parent_PhoneNumber",
                "cast",
                "District",
                "RefPerson"
            )
            var nid = 9999;
            studentDetailsList.forEach {

                it.apply {

                    writeRow(
                        id.ifBlank { nid++ },
                        rollno.ifBlank { "N/A" },
                        college.ifBlank { "N/A" },
                        branch.ifBlank { "N/A" },
                        phoneNumber.ifBlank { "N/A" },
                        name.ifBlank { "N/A" },
                        surname.ifBlank { "N/A" },
                        imageUrl.ifBlank { "N/A" },
                        section.ifBlank { "N/A" },
                        batch.ifBlank { "N/A" },
                        type.ifBlank { "N/A" },
                        email.ifBlank { "N/A" },
                        aadhar.ifBlank { "N/A" },
                        DOB.ifBlank { "N/A" },
                        category.ifBlank { "N/A" },
                        Address.ifBlank { "N/A" },
                        jnanabhumi_id.ifBlank { "N/A" },
                        Father_Name.ifBlank { "N/A" },
                        Mother_Name.ifBlank { "N/A" },
                        Parent_PhoneNumber.ifBlank { "N/A" },
                        cast.ifBlank { "N/A" },
                        District.ifBlank { "N/A" },
                        RefPerson.ifBlank { "N/A" }
                    )
                }

            }
        }

    }

}

suspend fun main() {
    val maker = ImageAndUsersListMaker()
    maker.format()
}