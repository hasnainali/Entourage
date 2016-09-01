package duxeye.com.entourage.sevices;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Ondoor (Hasnain) on 8/26/2016.
 */

public class ImageUploadIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ImageUploadIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        // Not a required implementation but you might want to setup any dependencies
        // here that can be reused with each intent that the service is about to
        // receive.

        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        // Process your intent, this presumably will include data such as the local
        // path of the image that you want to upload.
//        try {
//            uploadImageToServer(intent.getExtra("image_to_upload"), params);
//        } catch (Exception e) {
//            // Oh :( Consider updating any internal state here so we know the state
//            // of play for later
//        }

    }

//    public JSONObject uploadImageToServer(String imagePath, HashMap<String, String> params) throws Exception {
//        // All of your upload code
//    }
}
