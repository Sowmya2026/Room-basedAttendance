package com.example.roombasedattendance;

import com.google.mlkit.vision.face.Face;
import java.util.List;

public interface FaceDetectionCallback {
    void onFaceDetected(List<Face> faces);     // Called when faces are successfully detected
    void onNoFaceDetected();                   // Called when no faces are detected
    void onDetectionFailed(Exception e);       // Called when face detection fails
}
