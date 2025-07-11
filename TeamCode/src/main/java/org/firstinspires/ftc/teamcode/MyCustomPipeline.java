package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class MyCustomPipeline extends OpenCvPipeline {

    // Create matrices to hold image processing data
    private Mat hsv = new Mat();
    private Mat mask = new Mat();
    private Mat output = new Mat();

    // Color range: RED (you can change these)
    private final Scalar lowerBound = new Scalar(0, 100, 100);  // HSV lower
    private final Scalar upperBound = new Scalar(10, 255, 255); // HSV upper

    private volatile int detectedArea = 0;

    @Override
    public Mat processFrame(Mat input) {
        // Convert RGB input to HSV color space
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Create a mask where color is within the specified range
        Core.inRange(hsv, lowerBound, upperBound, mask);

        // Optional: Apply blur to reduce noise
        Imgproc.GaussianBlur(mask, mask, new Size(5, 5), 0);

        // Find contours or measure area
        detectedArea = Core.countNonZero(mask);

        // Convert mask to 3-channel image to draw on (for visual feedback)
        Imgproc.cvtColor(mask, output, Imgproc.COLOR_GRAY2RGB);

        // Draw a rectangle on the screen if detection passes threshold
        if (detectedArea > 500) {
            Imgproc.rectangle(output, new Point(20, 20), new Point(100, 100),
                    new Scalar(0, 255, 0), 2);
        }

        return input; // This is the frame you see on the Driver Station
    }

    public int getDetectedArea() {
        return detectedArea;
    }
}
