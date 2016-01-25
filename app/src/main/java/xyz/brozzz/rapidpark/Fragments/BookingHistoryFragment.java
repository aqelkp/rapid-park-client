package xyz.brozzz.rapidpark.Fragments;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import in.aqel.quickparksdk.Utils.AppConstants;
import xyz.brozzz.rapidpark.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingHistoryFragment extends Fragment {
    RecyclerView mRecyclerView;
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;
    public final static String STR = "BookingHistoryFragment";
    Firebase ref;

    public BookingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_booking_history, container, false);
        ImageView myImage = (ImageView) v.findViewById(R.id.QRimageView);

        ref = new Firebase(AppConstants.SERVER);
        // File f = QRCode.from("Fuck you aqel").to(ImageHeaderParser.ImageType.JPG).file();
        try {
            Bitmap bitmap = encodeAsBitmap(ref.getAuth().getUid());
            myImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
       // Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());



        // Inflate the layout for this fragment
        return v;

    }
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
