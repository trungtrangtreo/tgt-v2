package ca.TransCanadaTrail.TheGreatTrail;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ca.TransCanadaTrail.TheGreatTrail.MapView.TrailSegmentLight;
import ca.TransCanadaTrail.TheGreatTrail.MapView.TrailSegmentLightLight;
import ca.TransCanadaTrail.TheGreatTrail.database.ActivityDBHelperTrail;
import ca.TransCanadaTrail.TheGreatTrail.howtouse.HowToUseActivity;
import ca.TransCanadaTrail.TheGreatTrail.utils.JsonParserExecutor;
import ca.TransCanadaTrail.TheGreatTrail.utils.Logger;
import ca.TransCanadaTrail.TheGreatTrail.utils.SharedPrefUtils;

public class SplashScreenActivity extends Activity {

    //  private String download_file_path ="https://api.tctrail.ca";
    private String download_file_path = "https://firebasestorage.googleapis.com/v0/b/tgt-trail.appspot.com/o/trailDb.sqlite?alt=media&token=7616a69d-2400-46e3-af21-deebe104415d";
    private boolean updateAvailable = false;

    private String workPath = "/data/data/ca.TransCanadaTrail.TheGreatTrail/databases/";
    private String dbName = "trailDb.sqlite";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressDialog pd = new ProgressDialog(SplashScreenActivity.this, R.style.MyTheme);
        pd.setIndeterminate(false);
        pd.show();

        DoCalculations doCalculations = new DoCalculations(SplashScreenActivity.this);
        doCalculations.execute();

//      getTrailWarning();
//      checkDownloadAmenitiesDb();

//      checkPermissionReadWriteExternal();


//        achievementsParserAsyncTask = new AchievementsParserAsyncTask(this, this);
//        ArrayList<Achievement> achievements = AchievementsDao.getInstance().findAll(this);
//        if (achievements.size() == 0) {
//            achievementsParserAsyncTask.execute();
//        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void getTrailWarning() {

        if (!isNetworkAvailable()) {
            // close this activity
            return;
        }

        String LangageStringtoAppend = "";
        String urlTrailWarning = Constants.TRAIL_WARNING_SERVICE_URL;
        String messageKey = "Message";
        String locationKey = "Location";

        if (Locale.getDefault().getLanguage().equals("fr")) {
            LangageStringtoAppend = "_FR";
            messageKey = "Message" + LangageStringtoAppend;
            locationKey = "Location" + LangageStringtoAppend;
            urlTrailWarning = urlTrailWarning.replace("Location", locationKey);
            urlTrailWarning = urlTrailWarning.replace("Message", messageKey);
        }

        RequestQueue queue = Volley.newRequestQueue(this);  // this = context
        // final String checksum="";

