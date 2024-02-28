package emptyFormatter

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class StudentDetails(
    val id: String = "",
    val rollno: String = "",
    val college: String = " ",
    val branch: String = " ",
    val phoneNumber: String = " ",
    val name: String = " ",
    val surname: String = " ",
    val imageUrl: String = " ",
    val section: String = " ",
    val batch: String = " ",
    val type: String = " ",
    val email: String = " ",
    val aadhar: String = " ",
    val DOB: String = " ",
    val category: String = " ",
    val Address: String = " ",
    val jnanabhumi_id: String = " ",
    val Father_Name: String = " ",
    val Mother_Name: String = " ",
    val Parent_PhoneNumber: String = " ",
    val cast: String = " ",
    val District: String = " ",
    val RefPerson: String = " ",

    )


suspend fun main() {
    val formatter = EmptyFormatter()
    formatter.format()
}

class EmptyFormatter {
    private val finalList = arrayListOf<StudentDetails>()
    suspend fun format() {

        csvReader().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\assests\\detail2021_2025_csvUnformatted.csv") {
            readAllAsSequence().forEachIndexed { ind, details ->

                finalList.add(
                    StudentDetails(
                        id = details[0],
                        rollno = details[1],
                        college = details[2],
                        branch = details[3],
                        phoneNumber = details[4],
                        name = details[5],
                        surname = details[6],
                        imageUrl = details[7],
                        section = details[8],
                        batch = details[9],
                        type = details[10],
                        email = details[11],
                        aadhar = details[12],
                        DOB = details[13],
                        category = details[14],
                        Address = details[15],
                        jnanabhumi_id = details[16],
                        Father_Name = details[17],
                        Mother_Name = details[18],
                        Parent_PhoneNumber = details[19],
                        cast = details[20],
                        District = details[21],
                        RefPerson = details[22]
                    )
                )

            }

        }
        coroutineScope {
            this.launch {
                write()
            }
        }
    }

    private fun write() {
        csvWriter().open("C:\\Users\\Bharath\\Desktop\\CsvFormatter\\src\\main\\resources\\output\\formatted21_25Data.csv") {
            finalList.forEach {
                it.apply {

                    writeRow(
                        id.ifBlank { "999" },
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