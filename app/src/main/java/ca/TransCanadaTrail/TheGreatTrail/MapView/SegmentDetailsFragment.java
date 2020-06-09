package ca.TransCanadaTrail.TheGreatTrail.MapView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import ca.TransCanadaTrail.TheGreatTrail.AppController;
import ca.TransCanadaTrail.TheGreatTrail.Constants;
import ca.TransCanadaTrail.TheGreatTrail.DirectionTrailFragment;
import ca.TransCanadaTrail.TheGreatTrail.MainActivity;
import ca.TransCanadaTrail.TheGreatTrail.R;
import ca.TransCanadaTrail.TheGreatTrail.database.ActivityDBHelperTrail;
import ca.TransCanadaTrail.TheGreatTrail.utils.Utility;
import static ca.TransCanadaTrail.TheGreatTrail.ActivityTracker.ActivityTrackerFragment.trackerfragStack;
import static ca.TransCanadaTrail.TheGreatTrail.ActivityTracker.ActivityTrackerFragment.trackerfragTagStack;
import static ca.TransCanadaTrail.TheGreatTrail.MainActivity.currentTab;
import static ca.TransCanadaTrail.TheGreatTrail.MapView.MapFragment.mapfragStack;
import static ca.TransCanadaTrail.TheGreatTrail.MapView.MapFragment.mapfragTagStack;
import static ca.TransCanadaTrail.TheGreatTrail.MeasureTool.MeasureFragment.measurefragStack;
import static ca.TransCanadaTrail.TheGreatTrail.MeasureTool.MeasureFragment.measurefragTagStack;

public class SegmentDetailsFragment extends Fragment {
    private String TAG = "LocationService";
    private String trailId = "";
    private int objectId;
    private int x;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static SegmentDetailsFragment instance = null;
    public static List<Bitmap> images = new ArrayList<Bitmap>();

    String trailName = "";
    MainActivity activity;
    Tracker mTracker;

    @BindView(R.id.firstImage) ImageView firstImage;
    @BindView(R.id.imageGallery) LinearLayout imageGallery;
    @BindView(R.id.carouselHSV) HorizontalScrollView carouselHSV;
    @BindView(R.id.directionBtn) Button directionBtn;
    @BindView(R.id.descriptionTxt) TextView descriptionTxt;
    @BindView(R.id.trailTypeTxt) TextView trailTypeTxt;
    @BindView(R.id.activitiesTxt) TextView activitiesTxt;
    @BindView(R.id.environmentTxt) TextView environmentTxt;
    @BindView(R.id.resourcesTxt1) TextView resourcesTxt1;
    @BindView(R.id.resourcesTxt2) TextView resourcesTxt2;
    @BindView(R.id.resourcesTxt3) TextView resourcesTxt3;
    @BindView(R.id.resourcesTxt4) TextView resourcesTxt4;
    @BindView(R.id.resourcesTitle) TextView resourcesTitle;
    @BindView(R.id.segmentNameTxt) TextView segmentNameTxt;
    @BindView(R.id.resourcesSeparator) View resourcesSeparator;

    public SegmentDetailsFragment() {
        // Required empty public constructor
    }

    public static SegmentDetailsFragment getInstance() {
        if (instance == null) {
            Log.i("Instance AT", "New Creation");
            instance = new SegmentDetailsFragment();
        }
        Log.i("Instance AT", "No Creation");
        return instance;
    }


    public static SegmentDetailsFragment newInstance() {
        SegmentDetailsFragment segmentDetailsFragment = getInstance();

        return segmentDetailsFragment;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        AppController application = (AppController) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_segment_details, container, false);
        ButterKnife.bind(this, view);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        trailName = getArguments().getString("trail_name");
        activity.getSupportActionBar().setTitle(Html.fromHtml("<small>" + trailName + "</small>"));

        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = x / (dpToPx(150) + 30);
                Log.i(TAG, " Le click  num = " + num);