        final String finalMessageKey = messageKey;
        final String finalLocationKey = locationKey;
        StringRequest postRequest = new StringRequest(Request.Method.GET, urlTrailWarning,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        MainActivity.trailWarnings = new ArrayList<TrailWarning>();

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String features = jsonResponse.getString("features");
                            Log.i("LocationService", features.toString());
                            JSONArray jsonArray = new JSONArray(features);
                            Log.i("LocationService", jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject warning = new JSONObject(jsonArray.get(i).toString());

                                JSONObject attributes = new JSONObject(warning.getString("attributes"));
                                String message = attributes.getString(finalMessageKey);
                                String location = attributes.getString(finalLocationKey);

                                JSONObject geometry = new JSONObject(warning.getString("geometry"));
                                String x = geometry.getString("x");
                                String y = geometry.getString("y");

                                LatLng coordinate = new LatLng(Double.parseDouble(y), Double.parseDouble(x));

                                MainActivity.trailWarnings.add(new TrailWarning(message, location, coordinate));

                                Log.i("LocationService", geometry.toString());


                            }
                            Log.i("LocationService", MainActivity.trailWarnings.size() + "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO handle the error
                        error.printStackTrace();
                    }
                }
        ) {

        };
        queue.add(postRequest);
    }

    private boolean checkUpdate() {

        String url = download_file_path;
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            updateAvailable = jsonResponse.getBoolean("updateAvailable");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        // TODO handle the error
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apiKey", "0ee743d4c3907b30d9bc61670a7e62e11215ce1d");
                params.put("requestType", "resources.trailDb");
                params.put("iHave", mycheck(workPath + "trailDb.sqlite"));
                params.put("action", "updateCheck");
                params.put("appVersion", "1.0.0");
                params.put("apiVersion", "1.0");
                params.put("appPlatform", "Android");
                return params;
            }
        };
        queue.add(postRequest);
        return updateAvailable;
    }

    public interface VolleyCallback {
        void onSuccess(String result);

    }


    private void fileChecksumServer(final String file, final String resource, final String localChecksum, final VolleyCallback callback) {

        // si le fichier n existe pas ne pas  continuer
        String url = download_file_path;
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context
        // final String checksum="";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String checksum = jsonResponse.getString("checksum");
                            String result = response;
                            callback.onSuccess(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO handle the error
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apiKey", "0ee743d4c3907b30d9bc61670a7e62e11215ce1d");
                params.put("requestType", resource);
                params.put("iHave", localChecksum);
                params.put("action", "checksum");
                params.put("appVersion", "1.0.0");
                params.put("apiVersion", "1.0");
                params.put("appPlatform", "Android");
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void retrieveTrailFromDB() {
        if (MainActivity.listPoints != null && !MainActivity.listPoints.isEmpty())
            return;

        MainActivity.listSegments = new ArrayList<TrailSegmentLight>();
        MainActivity.listPoints = new HashMap<Integer, ArrayList<LatLng>>();

        MainActivity.listSegmentsByTrailId = new ArrayList<TrailSegmentLightLight>();
        MainActivity.listPointsByTrailId = new HashMap<String, ArrayList<LatLng>>();

        long time1 = Calendar.getInstance().getTimeInMillis();
        JsonParserExecutor jsonParserExecutor = JsonParserExecutor.getInstance(MainActivity.listPoints);
//        retrieveSegments(jsonParserExecutor);
//        retrieveSegmentsV2(jsonParserExecutor);
//        retrieveSegmentsV3(jsonParserExecutor);
        retrieveSegmentsV4(jsonParserExecutor, 0, 0);
        jsonParserExecutor.shutdown();
        jsonParserExecutor.awaitsTermination();
        long time2 = Calendar.getInstance().getTimeInMillis();

        Logger.e("time to parse data = " + (time2 - time1) / 1000 + "s");
    }

    private void retrieveSegmentsV4(JsonParserExecutor jsonParserExecutor, int endObjectId, int startObjectId) {
        ActivityDBHelperTrail db = ActivityDBHelperTrail.getInstance(this);
        Cursor cursor = db.getStartSegmentsLight(startObjectId);

        if (cursor == null || !cursor.moveToFirst()) {
            db.close();
            Logger.e("retrieveSegments cursor = null");
            return;
        }

        if (endObjectId == 0) {
            endObjectId = cursor.getCount();
        }

        String segmentPointsJsonString;
        TrailSegmentLight segment;
        int objectId = startObjectId;
        int testCount = 0;

        do {
            objectId++;
            segmentPointsJsonString = db.findSegmentPointsJsonString(cursor);
            if (segmentPointsJsonString == null) {
                Logger.e("failedList objectId = " + objectId);
                break;
            }
            segment = TrailSegmentLight.mapFromDatabase(cursor);

            if (segment.trailId.equals("0084")) {
                testCount++;
            }

            MainActivity.listSegments.add(segment);
            jsonParserExecutor.executeTaskWithId(segment.objectId, segmentPointsJsonString);
        } while (cursor.moveToNext());

        Logger.e("retrieveSegments seg = " + MainActivity.listSegments.size() + "   testCount = " + testCount);

        if (objectId < endObjectId) {
            retrieveSegmentsV4(jsonParserExecutor, endObjectId, objectId);
        }
        cursor.close();
        db.close();
    }

    /**
     * The Great Trail is made of small trails, each has unique name and id.
     * Some have similar colors others don't.
     * This method merges all segments belonging to the same trail in 2 steps.
     * step 1: group all the segments belonging to the same trail, by making use of trailId.
     * step 2: merge all the segments belonging to one trail.
     */
    private void mergeTrailSegments() {
        HashMap<String, ArrayList<TrailSegmentLight>> trailIdToSegments = new HashMap<>();

        for (TrailSegmentLight segment : MainActivity.listSegments) {
            ArrayList<TrailSegmentLight> segments = trailIdToSegments.get(segment.trailId);
            if (segments == null) segments = new ArrayList<>();
            segments.add(segment);
            trailIdToSegments.put(segment.trailId, segments);
        }

        for (ArrayList<TrailSegmentLight> segments : trailIdToSegments.values())
            combineSegmentsList(segments);
    }

    /**
     * let the segments looks like :
     * P1.....P2 ,  P3.....P4 , P5.....P6
     * This method combines the segments who have common boundary points.
     * P1.....P2 + P2.....P3 = P1..........P3
     * Segments which have common non-boundary points aren't merged because if they merged the drawn
     * Polyline will be defected.
     * P1..P2..P3 + P2.....P4 = P1..P2..P3 + P2.....P4
     *
     * @param segments
     */
    private void combineSegmentsList(ArrayList<TrailSegmentLight> segments) {
        TrailSegmentLight segment_j;

        for (TrailSegmentLight segment_i : segments) {
            if (segment_i == null) continue;

            for (int j = 0; j < segments.size(); j++) {
                segment_j = segments.get(j);

                if (segment_j != null && segment_i != segment_j && canBeMerged(segment_i, segment_j)) {
                    MainActivity.listPoints.get(segment_i.objectId).addAll(MainActivity.listPoints.remove(segment_j.objectId));
                    MainActivity.listSegments.remove(segment_j);
                    segments.set(j, null);
                    j = -1;
                }
            }
        }
    }

    /**
     * return true if the 2 segments have common boundary points as explain by drawing, otherwise returns false.
     * P1.....P2, P2.....P3    returns true
     * if the case is like the following,
     * P1.....P2, P3.....P2
     * the method reverses the second segment and returns true.
     *
     * @param s1
     * @param s2
     */
    public boolean canBeMerged(TrailSegmentLight s1, TrailSegmentLight s2) {
        ArrayList<LatLng> points1 = MainActivity.listPoints.get(s1.objectId);
        ArrayList<LatLng> points2 = MainActivity.listPoints.get(s2.objectId);
        if (points1 == null || points2 == null) return false;
        if (points1.isEmpty() || points2.isEmpty()) return false;

        LatLng s1LastPoint = points1.get(points1.size() - 1);
        LatLng s2FirstPoint = points2.get(0);
        LatLng s2LastPoint = points2.get(points2.size() - 1);

        if (s1LastPoint.equals(s2FirstPoint)) {
            return true;
        } else if (s1LastPoint.equals(s2LastPoint)) {
            Collections.reverse(points2);
            return true;
        }

        return false;
    }

    public String mycheck(String filePath) {
        // Test if file exist or not
        File file = new File(filePath);
        if (!file.exists())
            return "";

        // File currentJavaJarFile = new File(Checksum.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        //String filepath = currentJavaJarFile.getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");// MD5
            FileInputStream fis = new FileInputStream(filePath);
            byte[] dataBytes = new byte[1024];
            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1)
                md.update(dataBytes, 0, nread);

            byte[] mdbytes = md.digest();

            for (int i = 0; i < mdbytes.length; i++)
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Checksum: " + sb);
        return sb + "";
    }

    private class DoCalculations extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DoCalculations(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... param) {
            retrieveTrailFromDB();
            mergeTrailSegments();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            // mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        @Override
        protected void onPostExecute(String result) {

            mWakeLock.release();
            // mProgressDialog.dismiss();

            if (SharedPrefUtils.isClickGetStarted(getApplicationContext())) {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), HowToUseActivity.class);
                startActivity(i);
            }
            // close this activity
            finish();
        }
    }

    private void checkPermissionReadWriteExternal() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                          checkDownloadTrailDb();
                            DoCalculations doCalculations = new DoCalculations(SplashScreenActivity.this);
                            doCalculations.execute();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

}