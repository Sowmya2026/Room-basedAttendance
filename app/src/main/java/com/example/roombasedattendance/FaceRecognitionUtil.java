package com.example.roombasedattendance;

import android.graphics.Bitmap;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class FaceRecognitionUtil {

    private FaceDetector faceDetector;

    public FaceRecognitionUtil() {
        // Configure face detection options
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)  // Prioritize accuracy over speed
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)             // Landmark mode off for efficiency
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE) // No classification needed
                .build();

        // Initialize the face detector
        faceDetector = FaceDetection.getClient(options);
    }

    /**
     * Converts a Bitmap to an InputImage for ML Kit's Face Detection API
     */
    public InputImage convertBitmapToInputImage(Bitmap bitmap) {
        return InputImage.fromBitmap(bitmap, 0); // No rotation applied
    }

    /**
     * Detects faces in a given Bitmap image.
     * @param imageBitmap Bitmap image to detect faces in
     * @param callback    Callback to handle detection results asynchronously
     */
    public void detectFaces(Bitmap imageBitmap, FaceDetectionCallback callback) {
        InputImage image = convertBitmapToInputImage(imageBitmap);

        faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        // Face(s) detected
                        callback.onFaceDetected(faces);
                    } else {
                        // No face detected
                        callback.onNoFaceDetected();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error in face detection
                    callback.onDetectionFailed(e);
                });
    }

    /**
     * Compares two lists of detected faces to determine if they match.
     * This is a basic comparison based on face bounding box size as a similarity measure.
     * @param registeredFaces List of faces detected in the registered image
     * @param attendanceFaces List of faces detected in the attendance image
     * @return true if a match is found, false otherwise
     */
    public boolean compareFaces(List<Face> registeredFaces, List<Face> attendanceFaces) {
        if (registeredFaces.isEmpty() || attendanceFaces.isEmpty()) {
            return false;
        }

        // Simplified: comparing only the first face in each list
        Face registeredFace = registeredFaces.get(0);
        Face attendanceFace = attendanceFaces.get(0);

        // Calculate bounding box areas for a basic comparison
        float registeredArea = registeredFace.getBoundingBox().width() * registeredFace.getBoundingBox().height();
        float attendanceArea = attendanceFace.getBoundingBox().width() * attendanceFace.getBoundingBox().height();

        // Simple threshold comparison based on bounding box size difference
        return Math.abs(registeredArea - attendanceArea) < 5000; // Adjust threshold as needed
    }
}