                if (num < images.size()) {
                    Intent myIntent = new Intent(activity, FullImageActivity.class);
                    myIntent.putExtra("num", num);
                    activity.startActivity(myIntent);
                }

            }
        });

        imageGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = (int) event.getX();
                return false;
            }
        });

        Button uploadBtn = (Button) view.findViewById(R.id.uploadBtn);
        carouselHSV.setVisibility(View.GONE);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] permissos = {"android.permission.CAMERA"};

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity,
                            permissos,
                            MY_PERMISSIONS_REQUEST_CAMERA
                    );

                } else {
                    Intent chooseImageIntent = ImagePicker.getPickImageIntent(activity);
                    startActivityForResult(chooseImageIntent, 1234);

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);
                    }
                }
            }
        });
        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.no_internet)
                            .setMessage(R.string.must_online_trail)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }

                pushSegmentDetailFragment();
                DirectionTrailFragment directionTrailFragment = DirectionTrailFragment.newInstance(trailId);
                replaceFragment(directionTrailFragment);
            }
        });


        ActivityDBHelperTrail db = ActivityDBHelperTrail.getInstance(activity);
        Cursor cursor = db.getSpecificSegments(objectId);

        if (cursor != null && cursor.moveToFirst()) {
            TrailSegment segment = new TrailSegment(cursor);
            trailId = segment.trailId;
            searchAndDownloadFlickrFeaturedPhoto("TrailCode_" + segment.trailId + "_featured", false, "TrailCode_" + segment.provinceId + "_featured", true);

            searchAndDownloadTrailFlickrPhotos("TrailCode_" + segment.trailId, false);
            segmentNameTxt.setText(segment.trailName + "\n" + segment.sumLengthKm + " km");

            if (Locale.getDefault().getLanguage().equals("fr")) {
                if (!segment.description.equals("")) {
                    descriptionTxt.setVisibility(View.VISIBLE);
                    descriptionTxt.setText(segment.description_fr);
                }
                segmentNameTxt.setText(segment.trailName + "\n" + segment.sumLengthKm + " km");
                trailTypeTxt.setText(segment.trailType_fr);
                activitiesTxt.setText(segment.activities_fr);
                environmentTxt.setText(segment.environment_fr);
            } else {
                if (!segment.description.equals("")) {
                    descriptionTxt.setVisibility(View.VISIBLE);
                    descriptionTxt.setText(segment.description);
                }
                segmentNameTxt.setText(segment.trailName + "\n" + segment.sumLengthKm + " km");
                trailTypeTxt.setText(segment.trailType);
                activitiesTxt.setText(segment.activities);
                environmentTxt.setText(segment.environment);
            }


            if (!segment.groupName1.equals("") && !segment.websiteUrl1.equals("")) {
                Spanned link1, link2, link3, link4;
                resourcesTitle.setVisibility(View.VISIBLE);
                resourcesSeparator.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= 24) {
                    link1 = Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl1 + "\">" + segment.groupName1 + "</a>", Html.FROM_HTML_MODE_LEGACY); // for 24 api and more

                } else {
                    link1 = (Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl1 + "\">" + segment.groupName1 + "</a>")); // or for older api
                }

                resourcesTxt1.setMovementMethod(LinkMovementMethod.getInstance());
                resourcesTxt1.setText(link1);

                if (!segment.groupName2.equals("") && !segment.websiteUrl2.equals("")) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        link2 = Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl2 + "\">" + segment.groupName2 + "</a>", Html.FROM_HTML_MODE_LEGACY); // for 24 api and more

                    } else {
                        link2 = (Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl2 + "\">" + segment.groupName2 + "</a>")); // or for older api
                    }

                    resourcesTxt2.setMovementMethod(LinkMovementMethod.getInstance());
                    resourcesTxt2.setText(link2);
                }

                if (!segment.groupName3.equals("") && !segment.websiteUrl3.equals("")) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        link3 = Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl3 + "\">" + segment.groupName3 + "</a>", Html.FROM_HTML_MODE_LEGACY); // for 24 api and more

                    } else {
                        link3 = (Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl3 + "\">" + segment.groupName3 + "</a>")); // or for older api
                    }

                    resourcesTxt3.setMovementMethod(LinkMovementMethod.getInstance());
                    resourcesTxt3.setText(link3);
                }

                if (!segment.groupName4.equals("") && !segment.websiteUrl4.equals("")) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        link4 = Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl4 + "\">" + segment.groupName4 + "</a>", Html.FROM_HTML_MODE_LEGACY); // for 24 api and more

                    } else {
                        link4 = (Html.fromHtml("<a style=\"text-decoration: none\" href=\"" + segment.websiteUrl4 + "\">" + segment.groupName4 + "</a>")); // or for older api
                    }

                    resourcesTxt4.setMovementMethod(LinkMovementMethod.getInstance());
                    resourcesTxt4.setText(link4);
                }

            } else {
                resourcesTitle.setVisibility(View.GONE);
                resourcesSeparator.setVisibility(View.GONE);
            }


        }
        cursor.close();

        return view;
    }

    private void openWebView(String linkUrl) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(linkUrl));
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName("Trail Information View");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void replaceFragment(Fragment replaceFragment) {
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        switch (currentTab) {
            case "MapFragment":
                mFragmentTransaction
                        .replace(R.id.searchLayout, replaceFragment)
                        .commit();
                break;
            case "MeasureFragment":
                mFragmentTransaction
                        .replace(R.id.measureSearchLayout, replaceFragment)
                        .commit();

                break;
            case "ActivityTrackerFragment":
                mFragmentTransaction
                        .replace(R.id.trackerSearchLayout, replaceFragment)
                        .commit();

                break;
        }


    }

    private void pushSegmentDetailFragment() {
        String actlogtag = "SegmentDetailsFragment";
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fsegment = null;

        switch (currentTab) {
            case "MapFragment":
                actlogtag = "Map" + actlogtag;
                fsegment = fragmentManager.findFragmentById(R.id.searchLayout);

                if (fsegment instanceof SegmentDetailsFragment) {
                    mapfragStack.put(actlogtag, fsegment);
                    mapfragTagStack.push(actlogtag);
                }
                break;
            case "MeasureFragment":
                actlogtag = "Measure" + actlogtag;
                fsegment = fragmentManager.findFragmentById(R.id.measureSearchLayout);
                if (fsegment instanceof SegmentDetailsFragment) {
                    measurefragStack.put(actlogtag, fsegment);
                    measurefragTagStack.push(actlogtag);
                }
                break;
            case "ActivityTrackerFragment":
                actlogtag = "Tracker" + actlogtag;
                fsegment = fragmentManager.findFragmentById(R.id.trackerSearchLayout);
                if (fsegment instanceof SegmentDetailsFragment) {
                    trackerfragStack.put(actlogtag, fsegment);
                    trackerfragTagStack.push(actlogtag);
                }
                break;
        }


    }

    private boolean searchAndDownloadFlickrFeaturedPhoto(String tag, final boolean isThumbnail, final String tag2, final boolean isSegment) {
        final boolean[] result = {false};
        String url = Constants.URL_SEARCH_FLIKR + tag;
        RequestQueue queue = Volley.newRequestQueue(activity);  // this = context


        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("LocationService", "Response   ----------------------------------------------------------------------------------------------   " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject photos = jsonResponse.getJSONObject("photos");
                            int totalphotos = photos.getInt("total");
                            if (totalphotos > 0) {
                                JSONArray photoArray = new JSONArray(photos.getString("photo"));

                                JSONObject pic = new JSONObject(photoArray.get(0).toString());
                                String server = pic.getString("server");
                                String id = pic.getString("id");
                                String secret = pic.getString("secret");
                                int farm = pic.getInt("farm");
                                Log.i("LocationService", "Photo----------------------------------------------------------------------------------------------   " + id + "  " + server + "   " + secret + "   " + farm);

                                ImageLoader imageLoader = AppController.getInstance().getImageLoader();


                                String url = "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret;

                                if (isThumbnail) {
                                    url += "_q.jpg";
                                } else {
                                    url += "_b.jpg";
                                }

                                Log.i("LocationService", "url   ----------------------------------------------------------------------------------------------   " + url);


                                //  String url = "https://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_b"+".jpg" ;
                                // If you are using normal ImageView
                                imageLoader.get(url, new ImageLoader.ImageListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, "Image Load Error: " + error.getMessage());
                                    }

                                    @Override
                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {

                                        if (response == null) {
                                            pDialog.dismiss();
                                            if (isSegment) {
                                                searchAndDownloadFlickrFeaturedPhoto(tag2, false, "", false);
                                            }
                                            return;
                                        }
                                        Bitmap bitmap = response.getBitmap();
                                        if (bitmap != null) {
                                            Bitmap croppedBmp;

                                            if (firstImage.getWidth() > 0 && firstImage.getHeight() > 0) {
                                                croppedBmp = ThumbnailUtils.extractThumbnail(bitmap, firstImage.getWidth(), firstImage.getHeight());
                                            } else {
                                                croppedBmp = ThumbnailUtils.extractThumbnail(bitmap, carouselHSV.getWidth(), dpToPx(200));
                                            }

                                            firstImage.setImageBitmap(croppedBmp);
                                            pDialog.dismiss();
                                            result[0] = true;
                                            Log.i("LocationService", "True   ----------------------------------------------------------------------------------------------   " + result[0]);

                                        } else {
                                            pDialog.dismiss();

                                        }
                                    }
                                });

                            } else {
                                pDialog.dismiss();
                                result[0] = false;
                                if (isSegment) {
                                    searchAndDownloadFlickrFeaturedPhoto(tag2, false, "", false);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO handle the error
                        Utility.displayToast(activity, "Photos not available on Server, Try in while", Toast.LENGTH_LONG);
                        pDialog.dismiss();
                    }
                }
        );
        queue.add(postRequest);
        Log.i("LocationService", "Displayed result  ----------------------------------------------------------------------------------------------   " + result[0]);

        return result[0];

    }

    private void searchAndDownloadTrailFlickrPhotos(String tag, final boolean isThumbnail) {

        String url = Constants.URL_SEARCH_FLIKR + tag;
        RequestQueue queue = Volley.newRequestQueue(activity);  // this = context

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("LocationService", "Response   ----------------------------------------------------------------------------------------------   " + response);
                        images = new ArrayList<Bitmap>();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject photos = jsonResponse.getJSONObject("photos");
                            int totalphotos = photos.getInt("total");
                            if (totalphotos > 0) {
                                JSONArray photoArray = new JSONArray(photos.getString("photo"));

                                for (int i = 0; i < totalphotos; i++) {
                                    JSONObject pic = new JSONObject(photoArray.get(i).toString());
                                    String server = pic.getString("server");
                                    String id = pic.getString("id");
                                    String secret = pic.getString("secret");
                                    int farm = pic.getInt("farm");
                                    Log.i("LocationService", "Photo   ----------------------------------------------------------------------------------------------   " + id + "  " + server + "   " + secret + "   " + farm);

                                    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


                                    //"https://farm\(farm).staticflickr.com/\(serverId)/\(id)_\(secret)"   _q =150x150 , _b = 1024 on longest side

                                    String url = "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret;

                                    if (isThumbnail) {
                                        url += "_q.jpg";  //  150 x 150
                                    } else {
                                        url += "_b.jpg"; // 1024
                                    }
                                    imageLoader.get(url, new ImageLoader.ImageListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Image Load Error: " + error.getMessage());
                                        }

                                        @Override
                                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {

                                            if (response == null) {
                                                return;
                                            }
                                            Bitmap bitmap = response.getBitmap();
                                            if (bitmap != null) {
                                                if (carouselHSV.getVisibility() == View.GONE) {
                                                    carouselHSV.setVisibility(View.VISIBLE);
                                                }

                                                imageGallery.addView(getImageView(bitmap)); // add image to view
                                                images.add(bitmap); //  Add image to a list
                                            }
                                        }
                                    });

                                }

                            } else {


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // TODO handle the error
                        error.printStackTrace();
                    }
                }
        );
        queue.add(postRequest);


    }

    private View getImageView(Bitmap image) {
        image = scaleBitmap(image, dpToPx(150), dpToPx(150));
        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(150));
        lp.setMargins(15, 10, 15, 10); // left,top,right,bottom
        imageView.setLayoutParams(lp);
        imageView.setImageBitmap(image);
        return imageView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234) {

            Bitmap mphoto = ImagePicker.getImageFromResult(activity, resultCode, data);

            if (mphoto != null) {
                pushSegmentDetailFragment();
                UploadFlickrFragment uploadFlickrFragment = new UploadFlickrFragment();

                uploadFlickrFragment.photo = mphoto;
                uploadFlickrFragment.trailId = trailId;

                replaceFragment(uploadFlickrFragment);
            }
        }
    }

    public int dpToPx(float dp) {
        Resources resources = activity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (currentTab.equals("MapFragment") && mapfragStack.containsKey("MapSearchFragment")) {
            menu.clear();
        } else if (currentTab.equals("MeasureFragment") && measurefragStack.containsKey("MeasureSearchFragment")) {
            menu.clear();
        } else if (currentTab.equals("ActivityTrackerFragment") && trackerfragStack.containsKey("TrackerSearchFragment")) {
            menu.clear();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
