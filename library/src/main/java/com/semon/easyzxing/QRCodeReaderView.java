package com.semon.easyzxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.viniciusdsl.library.CameraView;
import com.viniciusdsl.library.listener.CameraListener;

/**
 * Created by Semon_Huang on 2014/9/9.
 */
public class QRCodeReaderView extends CameraView implements CameraListener{

    private static final String TAG = QRCodeReaderView.class.getName();

    public interface OnQRCodeReadListener {

        public void OnQRCodeRead(String text);

        public void cameraNotFound();

        public void QRCodeNotFoundOnCamImage();
    }

    private QRCodeReader qrCodeReader;

    private OnQRCodeReadListener listener;

    private int width = -1,height = -1;


    public QRCodeReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QRCodeReaderView(Context context) {
        super(context);
        init();
    }

    public QRCodeReaderView(Context context, CameraListener pCameraListener) {
        super(context, pCameraListener);
        init();
    }

    private void init(){
        qrCodeReader = new QRCodeReader();
        setCameraListener(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d(TAG,"onPreviewFrame");

        if (width == -1 || height ==-1){
            width = camera.getParameters().getPreviewSize().width;
            height = camera.getParameters().getPreviewSize().height;
        }

        PlanarYUVLuminanceSource source = buildLuminanceSource(
                data, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);



        HybridBinarizer hybBin = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(hybBin);

        try {
            Result result = qrCodeReader.decode(bitmap);
            Log.d(TAG,"OnQRCodeRead:"+result.getText());
            // Notify We're found a QRCode
            if (listener != null) {
                listener.OnQRCodeRead(result.getText());
            }

        } catch (ChecksumException e) {
            Log.d(TAG, "ChecksumException");
            e.printStackTrace();
        } catch (NotFoundException e) {
            // Notify QR not found
            Log.d(TAG, "QRCodeNotFoundOnCamImage");
            if (listener != null) {
                listener.QRCodeNotFoundOnCamImage();
            }
        } catch (FormatException e) {
            //
        } finally {
            qrCodeReader.reset();
        }
    }

    @Override
    public void onCameraPictureTaken(String pPath, Bitmap pBitmap) {

    }

    @Override
    public void onCameraPictureFailed(Exception pException) {

    }

    @Override
    public void onCameraOpenFailed(Exception pException) {
        if (listener!=null){
            listener.cameraNotFound();
        }
    }

    public void setListener(OnQRCodeReadListener listener) {
        this.listener = listener;
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on the format
     * of the preview buffers, as described by Camera.Parameters.
     *
     * @param data A preview frame.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {

        return new PlanarYUVLuminanceSource(data, width, height,0,0,width,height, false); // Search QR in all image along, not only in Framing Rect as original code done

    }
}
