# 📱 Room-Based Smart Attendance System with Facial Recognition

## 🏫 Project Overview
The **Room-Based Smart Attendance System** is a mobile-based solution designed to streamline the process of marking attendance for hostel students using facial recognition. Unlike conventional centralized biometric systems, this system allows students to mark attendance directly from their rooms, improving convenience, reducing congestion, and enhancing overall hostel management efficiency.

## ⭐ Features
- 📲 **Mobile-based attendance marking**: Students can record attendance from their rooms.
- 🧑‍💻 **Facial recognition authentication**: Ensures identity verification using Convolutional Neural Networks (CNNs).
- 🔑 **Dual login system**: Separate modules for hostel authorities and students.
- 🔔 **Automated notifications**: Alerts for incorrect attendance entries and missed attendance windows.
- 📊 **Admin dashboard**: Provides attendance reports and real-time monitoring.

## 🎯 Motivation
Traditional hostel attendance systems require students to visit centralized biometric stations, leading to inefficiencies such as delays, congestion, and challenges for students with mobility issues. By leveraging facial recognition and mobile technology, this project provides a more accessible, decentralized, and user-friendly solution.

## 🔬 Research Contribution
This project contributes to:
- ✅ Enhancing hostel attendance systems by decentralizing attendance marking.
- ✅ Improving flexibility and accessibility through mobile-based verification.
- ✅ Reducing physical movement and congestion at biometric stations.
- ✅ Automating record-keeping and notifications for better hostel management.

## 🏗️ System Architecture
The system consists of three main components:
1. 📱 **Mobile Application**: Students use their smartphones to scan their faces for attendance verification.
2. 🤖 **Facial Recognition Module**: Uses CNN-based algorithms to verify student identities.
3. 📊 **Admin Dashboard**: Provides tools for hostel authorities to manage attendance records and notifications.

## 📜 Pseudocode Representation
```
BEGIN
    DISPLAY Login Screen
    USER selects login type (Admin or Student)
    
    IF login_type == "Admin" THEN
        DISPLAY Room Register Module
        ADMIN registers student face
        STORE face in database
        EXIT
    
    ELSE IF login_type == "Student" THEN
        DISPLAY Attendance Module
        SCAN student face using phone camera
    
            IF face matches the registered face THEN
                RECORD attendance with timestamp
                EXIT
            ELSE
                DISPLAY "Face not recognized" error
                EXIT
            ENDIF
    ENDIF
    IF attendance window missed THEN
        SEND notification to student to visit the hostel office
    ENDIF
END
```

## 📌 Algorithm
1. **Start**
2. **Display Login Screen**:
   - Options: Admin or Student.
3. **Admin Login**:
   - Register students.
   - Capture and store student faces in the database.
4. **Student Login**:
   - Mark attendance by scanning face with phone camera.
5. **Check Face**:
   - If it matches, record attendance with timestamp.
   - If not, display a "Face not recognized" error and exit.
6. **Check Time Window**:
   - If outside the allowed time, notify the student to visit the hostel office.
7. **End**:
   - Return to Login Screen or Close Application.

## 🛠️ Technologies Used
- 📱 **Mobile Application Development**: Android (XML for UI, Java for logic)
- 🤖 **Facial Recognition**: OpenCV, TensorFlow/Keras (CNN-based model)
- 🗄️ **Database**: Firebase
- 🖥️ **Backend**: Firebase Functions (Java)
- 🎨 **Frontend**: XML for UI

## 🚀 Installation Guide
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/smart-attendance-system.git
   ```
2. **Install Dependencies**:
   ```bash
   cd smart-attendance-system
   ```
3. **Run the Application**:
   ```bash
   Open Android Studio and run the project on an emulator or physical device.
   ```

## 🔮 Future Enhancements
- 🔗 Integration with university ERP systems for seamless data management.
- 🛡️ AI-based anomaly detection to prevent fraudulent attendance.
- 🔑 Multi-factor authentication for additional security.

## 👩‍💻 Contributors
- **Beemer Sowmya** (21MIS1122)
- **Ainamilli Mounika** (21MIS1008)

## 👨‍🏫 Supervisor
- **Umamaheswari E** (Professor, Department of Scope, VIT Chennai)

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact
For any queries or feedback, feel free to reach out at:
📧 Email: your-email@domain.com

## ⭐ Support the Project
If you found this project useful, don't forget to star ⭐ the repository and contribute!

Happy Coding! 🚀

