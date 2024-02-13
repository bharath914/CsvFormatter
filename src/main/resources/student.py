import csv

fields = ['roll_no','name','password','branch','campus_code','profile_image','batch','phone_num','email']

mydict = [
    {
    'roll_no' : '216Q1A4371',
    'name':"Ayinala Bharat Prakash",
    'password':'216Q1A4371',
    'branch':'CAI',
    'campus_code':'KIEK',
    'profile_image':"https://jauqpatdbvxkelwtnyoy.supabase.co/storage/v1/object/public/images/216Q1A4371.jpg?t:2024-01-27T05%3A52%3A13.384Z",
    'batch':"2021-2025",
    'phone_num': "+917093219037",
    'email':"prakash.ayinala7@gmail.com"
    },
    {
    'roll_no' : '216Q1A4358',
    'name':"Kotini Sai Sandeep",
    'password':'216Q1A4358',
    'branch':'CAI',
    'campus_code':'KIEK',
    'profile_image':"https://jauqpatdbvxkelwtnyoy.supabase.co/storage/v1/object/public/images/216Q1A4358.jpg",
    'batch':"2021-2025",
    'phone_num': "+919398522789",
    'email':"saisandeepkotini@gmail.com"
    }


]


userFields = [
            'id',
            'rollno',
            'college',
            'branch',
            'phoneNumber',
            'name',
            'surname',
            'imageUrl',
            'section',
            'batch',
            'type',
            'email',
            'aadhar',
            'DOB',
            'category',
            'Address',
            'jnanabhumi_id',
            'Father_Name',
            'Mother_Name',
            'Parent_PhoneNumber',
            'cast',
            'District',
            'RefPerson',
]

userProfile=[
    {
            'id' : 1,
            'rollno' : "216Q1A4371",
            'name' : "Bharath Prakash",
            'surname' : "Ayinala",
            'college' : "KIET+",
            'branch' : "CSE-AI",
            'imageUrl' : "https://jauqpatdbvxkelwtnyoy.supabase.co/storage/v1/object/public/images/216Q1A4371",
            'section' : "CAI",
            'phoneNumber' : "7093219037",
            'type' : "Day -Scholar",
            'batch' : "2021-2025",
            'aadhar' : "386968428148",
            'DOB' : "14/09/2003",
            'category' : "B-Category",
            'Address' : "D:no 2-148 , Acthutapuram, Gokavaram Mandal",
            'jnanabhumi_id' : "3212412312421",
            'Father_Name': "Satya Rama Krishna Ayinala",
            'Mother_Name': "Manga Devi Ayinala",
            'Parent_PhoneNumber' : "9332039821",
            'cast' : "OC - KAPU",
            'District' : "East Godavari",
            'email' : "bharathayinala@gmail.com",
            'RefPerson': "V.L Prasanna"
    }
    ,{
            'id' : 2,
            'rollno' : "216Q1A4358",
            'name' : "Sai Sandeep",
            'surname' : "Kotini",
            'college' : "KIET+",
            'branch' : "CSE-AI",
            'imageUrl' : "https://jauqpatdbvxkelwtnyoy.supabase.co/storage/v1/object/public/images/216Q1A4358",
            'section' : "CAI",
            'phoneNumber' : "9398522789",
            'type' : "Day -Scholar",
            'batch' : "2021-2025",
            'aadhar' : "7687687465764",
            'DOB' : "4/09/2003",
            'category' : "B-Category",
            'Address' : "D:no 1-87/3 ,Rajempalem , Pedavegi",
            'jnanabhumi_id' : "3212412312421",
            'Father_Name': "Ashok Kumar Kotini",
            'Mother_Name': "Ratna Kumari Kotini",
            'Parent_PhoneNumber' : "9332039821",
            'cast' : "OC - KAPU",
            'District' : "West Godavari",
            'email' : "kotinisaisandeep@gmail.com",
            'RefPerson': "Y.Srinivasa Rao"
    }

]


fileName = "students.csv"
studentfileName = "studentDetails.csv"

with open(fileName,'w')as csvfile:
    writer = csv.DictWriter(csvfile,fields)
    writer.writeheader()
    writer.writerows(mydict)
with open(studentfileName,'w')as csvfile:
    writer = csv.DictWriter(csvfile,userFields)
    writer.writeheader()
    writer.writerows(userProfile)


